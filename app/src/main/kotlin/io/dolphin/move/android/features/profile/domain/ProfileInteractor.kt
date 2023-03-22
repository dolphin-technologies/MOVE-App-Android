package io.dolphin.move.android.features.profile.domain

import io.dolphin.move.android.basedata.network.responses.ApiContract
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.profile.data.ProfileRepository
import io.dolphin.move.android.features.profile.domain.dto.ProfileDto
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend fun getContractData(): State<ApiContract> {
        return profileRepository.requestContractData()
    }

    suspend fun requestLogout(): State<Boolean> {
        return profileRepository.requestLogout()
    }

    suspend fun requestSave(profileData: ProfileDto): State<ApiContract> {
        return profileRepository.requestSave(profileData)
    }

    suspend fun requestEmailChange(profileData: ProfileDto): State<Boolean> {
        return profileRepository.requestEmailChange(profileData)
    }

}