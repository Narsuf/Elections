package com.jorgedguezm.elections;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jorge on 14/11/16.
 */

public class LineChartFragment extends Fragment {
    private LineChart chart;
    private String path;
    private String[] files;
    private ArrayList<String> colors = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private Bundle bundle = new Bundle();
    private int index = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.line_chart, container, false);

        //Saves all the files in the path to the string array
        path = getArguments().getString("path");

        try {
            files = getResources().getAssets().list(path);

        } catch (IOException e) {
            e.printStackTrace();
        }

        chart = (LineChart) v.findViewById(R.id.line_chart);
        chart.setDescription("");
        chart.setTouchEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setVisibleXRange(0, files.length);
        chart.animateX(1500);

        LineData data = new LineData();
        chart.setData(data);

        chart.getLegend().setEnabled(false);

        XAxis x1 = chart.getXAxis();
        x1.setTextSize(12f);
        x1.setTextColor(Color.GRAY);
        x1.setDrawGridLines(false);
        x1.setPosition(XAxis.XAxisPosition.BOTTOM);
        x1.setAvoidFirstLastClipping(true);

        YAxisValueFormatter formatter = new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return String.valueOf(((int) value) + "%");

            }
        };
        YAxis y1 = chart.getAxisLeft();
        y1.setTextColor(Color.GRAY);
        y1.setValueFormatter(formatter);
        y1.setDrawGridLines(true);
        y1.setDrawAxisLine(false);
        y1.setAxisMaxValue(100);
        y1.setTextSize(12f);

        YAxis y12 = chart.getAxisRight();
        y12.setEnabled(false);

        //Reads every json
        for (int i = 0; i < files.length; i++) {
            JSONReader jsonReader = new JSONReader(getContext());
            jsonReader.loadJSON(path + "/" + files[i]);
            data.addXValue(String.valueOf(jsonReader.getInstitution().getYear()));

            //Adds one entry for each party
            for (Party one : jsonReader.getParties()) {
                addEntry(one.getName(), one.getColor(), one.getPercent().floatValue(), i);
            }
        }

        y1.setAxisMaxValue(new BigDecimal(data.getYMax()).round(new MathContext(1, RoundingMode.UP))
                .floatValue());

        //ListView
        String[] from = {"color", "partyName"};
        int[] to = {R.id.tvPartyColorHistoric, R.id.tvPartyNameHistoric};
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        for (int i = 0; i < this.colors.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put(from[0], this.colors.get(i));
            map.put(from[1], this.names.get(i));
            arrayList.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(getContext(), arrayList,
                R.layout.list_item_historic, from, to) {
            @Override
            public boolean isEnabled(int position) {
                return false;
            }
        };

        adapter.setViewBinder(new PartyColorBinder());
        ListView listOptions = (ListView) v.findViewById(R.id.listViewLineChart);
        listOptions.setAdapter(adapter);

        chart.notifyDataSetChanged();

        return v;
    }

    private void addEntry(String partyName, String partyColor, float votePercent,
                          int count) {
        LineData data = chart.getData();

        if (data != null) {
            LineDataSet set = (LineDataSet) data.getDataSetByIndex(bundle.getInt(partyColor) - 1);

            //In case set is null because there wasn't data in the bundle, it's created here
            if (set == null) {
                set = createSet(partyName, partyColor);
                data.addDataSet(set);
                bundle.putInt(partyColor, index + 1);
                index++;
                colors.add(partyColor);
                names.add(partyName);
            }

            /* As the party name isn't the id (the color is) we want it to change every year
            just in case the party name has changed */
            set.setLabel(partyName);
            /* We want to save the names in an ArrayList so we can show them next to the color
            in the legend */
            names.set(bundle.getInt(partyColor) - 1, partyName);
            data.addEntry(new Entry(votePercent, count), bundle.getInt(partyColor) - 1);
        }
    }

    private LineDataSet createSet(String partyName, String partyColor) {
        LineDataSet set = new LineDataSet(null, partyName);
        set.setColor(Color.parseColor(partyColor));
        set.setCircleColor(Color.parseColor(partyColor));
        set.setLineWidth(2.5f);
        set.setCircleRadius(5f);
        set.setFillColor(Color.parseColor(partyColor));
        set.setValueTextColor(Color.GRAY);
        set.setValueTextSize(0f);
        return set;
    }
}
