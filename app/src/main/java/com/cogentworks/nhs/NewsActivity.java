package com.cogentworks.nhs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    public ArrayList<NewsItem> listItems = new ArrayList<>();
    public ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mListView = findViewById(R.id.list);
        mListView = findViewById(R.id.list);
        mListView.setAdapter(new NewsAdapter(this, listItems));

        new GetNews(this).execute();

    }
}
