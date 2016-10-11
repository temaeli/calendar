package tz.co.wadau.calenderapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import tz.co.wadau.calenderapp.customviews.MCUtils;

public class CycleHistoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.cycle_history_toolbar);
        setSupportActionBar(toolbar);

        TextView avgPeriodDays = (TextView) findViewById(R.id.avg_period_days);
        TextView avgCycleDays = (TextView) findViewById(R.id.av_cycle_days);

        MCUtils.animateTextView(0, 4, 1000, avgPeriodDays);
        MCUtils.animateTextView(0, 30, 1000, avgCycleDays);

        BarChart barChart = (BarChart) findViewById(R.id.bar_chart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 10));
        entries.add(new BarEntry(1, 18));
        entries.add(new BarEntry(2, 12));
        entries.add(new BarEntry(3, 8));
        entries.add(new BarEntry(4, 17));
        entries.add(new BarEntry(5, 5));

        BarDataSet barDataSet = new BarDataSet(entries, "Cycle days");

        final ArrayList<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.75f);
        barChart.setDescription("");
        barChart.animateY(1000);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.invalidate();

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int)value);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

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
                int id = item.getItemId();

                switch (id){
                    case R.id.nav_calendar :
                        startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                        finish();
                        break;
                    case R.id.nav_cycle_history:
                        startActivity(new Intent(getApplicationContext(), CycleHistoryActivity.class));
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        break;
                    case R.id.nav_help:
                        startActivity(new Intent(getApplicationContext(), HelpActivity.class));
                        break;
                }
            }
        }, 300);

        return true;
    }
}
