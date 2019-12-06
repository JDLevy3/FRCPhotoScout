package com.example.frcphotoscout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TeamInfo extends AppCompatActivity {
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        Button takePhoto = findViewById(R.id.takePhotoButton);
        takePhoto.setOnClickListener(unused -> takePhoto());

        TextView teamName = findViewById(R.id.teamName);
        key = getIntent().getStringExtra("key");
        teamName.setText(MainActivity.teamKeys.get(getIntent().getStringExtra("key")).getName());
        TextView teamNumber = findViewById(R.id.teamNumber);
        teamNumber.setText(MainActivity.teamKeys.get(getIntent().getStringExtra("key")).getNumber());
        EditText teamInfo = findViewById(R.id.teamInfo);
        if(MainActivity.teamKeys.get(getIntent().getStringExtra("key")).getReport() != null) {
            teamInfo.setText(MainActivity.teamKeys.get(getIntent().getStringExtra("key")).getReport());
        }
    }
    final int REQUEST_IMAGE_CAPTURE = 1;
    private void takePhoto() {
        // This currently just opens the camera


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent takePictureIntent){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = takePictureIntent.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            MainActivity.teamKeys.get(key).setThumbnail(imageBitmap);
        }
    }

    protected void onPause() {
        super.onPause();
        saveText();
    }
    protected void onDestroy() {
        super.onDestroy();
        saveText();
    }
    private void saveText() {
        EditText report = findViewById(R.id.teamInfo);
        MainActivity.teamKeys.get(getIntent().getStringExtra("key")).setReport(report.getText().toString());
    }
}
