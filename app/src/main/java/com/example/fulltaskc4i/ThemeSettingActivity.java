package com.example.fulltaskc4i;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fulltaskc4i.ModelsPackage.SaveModel;

public class ThemeSettingActivity extends AppCompatActivity {
    Button darkTheme, normalTheme;
    SaveModel saveModel;

    View.OnClickListener changeToDarkTheme = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveModel.saveTheme(R.style.DarkTheme);
            recreate();
        }
    };

    View.OnClickListener changeToNormalTheme = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveModel.saveTheme(R.style.AppTheme);
            recreate();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveModel = new SaveModel(this);
        checkTheme();
        setContentView(R.layout.activity_theme_setting);
        initViews();
    }

    private void initViews() {
        darkTheme = findViewById(R.id.darkTheme);
        darkTheme.setOnClickListener(changeToDarkTheme);

        normalTheme = findViewById(R.id.normalTheme);
        normalTheme.setOnClickListener(changeToNormalTheme);
    }

    private void checkTheme() {
        int resId = saveModel.getThemeId();
        if (resId == 0) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(resId);
        }
    }

}
