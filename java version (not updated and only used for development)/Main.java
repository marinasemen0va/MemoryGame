/*
    author: Marina Semenova (@marinasemen0va)

    This is the main class.
 */

// IMPORTS
import processing.core.PApplet;
import processing.core.PFont;
import java.util.ArrayList;
import java.util.Arrays;

// code
public class Main extends PApplet {
    // VARIABLES

    // main
    PFont font, langFont;
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
        11 - typing
        12 - score display
        */
    int buttonW = 40, buttonH = 60;
    int maxPackages, maxScores;
    int r, g, b, grey = 125;

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
    String f = "project files/packages.txt";
    String scoresFile = "project files/scores.txt";

    // scores
    ArrayList<String> scores = new ArrayList<String>();
    int mode;
        /*
        1 - flashcards
        2 - matching
        3 - typing
         */
    int startIndex;

    // info
    int infoChoice;
        /*
        1 - about
        2 - importing
        3 - gameplay
         */

    // gameplay
    int currentPackageNum;
    ArrayList<String> a = new ArrayList<String>();
    ArrayList<String> q = new ArrayList<String>();
    boolean showAns = false;
    int index;
    String answer = "";
    String displayAnswer = "";
    boolean correct;
    int score = 0;
    int chosenCard = -1;
    int[] deck = new int[10];
    int[] matches = new int[10];
        /*
        0 - n/a
        1 - wrong
        2 - right
         */
    ArrayList<Integer> selected = new ArrayList<Integer>();
    int startTime = 0, stopTime = 0;
    boolean running = false;

    // SETUP

    // settings
    public void settings() {
        fullScreen();
    }

