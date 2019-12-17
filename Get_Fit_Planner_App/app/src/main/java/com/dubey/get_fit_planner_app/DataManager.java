package com.dubey.get_fit_planner_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataManager {

    private SQLiteDatabase db;

    public DataManager (Context context)
    {
        MySQLiteOpenHelper helper =new MySQLiteOpenHelper (context);
        db = helper.getWritableDatabase();
    }
    // Update weight in table
    public void updateWeight(String date, String weight)
    {
        Log.i("info","Inside update query");
        String query = "update fitness set weight = '"+weight+"' where date = '"+date+"'";

        db.execSQL(query);
    }

    //Update calorieOut
    public void updateCalorieOut(String date, String calorie )
    {
        String query = "update fitness set calorieOut = '"+calorie+ "' where date = '"+date+"'";
        db.execSQL(query);
    }

    //Updating exercise type
    public void updateWorkoutType(String additional, String type, String date)
    {
        String query1 = "";
        if(type.equals("cardio"))
        {
            query1 = "update fitness set cardio = '"+additional+"' where date = '"+date+"'";
        }
        else if(type.equals("strength"))
        {
            query1 = "update fitness set strength = '"+additional+"' where date = '"+date+"'";
        }
        else if(type.equals("yoga"))
        {
            query1 = "update fitness set yoga = '"+additional+"' where date = '"+date+"'";
        }
        else
        {
            query1 = "update fitness set other = '"+additional+"' where date = '"+date+"'";
        }
        db.execSQL(query1);
    }

    //Update calorieIn
    public void updateCalorieIn(String date, String calorie )
    {
        String query = "update fitness set calorieIn = '"+calorie+ "' where date = '"+date+"'";
        db.execSQL(query);

    }

    public void updateMealType(String additional, String type, String date)
    {
        String query1 = "";
        if(type.equals("breakfast"))
        {
            query1 = "update fitness set breakfast = '"+additional+"' where date = '"+date+"'";
        }
        else if(type.equals("lunch"))
        {
            query1 = "update fitness set lunch = '"+additional+"' where date = '"+date+"'";
        }
        else if(type.equals("dinner"))
        {
            query1 = "update fitness set dinner = '"+additional+"' where date = '"+date+"'";
        }
        else
        {
            query1 = "update fitness set snacks = '"+additional+"' where date = '"+date+"'";
        }
        db.execSQL(query1);
    }

    //Update water intake
    public void updateWater(String date, String waterIn)
    {
        String query = "update fitness "+
                        "set waterIn = '"+waterIn+
                        "' where date = '"+date+"'";

        db.execSQL(query);
    }

    public Cursor getUser()
    {
        String query = "select * from user";
        return db.rawQuery(query,null);
    }

    public void insertUserDetails(String firstname, String lastname, String weight, String height, String age, String gender)
    {
        String query;
        query = "insert into user"+
                    "(firstname, lastname, age, height, weight, gender)values"+
                    "('"+firstname+"', '"+lastname+"', '"+age+"', '"+height+"', '"+weight+"', '"+gender+"')";
        db.execSQL(query);
    }

    public Cursor getEntry()
    {
        String query = "select * from fitness";
        return db.rawQuery(query,null);
    }

    public void insertInitial(String date, String weight, String waterIn, String calorieIn, String calorieOut, String breakfast,
                              String lunch, String dinner, String snacks, String cardio, String strength, String yoga, String other)
    {
            String query = "insert into fitness"+
                    "(date, weight, waterIn, calorieIn, calorieOut, breakfast, lunch, dinner, snacks, cardio, strength, yoga, other)values"+
                    "('"+date+"', '"+weight+"', '"+waterIn+"', '"+calorieIn+"', '"+calorieOut+"', '"+breakfast+"', '"+
                    lunch+"', '"+dinner+"', '"+snacks+"', '"+cardio+"', '"+strength+"', '"+yoga+"', '"+other+"')";
            db.execSQL(query);
    }

    public void deleteUser()
    {
        String query = "delete from user";
        db.execSQL(query);
    }

    private class MySQLiteOpenHelper extends SQLiteOpenHelper{

        public MySQLiteOpenHelper (Context context)
        {
            super(context,"getFit",null,5);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            String newTable ="CREATE TABLE fitness ("
                    +"_id integer PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    +"date TEXT NOT NULL, "
                    +"weight TEXT, "
                    +"waterIn TEXT, "
                    +"calorieIn TEXT, "
                    +"calorieOut TEXT, "
                    +"breakfast TEXT, "
                    +"lunch TEXT, "
                    +"dinner TEXT, "
                    +"snacks TEXT, "
                    +"cardio TEXT, "
                    +"strength TEXT, "
                    +"yoga TEXT, "
                    +"other TEXT)";
            sqLiteDatabase.execSQL(newTable);

            String newTable1 ="CREATE TABLE user ("
                    +"_id integer PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    +"firstname TEXT, "
                    +"lastname TEXT, "
                    +"age TEXT, "
                    +"height TEXT, "
                    +"weight TEXT, "
                    +"gender TEXT)";
            sqLiteDatabase.execSQL(newTable1);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            String query1 = "drop table fitness";
            sqLiteDatabase.execSQL(query1);
            String newTable ="CREATE TABLE fitness ("
                    +"_id integer PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    +"date TEXT NOT NULL, "
                    +"weight TEXT, "
                    +"waterIn TEXT, "
                    +"calorieIn TEXT, "
                    +"calorieOut TEXT, "
                    +"breakfast TEXT, "
                    +"lunch TEXT, "
                    +"dinner TEXT, "
                    +"snacks TEXT, "
                    +"cardio TEXT, "
                    +"strength TEXT, "
                    +"yoga TEXT, "
                    +"other TEXT)";
            sqLiteDatabase.execSQL(newTable);
        }
    }
}
