/*
  author: Marina Semenova (@marinasemen0va)

  This is the file with all the helper methods.
*/

// VARIABLES

// transition
int transitionTime = 0;
boolean inTransition = false;

// file stuff
ArrayList<String[]> packages = new ArrayList<String[]>();
ArrayList<String> fileNames = new ArrayList<String>(); // TODO: honestly it's a bit redundant having the name in the file if there is the file names (fix later?)
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

// METHODS

// transition
void transition() {
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
void textShadow (String s, float a, float b) {
    fill(0, 0, 0, 75);
    text(s, a + 1, b + 1);
}

// boxed text shadow
void textShadow (String s, float a, float b, float c, float d) {
    fill(0, 0, 0, 75);
    text(s, a + 1, b + 1, c, d);
}

// unboxed text format
void textFormat (String s, float x, float y, int textSize, int textAlign, int r, int g, int b) {
    if (textAlign == 1) {
        textAlign(CENTER, CENTER);
    } else {
        textAlign(LEFT, CENTER);
    }
    textFont(font);
    textSize(textSize);
    textShadow(s, x, y);
    fill(r, g, b);
    text(s, x, y);
}

// boxed text format
void textFormat (String s, float x, float y, float w, float h, int textSize, int textAlign, int r, int g, int b, boolean card) {
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
void rectFormat(float x, float y, float w, float h, int r, int g, int b, boolean button) {
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
boolean fileIO(String f) {
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
void startupImport() {
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
void ellipseFormat(String s, int x, int y, int r, int g, int b, int adj){
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
boolean checkDelete() {
    for (boolean b : toDelete) {
        if (b) {
            return true;
        }
    }
    return false;
}

// delete imports
void runDelete() {
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
void title(String t, int r, int g, int b) {
    rectFormat(width/2-400, 25, 800, 100, r, g, b, false);
    textFormat(t, width/2, 75, 48, 1, r, g, b);
}

// reset variables
void reset() {
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
void startTime() {
    startTime = millis();
    running = true;
}

// stop time
void stopTime() {
    stopTime = millis();
    running = false;
}

// get elapsed
int getElapsedTime() {
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
int s() {
    return (getElapsedTime() / 1000) % 60;
}

// minutes
int m() {
    return (getElapsedTime() / (1000*60)) % 60;
}

// export
void export() {
    String[] temp = new String[fileNames.size()];
    for (int x = 0; x < temp.length; x++) {
        temp[x] = fileNames.get(x);
    }
    saveStrings("data/" + f, temp);
    temp = new String[scores.size()];
    for (int x = 0; x < temp.length; x++) {
        temp[x] = scores.get(x);
    }
    saveStrings("data/" + scoresFile, temp);
}

// make button uniform
void makeButton (String s, int x, int y, int r, int g, int b) {
    textFont(font);
    textSize(36);
    rectFormat(x - (textWidth(s)/2) - (buttonW/2), y - buttonH/2, textWidth(s) + buttonW, buttonH, r, g, b, true);
    textFormat(s, x, y, 36, 1, r, g, b);
}

// make button custom
void makeButton (String s, int x, int y, int r, int g, int b, int w, int h) {
    rectFormat(x - (w/2), y - h/2, w, h, r, g, b, true);
    textFormat(s, x - (w/2) + 25, y - (h/2), w-50, h, 36, 1, r, g, b, false);
}

// check rect uniform
boolean checkRect (String s, int x, int y) {
    textFont(font);
    textSize(36);
    return mouseX >= x - (textWidth(s)/2) - (buttonW/2) && mouseX <= x + (textWidth(s)/2) + (buttonW/2) && mouseY >= y - (buttonH/2) && mouseY <= y + (buttonH/2);
}

// check rect custom
boolean checkRect (int x, int y, int w, int h) {
    return mouseX >= x - (w/2) && mouseX <= x + (w/2) && mouseY >= y - (h/2) && mouseY <= y + (h/2);
}

// draw card
void drawCard(String s, int r, int g, int b) {
    rectFormat(300, 100, width - 600, height - 300, r, g, b, false);
    textFormat(s, 325, 125, width - 600 - 50, height - 300 - 50, 100, 1, r, g, b, true);
}

// loading
void loading(int r, int g,  int b, int l) {
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
void makeBigButtons (String[] s, int r, int g, int b) {
    int[] format = {-1, 1, 0};
    for (int x = 0; x < s.length; x++) {
        makeButton(s[x], width / 2 + (format[x] * ((s.length - 1) * 180)), height / 2, r, g, b, 240, 120);
    }
}

// long buttons
void makeLongButtons(String[] s, int r, int g, int b, int w, int h) {
    for (int x = 0; x < s.length; x++)  {
        makeButton(s[x], width/2, 200 + (75 * x), r, g, b, w, h);
    }
}

void setMatches(int n) {
    for (int x = 0; x < 2; x++) {
        matches[selected.get(x)] = n;
    }
}

// set screen
void setScreen(int n) {
    nextScreen = n;
    inTransition = true;
}

// add score
void addScore() {
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
void setColour(int n){
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
