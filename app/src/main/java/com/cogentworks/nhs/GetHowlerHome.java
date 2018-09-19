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

public class GetHowlerHome extends AsyncTask<String, Void, NewsItem> {

    Context context;

    public GetHowlerHome(Context context) {
        this.context = context;
    }

    @Override
    protected NewsItem doInBackground(String... taskParams) {

        try {
            String url = "http://thehowleronline.org/category/print/";
            Document document = Jsoup.connect(url).get();
            NewsItem result = new NewsItem();

            Element article = document.selectFirst("article");
            result.imageUrl = article.select("img").attr("src");
            if (result.imageUrl.equals("http://thehowleronline.org/wp-content/themes/justwrite/images/no-thumbnail.png"))
                result.imageUrl = null;
            result.title = article.select("h2").text();
            result.desc = article.select("p").text();
            result.url = article.select("a").attr("href");

            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(final NewsItem result) {
        if (result != null) {

            final Activity activity = (Activity) context;

            ImageView newsImage = activity.findViewById(R.id.image_howler);
            if (result.imageUrl != null) {
                Glide.with(context)
                        .load(result.imageUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(newsImage);
            }
            TextView title = activity.findViewById(R.id.title_howler);
            title.setText(result.title);
            TextView desc = activity.findViewById(R.id.desc_howler);
            desc.setText(result.desc);

            activity.findViewById(R.id.card_howler).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(activity.getResources().getColor(R.color.colorPrimary));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(context, Uri.parse(result.url));
                }
            });

        }
    }

}
