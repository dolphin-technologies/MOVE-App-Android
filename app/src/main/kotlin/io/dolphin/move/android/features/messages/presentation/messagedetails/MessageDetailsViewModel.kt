package io.dolphin.move.android.features.messages.presentation.messagedetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dolphin.move.android.basepresentation.BaseViewModel
import io.dolphin.move.android.basepresentation.delegate
import io.dolphin.move.android.basepresentation.mapDistinct
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.messages.domain.MarkAsReadInteractor
import io.dolphin.move.android.features.messages.domain.MessagesInteractor
import io.dolphin.move.android.features.messages.domain.RemoveMessageInteractor
import io.dolphin.move.android.ui.navigation.Routes
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class MessageDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val messagesInteractor: MessagesInteractor,
    private val removeMessageInteractor: RemoveMessageInteractor,
    private val markAsReadInteractor: MarkAsReadInteractor,
) : BaseViewModel(), MessageDetailsView {

    private val eventState: MutableSharedFlow<MessageDetailsEvent> = MutableSharedFlow()
    val events: SharedFlow<MessageDetailsEvent> = eventState.asSharedFlow()

    private var messagesList: List<MessageDetailsViewState>? = null
    private var indexToMove: Int? = null

    private val liveState = MutableLiveData(MessageDetailsViewState())
    private var viewState: MessageDetailsViewState by liveState.delegate()

    val messageDetailsState = liveState.mapDistinct { it }

    init {
        val messageId = savedStateHandle.get<Long>(
            Routes.MessagesRoot.MessageDetails.Args.messageId
        )
        var isInit = true
        messagesInteractor.getMessagesFlow()
            .onEach { moveMessages ->
                if (moveMessages.isEmpty()) eventState.offerEvent(MessageDetailsEvent.NoMessages)
                messagesList =
                    moveMessages.mapToMessageStateList()
                val index: Int? = if (isInit) {
                    moveMessages.indexOfFirst { it.id == messageId }.takeIf { it >= 0 }
                } else if (indexToMove != null) {
                    indexToMove
                } else {
                    moveMessages.indexOfFirst { it.id == viewState.id }.takeIf { it >= 0 }
                }
                showMessage(index)
                isInit = false
                hideProgress()
            }
            .launchIn(viewModelScope)
    }

    override fun showMessage(index: Int?) {
        if (index == null) return
        messagesList?.get(index)?.let { messageDetailsState ->
            if (messageDetailsState.isNew) {
                markAsRead(messageDetailsState.id)
            }
            viewState = messageDetailsState
        }
    }

    override fun removeMessage(id: Long) {
        indexToMove = viewState.prevIndex ?: viewState.nextIndex
        showProgress()
        viewModelScope.launch {
            removeMessageInteractor(id)
        }
    }

    private fun markAsRead(id: Long) {
        showProgress()
        viewModelScope.launch {
            when (markAsReadInteractor(id)) {
                is State.Error -> hideProgress()
                is State.Data,
                State.Loading,
                State.None -> { /* do nothing*/
                }
            }
        }
    }
}