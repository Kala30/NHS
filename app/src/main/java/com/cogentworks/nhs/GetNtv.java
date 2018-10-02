package com.cogentworks.nhs;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.BaseAdapter;

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

public class GetNtv extends AsyncTask<String, Void, ArrayList<NewsItem>> {

    private Context context;
    private NtvActivity activity;

    public GetNtv(Context context) {
        this.context = context;
        this.activity = (NtvActivity) context;
    }

    @Override
    protected ArrayList<NewsItem> doInBackground(String... taskParams) {

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.US);
            int year = Integer.parseInt(dateFormat.format(new Date()));

            String url = "https://iusd.tv/channel/NTV%2B" + year + "-" + (year+1);
            Document document = Jsoup.connect(url).get();

            Elements videos = document.select(".galleryItem");
            ArrayList<NewsItem> results = new ArrayList<NewsItem>();

            for (Element video : videos) {
                NewsItem item = new NewsItem();
                item.imageUrl = video.select("img").attr("src");
                item.title = video.selectFirst(".thumb_name_content").text();
                item.desc = video.selectFirst(".duration").text();
                item.date = video.selectFirst(".thumbTimeAdded").text();
                item.url = "https://iusd.tv" + video.selectFirst("a").attr("href");

                results.add(item);
            }

            return results;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<NewsItem> result) {
        if (result != null) {

            activity.listItems.clear();
            activity.listItems.addAll(result);
            ((BaseAdapter)activity.mListView.getAdapter()).notifyDataSetChanged();

        }
    }

}
