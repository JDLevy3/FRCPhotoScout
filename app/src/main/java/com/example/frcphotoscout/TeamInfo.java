package com.example.frcphotoscout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TeamInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        Button takePhoto = findViewById(R.id.takePhotoButton);
        takePhoto.setOnClickListener(unused -> takePhoto());

        TextView teamName = findViewById(R.id.teamName);
        teamName.setText(MainActivity.teamKeys.get(getIntent().getStringExtra("key")).getName());
        TextView teamNumber = findViewById(R.id.teamNumber);
        teamNumber.setText(MainActivity.teamKeys.get(getIntent().getStringExtra("key")).getNumber());
        EditText teamInfo = findViewById(R.id.teamInfo);
        if(MainActivity.teamKeys.get(getIntent().getStringExtra("key")).getReport() != null) {
            teamInfo.setText(MainActivity.teamKeys.get(getIntent().getStringExtra("key")).getReport());
        }
    }
    private void takePhoto() {
        final int REQUEST_IMAGE_CAPTURE = 1;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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
