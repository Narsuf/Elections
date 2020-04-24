package com.jorgedguezm.elections.view.binders

import android.graphics.Color
import android.view.View
import android.widget.SimpleAdapter
import android.widget.TextView

class PartyColorBinder: SimpleAdapter.ViewBinder {

    override fun setViewValue(view: View?, data: Any?, textRepresentation: String?): Boolean {
        if (view is TextView && data.toString().matches(Regex("[#].*"))) {
            view.setBackgroundColor(Color.parseColor(data.toString()))
            return true
        }

        return false
    }
}