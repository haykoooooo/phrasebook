package com.haykabelyan.phrasebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HoroscopeGrid extends BaseAdapter {
    private Context mContext;

    public HoroscopeGrid(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return mThumbIds[position];
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;

        if (convertView == null) {
            grid = new View(mContext);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.cellgrid, parent, false);
        } else {
            grid = (View) convertView;
        }

        String zodiac = "";
        ImageView imageView = (ImageView) grid.findViewById(R.id.imagepart);
        TextView textView = (TextView) grid.findViewById(R.id.textpart);
        imageView.setImageResource(mThumbIds[position]);
        switch (position) {
            case 0:
                zodiac = "Խոյ";
                break;
            case 1:
                zodiac = "Ցուլ";
                break;
            case 2:
                zodiac = "Երկվորյակներ";
                break;
            case 3:
                zodiac = "Խեցգետին";
                break;
            case 4:
                zodiac = "Առյուծ";
                break;
            case 5:
                zodiac = "Կույս";
                break;
            case 6:
                zodiac = "Կշեռք";
                break;
            case 7:
                zodiac = "Կարիճ";
                break;
            case 8:
                zodiac = "Աղեղնավոր";
                break;
            case 9:
                zodiac = "Այծեղջյուր";
                break;
            case 10:
                zodiac = "Ջրհոս";
                break;
            case 11:
                zodiac = "Ձկներ";
                break;
        }
        textView.setText(zodiac);

        return grid;
    }

    // references to our images
    public Integer[] mThumbIds = {R.drawable.xoy, R.drawable.cul, R.drawable.erk, R.drawable.xec,
            R.drawable.ary, R.drawable.kuy, R.drawable.ksh, R.drawable.kar, R.drawable.agh,
            R.drawable.ayc, R.drawable.jrh, R.drawable.dzk};
}