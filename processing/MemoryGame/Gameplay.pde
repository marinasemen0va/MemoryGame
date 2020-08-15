/*
  author: Marina Semenova (@marinasemen0va)

  This is the file with all the gameplay screens.
*/

// VARIABLES

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

// METHODS

// instructions
void instructions() {
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
    textFormat(text, width/2 - ((width - (width/5))/2), height/2 - ((height - (200 - buttonH/2) - (125 + buttonH/2) - 15)/2), width - (width/5),height - (200 - buttonH/2) - (125 + buttonH/2) - 15, 48, 1, 255,255,255, false);
    makeButton("play", width/2, height - 125, r, g, b);
}

// flashcards
void flashcards() {
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
void matching() {
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
                    rectFormat(125 + ((250 + ((width - 250 - (5 * 250)) / 4)) * x), 50 + (450 * y), 250, 350, r, g, b, true);
                }
                if (matches[y*5 + x] == 1) {
                    rectFormat(125 + ((250 + ((width - 250 - (5 * 250)) / 4)) * x), 50 + (450 * y), 250, 350, 255, 72, 80, true);
                }
                if (selected.size() == 1 && matches[y*5 + x] == 1) {
                    matches[y*5 + x] = 0;
                }
                if (selected.size() == 1 && y*5 + x == chosenCard) {
                    rectFormat(125 + ((250 + ((width - 250 - (5 * 250)) / 4)) * x), 50 + (450 * y), 250, 350, grey, grey, grey, true);
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
void typing() {
    setColour(1);
    rectFormat(width/2 - 600, height - 95, 1200, 50, r,g, b, false); // textbox
    drawCard(q.get(index), r, g, b);
    if (!showAns) {
        textFormat(displayAnswer, width / 2 - 57, height - 70, 32, 2, grey, grey, grey);
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
void scoreDisplay() {
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
