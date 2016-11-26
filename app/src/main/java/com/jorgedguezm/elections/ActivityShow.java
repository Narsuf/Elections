package com.jorgedguezm.elections;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by jorge on 14/11/16.
 */

public class ActivityShow extends AppCompatActivity {
    private String congress;
    private String senate;
    private String from;
    private String path;
    Bundle bundle = new Bundle();
    ChartFragment chartFragment;
    LineChartFragment lineChartFragment;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        from = getIntent().getStringExtra("from");
        //Checks from where has been called this Activity
        if (this.from.equals("general")) {
            congress = getIntent().getStringExtra("congress");
            senate = getIntent().getStringExtra("senate");
            path = congress;
            beginTransaction();

        } else if (this.from.equals("regional")) {
            path = getIntent().getStringExtra("path");
            beginTransaction();

        } else if (this.from.equals("activity")) {
            path = getIntent().getStringExtra("path");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            beginHistoricalTransaction();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean b = false;
        if (from.equals("general")) {
            getMenuInflater().inflate(R.menu.menu_congress_senate, menu);
            b = true;
        } else if (from.equals("regional")) {
            getMenuInflater().inflate(R.menu.menu_change_year, menu);
            b = true;
        }
        return b;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Checks the current value of the path variable before making the transaction
        if (id == R.id.menu_option_congress && path != congress) {
            path = congress;
            beginTransaction();
            return true;
        } else if (id == R.id.menu_option_senate && path != senate) {
            path = senate;
            beginTransaction();
            return true;
        } else if (id == R.id.menu_option_change_year) {
            Bundle bundle = new Bundle();
            bundle.putString("path", path.substring(0, path.length() - 5));
            bundle.putString("from", "activity");
            DialogYears dialogYears = new DialogYears();
            dialogYears.setArguments(bundle);
            dialogYears.show(getSupportFragmentManager(), "DialogYears");
        } else if (id == R.id.menu_option_historic) {
            Intent intent = new Intent(this, ActivityShow.class);
            intent.putExtra("path", path.substring(0, path.length() - 5));
            intent.putExtra("from", "activity");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void beginTransaction() {
        bundle.putString("path", path);
        chartFragment = new ChartFragment();
        chartFragment.setArguments(bundle);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.show_content, chartFragment);
        transaction.commit();
    }

    public void beginHistoricalTransaction() {
        bundle.putString("path", path);
        lineChartFragment = new LineChartFragment();
        lineChartFragment.setArguments(bundle);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.show_content, lineChartFragment);
        transaction.commit();
    }
}
