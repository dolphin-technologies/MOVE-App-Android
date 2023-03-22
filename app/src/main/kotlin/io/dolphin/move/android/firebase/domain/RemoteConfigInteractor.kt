package io.dolphin.move.android.firebase.domain

import io.dolphin.move.android.firebase.data.RemoteConfigRepository
import io.dolphin.move.android.firebase.data.RemoteConfigs
import javax.inject.Inject

class RemoteConfigInteractor @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) {

    fun initRemoteConfigs() {
        remoteConfigRepository.initRemoteConfigs()
    }

    fun getRemoteConfigs(): RemoteConfigs {
        return remoteConfigRepository.getRemoteConfigs()
    }
}