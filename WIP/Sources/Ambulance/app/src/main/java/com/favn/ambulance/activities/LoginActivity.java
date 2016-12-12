package com.favn.ambulance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.favn.ambulance.utils.Constants;
import com.favn.ambulance.commons.UserSender;
import com.favn.mikey.ambulance.R;

public class LoginActivity extends AppCompatActivity {
    private String urlAddress;
    private ImageView bg;
    private EditText etUsername;
    private EditText etPass;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        initControl();
        getSupportActionBar().hide();
    }

    // INITIATION SCREEN CONTROLS
    private void initControl() {

        //urlAddress = "http://104.199.149.193/ambulancelogin";
        urlAddress = "http://10.20.19.73/capston/WIP/Sources/FAVN_web/public/ambulancelogin";
        bg = (ImageView) findViewById(R.id.bg_login);
        etUsername = (EditText) findViewById(R.id.editext_name);
        etPass = (EditText) findViewById(R.id.editext_pass);
        btnLogin = (Button) findViewById(R.id.button_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loginRequest();

                Intent intent = new Intent(LoginActivity.this, WaitingActivity.class);
                intent.putExtra("isReady", true);
                startActivity(intent);
            }
        });

        etPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginRequest();
                }
                return false;
            }
        });
    }

    private void loginRequest() {
        // Check validate input
        if (isValidateInput() == false) {
            return;
        }
        // Assign values
        String username = etUsername.getText().toString();
        String password = etPass.getText().toString();

        UserSender us = new UserSender();
        us.setContext(LoginActivity.this);
        us.setUrlAddress(urlAddress);
        us.setUsername(username);
        us.setPassword(password);
        us.execute();
    }

    private boolean isValidateInput() {
        if (etUsername.getText().toString().trim().length() < 1) {
            etUsername.setError(Constants.REQUIRED_ENTER_USERNAME);
            etUsername.requestFocus();
            return false;
        }

        if (etPass.getText().toString().trim().length() < 1) {
            etPass.setError(Constants.REQUIRED_ENTER_PASSWORD);
            etPass.requestFocus();
            return false;
        }


        return true;
    }

    @Override
    public void onBackPressed() {
    }
}
