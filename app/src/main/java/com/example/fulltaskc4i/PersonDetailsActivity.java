package com.example.fulltaskc4i;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fulltaskc4i.ModelsPackage.SaveModel;
import com.example.fulltaskc4i.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class PersonDetailsActivity extends AppCompatActivity {
    private SaveModel saveModel;
    private TextView txtPersonName, txtPersonInfo;
    private ImageView imgPersonImage;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveModel = new SaveModel(this);
        checkTheme();
        setContentView(R.layout.activity_person_details);
        initViews();
        getReceivedIntent();
        initAds();
    }

    private void checkTheme() {
        int resId = saveModel.getThemeId();
        if (resId == 0) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(resId);
        }
    }

    private void initAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ads_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void getReceivedIntent() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String saying = intent.getStringExtra("saying");
        byte[] image = intent.getByteArrayExtra("image");
        setViews(name, saying, image);
    }


    private void initViews() {
        txtPersonName = findViewById(R.id.txtPersonName);
        txtPersonInfo = findViewById(R.id.txtPersonInfo);
        imgPersonImage = findViewById(R.id.imgPersonImage);
    }

    private void setViews(String name, String saying, byte[] image) {
        txtPersonName.setText(name);
        txtPersonInfo.setText(saying);
        if (image!=null){
            imgPersonImage.setImageBitmap(bytesToImage(image));
        }
    }

    private Bitmap bytesToImage(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
}
