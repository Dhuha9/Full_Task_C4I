package com.example.fulltaskc4i;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fulltaskc4i.ModelsPackage.SaveModel;

public class FirstActivity extends AppCompatActivity {

    Button btnSignIn;
    Button btnSignUp;
    SaveModel saveModel;

    View.OnClickListener goToSignIn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(FirstActivity.this, btnSignIn, "sharedButton");
            Intent intent = new Intent(FirstActivity.this, SignInActivity.class);
            startActivity(intent, options.toBundle());
        }
    };
    View.OnClickListener goToSignUp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(FirstActivity.this, SignUpActivity.class);
            startActivity(intent);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveModel = new SaveModel(this);
        checkTheme();
        setContentView(R.layout.activity_first);
        checkSignInStatus();
        setToolbar();
        initViews();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkTheme();
    }

    private void checkTheme() {
        int resId = saveModel.getThemeId();
        if (resId == 0) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(resId);
        }
    }

    private void checkSignInStatus() {

        if (saveModel.isSignedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initViews() {
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(goToSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(goToSignUp);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
