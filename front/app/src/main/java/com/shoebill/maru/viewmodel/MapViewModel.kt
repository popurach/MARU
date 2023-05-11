package com.shoebill.maru.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.ui.focus.FocusManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.gson.JsonObject
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraBoundsOptions
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.CoordinateBounds
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.scalebar.scalebar
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateBearing
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateOptions
import com.mapbox.maps.plugin.viewport.state.FollowPuckViewportState
import com.mapbox.maps.plugin.viewport.viewport
import com.shoebill.maru.R
import com.shoebill.maru.model.data.Coordinate
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.data.request.BoundingBox
import com.shoebill.maru.model.repository.LandmarkRepository
import com.shoebill.maru.model.repository.SpotRepository
import com.shoebill.maru.util.Filter.ALL
import com.shoebill.maru.util.Filter.LANDMARK
import com.shoebill.maru.util.Filter.MYSPOT
import com.shoebill.maru.util.Filter.SPOT
import com.shoebill.maru.util.SpotType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

const val TAG = "LANDMARK"

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MapViewModel @Inject constructor(
    private val landmarkRepository: LandmarkRepository,
    private val spotRepository: SpotRepository
) : ViewModel() {
    private lateinit var mapView: MapView
    lateinit var mapBoxMap: MapboxMap
    private lateinit var _focusManager: FocusManager

    private lateinit var pointAnnotationManager: PointAnnotationManager

    private val _isTracking = MutableLiveData(false)
    val isTracking get() = _isTracking

    private val _canSearch = MutableLiveData(false)
    val canSearch get() = _canSearch

    private var landmarkImage: Bitmap? = null
    private var spotImage: Bitmap? = null
    private var clusterImage: MutableList<Bitmap> = mutableListOf()

    private var lastRequestPos: Point = Point.fromLngLat(-74.005974, 40.712776)

    private val landmarkAnnotations = mutableListOf<PointAnnotationOptions>()
    private val spotAnnotations = mutableListOf<PointAnnotationOptions>()

    val visitingLandmark = MutableLiveData<PointAnnotationOptions?>(null)

    private val _filterState = MutableLiveData(ALL)
    val filterState get() = _filterState

    private lateinit var bottomSheetController: NavHostController

    fun initBottomSheetController(controller: NavHostController) {
        bottomSheetController = controller
    }

    private val _bottomSheetOpen = MutableLiveData(false)
    val bottomSheetOpen get() = _bottomSheetOpen

    fun updateBottomSheetState(value: Boolean) {
        _bottomSheetOpen.value = value
    }

    private val _spotList = MutableLiveData<List<Spot>>()

    val spotList: LiveData<List<Spot>> get() = _spotList

    fun updateFilterState(value: Int) {
        _filterState.value = value
    }

    fun initFocusManager(fm: FocusManager) {
        _focusManager = fm
    }

    fun initImages(context: Context) {
        landmarkImage = drawableToBitmap(
            getDrawable(
                context,
                R.drawable.landmark
            )
        )
        spotImage = drawableToBitmap(
            getDrawable(
                context,
                R.drawable.spot_marker
            )
        )
        for (i in 2..9) {
            val resId =
                R.drawable::class.java.getField("cluster_$i").getInt(null)
            clusterImage.add(
                drawableToBitmap(
                    getDrawable(
                        context,
                        resId
                    )
                )
            )
        }
        clusterImage.add(
            drawableToBitmap(
                getDrawable(
                    context,
                    R.drawable.cluster_more_9
                )
            )
        )
    }

    private fun drawableToBitmap(image: Drawable?): Bitmap = (image as BitmapDrawable).bitmap

    fun clearFocus() {
        _focusManager.clearFocus()
    }

    fun loadMarker() {
        _canSearch.value = false
        deletePin()
        _canSearch.value = false
        when (_filterState.value) {
            ALL -> {
                loadLandmarkPos()
                loadSpotPos()
            }

            LANDMARK -> loadLandmarkPos()
            SPOT -> loadSpotPos()
            MYSPOT -> loadSpotPos(mine = true)
        }
        loadAroundSpots()
    }

    fun loadSpotPos(mine: Boolean = false) {
        val projection = getProjection()
        viewModelScope.launch {
            val boundingBox = BoundingBox(
                projection.west(),
                projection.south(),
                projection.east(),
                projection.north(),
                ceil(mapBoxMap.cameraState.zoom).toInt()
            )
            try {
                val spotList = spotRepository.getSpotMarker(boundingBox, mine)
                spotList.forEach {
                    addMarker(
                        spotType = when (it.properties.geoType) {
                            "POINT" -> SpotType.SPOT
                            else -> when (it.properties.count) {
                                2 -> SpotType.CLUSTER_2
                                3 -> SpotType.CLUSTER_3
                                4 -> SpotType.CLUSTER_4
                                5 -> SpotType.CLUSTER_5
                                6 -> SpotType.CLUSTER_6
                                7 -> SpotType.CLUSTER_7
                                8 -> SpotType.CLUSTER_8
                                9 -> SpotType.CLUSTER_9
                                else -> SpotType.CLUSTER_MORE_9
                            }
                        },
                        coordinate = Coordinate(
                            it.geometry.coordinates[0],
                            it.geometry.coordinates[1]
                        ),
                        id = it.properties.id
                    )
                }
            } catch (e: Error) {
                Log.e(TAG, "fail to load spot pos: $e")
            }
        }
    }

    private fun landmarkClicked(landmarkId: Long) {
        bottomSheetController.navigate("landmark/main/$landmarkId") {
            popUpTo("spot/list")
        }
        _bottomSheetOpen.value = true
    }

    private fun spotClicked(spotId: Long) {
        bottomSheetController.navigate("spot/detail/$spotId") {
            popUpTo("spot/list")
        }
        _bottomSheetOpen.value = true
    }

    private fun clusterClicked(point: Point) {
        val curZoomLevel = mapBoxMap.cameraState.zoom
        if (curZoomLevel >= 17.0) return
        val nextZoomLevel = curZoomLevel + 1.0
        val cameraOptions = CameraOptions.Builder()
            .zoom(nextZoomLevel)
            .center(point)
            .build()
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                mapBoxMap.flyTo(cameraOptions)
            }
            loadMarker()
        }

    }

    fun createMapView(context: Context): MapView {
        mapView = MapView(context)
        mapBoxMap = mapView.getMapboxMap()
        pointAnnotationManager = mapView.annotations.createPointAnnotationManager()

        // 마커 클릭 리스너 등록
        pointAnnotationManager.addClickListener(OnPointAnnotationClickListener {
            val type: Int = it.getData()!!.asJsonObject!!.get("type")!!.asInt
            val id = it.getData()!!.asJsonObject!!.get("id")?.asLong
            when (type) {
                SpotType.LANDMARK -> if (id != null) landmarkClicked(id)
                SpotType.SPOT -> if (id != null) spotClicked(id)
                else -> clusterClicked(it.point)
            }
            true
        })

        mapView.apply {
            getMapboxMap().loadStyleUri("mapbox://styles/chartype/clgd8mwak000001sczpqlrb72") {
                scalebar.enabled = false
                compass.enabled = false

                val boundsOptions = CameraBoundsOptions.Builder()
                    .bounds(
                        CoordinateBounds(
                            Point.fromLngLat(126.75201, 37.72348),
                            Point.fromLngLat(127.19696, 37.40671),
                            false
                        )
                    )
                    .minZoom(10.0)
                    .build()
                mapBoxMap.setBounds(boundsOptions)

                cameraOptions {
                    center(Point.fromLngLat(126.979384, 37.563573))
                    zoom(19.0)
                    pitch(50.0)
                }
            }
            mapBoxMap.addOnMoveListener(object : OnMoveListener {
                override fun onMove(detector: MoveGestureDetector): Boolean {
                    return false
                }

                override fun onMoveBegin(detector: MoveGestureDetector) {
                    if (_isTracking.value == true) unTrackUser()
                    _focusManager.clearFocus()
                }

                override fun onMoveEnd(detector: MoveGestureDetector) {
                    _canSearch.value = true
                }
            })
        }
        return mapView
    }

    fun trackCameraToUser(context: Context) {
        clearFocus()
        _canSearch.value = false
        if (_isTracking.value == false) {
            _isTracking.value = true
            moveCameraLinearly()
            mapView.apply {
                location.addOnIndicatorPositionChangedListener { myPos ->
                    // 0.1Km == 100m
                    val minDist = 0.1
                    val distanceFromLastRequest = getDistance(myPos, lastRequestPos)
                    if (distanceFromLastRequest > minDist) {
                        lastRequestPos = myPos
                        loadMarker()
                    }
                    landmarkAnnotations.forEach { landmark ->
                        val jsonObject = landmark.getData()!!.asJsonObject!!
                        val landmarkPos = landmark.getPoint()
                        val distance = getDistance(myPos, landmarkPos!!)
                        // 멀어질 때
                        if (visitingLandmark.value == landmark && distance > minDist) {
                            visitingLandmark.value = null
                        }
                        // 랜드마크 범위에 들어갔을 때
                        if (visitingLandmark.value != landmark && distance <= minDist) {
                            Log.d("LANDMARK", jsonObject.toString())
                            visitingLandmark.value = landmark
                            val landmarkId =
                                jsonObject.get("id")?.asLong
                            val isVisit = jsonObject.get("isVisit")?.asBoolean!!
                            _bottomSheetOpen.value = true
                            bottomSheetController.navigate(if (isVisit) "landmark/main/$landmarkId" else "landmark/first/$landmarkId")
                        }
                    }
                }
                location.updateSettings {
                    enabled = true
                    pulsingEnabled = true
                    pulsingMaxRadius = 100f
                    locationPuck = LocationPuck2D(
                        topImage = getDrawable(
                            context,
                            com.mapbox.maps.R.drawable.mapbox_user_icon
                        ),
                        bearingImage = getDrawable(
                            context,
                            com.mapbox.maps.R.drawable.mapbox_user_bearing_icon
                        ),
                        shadowImage = getDrawable(
                            context,
                            com.mapbox.maps.R.drawable.mapbox_user_stroke_icon
                        ),
                        scaleExpression = interpolate {
                            linear()
                            zoom()
                            stop {
                                literal(0.0)
                                literal(0.6)
                            }
                            stop {
                                literal(20.0)
                                literal(1.0)
                            }
                        }.toJson(),
                    )
                }
            }
        } else {
            unTrackUser()
        }
    }

    private fun moveCameraLinearly() {
        val viewportPlugin = mapView.viewport
        val followPuckViewportState: FollowPuckViewportState =
            viewportPlugin.makeFollowPuckViewportState(
                FollowPuckViewportStateOptions.Builder()
                    .zoom(17.0)
                    .pitch(mapBoxMap.cameraState.pitch)
                    .bearing(FollowPuckViewportStateBearing.Constant(mapBoxMap.cameraState.bearing))
                    .build()
            )
        viewportPlugin.transitionTo(followPuckViewportState) {
        }
    }

    private fun unTrackUser() {
        _isTracking.value = false
        mapView.location.addOnIndicatorPositionChangedListener {}
        mapView.location.updateSettings {
            enabled = false
        }
    }

    private fun getProjection(): CoordinateBounds {
        val options = CameraOptions.Builder()
            .zoom(mapBoxMap.cameraState.zoom)
            .center(mapBoxMap.cameraState.center)
            .build()
        return mapBoxMap.coordinateBoundsForCamera(options)
    }

    fun loadLandmarkPos() {
        val projection = getProjection()
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            val deferredListOfLandmark = async {
                landmarkRepository.getLandmarkByPos(
                    projection.west(),
                    projection.south(),
                    projection.east(),
                    projection.north()
                )
            }
            // 리스트가 null이면 종료 아니면 리스트 추가
            val listOfLandmark = deferredListOfLandmark.await() ?: return@launch
            withContext(Dispatchers.Main) {
                // 현재 존재하는 모든 마커 삭제
                listOfLandmark.forEach { landmark ->
                    addMarker(SpotType.LANDMARK, landmark.coordinate, landmark.visited, landmark.id)
                }
                lastRequestPos = mapBoxMap.cameraState.center
//                _landmarks.value = listOfLandmark
            }
        }
    }

    private fun getDistance(a: Point, b: Point): Double {
        val distance: Double
        val radius = 6371.0 // 지구 반지름(km)
        val toRadian = Math.PI / 180
        val deltaLatitude = abs(a.latitude() - b.latitude()) * toRadian
        val deltaLongitude = abs(a.longitude() - b.longitude()) * toRadian
        val sinDeltaLat = sin(deltaLatitude / 2)
        val sinDeltaLng = sin(deltaLongitude / 2)
        val squareRoot = sqrt(
            sinDeltaLat * sinDeltaLat +
                    cos(b.latitude() * toRadian) * cos(a.latitude() * toRadian) * sinDeltaLng * sinDeltaLng
        )
        distance = 2 * radius * asin(squareRoot)
        return distance
    }


    private fun addMarker(
        spotType: Int,
        coordinate: Coordinate,
        isVisit: Boolean? = null,
        id: Long?
    ) {
        val iconImage = when (spotType) {
            0 -> landmarkImage
            1 -> spotImage
            else -> clusterImage[spotType - 2]
        }
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(Point.fromLngLat(coordinate.lng, coordinate.lat))
            .withIconImage(iconImage!!)
            .withIconAnchor(IconAnchor.BOTTOM)
            .withIconSize(if (spotType == SpotType.LANDMARK) 2.0 else 1.5)
            .withData(
                JsonObject().apply {
                    addProperty("type", spotType)
                    if (isVisit != null) addProperty("isVisit", isVisit)
                    if (id != null) addProperty("id", id)
                }
            )
        if (spotType == SpotType.LANDMARK) landmarkAnnotations.add(pointAnnotationOptions)
        else spotAnnotations.add(pointAnnotationOptions)
        pointAnnotationManager.create(pointAnnotationOptions)
    }

    fun deletePin() {
        landmarkAnnotations.clear()
        spotAnnotations.clear()
        pointAnnotationManager.deleteAll()
    }

    fun loadAroundSpots() {
        val projection = getProjection()
        viewModelScope.launch {
            _spotList.value = spotRepository.getAroundSpots(
                west = projection.west(),
                south = projection.south(),
                east = projection.east(),
                north = projection.north()
            )
        }
    }

    fun moveCamera(lat: Double, lng: Double) {
        val point = Point.fromLngLat(lng, lat)
        val cameraOptions = CameraOptions.Builder()
            .center(point)
            .zoom(17.0)
            .build()
        mapBoxMap.flyTo(cameraOptions)
    }
}

