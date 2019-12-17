package com.dubey.get_fit_planner_app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Water_Activity extends AppCompatActivity {

    private SimpleDateFormat dateFormat;
    private TextView dateDisplay;
    private Calendar calendar;
    private String date;
    private TextView waterView;
    private TextView targetView;
    private int targetNumOfGlasses = 10;
    private int currNumOfGlasses;
    private List<FitnessData> dataList;
    private FitnessData currWater;
    private DataManager dataManager;
    private String currNumOfGlassesS;
    private BarChart barChart;
    private BarData barData;
    private BarDataSet set1;
    private BarDataSet set2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        dataManager = new DataManager(this);
        dateDisplay = findViewById(R.id.date1Display_value);
        waterView = findViewById(R.id.waterIntake_value);
        dataList = new ArrayList<FitnessData>();
        barChart = findViewById(R.id.BarChart_glass);
        setTitle("Today's Water Consumption");
        targetView = findViewById(R.id.targetText);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void addWater(View v)
    {
        currNumOfGlasses++;
        waterView.setText(Integer.toString(currNumOfGlasses));
        dataManager.updateWater(date,Integer.toString(currNumOfGlasses));

        if(currNumOfGlasses>=targetNumOfGlasses)
        {
            targetView.setText("You reached the target !");
        }
        loadData();
        makeBarChart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        dateDisplay.setText(date);

        currWater = getObject(date);
        if(currWater==null)
        {
            currNumOfGlasses = 0;
            currNumOfGlassesS = Integer.toString(currNumOfGlasses);
        }
        else
        {
            currNumOfGlassesS = currWater.getWaterIn();
            currNumOfGlasses = Integer.parseInt(currNumOfGlassesS);
        }

        waterView.setText(currNumOfGlassesS);
        if(currNumOfGlasses>=targetNumOfGlasses)
        {
            targetView.setText("You reached the target !");
        }
        makeBarChart();
    }

    // Loading the data
    public void loadData()
    {
        Cursor cursor = dataManager.getEntry();
        dataList.clear();
        if(cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                String date = cursor.getString(1);
                String weight = cursor.getString(2);
                String waterIn = cursor.getString(3);
                String calorieIn = cursor.getString(4);
                String calorieOut = cursor.getString(5);
                String breakfast = cursor.getString(6);
                String lunch = cursor.getString(7);
                String dinner = cursor.getString(8);
                String snacks = cursor.getString(9);
                String cardio = cursor.getString(10);
                String strength = cursor.getString(11);
                String yoga = cursor.getString(12);
                String other = cursor.getString(13);

                FitnessData data1 =new FitnessData(date,weight,waterIn,calorieIn,calorieOut,breakfast,lunch,dinner,snacks,cardio,strength,yoga,other);
                dataList.add(data1);
            }
        }
    }

    // Find date related data
    public FitnessData getObject(String date)
    {
        if(dataList.isEmpty())
        {
            return null;
        }
        else
        {
            for(int i =0;i<dataList.size();i++)
            {
                if(dataList.get(i).getDate().equals(date))
                {
                    return dataList.get(i);
                }
            }
        }
        return null;
    }

    // Bar chart function
    public void makeBarChart()
    {
        currWater = getObject(date);
        float s2 = Float.parseFloat(currWater.getWaterIn());

        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();

        values1.add(new BarEntry(5f,10));
        values2.add(new BarEntry(6f,s2));

        set1 = new BarDataSet(values1, "Standard Water Consumption");
        set1.setColor(Color.rgb(0, 0, 255));
        set1.setValueTextSize(18f);
        set2 = new BarDataSet(values2, "Your Water Consumption");
        set2.setColor(Color.rgb(0, 155, 0));
        set2.setValueTextSize(18f);
        barData = new BarData(set1,set2);
        barData.setValueFormatter(new LargeValueFormatter());
        barChart.setData(barData);
        barChart.setDrawGridBackground(true);
        barChart.setFitBars(true);
        barChart.animateXY(2000, 2000);

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTextSize(14f);
        l.setXEntrySpace(4f);
        Description des = new Description();
        des.setText("");
        barChart.setDescription(des);
        barChart.setX(5);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(false);
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawLabels(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        if (id == R.id.action_account) {
            Intent menuIntent = new Intent(this, MainActivity.class);
            startActivity(menuIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
