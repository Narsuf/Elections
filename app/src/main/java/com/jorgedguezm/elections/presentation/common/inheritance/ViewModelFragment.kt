package com.jorgedguezm.elections.presentation.common.inheritance

import android.content.Context
import androidx.fragment.app.Fragment
import com.jorgedguezm.elections.presentation.common.PresentationUtils
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

open class ViewModelFragment : Fragment() {

    @Inject
    lateinit var utils: PresentationUtils

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}
