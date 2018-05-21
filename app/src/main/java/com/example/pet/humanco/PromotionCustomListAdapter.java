package com.example.pet.humanco;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pet.humanco.model.PromotionList;

import java.util.ArrayList;

/**
 * Created by NARAYANA on 03-05-2018.
 */

public class PromotionCustomListAdapter extends ArrayAdapter<PromotionList> {
    Context context;

    PromotionCustomListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PromotionList> PromotionList) {
        super(context, resource, PromotionList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.promotion_list,null,true);
        }
        PromotionList PromotionList = getItem(position);
        TextView id = convertView.findViewById(R.id.Id_PM);
        assert PromotionList != null;
        id.setText(PromotionList.getId());
        //Toast.makeText(context, ""+id, Toast.LENGTH_SHORT).show();
        ImageView imageView = convertView.findViewById(R.id.img_PM);
        //assert imageList != null;
        //Picasso.with(context).load(PromotionList.getImage()).into(imageView);
        Glide.with(context).load(PromotionList.getImage())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        TextView name = convertView.findViewById(R.id.name_PM);
        name.setText(PromotionList.getShop_name());
        TextView phone = convertView.findViewById(R.id.phone_PM);
        phone.setText(PromotionList.getMobile_number());
        TextView location = convertView.findViewById(R.id.location_PM);
        location.setText(PromotionList.getLocation());
        TextView RatingText = convertView.findViewById(R.id.RatingText);
        RatingBar ratingBar_PM = convertView.findViewById(R.id.ratingBar_PM);
        RatingText.setText(PromotionList.getRating());
        String RatingBarValue = RatingText.getText().toString();
        float rating123 = Float.parseFloat(RatingBarValue);
        ratingBar_PM.setRating(rating123);

        return convertView;
    }
}