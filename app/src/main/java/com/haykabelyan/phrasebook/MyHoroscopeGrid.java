package com.haykabelyan.phrasebook;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyHoroscopeGrid extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] news;
    private final int[] images;

    public MyHoroscopeGrid(Activity context, String[] news, int[] images) {
        super(context, R.layout.news_list, news);
        this.context = context;
        this.news = news;
        this.images = images;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mycellgrid, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.mytextpart);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.myimagepart);
        txtTitle.setText(news[position]);
        imageView.setImageDrawable(context.getResources().getDrawable(images[position]));
        return rowView;
    }
}