package com.arappca.kresogretmen.ListAdapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arappca.kresogretmen.R;
import com.arappca.library.Classes.EduClasses.Ders;

import java.util.List;

import static com.arappca.library.Fonksiyonlar.hafizadanResimYukle;

/**
 * Created by Yusuf on 2.04.2016.
 */
public class DersListAdapter extends BaseAdapter {
    TextView lblPuan;
    private List<Ders> Dersler;
    private LayoutInflater mInflater;
    private Activity myActivity;

    public DersListAdapter(Activity activity, List<Ders> dersler) {
        Dersler = dersler;
        myActivity = activity;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Dersler.size();
    }

    @Override
    public Object getItem(int position) {
        return Dersler.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satirView;

        satirView = mInflater.inflate(R.layout.ders_item, null);

        ImageView imgDers = (ImageView) satirView.findViewById(R.id.imgDersResmi);
        TextView lblDersAdi = (TextView) satirView.findViewById(R.id.lblDersAdi);
        lblPuan = (TextView) satirView.findViewById(R.id.lblPuan);
        Ders ders = Dersler.get(position);
        ders.setResim(hafizadanResimYukle(myActivity.getFilesDir().getPath() + "/" + ders.getResimadi(), 1));
        lblDersAdi.setText(ders.getDersadi());
        imgDers.setImageBitmap(ders.getResim());
        lblPuan.setText(String.valueOf(ders.getPuan()));

        if (ders.isOlumlu()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                lblPuan.setBackgroundDrawable(myActivity.getResources().getDrawable(R.drawable.roundedtextviewyesil));
            } else {
                lblPuan.setBackground(myActivity.getResources().getDrawable(R.drawable.roundedtextviewyesil));
            }

        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                lblPuan.setBackgroundDrawable(myActivity.getResources().getDrawable(R.drawable.roundedtextviewkirmizi));
            } else {
                lblPuan.setBackground(myActivity.getResources().getDrawable(R.drawable.roundedtextviewkirmizi));
            }
        }

        return satirView;
    }


}
