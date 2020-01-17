package com.example.fulltaskc4i;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fulltaskc4i.ModelsPackage.LoginModel;
import com.example.fulltaskc4i.ModelsPackage.SaveModel;
import com.example.fulltaskc4i.SQLiteDBPackage.LoginDbManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignInActivity extends AppCompatActivity {

    private TextInputEditText username, userPassword;
    private Button btnSignIn;
    private LoginDbManager loginDbManager;
    private SaveModel saveModel;
    private TextInputLayout passwordInputLayout;
    private CheckBox cbStaySignedIn;

    private View.OnClickListener login = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (username.getText() != null && userPassword.getText() != null) {
                LoginModel user = loginDbManager.getUser(username.getText().toString());
                if (user != null) {
                    if (userPassword.getText().toString().equals(user.getPassword())) {
                        displayMainActivity();
                        saveCheckBoxStatus();
                        finish();
                    } else {
                        makeToastMessage("Incorrect Password");
                    }
                } else {
                    makeToastMessage("This user name does not exist");
                }
            } else {
                makeToastMessage("Please enter username and password");
            }
        }
    };


    private TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (charSequence.length() > passwordInputLayout.getCounterMaxLength()) {
                passwordInputLayout.setError("password is too long");
            } else {
                passwordInputLayout.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private void displayMainActivity() {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignInActivity.this);
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent, options.toBundle());
        finish();
    }

    private void saveCheckBoxStatus() {
        boolean isChecked = cbStaySignedIn.isChecked();
        SaveModel saveModel = new SaveModel(this);
        if (isChecked) {
            saveModel.saveSignedInStatus(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveModel = new SaveModel(this);
        checkTheme();
        setContentView(R.layout.activity_sign_in);
        setToolbar();
        initViews();
        loginDbManager = new LoginDbManager(this).open();
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

    private void initViews() {
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        username = findViewById(R.id.username);
        userPassword = findViewById(R.id.userPassword);
        userPassword.addTextChangedListener(passwordWatcher);
        cbStaySignedIn = findViewById(R.id.cbStaySignedIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(login);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void makeToastMessage(String str) {
        Toast.makeText(SignInActivity.this, str, Toast.LENGTH_LONG).show();
    }

}
