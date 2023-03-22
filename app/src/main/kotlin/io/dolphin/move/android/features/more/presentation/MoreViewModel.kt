package io.dolphin.move.android.features.more.presentation

import dagger.hilt.android.lifecycle.HiltViewModel
import io.dolphin.move.android.BuildConfig
import io.dolphin.move.android.basedata.local.storage.UserStorage
import io.dolphin.move.android.basepresentation.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val userStorage: UserStorage,
) : BaseViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext = Dispatchers.Main + job

    fun getUserId(): String {
        return userStorage.getUser()?.contractId ?: ""
    }

    fun getAppVersion(): String {
        return BuildConfig.VERSION_NAME
    }

}
