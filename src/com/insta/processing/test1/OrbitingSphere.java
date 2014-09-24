package com.insta.processing.test1;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * @author Rodolphe
 *         <p/>
 *         Created at : 24/09/2014
 *         Project : com.insta.processing.1
 */
public class OrbitingSphere {

    int radius;
    PApplet engine;
    PVector location;
    PVector acceleration;
    PVector center;
    PVector velocity;
    float topSpeed = 10.f;

    public OrbitingSphere(int radius, PVector pVector, PApplet engine) {
        this.radius = radius;
        this.engine = engine;
        location = pVector;
        acceleration = PVector.random3D();
        velocity = PVector.random3D();
        center = new PVector(
                0,0,400
        );
    }

    void update() {

        // Our algorithm for calculating acceleration:

        PVector dir = PVector.sub(center,location);  // Find vector pointing towards mouse
        dir.normalize();     // Normalize
//        dir.mult((float) 0.005);       // Scale
        acceleration = dir;  // Set to acceleration

        // Motion 101!  Velocity changes by acceleration.  Location changes by velocity.
        velocity.add(acceleration);
        velocity.limit(topSpeed);
        location.add(velocity);
    }

    void display() {
        engine.pushMatrix();
        engine.noStroke();
        engine.fill(0xff3f729b);
        engine.translate(location.x, location.y, location.z);
        engine.sphere(10);
        engine.popMatrix();
    }
}
