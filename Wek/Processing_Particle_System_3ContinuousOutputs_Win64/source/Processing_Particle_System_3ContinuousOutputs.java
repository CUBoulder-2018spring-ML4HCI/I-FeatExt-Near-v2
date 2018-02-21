import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import oscP5.*; 
import netP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Processing_Particle_System_3ContinuousOutputs extends PApplet {

// Adapted from example Exercise 4.10 in
// The Nature of Code
// Daniel Shiffman
// http://natureofcode.com
// Released under LGPL 2.1 license: http://creativecommons.org/licenses/LGPL/2.1/
// Modified by Rebecca Fiebrink to accept control data from Wekinator
// Receives 3 continuous (regression) values, in range 0-1
// Listens on port 12000

// Array of Images for particle textures

ParticleSystem ps;

PImage[] imgs;

float p1 = 0.5f;
float p2 = 0.5f;
float p3 = 0.5f;

//Necessary for OSC communication with Wekinator:


OscP5 oscP5;
NetAddress dest;

public void setup() {
  
  
    //Initialize OSC communication
  oscP5 = new OscP5(this,12000); //listen for OSC messages on port 12000 (Wekinator default)
  dest = new NetAddress("127.0.0.1",6448); //send messages back to Wekinator on port 6448, localhost (this machine) (default)
  

  imgs = new PImage[5];
  imgs[0] = loadImage("corona.png");
  imgs[1] = loadImage("emitter.png");
  imgs[2] = loadImage("particle.png");
  imgs[3] = loadImage("texture.png");
  imgs[4] = loadImage("reflection.png");

  ps = new ParticleSystem(imgs, new PVector(width/2, 50));
}

public void draw() {

  // Additive blending!
  blendMode(ADD);

  background(0);
  
  //PVector up = new PVector(0,-0.2);
  
  PVector up = new PVector(0, -0.5f * (p3-0.5f));
  
  ps.applyForce(up);
  
  ps.run();
  for (int i = 0; i < 5; i++) {
    //ps.addParticle(mouseX,mouseY);
    ps.addParticle(p1 * width, p2 * height);
    
  }
  
  fill(0, 255, 0);
  text( "Use 3 continuous Wekinator outputs between 0 and 1", 5, 15 );
  text( "Listening for /wek/outputs on port 12000", 5, 30 );

}

//This is called automatically when OSC message is received
public void oscEvent(OscMessage theOscMessage) {
 if (theOscMessage.checkAddrPattern("/wek/outputs")==true) {
     if(theOscMessage.checkTypetag("fff")) { //Now looking for 2 parameters
        p1 = theOscMessage.get(0).floatValue(); //get this parameter
        p2 = theOscMessage.get(1).floatValue(); //get 2nd parameter
        p3 = theOscMessage.get(2).floatValue(); //get third parameters
        println("Received new params value from Wekinator");  
      } else {
        println("Error: unexpected params type tag received by Processing");
      }
 }
}
// The Nature of Code
// Daniel Shiffman
// http://natureofcode.com

// Simple Particle System

class Particle {
  PVector loc;
  PVector vel;
  PVector acc;
  float lifespan;

  PImage img;

  // Another constructor (the one we are using here)
  Particle(float x, float y, PImage img_) {
    // Boring example with constant acceleration
    acc = new PVector(0, 0);
    vel = PVector.random2D();
    loc = new PVector(x, y);
    lifespan = 255;
    img = img_;
  }

  public void run() {
    update();
    render();
  }

  public void applyForce(PVector f) {
    acc.add(f);
  }

  // Method to update location
  public void update() {
    vel.add(acc);
    loc.add(vel);
    acc.mult(0);
    lifespan -= 2.0f;
  }

  // Method to display
  public void render() {
    imageMode(CENTER);
    tint(lifespan);
    image(img, loc.x, loc.y, 32, 32);
  }

  // Is the particle still useful?
  public boolean isDead() {
    if (lifespan <= 0.0f) {
      return true;
    } 
    else {
      return false;
    }
  }
}
// The Nature of Code
// Daniel Shiffman
// http://natureofcode.com

// A class to describe a group of Particles
// An ArrayList is used to manage the list of Particles 

class ParticleSystem {

  ArrayList<Particle> particles;    // An arraylist for all the particles

  PImage[] textures;

  ParticleSystem(PImage[] imgs, PVector v) {
    textures = imgs;
    particles = new ArrayList();              // Initialize the arraylist
  }

  public void run() {
    for (int i = particles.size()-1; i >= 0; i--) {
      Particle p = particles.get(i);
      p.run();
      if (p.isDead()) {
        particles.remove(i);
      }
    }
  }

  public void addParticle(float x, float y) {
    int r = PApplet.parseInt(random(textures.length));
    particles.add(new Particle(x,y,textures[r]));
  }
  
  
  public void applyForce(PVector f) {
    for (Particle p : particles) {
      p.applyForce(f);
    }
  }

  public void addParticle(Particle p) {
    particles.add(p);
  }

  // A method to test if the particle system still has particles
  public boolean dead() {
    if (particles.isEmpty()) {
      return true;
    } 
    else {
      return false;
    }
  }
}
  public void settings() {  size(640, 360, P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Processing_Particle_System_3ContinuousOutputs" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
