package com.insta.processing.test1;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PApplet;

/**
 * @author Rodolphe
 *         <p/>
 *         Created at : 23/09/2014
 *         Project : com.insta.processing.1
 */
public class InstagramMedia implements Locatable {

    Geo geo;

    @Override
    public Marker getMarker(PApplet applet) {
        return new PlaceMarker(new Location(geo.latitude, geo.longitude), applet.loadImage("C:\\Users\\Rodolphe\\IdeaProjects\\cours\\com.insta.processing.1\\Projet-Twitter-Unfolding\\data\\img\\instagram.png"));
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
}
