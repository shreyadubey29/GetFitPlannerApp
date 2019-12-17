package com.dubey.get_fit_planner_app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalorieBurn_Activity extends AppCompatActivity {
    private SimpleDateFormat dateFormat;
    private TextView dateDisplay;
    private Calendar calendar;
    private String date;
    private String date1;
    private List<String> dates;
    private DataManager dataManager;
    private TextView currCalorieBurn;
    private EditText addCalorieBurn;
    private List<FitnessData> dataList;
    private FitnessData currObject;
    private  double currCalorieOut;
    private RadioButton cardioView;
    private RadioButton strengthView;
    private RadioButton yogaView;
    private PieChart pieChart;
    private BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_burn_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        // current date display
        dateDisplay = findViewById(R.id.date3Display_value);
        dataManager = new DataManager(this);
        currCalorieBurn = findViewById(R.id.currCalorie_value);
        addCalorieBurn = findViewById(R.id.addCalorie_value);
        cardioView = findViewById(R.id.cardio);
        strengthView = findViewById(R.id.strength);
        yogaView = findViewById(R.id.yoga);
        pieChart = findViewById(R.id.chart1);
        dataList = new ArrayList<FitnessData>();
        chart = findViewById(R.id.barchart);
        setTitle("Calories Burned");
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
        if (id == R.id.action_account){
            Intent menuIntent = new Intent(this,MainActivity.class);
            startActivity(menuIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    //Resume function
    @Override
    protected void onResume() {
        super.onResume();
        loadData();

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        dateDisplay.setText(date);
        currObject = getObject(date);

        if(currObject!=null) {
            String calorieOutS = currObject.getCalorieOut();
            currCalorieBurn.setText(calorieOutS);
            currCalorieOut = Double.parseDouble(calorieOutS);
        }
        else {
            currCalorieBurn.setText("0.0");
            currCalorieOut = 0.0;
        }
        addCalorieBurn.setText("0.0");
        makePieChart();
        makeBarChart();
    }

    // Add button function
    public void addCalorieBurn(View v)
    {
        String additionalBurnCalorie = addCalorieBurn.getText().toString();
        currCalorieOut = currCalorieOut+(Double.parseDouble(additionalBurnCalorie));
        String currCalorieOutS = Double.toString(currCalorieOut);
        dataManager.updateCalorieOut(date,currCalorieOutS);
        double newCalorieOut = 0.0;
        if(cardioView.isChecked()) {
            newCalorieOut = Double.parseDouble(additionalBurnCalorie)+Double.parseDouble(currObject.getCardio());
            dataManager.updateWorkoutType(Double.toString(newCalorieOut),"cardio",date);
        }
        else if(strengthView.isChecked()) {
            newCalorieOut = Double.parseDouble(additionalBurnCalorie)+Double.parseDouble(currObject.getStrength());
            dataManager.updateWorkoutType(Double.toString(newCalorieOut),"strength",date);
        }
        else if(yogaView.isChecked()) {
            newCalorieOut = Double.parseDouble(additionalBurnCalorie)+Double.parseDouble(currObject.getYoga());
            dataManager.updateWorkoutType(Double.toString(newCalorieOut),"yoga",date);
        }
        else {
            newCalorieOut = Double.parseDouble(additionalBurnCalorie)+Double.parseDouble(currObject.getOther());
            dataManager.updateWorkoutType(Double.toString(newCalorieOut),"other",date);
        }
        loadData();
        currCalorieBurn.setText(getObject(date).getCalorieOut());
        addCalorieBurn.setText("0.0");
        makePieChart();
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

    public void makePieChart()
    {
        currObject = getObject(date);
        float s1 = Float.parseFloat(currObject.getCardio());
        float s2 = Float.parseFloat(currObject.getStrength());
        float s3 = Float.parseFloat(currObject.getYoga());
        float s4 = Float.parseFloat(currObject.getOther());

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(true);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        ArrayList<PieEntry> yValues = new ArrayList<>();
        if(s1!=0)
        {
            yValues.add(new PieEntry(s1,"CARDIO"));
        }
        if(s2!=0)
        {
            yValues.add(new PieEntry(s2,"STRENGTH"));
        }
        if(s3!=0)
        {
            yValues.add(new PieEntry(s3,"YOGA"));
        }
        if(s4!=0)
        {
            yValues.add(new PieEntry(s4,"OTHER"));
        }

        PieDataSet dataSet = new PieDataSet(yValues, "Types of Exercises");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData((dataSet));
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.YELLOW);
        pieChart.setData(pieData);
        Description des = new Description();
        des.setText("Today's distribution");
        des.setTextSize(15f);
        pieChart.setDescription(des);
        //PieChart Ends Here
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    // Function for bar chart
    public void makeBarChart()
    {
        loadData();
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
                float w = Float.parseFloat(obj1.getCalorieOut());
                BarEntry v1e1 = new BarEntry((j),w );
                valueSet1.add(v1e1);
            }
            else
            {
                BarEntry v1e1 = new BarEntry((j),0f );
                valueSet1.add(v1e1);
            }
        }
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Calories (kcal)");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        barDataSet1.setValueTextSize(15f);
        dataSets = new ArrayList();
        dataSets.add(barDataSet1);
        return dataSets;
    }
}
