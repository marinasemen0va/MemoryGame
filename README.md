# I am Speed: a Memory Game 
[ *last updated 14/08/20* ] 
 
    > time to actual code something huh 
    
    me, right before spending a whole lot of time making this game

## Setup
- have [Processing](https://processing.org/download/ "download Processing") installed 
- download [`MemoryGame-master.zip`](https://github.com/marinasemen0va/MemoryGame/archive/master.zip "download MemoryGame-master.zip") and unzip it
- put any packages in `processing/MemoryGame/data/packages`
- if a new font is needed replace `cardFont.ttf` in `data/font` with a file of the same name and extension
- open `processing/MemoryGame/MemoryGame.pde` in Processing and run it
- on the menu screen, click the `+` button to import the packages from `processing/MemoryGame/data/packages`
- select a package to use and click on the button that displays its name
- enjoy the game! (•‿•)

## About
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This is a game developed for the 
purpose of learning things faster, hence its name. It is intended to help 
improve memorization. This educational tool was developed for personal use 
with the goal of learning new languages but is available publicly on 
@marinasemen0va’s GitHub page for anyone to try. This game revolves 
around using “packages” containing pairs of information. See the Importing 
page for more details. 

## Importing
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;For a package to be valid, it must be 
a .txt file in data/packages, it must contain its name in its first line, 
and the remaining lines must be pairs of information. Additionally, 
one can import a new font in data/font as cardFont.ttf. Please do not modify 
anything in data/project files or the game may not work properly. 
Remember that if the content of a line is too long it may not be 
shown entirely, and the same goes for the package name.

## Gameplay
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This game has three modes: 
flashcards, matching, and typing. Flashcards are meant to help 
study the content, matching is meant to help remember said content, 
and typing allows one to apply their knowledge. Each game mode is 
explained further when selected. The matching mode will only be 
available is there are 5 or more pairs in a package.

#### Flashcards
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This game mode is for 
learning the material. It is simply going through a deck of 
cards with the pairs on both sides of each card. This game 
mode is scored by the number of cards studied. One can exit 
the game at any time and the number of cards with still be 
recorded. The flip button on the game screen allows the 
user to flip between the two sides of the card, and the 
next button will take the user to the following card. 

#### Matching
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This game mode allows 
one to practice their knowledge of the material. There 
will be 10 cards on the screen, and one will be scored 
on the time taken to match all the pairs. Make a pair by 
selecting two consecutive cards. If the cards are gone 
after the two cards have been selected it is a match, 
while if they have a red border they are not a match. 
The card with the grey border will indicate the first 
card of the two chosen.

#### Typing
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This game mode 
is intended for the application of learned material. 
One will enter their answer in the text box and press 
the show button. If the border is green the answer was 
correct, while if it is red the answer was off and the 
correct one will be displayed. This game is scored based 
on have many cards were done correctly. The entry works 
only with the English alphabet, and it works best with 
short answers, as the match is determined by checking the 
contents exactly to the letter. 
