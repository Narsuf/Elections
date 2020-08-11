package com.jorgedguezm.elections.models

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

import com.jorgedguezm.elections.models.network.ErrorEnvelope

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE", "LiftReturnOrAssignment", "RedundantOverride", "SpellCheckingInspection")
class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    var errorEnvelope: ErrorEnvelope? = null

    init {
        message?.let {
            try {
                val gson = Gson()
                errorEnvelope = gson.fromJson(message, ErrorEnvelope::class.java) as ErrorEnvelope
            } catch (e: JsonSyntaxException) {
                errorEnvelope = ErrorEnvelope(400, "BAD_REQUEST", message, false)
            }
        }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }

        val resource = o as Resource<*>

        if (status !== resource.status) {
            return false
        }
        if (if (message != null) message != resource.message else resource.message != null) {
            return false
        }
        return if (data != null) data == resource.data else resource.data == null
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return "Resource[" +
                "status=" + status + '\'' +
                ",message='" + message + '\'' +
                ",data=" + data +
                ']'
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(status = Status.SUCCESS, data = data, message = null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(status = Status.ERROR, data = data, message = msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(status = Status.LOADING, data = data, message = null)
        }
    }
}