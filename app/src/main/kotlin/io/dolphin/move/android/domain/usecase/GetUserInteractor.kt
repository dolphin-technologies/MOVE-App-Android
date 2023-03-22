package io.dolphin.move.android.domain.usecase

import io.dolphin.move.android.basedata.local.storage.UserStorage
import io.dolphin.move.android.basedata.network.responses.ApiContract
import io.dolphin.move.android.basedata.network.responses.ApiLogin
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class GetUserInteractor @Inject constructor(
    private val userStorage: UserStorage,
) {
    fun subscribeUser(): Flow<ApiLogin> {
        return userStorage.subscribeUserData()
    }

    fun subscribeUnreadMessages(): StateFlow<Int> {
        return userStorage.getUnreadMessagesCountLive()
    }

    fun getUser(): ApiLogin? {
        return userStorage.getUser()
    }

    fun getContract(): ApiContract? {
        return userStorage.getContract()
    }

    fun deleteUserData() {
        userStorage.setUser(null)
        userStorage.setContract(null)
    }

}