package tz.co.wadau.calenderapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import tz.co.wadau.calenderapp.customviews.MCUtils;
import tz.co.wadau.calenderapp.helper.MyCycleDbHelper;

public class CycleHistoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final  String TAG = CycleHistoryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.cycle_history_toolbar);
        setSupportActionBar(toolbar);

        MyCycleDbHelper db = new MyCycleDbHelper(this);

        TextView avgPeriodDays = (TextView) findViewById(R.id.avg_period_days);
        TextView avgCycleDays = (TextView) findViewById(R.id.av_cycle_days);

        MCUtils.animateTextView(0, 4, 1000, avgPeriodDays);
        MCUtils.animateTextView(0, 30, 1000, avgCycleDays);

        loadBarGraph();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        //Pause for 300ms till navigation drawer is closed
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Handle navigation view item clicks here.
                Context context = getApplicationContext();
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_calendar:
                        startActivity(new Intent(context, CalendarActivity.class));
                        finish();
                        break;
                    case R.id.nav_cycle_history:
                        startActivity(new Intent(context, CycleHistoryActivity.class));
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(context, SettingsActivity.class));
                        break;
                    case R.id.nav_help:
                        startActivity(new Intent(context, HelpActivity.class));
                        break;
                }
            }
        }, 300);

        return true;
    }


    public void loadBarGraph() {
        MyCycleDbHelper db = new MyCycleDbHelper(getApplicationContext());
        BarChart barChart = (BarChart) findViewById(R.id.bar_chart);

        Log.d(TAG, String.valueOf(db.getYCycleHistory()));

        List<String> yValues = db.getYPeriodHistory();
        List<Long> yCycleHistory = db.getYCycleHistory();
        final List<String> xValues = db.getXPeriodHistory();
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i=0; i < yValues.size(); i++){
            entries.add(new BarEntry(i, new float[] { Float.parseFloat(yValues.get(i)), yCycleHistory.get(i)}));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Cycle days");
        barDataSet.setColors(new int[] { Color.parseColor("#4DD0E1"), Color.parseColor("#8ceaff")});
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.8f);
        barChart.setDescription("");
        barChart.animateY(1000);
        barChart.setFitBars(true);
        barChart.setTouchEnabled(false);
//        barChart.setHighlightFullBarEnabled(true);
        barChart.setData(barData);
        barChart.invalidate();

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xValues.get((int) value);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

//        LimitLine limitLine = new LimitLine(30f, "Average Cycle days");
//        limitLine.setLineColor(Color.RED);
//        limitLine.setTextSize(10);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinValue(0f);
        leftAxis.setAxisMaxValue(50f);
//        leftAxis.addLimitLine(limitLine);
    }
}
