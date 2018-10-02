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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GetNtvHome extends AsyncTask<String, Void, NewsItem> {

    Context context;

    public GetNtvHome(Context context) {
        this.context = context;
    }

    @Override
    protected NewsItem doInBackground(String... taskParams) {

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.US);
            int year = Integer.parseInt(dateFormat.format(new Date()));

            String url = "https://iusd.tv/channel/NTV%2B" + year + "-" + (year+1);
            Document document = Jsoup.connect(url).get();
            NewsItem result = new NewsItem();

            Element video = document.selectFirst(".galleryItem");
            result.imageUrl = video.select("img").attr("src");
            result.title = video.selectFirst(".thumb_name_content").text();
            result.desc = video.selectFirst(".duration").text();
            result.date = video.selectFirst(".thumbTimeAdded").text();
            result.url = "https://iusd.tv" + video.selectFirst("a").attr("href");


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


            ImageView ntvImage = activity.findViewById(R.id.image_ntv);
            Glide.with(context)
                    .load(result.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ntvImage);

            TextView ntvTitle = activity.findViewById(R.id.title_ntv);
            ntvTitle.setText(result.title);

            TextView ntvDesc = activity.findViewById(R.id.desc_ntv);
            ntvDesc.setText(result.desc);

            TextView ntvDate = activity.findViewById(R.id.date_ntv);
            ntvDate.setText(result.date);

            activity.findViewById(R.id.card_ntv).setOnClickListener(new View.OnClickListener() {
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

    public class NtvData {
        public String ntvImageUrl;
        public String ntvTitle;
        public String ntvDesc;
        public String ntvUrl;
        public String moreNtvUrl;
    }
}
