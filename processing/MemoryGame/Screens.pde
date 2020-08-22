/*
  author: Marina Semenova (@marinasemen0va)

  This is the file with all the screens.
*/

// VARIABLES

// preloader
int barV = 0;
int load = 0;

// name
String name = "";
String error = "";

// info
int infoChoice;
    /*
    1 - about
    2 - importing
    3 - gameplay
     */
     
// METHODS

// preloader
void preloader() {
    setColour(5);
    String[] loading = {"Loading","Loading.","Loading..", "Loading..."};
    textFormat(loading[load], width/2 - 115, height/2 - 50, 48, 2, 255, 255, 255);
    loading(r, g, b, loading.length);
}

// name
void name() {
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
void welcome() {
    setColour(4);
    String s = "Welcome to I am Speed: a Memory Game, " + name + "!\nClick the information logo on the menu page to learn more about how to use this game! Hope you enjoy!";
    textFormat(s, width/2 - 500, height/2 - 300, 1000, 500, 46, 1, 255, 255, 255, false); // message
    makeButton("next", width/2, height - 175, r, g , b);
}

// menu
void menu() {
    setColour(1);
    title("I am Speed: a Memory Game", r, g, b);
    if (packagesNum == 0){
        textFormat("There are no packages yet :(", width/2, height/2, 48, 1, 255,255, 255);
        ellipseFormat("+", 75, height - 75, r, g, b, 0);
    } else {
        if (!edit) {
            makeButton("edit", 100, height - 75, r, g, b);
            String[] temp = new String[packagesNum];
            for (int x = 0; x < temp.length; x++)  {
                temp[x] = packages.get(x)[0];
            }
            makeLongButtons(temp, r, g, b, width - 900, 60);
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
                    makeButton(packages.get(x)[0], width/2, 200 + (75 * x), grey, grey, grey, width - 900, 60);
                } else {
                    makeButton(packages.get(x)[0], width/2, 200 + (75 * x), r, g, b, width - 900, 60);
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
void info() {
    setColour(3);
    title("Information", r, g, b);
    String[] labels = {"About", "Gameplay", "Importing"};
    makeBigButtons(labels, r, g, b);
    makeButton("back", width/2, height - 125, r, g, b);
}

// info display
void infoDisplay() {
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
    textFormat(text, width/2 - ((width - (width/5))/2), height/2 - ((height - (200 - buttonH/2) - (125 + buttonH/2) - 15)/2), width - (width/5),height - (200 - buttonH/2) - (125 + buttonH/2) - 15, 48, 1, 255,255,255, false);
    makeButton("back", width/2, height - 125, r, g, b);
}

// exit
void exitGame() {
    setColour(3);
    textFormat("Thanks for using this game!!!", width/2 - 250, height/2 - 250, 500,500, 48, 1, 255,255,255, false);
    makeButton("close", width/2, height - 175, r, g, b);
}

// import files
void importFiles() {
    setColour(4);
    rectFormat(width/2 - 450, height/2 - 25, 900, 50, r,g, b, false);
    textFormat("Please enter the file name:", width/2, height/2 - 75, 48, 1, 255, 255, 255);
    textFormat(fileName, width / 2 - 425, height / 2, 32, 2, grey, grey, grey);
    textFormat(error, width/2 - 430, height/2 + 50, 18, 2, 234, 7, 0);
    makeButton("import", width/2, height - 175, r, g, b);
    makeButton("back", 100, height-75, r, g, b);
}

// scores
void scores() {
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
        makeLongButtons(temp, r, g, b, width - 300, 60);
    }
}

// choose mode
void chooseMode() {
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
