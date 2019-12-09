package com.example.frcphotoscout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static Map<String, Team> teamKeys = new HashMap<>();
    //private String key;
    private LinearLayout teamList;
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
        teamList = findViewById(R.id.teamList);
        JsonParser parser = new JsonParser();
        JsonElement tList = parser.parse(response);
        JsonArray tArray = tList.getAsJsonArray();
        for(JsonElement tElement: tArray) {
            View messageChunk = getLayoutInflater().inflate(R.layout.team_chunk,
                    teamList, false);
            //populate chunk
            JsonObject tObject = tElement.getAsJsonObject();
            String key = tObject.get("key").getAsString();
            Team t = new Team(tObject.get("team_number").getAsString(), tObject.get("nickname").getAsString(), key);
            //TextView teamName = messageChunk.findViewById(R.id.teamName);
            //TextView teamNumber = messageChunk.findViewById(R.id.teamNumber);
            //teamName.setText(t.getName());
            //teamNumber.setText(t.getNumber());
            teamKeys.put(key, t);
            //if (teamKeys.get(key).getThumbnail() != null) {
            //    ImageView i = messageChunk.findViewById(R.id.teamImageThumb);
            //    i.setImageBitmap(teamKeys.get(key).getThumbnail());

            //}

            //chunk onClicklistener
            //Intent selectedTeam = new Intent(this, TeamInfo.class);
            //selectedTeam.putExtra("key", key);
            //messageChunk.setOnClickListener(unused -> startActivity(selectedTeam));
            //teamList.addView(messageChunk);
        }
        drawUI();
    }
    // Is called when mainactivity is back into focus, allows thumbnails to be displayed properly after they are taken
    public void onResume() {
        super.onResume();
        drawUI();
    }

    private void drawUI() {
        if (teamList != null) {
            teamList.removeAllViews();
            for (Map.Entry<String, Team> entry : teamKeys.entrySet()) {
                View messageChunk = getLayoutInflater().inflate(R.layout.team_chunk,
                        teamList, false);
                Team t = teamKeys.get(entry.getKey());
                TextView teamName = messageChunk.findViewById(R.id.teamName);
                TextView teamNumber = messageChunk.findViewById(R.id.teamNumber);
                teamName.setText(t.getName());
                teamNumber.setText(t.getNumber());
                if (teamKeys.get(entry.getKey()).getThumbnail() != null) {
                    ImageView teamThumb = messageChunk.findViewById(R.id.teamImageThumb);
                    teamThumb.setImageBitmap(teamKeys.get(entry.getKey()).getThumbnail());
                }
                //chunk onClicklistener
                Intent selectedTeam = new Intent(this, TeamInfo.class);
                selectedTeam.putExtra("key", entry.getKey());
                messageChunk.setOnClickListener(unused -> startActivity(selectedTeam));
                teamList.addView(messageChunk);
            }
        }
    }

}
