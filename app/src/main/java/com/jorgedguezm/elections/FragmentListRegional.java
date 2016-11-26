package com.jorgedguezm.elections;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jorge on 14/11/16.
 */

public class FragmentListRegional extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_view, container, false);

        String[] data = getResources().getStringArray(R.array.region_names);
        String[] from = {"name", "flag"};
        int[] to = {R.id.cityName, R.id.cityFlag};
        TypedArray flags = getResources().obtainTypedArray(R.array.region_flags);
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put(from[0], data[i]);
            map.put(from[1], flags.getResourceId(i, 0));
            arrayList.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(getContext(), arrayList, R.layout.list_item_local, from, to);

        ListView listOptions = (ListView) v.findViewById(R.id.listView);
        listOptions.setAdapter(adapter);
        listOptions.setDivider(getResources().getDrawable(R.drawable.divider));

        listOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("path", "regional/" + getResources().
                        getStringArray(R.array.region_paths)[position]);
                bundle.putString("from", "fragment");
                DialogYears dialogYears = new DialogYears();
                dialogYears.setArguments(bundle);
                dialogYears.show(getActivity().getSupportFragmentManager(), "DialogYears");
            }
        });
        return v;
    }

}
