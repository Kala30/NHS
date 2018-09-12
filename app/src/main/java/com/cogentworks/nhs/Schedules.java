package com.cogentworks.nhs;

import android.util.Pair;

import java.util.ArrayList;

public class Schedules {

    public static final ArrayList<Pair<String, Integer>> ADVISEMENT;
    static
    {
        ADVISEMENT = new ArrayList<>();
        ADVISEMENT.add(new Pair<>("Period 0", 700));
        ADVISEMENT.add(new Pair<>("Period %1", 800));
        ADVISEMENT.add(new Pair<>("Break", 930));
        ADVISEMENT.add(new Pair<>("Advisement", 945));
        ADVISEMENT.add(new Pair<>("Period %3", 1005));
        ADVISEMENT.add(new Pair<>("Lunch", 1135));
        ADVISEMENT.add(new Pair<>("Period %5", 1215));
        ADVISEMENT.add(new Pair<>("Break", 145));
        ADVISEMENT.add(new Pair<>("Period %7", 1400));
    }

    public static final ArrayList<Pair<String, Integer>> TUTORIAL;
    static
    {
        TUTORIAL = new ArrayList<>();
        TUTORIAL.add(new Pair<>("Period 0", 700));
        TUTORIAL.add(new Pair<>("Period %1", 800));
        TUTORIAL.add(new Pair<>("Break", 925));
        TUTORIAL.add(new Pair<>("Advisement/Tutorial", 940));
        TUTORIAL.add(new Pair<>("Period %3", 1025));
        TUTORIAL.add(new Pair<>("Lunch", 1150));
        TUTORIAL.add(new Pair<>("Period %5", 1230));
        TUTORIAL.add(new Pair<>("Break", 1355));
        TUTORIAL.add(new Pair<>("Period %7", 1405));
    }

    public static final ArrayList<Pair<String, Integer>> COLLABORATION;
    static
    {
        COLLABORATION = new ArrayList<>();
        COLLABORATION.add(new Pair<>("Period 0", 800));
        COLLABORATION.add(new Pair<>("Period %1", 900));
        COLLABORATION.add(new Pair<>("Break", 1020));
        COLLABORATION.add(new Pair<>("Period %3", 1035));
        COLLABORATION.add(new Pair<>("Lunch", 1155));
        COLLABORATION.add(new Pair<>("Period %5", 1235));
        COLLABORATION.add(new Pair<>("Break", 1355));
        COLLABORATION.add(new Pair<>("Period %7", 1410));
    }
}
