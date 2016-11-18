package com.favn.ambulance.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.favn.ambulance.models.Commons.UserSender;
import com.favn.mikey.ambulance.R;

public class MainActivity extends AppCompatActivity {
    String urlAddress;
    private ImageView bg;
    private EditText etUsername;
    private EditText etPass;
    private Button btnLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControl();
    }

    // INITIATION SCREEN CONTROLS
    private void initControl() {

        urlAddress = "http://104.199.149.193/ambulancelogin";
          bg = (ImageView) findViewById(R.id.bg_login);
          etUsername = (EditText)findViewById(R.id.editext_name);
          etPass = (EditText)findViewById(R.id.editext_pass);
          btnLogin = (Button)findViewById(R.id.button_login);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Check validate input
                if(isValidateInput() == false){
                    return;
                }
                // Assign values
                String username = etUsername.getText().toString();
                String password = etPass.getText().toString();

                UserSender us = new UserSender();
                us.setContext(MainActivity.this);
                us.setUrlAddress(urlAddress);
                us.setUsername(username);
                us.setPassword(password);
                us.execute();
            }
        });
    }

    private boolean isValidateInput(){
        if(etUsername.getText().toString().trim().length() < 1) {
            etUsername.setError("Chưa nhập tên đăng nhập");
            etUsername.requestFocus();
            return false;
        }

        if(etPass.getText().toString().trim().length() < 1) {
            etPass.setError("Chưa nhập tên mật khẩu");
            etPass.requestFocus();
            return false;
        }


        return true;
    }
}