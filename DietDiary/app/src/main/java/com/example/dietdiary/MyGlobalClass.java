package com.example.dietdiary;

import android.app.Application;

public class MyGlobalClass extends Application {
    private int globalSteps;
    public int getData()
    {
        return globalSteps;
    }
    public void setData(int steps)
    {
        this.globalSteps = steps;
    }
    private static MyGlobalClass instance = null;

    public static synchronized MyGlobalClass getInstance(){
        if(null == instance){
            instance = new MyGlobalClass();
        }
        return instance;
    }
}
