package com.insta.processing.test1;

import de.fhpotsdam.unfolding.marker.Marker;
import processing.core.PApplet;

/**
 * @author Rodolphe
 *         <p/>
 *         Created at : 23/09/2014
 *         Project : com.insta.processing.1
 */
public interface Locatable {

    Marker getMarker(PApplet applet);
    void setGeo(Geo geo);
    void setGeo(double latitude, double longitude);


}
