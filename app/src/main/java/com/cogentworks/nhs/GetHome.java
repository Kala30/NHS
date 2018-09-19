package com.cogentworks.nhs;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.customtabs.CustomTabsIntent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class GetHome extends AsyncTask<String, Void, GetHome.HomeData> {

    Context context;

    public GetHome(Context context) {
        this.context = context;
    }

    @Override
    protected HomeData doInBackground(String... taskParams) {

        try {
            String url = "https://northwoodhigh.iusd.org/";
            Document document = Jsoup.connect(url).get();
            HomeData result = new HomeData();

            Element news = document.selectFirst(".view-news");
            result.newsImageUrl = news.select("img").attr("src");
            result.newsTitle = news.selectFirst("h3").text();
            result.newsDesc = news.selectFirst("p").text();
            result.newsUrl = "https://northwoodhigh.iusd.org" + news.selectFirst("a").attr("href");

            Element ntv = document.selectFirst(".group-wrapper");
            result.ntvImageUrl = ntv.select("img").attr("src");
            result.ntvTitle = ntv.select(".field-name-field-title").text();
            result.ntvDesc = ntv.select("p").text();
            result.ntvUrl = ntv.selectFirst("a").attr("href");


            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(final HomeData result) {
        if (result != null) {

            final Activity activity = (Activity) context;


            ImageView newsImage = activity.findViewById(R.id.image_news);
            Glide.with(context)
                    .load(result.newsImageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newsImage);
            TextView newsTitle = activity.findViewById(R.id.title_news);
            newsTitle.setText(result.newsTitle);
            TextView newsDesc = activity.findViewById(R.id.desc_news);
            newsDesc.setText(result.newsDesc);
            activity.findViewById(R.id.card_news).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(activity.getResources().getColor(R.color.colorPrimary));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(context, Uri.parse(result.newsUrl));
                }
            });


            ImageView ntvImage = activity.findViewById(R.id.image_ntv);
            Glide.with(context)
                    .load(result.ntvImageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ntvImage);
            TextView ntvTitle = activity.findViewById(R.id.title_ntv);
            ntvTitle.setText(result.ntvTitle);
            TextView ntvDesc = activity.findViewById(R.id.desc_ntv);
            ntvDesc.setText(result.ntvDesc);
            activity.findViewById(R.id.card_ntv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(activity.getResources().getColor(R.color.colorPrimary));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(context, Uri.parse(result.ntvUrl));
                }
            });

        }
    }

    public class HomeData {
        public String newsImageUrl;
        public String newsTitle;
        public String newsDesc;
        public String newsUrl;

        public String ntvImageUrl;
        public String ntvTitle;
        public String ntvDesc;
        public String ntvUrl;
        public String moreNtvUrl;
    }
}
