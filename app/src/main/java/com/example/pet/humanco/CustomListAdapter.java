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
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pet.humanco.model.ImageList;

import java.util.ArrayList;

/**
 * Created by NARAYANA on 03-05-2018.
 */

public class CustomListAdapter extends ArrayAdapter<ImageList> {
    ArrayList<ImageList> imageLists;
    Context context;
    int resource;
    public CustomListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ImageList> imageLists) {
        super(context, resource, imageLists);
        this.imageLists = imageLists;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.model_list,null,true);
        }

        ImageList imageList = getItem(position);

        TextView id = convertView.findViewById(R.id.Id);
        id.setText(imageList.getId());

        ImageView imageView = convertView.findViewById(R.id.img);

        Glide.with(context).load(imageList.getImage())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        TextView type = convertView.findViewById(R.id.type_M);
        type.setText(imageList.getType());
        TextView breed = convertView.findViewById(R.id.breed_M);
        breed.setText(imageList.getBreed());
        TextView age = convertView.findViewById(R.id.age_M);
        age.setText(imageList.getAge());
        TextView location = convertView.findViewById(R.id.location_M);
        location.setText(imageList.getLocation());

        return convertView;
    }



}
