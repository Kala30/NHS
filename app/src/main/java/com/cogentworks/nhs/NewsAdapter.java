package com.cogentworks.nhs;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<NewsItem> {
    public NewsAdapter(Context context, ArrayList<NewsItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NewsItem newsItem = getItem(position);

        if (newsItem == null)
            return null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell_news, parent, false);
        }

        TextView title = convertView.findViewById(R.id.title);
        TextView desc = convertView.findViewById(R.id.desc);
        TextView date = convertView.findViewById(R.id.date);
        ImageView image = convertView.findViewById(R.id.image);
        Button button = convertView.findViewById(R.id.button);

        title.setText(newsItem.title);
        desc.setText(newsItem.desc);
        date.setText(newsItem.date);

        final String url = newsItem.url;
        final Context context = convertView.getContext();
        final Resources resources = context.getResources();
        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(resources.getColor(R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(context, Uri.parse(url));
            }
        };

        convertView.setOnClickListener(onClick);
        button.setOnClickListener(onClick);


        Glide.with(convertView)
                .load(newsItem.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image);


        return convertView;
    }

}