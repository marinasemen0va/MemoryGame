/*
    author: Marina Semenova (@marinasemen0va)

    This is the main class.
 */

// imports
import processing.core.PApplet;
import processing.core.PFont;
import java.util.ArrayList;
import java.util.Arrays;

// code
public class Main extends PApplet {
    // VARIABLES

    // main
    PFont font, dyslexiaFont;
    int screen = -1, nextScreen;
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
        11 - score display
        */
    int currentPackage;

    // transition
    int transitionTime = 0;
    boolean inTransition = false;

    // preloader
    int barV = 0;
    int load = 0;

    // name
    String name = "";
    String error = "";

    // file stuff
    ArrayList<String[]> packages = new ArrayList<String[]>();
    ArrayList<String> fileNames = new ArrayList<String>();
    String fileName = "";
    int packagesNum = 0;
    boolean edit = false;
    boolean[] toDelete;
    String f = "packages/packages.txt";

    // scores
    ArrayList<String[]> scores = new ArrayList<String[]>();
    int mode; //TODO use game mode to keep track of scores (each pos will have [game mode, rest of scores])
        /*
        1 - flashcards
        2 - matching
         */

    // info
    int infoChoice;
        /*
        1 - about
        2 - importing
        3 - gameplay
         */

    // SETUP

    // settings
    public void settings() {
        fullScreen();
        smooth(0);
    }

