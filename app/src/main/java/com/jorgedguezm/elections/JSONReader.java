package com.jorgedguezm.elections;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by jorge on 13/11/16.
 */

public class JSONReader {
    private Institution institution;
    private Votes votes;
    private ArrayList<Party> parties = new ArrayList();
    private Context c;

    public JSONReader(Context c) {
        this.c = c;
    }

    public void loadJSON(String JSONpath) {
        String json;

        try {
            InputStream is = c.getAssets().open(JSONpath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            json = null;
        }

        if(json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);

                JSONObject institutions = jsonObject.getJSONObject("institution");
                this.institution = new Institution(institutions.getString("name"),
                        institutions.getString("electName"), institutions.getInt("totalElects"),
                        institutions.getString("place"), institutions.getInt("year"));

                JSONObject votes = jsonObject.getJSONObject("votes");
                this.votes = new Votes(votes.getInt("counted"), votes.getInt("abstentions"),
                        votes.getInt("null"), votes.getInt("blank"),
                        votes.getDouble("scrutinized"));

                JSONArray parties = jsonObject.getJSONArray("parties");
                for (int i = 0; i < parties.length(); i++) {
                    JSONObject one = parties.getJSONObject(i);
                    Party party = new Party(one.getString("name"), one.getInt("elects"),
                            one.getInt("nVotes"), this.votes, one.getString("color"));
                    this.parties.add(party);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Institution getInstitution() {
        return institution;
    }

    public Votes getVotes() {
        return votes;
    }

    public ArrayList<Party> getParties() {
        return parties;
    }
}
