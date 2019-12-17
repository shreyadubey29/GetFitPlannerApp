package com.dubey.get_fit_planner_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText firstNameView;
    private  EditText lastNameView;
    private EditText ageView;
    private EditText heightView;
    private  EditText weightView;
    private RadioButton choice1View;
    private RadioButton choice2View;
    private DataManager dataManager;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Get Fit Planner");

        firstNameView = findViewById(R.id.first_value);
        lastNameView = findViewById(R.id.last_value);
        ageView = findViewById(R.id.age_value);
        heightView = findViewById(R.id.height_value);
        weightView = findViewById(R.id.weight_value);
        choice1View = findViewById(R.id.male_button);
        choice2View = findViewById(R.id.female_button);

        userList = new ArrayList<User>();

        dataManager = new DataManager(this);

        //Setting default values
        ageView.setText("0");
        heightView.setText("0.0");
        weightView.setText("0.0");

    }

    public void onSubmit(View v)
    {
        String firstName = firstNameView.getText().toString();
        String lastName = lastNameView.getText().toString();
        double height = Double.parseDouble(heightView.getText().toString());
        double weight = Double.parseDouble(weightView.getText().toString());
        int age = Integer.parseInt(ageView.getText().toString());
        String gender;
        if(choice1View.isChecked())
        {
            gender = "male";
        }
        else if(choice2View.isChecked())
        {
            gender = "female";
        }
        else
        {
            gender = "other";
        }
        dataManager.insertUserDetails(firstName,lastName,Double.toString(weight),Double.toString(height),Integer.toString(age),gender);
        loadUser();
        Intent menuIntent = new Intent(this,Menu_Activity.class);
        startActivity(menuIntent);
    }

    public void onSkip(View v)
    {
        Intent menuIntent = new Intent(this,Menu_Activity.class);
        startActivity(menuIntent);
    }

    public void loadUser()
    {
        Cursor userCursor =dataManager.getUser();
        if(userCursor.getCount()>0)
        {
            userList.clear();
            while (userCursor.moveToNext())
            {
                int id = userCursor.getInt(0);
                String firstname = userCursor.getString(1);
                String lastname = userCursor.getString(2);
                String age = userCursor.getString(3);
                String height = userCursor.getString(4);
                String weight = userCursor.getString(5);
                String gender = userCursor.getString(6);

                User user1 = new User(id,firstname,lastname,age,height,weight,gender);
                userList.add(user1);
                Log.i("info","name = "+id+" "+firstname);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUser();

        if(!(userList.isEmpty()))
        {
            int k = userList.size()-1;
            firstNameView.setText(userList.get(k).getFirstname());
            lastNameView.setText(userList.get(k).getLastname());
            ageView.setText(userList.get(k).getAge());
            heightView.setText(userList.get(k).getHeight());
            weightView.setText(userList.get(k).getWeight());
            if(userList.get(k).getGender().equalsIgnoreCase("male"))
            {
                choice1View.setChecked(true);
            }
            else if(userList.get(k).getGender().equalsIgnoreCase("female"))
            {
                choice2View.setChecked(true);
            }
            else
            {
                choice1View.setChecked(false);
                choice2View.setChecked(false);
            }
        }
    }
}
