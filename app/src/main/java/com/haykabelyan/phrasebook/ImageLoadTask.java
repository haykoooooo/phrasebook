package com.haykabelyan.phrasebook;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class ImageLoadTask {

    Context context;
    String url;
    ImageView imageView;

    public ImageLoadTask(Context context, String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
        this.context = context;
    }

    public void execute() {
        Glide.with(context).load(url).into(imageView);
    }
}