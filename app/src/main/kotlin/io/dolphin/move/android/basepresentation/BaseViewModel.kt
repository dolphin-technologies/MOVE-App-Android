package io.dolphin.move.android.basepresentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dolphin.move.android.basepresentation.viewmodel.Event
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _showProgress = MutableLiveData(false)
    val showProgress = _showProgress.mapDistinct { it }

    protected fun showProgress() {
        _showProgress.postValue(true)
    }

    protected fun hideProgress() {
        _showProgress.postValue(false)
    }

    protected inline fun <reified T : Event> MutableSharedFlow<T>.offerEvent(event: T) {
        viewModelScope.launch {
            this@offerEvent.emit(event)
        }
    }
}