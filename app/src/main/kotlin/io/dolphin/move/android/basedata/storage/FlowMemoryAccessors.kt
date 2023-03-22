package io.dolphin.move.android.basedata.storage

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.ReadWriteProperty

fun <T : Any> HashMapMemory.sharedFlow(
    propertyName: String? = null,
): ReadWriteProperty<Any?, MutableSharedFlow<T>> {
    return getOrPutProperty(propertyName) {
        MutableSharedFlow()
    }
}

fun <T : Any> HashMapMemory.stateFlow(
    default: T,
    propertyName: String? = null,
): ReadWriteProperty<Any?, MutableStateFlow<T>> {
    return getOrPutProperty(propertyName) {
        MutableStateFlow(default)
    }
}