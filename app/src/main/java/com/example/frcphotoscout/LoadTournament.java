package com.example.frcphotoscout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.w3c.dom.Text;

public class LoadTournament extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_tournament);

        //load tournaments from bluealliance api

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        //change year to get the year and not just 2020
        String url ="https://www.thebluealliance.com/api/v3/events/2020/simple?X-TBA-Auth-Key=X0YXXGjfE4C5TkeDFUYHtUvphev1Y3fuVffMPU2qgitwTAxLm8vwkzDoA3NW6yt7";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        listTournaments(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorString = error.toString();
                Log.e("Cant get tournaments", errorString);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
    private void listTournaments(String response) {
        System.out.println(response);
        LinearLayout tournamentList = findViewById(R.id.tournamentList);
        JsonParser parser = new JsonParser();
        JsonElement tList = parser.parse(response);
        JsonArray tArray = tList.getAsJsonArray();
        for(JsonElement tElement: tArray) {
            View messageChunk = getLayoutInflater().inflate(R.layout.tournament_chunk,
                    tournamentList, false);
            //populate chunk
            JsonObject tObject = tElement.getAsJsonObject();
            String key = tObject.get("key").getAsString();
            TextView eventName = messageChunk.findViewById(R.id.eventName);
            TextView eventDate = messageChunk.findViewById(R.id.eventDate);
            TextView eventCity = messageChunk.findViewById(R.id.eventCity);
            eventName.setText(tObject.get("name").getAsString());
            eventDate.setText(tObject.get("start_date").getAsString());
            eventCity.setText(tObject.get("city").getAsString());
            //chunk onClicklistener
            Intent selectedTournament = new Intent(this, MainActivity.class);
            selectedTournament.putExtra("key", key);
            messageChunk.setOnClickListener(unused -> startActivity(selectedTournament));
            tournamentList.addView(messageChunk);
        }
    }
}
