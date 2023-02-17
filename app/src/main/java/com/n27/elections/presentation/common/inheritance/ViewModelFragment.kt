package com.n27.elections.presentation.common.inheritance

import android.content.Context
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.n27.elections.presentation.common.PresentationUtils
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

open class ViewModelFragment : Fragment() {

    @Inject
    lateinit var utils: PresentationUtils

    @Inject
    lateinit var analytics: FirebaseAnalytics

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}
