package com.insta.processing.test1;

import com.temboo.Library.Instagram.RecentlyTaggedMedia;
import com.temboo.Library.Instagram.SearchTags;
import com.temboo.Library.Twitter.Trends.Place;
import com.temboo.core.TembooSession;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import processing.core.PApplet;

import java.util.ArrayList;

/**
 * @author Rodolphe
 *         <p/>
 *         Created at : 23/09/2014
 *         Project : com.insta.processing.1
 */
public class InstagramMedia implements Locatable {

    Geo geo;
    String text;
    String mediaUrl;
    String author;

    @Override
    public Marker getMarker(PApplet applet) {
        PlaceMarker marker = new PlaceMarker(new Location(geo.latitude, geo.longitude), applet.loadImage("https://cdn3.iconfinder.com/data/icons/follow-me/256/Instagram-32.png"));
        marker.setMediaInfos(text, applet.loadImage(mediaUrl), author );
        marker.setEngine(applet);

        return marker;
    }

    public void setMediaInfos(String text, String mediaUrl, String author) {
        this.text = text;
        this.mediaUrl = mediaUrl;
        this.author = author;

    }

    @Override
    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    @Override
    public void setGeo(double latitude, double longitude) {
        this.geo = new Geo();
        this.geo.latitude = latitude;
        this.geo.longitude = longitude;
    }

    public static ArrayList<InstagramMedia> parseJSON(String input) throws JSONException {
        ArrayList<InstagramMedia> medias = new ArrayList<InstagramMedia>();
        System.out.println("Retrieving Instragram Media");
        JSONObject jsonObject = new JSONObject(input);
        JSONArray data = jsonObject.getJSONArray("data");
        if (data.length() > 0) {
            for (int i = 0 ; i < data.length() ; i++) {
                JSONObject instagramJSON = data.getJSONObject(i);
                JSONObject location = instagramJSON.getJSONObject("location");
                if (location  != null) {
                    InstagramMedia instagramMedia = new InstagramMedia();
                    instagramMedia.setGeo(location.getDouble("latitude"), location.getDouble("longitude"));
                    instagramMedia.setMediaInfos(
                            (instagramJSON.getJSONObject("caption") != null ? instagramJSON.getJSONObject("caption").getString("text") : ""),
                            instagramJSON.getJSONObject("images").getJSONObject("thumbnail").getString("url"),
                            instagramJSON.getJSONObject("user").getString("username")
                    );
                    medias.add(instagramMedia);
                }
            }
        }
        System.out.println(data.length() + "founded, "+medias.size()+" localized");
        return medias;
    }

    public static ArrayList<InstagramMedia> getInstagramMedias(String search, TembooSession session) throws JSONException {
        ArrayList<InstagramMedia> medias = new ArrayList<InstagramMedia>();
        SearchTags searchTags = new SearchTags(session);
        searchTags.setAccessToken("53267369.1fb234f.9bfabe4bbfc746278a4db7f1c3cc3467");
        searchTags.setQuery(search);

        String result = searchTags.run().getResponse();

        JSONObject jsonObject = new JSONObject(result);
        JSONArray data = jsonObject.getJSONArray("data");
        if (data.length() > 0) {
            RecentlyTaggedMedia media = new RecentlyTaggedMedia(session);
            media.setAccessToken(/*oAuth.run().getAccessToken()*/"53267369.1fb234f.9bfabe4bbfc746278a4db7f1c3cc3467");
            media.setTagName(data.getJSONObject(0).getString("name"));
            media.setClientID("15c2b97cc14d47768b9ab1052efeb016");


            result = media.run().getResponse();
            medias = InstagramMedia.parseJSON(result);
        }

        return medias;
    }
}
