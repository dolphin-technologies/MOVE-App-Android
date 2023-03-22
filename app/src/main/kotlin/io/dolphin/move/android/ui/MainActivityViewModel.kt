package io.dolphin.move.android.ui

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dolphin.move.android.basepresentation.AppEvent
import io.dolphin.move.android.basepresentation.BaseViewModel
import io.dolphin.move.android.basepresentation.delegate
import io.dolphin.move.android.basepresentation.mapDistinct
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.domain.usecase.AppEventsInteractor
import io.dolphin.move.android.domain.usecase.GetUserInteractor
import io.dolphin.move.android.features.messages.domain.MessagesInteractor
import io.dolphin.move.android.pushnotification.domain.CheckPushTokenInteractor
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userInteractor: GetUserInteractor,
    private val appEventsInteractor: AppEventsInteractor,
    private val messagesInteractor: MessagesInteractor,
    private val checkPushTokenInteractor: CheckPushTokenInteractor,
) : BaseViewModel() {

    private val eventState: MutableSharedFlow<AppEvent> = MutableSharedFlow()
    val appEvents: SharedFlow<AppEvent> = eventState.asSharedFlow()

    private val liveState = MutableLiveData(MainActivityViewState())
    private var viewState: MainActivityViewState by liveState.delegate()

    val mainViewState = liveState.mapDistinct { it }

    private var previousBackButtonState = false

    init {
        subscribeAppEvents()
        subscribeNewMessagesCount()
    }

    fun showBackButton(value: Boolean) {
        previousBackButtonState = viewState.showBackButton
        viewState = viewState.copy(showBackButton = value)
    }

    fun showMessagesIcon(value: Boolean) {
        viewState = viewState.copy(showMessagesIcon = value)
    }

    fun onBack() {
        viewState = viewState.copy(showBackButton = previousBackButtonState)
    }

    fun updateTitleId(@StringRes titleId: Int) {
        viewState = viewState.copy(titleId = titleId)
    }

    fun reuqestMessages() {
        if (userInteractor.getUser() != null) {
            // Request for the new messages. Also we check and handle errors where app should logout
            viewModelScope.launch {
                val dataResult = messagesInteractor.requestMessages()
                if (dataResult is State.Data) {
                    checkPushTokenInteractor()
                }
            }
        }
    }

    private fun subscribeAppEvents() {
        appEventsInteractor.subscribeAppEvents()
            .onEach { event ->
                viewModelScope.launch { eventState.emit(event) }
            }
            .launchIn(viewModelScope)
    }

    private fun subscribeNewMessagesCount() {
        userInteractor.subscribeUnreadMessages()
            .onEach { count ->
                viewState = viewState.copy(unreadMessagesCount = count)
            }
            .launchIn(viewModelScope)
    }
}