    // setup
    public void setup() {
        frameRate(60);
        font = loadFont("project files/CenturyGothic-Bold-48.vlw");
        langFont = createFont("font/cardFont.ttf", 100);
        textFont(font);
        maxPackages = (height - (200 - buttonH/2) - (50 + buttonH/2)) / 75; // (width - widthOfTitle - widthOfDelete) / (widthOfButton + widthOfGap)
        maxScores = (height - (200 - buttonH/2) - 25) / 75; // (width - widthOfTitle - widthOfBottom) / (widthOfButton + widthOfGap)
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

    // key pressed
    public void keyPressed() {
        if (key != ENTER) {
            if (screen == 0) {
                if (width / 2 - 175 + textWidth(name + key) >= width/2 + 225) {
                    error = "your name cannot be longer!";
                } else {
                    error = "";
                    if (key == BACKSPACE) {
                        if (name.length() <= 1) {
                            name = "";
                        } else {
                            name = name.substring(0, name.length() - 1);
                        }
                    } else {
                        name = name + key;
                    }
                }
            }
            if (screen == 5) {
                if (width / 2 - 175 + textWidth(fileName + key) >= width/2 + 225) {
                    error = "the file name cannot be longer!";
                } else {
                    error = "";
                    if (key == BACKSPACE) {
                        if (fileName.length() <= 1) {
                            fileName = "";
                        } else {
                            fileName = fileName.substring(0, fileName.length() - 1);
                        }
                    } else {
                        fileName = fileName + key;
                    }
                }
            }
            if (screen == 6) {
                if (keyCode == DOWN && maxScores < scores.size() && startIndex - maxScores > 0) {
                    startIndex--;
                }
                if (keyCode == UP && maxScores < scores.size() && startIndex < scores.size()) {
                    startIndex++;
                }
            }
            if (screen == 11 && !showAns) {
                if (key != BACKSPACE && width / 2 - 525 + textWidth(answer + key) >= width/2 - 525 + 900) {
                    answer = answer + key;
                    displayAnswer = answer.substring(answer.length() - displayAnswer.length());
                } else {
                    error = "";
                    if (key == BACKSPACE) {
                        if (displayAnswer.length() <= 1) {
                            displayAnswer = "";
                            answer = "";
                        } else if (width / 2 - 525 + textWidth(answer + key) >= width/2 - 525 + 900) {
                            answer = answer.substring(0, answer.length() - 1);
                            displayAnswer = answer.substring(answer.length() - displayAnswer.length());
                        }
                        else {
                            answer = answer.substring(0, answer.length() - 1);
                            displayAnswer = answer;
                        }
                    } else {
                        answer = answer + key;
                        displayAnswer = answer;
                    }
                }
            }
        }
    }

    // mouse clicked
    public void mouseClicked() {
        if (screen == 0) {
            if (checkRect("next", width/2, height - 175)) { // next
                if (name.equals("")){
                    name = "user";
                }
                setScreen(1);
            }
        }
        if (screen == 1) {
            if (checkRect("next", width/2, height - 175)) { // next
                setScreen(2);
            }
        }
        if (screen == 2) {
            if (!edit && sq(mouseX - (width - 75)) + sq(mouseY - (height - 75)) <= sq(30)) { // info
                setScreen(3);
            }
            if (!edit && sq(mouseX - (width - 75)) + sq(mouseY - 75) <= sq(30)) { // exit
                setScreen(4);
            }
            if (!edit && checkRect("scores", 125, 70)) { // scores
                startIndex = scores.size();
                setScreen(6);
            }
            if (packagesNum == 0) {
                if (sq(mouseX - 75) + sq(mouseY - (height - 75)) <= sq(30)) { // +
                    setScreen(5);
                }
            } else {
                if (edit && checkRect("back", width - 185, height-75)) { // back from edit
                    edit = false;
                    toDelete = null;
                } else if (edit && sq(mouseX - 75) + sq(mouseY - (height - 75)) <= sq(30)) { // +
                    setScreen(5);
                } else if (!edit && checkRect("edit", 100, height - 75)) { // edit
                    edit = true;
                    toDelete = new boolean[packagesNum];
                    for (int x =0; x < packagesNum; x++){
                        toDelete[x] = false;
                    }
                }
            }
            if (edit) {
                for (int x = 0; x < packagesNum; x++) {
                    if (checkRect(width/2, 200 + (75 * x), 600, 60)) {
                        toDelete[x] = !toDelete[x];
                    }
                }
            } else { // go to game
                for (int x = 0; x < packagesNum; x++) {
                    if (checkRect(width/2, 200 + (75 * x), 600, 60)) {
                        currentPackageNum = x;
                        setScreen(7);
                    }
                }
            }
            if (edit && checkDelete() && checkRect("delete", width / 2, height - 50)) { // delete
                runDelete();
                edit = false;
                toDelete = null;
            }
        }
        if (screen == 3) {
            if (checkRect("back", width/2, height - 125)) { // back
                setScreen(2);
            }
            if (checkRect(width/2 - 360, height/2, 240, 120)) { // about
                infoChoice = 1;
                setScreen(35);
            }
            if (checkRect(width/2, height/2, 240, 120)) { // importing
                infoChoice = 2;
                setScreen(35);
            }
            if (checkRect(width/2 + 360, height/2, 240, 120)) { // gameplay
                infoChoice = 3;
                setScreen(35);
            }
        }
        if (screen == 35) {
            if (checkRect("back", width/2, height - 125)) { // back
                setScreen(3);
            }
        }
        if (screen == 4){
            if (checkRect("close", width/2, height - 175)) { // close
                export();
                exit();
            }
        }
        if (screen == 5) {
            if (checkRect("import", width/2, height - 175)) { // import
                if (fileName.equals("")){
                    error = "enter a file name!";
                } else {
                    boolean status = fileIO(fileName);
                    if (status) {
                        edit = false;
                        toDelete = null;
                        setScreen(2);
                    } else {
                        error = "oops! something went wrong!";
                    }
                }
            }
            if (checkRect("back", 100, height-75)) { // back
                edit = false;
                toDelete = null;
                setScreen(2);
            }
        }
        if (screen == 6) {
            if (checkRect("back", width - 125, 70)) { // back
                setScreen(2);
            }
        }
        if (screen == 7) {
            if (packages.get(currentPackageNum).length-1 >= 10) {
                if (checkRect(width/2 - 360, height/2, 240, 120)) { // flashcards
                    mode = 1;
                    setScreen(8);
                    reset();
                }
                if (checkRect(width/2, height/2, 240, 120)) { // matching
                    mode = 2;
                    setScreen(8);
                    reset();
                }
                if (checkRect(width/2 + 360, height/2, 240, 120)) { // typing
                    mode = 3;
                    setScreen(8);
                    reset();
                }
            } else {
                if (checkRect(width/2 - 180, height/2, 240, 120)) { // flashcards
                    mode = 1;
                    setScreen(8);
                    reset();
                }
                if (checkRect(width/2 + 180, height/2, 240, 120)) { // typing
                    mode = 3;
                    setScreen(8);
                    reset();
                }
            }
        }
        if (screen == 8) {
            if (checkRect("play", width/2, height - 125)) { // play
                if (mode == 1) {
                    setScreen(9);
                }
                if (mode == 2) {
                    setScreen(10);
                }
                if (mode == 3) {
                    setScreen(11);
                }
            }
        }
        if (screen == 9) {
            if (showAns && checkRect("next", width - 125, height - 75)) { // next
                if (!(q.size() == 1)) {
                    setScreen(9);
                } else {
                    setScreen(12);
                }
            }
            if (checkRect("flip", 100, height - 75)) { // flip
                showAns = !showAns;
            }
            if (sq(mouseX - (width - 75)) + sq(mouseY - 75) <= sq(30)) { // exit
                setScreen(12);
            }
        }
        if (screen == 10) {
            for (int y = 0; y < 2; y++) {
                for (int x = 0; x < 5; x++) {
                    if (!showAns && matches[y*5 + x] != 2 && checkRect(200 + ((150 + ((width-250-(5*150))/4)) * x), 175 + (300 * y), 150, 250)) {
                        if (!(selected.size() == 1 && selected.get(0) == y*5 + x)) {
                            showAns = true;
                            chosenCard = y * 5 + x;
                            selected.add(chosenCard);
                        }
                    }
                }
            }
            if (checkRect("close", width - 125, height - 75)) { // close
                showAns = false;
                if (selected.size() == 2) {
                    if (abs(deck[selected.get(0)]-deck[selected.get(1)]) == 5) {
                        setMatches(2);
                        score++;
                    } else {
                        setMatches(1);
                    }
                    selected.clear();
                }
                if (score == 5) {
                    showAns = true;
                    setScreen(12);
                }
            }
        }
        if (screen == 11) {
            if (showAns && checkRect("next", width - 125, height - 75)) { // next
                if (!(q.size() == 1)) {
                    setScreen(11);
                } else {
                    setScreen(12);
                }
            }
            if (!showAns && checkRect("show", width - 125, height - 75)) { // show
                showAns = !showAns;
                correct = answer.trim().equals(a.get(index).trim());
                if (correct) {
                    score++;
                }
            }
            if (sq(mouseX - (width - 75)) + sq(mouseY - 75) <= sq(30)) { // exit
                setScreen(12);
            }
        }
        if (screen == 12) {
            if (checkRect("next", width/2, height - 125)) { // next
                setScreen(2);
                addScore();
            }
        }
    }

    // CUSTOM METHODS

    // preloader
    public void preloader() {
        setColour(5);
        String[] loading = {"Loading","Loading.","Loading..", "Loading..."};
        textFormat(loading[load], width/2 - 115, height/2 - 50, 48, 2, 255, 255, 255);
        loading(r, g, b, loading.length);
    }

    // name
    public void name() {
        setColour(3);
        rectFormat(width/2 - 200, height/2 - 25, 400, 50, r,g, b, false); // textbox
        textFormat("Please enter your name:", width/2, height/2 - 75, 48, 1, 255, 255, 255);
        if (!name.equals("user")) { // restrict
            textFormat(name, width / 2 - 175, height / 2, 32, 2, grey, grey, grey); // name
        }
        textFormat(error, width/2 - 180, height/2 + 50, 18, 2, 234, 7, 0);
        makeButton("next", width/2, height - 175, r, g, b);
    }

    // welcome
    public void welcome() {
        setColour(4);
        String s = "Welcome to I am Speed: a Memory Game, " + name + "!\nClick the information logo on the menu page to learn more about how to use this game! Hope you enjoy!";
        textFormat(s, width/2 - 500, height/2 - 300, 1000, 500, 46, 1, 255, 255, 255, false); // message
        makeButton("next", width/2, height - 175, r, g , b);
    }

    // menu
    public void menu() {
        setColour(1);
        title("I am Speed: a Memory Game", r, g, b);
        if (packagesNum == 0){
            textFormat("There are no packages yet :(", width/2, height/2, 36, 1, 255,255, 255);
            ellipseFormat("+", 75, height - 75, r, g, b, 0);
        } else {
            if (!edit) {
                makeButton("edit", 100, height - 75, r, g, b);
                String[] temp = new String[packagesNum];
                for (int x = 0; x < temp.length; x++)  {
                    temp[x] = packages.get(x)[0];
                }
                makeLongButtons(temp, r, g, b, 600, 60);
            } else {
                if (packagesNum < maxPackages) {
                    ellipseFormat("+", 75, height - 75, r, g, b, 0);
                }
                if (!checkDelete()) {
                    makeButton("delete", width / 2, height - 50, grey, grey, grey);
                } else {
                    makeButton("delete", width / 2, height - 50, r, g, b);
                }
                for (int x = 0; x < packagesNum; x++) {
                    if (!toDelete[x]) {
                        makeButton(packages.get(x)[0], width/2, 200 + (75 * x), grey, grey, grey, 600, 60);
                    } else {
                        makeButton(packages.get(x)[0], width/2, 200 + (75 * x), r, g, b, 600, 60);
                    }
                }
                makeButton("back", width - 185, height-75, r, g, b);
            }
        }
        if (!edit) {
            ellipseFormat("i", width - 75, height - 75, r, g, b, 0);
            ellipseFormat("x", width - 75, 75, r, g, b, -3);
            makeButton("scores", 125, 70, r, g, b);
        }
    }

    // info
    public void info() {
        setColour(3);
        title("Information", r, g, b);
        String[] labels = {"About", "Gameplay", "Importing"};
        makeBigButtons(labels, r, g, b);
        makeButton("back", width/2, height - 125, r, g, b);
    }

    // info display
    public void infoDisplay() {
        String text;
        if (infoChoice == 1) {
            setColour(2);
            title ("About", r, g, b);
            text = "This is a game developed for the purpose of learning things faster, hence its name. It is intended to help improve memorization. This educational tool was developed for personal use with the goal of learning new languages but is available publicly on @marinasemen0va’s GitHub page for anyone to try. This game revolves around using \"packages\" containing pairs of information. See the Importing page for more details.";
        } else if (infoChoice == 2) {
            setColour(4);
            title("Importing", r, g, b);
            text = "For a package to be valid, it must be a .txt file in data/packages, it must contain its name in its first line, and the remaining lines must be pairs of information. Additionally, one can import a new font in data/font as cardFont.ttf. Please do not modify anything in data/project files or the game may not work properly. Remember that if the content of a line is too long it may not be shown entirely, and the same goes for the package name. ";
        } else {
            setColour(5);
            title ("Gameplay", r, g, b);
            text = "This game has three modes: flashcards, matching, and typing. Flashcards are meant to help study the content, matching is meant to help remember said content, and typing allows one to apply their knowledge. Each game mode is explained further when selected. The matching mode will only be available is there are 5 or more pairs in a package.";
        }
        textFormat(text, width/2 - ((width - (width/5))/2), height/2 - ((height - (200 - buttonH/2) - (125 + buttonH/2) - 15)/2), width - (width/5),height - (200 - buttonH/2) - (125 + buttonH/2) - 15, 36, 1, 255,255,255, false);
        makeButton("back", width/2, height - 125, r, g, b);
    }

    // exit
    public void exitGame() {
        setColour(3);
        textFormat("Thanks for using this game!!!", width/2 - 250, height/2 - 250, 500,500, 48, 1, 255,255,255, false);
        makeButton("close", width/2, height - 175, r, g, b);
    }

    // import files
    public void importFiles() {
        setColour(4);
        rectFormat(width/2 - 200, height/2 - 25, 400, 50, r,g, b, false);
        textFormat("Please enter the file name:", width/2, height/2 - 75, 48, 1, 255, 255, 255);
        textFormat(fileName, width / 2 - 175, height / 2, 32, 2, grey, grey, grey);
        textFormat(error, width/2 - 180, height/2 + 50, 18, 2, 234, 7, 0);
        makeButton("import", width/2, height - 175, r, g, b);
        makeButton("back", 100, height-75, r, g, b);
    }

    // scores
    public void scores() {
        setColour(2);
        title("Scores", r, g, b);
        makeButton("back", width - 125, 70, r, g, b);
        if (scores.isEmpty()) {
            textFormat("You haven't played anything yet!", width/2, height/2, 36, 1, 255,255, 255);
        } else {
            int s = Math.min(scores.size(), maxScores);
            String[] temp = new String[s]; int i = 0;
            for (int x = startIndex - 1; x >= startIndex - s; x--) {
                temp[i] = scores.get(x); i++;
            }
            makeLongButtons(temp, r, g, b, 1000, 60);
        }
    }

    // choose mode
    public void chooseMode() {
        setColour(3);
        textFormat("Please choose a game mode:", width/2, height/2 - 175, 48, 1, 255, 255, 255);
        if (packages.get(currentPackageNum).length-1 >= 10) {
            String[] labels = {"Flashcards", "Typing", "Matching"};
            makeBigButtons(labels, r, g, b);
        } else {
            String[] labels = {"Flashcards", "Typing"};
            makeBigButtons(labels, r, g, b);
        }
    }

    // instructions
    public void instructions() {
        setColour(4);
        String text, title;
        if (mode == 1) {
            text = "This game mode is for learning the material. It is simply going through a deck of cards with the pairs on both sides of each card. This game mode is scored by the number of cards studied. One can exit the game at any time and the number of cards with still be recorded. The flip button on the game screen allows the user to flip between the two sides of the card, and the next button will take the user to the following card.";
            title = "Flashcards: Instructions";
        } else if (mode == 2){
            text = "This game mode allows one to practice their knowledge of the material. There will be 10 cards on the screen, and one will be scored on the time taken to match all the pairs. Make a pair by selecting two consecutive cards. If the cards are gone after the two cards have been selected it is a match, while if they have a red border they are not a match. The card with the grey border will indicate the first card of the two chosen.";
            title = "Matching: Instructions";
        } else {
            text = "This game mode is intended for the application of learned material. One will enter their answer in the text box and press the show button. If the border is green the answer was correct, while if it is red the answer was off and the correct one will be displayed. This game is scored based on have many cards were done correctly. The entry works only with the English alphabet, and it works best with short answers, as the match is determined by checking the contents exactly to the letter. ";
            title = "Typing: Instructions";
        }
        title (title, r, g, b);
        textFormat(text, width/2 - ((width - (width/5))/2), height/2 - ((height - (200 - buttonH/2) - (125 + buttonH/2) - 15)/2), width - (width/5),height - (200 - buttonH/2) - (125 + buttonH/2) - 15, 36, 1, 255,255,255, false);
        makeButton("play", width/2, height - 125, r, g, b);
    }

    // flashcards
    public void flashcards() {
        setColour(3);
        if (!showAns) {
            drawCard(q.get(index), r, g, b);
        } else {
            drawCard(a.get(index), r, g, b);
            makeButton("next", width - 125, height - 75, r, g, b);
        }
        makeButton("flip", 100, height - 75, r, g, b);
        ellipseFormat("x", width - 75, 75, r, g, b, -3);
    }

    // matching
    public void matching() {
        setColour(5);
        if (showAns) {
            if (deck[chosenCard] < 5) {
                drawCard(q.get(deck[chosenCard]), r, g, b);
            } else {
                drawCard(a.get(deck[chosenCard] - 5), r, g, b);
            }
            makeButton("close", width - 125, height - 75, r, g, b);
        } else {
            for (int y = 0; y < 2; y++) {
                for (int x = 0; x < 5; x++) {
                    if (matches[y*5 + x] == 0){
                        rectFormat(125 + ((150 + ((width - 250 - (5 * 150)) / 4)) * x), 50 + (300 * y), 150, 250, r, g, b, true);
                    }
                    if (matches[y*5 + x] == 1) {
                        rectFormat(125 + ((150 + ((width - 250 - (5 * 150)) / 4)) * x), 50 + (300 * y), 150, 250, 255,72, 80, true);
                    }
                    if (selected.size() == 1 && matches[y*5 + x] == 1) {
                        matches[y*5 + x] = 0;
                    }
                    if (selected.size() == 1 && y*5 + x == chosenCard) {
                        rectFormat(125 + ((150 + ((width - 250 - (5 * 150)) / 4)) * x), 50 + (300 * y), 150, 250, grey,grey, grey, true);
                    }
                }
            }
            if (s() >= 10) {
                textFormat(m() + ":" + s(), width / 2, height - 60, 60, 1, 255, 255, 255);
            } else {
                textFormat(m() + ":0" + s(), width / 2, height - 60, 60, 1, 255, 255, 255);
            }
        }
    }

    // typing
    public void typing() {
        setColour(1);
        rectFormat(width/2 - 550, height - 95, 950, 50, r,g, b, false); // textbox
        drawCard(q.get(index), r, g, b);
        if (!showAns) {
            textFormat(displayAnswer, width / 2 - 525, height - 70, 32, 2, grey, grey, grey);
            makeButton("show", width - 125, height - 75, r, g, b);
        } else {
            if (correct) {
                rectFormat(width/2 - 550, height - 95, 950, 50, 146,255, 127, false);
            } else {
                rectFormat(width/2 - 550, height - 95, 950, 50, 255,72, 80, false);
            }
            textFormat(a.get(index), width / 2 - 525, height - 94, 900, 50, 32, 2, grey, grey, grey, false);
            makeButton("next", width - 125, height - 75, r, g, b);
        }
        ellipseFormat("x", width - 75, 75, r, g, b, -3);
    }

    // score display
    public void scoreDisplay() {
        setColour(4);
        title ("Score", r, g, b);
        String text;
        if (mode == 1) {
            text = "Cards studied: " + ((packages.get(currentPackageNum).length-1)/2 - q.size());
        } else if (mode == 2) {
            text = "Time taken: " + m() + ":" + s();
        } else {
            int a = round((((float) score)/((packages.get(currentPackageNum).length-1)/2 - q.size()))*100);
            text = "Cards studied: " + ((packages.get(currentPackageNum).length-1)/2 - q.size()) + "\nCorrect: " + score + "\nAccuracy: " + a + "%";
        }
        textFormat(text, width/2 - 250, height/2 - 250, 500,500, 60, 1, 255,255,255, false);
        makeButton("next", width/2, height - 125, r, g, b);
    }

    // HELPER METHODS

    // transition
    public void transition() {
        transitionTime++;
        float transparency = (-1 * abs(transitionTime - 25) + 25) * 10;
        if (transitionTime == 25) {
            if (screen == 9 || screen == 11) {
                q.remove(index);
                a.remove(index);
                index = (int) random(0, q.size());
                showAns = false;
                answer = "";
                displayAnswer = "";
            }
            if (screen == 8) {
                startTime();
            }
            if (nextScreen == 12) {
                stopTime();
            }
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
    public void textShadow (String s, float a, float b) {
        fill(0, 0, 0, 75);
        text(s, a + 1, b + 1);
    }

    // boxed text shadow
    public void textShadow (String s, float a, float b, float c, float d) {
        fill(0, 0, 0, 75);
        text(s, a + 1, b + 1, c, d);
    }

    // unboxed text format
    public void textFormat (String s, float x, float y, int textSize, int textAlign, int r, int g, int b) {
        if (textAlign == 1) {
            textAlign(CENTER, CENTER);
        } else {
            textAlign(LEFT, CENTER);
        }
        textSize(textSize);
        textShadow(s, x, y);
        fill(r, g, b);
        text(s, x, y);
    }

    // boxed text format
    public void textFormat (String s, float x, float y, float w, float h, int textSize, int textAlign, int r, int g, int b, boolean card) {
        if (card) {
            textFont(langFont);
        } else {
            textFont (font);
        }
        if (textAlign == 1) {
            textAlign(CENTER, CENTER);
        } else {
            textAlign(LEFT, CENTER);
        }
        textSize(textSize);
        textShadow(s, x, y, w, h);
        fill(r, g, b);
        text(s, x, y, w, h);
    }

    // rounded bordered rect
    public void rectFormat(float x, float y, float w, float h, int r, int g, int b, boolean button) {
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
    public void startupImport() {
        String[] tempFileNames = loadStrings(f); // there's an error message printed here if it fails but like ignore it it's Fine
        String[] tempScores = loadStrings(scoresFile); // there's an error message printed here if it fails but like ignore it it's Fine
        fileNames.addAll(Arrays.asList(tempFileNames));
        packagesNum = min(maxPackages, fileNames.size());
        for (int x = 0; x < packagesNum; x++) {
            packages.add(loadStrings("data/packages/" + fileNames.get(x) + ".txt"));
        }
        scores.addAll(Arrays.asList(tempScores));
        startIndex = scores.size();
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
        for (boolean b : toDelete) {
            if (b) {
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
    }

    // title
    public void title(String t, int r, int g, int b) {
        rectFormat(width/2-400, 25, 800, 100, r, g, b, false);
        textFormat(t, width/2, 75, 48, 1, r, g, b);
    }

    // reset variables
    public void reset() {
        if (mode == 1 || mode == 3) {
            q.clear();
            a.clear();
            score = 0;
            for (int i = 1; i < packages.get(currentPackageNum).length - 1; i += 2) {
                q.add(packages.get(currentPackageNum)[i]);
                a.add(packages.get(currentPackageNum)[i + 1]);
            }
            index = (int) random(0, q.size());
        } else {
            matches = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            chosenCard = -1;
            q.clear();
            a.clear();
            ArrayList<String> tempQ = new ArrayList<String>();
            ArrayList<String> tempA = new ArrayList<String>();
            for (int x = 1; x < packages.get(currentPackageNum).length - 1; x += 2) {
                tempQ.add(packages.get(currentPackageNum)[x]);
                tempA.add(packages.get(currentPackageNum)[x + 1]);
            }
            score = 0;
            for (int x = 0; x < 5; x++) {
                int r = (int) random(tempQ.size());
                q.add(tempQ.get(r));
                a.add(tempA.get(r));
                tempQ.remove(r);
                tempA.remove(r);
            }
            ArrayList<Integer> temp = new ArrayList<Integer>();
            for (int x = 0; x < 10; x++){
                temp.add(x);
            }
            for (int x = 0; x < 10; x++) {
                int r = (int) random(temp.size());
                deck[x] = temp.get(r);
                temp.remove(r);
            }
        }
    }

    // start time
    public void startTime() {
        startTime = millis();
        running = true;
    }

    // stop time
    public void stopTime() {
        stopTime = millis();
        running = false;
    }

    // get elapsed
    public int getElapsedTime() {
        int elapsed;
        if (running) {
            elapsed = (millis() - startTime);
        }
        else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }

    // seconds
    public int s() {
        return (getElapsedTime() / 1000) % 60;
    }

    // minutes
    public int m() {
        return (getElapsedTime() / (1000*60)) % 60;
    }

    // export
    public void export() { // TODO
        // TODO: write updated scores
        // TODO: write updated packages list
    }

    // make button uniform
    public void makeButton (String s, int x, int y, int r, int g, int b) {
        textFont(font);
        textSize(36);
        rectFormat(x - (textWidth(s)/2) - (buttonW/2), y - buttonH/2, textWidth(s) + buttonW, buttonH, r, g, b, true);
        textFormat(s, x, y, 36, 1, r, g, b);
    }

    // make button custom
    public void makeButton (String s, int x, int y, int r, int g, int b, int w, int h) {
        rectFormat(x - (w/2), y - h/2, w, h, r, g, b, true);
        textFormat(s, x - (w/2) + 25, y - (h/2), w-50, h, 36, 1, r, g, b, false);
    }

    // check rect uniform
    public boolean checkRect (String s, int x, int y) {
        textFont(font);
        textSize(36);
        return mouseX >= x - (textWidth(s)/2) - (buttonW/2) && mouseX <= x + (textWidth(s)/2) + (buttonW/2) && mouseY >= y - (buttonH/2) && mouseY <= y + (buttonH/2);
    }

    // check rect custom
    public boolean checkRect (int x, int y, int w, int h) {
        return mouseX >= x - (w/2) && mouseX <= x + (w/2) && mouseY >= y - (h/2) && mouseY <= y + (h/2);
    }

    // draw card
    public void drawCard(String s, int r, int g, int b) {
        rectFormat(150, 50, width - 300, height - 200, r, g, b, false);
        textFormat(s, 175, 75, width - 300 - 50, height - 200 - 50, 100, 1, r, g, b, true);
    }

    // loading
    public void loading(int r, int g,  int b, int l) {
        noStroke();
        fill(255);
        rect(width/2 - 200, height/2, barV, 20); // bar
        noFill();
        strokeWeight(5);
        stroke(r,g,b);
        rect(width/2 - 200, height/2, 400, 20); // border
        if (barV >= 400) {
            nextScreen = 0;
            inTransition = true;
        } else {
            barV += random(0,30);
            if (barV > 400) {
                barV = 400;
            }
            delay(200);
            load++;
            if (load == l) {
                load = 0;
            }
        }
    }

    // big buttons
    public void makeBigButtons (String[] s, int r, int g, int b) {
        int[] format = {-1, 1, 0};
        for (int x = 0; x < s.length; x++) {
            makeButton(s[x], width / 2 + (format[x] * ((s.length - 1) * 180)), height / 2, r, g, b, 240, 120);
        }
    }

    // long buttons
    public void makeLongButtons(String[] s, int r, int g, int b, int w, int h) {
        for (int x = 0; x < s.length; x++)  {
            makeButton(s[x], width/2, 200 + (75 * x), r, g, b, w, h);
        }
    }

    public void setMatches(int n) {
        for (int x = 0; x < 2; x++) {
            matches[selected.get(x)] = n;
        }
    }

    // set screen
    public void setScreen(int n) {
        nextScreen = n;
        inTransition = true;
    }

    // add score
    public void addScore() {
        if (mode == 1) {
            scores.add("Flashcards - package name: " + packages.get(currentPackageNum)[0] + ", cards studied: " + ((packages.get(currentPackageNum).length-1)/2 - q.size()));
        } else if (mode == 2) {
            scores.add("Matching - package name: " + packages.get(currentPackageNum)[0] + ", time taken: " + m() + ":" + s());
        } else {
            int a = round((((float) score)/((packages.get(currentPackageNum).length-1)/2 - q.size()))*100);
            scores.add("Typing - package name: " + packages.get(currentPackageNum)[0] + ", cards studied: " + ((packages.get(currentPackageNum).length-1)/2 - q.size()) + ", accuracy: " + a + "%");
        }
    }

    // set colour
    public void setColour(int n){
        if (n == 1) {
            background(184,237,243); r = 130; g = 195; b = 209;
        } else if (n == 2) {
            background(214,202,224); r = 186; g = 161; b = 209;
        } else if (n == 3) {
            background (255, 207, 204); r = 209; g = 167; b = 172;
        } else if (n == 4) {
            background (187, 255, 207); r = 123; g = 255; b = 168;
        } else if (n == 5) {
            background(184, 206, 245); r = 151; g = 180; b = 238;
        }
    }

    // MAIN

    public static void main (String[] args){
        PApplet.main("Main", args);
    }
}