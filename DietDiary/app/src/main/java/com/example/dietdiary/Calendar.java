package com.example.dietdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class Calendar extends AppCompatActivity {

    DatePicker datePicker;
    TextView viewDatePick;
    EditText edtDiary;
    Button btnSave;
    String fileName;

    RadioGroup rGroup;
    RadioButton rdoDiary, rdoToDo, rdoWater, rdoWalk;
    LinearLayout DiaryLayout;
    myDBHelper myDBHelper;
    SQLiteDatabase sqlDB;

    LinearLayout ToDoLayout;
    ViewFlipper viewFlipper;
    LinearLayout WaterLayout;
    LinearLayout WalkLayout;

    TextView steps;

    int m_year=0;
    int m_month=0;
    int m_date=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        datePicker=(DatePicker) findViewById(R.id.datePicker);
        viewDatePick=(TextView) findViewById(R.id.viewDatePick);
        edtDiary=(EditText) findViewById(R.id.edtDiary);
        btnSave=(Button) findViewById(R.id.btnSave);

        rGroup = (RadioGroup) findViewById(R.id.Rgroup);
        rdoDiary = (RadioButton) findViewById(R.id.Diary);
        rdoToDo = (RadioButton) findViewById(R.id.ToDo);
        rdoWater = (RadioButton) findViewById(R.id.Water);
        rdoWalk = (RadioButton) findViewById(R.id.Walk);

        DiaryLayout = (LinearLayout) findViewById(R.id.DiaryLayout);
        ToDoLayout = (LinearLayout) findViewById(R.id.ToDoLayout);
        viewFlipper =(ViewFlipper) findViewById(R.id.VF1) ;
        WaterLayout =(LinearLayout) findViewById(R.id.WaterLayout);
        WalkLayout =(LinearLayout) findViewById(R.id.WalkLayout);
        myDBHelper = new myDBHelper(this);

        WalkLayout = (LinearLayout)findViewById(R.id.WalkLayout);

        steps=(TextView)findViewById(R.id.Steps);

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(rGroup.getCheckedRadioButtonId()){
                    case R.id.Diary:
                        viewFlipper.setDisplayedChild(0);
                        break;

                    case R.id.ToDo:
                        viewFlipper.setDisplayedChild(1);
                        break;

                    case R.id.Water:
                        viewFlipper.setDisplayedChild(2);
                        break;

                    case R.id.Walk:
                        viewFlipper.setDisplayedChild(3);
                        steps.setText("걸음 수: "+String.valueOf(MyGlobalClass.getInstance().getData()));
                        break;

                }
            }
        });

        java.util.Calendar c = java.util.Calendar.getInstance();
        int cYear = c.get(java.util.Calendar.YEAR);
        int cMonth = c.get(java.util.Calendar.MONTH);
        int cDay = c.get(java.util.Calendar.DAY_OF_MONTH);

        checkedDaySQL(cYear, cMonth, cDay);
        m_year=cYear;
        m_month=cMonth;
        m_date=cDay;
        datePicker.init(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener(){
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dateOfMonth){
                checkedDaySQL(year, monthOfYear, dateOfMonth);
                m_year=year;
                m_month=monthOfYear;
                m_date=dateOfMonth;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                saveDiarySQL(m_year, m_month, m_date);
            }
        });
    }

    private void saveDiarySQL(int year, int monthOfYear, int dayOfMonth){
        sqlDB = myDBHelper.getWritableDatabase();
        String content = edtDiary.getText().toString();
        String tmp = Integer.toString(year)+Integer.toString(monthOfYear)+Integer.toString(dayOfMonth);
        sqlDB.execSQL("INSERT INTO groupTBL VALUES ( '"+tmp+"' , '"+content +"');");
        sqlDB.close();
        Toast.makeText(getApplicationContext(),"입력됨",Toast.LENGTH_SHORT).show();
    }

    private void checkedDaySQL(int year, int monthOfYear, int dayOfMonth){

        sqlDB = myDBHelper.getReadableDatabase();
        Cursor cursor;
        String tmp = Integer.toString(year)+Integer.toString(monthOfYear)+Integer.toString(dayOfMonth);
        cursor = sqlDB.rawQuery("SELECT * FROM groupTBL WHERE gDate = '"+tmp+"';",null);

        String strNames = "";

        while (cursor.moveToNext()){
            strNames += cursor.getString(1) + "\r\n";
        }
        edtDiary.setText(strNames);
        cursor.close();
        sqlDB.close();
    }

    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "groupDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE groupTBL ( gDate CHAR(20) PRIMARY KEY, gText CHAR(20));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupTBL");
            onCreate(db);

        }
    }
}