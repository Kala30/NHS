package com.cogentworks.nhs;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GetSchedule extends AsyncTask<String, Void, String[]> {

    Context context;

    public GetSchedule(Context context) {
        this.context = context;
    }

    @Override
    protected String[] doInBackground(String... taskParams) {

        try {
            String url = "https://northwoodhigh.iusd.org/today-event/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());;
            Document document = Jsoup.connect(url).get();

            Element content = document.selectFirst(".view-content");
            Elements events = content.select("a");

            ArrayList<String> strings = new ArrayList<>();
            for (Element element : events) {
                strings.add(element.text());
            }

            DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy (EEEE)", Locale.US);
            DateFormat timeFormat = new SimpleDateFormat("kmm", Locale.US);
            DateFormat weekFormat = new SimpleDateFormat("u", Locale.US);
            Date date = new Date();
            int currentTime = Integer.parseInt(timeFormat.format(date));
            //int currentTime = 1200;
            int dayofWeek = Integer.parseInt(weekFormat.format(date));
            String strDate = dateFormat.format(date);

            ArrayList<Pair<String, Integer>> schedule = new ArrayList<>();

            if (dayofWeek == 1 || dayofWeek == 5) // MON,FRI
                schedule = Schedules.ADVISEMENT;
            else if (dayofWeek == 2 || dayofWeek == 4) // TUE,THU
                schedule = Schedules.TUTORIAL;
            else if (dayofWeek == 3) // WED
                schedule = Schedules.COLLABORATION;

            String time = null;
            for (int i = 0; i < schedule.size(); i++) {
                Pair<String, Integer> period = schedule.get(i);
                if (period.second > currentTime) {
                    time = period.first + " in " + toInterval(period.second - currentTime);
                    break;
                }
            }

            String schedStr = "";
            for (int i = 0; i < strings.size(); i++) {
                String str = strings.get(i);
                if (str.toLowerCase().contains("day") || str.toLowerCase().contains("schedule"))
                schedStr += strings.get(i);
                if (i != strings.size() - 1)
                    schedStr += ", ";
            }

            String[] result = {strDate, time, schedStr};

            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (result != null) {
            TextView time = ((Activity)context).findViewById(R.id.text_time);
            TextView date = ((Activity)context).findViewById(R.id.text_date);

            if (result[1] != null) {
                time.setText(result[1]);
                date.setText(result[0]);
            } else
                time.setText(result[0]);

            TextView schedule = ((Activity)context).findViewById(R.id.text_schedule);
            schedule.setText(result[2]);
        }
    }

    private String toInterval(int mins) {
        if (mins < 60)
            return "" + mins + " min";
        else {
            int hours = mins / 60;
            mins = mins % 60;
            if (hours < 24)
                return hours + " hr " + mins + " min";
            else {
                int days = hours / 24;
                hours = hours % 24;
                return days + " days " + hours + " hr " + mins + " min";
            }
        }
    }
}
