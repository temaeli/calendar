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
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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

        TextView avgPeriodDays = (TextView) findViewById(R.id.avg_period_days);
        TextView avgCycleDays = (TextView) findViewById(R.id.av_cycle_days);

        MCUtils.animateTextView(0, 4, 1000, avgPeriodDays);
        MCUtils.animateTextView(0, 30, 1000, avgCycleDays);

        loadCombinedGraph();

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

    public void loadCombinedGraph() {
        MyCycleDbHelper db = new MyCycleDbHelper(getApplicationContext());
        final List<String> xValues = db.getXPeriodHistory();
        CombinedChart combinedChart = (CombinedChart) findViewById(R.id.combined_chart);

        combinedChart.setDescription("");
        combinedChart.animateY(1000);
        combinedChart.setTouchEnabled(false);
        XAxis xAxis = combinedChart.getXAxis();
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

        YAxis rightAxis = combinedChart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = combinedChart.getAxisLeft();
        leftAxis.setAxisMinValue(0f);


        Legend legend = combinedChart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(generateBarData(db));
        combinedData.setData(generateLineData(db));
        leftAxis.setAxisMaxValue(combinedData.getYMax() + 5f);
        combinedChart.setData(combinedData);
        combinedChart.invalidate();
    }

    private BarData generateBarData(MyCycleDbHelper dbHelper){
        List<Long> yCycleHistory = dbHelper.getYCycleHistory();
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        for(int i=0; i < yCycleHistory.size(); i++){
            barEntries.add(new BarEntry(i, yCycleHistory.get(i)));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Cycle days");
//        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.8f);
        return barData;
    }

    private LineData generateLineData(MyCycleDbHelper dbHelper){
        List<String> yValues = dbHelper.getYPeriodHistory();
        ArrayList<Entry> lineEntries = new ArrayList<>();

        for(int i=0; i < yValues.size(); i++){
            lineEntries.add(new Entry(i, Float.parseFloat(yValues.get(i))));
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Period days");
        LineData lineData = new LineData();
        lineDataSet.setColor(Color.parseColor("#eb9393"));
        lineDataSet.setCircleColor(Color.parseColor("#eb9393"));
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setLineWidth(2.5f);
//        lineDataSet.setValueTextSize(10f);
        lineDataSet.setDrawValues(true);
//        lineDataSet.setValueTextColor(Color.parseColor("#eb9393"));
        lineDataSet.setCircleRadius(5f);
        lineData.addDataSet(lineDataSet);
        return lineData;
    }
}
