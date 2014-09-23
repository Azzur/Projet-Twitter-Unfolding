package com.insta.processing.test1;

import com.temboo.Library.Instagram.*;
import com.temboo.Library.Twitter.Search.Tweets;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.Location;
import controlP5.*;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import processing.core.*;
import com.temboo.core.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Rodolphe
 *         <p/>
 *         Created at : 19/09/2014
 *         Project : com.insta.processing.1
 */
public class Program extends PApplet {
    ControlP5 cp5;
    UnfoldingMap map;
    TembooSession session = new TembooSession("senorihl", "Processing", "ee23a86b447a45eba3c7f798bb1aa1b4");
    Textfield textfield;
    String searchField = "#subway";
    Button button;

    public void setup() {
        size(1000, 600, OPENGL);
        background(0,0,0);
        pushMatrix();
        fill(0 , 0, 0);
        rect(209, 9, 782, 582);
        popMatrix();
        pushMatrix();
        fill(255,255,255);
        stroke(0,0,0);
        rect(9,9, 202, 582 );
        popMatrix();
        noStroke();
        noFill();
        PFont font = createFont("arial",20);

        cp5 = new ControlP5(this);

        textfield = cp5.addTextfield("input")
                .setFont(font)
                .setPosition(15,15)
                .setSize(185,40)
                .setFocus(true)
                .setColor(color(255,0,0))
                .setText(searchField)
        ;
        button = cp5.addButton("submitForm")
                .setPosition(15, 60)
                .setSize(185, 40);
        button = cp5.addButton("resetZoom")
                .setPosition(15,545)
                .setSize(185,40);

        map = new UnfoldingMap(this , 210, 10, 780 , 580, new Google.GoogleTerrainProvider());

        MapUtils.createDefaultEventDispatcher(this, map);
        thread("updateMap");

    }

    public void draw() {

        map.draw();
    }

    public static void main(String[] args) {
        PApplet.main(
                "com.insta.processing.test1.Program"
        );
    }

    ArrayList<Tweet> getTweetSearch(String search) throws JSONException {

        System.out.println("Retrieving Tweets");

        Tweets tweetsChoreo = new Tweets(session);
        tweetsChoreo.setAccessToken("89814238-xX3Grmyv8jteVfPCkjM203OKAMfDWhXjPop3OUCp9");
        tweetsChoreo.setAccessTokenSecret("OSJ0anNnFfSMrIWlUREKjku8z0Un4PKfdl3O8gbIT5R1E");
        tweetsChoreo.setConsumerSecret("lINt19NMyVCHZP6Gqs0LyKibuv9DnanMzDqqNPXHkTFEjjrwt8");
        tweetsChoreo.setConsumerKey("OrniWZuYBj5Nns3SqbbY7Wb7T");
        tweetsChoreo.setQuery(search);
        tweetsChoreo.setCount(200);

        String result = tweetsChoreo.run().getResponse();

        return Tweet.parseJSON(result);

    }

    ArrayList<InstagramMedia> getInstagramMedias(String search) throws JSONException {
        System.out.println("Retrieving Instragram Media");
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
            jsonObject = new JSONObject(result);
            data = jsonObject.getJSONArray("data");
            if (data.length() > 0) {
                for (int i = 0 ; i < data.length() ; i++) {
                    JSONObject location = data.getJSONObject(i).getJSONObject("location");
                    if (location  != null) {
                        InstagramMedia instagramMedia = new InstagramMedia();
                        System.out.println(location);
                        instagramMedia.setGeo(location.getDouble("latitude"), location.getDouble("longitude"));
                        medias.add(instagramMedia);
                    }
                }

            }
            System.out.println(data.length() + "founded, "+medias.size()+" localized");

        }

        return medias;
    }

    public void submitForm() {
        searchField = textfield.getText();
        thread("updateMap");
    }

    public void updateMap() {

        button.setOff();

        map.getDefaultMarkerManager().clearMarkers();

        try {
            ArrayList<Locatable> localisations = new ArrayList<Locatable>();
            localisations.addAll(getTweetSearch(searchField));
            localisations.addAll(getInstagramMedias(searchField));

            for (Locatable localisation : localisations) {
                Marker marker = localisation.getMarker(this);
                if (marker != null) {
                    map.addMarker(marker);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        button.setOn();
    }

    public void resetZoom() {
        map.zoomLevelOut();
    }

}
