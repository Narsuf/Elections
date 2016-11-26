package com.jorgedguezm.elections;

import android.graphics.Color;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * Created by jorge on 24/11/16.
 */

public class PartyColorBinder implements SimpleAdapter.ViewBinder {
    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {
        if (view instanceof TextView && data.toString().matches("[#].*")) {
            view.setBackgroundColor(Color.parseColor(data.toString()));
            return true;
        }
        return false;
    }
}