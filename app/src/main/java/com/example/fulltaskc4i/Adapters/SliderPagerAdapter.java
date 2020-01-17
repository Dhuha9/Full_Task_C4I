package com.example.fulltaskc4i.Adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.fulltaskc4i.PersonDetailsActivity;
import com.example.fulltaskc4i.ModelsPackage.PersonModel;
import com.example.fulltaskc4i.R;

import java.util.ArrayList;

public class SliderPagerAdapter extends PagerAdapter {
    private ArrayList<PersonModel> sliderList;
    private ImageView sliderImage;
    private TextView sliderTitle;
    private Context context;
    private PersonModel sliderItem;

    public SliderPagerAdapter(ArrayList<PersonModel> sliderList, Context context) {
        this.sliderList = sliderList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.slider, container, false);
        sliderImage = view.findViewById(R.id.sliderImage);
        sliderItem = sliderList.get(position);
        if (sliderItem.getPersonImage()!=null){
            sliderImage.setImageBitmap(bytesToImage(sliderItem.getPersonImage()));
        }

        sliderTitle = view.findViewById(R.id.sliderTitle);
        sliderTitle.setText(sliderItem.getPersonName());
        container.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(sliderTitle, "sharedPersonName");
                pairs[1] = new Pair<View, String>(sliderImage, "sharedPersonImage");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairs);
                Intent intent = new Intent(context, PersonDetailsActivity.class);
                intent.putExtra("name", sliderItem.getPersonName());
                intent.putExtra("saying", sliderItem.getPersonSaying());
                intent.putExtra("image", sliderItem.getPersonImage());

                view.getContext().startActivity(intent, options.toBundle());
            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    private Bitmap bytesToImage(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
}
