package com.dubey.get_fit_planner_app;

public class FitnessData {
    private String date;
    private String weight;
    private String waterIn;
    private String calorieIn;
    private String calorieOut;
    private String breakfast;
    private String lunch;
    private String dinner;
    private String snacks;
    private String cardio;
    private String strength;
    private String yoga;
    private String other;

    public FitnessData(String date, String weight, String waterIn, String calorieIn, String calorieOut, String breakfast,
            String lunch, String dinner, String snacks, String cardio, String strength, String yoga, String other)
    {
        this.date = date;
        this.weight = weight;
        this.waterIn = waterIn;
        this.calorieIn = calorieIn;
        this.calorieOut = calorieOut;
        this.breakfast = breakfast;
        this.lunch =lunch;
        this.dinner = dinner;
        this.snacks = snacks;
        this.cardio = cardio;
        this.strength = strength;
        this.yoga = yoga;
        this.other = other;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public void setCalorieIn(String calorieIn) {
        this.calorieIn = calorieIn;
    }

    public void setCalorieOut(String calorieOut) {
        this.calorieOut = calorieOut;
    }

    public void setCardio(String cardio) {
        this.cardio = cardio;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public void setSnacks(String snacks) {
        this.snacks = snacks;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public void setWaterIn(String waterIn) {
        this.waterIn = waterIn;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public void setYoga(String yoga) {
        this.yoga = yoga;
    }

    public String getWeight() {
        return weight;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public String getCalorieIn() {
        return calorieIn;
    }

    public String getCalorieOut() {
        return calorieOut;
    }

    public String getCardio() {
        return cardio;
    }

    public String getDate() {
        return date;
    }

    public String getDinner() {
        return dinner;
    }

    public String getLunch() {
        return lunch;
    }

    public String getSnacks() {
        return snacks;
    }

    public String getStrength() {
        return strength;
    }

    public String getOther() {
        return other;
    }

    public String getWaterIn() {
        return waterIn;
    }

    public String getYoga() {
        return yoga;
    }
}
