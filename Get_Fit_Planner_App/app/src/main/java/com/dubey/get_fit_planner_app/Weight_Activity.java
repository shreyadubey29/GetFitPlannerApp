package com.dubey.get_fit_planner_app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Weight_Activity extends AppCompatActivity {

    private TextView dateDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private String date1;
    private DataManager dataManager;
    private TextView currWeightView;
    private EditText newWeightView;
    private List<FitnessData> dataList;
    private BarChart chart;
    //private LineChart chart;
    private List<String> dates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        dataManager = new DataManager(this);
        dataList = new ArrayList<FitnessData>();
        dateDisplay = findViewById(R.id.dateDisplay);
        currWeightView = findViewById(R.id.currentWeightValue);
        newWeightView = findViewById(R.id.newWeight_value);
        chart = findViewById(R.id.weight_chart);
        setTitle("Weight Tracker");
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
        if(id == R.id.action_account)
        {
            Intent mainIntent = new Intent(this,MainActivity.class);
            startActivity(mainIntent);
        }
        return super.onOptionsItemSelected(item);
    }
    // For back button
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    // Function for Add button
    public void addWeight(View v)
    {
        String newEntryS = newWeightView.getText().toString();
        dataManager.updateWeight(date,newEntryS);
        loadData();
        currWeightView.setText(newEntryS);
        newWeightView.setText("0.0");
        makeChart();
    }

    // For clear button
    public void onClear(View v)
    {
        newWeightView.setText("0.0");
    }

    // Function for bar chart
    public void makeChart()
    {
        loadData();
        //LineData data = new LineData(getDataSet());
        BarData data = new BarData(getDataSet());
        chart.setData(data);
        Description des = new Description();
        des.setText("");
        chart.setDescription(des);
        chart.setDrawGridBackground(false);
        chart.animateXY(2000, 2000);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return dates.get((int)value-1).substring(0,5);
            }
        });

        YAxis yAxis = chart.getAxisRight();
        yAxis.setDrawLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12f);
        Legend l = chart.getLegend();
        l.setTextSize(15f);
        chart.invalidate();
    }

    private ArrayList getDataSet() {

        dates = new ArrayList<String>();
        for(int i =0;i>-7;i--)
        {
            calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,i);
            dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            date1 = dateFormat.format(calendar.getTime());
            dates.add(date1);
        }

        ArrayList dataSets = null;
        ArrayList valueSet1 = new ArrayList();

        for(int j = dates.size();j>0;j--)
        {
            FitnessData obj1 = getObject(dates.get(j-1));
            if(obj1!=null)
            {
                float w = Float.parseFloat(obj1.getWeight());
                //Entry v1e1 = new Entry((j),w ); // Jan
                BarEntry v1e1 = new BarEntry((j),w ); // Jan
                valueSet1.add(v1e1);
            }
            else
            {
                //Entry v1e1 = new Entry((j),0f ); // Jan
                BarEntry v1e1 = new BarEntry((j),0f ); // Jan
                valueSet1.add(v1e1);
            }
        }
        //LineDataSet barDataSet1 = new LineDataSet(valueSet1, "Weights (lbs)");
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Weights (lbs)");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        barDataSet1.setValueTextSize(15f);
        dataSets = new ArrayList();
        dataSets.add(barDataSet1);
        return dataSets;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        dateDisplay.setText(date);

        FitnessData currData = getObject(date);
        if(currData!=null) {
            currWeightView.setText(currData.getWeight());
        }
        else {
            currWeightView.setText("0.0");
        }
        newWeightView.setText("0.0");
        makeChart();
    }

    public FitnessData getObject(String date)
    {
        if(dataList.isEmpty()) {
            return null;
        }
        else {
            for(int i =0;i<dataList.size();i++) {
                if(dataList.get(i).getDate().equals(date)) {
                    return dataList.get(i);
                }
            }
        }
        return null;
    }

    // Loading the data
    public void loadData()
    {
        Cursor cursor = dataManager.getEntry();
        if(cursor.getCount()>0)
        {
            dataList.clear();
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
}
