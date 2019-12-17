package com.dubey.get_fit_planner_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class BMI_Activity extends AppCompatActivity {

    private EditText heightView;
    private EditText weightView;
    private TextView bmiView;
    private TextView bmiResultView;
    private ImageView normal;
    private ImageView under;
    private ImageView over;
    private ImageView obese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        heightView = findViewById(R.id.height_value);
        weightView = findViewById(R.id.weight_value);
        bmiView = findViewById(R.id.bmi_value);
        bmiResultView = findViewById(R.id.bmi_result);
        normal = findViewById(R.id.normal);
        under = findViewById(R.id.underweight);
        over = findViewById(R.id.overweight);
        obese = findViewById(R.id.obese);

        setTitle("BMI Calculator");

        heightView.setText("0.0");
        weightView.setText("0.0");
        normal.setVisibility(View.INVISIBLE);
        under.setVisibility(View.INVISIBLE);
        over.setVisibility(View.INVISIBLE);
        obese.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void calculateBMI (View v)
    {
        double height = Double.parseDouble(heightView.getText().toString());;
        double weight = Double.parseDouble(weightView.getText().toString());;

        if(height!=0.0)
        {
            double bmi = (weight*703)/(height*height);
            bmi = Math.round(bmi * 100D) / 100D;

            String bmiS = Double.toString(bmi);
            String ans1 = "BMI = "+bmiS;
            bmiView.setText(ans1);

            if(bmi<18.5)
            {
                bmiResultView.setText("You are Under weight !");
                obese.setVisibility(View.INVISIBLE);
                over.setVisibility(View.INVISIBLE);
                normal.setVisibility(View.INVISIBLE);
                under.setVisibility(View.VISIBLE);
            }
            else if(bmi<25)
            {
                bmiResultView.setText("You are Normal weight !");
                under.setVisibility(View.INVISIBLE);
                obese.setVisibility(View.INVISIBLE);
                over.setVisibility(View.INVISIBLE);
                normal.setVisibility(View.VISIBLE);
            }
            else if(bmi<30)
            {
                bmiResultView.setText("You are Over weight !");
                under.setVisibility(View.INVISIBLE);
                obese.setVisibility(View.INVISIBLE);
                normal.setVisibility(View.INVISIBLE);
                over.setVisibility(View.VISIBLE);
            }
            else
            {
                bmiResultView.setText("You are Obese !");
                under.setVisibility(View.INVISIBLE);
                over.setVisibility(View.INVISIBLE);
                normal.setVisibility(View.INVISIBLE);
                obese.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            Toast.makeText(this,"Enter valid values",Toast.LENGTH_SHORT).show();
        }


    }

    public void clear (View v)
    {
        heightView.setText("0.0");
        weightView.setText("0.0");
        bmiView.setText("");
        bmiResultView.setText("");

        under.setVisibility(View.INVISIBLE);
        obese.setVisibility(View.INVISIBLE);
        over.setVisibility(View.INVISIBLE);
        normal.setVisibility(View.INVISIBLE);

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
