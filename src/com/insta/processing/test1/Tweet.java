package com.insta.processing.test1;

import com.temboo.Library.Twitter.Search.Tweets;
import com.temboo.core.TembooSession;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import processing.core.PApplet;

import java.util.ArrayList;

/**
 * @author Azzur
 *         <p/>
 *         Created at : 22/09/2014
 *         Project : com.insta.processing.1
 */
public class Tweet implements Locatable {

    private int id;
    private String text;
    private Geo geo;
    private TweetPlace place;
    private String mediaUrl;
    private String author;


    public static ArrayList<Tweet> parseJSON(String response) throws JSONException {
        System.out.println("Retrieving Tweets");
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

            if (location != null || place != null) {
                status.setMediaInfos(
                        statusJSON.getString("text"),
                        "",
                        statusJSON.getJSONObject("user").getString("screen_name")
                );
                statuses.add(status);
            }

        }
        System.out.println(len + " founded, "+statuses.size()+" localized");
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
    public void setGeo(double latitude, double longitude) {
        this.geo = new Geo();
        this.geo.latitude = latitude;
        this.geo.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", geo=" + geo +
                '}';
    }

    @Override
    public Marker getMarker(PApplet applet) {

        if (this.geo != null) {
            PlaceMarker marker = new PlaceMarker(new Location(geo.latitude, geo.longitude), applet.loadImage("https://cdn3.iconfinder.com/data/icons/follow-me/256/Twitter-32.png"));
            marker.setMediaInfos(text, null, author);
            marker.setEngine(applet);
            return marker;
        }

        return null;
    }

    public void setMediaInfos(String text, String mediaUrl, String author) {
        this.text = text;
        this.mediaUrl = mediaUrl;
        this.author = author;

    }

    public static ArrayList<Tweet> getTweetSearch(String search, TembooSession session) throws JSONException {
        Tweets tweetsChoreo = new Tweets(session);
        tweetsChoreo.setAccessToken("2775368387-j63m9FFKfDGMH7FSqVYTglvN7ZcJdsuxe1MpwSz");
        tweetsChoreo.setAccessTokenSecret("JfFDFqaJsa2CNNVY1my20TkGaDSzxmT695p3vnGv2Pdzz");
        tweetsChoreo.setConsumerSecret("l1EvbO2LYN5FCWrMoFxT1bI7GkbteYlBBqsebrYbDiEEBVzHZ3");
        tweetsChoreo.setConsumerKey("6m1HphSLGe99BnixQGCPmG4W0");
        tweetsChoreo.setQuery(search);
        tweetsChoreo.setCount(200);

        String result = tweetsChoreo.run().getResponse();

        return Tweet.parseJSON(result);

    }
}
