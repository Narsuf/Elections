package com.jorgedguezm.elections.view.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jorgedguezm.elections.presentation.common.Utils
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

open class ViewModelFragment : Fragment() {

    @Inject
    lateinit var utils: Utils

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    protected inline fun <reified VM : ViewModel>
            viewModel(): Lazy<VM> = viewModels { viewModelFactory }
}
