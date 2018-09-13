package com.cogentworks.nhs;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class GetHowlerHome extends AsyncTask<String, Void, String[]> {

    Context context;

    public GetHowlerHome(Context context) {
        this.context = context;
    }

    @Override
    protected String[] doInBackground(String... taskParams) {

        try {
            String url = "http://thehowleronline.org/category/print/";
            Document document = Jsoup.connect(url).get();

            Element article = document.selectFirst("article");
            String imageUrl = article.select("img").attr("src");
            String title = article.select("h2").text();
            String desc = article.select("p").text();

            return new String[] {imageUrl, title, desc + "..."};

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (result != null && result.length >= 3) {

            Activity activity = (Activity) context;


            ImageView newsImage = activity.findViewById(R.id.image_howler);
            Glide.with(context)
                    .load(result[0])
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(newsImage);
            TextView title = activity.findViewById(R.id.title_howler);
            title.setText(result[1]);
            TextView desc = activity.findViewById(R.id.desc_howler);
            desc.setText(result[2]);

        }
    }

}