    // setup
    public void setup() {
        frameRate(60);
        font = loadFont("project files/CenturyGothic-Bold-48.vlw");
        dyslexiaFont = createFont("project files/open dyslexic/OpenDyslexic-Regular.otf", 32);
        textFont(font);
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
            scoreDisplay();
        }
        if (inTransition){
            transition();
        }
    }

    // key pressed
    public void keyPressed() {
        if (key != ENTER) {
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
            }
            if (screen == 5) {
                if (fileName.length() >= 12) {
                    error = "the file name cannot be longer!";
                    if (key == BACKSPACE) {
                        fileName = fileName.substring(0, fileName.length()-1);
                        error = "";
                    }
                } else {
                    error = "";
                    if (key == BACKSPACE) {
                        if (fileName.length() <= 1){
                            fileName = "";
                        } else {
                            fileName = fileName.substring(0, fileName.length() - 1);
                        }
                    }
                    else {
                        fileName = fileName + key;
                    }
                }
            }
        }
    }

    // mouse clicked
    public void mouseClicked() {
        if (screen == 0) {
            if (checkRect(width/2-60, height - 200, 120, 55)) {
                if (name.equals("")){
                    name = "user";
                }
                nextScreen = 1;
                inTransition = true;
            }
        }
        if (screen == 1) {
            if (checkRect(width/2-60, height - 200, 120, 55)) {
                nextScreen = 2;
                inTransition = true;
            }
        }
        if (screen == 2) {
            if (!edit && sq(mouseX - (width - 75)) + sq(mouseY - (height - 75)) <= sq(30)) { // info
                nextScreen = 3;
                inTransition = true;
            }
            if (!edit && sq(mouseX - (width - 75)) + sq(mouseY - 75) <= sq(30)) { // exit
                nextScreen = 4;
                inTransition = true;
            }
            if (!edit && checkRect(50, 45, 140, 55)) {
                nextScreen = 6;
                inTransition = true;
            }
            if (packagesNum == 0) {
                if (sq(mouseX - 75) + sq(mouseY - (height - 75)) <= sq(30)) { // +
                    nextScreen = 5;
                    inTransition = true;
                }
            } else {
                if (edit && checkRect(width - 200, height - 100, 120, 55)) { // back from edit
                    edit = false;
                    toDelete = null;
                } else if (edit && sq(mouseX - 75) + sq(mouseY - (height - 75)) <= sq(30)) { // +
                    nextScreen = 5;
                    inTransition = true;
                } else if (!edit && checkRect(60, height-100, 140, 55)) { // edit
                    edit = true;
                    toDelete = new boolean[packagesNum];
                    for (int x =0; x < packagesNum; x++){
                        toDelete[x] = false;
                    }
                }
            }
            // button stuff
            if (edit) { // add to delete list & brighten/unbrighten
                for (int x = 0; x < packagesNum; x++) {
                    if (checkRect(width/2-300, 175 + (75 * x), 600, 50)) {
                        toDelete[x] = !toDelete[x];
                    }
                }
            } else { // go to game
                for (int x = 0; x < packagesNum; x++) {
                    if (checkRect(width/2-300, 175 + (75 * x), 600, 50)) {
                        currentPackage = x;
                        nextScreen = 7;
                        inTransition = true;
                    }
                }
            }
            if (edit && checkDelete() && checkRect(width / 2 - 70, height - 75, 140, 55)) {
                runDelete();
                edit = false;
                toDelete = null;
            }
        }
        if (screen == 3) {
            if (checkRect(width/2-60, height - 150, 120, 55)) {
                nextScreen = 2;
                inTransition = true;
            }
            if (checkRect(width/2 - 480, height/2 - 50, 240, 100)) {
                infoChoice = 1;
                nextScreen = 35;
                inTransition = true;
            }
            if (checkRect(width/2 - 120, height/2 - 50, 240, 100)) {
                infoChoice = 2;
                nextScreen = 35;
                inTransition = true;
            }
            if (checkRect(width/2 + 240, height/2 - 50, 240, 100)) {
                infoChoice = 3;
                nextScreen = 35;
                inTransition = true;
            }
        }
        if (screen == 35) {
            if (checkRect(width/2-60, height - 150, 120, 55)) {
                nextScreen = 3;
                inTransition = true;
            }
        }
        if (screen == 4){
            if (checkRect(width/2-60, height - 200, 120, 55)) {
                exit();
            }
        }
        if (screen == 5) {
            if (checkRect(width/2-60, height - 200, 120, 55)) {
                if (fileName.equals("")){
                    error = "enter a file name!";
                } else {
                    boolean status = fileIO(fileName);
                    if (status) {
                        edit = false;
                        toDelete = null;
                        nextScreen = 2;
                        inTransition = true;
                    } else {
                        error = "oops! something went wrong!";
                    }
                }
            }
            if (checkRect(60, 120, height - 100, 55)) {
                edit = false;
                toDelete = null;
                nextScreen = 2;
                inTransition = true;
            }
        }
        if (screen == 6) {
            if (checkRect(width - 200, 45, 120, 55)) {
                nextScreen = 2;
                inTransition = true;
            }
        }
        if (screen == 7) {
            if (checkRect(width/2 - 400, height/2 - 100, 300, 200)) {
                mode = 1;
                nextScreen = 8;
                inTransition = true;
            }
            if (checkRect(width/2 + 100, height/2 - 100, 300, 200)) {
                mode = 2;
                nextScreen = 8;
                inTransition = true;
            }
        }
        if (screen == 8) {
            if (checkRect(width/2-60, height - 150, 120, 55)) {
                if (mode == 1) {
                    nextScreen = 9;
                    inTransition = true;
                }
                if (mode == 2) {
                    nextScreen = 10;
                    inTransition = true;
                }
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
        rectFormat(width/2 - 200, height/2 - 25, 400, 50, 209,167, 172, false);
        textFormat("Please enter your name:", width/2, height/2 - 75, 48, 1, 255, 255, 255);
        // restrict
        if (!name.equals("user")) {
            textFormat(name, width / 2 - 175, height / 2, 32, 2, 91, 91, 91); // 12 max
        }
        textFormat(error, width/2 - 180, height/2 + 50, 18, 2, 234, 7, 0);
        rectFormat(width/2-60, height - 200, 120, 55, 209,167, 172, true);
        textFormat("next", width/2, height - 175, 36, 1, 209,167, 172);
    }

    // welcome
    public void welcome() {
        background (187, 255, 207);
        String s = "Welcome to I am Speed: a Memory Game, " + name + "!\nClick the information logo on the menu page to learn more about how to use this game! Hope you enjoy!";
        textFormat(s, width/2 - 500, height/2 - 300, 1000, 500, 46, 1, 255, 255, 255);
        rectFormat(width/2-60, height - 200, 120, 55, 123,255, 168, true);
        textFormat("next", width/2, height - 175, 36, 1, 123,255, 168);
    }

    // menu
    public void menu() {
        background(166,242,255);
        title("I am Speed: a Memory Game", 80, 211, 255);
        if (packagesNum == 0){
            textFormat("There are no packages yet :(", width/2, height/2, 36, 1, 255,255, 255);
            ellipseFormat("+", 75, height - 75, 80, 211, 255, 0);
        } else {
            if (!edit) {
                rectFormat(60, height - 100, 100, 55, 80, 211, 255, true);
                textFormat("edit", 75, height - 75, 36, 2, 80, 211, 255);
                // buttons
                if (packagesNum > 6) {
                    packagesNum = 6; // TODO: remove excess imports from variables and file (since writing is broken rn do later)
                }
                for (int x = 0; x < packagesNum; x++) {
                    rectFormat(width/2-300, 175 + (75 * x), 600, 50, 80, 211, 255, true);
                    textFormat(packages.get(x)[0], width/2, 200 + (75 * x), 32, 1, 80, 211, 255);
                }
            } else {
                if (packagesNum < 6) {
                    ellipseFormat("+", 75, height - 75, 80, 211, 255, 0);
                }
                if (!checkDelete()) {
                    rectFormat(width / 2 - 70, height - 75, 140, 55, 91, 91, 91, true);
                    textFormat("delete", width / 2, height - 50, 36, 1, 91, 91, 91);
                } else {
                    rectFormat(width / 2 - 70, height - 75, 140, 55, 80, 211, 255, true);
                    textFormat("delete", width / 2, height - 50, 36, 1, 80, 211, 255);
                }
                for (int x = 0; x < packagesNum; x++) {
                    if (!toDelete[x]) {
                        rectFormat(width / 2 - 300, 175 + (75 * x), 600, 50, 91, 91, 91, true);
                        textFormat(packages.get(x)[0], width / 2, 200 + (75 * x), 32, 1, 91, 91, 91);
                    } else {
                        rectFormat(width / 2 - 300, 175 + (75 * x), 600, 50, 80, 211, 255, true);
                        textFormat(packages.get(x)[0], width / 2, 200 + (75 * x), 32, 1, 80, 211, 255);
                    }
                }
                rectFormat(width - 200, height-100, 120, 55, 80, 211, 255, true);
                textFormat("back", width - 185, height-75, 36, 2, 80, 211, 255);
            }
        }
        if (!edit) {
            ellipseFormat("i", width - 75, height - 75, 80, 211, 255, 0);
            ellipseFormat("x", width - 75, 75, 80, 211, 255, -3);
            rectFormat(50, 45, 140, 55, 80, 211, 255, true);
            textFormat("scores", 65, 70, 36, 2, 80, 211, 255);
        }
    }

    // info
    public void info() {
        background(255,214,219);
        title("Information", 215, 158, 156);
        rectFormat(width/2 - 120, height/2 - 50, 240, 100, 215, 158, 156, true);
        textFormat("importing", width/2, height/2, 36, 1, 215, 158, 156);
        rectFormat(width/2 - 480, height/2 - 50, 240, 100, 215, 158, 156, true);
        textFormat("about", width/2 - 360, height/2, 36, 1, 215, 158, 156);
        rectFormat(width/2 + 240, height/2 - 50, 240, 100, 215, 158, 156, true);
        textFormat("gameplay", width/2 + 360, height/2, 36, 1, 215, 158, 156);
        rectFormat(width/2-60, height - 150, 120, 55, 215, 158, 156, true);
        textFormat("back", width/2, height - 125, 36, 1, 215, 158, 156);
    }

    // info display
    public void infoDisplay() { // TODO: write u fool
        background(255,214,219);
        String text;
        if (infoChoice == 1) {
            title ("About",215, 158, 156);
            String temp = "This game is meant for educational purposes, specifically to make learning faster and more efficient.";
            text = "";
        } else if (infoChoice == 2) {
            title("Importing", 215, 158, 156);
            text = "";
        } else {
            title ("Gameplay", 215, 158, 156);
            text = "";
        }
        textFormat(text, width/2 - 250, height/2 - 250, 500,500, 32, 1, 255,255,255);
        rectFormat(width/2-60, height - 150, 120, 55, 215, 158, 156, true);
        textFormat("back", width/2, height - 125, 36, 1, 215, 158, 156);
    }

    // exit
    public void exitGame() {
        background(255,192,164);
        textFormat("Thanks for using this game!!!", width/2 - 250, height/2 - 250, 500,500, 48, 1, 255,255,255);
        rectFormat(width/2-60, height - 200, 120, 55, 255,141,109, true);
        textFormat("close", width/2, height - 175, 36, 1, 255,141,109);
    }

    // import files
    public void importFiles() {
        background(207,255,149);
        rectFormat(width/2 - 200, height/2 - 25, 400, 50, 110,217, 90, false);
        textFormat("Please enter the file name:", width/2, height/2 - 75, 48, 1, 255, 255, 255);
        textFormat(fileName, width / 2 - 175, height / 2, 32, 2, 91, 91, 91);
        textFormat(error, width/2 - 180, height/2 + 50, 18, 2, 234, 7, 0);
        rectFormat(width/2-70, height - 200, 140, 55, 110,217, 90, true);
        textFormat("import", width/2, height - 175, 36, 1, 110,217, 90);
        rectFormat(60, height-100, 120, 55, 110,217, 90, true);
        textFormat("back", 75, height-75, 36, 2, 110,217, 90);
    }

    // scores
    public void scores() {
        background(224,213,255);
        title("Recent Scores", 203, 153, 240);
        rectFormat(width - 200, 45, 120, 55, 203, 153, 240, true);
        textFormat("back", width - 185, 70, 36, 2, 203, 153, 240);
        if (scores.isEmpty()) {
            textFormat("You haven't played anything yet!", width/2, height/2, 36, 1, 255,255, 255);
        } else {
            int s;
            if (scores.size() > 7) {
                s = 7;
            } else {
                s = scores.size();
            }
            for (int x = s - 1; x >= 0; x--) {
                String text = scores.get(x)[0] + ":";
                for (int i = 1; i < scores.get(x).length; i++) {
                    text += " " + scores.get(x)[i];
                }
                rectFormat(width / 2 - 500, 175 + (75 * x), 1000, 50, 203, 153, 240, false);
                textFormat(text, width / 2 - 475, 200 + (75 * x), 32, 2, 203, 153, 240);
            }
        }
    }

    // choose mode
    public void chooseMode() {
        background(150, 255,138);
        textFormat("Please choose a game mode:", width/2, height/2 - 200, 48, 1, 255, 255, 255);
        rectFormat(width/2 - 400, height/2 - 100, 300, 200, 110,217, 90, true);
        textFormat("flashcards", width/2 - 250, height/2, 36, 1, 110,217, 90);
        rectFormat(width/2 + 100, height/2 - 100, 300, 200, 110,217, 90, true);
        textFormat("matching", width/2 + 250, height/2, 36, 1, 110,217, 90);
    }

    // instructions
    public void instructions() {
        background(255,214,219);
        String text, title;
        if (mode == 1) { // TODO: write u fool
            text = "";
            title = "Flashcards: Instructions";
        } else {
            text = "";
            title = "Matching: Instructions";
        }
        title (title, 215, 158, 156);
        rectFormat(width/2-60, height - 150, 120, 55, 215, 158, 156, true);
        textFormat("play", width/2, height - 125, 36, 1, 215, 158, 156);
    }

    // flashcards
    public void flashcards() { // TODO: the game
        background(197,219,255);
    }

    // matching
    public void matching() { // TODO: the game
        background(180, 255, 226);
    }

    // score display
    public void scoreDisplay() {

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
            error = "";
            fileName = "";
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

    // rounded bordered rect
    public void rectFormat(int x, int y, int w, int h, int r, int g, int b, boolean button) {
        if (button) {
            if (mouseX >= x && mouseX <= x+w && mouseY >= y && mouseY <= y+h){
                fill(0, 0, 0, 75);
                strokeWeight(5);
                stroke (0, 0, 0, 75);
                rect(x + 2, y + 2, w, h, 7);
            }
        }
        fill(255);
        strokeWeight(5);
        stroke(r,g,b);
        rect(x, y, w, h, 7);
    }

    // file IO
    public boolean fileIO(String f) {
        String[] newPackage = loadStrings("data/packages/" + f + ".txt");
        if (newPackage == null) {
            return false;
        }
        packagesNum++;
        fileNames.add(f);
        packages.add(newPackage);
        return true;
    }

    // startup import
    public void startupImport(){
        String[] tempFileNames = loadStrings(f); // there's an error message printed here if it fails but like ignore it it's Fine
        if (tempFileNames != null) {
            fileNames.addAll(Arrays.asList(tempFileNames));
            packagesNum = fileNames.size();
            for (int x = 0; x < packagesNum; x++) {
                packages.add(loadStrings("data/packages/" + fileNames.get(x) + ".txt"));
            }
        } else {
            String[] empty = {""};
            saveStrings(dataPath(f), empty); // TODO: this is very broken bc of intellij (i.e. it writes to MemoryGame/data/.. instead of MemoryGame/src/data/..)
        }
    }

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

    // check if anything to delete
    public boolean checkDelete() {
        for (int x = 0; x < toDelete.length; x++) {
            if (toDelete[x]) {
                return true;
            }
        }
        return false;
    }

    // delete imports
    public void runDelete() {
        String[][] tempPackages = new String[packagesNum][];
        String[] tempFileNames = new String[packagesNum];
        for (int x =0; x < packages.size(); x++) {
            tempPackages[x] = packages.get(x);
            tempFileNames[x] = fileNames.get(x);
        }
        for (int x =0; x < toDelete.length; x++) {
            if (toDelete[x]){
                tempPackages[x] = null;
                tempFileNames[x] = null;
            }
        }
        packages.clear();
        fileNames.clear();
        for (int x =0; x < toDelete.length; x++) {
            if (tempPackages[x] != null) {
                packages.add(tempPackages[x]);
                fileNames.add(tempFileNames[x]);
            }
        }
        packagesNum = packages.size();
        String[] write = new String[packagesNum];
        for (int x = 0; x < write.length; x++) {
            write[x] = fileNames.get(x);
        }
        // saveStrings(dataPath(f), write); // TODO: this is very broken bc of intellij (i.e. it writes to MemoryGame/data/.. instead of MemoryGame/src/data/..)
    }

    // title
    public void title(String t, int r, int g, int b) {
        rectFormat(width/2-400, 25, 800, 100, r, g, b, false);
        textFormat(t, width/2, 75, 48, 1, r, g, b);
    }

    // check rect bounds
    public boolean checkRect(int x, int y, int w, int h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    // MAIN

    public static void main (String[] args){
        PApplet.main("Main", args);
    }
}
