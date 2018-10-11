package com.cogentworks.nhs;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.view.View;
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
            String urlDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String url = "https://northwoodhigh.iusd.org/today-event/" + urlDate;
            Log.d("GetSchedule", urlDate);
            //String url = "https://northwoodhigh.iusd.org/today-event/2018-09-13";
            Document document = Jsoup.connect(url).get();

            Element content = document.selectFirst(".view-content");
            Elements eventsContent = content.select("a");

            boolean noSchool = false;
            boolean isOdd = true;
            ArrayList<String> events = new ArrayList<>();
            for (Element element : eventsContent) {
                events.add(element.text());
                Log.d("GetSchedule", element.text());
                if (element.text().toLowerCase().contains("even"))
                    isOdd = false;
                if (element.text().toLowerCase().contains("no school"))
                    noSchool = true;
            }

            DateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US);
            DateFormat timeFormat = new SimpleDateFormat("kmm", Locale.US);
            DateFormat weekFormat = new SimpleDateFormat("u", Locale.US);
            Date date = new Date();
            int currentTime = Integer.parseInt(timeFormat.format(date));
            //int currentTime = 1000;
            int dayofWeek = Integer.parseInt(weekFormat.format(date));
            String strDate = dateFormat.format(date);

            ArrayList<Pair<String, Integer>> schedule = new ArrayList<>();

            if (dayofWeek == 1 || dayofWeek == 5) // MON,FRI
                schedule = Schedules.ADVISEMENT;
            else if (dayofWeek == 2 || dayofWeek == 4) // TUE,THU
                schedule = Schedules.TUTORIAL;
            else if (dayofWeek == 3) // WED
                schedule = Schedules.COLLABORATION;
            else
                noSchool = true;

            String time = null;
            for (int i = 0; i < schedule.size(); i++) {
                Pair<String, Integer> period = schedule.get(i);
                if (period.second > currentTime) {
                    String periodStr = period.first;
                    if (periodStr.contains("%")) {
                        periodStr = periodStr.replace("%", "");
                        if (!isOdd)
                            periodStr = periodStr.substring(0, periodStr.length()-1) + (Integer.parseInt(periodStr.substring(periodStr.length()-1))+1);
                    }
                    time = periodStr + " in " + toInterval(period.second - currentTime);
                    break;
                }
            }

            String schedStr = null;
            if (!noSchool) {
                if (isOdd)
                    schedStr = "ODD";
                else
                    schedStr = "EVEN";
            }

            String special = null;
            for (int i = 0; i < events.size(); i++) {
                String str = events.get(i);
                if (str.toLowerCase().contains("schedule") || str.toLowerCase().contains("no school")) {
                    special = str;
                    break;
                }
            }

            String[] result = {strDate, time, schedStr, special};

            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (result != null) {
            Activity activity = (Activity) context;

            TextView time = ((Activity)context).findViewById(R.id.text_time);
            TextView date = ((Activity)context).findViewById(R.id.text_date);

            if (result[1] != null) {
                time.setText(result[1]);
                date.setText(result[0]);
            } else
                time.setText(result[0]);

            if (result[2] != null) {
                TextView schedule = activity.findViewById(R.id.text_schedule);
                activity.findViewById(R.id.card_sched).setVisibility(View.VISIBLE);
                schedule.setText(result[2]);
            }

            if (result[3] != null) {
                TextView specialSched = ((Activity)context).findViewById(R.id.text_special);
                specialSched.setVisibility(View.VISIBLE);
                specialSched.setText(result[3]);
            }
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
