package com.insta.processing.test1;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * @author Rodolphe
 *         <p/>
 *         Created at : 24/09/2014
 *         Project : com.insta.processing.1
 */
public class OrbitingSystem {

    OrbitingSphere[] spheres = new OrbitingSphere[10];
    PApplet engine;

    public OrbitingSystem(PApplet engine) {
        this.engine = engine;
        for (int i = 0 ; i <10; i++) {
            PVector location = new PVector(
                    engine.random(-300,300),
                    engine.random(-300,300),
                    engine.random(-300,300)
            );
            spheres[i] = (new OrbitingSphere(5, location, engine));
        }
    }

    public void draw() {
        for (int i = 0 ; i <10; i++) {
            spheres[i].update();
            spheres[i].display();
        }
    }
}
