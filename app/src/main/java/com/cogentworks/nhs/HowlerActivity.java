package com.cogentworks.nhs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class HowlerActivity extends AppCompatActivity {

    public ArrayList<NewsItem> listItems = new ArrayList<>();
    public ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howler);

        mListView = findViewById(R.id.list);
        mListView = findViewById(R.id.list);
        mListView.setAdapter(new NewsAdapter(this, listItems));

        new GetHowler(this).execute();
    }
}
