package com.n27.core.presentation.detail.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.R
import com.n27.core.databinding.DialogDetailBinding
import com.n27.core.domain.election.Election
import com.n27.core.presentation.PresentationUtils
import com.n27.core.presentation.detail.DetailActivity
import com.n27.core.presentation.detail.dialog.adapters.InfoAdapter
import com.n27.core.presentation.detail.dialog.mappers.toInfoList
import javax.inject.Inject

class DetailDialog : DialogFragment() {

    private var _binding: DialogDetailBinding? = null
    private val binding get() = _binding!!
    @Inject internal lateinit var utils: PresentationUtils

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as DetailActivity).coreComponent.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogDetailBinding.inflate(layoutInflater)

        val election = arguments?.getSerializable(KEY_ELECTION) as Election

        val title = "${election.chamberName} ${election.place} ${election.date}"
        binding.titleDialogDetail.text = title
        binding.listDialogDetail.apply {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically() = false
            }

            adapter = InfoAdapter(
                election.toInfoList(),
                names = resources.getStringArray(R.array.text_array_dialog_detail)
            )
        }

        return AlertDialog.Builder(activity).apply {
            setView(binding.root)
            setPositiveButton(resources.getString(R.string.close), null)
        }.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
