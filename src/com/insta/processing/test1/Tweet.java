package com.insta.processing.test1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Azzur
 *         <p/>
 *         Created at : 22/09/2014
 *         Project : com.insta.processing.1
 */
public class Tweet {

    private int id;
    private String text;
    private Geo geo;
    private TweetPlace place;


    public static ArrayList<Tweet> parseJSON(String response) throws JSONException {
        ArrayList<Tweet> statuses = new ArrayList<Tweet>();
        JSONArray tweets = (new JSONObject(response)).getJSONArray("statuses");
        int len = tweets.length();

        for (int i = 0 ; i < len ; i++) {
            JSONObject statusJSON = tweets.getJSONObject(i);
            Tweet status = new Tweet();
            status.setId(statusJSON.getInt("id"));
            status.setText(statusJSON.getString("text"));
            JSONObject location = statusJSON.getJSONObject("geo");

            if (null != location) {
                Geo geo = new Geo();
                geo.latitude = location.getJSONArray("coordinates").getDouble(0);
                geo.longitude = location.getJSONArray("coordinates").getDouble(1);
                status.setGeo(geo);

            } else { status.setGeo(null); }

            JSONObject place = statusJSON.getJSONObject("place");

            if (null != place) {
                status.setPlace(TweetPlace.parseJSON(place.toString()));

            } else { status.setPlace(null); }

            statuses.add(status);

        }

        return statuses;
    }

    public TweetPlace getPlace() {
        return place;
    }

    public void setPlace(TweetPlace place) {
        this.place = place;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", geo=" + geo +
                '}';
    }
}
