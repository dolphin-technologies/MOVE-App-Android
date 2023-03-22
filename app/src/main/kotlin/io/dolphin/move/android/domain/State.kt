package io.dolphin.move.android.domain

/**
 * Holder for any action
 */

sealed class State<out T : Any> {
    object None : State<Nothing>()
    object Loading : State<Nothing>()
    data class Data<C : Any>(val data: C) : State<C>()
    data class Error(val error: Throwable) : State<Nothing>() {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Error
            if (error.javaClass != other.error.javaClass) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + error.hashCode()
            return result
        }
    }

    fun asData(): T {
        return (this as Data<T>).data
    }

    fun asError(): Throwable {
        return (this as Error).error
    }

    fun asDataOrNull(): T? {
        return (this as? Data<T>)?.data
    }

    fun asErrorOrNull(): Throwable? {
        return (this as? Error)?.error
    }

    override fun toString(): String = javaClass.simpleName
}
