package io.dolphin.move.android.features.launch.presentation

import dagger.hilt.android.lifecycle.HiltViewModel
import io.dolphin.move.android.basedata.network.responses.ApiLogin
import io.dolphin.move.android.basepresentation.BaseViewModel
import io.dolphin.move.android.domain.usecase.GetUserInteractor
import io.dolphin.move.android.firebase.domain.RemoteConfigInteractor
import javax.inject.Inject

@HiltViewModel
class LaunchScreenViewModel @Inject constructor(
    remoteConfigInteractor: RemoteConfigInteractor,
    private val getUserInteractor: GetUserInteractor,
) : BaseViewModel() {

    fun getUser(): ApiLogin? {
        return getUserInteractor.getUser()
    }
}