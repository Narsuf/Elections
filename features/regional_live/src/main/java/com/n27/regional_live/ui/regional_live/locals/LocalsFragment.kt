package com.n27.regional_live.ui.regional_live.locals

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.n27.regional_live.databinding.FragmentLocalsBinding
import com.n27.regional_live.ui.regional_live.RegionalLiveActivity

class LocalsFragment : Fragment() {

    private var _binding: FragmentLocalsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val viewModel = ViewModelProvider(this)[LocalsViewModel::class.java]

        _binding = FragmentLocalsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        viewModel.text.observe(viewLifecycleOwner) { textView.text = it }
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as RegionalLiveActivity).regionalLiveComponent.inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}