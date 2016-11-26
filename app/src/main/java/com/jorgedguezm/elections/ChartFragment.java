package com.jorgedguezm.elections;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jorge on 24/11/16.
 */

public class ChartFragment extends Fragment {
    private ArrayList<Party> parties;
    private String[] partyNames;
    private int[] elects;
    private String[] colors;
    private int[] votesPerParty;
    private BigDecimal[] percentVotesPerParty;
    private Institution institution;
    private Votes votes;
    private PieChart chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pie_chart, container, false);

        FloatingActionButton vFB = (FloatingActionButton) v.findViewById(R.id.votesFloatingButton);
        vFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("institution", institution);
                bundle.putParcelable("votes", votes);
                DialogInfo dialog = new DialogInfo();
                dialog.setArguments(bundle);
                dialog.show(getActivity().getSupportFragmentManager(), "DialogInfo");

            }
        });

        //Creates a JSONReader which internally fills the class fields using the path we give
        JSONReader reader = new JSONReader(getContext());
        reader.loadJSON(getArguments().getString("path"));

        //Initializes the class fields with the reader values
        institution = reader.getInstitution();
        votes = reader.getVotes();
        parties = reader.getParties();
        int sizeArrayList = parties.size();
        partyNames = new String[sizeArrayList];
        elects = new int[sizeArrayList];
        colors = new String[sizeArrayList];
        votesPerParty = new int[sizeArrayList];
        percentVotesPerParty = new BigDecimal[sizeArrayList];

        for (int i = 0; i < sizeArrayList; i++) {
            Party one = parties.get(i);
            partyNames[i] = one.getName();
            elects[i] = one.getElects();
            colors[i] = one.getColor();
            votesPerParty[i] = one.getVotes();
            percentVotesPerParty[i] = one.getPercent();

        }

        //Creates the chart
        chart = (PieChart) v.findViewById(R.id.chart);
        final int[] yData = this.elects;
        final String[] xData = this.partyNames;

        chart.setDescription(null);
        chart.getLegend().setEnabled(false);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColorTransparent(true);
        chart.setHoleRadius(60);
        chart.animateXY(1500, 1500);

        ArrayList<Entry> yVals1 = new ArrayList<>();

        for (int i = 0; i < yData.length; i++)
            yVals1.add(new Entry(yData[i], i));

        ArrayList<String> xVals1 = new ArrayList<>();

        for (int i = 0; i < xData.length; i++)
            xVals1.add(xData[i]);

        PieDataSet dataSet = new PieDataSet(yVals1, null);
        dataSet.setSliceSpace(0.8f);

        ArrayList<Integer> colors = new ArrayList<>();
        for (String one : this.colors) {
            colors.add(Color.parseColor(one));

        }

        dataSet.setColors(colors);

        PieData data = new PieData(xVals1, dataSet);
        data.setValueTextSize(0f);

        chart.setData(data);
        chart.setMaxAngle(180);
        chart.setRotationAngle(180f);
        chart.setTouchEnabled(false);

        chart.invalidate();

        //Creates a SimpleAdapter
        String[] from = {"color", "partyName", "numberVotes", "votesP", "elects"};
        int[] to = {R.id.tvPartyColor, R.id.tvPartyName, R.id.tvNumberVotes,
                R.id.tvVotesP, R.id.tvElects};
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        for (int i = 0; i < this.colors.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put(from[0], this.colors[i]);
            map.put(from[1], this.partyNames[i]);
            map.put(from[2], this.votesPerParty[i]);
            map.put(from[3], this.percentVotesPerParty[i] + "%");
            map.put(from[4], this.elects[i]);
            arrayList.add(map);

        }

        SimpleAdapter adapter = new SimpleAdapter(getContext(), arrayList,
                R.layout.list_item_results, from, to);

        adapter.setViewBinder(new PartyColorBinder());
        ListView listOptions = (ListView) v.findViewById(R.id.listViewPieChart);
        listOptions.setAdapter(adapter);

        final CountDownTimer countDownTimer = new CountDownTimer(3000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                chart.highlightValue(-1, 0);

            }

        };

        listOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chart.highlightValue(position, 0);
                countDownTimer.start();

            }
        });

        return v;
    }

}
