package com.shoebill.maru.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.locationcomponent.R
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.scalebar.scalebar
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateBearing
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateOptions
import com.mapbox.maps.plugin.viewport.state.FollowPuckViewportState
import com.mapbox.maps.plugin.viewport.viewport
import com.shoebill.maru.model.data.Spot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {
    private lateinit var mapView: MapView
    private lateinit var mapBoxMap: MapboxMap
    private var isTracking = false
    private lateinit var _focusManager: FocusManager

    private val _spotList = MutableLiveData<List<Spot>>(
        listOf(
            Spot(
                0,
                "https://picsum.photos/id/237/200/300",
                false,
                listOf("#도로", "#길거리", "#감성", "#인생샷스팟")
            ),
            Spot(
                1,
                "https://picsum.photos/id/237/200/300",
                false,
                listOf("#도로", "#길거리", "#감성", "#인생샷스팟")
            ),
            Spot(
                2,
                "https://picsum.photos/id/237/200/300",
                false,
                listOf("#도로", "#길거리", "#감성", "#인생샷스팟")
            ),
        )
    )
    val spotList: LiveData<List<Spot>> get() = _spotList

    val myLocationColor: Brush
        get() {
            return if (isTracking) {
                Brush.linearGradient(listOf(Color(0xFF6039DF), Color(0xFFA14AB7)))
            } else {
                Brush.linearGradient(listOf(Color(0xFF6039DF), Color(0xFFA14AB7)))
            }
        }

    fun initFocusManager(fm: FocusManager) {
        _focusManager = fm
    }

    fun clearFocus() {
        _focusManager.clearFocus()
    }

    fun createMapView(context: Context): MapView {
        mapView = MapView(context)
        mapBoxMap = mapView.getMapboxMap()
        mapView.apply {
            getMapboxMap().loadStyleUri("mapbox://styles/chartype/clgd8mwak000001sczpqlrb72") {
                scalebar.enabled = false
                compass.enabled = false
                cameraOptions {
                    zoom(19.0)
                    pitch(50.0)
                }
            }
            mapBoxMap.addOnMoveListener(object : OnMoveListener {
                override fun onMove(detector: MoveGestureDetector): Boolean {
                    return false
                }

                override fun onMoveBegin(detector: MoveGestureDetector) {
                    if (isTracking) unTrackUser()
                    _focusManager.clearFocus()
                }

                override fun onMoveEnd(detector: MoveGestureDetector) {
                }
            })
        }
        val annotationApi = mapView.annotations
        val pointAnnotationManager = annotationApi.createPointAnnotationManager()
// Set options for the resulting symbol layer.
        val drawable = getDrawable(context, com.shoebill.maru.R.drawable.marker)
        val bitmapDrawable = drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            // Define a geographic coordinate.
            .withPoint(Point.fromLngLat(127.0361793, 37.5001917))
            // Specify the bitmap you assigned to the point annotation
            // The bitmap will be added to map style automatically.
            .withIconImage(bitmap)
            .withIconAnchor(IconAnchor.BOTTOM)
            .withIconSize(0.2)
// Add the resulting pointAnnotation to the map.
        pointAnnotationManager.create(pointAnnotationOptions)
        return mapView
    }

    fun trackCameraToUser(context: Context) {
        clearFocus()
        if (!isTracking) {
            isTracking = true
            moveCameraLinearly()
            mapView.apply {
                location.updateSettings {
                    enabled = true
                    pulsingEnabled = true
                    pulsingMaxRadius = 100f
                    locationPuck = LocationPuck2D(
                        topImage = getDrawable(
                            context,
                            R.drawable.mapbox_user_icon
                        ),
                        bearingImage = getDrawable(
                            context,
                            R.drawable.mapbox_user_bearing_icon
                        ),
                        shadowImage = getDrawable(
                            context,
                            R.drawable.mapbox_user_stroke_icon
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
        isTracking = false
        mapView.location.updateSettings {
            enabled = false
        }
    }
}

