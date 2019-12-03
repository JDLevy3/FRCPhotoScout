package com.example.frcphotoscout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static List<Team> teamKeys = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //import tournament button functionality
        Button importTournament = findViewById(R.id.importTournament);
        importTournament.setOnClickListener(unused -> startActivity(new Intent(this, LoadTournament.class)));
        String key = getIntent().getStringExtra("key");
        if (key != null) {
            importTournament.setVisibility(View.GONE);


            RequestQueue queue = Volley.newRequestQueue(this);

            String url = "https://www.thebluealliance.com/api/v3/event/";
            //System.out.println(key);
            url += key;
            url += "/teams/simple?X-TBA-Auth-Key=X0YXXGjfE4C5TkeDFUYHtUvphev1Y3fuVffMPU2qgitwTAxLm8vwkzDoA3NW6yt7";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            listTeams(response);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String errorString = error.toString();
                    Log.e("Cant get teams", errorString);
                }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }
    //Crashes when loading teams
    private void listTeams(String response) {
        //System.out.println(response);
        LinearLayout teamList = findViewById(R.id.teamList);
        JsonParser parser = new JsonParser();
        JsonElement tList = parser.parse(response);
        JsonArray tArray = tList.getAsJsonArray();
        for(JsonElement tElement: tArray) {
            View messageChunk = getLayoutInflater().inflate(R.layout.team_chunk,
                    teamList, false);
            //populate chunk
            JsonObject tObject = tElement.getAsJsonObject();
            String key = tObject.get("key").getAsString();
            TextView teamName = messageChunk.findViewById(R.id.teamName);
            TextView teamNumber = messageChunk.findViewById(R.id.teamNumber);
            teamName.setText(tObject.get("nickname").getAsString());
            teamNumber.setText(tObject.get("team_number").getAsString());

            //chunk onClicklistener
            Intent selectedTeam = new Intent(this, TeamInfo.class);
            selectedTeam.putExtra("key", key);
            messageChunk.setOnClickListener(unused -> startActivity(selectedTeam));
            teamList.addView(messageChunk);
        }
    }
}
