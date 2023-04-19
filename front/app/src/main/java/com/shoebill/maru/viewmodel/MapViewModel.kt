package com.shoebill.maru.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModel
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.R
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.scalebar.scalebar

@SuppressLint("StaticFieldLeak")
class MapViewModel() : ViewModel() {
    private lateinit var mapView: MapView
    private lateinit var mapBoxMap: MapboxMap
    private var isTracking = false

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
                }

                override fun onMoveEnd(detector: MoveGestureDetector) {
                }
            })
        }
        return mapView
    }

    fun trackCameraToUser(context: Context) {
        if (!isTracking) {
            isTracking = true
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
                location.addOnIndicatorBearingChangedListener {
                    getMapboxMap()
                        .setCamera(CameraOptions.Builder().build())
                }
                location.addOnIndicatorPositionChangedListener {
                    getMapboxMap()
                        .setCamera(CameraOptions.Builder().center(it).zoom(18.0).build())
                    gestures.focalPoint = getMapboxMap().pixelForCoordinate(it)
                }
            }
        } else {
            unTrackUser()
        }
    }

    private fun unTrackUser() {
        isTracking = false
        mapView.apply {
            location.updateSettings {
                enabled = false
            }
        }
    }
}

