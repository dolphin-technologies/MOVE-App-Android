package io.dolphin.move.android.basepresentation.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import java.util.*

class EventsQueue : MutableLiveData<Queue<Event>>() {

    @MainThread
    fun offer(event: Event) {
        val queue = (value ?: LinkedList()).apply {
            add(event)
        }
        value = queue
    }

    @MainThread
    fun pop() {
        val queue = (value ?: LinkedList()).apply {
            remove()
        }
        value = queue
    }
}

class GenericEventsQueue<T : Event> : MutableLiveData<Queue<T>>(LinkedList()) {

    @MainThread
    fun offerEvent(event: T) {
        val queue = (value ?: LinkedList()).apply {
            add(event)
        }
        value = queue
    }

    @MainThread
    fun pop(event: Event) {
        val queue = (value ?: LinkedList()).apply {
            remove(event)
        }
        value = queue
    }
}
