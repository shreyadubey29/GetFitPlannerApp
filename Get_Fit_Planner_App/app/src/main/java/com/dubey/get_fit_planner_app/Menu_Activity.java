package com.dubey.get_fit_planner_app;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Menu_Activity extends AppCompatActivity {
    private SimpleDateFormat dateFormat;
    private TextView dateDisplay;
    private Calendar calendar;
    private String date;
    private TextView userNameView;
    private TextView balanceCalorieView;
    private DataManager dataManager;
    private List<FitnessData> dataList;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Main Menu");
        dataManager = new DataManager(this);

        // getting view to display
        userNameView = findViewById(R.id.user_name);
        balanceCalorieView = findViewById(R.id.balance_calorie_value);
        dateDisplay = findViewById(R.id.date_display1_value);
        dataList = new ArrayList<FitnessData>();
        userList = new ArrayList<User>();
    }
    // On resume function
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        if(userList.isEmpty())
        {
            Intent mainIntent = new Intent(this,MainActivity.class);
            startActivity(mainIntent);
        }

        // current date display
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        dateDisplay.setText(date);

        //display the user name
        if(!(userList.isEmpty()))
        {
            userNameView.setText(userList.get(userList.size()-1).getFirstname());
        }

        // Getting calorie from table
        FitnessData currData = getObject(date);
        if(currData!=null)
        {
            double calorieIn = Double.parseDouble(currData.getCalorieIn());
            double calorieOut = Double.parseDouble(currData.getCalorieOut());
            String balanceCalorie = Double.toString(calorieIn-calorieOut);
            balanceCalorieView.setText(balanceCalorie);
        }
        else {
            balanceCalorieView.setText("0.0");
            dataManager.insertInitial(date, "0.0", "0", "0.0", "0.0", "0.0", "0.0",
                    "0.0", "0.0", "0.0", "0.0", "0.0", "0.0");
            loadData();
        }

    }

    public void bmiCalculator(View v)
    {
        Intent bmiIntent = new Intent(this,BMI_Activity.class);
        startActivity(bmiIntent);
    }
    public void weightTracker(View v)
    {
        Intent weightIntent = new Intent(this,Weight_Activity.class);
        startActivity(weightIntent);
    }
    public void waterTracker(View v)
    {
        Intent waterIntent = new Intent(this,Water_Activity.class);
        startActivity(waterIntent);
    }
    public void calorieInput(View v)
    {
        Intent calorieInputIntent = new Intent(this,CalorieIntake_Activity.class);
        startActivity(calorieInputIntent);
    }
    public void calorieBurn(View v)
    {
        Intent calorieOutputIntent = new Intent(this,CalorieBurn_Activity.class);
        startActivity(calorieOutputIntent);
    }
    public void logDetails(View v)
    {
        Intent logDetailIntent = new Intent(this,LogDetailsActivity.class);
        startActivity(logDetailIntent);
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
        if (id == R.id.action_account)
        {
            Intent mainIntent = new Intent(this,MainActivity.class);
            startActivity(mainIntent);
        }
        return super.onOptionsItemSelected(item);
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

        Cursor userCursor = dataManager.getUser();
        if(userCursor.getCount()>0)
        {
            userList.clear();
            while (userCursor.moveToNext())
            {
                int userId = userCursor.getInt(0);
                String firstname = userCursor.getString(1);
                String lastname = userCursor.getString(2);
                String age = userCursor.getString(3);
                String height = userCursor.getString(4);
                String weight = userCursor.getString(5);
                String gender = userCursor.getString(6);

                User data2 = new User(userId,firstname,lastname,age,height,weight,gender);
                userList.add(data2);

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



}
