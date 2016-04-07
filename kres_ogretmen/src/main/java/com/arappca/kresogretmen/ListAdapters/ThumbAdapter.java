package com.arappca.kresogretmen.ListAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.arappca.kresogretmen.CustomClasses.CustomThumb;
import com.arappca.kresogretmen.R;

import java.util.List;

import static com.arappca.library.Fonksiyonlar.hafizadanResimYukle;

/**
 * Created by Yusuf on 5.04.2016.
 */
public class ThumbAdapter extends
        RecyclerView.Adapter<ThumbAdapter.ViewHolder> {
    private List<CustomThumb> mThumbs;


    // Pass in the contact array into the constructor
    public ThumbAdapter(List<CustomThumb> thumbs) {
        mThumbs = thumbs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View ThumbView = inflater.inflate(R.layout.image_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(ThumbView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
        final CustomThumb thumb = mThumbs.get(position);

        // Set item views based on the data model
        ImageView imgThumb = holder.imgThumb;
        final CheckBox chkThumb = holder.chkThumb;
        imgThumb.setImageBitmap(hafizadanResimYukle(thumb.getResimyeri(), 4));

        //  imgThumb.setImageBitmap(BitmapFactory.decodeFile(thumb.getResimyeri()));

        imgThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkThumb.setChecked(!chkThumb.isChecked());
                Log.d("resim", thumb.getResimyeri());
            }
        });
        chkThumb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                thumb.setSecili(chkThumb.isChecked());
            }
        });
        chkThumb.setChecked(thumb.isSecili());
    }

    @Override
    public int getItemCount() {
        return mThumbs.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private ImageView imgThumb;
        private CheckBox chkThumb;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            imgThumb = (ImageView) itemView.findViewById(R.id.imgThumb);
            chkThumb = (CheckBox) itemView.findViewById(R.id.chkThumb);


        }
    }


}
