package com.jorgedguezm.elections.data.injection.vm

import androidx.lifecycle.ViewModel

import dagger.MapKey

import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)
