/*
  author: Marina Semenova (@marinasemen0va)

  This is the file with all the sensory methods.
*/

// key pressed
void keyPressed() {
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
        if (screen == 11 && !showAns) { // TODO: highkey broken
            textFont(font);
            textSize(32);
            if (key != BACKSPACE && width/2 - 575 + textWidth(answer + key) >= width/2 - 575 + 1175) {
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
void mouseClicked() {
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
                if (checkRect(width/2, 200 + (75 * x), width - 900, 60)) {
                    toDelete[x] = !toDelete[x];
                }
            }
        } else { // go to game
            for (int x = 0; x < packagesNum; x++) {
                if (checkRect(width/2, 200 + (75 * x), width - 900, 60)) {
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
                if (!showAns && matches[y*5 + x] != 2 && checkRect(250 + ((250 + ((width-250-(5*250))/4)) * x), 225 + (450 * y), 250, 350)) {
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
