/*
  author: Marina Semenova (@marinasemen0va)

  This is the main file.
*/
 
// IMPORTS
import java.util.ArrayList;
import java.util.Arrays;
import processing.sound.*;

// VARIABLES

// main
PFont font, langFont;
int screen = 2, nextScreen;
    /*
    -1 - preloader
    0 - name
    1 - welcome
    2 - menu
    3 - info
    35 - info display
    4 - exit
    5 - import files
    6 - scores
    7 - choose mode
    8 - instructions
    9 - flashcards
    10 - matching
    11 - typing
    12 - score display
    */
int buttonW = 40, buttonH = 60;
int maxPackages, maxScores;
int r, g, b, grey = 125;

// SETUP

public void setup() {
    fullScreen();
    frameRate(60);
    font = loadFont("project files/CenturyGothic-Bold-100.vlw");
    langFont = createFont("font/cardFont.ttf", 100);
    textFont(font);
    maxPackages = 10;
    maxScores = 11;
    startupImport();
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
    } else if (screen == 3){
        info();
    } else if (screen == 35) {
        infoDisplay();
    } else if (screen == 4){
        exitGame();
    } else if (screen == 5){
        importFiles();
    } else if (screen == 6) {
        scores();
    } else if (screen == 7) {
        chooseMode();
    } else if (screen == 8) {
        instructions();
    } else if (screen == 9) {
        flashcards();
    } else if (screen == 10) {
        matching();
    } else if (screen == 11) {
        typing();
    } else if (screen == 12) {
        scoreDisplay();
    }
    if (inTransition){
        transition();
    }
}
