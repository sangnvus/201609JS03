package com.example.mikey.ambulance.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.mikey.ambulance.R;

public class Quest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        AlertDialog.Builder dialog  = new AlertDialog.Builder(this);
        dialog.setTitle("NHIỆM VỤ MỚI");
        dialog.setMessage("Nhận nhiệm vụ mới");
        dialog.setPositiveButton("Nhận", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // You don't have to do anything here if you just want it dismissed when clicked
            }
        });

        dialog.setCancelable(true);
        dialog.create().show();

        //ấn nút hoàn thành nhiệm vụ
        Button done = (Button) findViewById(R.id.button_complete);
        done.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Infor.class);
                startActivity(intent);
            }
        });

        //ấn nút hủy
        Button cancel = (Button) findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Infor.class);
                startActivity(intent);
            }
        });

        Button delete = (Button) findViewById(R.id.button_delete);
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Infor.class);
                startActivity(intent);
            }
        });
    }
}
