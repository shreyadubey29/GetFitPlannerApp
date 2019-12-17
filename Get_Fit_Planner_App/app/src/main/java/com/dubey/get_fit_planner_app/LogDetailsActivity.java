package com.dubey.get_fit_planner_app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LogDetailsActivity extends AppCompatActivity {

    private TextView givenDateView;
    private TextView caloriIntakeView;
    private TextView calorieBurnView;
    private TextView calorieBalanceView;
    private TextView waterView;
    private TextView weightView;
    private DataManager dataManager;
    private List<FitnessData> dataList;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private PieChart pieChartBurn;
    private PieChart pieChartEat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        setTitle("Log Details");

        givenDateView = findViewById(R.id.givenDate);
        calorieBalanceView = findViewById(R.id.calorieBalanceSummary);
        calorieBurnView = findViewById(R.id.calorieOutSummary);
        caloriIntakeView = findViewById(R.id.calorieInSummary);
        weightView = findViewById(R.id.weightSummary);
        waterView = findViewById(R.id.waterSummary);
        dataManager = new DataManager(this);
        dataList = new ArrayList<FitnessData>();
        pieChartBurn = findViewById(R.id.piechart2);
        pieChartEat = findViewById(R.id.piechart1);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void showDateChanger(View v)
    {
        ShowDateChange dialog= new ShowDateChange();
        dialog.show(getSupportFragmentManager(),"");
    }

    @Override
    protected void onResume() {
        super.onResume();

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        changeDate(date);
    }

    public  void changeDate(String dateS)
    {
        loadData();
        date = dateS;
        givenDateView.setText(date);

        FitnessData currEntry = getObject(date);
        if(currEntry!=null)
        {
            caloriIntakeView.setText(currEntry.getCalorieIn());
            if(currEntry.getCalorieIn().equalsIgnoreCase("0.0"))
            {
                pieChartEat.setVisibility(View.INVISIBLE);
                if(currEntry.getCalorieOut().equalsIgnoreCase("0.0"))
                {
                    pieChartBurn.setVisibility(View.INVISIBLE);
                }
                else
                {
                    pieChartBurn.setVisibility(View.VISIBLE);
                    makePieChartSecond();
                }
            }
            else
            {
                pieChartEat.setVisibility(View.VISIBLE);
                makePieChart();
                if(currEntry.getCalorieOut().equalsIgnoreCase("0.0"))
                {
                    pieChartBurn.setVisibility(View.INVISIBLE);
                }
                else
                {
                    pieChartBurn.setVisibility(View.VISIBLE);
                    makePieChartSecond();
                }
            }

            calorieBurnView.setText(currEntry.getCalorieOut());
            double balance = Double.parseDouble(currEntry.getCalorieIn())-Double.parseDouble(currEntry.getCalorieOut());
            calorieBalanceView.setText(Double.toString(balance));
            waterView.setText(currEntry.getWaterIn());
            weightView.setText(currEntry.getWeight());
        }
        else
        {
            caloriIntakeView.setText("0.0");
            calorieBurnView.setText("0.0");
            calorieBalanceView.setText("0.0");
            waterView.setText("0.0");
            weightView.setText("0.0");
            pieChartEat.setVisibility(View.INVISIBLE);
            pieChartBurn.setVisibility(View.INVISIBLE);
        }
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
        FitnessData currObject = getObject(date);
        float s1 = Float.parseFloat(currObject.getCardio());
        float s2 = Float.parseFloat(currObject.getStrength());
        float s3 = Float.parseFloat(currObject.getYoga());
        float s4 = Float.parseFloat(currObject.getOther());

        pieChartBurn.setUsePercentValues(true);
        pieChartBurn.getDescription().setEnabled(true);
        pieChartBurn.setExtraOffsets(5,10,5,5);
        pieChartBurn.setDragDecelerationFrictionCoef(0.9f);
        pieChartBurn.setTransparentCircleRadius(61f);
        pieChartBurn.setHoleColor(Color.WHITE);
        pieChartBurn.animateY(1000, Easing.EasingOption.EaseInOutCubic);
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
        pieChartBurn.setData(pieData);
        Description des = new Description();
        des.setText("Calorie Burn distribution");
        des.setTextSize(14f);
        pieChartBurn.setDescription(des);
        //PieChart Ends Here
    }

    public void makePieChartSecond()
    {
        FitnessData currData = getObject(date);
        float s1 = Float.parseFloat(currData.getBreakfast());
        float s2 = Float.parseFloat(currData.getLunch());
        float s3 = Float.parseFloat(currData.getDinner());
        float s4 = Float.parseFloat(currData.getSnacks());

        pieChartEat.setUsePercentValues(true);
        pieChartEat.getDescription().setEnabled(true);
        pieChartEat.setExtraOffsets(5,10,5,5);
        pieChartEat.setDragDecelerationFrictionCoef(0.9f);
        pieChartEat.setTransparentCircleRadius(61f);
        pieChartEat.setHoleColor(Color.WHITE);
        pieChartEat.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        ArrayList<PieEntry> yValues = new ArrayList<>();
        if(s1!=0)
        {
            yValues.add(new PieEntry(s1,"BREAKFAST"));
        }
        if(s2!=0)
        {
            yValues.add(new PieEntry(s2,"LUNCH"));
        }
        if(s3!=0)
        {
            yValues.add(new PieEntry(s3,"DINNER"));
        }
        if(s4!=0)
        {
            yValues.add(new PieEntry(s4,"SNACKS"));
        }

        PieDataSet dataSet = new PieDataSet(yValues, "Types of Meals");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData((dataSet));
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.YELLOW);
        pieChartEat.setData(pieData);
        Description des = new Description();
        des.setText("Calorie Intake Distribution");
        des.setTextSize(14f);
        pieChartEat.setDescription(des);
        //PieChart Ends Here
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
        //if (id == R.id.action_settings) {
        //   return true;
        //}
        if(id == R.id.action_account)
        {
            Intent mainIntent = new Intent(this,MainActivity.class);
            startActivity(mainIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
