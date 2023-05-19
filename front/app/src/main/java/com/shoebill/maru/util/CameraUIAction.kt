package com.shoebill.maru.util

sealed class CameraUIAction {
    object OnCameraClick : CameraUIAction()
    object OnSwitchCameraClick : CameraUIAction()
}
