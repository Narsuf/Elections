package com.jorgedguezm.elections.extension

import android.view.View

import com.google.android.material.snackbar.Snackbar

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.models.Resource
import com.jorgedguezm.elections.models.Status

inline fun <reified T> View.bindResource(resource: Resource<T>?, onSuccess: (T?) -> Unit) {
    if (resource != null) {
        when (resource.status) {
            Status.LOADING -> Unit
            Status.SUCCESS -> onSuccess(resource.data)
            Status.ERROR -> {
                Snackbar.make(this, context.getString(R.string.something_wrong),
                        Snackbar.LENGTH_LONG).show()

                // Debug
                //Snackbar.make(this, resource.errorEnvelope?.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        }
    }
}