package com.jorgedguezm.elections.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_ELECTIONS
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_PARTIES
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_RESULTS
import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Results
import com.jorgedguezm.elections.utils.Utils

import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.detail_activity.*

import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    lateinit var election: Election
    lateinit var partiesColor: HashMap<String, String>
    lateinit var results: ArrayList<Results>

    @Inject
    lateinit var utils: Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        setSupportActionBar(toolbar)

        AndroidInjection.inject(this)

        val bundle = intent.extras
        election = bundle?.getSerializable(KEY_ELECTIONS) as Election
        partiesColor = bundle.getSerializable(KEY_PARTIES) as HashMap<String, String>
        results = bundle.getSerializable(KEY_RESULTS) as ArrayList<Results>

        utils.drawPieChart(pie_chart, utils.getElectsFromResults(results),
                utils.getColorsFromResults(results, partiesColor))
    }
}