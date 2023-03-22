package io.dolphin.move.android.features.messages.presentation.messages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dolphin.move.android.basepresentation.BaseViewModel
import io.dolphin.move.android.basepresentation.components.ErrorState
import io.dolphin.move.android.basepresentation.delegate
import io.dolphin.move.android.basepresentation.mapDistinct
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.messages.domain.MessagesInteractor
import io.dolphin.move.android.features.messages.domain.RemoveMessageInteractor
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val messagesInteractor: MessagesInteractor,
    private val removeMessageInteractor: RemoveMessageInteractor,
) : BaseViewModel(), MessagesView {

    private val liveState = MutableLiveData(MessagesViewState())
    private var viewState: MessagesViewState by liveState.delegate()

    val messagesState = liveState.mapDistinct { it }

    init {
        requestMessages()
        messagesInteractor.getMessagesFlow()
            .onEach { messagesList ->
                viewState = viewState.copy(
                    messages = messagesList.map { it.mapToMessageState() },
                    error = null,
                    onRemoveErrorReset = false,
                )
                hideProgress()
            }
            .launchIn(viewModelScope)
    }

    override fun retryOnError() {
        requestMessages()
    }

    override fun onRemoveItem(id: Long) {
        showProgress()
        viewModelScope.launch {
            removeMessageInteractor(id)
        }
    }

    override fun markAsRead(id: Long) {
        /*val updatedList = buildList {
            viewState.messages.forEach { messageState ->
                if (messageState.id == id) {
                    add(messageState.copy(isNew = false))
                } else {
                    add(messageState)
                }
            }
        }
        viewState = viewState.copy(
            messages = updatedList,
            onRemoveErrorReset = false,
        )*/
    }

    private fun requestMessages() {
        viewModelScope.launch {
            showProgress()
            when (messagesInteractor.requestMessages()) {
                is State.Error -> {
                    hideProgress()
                    viewState = MessagesViewState(error = ErrorState())
                }
                is State.Data,
                State.Loading,
                State.None -> { /* do nothing*/
                }
            }
        }
    }
}