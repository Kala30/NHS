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

public class GetNews extends AsyncTask<String, Void, ArrayList<NewsItem>> {

    private Context context;
    private NewsActivity activity;

    public GetNews(Context context) {
        this.context = context;
        this.activity = (NewsActivity) context;
    }

    @Override
    protected ArrayList<NewsItem> doInBackground(String... taskParams) {

        try {
            String url = "https://northwoodhigh.iusd.org/news-center";
            Document document = Jsoup.connect(url).get();
            Element content = document.selectFirst("article");

            Elements articles = content.select(".views-row");

            ArrayList<NewsItem> results = new ArrayList<NewsItem>();
            for (Element article : articles) {
                NewsItem item = new NewsItem();
                item.imageUrl = article.selectFirst("img").attr("src");
                item.title = article.select("a").get(1).text();
                item.desc = article.selectFirst(".field-name-field-body").text();
                item.url = "https://northwoodhigh.iusd.org" + article.selectFirst("a").attr("href");
                item.date = article.selectFirst(".date-display-single").text();

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
