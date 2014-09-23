package com.insta.processing.test1;

import com.temboo.Library.Instagram.*;
import com.temboo.Library.Twitter.Search.Tweets;
import controlP5.Button;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.Location;
import controlP5.*;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.Yahoo;
import de.fhpotsdam.unfolding.utils.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import processing.core.*;
import com.temboo.core.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Azzur
 *         <p/>
 *         Created at : 19/09/2014
 *         Project : com.insta.processing.1
 */
public class Program extends PApplet {
    ControlP5 cp5;
    UnfoldingMap map,map1,map2,currentMap;
    TembooSession session = new TembooSession("azzur", "TwitterApp", "66138148fcf54ef9a5e806ac600bd079");
    Textfield textfield;
    String searchField = "#subway";
    Button button,button2;
    ArrayList<UnfoldingMap>maps = new ArrayList<UnfoldingMap>();
    PImage buttonimg,buttonimg2;
    List<Marker> markers = new ArrayList<Marker>();


    public void setup() {
        size(displayWidth, displayHeight, OPENGL);

        smooth();
        background(0,0,0);
        pushMatrix();
        fill(204,204,204);
        stroke(0,0,0);
        rect(9,9, 202, displayWidth );
        popMatrix();
        noStroke();
        noFill();
        PFont font = createFont("arial",20);

        cp5 = new ControlP5(this);



        textfield = cp5.addTextfield("input")
                .setFont(font)
                .setPosition(15,150)
                .setSize(185,40)
                .setFocus(true)
                .setColor(color(255,0,0))
                .setText(searchField)
        ;

        buttonimg2 = loadImage("C:\\Users\\Azzzur\\Desktop\\insta.png");
        button2 = cp5.addButton("submitForm2")
                .setPosition(40, 30)
                .setImage(buttonimg2)
                .setSize(173, 238);


        buttonimg = loadImage("https://cdn1.iconfinder.com/data/icons/windows-8-metro-style/64/google_web_search.png");
        button = cp5.addButton("submitForm")
                .setPosition(70, 200)
                .setImage(buttonimg)
                .setSize(173, 238);

        button = cp5.addButton("resetZoom")
                .setPosition(15, 280)
                .setSize(185, 40);


        maps.add(new UnfoldingMap(this , 210, 10, displayWidth, displayHeight, new Microsoft.AerialProvider()));
        maps.add(new UnfoldingMap(this, 210, 10, displayWidth, displayHeight, new Google.GoogleMapProvider()));
        maps.add(new UnfoldingMap(this,210, 10, displayWidth, displayHeight, new Yahoo.RoadProvider()));
        map = new UnfoldingMap(this , 210, 10, displayWidth, displayHeight, new Microsoft.AerialProvider());
        map1 = new UnfoldingMap(this,210, 10, displayWidth, displayHeight, new Google.GoogleMapProvider());
        map2 = new UnfoldingMap(this,210, 10, displayWidth, displayHeight, new Yahoo.RoadProvider());
        MapUtils.createDefaultEventDispatcher(this, map,map1, map2);
        currentMap = map;
        currentMap.setTweening(true);
        updateMap();

    }

    public void keyPressed() {
        if (key == '1') {
            currentMap = map;
            currentMap.setZoomRange(3, 20);
            updateMap();
        } else if (key == '2') {
            currentMap = map1;
            currentMap.setZoomRange(3, 20);
            updateMap();
        } else if (key == '3') {
            currentMap = map2;
            currentMap.setZoomRange(3, 20);
            updateMap();
        }

    }

    public void draw() {
        if(frameCount%120==0)
        {
            System.out.println(textfield.getInfo());
        }
        currentMap.draw();
    }


    public static void main(String[] args) {
        PApplet.main(
                "com.insta.processing.test1.Program"
        );
    }

    ArrayList<Tweet> getTweetSearch(String search) throws JSONException {
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
        updateMap();
    }

    public void updateMap() {

        button.setOff();

        currentMap.getDefaultMarkerManager().clearMarkers();
        if(markers.size()>0)
        {
            currentMap.addMarkers(markers);
        }
        else
        {
            try {
                ArrayList<Locatable> localisations = new ArrayList<Locatable>();
                localisations.addAll(getTweetSearch(searchField));
                localisations.addAll(getInstagramMedias(searchField));

                for (Locatable localisation : localisations) {
                    Marker marker = localisation.getMarker(this);
                    if (marker != null) {
                        currentMap.addMarker(marker);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        button.setOn();
    }

    public void resetZoom() {
        currentMap.zoomLevelOut();
    }

}
