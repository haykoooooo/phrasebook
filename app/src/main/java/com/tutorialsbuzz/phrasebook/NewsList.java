package com.tutorialsbuzz.phrasebook;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] news;
    private final String[] images;

    public NewsList(Activity context, String[] news, String[] images) {
        super(context, R.layout.news_list, news);
        this.context = context;
        this.news = news;
        this.images = images;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.news_list, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(news[position]);
        new ImageLoadTask(context, images[position], imageView).execute();
        return rowView;
    }
}