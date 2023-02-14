package com.jorgedguezm.elections.presentation.common.inheritance

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.jorgedguezm.elections.data.utils.DataUtils
import dagger.android.AndroidInjection
import javax.inject.Inject

open class ViewModelActivity : AppCompatActivity() {

    @Inject
    lateinit var dataUtils: DataUtils

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    protected inline fun <reified VM : ViewModel> viewModel(): Lazy<VM> = viewModels { viewModelFactory }
}
