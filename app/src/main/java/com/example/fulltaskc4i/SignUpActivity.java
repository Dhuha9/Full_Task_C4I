package com.example.fulltaskc4i;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fulltaskc4i.ModelsPackage.LoginModel;
import com.example.fulltaskc4i.ModelsPackage.SaveModel;
import com.example.fulltaskc4i.SQLiteDBPackage.LoginDbManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    private LoginDbManager loginDbManager;
    private TextInputEditText username, userPassword;
    private Button btnSignUp;
    private SaveModel saveModel;
    private TextInputLayout passwordInputLayout;

    View.OnClickListener signUp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (username.getText() != null && userPassword.getText() != null) {
                LoginModel user = loginDbManager.getUser(username.getText().toString());
                if (user == null) {
                    loginDbManager.addUser(new LoginModel(username.getText().toString(), userPassword.getText().toString()));
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "this user name already taken", Toast.LENGTH_LONG).show();
                }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveModel = new SaveModel(this);
        checkTheme();
        setContentView(R.layout.activity_sign_up);
        setToolbar();
        loginDbManager = new LoginDbManager(this).open();
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

    private void initViews() {
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        username = findViewById(R.id.username);
        userPassword = findViewById(R.id.userPassword);
        userPassword.addTextChangedListener(passwordWatcher);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(signUp);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
