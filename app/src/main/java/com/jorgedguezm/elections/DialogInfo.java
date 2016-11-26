package com.jorgedguezm.elections;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jorge on 26/11/16.
 */

public class DialogInfo extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ActivityShow activity = (ActivityShow) getActivity();
        LayoutInflater li = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View inflatedLayout = li.inflate(R.layout.dialog_info, null);
        AlertDialog.Builder window = new AlertDialog.Builder(activity);
        Institution institution = getArguments().getParcelable("institution");
        Votes votes = getArguments().getParcelable("votes");
        TextView textView = (TextView) inflatedLayout.findViewById(R.id.chamberName);
        textView.setText(institution.getChamberName() + " " + institution.getPlace()
                + " " + String.valueOf(institution.getYear()));

        String[] from = {"text", "number", "percentage"};
        int[] to = {R.id.tvText, R.id.tvNumber, R.id.tvPercentage};
        String[] textData = {getResources().getString(R.string.scrutinized),
                getResources().getString(R.string.elects),
                getResources().getString(R.string.countedVotes),
                getResources().getString(R.string.abstentions),
                getResources().getString(R.string.nullVotes),
                getResources().getString(R.string.blankVotes)};
        String[] numberData = {"", String.valueOf(institution.getTotalElects()),
                String.valueOf(votes.getCounted()), String.valueOf(votes.getAbstentions()),
                String.valueOf(votes.getNullVote()), String.valueOf(votes.getBlankVote())};
        String[] percentageData = {String.valueOf(votes.getScrutinized()), "",
                String.valueOf(votes.getPercentageOfParticipation()),
                String.valueOf(votes.getPercentageOfAbstentions()),
                String.valueOf(votes.getPercentageOfNull()),
                String.valueOf(votes.getPercentageOfBlank())};
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        for (int i = 0; i < textData.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put(from[0], textData[i]);
            map.put(from[1], numberData[i]);
            if (i == 1) {
                map.put(from[2], percentageData[i]);
            } else {
                map.put(from[2], percentageData[i] + "%");
            }
            arrayList.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(activity, arrayList, R.layout.list_item_info,
                from, to);

        ListView listOptions = (ListView) inflatedLayout.findViewById(R.id.listViewInfo);
        listOptions.setAdapter(adapter);

        window.setView(inflatedLayout);
        window.setPositiveButton(getResources().getString(R.string.close), null);
        return window.create();
    }
}
