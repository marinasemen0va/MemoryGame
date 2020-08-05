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
    PFont font, dyslexiaFont;
    int screen = 0, nextScreen;
        /*
        -1 - preloader
        0 - name
        1 - welcome
        2 - menu
        3 - info
        4 - exit
        5 - import files
        */
    String[][] packages;

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
        else if (screen == 3){
            info();
        }
        else if (screen == 4){
            exitGame();
        }
        else if (screen == 5){
            importFiles();
        }
        if (inTransition){
            transition();
        }
    }

    // key pressed
    public void keyPressed() {
        if (screen == 0) {
            if (name.length() >= 12) {
                error = "your name cannot be longer!";
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
        } else if (screen == 1) {
            if (key == ENTER) {
                nextScreen = 2;
                inTransition = true;
            }
        } else if (screen == 2){
            menu();
        }
    }

    // mouse clicked
    public void mouseClicked() {
        if (screen == 0) {
            if (mouseX >= width/2-60 && mouseX <= width/2 + 60 && mouseY >= height - 200 && mouseY <= height - 200 + 55) {
                if (name.equals("")){
                    name = "user";
                }
                nextScreen = 1;
                inTransition = true;
            }
        }
        if (screen == 1) {
            if (mouseX >= width/2-60 && mouseX <= width/2 + 60 && mouseY >= height - 200 && mouseY <= height - 200 + 55) {
                nextScreen = 2;
                inTransition = true;
            }
        }
        if (screen == 2) {
            if (sq(mouseX - (width - 75)) + sq(mouseY - (height - 75)) <= sq(30)) {
                nextScreen = 3;
                inTransition = true;
            }
            if (sq(mouseX - (width - 75)) + sq(mouseY - 75) <= sq(30)) {
                nextScreen = 4;
                inTransition = true;
            }
            if (mouseX >= 60 && mouseX <= 200 && mouseY >= height-100 && mouseY <= height-45){
                nextScreen = 5;
                inTransition = true;
            }
        }
        if (screen == 3) {
            if (mouseX >= width/2-60 && mouseX <= width/2 + 60 && mouseY >= height - 200 && mouseY <= height - 200 + 55) {
                nextScreen = 2;
                inTransition = true;
            }
        }
        if (screen == 4){
            if (mouseX >= width/2-60 && mouseX <= width/2 + 60 && mouseY >= height - 200 && mouseY <= height - 200 + 55) {
                exit();
            }
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
        rect(width/2 - 200, height/2, 400, 20);
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
            // loading animation
            load++;
            if (load == loading.length) {
                load = 0;
            }
        }
    }

    // name
    public void name() {
        background (255, 207, 204);
        rectFormat(width/2 - 200, height/2 - 25, 400, 50, 7, 209,167, 172, false);
        textFormat("Please enter your name:", width/2, height/2 - 75, 48, 1, 255, 255, 255);
        // restrict
        if (!name.equals("user")) {
            textFormat(name, width / 2 - 175, height / 2, 32, 2, 91, 91, 91); // 12 max
        }
        textFormat(error, width/2 - 180, height/2 + 50, 18, 2, 234, 7, 0);
        rectFormat(width/2-60, height - 200, 120, 55, 7, 209,167, 172, true);
        textFormat("next", width/2, height - 175, 36, 1, 209,167, 172);
    }

    // welcome
    public void welcome() {
        background (187, 255, 207);
        String s = "Welcome to I am Speed: a Memory Game, " + name + "!\nClick the information logo on the menu page to learn more about how to use this game! Hope you enjoy!";
        textFormat(s, width/2 - 500, height/2 - 300, 1000, 500, 46, 1, 255, 255, 255);
        rectFormat(width/2-60, height - 200, 120, 55, 7, 123,255, 168, true);
        textFormat("next", width/2, height - 175, 36, 1, 123,255, 168);
    }

    // menu
    public void menu() {
        background(166,242,255);
        rectFormat(width/2-400, 25, 800, 100, 7, 80, 211, 255, false);
        textFormat("I am Speed: a Memory Game", width/2, 75, 48, 1, 80, 211, 255);
        int files = fileIO();
        if (files == 0){
            textFormat("There are no packages yet :(", width/2, height/2, 36, 1, 255,255, 255);
        } else {
            for (int x = 0; x < files; x++){
                print("temp");
            }
        }
        ellipseFormat("i", width - 75, height - 75, 80, 211, 255, 0);
        ellipseFormat("x", width - 75, 75, 80, 211, 255, -3);
        rectFormat(60, height-100, 140, 55, 7, 80, 211, 255, true);
        textFormat("import", 75, height-75, 36, 2, 80, 211, 255);
    }

    // info
    public void info() {
        background(255,214,219);
        rectFormat(width/2-400, 25, 800, 100, 7, 215, 158, 156, false);
        textFormat("Instructions", width/2, 75, 48, 1, 215, 158, 156);
        String temp = "This game is meant for educational purposes, specifically to make learning faster and more efficient.";
        textFormat("txt", width/2 - 250, height/2 - 250, 500,500, 32, 1, 255,255,255);
        rectFormat(width/2-60, height - 200, 120, 55, 7, 215, 158, 156, true);
        textFormat("back", width/2, height - 175, 36, 1, 215, 158, 156);
    }

    // exit
    public void exitGame() {
        background(255,192,164);
        textFormat("Thanks for using this game!!!", width/2 - 250, height/2 - 250, 500,500, 48, 1, 255,255,255);
        rectFormat(width/2-60, height - 200, 120, 55, 7, 255,141,109, true);
        textFormat("close", width/2, height - 175, 36, 1, 255,141,109);
    }

    // import files
    public void importFiles() {
        background(207,255,149);
        rectFormat(width/2 - 200, height/2 - 25, 400, 50, 7, 110,217, 90, false);
        textFormat("Please enter the file name:", width/2, height/2 - 75, 48, 1, 255, 255, 255);
        textFormat("placeholder", width / 2 - 175, height / 2, 32, 2, 91, 91, 91);
        textFormat(error, width/2 - 180, height/2 + 50, 18, 2, 234, 7, 0);
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
    public void textShadow (String s, float a, float b, int ts) {
        fill(0, 0, 0, 75);
        if (ts <= 24){
            text(s, a + 1, b + 1);
        } else {
            text(s, a + 2, b + 2);
        }
    }

    // boxed text shadow
    public void textShadow (String s, float a, float b, float c, float d, int ts) {
        fill(0, 0, 0, 75);
        if (ts <= 24){
            text(s, a + 1, b + 1, c, d);
        } else {
            text(s, a + 2, b + 2, c, d);
        }
    }

    // unboxed text format
    public void textFormat (String s, float x, float y, int textSize, int textAlign, int r, int g, int b) {
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
    public void textFormat (String s, float x, float y, float w, float h, int textSize, int textAlign, int r, int g, int b){
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

    // bordered rect
    public void rectFormat(int x, int y, int w, int h, int r, int g, int b, boolean button) {
        if (button) {
            if (mouseX >= x && mouseX <= x+w && mouseY >= y && mouseY <= y+h){
                fill(0, 0, 0, 75);
                strokeWeight(5);
                stroke (0, 0, 0, 75);
                rect(x + 2, y + 2, w, h);
            }
        }
        fill(255);
        strokeWeight(5);
        stroke(r,g,b);
        rect(x, y, w, h);
    }

    // rounded bordered rect
    public void rectFormat(int x, int y, int w, int h, int radius, int r, int g, int b, boolean button) {
        if (button) {
            if (mouseX >= x && mouseX <= x+w && mouseY >= y && mouseY <= y+h){
                fill(0, 0, 0, 75);
                strokeWeight(5);
                stroke (0, 0, 0, 75);
                rect(x + 2, y + 2, w, h, radius);
            }
        }
        fill(255);
        strokeWeight(5);
        stroke(r,g,b);
        rect(x, y, w, h, radius);
    }

    // file IO
    public int fileIO() {
        return 0;
    } // TODO

    // ellipse format
    public void ellipseFormat(String s, int x, int y, int r, int g, int b, int adj){
        if (sq(mouseX - x) + sq(mouseY - y) <= sq(30)){
            fill(0, 0, 0, 75);
            strokeWeight(5);
            stroke (0, 0, 0, 75);
            ellipse(x + 3, y + 3, 60, 60);
        }
        fill(255);
        strokeWeight(5);
        stroke (r, g, b);
        ellipse(x, y, 60, 60);
        textFormat(s, x, y + adj, 48, 1, r, g, b);
    }

    // MAIN

    public static void main (String[] args){
        PApplet.main("Main", args);
    }
}
