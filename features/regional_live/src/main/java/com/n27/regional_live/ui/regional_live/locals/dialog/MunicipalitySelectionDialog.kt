package com.n27.regional_live.ui.regional_live.locals.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.data.models.Election
import com.n27.core.presentation.PresentationUtils
import com.n27.regional_live.R
import com.n27.regional_live.ui.regional_live.RegionalLiveActivity
import java.text.NumberFormat.getIntegerInstance
import javax.inject.Inject

class MunicipalitySelectionDialog : DialogFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as RegionalLiveActivity).regionalLiveComponent.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity as RegionalLiveActivity
        val layoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val inflatedLayout = layoutInflater.inflate(R.layout.dialog_municipality_selection, null)
        val window = AlertDialog.Builder(activity)

        /*val provinces = arguments?.getSerializable(KE) as Election

        val textViewTitle = inflatedLayout.findViewById(R.id.text_view_title) as TextView
        val concatenatedText = "${election.chamberName} ${election.place} ${election.date}"
        textViewTitle.text = concatenatedText*/

        return window.apply {
            setView(inflatedLayout)
            setPositiveButton(resources.getString(R.string.close), null)
        }.create()
    }
}
