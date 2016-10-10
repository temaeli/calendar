package tz.co.wadau.calenderapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

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

        ArrayList<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.75f);
        barChart.setData(barData);
        barChart.setDescription("");
        barChart.animateY(1000);
        barChart.setFitBars(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.cycle_history, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        switch (id){
            case R.id.nav_calendar :

                startActivity(new Intent(this, CalendarActivity.class));
                finish();
                break;
            case R.id.nav_cycle_history:
                startActivity(new Intent(this, CycleHistoryActivity.class));
                finish();
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                break;
            case R.id.nav_help:
                startActivity(new Intent(this, HelpActivity.class));
                finish();
                break;
        }

        return true;
    }
}
