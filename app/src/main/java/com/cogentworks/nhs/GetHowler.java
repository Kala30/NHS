package com.cogentworks.nhs;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.BaseAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GetHowler extends AsyncTask<String, Void, ArrayList<NewsItem>> {

    private Context context;
    private HowlerActivity activity;

    public GetHowler(Context context) {
        this.context = context;
        this.activity = (HowlerActivity) context;
    }

    @Override
    protected ArrayList<NewsItem> doInBackground(String... taskParams) {

        try {
            String url = "http://thehowleronline.org/category/print/";
            Document document = Jsoup.connect(url).get();

            Elements articles = document.select("article");

            ArrayList<NewsItem> results = new ArrayList<NewsItem>();
            for (Element article : articles) {
                NewsItem item = new NewsItem();
                item.imageUrl = article.selectFirst("img").attr("src");
                item.title = article.selectFirst("h2").text();
                item.desc = article.selectFirst("p").text() + "...";
                item.url = article.selectFirst("a").attr("href");
                item.date = article.selectFirst("time").text();

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