/*
    author: Marina Semenova (@marinasemen0va)

    This is the main class.
 */

// imports
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

// code
public class Main extends PApplet {
    // VARIABLES

    // main
    boolean start = true;
    PImage bg;
    PFont font, dyslexiaFont;
    int screen = 1, nextScreen; // TODO
        /*
        0 - name
        1 - welcome
        2 - menu
        3 -
        */

    // transition
    int transitionTime = 0;
    boolean inTransition = false;

    // preloader
    int barV = 0;
    int load = 0;

    // name
    String name = "";
    String error = "";

    // SETUP

    // settings
    public void settings() {
        fullScreen();
        smooth(0);
        bg = loadImage("bg.jpg");
    }

    // setup
    public void setup() {
        frameRate(60);
        font = loadFont("CenturyGothic-Bold-48.vlw");
        dyslexiaFont = createFont("open dyslexic/OpenDyslexic-Regular.otf", 32);
        textFont(font);
    }

    // STANDARD METHODS

    // draw
    public void draw() {
        if (screen == -1) {
            preloader();
        } else if (screen == 0){
            name();
        } else if (screen == 1){
            welcome();
        } else if (screen == 2){
            menu();
        }
        if (inTransition){
            transition();
        }
    }

    // key pressed
    public void keyPressed() {
        if (screen == 0) {
            if (key == ENTER) {
                if (name == ""){
                    name = "user";
                }
                nextScreen = 1;
                inTransition = true;
            } else {
                if (name.length() >= 12) {
                    error = "enter a shorter name!";
                    if (key == BACKSPACE) {
                        name = name.substring(0, name.length()-1);
                        error = "";
                    }
                } else {
                    error = "";
                    if (key == BACKSPACE) {
                        if (name.length() <= 1){
                            name = "";
                        } else {
                            name = name.substring(0, name.length() - 1);
                        }
                    }
                    else {
                        name = name + key;
                    }
                }
            }
        } else if (screen == 1) {
            if (key == ENTER) {
                nextScreen = 2;
                inTransition = true;
            }
        } else if (screen == 2){
            menu();
        }
    }

    // CUSTOM METHODS

    // preloader
    public void preloader() {
        background(184, 206, 245);
        // text
        String[] loading = {"Loading","Loading.","Loading..", "Loading..."};
        textFormat(loading[load], width/2 - 115, height/2 - 50, 48, 2, 255, 255, 255);
        // bar
        noStroke();
        fill(255);
        rect(width/2 - 200, height/2, barV, 20);
        // border
        noFill();
        strokeWeight(5);
        stroke(151,180,238);
        rect(width/2 - 200, height/2, 405, 20);
        // compute
        if (barV >= 400) {
            nextScreen = 0;
            inTransition = true;
        } else {
            barV += random(0,30);
            if (barV > 400) {
                barV = 400;
            }
            delay(200);
        }
        // loading animation
        load++;
        if (load == loading.length) {
            load = 0;
        }
    }

    public void name() {
        background (255, 207, 204);
        // input box
        fill(255);
        noStroke();
        rect(width/2 - 200, height/2 - 25, 400, 50, 7);
        // border
        noFill();
        strokeWeight(5);
        stroke(209,167,172);
        rect(width/2 - 200, height/2 - 25, 400, 50, 7);
        // text
        textFormat("Please enter your name:", width/2, height/2 - 75, 48, 1, 255, 255, 255);
        // restrict
        if (!name.equals("user")) {
            textFormat(name, width / 2 - 175, height / 2, 32, 2, 91, 91, 91); // 12 max
        }
        textFormat(error, width/2 - 180, height/2 + 50, 18, 2, 234, 7, 0);
    }

    // welcome
    public void welcome() {
        background (187, 255, 207);
        String s = "Welcome to I am Speed: a Memory Game, " + name + "!\nClick the information logo on the menu page to learn more about how to use this game! Hope you enjoy!\n\n - M (the creator)";
        textFormat(s, width/2 - 500, height/2 - 250, 1000, 500, 48, 1, 255, 255, 255);
    }

    // menu
    public void menu() {
        image(bg, 0, 0, width, height);
        filter(BLUR);
    }

    // info
    public void info() {
        String temp = "This game is meant for educational purposes, specifically to make learning faster and more efficient.";
    }
    // HELPER METHODS

    // transition
    public void transition() {
        transitionTime++;
        float transparency = (-1 * abs(transitionTime - 25) + 25) * 10;
        if (transitionTime == 25) {
            screen = nextScreen;
        }
        if (transitionTime > 50) {
            inTransition = false;
            transitionTime = 0;
        }
        fill(0, transparency);
        noStroke();
        rect(0, 0, width, height);
    }

    // unboxed text shadow
    public void textShadow (String s, int a, int b, int ts) {
        fill(0, 0, 0, 75);
        if (ts <= 24){
            text(s, a + 1, b + 1);
        } else {
            text(s, a + 2, b + 2);
        }
    }

    // boxed text shadow
    public void textShadow (String s, int a, int b, int c, int d, int ts) {
        fill(0, 0, 0, 75);
        if (ts <= 24){
            text(s, a + 1, b + 1, c, d);
        } else {
            text(s, a + 2, b + 2, c, d);
        }
    }

    // unboxed text format
    public void textFormat (String s, int x, int y, int textSize, int textAlign, int r, int g, int b) {
        if (textAlign == 1) {
            textAlign(CENTER, CENTER);
        } else if (textAlign == 2) {
            textAlign(LEFT, CENTER);
        } else {
            textAlign(RIGHT, CENTER);
        }
        textSize(textSize);
        textShadow(s, x, y, textSize);
        fill(r, g, b);
        text(s, x, y);
    }

    // boxed text format
    public void textFormat (String s, int x, int y, int w, int h, int textSize, int textAlign, int r, int g, int b){
        if (textAlign == 1) {
            textAlign(CENTER, CENTER);
        } else if (textAlign == 2) {
            textAlign(LEFT, CENTER);
        } else {
            textAlign(RIGHT, CENTER);
        }
        textSize(textSize);
        textShadow(s, x, y, w, h, textSize);
        fill(r, g, b);
        text(s, x, y, w, h);
    }

    // MAIN

    public static void main (String[] args){
        PApplet.main("Main", args);
    }
}
