package com.cogentworks.nhs;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GetEvents extends AsyncTask<String, Void, ArrayList<String>> {

    private Context context;
    private CalendarActivity activity;
    private String date;

    public GetEvents(Context context, String date) {
        this.context = context;
        this.activity = (CalendarActivity) context;
        this.date = date;
    }

    @Override
    protected ArrayList<String> doInBackground(String... taskParams) {

        try {
            String url = "https://northwoodhigh.iusd.org/today-event/" + date;
            Document document = Jsoup.connect(url).get();
            Element content = document.selectFirst(".today-event");
            Elements eventsContent = content.select(".views-field-title");

            ArrayList<String> events = new ArrayList<>();
            for (Element element : eventsContent) {
                events.add(element.text());
            }

            return events;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        if (result != null) {
            activity.listItems.clear();
            activity.listItems.addAll(result);
            ((BaseAdapter)activity.mListView.getAdapter()).notifyDataSetChanged();

        }
    }

}
