package io.dolphin.move.android.features.permissions.presentation.components

import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalPermissionsApi
interface PermissionsStateHeaderView {

    fun turnMoveSdkOn()

    fun turnMoveSdkOff()
}