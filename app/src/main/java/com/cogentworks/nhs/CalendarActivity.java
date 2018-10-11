package com.cogentworks.nhs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView mCalendarView;
    public ListView mListView;
    public ArrayList<String> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useDarkTheme = sharedPrefs.getBoolean(MainActivity.PREF_DARK_THEME, false);
        if (useDarkTheme)
            setTheme(R.style.AppDarkTheme);

        setContentView(R.layout.activity_calendar);

        mListView = findViewById(R.id.list);
        mListView = findViewById(R.id.list);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems));

        final Context context = this;
        mCalendarView = (CalendarView) findViewById(R.id.calendar_view);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m, int d) {
                String month = String.format("%02d", m+1);
                String day = String.format("%02d", d);
                new GetEvents(context, y + "-" + month + "-" + day).execute();
            }
        });

        new GetEvents(this, new SimpleDateFormat("yyyy-MM-dd").format(new Date())).execute();
    }


    public class NewsAdapter extends ArrayAdapter<String> {
        public NewsAdapter(Context context, ArrayList<String> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            String item = getItem(position);

            if (item == null)
                return null;

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            TextView title = convertView.findViewById(android.R.id.text1);
            title.setText(item);

            return convertView;
        }

    }
}
