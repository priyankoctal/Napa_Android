package com.octalsoftaware.archi.models;

import java.util.ArrayList;

/**
 * Created by anandj on 4/27/2017.
 */

public class HomePageDaysModal {
    String days_name;
    ArrayList<HomePageModal> homePageModals;

    public String getDays_name() {
        return days_name;
    }

    public void setDays_name(String days_name) {
        this.days_name = days_name;
    }

    public ArrayList<HomePageModal> getHomePageModals() {
        return homePageModals;
    }

    public void setHomePageModals(ArrayList<HomePageModal> homePageModals) {
        this.homePageModals = homePageModals;
    }
}
