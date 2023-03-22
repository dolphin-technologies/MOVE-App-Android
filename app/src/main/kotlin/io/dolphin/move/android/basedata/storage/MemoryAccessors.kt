package io.dolphin.move.android.basedata.storage

import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <M : HashMapMemory> M.fillFrom(memory: HashMapMemory): M = apply { putAll(memory) }

/**
 * Delegate for nullable values from [HashMapMemory].
 * Returns null, if there is no value for the key in memory..
 */
fun <T : Any> HashMapMemory.nullable(propertyName: String? = null): ReadWriteProperty<Any?, T?> {
    return object : ReadWriteProperty<Any?, T?> {
        @Suppress("UNCHECKED_CAST")
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? = get(propertyName ?: property.name) as T?

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            if (value == null) {
                remove(propertyName ?: property.name)
            } else {
                put(propertyName ?: property.name, value)
            }
        }
    }
}

/**
 * Delegate for not null values from [HashMapMemory].
 * Returns a default value and puts it into memory, if there is no value for the key in memory..
 */
fun <T : Any> HashMapMemory.notNull(propertyName: String? = null, default: () -> T): ReadWriteProperty<Any?, T> {
    return getOrPutProperty(propertyName, default)
}

/**
 * Delegate for [HashMap] from [HashMapMemory].
 * Returns an empty[HashMap] (and puts it into memory), if there is no value for the key in memory..
 */
fun <K, V> HashMapMemory.map(propertyName: String? = null): ReadWriteProperty<Any?, MutableMap<K, V>> {
    return getOrPutProperty(propertyName) { ConcurrentHashMap<K, V>() }
}

/**
 * Delegate for [EnumMap] from [HashMapMemory].
 * Returns an empty [EnumMap] (and puts it into memory), if there is no value for the key in memory..
 */
inline fun <reified K : Enum<K>, V> HashMapMemory.enumMap(
    propertyName: String? = null,
): ReadWriteProperty<Any?, EnumMap<K, V>> {
    return getOrPutProperty(propertyName) { EnumMap(K::class.java) }
}

/**
 * Delegate for [MutableList] from [HashMapMemory].
 * Returns an empty list (and puts it into memory), if there is no value for the key in memory..
 */
fun <T> HashMapMemory.list(propertyName: String? = null): ReadWriteProperty<Any?, MutableList<T>> {
    return getOrPutProperty(propertyName) { mutableListOf() }
}

/**
 * Delegate for [MutableSet] from [HashMapMemory].
 * Returns an empty set (and puts it into memory), if there is no value for the key in memory..
 */
fun <T> HashMapMemory.set(propertyName: String? = null): ReadWriteProperty<Any?, MutableSet<T>> {
    return getOrPutProperty(propertyName) { mutableSetOf() }
}

inline fun <T : Any> HashMapMemory.getOrPutProperty(
    propertyName: String? = null,
    crossinline default: () -> T,
): ReadWriteProperty<Any?, T> {
    return object : ReadWriteProperty<Any?, T> {
        @Suppress("UNCHECKED_CAST")
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return getOrPut(propertyName ?: property.name, default) as T
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            put(propertyName ?: property.name, value)
        }
    }
}
