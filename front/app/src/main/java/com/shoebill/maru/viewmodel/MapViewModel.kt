package com.shoebill.maru.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
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

@SuppressLint("StaticFieldLeak")
class MapViewModel : ViewModel() {
    private lateinit var mapView: MapView
    private lateinit var mapBoxMap: MapboxMap
    private var _isTracking = false

    val myLocationColor: Brush
        get() {
            return if (_isTracking) {
                Brush.linearGradient(listOf(Color(0xFF6039DF), Color(0xFFA14AB7)))
            } else {
                Brush.linearGradient(listOf(Color(0xFF6039DF), Color(0xFFA14AB7)))
            }
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
                    if (_isTracking) unTrackUser()
                }

                override fun onMoveEnd(detector: MoveGestureDetector) {
                }
            })
        }
        return mapView
    }

    fun trackCameraToUser(context: Context) {
        if (!_isTracking) {
            _isTracking = true
            moveCameraLinearly()
            mapView.apply {
                location.updateSettings {
                    enabled = true
                    pulsingEnabled = true
                    pulsingMaxRadius = 100f
                    locationPuck = LocationPuck2D(
                        topImage = AppCompatResources.getDrawable(
                            context,
                            R.drawable.mapbox_user_icon
                        ),
                        bearingImage = AppCompatResources.getDrawable(
                            context,
                            R.drawable.mapbox_user_bearing_icon
                        ),
                        shadowImage = AppCompatResources.getDrawable(
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
                    .bearing(FollowPuckViewportStateBearing.Constant(mapBoxMap.cameraState.bearing))
                    .build()
            )
        Log.d("moveCameraLinearly", "middle moveCameraLinearly")
        viewportPlugin.transitionTo(followPuckViewportState) {
            Log.d("moveCameraLinearly", "end moveCameraLinearly")
        }
    }

    private fun unTrackUser() {
        _isTracking = false
        mapView.location.updateSettings {
            enabled = false
        }
    }
}

