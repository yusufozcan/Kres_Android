package com.arappca.kresogretmen.ListAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arappca.kresogretmen.R;
import com.arappca.library.Classes.EduClasses.Ogrenci;

import java.util.List;

/**
 * Created by Yusuf on 1.04.2016.
 */
public class OgrenciListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Ogrenci> mOgrenciler;

    public OgrenciListAdapter(Activity activity, List<Ogrenci> ogrenciler) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mOgrenciler = ogrenciler;
    }

    @Override
    public int getCount() {
        return mOgrenciler.size();
    }

    @Override
    public Ogrenci getItem(int position) {
        return mOgrenciler.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satirView;

        satirView = mInflater.inflate(R.layout.ogrenci_item_view, null);

        TextView lblAdSoyad = (TextView) satirView.findViewById(R.id.lblAdSoyad);
        TextView lblSonPuan = (TextView) satirView.findViewById(R.id.lblSonPuan);

        Ogrenci ogrenci = mOgrenciler.get(position);
        lblAdSoyad.setText(ogrenci.getAdsoyad());
        lblSonPuan.setText(String.valueOf(ogrenci.getSonpuan()));


        return satirView;
    }
}
