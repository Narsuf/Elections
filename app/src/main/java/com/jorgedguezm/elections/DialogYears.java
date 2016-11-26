package com.jorgedguezm.elections;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.IOException;

/**
 * Created by jorge on 14/11/16.
 */

public class DialogYears extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View inflatedLayout = li.inflate(R.layout.dialog_years, null);
        AlertDialog.Builder window = new AlertDialog.Builder(getActivity());

        final Spinner spinner = (Spinner) inflatedLayout.findViewById(R.id.spinnerYears);

        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.
                    list_item_years, R.id.tvYear, getActivity().getAssets().list(getArguments().
                    getString("path")));
            spinner.setAdapter(adapter);

        } catch (IOException e) {
            e.printStackTrace();
        }

        window.setView(inflatedLayout);
        window.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getArguments().getString("from").equals("fragment")) {
                    Intent intent = new Intent(getActivity(), ActivityShow.class);
                    intent.putExtra("path", getArguments().getString("path") + "/" + spinner.
                            getSelectedItem().toString());
                    intent.putExtra("from", "regional");
                    startActivity(intent);
                } else if (getArguments().getString("from").equals("activity")) {
                    ActivityShow activityShow = (ActivityShow) getActivity();
                    activityShow.setPath(getArguments().getString("path")
                            + "/" + spinner.getSelectedItem().toString());
                    activityShow.beginTransaction();
                }
            }
        });
        window.setNegativeButton(R.string.cancel, null);
        return window.create();
    }
}
