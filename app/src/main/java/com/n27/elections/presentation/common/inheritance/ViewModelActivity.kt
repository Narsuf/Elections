package com.n27.elections.presentation.common.inheritance

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.n27.elections.data.DataUtils
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
