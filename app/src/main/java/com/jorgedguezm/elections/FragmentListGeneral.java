package com.jorgedguezm.elections;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by jorge on 13/11/16.
 */

public class FragmentListGeneral extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_view, container, false);
        String[] data = getResources().getStringArray(R.array.item_list_general);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.list_item_general, R.id.lTitle, data);

        ListView listOptions = (ListView) v.findViewById(R.id.listView);
        listOptions.setAdapter(adapter);
        listOptions.setDivider(getResources().getDrawable(R.drawable.general_divider));

        listOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Intent intent = new Intent(getActivity(), ActivityShow.class);
                intent.putExtra("congress", "general/congress/" + getResources().
                        getStringArray(R.array.general_paths)[position]);
                intent.putExtra("senate", "general/senate/" + getResources().
                        getStringArray(R.array.general_paths)[position]);
                intent.putExtra("from", "general");
                startActivity(intent);

            }
        });
        return v;

    }

}
