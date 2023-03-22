package io.dolphin.move.android.basepresentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T : Any> MutableLiveData<T>.delegate(): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = onNext(value)
        override fun getValue(thisRef: Any, property: KProperty<*>): T = requireValue()
    }
}

fun <T> MutableLiveData<T>.onNext(next: T) {
    this.value = next
}

fun <T : Any> LiveData<T>.requireValue(): T = checkNotNull(value)

inline fun <X, Y> LiveData<X>.mapDistinct(crossinline transform: (X) -> Y): LiveData<Y> {
    return map(transform).distinctUntilChanged()
}