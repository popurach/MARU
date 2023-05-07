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

    private var landmarkImage: Bitmap? = null
    private var spotImage: Bitmap? = null
    private var clusterImage: Bitmap? = null

    private var lastRequestPos: Point? = null
    private var lastRequestZoom: Double = 0.0

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

    private val _spotList = MutableLiveData<List<Spot>>(
        listOf(
            Spot(
                0,
                imageUrl = "https://picsum.photos/id/237/200/300",
                liked = false,
            ),
            Spot(
                1,
                imageUrl = "https://picsum.photos/id/237/200/300",
                liked = false,
            ),
            Spot(
                2,
                imageUrl = "https://picsum.photos/id/237/200/300",
                liked = false,
            ),
        )
    )

    val spotList: LiveData<List<Spot>> get() = _spotList

    fun updateFilterState(value: Int) {
        _filterState.value = value
    }

    fun initFocusManager(fm: FocusManager) {
        _focusManager = fm
    }

    fun initLandmarkImage(image: Drawable?) {
        landmarkImage = drawableToBitmap(image)
    }

    fun initSpotImage(image: Drawable?) {
        spotImage = drawableToBitmap(image)
    }

    fun initClusterImage(image: Drawable?) {
        clusterImage = drawableToBitmap(image)
    }

    private fun drawableToBitmap(image: Drawable?): Bitmap = (image as BitmapDrawable).bitmap

    fun clearFocus() {
        _focusManager.clearFocus()
    }

//    fun loadMarker() {
//        when (_filterState.value) {
//            ALL -> {
//                loadLandmarkPos()
//                loadSpotPos()
//            }
//
//            LANDMARK -> loadLandmarkPos()
//            SPOT -> loadSpotPos()
//            MYSPOT -> loadSpotPos(mine = true)
//        }
//    }

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
                    val coordinate = Coordinate(it.geometry.coordinates[0], it.geometry.coordinates[1])
                    addMarker(spotType = SpotType.SPOT, coordinate = coordinate, id = it.properties.id)
                }
            } catch (e: Error) {
                Log.e(TAG, "fail to load spot pos: $e")
            }
        }
    }

    private fun landmarkClicked(landmarkId: Long) {
        bottomSheetController.navigate("landmark/main/$landmarkId")
        _bottomSheetOpen.value = true
    }

    fun spotClicked() {
        // TODO: 바텀시트 연 후 스팟 상세 페이지로 이동
    }

    fun clusterClicked() {
        // TODO: 줌레벨 올리기
    }

    fun createMapView(context: Context): MapView {
        mapView = MapView(context)
        mapBoxMap = mapView.getMapboxMap()
        pointAnnotationManager = mapView.annotations.createPointAnnotationManager()

        // 마커 클릭 리스너 등록
        pointAnnotationManager.addClickListener(OnPointAnnotationClickListener {
            val type: Int = it.getData()!!.asJsonObject!!.get("type")!!.asInt
            Log.d(
                "MARKER",
                "markerClicked: $type"
            )
            val id = it.getData()!!.asJsonObject!!.get("id")!!.asLong
            Log.d(TAG, "createMapView: $type $id")
            when (type) {
                SpotType.LANDMARK -> landmarkClicked(id)
                SpotType.SPOT -> spotClicked()
                SpotType.CLUSTER -> clusterClicked()
            }
            true
        })

        mapView.apply {
            getMapboxMap().loadStyleUri("mapbox://styles/chartype/clgd8mwak000001sczpqlrb72") {
                scalebar.enabled = false
                compass.enabled = false
                cameraOptions {
                    zoom(19.0)
                    pitch(50.0)
                    center(Point.fromLngLat(126.995340, 37.564916))
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
                    val curPoint = mapBoxMap.cameraState.center
                    if (isFarEnough(curPoint)) {
                        deletePin()
                        if (_filterState.value == ALL || _filterState.value == LANDMARK) loadLandmarkPos()
                        if (_filterState.value == ALL || _filterState.value == SPOT) loadSpotPos()
                        if (_filterState.value == MYSPOT) loadSpotPos(mine = true)
                    }
                }
            })
        }
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
        return mapView
    }

    fun trackCameraToUser(context: Context) {
        clearFocus()
        if (_isTracking.value == false) {
            _isTracking.value = true
            moveCameraLinearly()
            mapView.apply {
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

    private fun isFarEnough(curPoint: Point): Boolean {
        if (lastRequestPos == null) return true
        val distance = distanceBetweenLastRequestPosAndCurPoint(curPoint)
        Log.d("LANDMARK", "isFarEnough: $distance")
        if (distance > 2) return true
        return false
    }


    private fun moveCameraLinearly() {
        val viewportPlugin = mapView.viewport
        Log.d("moveCameraLinearly", "start moveCameraLinearly")
        val followPuckViewportState: FollowPuckViewportState =
            viewportPlugin.makeFollowPuckViewportState(
                FollowPuckViewportStateOptions.Builder()
                    .zoom(16.5)
                    .pitch(mapBoxMap.cameraState.pitch)
                    .bearing(FollowPuckViewportStateBearing.Constant(mapBoxMap.cameraState.bearing))
                    .build()
            )
        viewportPlugin.transitionTo(followPuckViewportState) {
        }
    }

    private fun unTrackUser() {
        _isTracking.value = false
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

    private fun distanceBetweenLastRequestPosAndCurPoint(curPoint: Point): Double {
        if (lastRequestPos == null) return 30.0
        val distance: Double
        val radius = 6371.0 // 지구 반지름(km)
        val toRadian = Math.PI / 180
        val deltaLatitude = abs(curPoint.latitude() - lastRequestPos!!.latitude()) * toRadian
        val deltaLongitude = abs(curPoint.longitude() - curPoint.longitude()) * toRadian
        val sinDeltaLat = sin(deltaLatitude / 2)
        val sinDeltaLng = sin(deltaLongitude / 2)
        val squareRoot = sqrt(
            sinDeltaLat * sinDeltaLat +
                    cos(curPoint.latitude() * toRadian) * cos(lastRequestPos!!.latitude() * toRadian) * sinDeltaLng * sinDeltaLng
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
        val iconImage = if (spotType == SpotType.LANDMARK) landmarkImage!! else spotImage!!
        // Set options for the resulting symbol layer.
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(Point.fromLngLat(coordinate.lng, coordinate.lat))
            .withIconImage(iconImage)
            .withIconAnchor(IconAnchor.BOTTOM)
            .withIconSize(1.0)
            .withData(
                JsonObject().apply {
                    addProperty("type", SpotType.LANDMARK)
                    if (isVisit != null) addProperty("isVisit", isVisit)
                    if (id != null) addProperty("id", id)
                }
            )
        // Add the resulting pointAnnotation to the map.
        pointAnnotationManager.create(pointAnnotationOptions)
    }

    fun deletePin() {
        pointAnnotationManager.deleteAll()
    }
}

