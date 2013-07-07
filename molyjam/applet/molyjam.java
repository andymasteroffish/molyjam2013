import processing.core.*; 
import processing.xml.*; 

import ddf.minim.*; 
import ddf.minim.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class molyjam extends PApplet {

/********************************************************
 * QWOPassage: The Journey of Peter Molyneux            *
 *                                                      *
 * This is an important game and Im glad you found it   *
 *                                                      *
 * This game should only be played by                   *
 * those seeking a true emotional experience            *
 *                                                      *
 * Game By Ezra Schrage, Jane Friedhoff,                 *
 * Ben Johnson & Andy Wallace for Molyjam 2013          *
 ********************************************************/
 


//timing
float prevFrameTime;

//player
Person guy = new Person();

//emotions
ArrayList<Emotion> emotions = new ArrayList<Emotion>();
float emotionSpawnTimer;
float emotionMaxNextSpawnTime = 10;
float emotionMinNextSpawnTime = 3;

// background
Background bg = new Background();
float playerTargetX;

// text displayer
TextDisplayer textDisplayer = new TextDisplayer();

//sound manager
SoundManager SM = new SoundManager();
Minim minim;

//fade in/out timer for endgame + bonus screen
float startMessageTimer;
float intervalTimer;
float fadeAlpha;


int groundY;

boolean showHidden;

//title stuff
String gameState;
PImage titlePic;
PImage endingHighEmotions;
PImage endingMedEmotions;
PImage endingLowEmotions;
PImage bonus;


public void setup() {

  size(900, 500);

  frameRate(60);
  smooth();

  groundY = height - 50;

  guy.setup(groundY);
  playerTargetX = width/2;
  

  showHidden = false;

  prevFrameTime = millis();

  bg.setup();
  textDisplayer.setup();

  minim = new Minim(this);
  SM.setup(minim);
  
  //setup the title
  gameState = "title";
  titlePic = loadImage("titleScreen.png");
  
  intervalTimer = 10000; // for reading end screen
}

public void startGame(){
    //start with some time before the first emotion spawn
  emotionSpawnTimer = emotionMaxNextSpawnTime;
  
  bg.reset();
  guy.resetPlayer();
  
  gameState = "game";
}

public void update() {
  float deltaTime = ((float)millis()-prevFrameTime)/1000.0f;
  prevFrameTime = millis();

  if (gameState.equals("title")){
    //fucking nothing right now goddamn
  }else if (gameState.equals("game")){
    guy.resetForces();
  
    guy.update(deltaTime);
  
    //check what kind of text we should be showing
    boolean showEmotionalText = guy.emotionalLevel > 50;
    textDisplayer.updateShowEmotionalText( showEmotionalText, guy.emotionalLevel, guy.emotionalLevelCutOff );
  
    //update emotion pick ups
    for (int i=emotions.size()-1; i>=0; i--) {
      Emotion thisEmotion = emotions.get(i);
      thisEmotion.update(deltaTime, guy);
  
      if (thisEmotion.killMe) {
        emotions.remove(i);
        SM.playemotionGet();
      }
    }
  
    //is it time for a new emotion?
    emotionSpawnTimer -= deltaTime;
    if (emotionSpawnTimer <= 0) {
      spawnEmotion();
      emotionSpawnTimer = random(emotionMinNextSpawnTime, emotionMaxNextSpawnTime);
    }
  
    //bg.updateBackground();
  
    //check for scrolling (on his pelvis)
    float playerDistFromCenter = guy.particles[3].pos.x - playerTargetX;
    if (playerDistFromCenter > 0 || bg.pos.x <= bg.startPos) {
      scroll(-playerDistFromCenter*0.1f);  //xeno to make it smoother
    }
  } else if (gameState.equals("end")) {
    // startmessagetimer needs to be set at the moment end is triggered
    if (millis() > startMessageTimer + intervalTimer) {
        fade("in");
        if (fadeAlpha > 255) {
          gameState = "bonus";  
        }
    }
  } else if (gameState.equals("bonus")) {
    fade("out");      
  }
}

public void draw() {
  update();

  background(255);
  
  if (gameState.equals("title")){
    image(titlePic,0,0);
  }
  else if (gameState.equals("game")){
    bg.draw(playerTargetX);
  
    guy.draw(showHidden);
  
    textDisplayer.draw();
  
    stroke(0);
    line(0, groundY, width, groundY);
  
    //draw the emotion pick ups
    for (int i=0; i<emotions.size(); i++) {
      Emotion thisEmotion = emotions.get(i);
      thisEmotion.draw();
    }
  } else if (gameState.equals("end")) {
    if (guy.emotionalLevel < 33) {
//      image(endingLowEmotions, 0, 0); 
    } else if (guy.emotionalLevel >= 33 && guy.emotionalLevel < 66) {
//      image(endingMedEmotions, 0, 0);
    } else if (guy.emotionalLevel > 66) {
//      image(endingHighEmotions, 0, 0);
    }
    
    if (millis() > startMessageTimer + intervalTimer) {
      noStroke();
      fill(0, fadeAlpha);
      rect(0, 0, width, height);
      stroke(0);
    }
  } else if (gameState.equals("bonus")) {
//    image(bonus, 0, 0);
    noStroke();
    fill(0, fadeAlpha);
    rect(0, 0, width, height);
    stroke(0);
  }
}

public void keyPressed() {
  
  if (gameState.equals("title")){
    startGame();
  }
  else if (gameState.equals("game")){
    guy.checkKeyDown(key);
  }
  
  


  //DEBUG STUFF
  if (key == 'd') {
    showHidden = !showHidden;
  }

  if (keyCode == ENTER) {
    guy.resetPlayer();
  }

  //tetsing EMOTIONS
  if (key == '5') {
    spawnEmotion();
  }
  
//  if (key == ' ') {
//    gameState = "end";  
//  }

//  if (key == 's') {
//    SM.songDull.pause();
//    SM.songEmotional.play();
//  }
//  if (key == 'd') {
//    SM.songEmotional.pause();
//    SM.songDull.play();
//  }

}

public void keyReleased() {
  guy.checkKeyUp(key);
}


public void scroll(float scrollX) {
  guy.scroll(scrollX);
  bg.scroll(scrollX);
  for (int i=0; i<emotions.size(); i++) {
    Emotion thisEmotion = emotions.get(i);
    thisEmotion.scroll(scrollX);
  }
}

public void spawnEmotion() {
  Emotion newEmotion = new Emotion();
  newEmotion.setup();
  emotions.add(newEmotion);
}

public void fade(String whichWay) {
 if (whichWay.equals("in")) {
    if (millis() > startMessageTimer + intervalTimer) {
      fadeAlpha++;
    }
 } else if (whichWay.equals("out")) {
    fadeAlpha--;
 } 
}

class Background {
  PVector pos;

  PImage[] backgroundPics = new PImage[5];
  String[] places = new String[5];

  //PImage myBackground;  //kill me

  //the backgrounds for this round
//  PImage[] theseBackgrounds = new PImage[6];
//  String[] thesePlaces = new String[6];



  String backgroundsUsed = "";

  PImage output;

  int currentIndex;

  float startPos;


  // 700 x 600

  public void setup() {
    startPos = -15;
    

    loadImages();

    
  }
  
  public void reset(){
    pos = new PVector(startPos, 0);
    
  }


  public void scroll(float scrollX) {
    pos.x += scrollX;
  }

  public void draw(float playerTargetX) {
    //image(myBackground, pos.x, pos.y);
    //image(myBackground, 10, 10, myBackground.width/8, myBackground.height/8);
    
    //draw the ones on screen
    int curWidth = 0;
    for (int i=0; i<backgroundPics.length; i++){
      int xPos = (int)pos.x + curWidth;
      
      if (xPos > -backgroundPics[i].width && xPos < width){
        image(backgroundPics[i], xPos, pos.y); 
      }
      
      
      //if this is the one the player is on, give that title to the text display
      if (xPos < playerTargetX && xPos+backgroundPics[i].width > playerTargetX){
        textDisplayer.updateText(places[i]);
      }
      
      
      curWidth += backgroundPics[i].width;
      
    }
  }


  public void loadImages() {
    backgroundPics[0] = loadImage("brickwall.png");
    backgroundPics[1] = loadImage("fastfood.png");
    backgroundPics[2] = loadImage("graduation.png");
    backgroundPics[3] = loadImage("church_interior.png");
    backgroundPics[4] = loadImage("hospital.png");

    places[0] = "childhood";
    places[1] = "fastFood";
    places[2] = "graduation";
    places[3] = "church";
    places[4] = "hospital";
    
  }

}

class Emotion {

  float xPos, yPos;
  float speed;
  float hitSize;

  boolean killMe;
  
  float value;

  public void setup() {

    xPos = width+hitSize*2;
    yPos = 145+50;

    speed = 100;

    hitSize = 30;
    
    value = 20;

    killMe = false;
  }

  public void update(float deltaTime, Person player) {

    xPos -= speed*deltaTime;

    //check all points in the person to see if any are touching
    for (int i=0; i<player.particles.length; i++) {
      if ( dist(xPos, yPos, player.particles[i].pos.x, player.particles[i].pos.y) < hitSize) {
        killMe = true;
        player.earnEmotion(value);
        return;  //no need to keep checking
      }
    }
  }

  public void draw() {

    fill(20, 20, 180);
    ellipse(xPos, yPos, hitSize, hitSize);
  }
  
  public void scroll(float scrollX){
    xPos+=scrollX;
  }
}

class MuscleKey {

  char myKey;
  boolean isDown;
  spring muscle;

  float speed;
  float minPrc;
  float maxPrc;

  float curPrc;
  
  boolean isGrowing;
  
  particle liftPoint;

  public void setup(char _key, spring _muscle, boolean setToGrow, particle _liftPoint) {
    myKey = _key;
    muscle = _muscle;
    curPrc = 1;

    isDown = false;

    speed = 3;
    maxPrc = 1;
    minPrc = 0.5f;

    liftPoint = _liftPoint;

    if (setToGrow) {
      speed*=-1;
      maxPrc = 1.5f;
      minPrc = 1;
    }
  }


  public void checkKeyDown(char thisKey) {
    if (thisKey == myKey) {
      isDown = true;
    }
  }

  public void checkKeyUp(char thisKey) {
    if (thisKey == myKey) {
      isDown = false;
    }
  }

  public void update(float deltaTime) {
    if (isDown) {
      curPrc -= speed*deltaTime;
    }
    else {
      curPrc += speed*deltaTime;
    }

    curPrc = constrain(curPrc, minPrc, maxPrc);
    
    muscle.distance = muscle.originalDistance * curPrc;
    
    if (liftPoint != null){
      
      if (myKey == 'i'){
        liftPoint.vel.x += map(curPrc, minPrc, maxPrc, 0, 1)*1.5f;
      }
      else if (myKey == 'e'){
        liftPoint.vel.x -= map(curPrc, minPrc, maxPrc, 0, 1)*1.5f;
      }else{
       liftPoint.vel.y -= map(curPrc, minPrc, maxPrc, 1, 0)*2;
      }
    }
  }
}

class Person {

  particle[] particles = new particle[12];
  spring[] springs = new spring[27];
  float thisScale;

  PImage[] facePics = new PImage[2];
  PImage curFacePic;
  PVector facePoint;
  float faceAngle;


  MuscleKey[] muscleKeys = new MuscleKey[6];


  int groundY;
  boolean onTheGround;
  
  //emotions
  float emotionalLevel;
  float emotionalDrainPerSec;
  float emotionalLevelCutOff = 50;

  public void setup(int _groundY) {
    facePics[0] = loadImage("pic/pmneuxSad.png");
    facePics[1] = loadImage("pic/pmneux1.png");

    thisScale = 35;   //how much to multiply everything by
    

    groundY = _groundY;
    
    emotionalLevel = 100;
    emotionalDrainPerSec = 2;

    //particle thisParticle;

    for (int i=0; i<particles.length; i++){
      particles[i] = new particle();
    }
    for (int i=0; i<springs.length; i++){
      springs[i] = new spring();
    }
    for (int i=0; i<muscleKeys.length; i++){
      muscleKeys[i] = new MuscleKey();
    }

    resetPlayer();
    
  }
  
  public void resetPlayer(){
    float xOffset = 200;
    float yOffset = 100;

    float defaultSpringiness = 0.2f;
    
    onTheGround = false;
    
    int curParticle = 0;
    int curSpring  = 0;

    //head
    particles[curParticle++].setInitialCondition(xOffset+4* thisScale, yOffset+1* thisScale, 0, 0);
    particles[curParticle++].setInitialCondition(xOffset+4* thisScale, yOffset+2* thisScale, 0, 0);

    //body
    particles[curParticle++].setInitialCondition(xOffset+4* thisScale, yOffset+3* thisScale, 0, 0);
    particles[curParticle++].setInitialCondition(xOffset+4* thisScale, yOffset+5* thisScale, 0, 0);

    //left arm
    particles[curParticle++].setInitialCondition(xOffset+2* thisScale, yOffset+4* thisScale, 0, 0);
    particles[curParticle++].setInitialCondition(xOffset+2* thisScale, yOffset+2* thisScale, 0, 0);

    //right arm
    particles[curParticle++].setInitialCondition(xOffset+6* thisScale, yOffset+0* thisScale, 0, 0);
    particles[curParticle++].setInitialCondition(xOffset+6* thisScale, yOffset+2* thisScale, 0, 0);

    //left leg
    particles[curParticle++].setInitialCondition(xOffset+2* thisScale, yOffset+6* thisScale, 0, 0);
    particles[curParticle++].setInitialCondition(xOffset+2* thisScale, yOffset+8* thisScale, 0, 0);

    //right leg
    particles[curParticle++].setInitialCondition(xOffset+6* thisScale, yOffset+6* thisScale, 0, 0);
    particles[curParticle++].setInitialCondition(xOffset+6* thisScale, yOffset+8* thisScale, 0, 0);
    
    //set the ground for all springs
    for (int i=0; i<particles.length; i++){
      particles[i].groundY = groundY; 
    }

    //put in the visible springs
    //spring visibleSpring;
    //visibleSpring.springiness = defaultSpringiness;
    //visibleSpring.showing = true;

    //head and body
    springs[curSpring++].setFromParticles(particles[0], particles[1], defaultSpringiness, true);
    springs[curSpring++].setFromParticles(particles[1], particles[2], defaultSpringiness*4, true);
    springs[curSpring++].setFromParticles(particles[2], particles[3], defaultSpringiness, true);

    //left arm
    springs[curSpring++].setFromParticles(particles[4], particles[5], defaultSpringiness, true);
    springs[curSpring++].setFromParticles(particles[5], particles[2], defaultSpringiness, true);

    //right arm
    springs[curSpring++].setFromParticles(particles[6], particles[7], defaultSpringiness, true);
    springs[curSpring++].setFromParticles(particles[7], particles[2], defaultSpringiness, true);

    //left leg
    springs[curSpring++].setFromParticles(particles[9], particles[8], defaultSpringiness, true);
    springs[curSpring++].setFromParticles(particles[8], particles[3], defaultSpringiness, true);

    //right leg
    springs[curSpring++].setFromParticles(particles[11], particles[10], defaultSpringiness, true);
    springs[curSpring++].setFromParticles(particles[10], particles[3], defaultSpringiness, true);

    //secret support springs
    //spring supportSpring;
    //supportSpring.springiness = defaultSpringiness;
    //supportSpring.showing = false;

    //left head and arms
    springs[curSpring++].setFromParticles(particles[4], particles[0], defaultSpringiness*4, false);
    springs[curSpring++].setFromParticles(particles[5], particles[1], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[5], particles[0], defaultSpringiness, false);

    //right head and arms
    springs[curSpring++].setFromParticles(particles[6], particles[0], defaultSpringiness*4, false);
    springs[curSpring++].setFromParticles(particles[7], particles[1], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[7], particles[0], defaultSpringiness, false);

    //left body
    springs[curSpring++].setFromParticles(particles[5], particles[8], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[8], particles[2], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[5], particles[3], defaultSpringiness, false);

    //right body
    springs[curSpring++].setFromParticles(particles[7], particles[10], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[10], particles[2], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[3], particles[7], defaultSpringiness, false);

    //legs
    springs[curSpring++].setFromParticles(particles[8], particles[10], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[9], particles[11], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[9], particles[10], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[8], particles[11], defaultSpringiness, false);


    //set up keys
    int curMuscleKey = 0;
    muscleKeys[curMuscleKey++].setup('q', springs[17], false, particles[5]);
    muscleKeys[curMuscleKey++].setup('w', springs[8], false, null);
    muscleKeys[curMuscleKey++].setup('e', springs[25], true, particles[9] );
    
    muscleKeys[curMuscleKey++].setup('i', springs[26], true, particles[11]);
    muscleKeys[curMuscleKey++].setup('o', springs[10], false, null);
    muscleKeys[curMuscleKey++].setup('p', springs[20], false, particles[7]);
  }

  public void resetForces() {
    for (int i = 0; i < particles.length; i++) {
      particles[i].resetForce();
    }
  }

  public void update(float deltaTime) {
    float stretchDist = 50;
    
    //deal with the emotional drain
    emotionalLevel -= emotionalDrainPerSec*deltaTime;
    emotionalLevel = constrain(emotionalLevel, 0, 100);
    curFacePic = (emotionalLevel < emotionalLevelCutOff) ? facePics[0] : facePics[1];


    //check the keys
    for (int i=0; i<muscleKeys.length; i++) {
      muscleKeys[i].update(deltaTime);
    }


    for (int i = 0; i < particles.length; i++) {
      //because of the different orientation, x and y need to be flipped
      particles[i].addForce(0, 0.3f);
      //particles[i].addRepulsionForce(mouseX, mouseY, 300, 0.7f);
    }

    for (int i = 0; i < springs.length; i++) {
      springs[i].update();
    }

    for (int i = 0; i < particles.length; i++) {
      particles[i].bounceOffWalls();
      particles[i].addDampingForce();
      particles[i].update();
    }

    //set the face
    PVector mathVector = particles[0].pos;
    mathVector.mult(2);
    mathVector.add(particles[1].pos);
    mathVector.div(3);
    facePoint = mathVector;
    //facePoint = (particles[0].pos.mult(2) +particles[1].pos)/3;    //favoring point 0
    faceAngle = atan2(particles[2].pos.y-particles[0].pos.y, particles[2].pos.x-particles[0].pos.x)-PI/2;
    
    
    //checking for neck snap
    PVector[] neckCheckPoints = new PVector[4];
    neckCheckPoints[0] = particles[0].pos;
    neckCheckPoints[1] = particles[5].pos;
    neckCheckPoints[2] = particles[2].pos;
    neckCheckPoints[3] = particles[7].pos;
    if (!checkInPolygon(neckCheckPoints, particles[1].pos.x, particles[1].pos.y)) {
      println("you broke his neck");
      //put it back, honky
      //reset the two head particles based on the bottom of the neck
      particles[0].setInitialCondition(particles[2].pos.x, particles[2].pos.y- 2*thisScale, 0, 0);
      particles[1].setInitialCondition(particles[2].pos.x, particles[2].pos.y- 1*thisScale, 0, 0);

    }
    
    //check if he is on the ground
    float groundDistToCount = 1;
    if ( particles[5].pos.y >= groundY-groundDistToCount)  onTheGround = true;
    if ( particles[7].pos.y >= groundY-groundDistToCount)  onTheGround = true;
    
    if (onTheGround){
      resetPlayer();
    }
  }


  public void checkKeyDown(char thisKey) {
    for (int i=0; i<muscleKeys.length; i++) {
      muscleKeys[i].checkKeyDown(thisKey);
    }
  }

  public void checkKeyUp(char thisKey) {
    for (int i=0; i<muscleKeys.length; i++) {
      muscleKeys[i].checkKeyUp(thisKey);
    }
  }


  public void draw(boolean showDebug) {



    fill(0);
    particles[4].draw();
    particles[6].draw();
    particles[9].draw();
    particles[11].draw();

    strokeWeight(2);
    for (int i = 0; i < springs.length; i++) {
      if (springs[i].showing) {
        strokeWeight(2);
        stroke(10);
        springs[i].draw();
      }
      else if (showDebug) {
        strokeWeight(1);
        stroke(100);
        springs[i].draw();
      }

      //in dbeug, show numbers
      if (showDebug) {
        fill(200, 0, 0);
          float xPos = (springs[i].particleA.pos.x + springs[i].particleB.pos.x)/2;
          float yPos = (springs[i].particleA.pos.y + springs[i].particleB.pos.y)/2; 
          String words = ""+i;
          text (words, xPos, yPos);
      }
    }
    strokeWeight(1);

    fill(255);
    pushMatrix();
    translate(facePoint.x, facePoint.y);
    rotate(faceAngle);
    image(curFacePic, -curFacePic.width/2, -curFacePic.height/2);
    popMatrix();

    if (showDebug){
      fill(0,0,255);
      for (int i = 0; i < particles.length; i++){
         String words = ""+i;
         text (words, particles[i].pos.x, particles[i].pos.y);
      }
    }
  }
  
  public void earnEmotion(float val){
    emotionalLevel += val;
  }
  
  public void scroll(float scrollX){
    for (int i=0; i<particles.length; i++){
      particles[i].pos.x += scrollX;
    } 
  }
  
  
  public boolean checkInPolygon(PVector[] p, float x, float y)
  {
    int i =0;
    int j = 0;

    boolean c = false;

    for (i = 0, j = p.length-1; i < p.length; j = i++) {
      if ((((p[i].y <= y) && (y < p[j].y)) ||
        ((p[j].y <= y) && (y < p[i].y))) &&
        (x < (p[j].x - p[i].x) * (y - p[i].y) / (p[j].y - p[i].y) + p[i].x))
        c = !c;
    }
    return c;
  }
}

class PlaceText {

  ArrayList<String>  dullText = new ArrayList<String>();  
  ArrayList<String>  emotionalText = new ArrayList<String>();


  String area;

  public void setup(String _area) {
    area = _area;

    //fill it up!
    checkTextRefill();
  }

  public void checkTextRefill() {

    if (area.equals("childhood")) {
      if (dullText.size() == 0) {
        //println("refill dull childhood now");
        refillDullChildhoodText();
      }
      if (emotionalText.size() == 0) {
        refillEmotionalChildhoodText();
      }
    }

    if (area.equals("hospital")) {
      if (dullText.size() == 0) {
        refillDullHospitalText();
      }
      if (emotionalText.size() == 0) {
        refillEmotionalHospitalText();
      }
    }

    if (area.equals("church")) {
      if (dullText.size() == 0) {
        refillDullChurchText();
      }
      if (emotionalText.size() == 0) {
        refillEmotionalChurchText();
      }
    }

    if (area.equals("fastFood")) {
      if (dullText.size() == 0) {
        refillDullFastFoodText();
      }
      if (emotionalText.size() == 0) {
        refillEmotionalFastFoodText();
      }
    }
    if (area.equals("graduation")) {
      if (dullText.size() == 0) {
        refillDullGraduationText();
      }
      if (emotionalText.size() == 0) {
        refillEmotionalGraduationText();
      }
    }
  }

  public String getDullText() {
    //println("get dull for "+area);
    int randNum = PApplet.parseInt(random(dullText.size()));
    String returnText = dullText.get(randNum);
    //remove it
    dullText.remove(randNum);
    //see if we need more
    //println("you check it");
    checkTextRefill();

    return returnText;
  }

  public String getEmotionalText() {
    int randNum = PApplet.parseInt(random(emotionalText.size()));
    String returnText = emotionalText.get(randNum);
    //remove it
    emotionalText.remove(randNum);
    //see if we need more
    checkTextRefill();

    return returnText;
  }
  
  
  
  public void refillEmotionalChildhoodText() {
    emotionalText.clear();

    emotionalText.add("A butterfly lands on a flower, the colors of the two combining to form the most gorgeous thing you've ever seen. For the first time, you know what true sadness is.");

    emotionalText.add("Your father has purchased you a red ball for your birthday. You begin to realize just how many emotions there really are in the world, and how much you have to learn.");

    emotionalText.add("When you got an A on an assignment, you immediately think, 'Am I really as good or better than everyone else here? Who can make that decision? Can anyone?' You ponder this, lost in thought as you accidentally get locked in school again.");

    emotionalText.add("You see a car drive by your house. Are they on their way home? To a wedding? A funeral? Both? Could anyone handle the number of emotions a person must feel to attend a wedding/funeral? Part of you hopes to never find out, but part of you does.");

    emotionalText.add("A bully beats you up after school. But what hurts more than the punches is not your pain, but thinking about the pain he must feel that he became a bully. Who, really, is suffering more?");

    emotionalText.add("As you successfully use the toilet for the first time, you consider how lonely your potty will feel. How sad it will be that no one will ever use it again, that it will be a relic of your, and only your, childhood. This keeps you up for three nights.");
  }


  public void refillDullChildhoodText() {
    dullText.clear();
    
    //println("fill it you dull child");

    dullText.add("Your dad gives you a red ball for your birthday. You are happy that you can utilize it for exercise, as well as appreciating the difficulty in manufacturing a completely circular ball. You nod to your father.");
    dullText.add("As a butterfly lands on a flower, you don't know whether to crush in order to rid the world of bugs or let it be devoured by some other insect. Bugs have no place in a civilized society.");
  }


  public void refillEmotionalHospitalText() {
    emotionalText.clear();

    emotionalText.add("Hundreds of millions of years from now, the sun will die out, and there will be no more sunsets. \"The people of the future will never be as emotionally fulfilled as us,\" you think.");
    emotionalText.add("'What if everyone can feel more than I can?' This brings tears to your eyes.");
  }

  public void refillDullHospitalText() {
    dullText.clear();

    dullText.add("You stare at the dead and dying in the hospital, wondering just where all their stuff goes after they die. Does the hospital get it? Do they return it to the family. These are important things to consider.");
    dullText.add("You see a mother weeping over the body of her child. If you had the strength, you would go up to her, put your hand on her shoulder, and tell her to compose herself in public.");
  }



  public void refillEmotionalChurchText() {
    emotionalText.clear();

    emotionalText.add("You begin to think of how a child must feel when they are separated from their mother. Children, you realize, have much to teach us.");
    emotionalText.add("You watch as the couple before you exchange vows. Through your life, you thought that you had potentially cried all the tears you ever could. You now know that you have, if ");
  }

  public void refillDullChurchText() {
    dullText.clear();

    dullText.add("As you walk by a funeral and see dozens of people weeping, you think about watering your cactus. You then remember you let it wither from neglect. This does not bother you.");
    dullText.add("When you hear about your best friend getting a divorce, you are sad to think of how only one of them will be able to claim their child as a dependent.");
  }



  public void refillEmotionalFastFoodText() {
    emotionalText.clear();

    emotionalText.add("As you watch a boy play with a puppy, you become stricken with grief.");
    emotionalText.add("You know that each one of these hamburgers is mostly the same, but some part of them is slightly different. They are each their own, unique hamburger, enjoyed by their own unique person, in a unique location, all across the world. Your hamburger is now filled with tears and must be sent back.");
  }

  public void refillDullFastFoodText() {
    dullText.clear();

    dullText.add("You see children laughing and playing without a care in the world, free from the oppression of responsibilities as the warm sun shines down. 'Good,' you think, 'my daily Vitamin D requirements have been met. Back inside.'");
    dullText.add("You write a review for the last video game you played and end it with 'but the graphics were next-gen, so it's worth purchasing.'");
  }


  public void refillEmotionalGraduationText() {
    emotionalText.clear();

    emotionalText.add("As you microwave your dinner for the evening, you see your rotating food as a representation for man's struggle to find good in the world. You are not sure you can eat such a perfect metaphor now.");
    emotionalText.add("After completing a game with cutting edge graphics, you feel overwhelmed with the sadness thinking about how the artists of the game will never match the beauty of a rainbow.");
  }

  public void refillDullGraduationText() {
    dullText.clear();

    dullText.add("As a bright rainbow shines over the a hill, creating a picturesque view of a nature, you shut the blinds and play another game of solitaire.");
    dullText.add("As you step up to the podium to receive your degree in accounting, you nod your head in acknowledgment to your teachers and friends, happy you never formed a single connection to anyone. There is no time for friendship in this world.");
  }
  

}



class SoundManager {

  AudioPlayer songDull;
  AudioPlayer songEmotional;
  
  AudioPlayer emotionGet;
  AudioPlayer grunt;
  AudioPlayer startGame;
  
  Minim minim;
  
  
  public void setup(Minim _minim){
    minim = _minim;
    
    //music
    songDull = minim.loadFile("audio/dull.mp3", 2048);
    songDull.loop();
    
    songEmotional = minim.loadFile("audio/emotional.mp3", 2048);
    songEmotional.loop();
    
    songDull.pause();
    songEmotional.pause();
    
    
    //start game
    emotionGet = minim.loadFile("audio/emotionGetCut.wav");
    grunt = minim.loadFile("audio/grunt.wav");
    startGame = minim.loadFile("audio/startGame.wav");
    
    
    
  }
  
  public void playGrunt(){
    grunt.play(); 
  }
  public void playemotionGet(){
    emotionGet.rewind();
   emotionGet.play(); 
  }
  public void playStartGame(){
   startGame.play(); 
  }
}

class TextDisplayer {
  int NUM_AREAS = 5;
  PFont emotional;
  PFont unemotional;

  PlaceText[] allText = new PlaceText[NUM_AREAS];
  String currentLine;

  //sorting it all by place/emotion
  int placeNum;
  boolean showEmotionalText = true;


  ArrayList<String> parsed = new ArrayList<String>();
  ArrayList<Float> triggerTimes;
  ArrayList<Float> alphas;

  boolean shouldFadeAllOut;
  boolean shouldShowAnyText;

  float interval;

  float delayBetweenPassages;
  
  float curEmotionalLevel;
  float emotionalCutOff;

  public void setup() {
    interval = 6000;
    delayBetweenPassages = 5000;

    emotional = loadFont("JosefinSans-24.vlw");
    textFont(emotional, 24);
    textAlign(CENTER);

    //get our text
    for (int i=0; i<NUM_AREAS; i++)  allText[i] = new PlaceText();
    allText[0].setup("childhood");
    allText[1].setup("hospital");
    allText[2].setup("church");
    allText[3].setup("fastFood");
    allText[4].setup("graduation");

    selectString();
    parsed = parseString(currentLine);
    triggerText(parsed);
    shouldShowAnyText = true;

    showEmotionalText = true;
  }

  public void updateText(String location) {
    placeNum = 0;  //default in case nothing else triggers
    //println("I'm in "+location);
    for (int i=0; i<allText.length; i++) {
      if (allText[i].area.equals(location)) {  
        placeNum = i;
        //println("that's number "+i);
      }
    }
  }

  public void updateShowEmotionalText(boolean _showEmotionalText, float _curEmotionalLevel, float _emotionalCutOff) {
    showEmotionalText = _showEmotionalText;
    
    curEmotionalLevel = _curEmotionalLevel;
    emotionalCutOff = _emotionalCutOff;
  }

  public void draw() {
    if (shouldShowAnyText) {      
      // fade out if last trigger happened + interval
      if (millis() > triggerTimes.get(triggerTimes.size() - 1) + interval) {
        shouldFadeAllOut = true;
      }

      int curY = 50;
      int spacePerLine = 33;
      if (!shouldFadeAllOut) { // if we're not fading all of them out
        for (int i = 0; i < triggerTimes.size(); i++) {
          if (millis() > triggerTimes.get(i)) {
            fill(0, alphas.get(i));
            text(parsed.get(i), width/2, curY);
            if (alphas.get(i) < 255) {
              float tempAlpha = alphas.get(i);
              tempAlpha++;
              alphas.set(i, tempAlpha);
            }
            curY += spacePerLine;
            //add the spacing again if it has a line break
            if (parsed.get(i).contains("\n")) {
              curY += spacePerLine;
            }
          }
        }
      } 
      else {
        for (int i = 0; i < triggerTimes.size(); i++) {
          fill(0, alphas.get(i));
          text(parsed.get(i), width/2, curY);
          curY += spacePerLine;
          //add the spacing again if it has a line break
          if (parsed.get(i).contains("\n")) {
            curY += spacePerLine;
          }
          if (alphas.get(i) > 0) {
            float tempAlpha = alphas.get(i);
            tempAlpha--;
            alphas.set(i, tempAlpha);
          } 
          else {
            reset();
            break;
          }

        }
      }
    }
  }

  public void reset() {
    //println("reset snoopy balls");
    parsed.clear();
    triggerTimes.clear();
    alphas.clear();
    shouldFadeAllOut = false;

    selectString();
    parsed = parseString(currentLine);
    triggerText(parsed);
    shouldShowAnyText = true;
    delayBetweenPassages = PApplet.parseInt(random(4000, 7000));
  }

  public void triggerText(ArrayList<String> parsed) {
    triggerTimes = new ArrayList<Float>();
    alphas = new ArrayList<Float>();

    int startTime = millis();
    for (int i = 0; i < parsed.size(); i++) {
      triggerTimes.add(startTime + delayBetweenPassages + (i * interval)); 
      alphas.add(PApplet.parseFloat(0));
    }
  }

  public void selectString() {
    if (showEmotionalText) {
      //println("get it exciting");
      currentLine = allText[placeNum].getEmotionalText();
    }
    else {
      //println("get it dull");
      currentLine = allText[placeNum].getDullText();
    }
  }

  public ArrayList<String> parseString(String currentLine) {

    ArrayList<String> broken = new ArrayList<String>();

    String tempLine[] = currentLine.split("(?<=[.?!]) ");
    for (int i = 0; i < tempLine.length; i++) {
      broken.add(tempLine[i]);
    }

    //go through each parsed line and make sure it will fit on screen
    ArrayList<String> trimmed = new ArrayList<String>();
    for (int i=0; i<broken.size(); i++) {
      String thisLine = broken.get(i);
      String withBreaks = addLineBreaks(thisLine);

      trimmed.add(withBreaks);
    }

    return trimmed;
  }

  public String addLineBreaks(String rawLine) {

    String cleanLine = ""; 

    int curChar = -1;


    while (curChar < rawLine.length ()-1) {

      boolean wentOver = false;
      while (curChar < rawLine.length ()-1 && !wentOver) {

        curChar++;
        cleanLine += rawLine.charAt(curChar);


        //println("cur width "+textWidth(cleanLine));
        if (textWidth(cleanLine) > width*0.98f) {
          wentOver = true;
        }
      }

      if (wentOver) {
        //go back until we hit a space
        int spaceChar = curChar;
        if (wentOver) {
          while (cleanLine.charAt (spaceChar) != ' ') {
            //println("space char "+spaceChar);
            spaceChar--;
          } 
          cleanLine = cleanLine.substring(0, spaceChar) +"\n" + cleanLine.substring(spaceChar+1, cleanLine.length() );
        }
      }
    }
    
    return cleanLine;
 }
 
 
}


class particle
{
  PVector pos;
  PVector vel;
  PVector frc;   // frc is also know as acceleration (newton says "f=ma")

  boolean  bFixed;


  float groundY;  //set in Person
  
  float damping;

  //------------------------------------------------------------
  particle() {
    setInitialCondition(0, 0, 0, 0);
    damping = 0.05f;
    bFixed = false;
  }

  //------------------------------------------------------------
  public void resetForce() {
    // we reset the forces every frame
    frc = new PVector(0,0);
  }

  //------------------------------------------------------------
  public void addForce(float x, float y) {
    // add in a fPVectororce in X and Y for this frame.
    frc.x = frc.x + x;
    frc.y = frc.y + y;
  }

/*
  //------------------------------------------------------------
  void addRepulsionForce(float x, float y, float radius, float scale) {

    // ----------- (1) make a vector of where this position is: 

    PVector posOfForce = new PVector(x, y);

    // ----------- (2) calculate the difference & length 

    PVector diff	= pos - posOfForce;
    float length	= diff.length();

    // ----------- (3) check close enough

      bool bAmCloseEnough = true;
    if (radius > 0) {
      if (length > radius) {
        bAmCloseEnough = false;
      }
    }

    // ----------- (4) if so, update force

      if (bAmCloseEnough == true) {
      float pct = 1 - (length / radius);  // stronger on the inside
      diff.normalize();
      frc.x = frc.x + diff.x * scale * pct;
      frc.y = frc.y + diff.y * scale * pct;
    }
  }
  

  //------------------------------------------------------------
  void addAttractionForce(float x, float y, float radius, float scale) {

    // ----------- (1) make a vector of where this position is: 

    PVector posOfForce;
    posOfForce.set(x, y);

    // ----------- (2) calculate the difference & length 

    PVector diff	= pos - posOfForce;
    float length	= diff.length();

    // ----------- (3) check close enough

      bool bAmCloseEnough = true;
    if (radius > 0) {
      if (length > radius) {
        bAmCloseEnough = false;
      }
    }

    // ----------- (4) if so, update force

      if (bAmCloseEnough == true) {
      float pct = 1 - (length / radius);  // stronger on the inside
      diff.normalize();
      frc.x = frc.x - diff.x * scale * pct;
      frc.y = frc.y - diff.y * scale * pct;
    }
  }

  //------------------------------------------------------------
  void addRepulsionForce(particle p, float radius, float scale) {

    // ----------- (1) make a vector of where this particle p is: 
    PVector posOfForce;
    posOfForce.set(p.pos.x, p.pos.y);

    // ----------- (2) calculate the difference & length 

    PVector diff	= pos - posOfForce;
    float length	= diff.length();

    // ----------- (3) check close enough

      bool bAmCloseEnough = true;
    if (radius > 0) {
      if (length > radius) {
        bAmCloseEnough = false;
      }
    }

    // ----------- (4) if so, update force

      if (bAmCloseEnough == true) {
      float pct = 1 - (length / radius);  // stronger on the inside
      diff.normalize();
      frc.x = frc.x + diff.x * scale * pct;
      frc.y = frc.y + diff.y * scale * pct;
      p.frc.x = p.frc.x - diff.x * scale * pct;
      p.frc.y = p.frc.y - diff.y * scale * pct;
    }
  }
  
  
  //------------------------------------------------------------
  void addAttractionForce(particle p, float radius, float scale) {

    // ----------- (1) make a vector of where this particle p is: 
    PVector posOfForce;
    posOfForce.set(p.pos.x, p.pos.y);

    // ----------- (2) calculate the difference & length 

    PVector diff	= pos - posOfForce;
    float length	= diff.length();

    // ----------- (3) check close enough

      bool bAmCloseEnough = true;
    if (radius > 0) {
      if (length > radius) {
        bAmCloseEnough = false;
      }
    }

    // ----------- (4) if so, update force

      if (bAmCloseEnough == true) {
      float pct = 1 - (length / radius);  // stronger on the inside
      diff.normalize();
      frc.x = frc.x - diff.x * scale * pct;
      frc.y = frc.y - diff.y * scale * pct;
      p.frc.x = p.frc.x + diff.x * scale * pct;
      p.frc.y = p.frc.y + diff.y * scale * pct;
    }
  }
  
*/

  //------------------------------------------------------------
  public void addDampingForce() {

    // the usual way to write this is  vel *= 0.99
    // basically, subtract some part of the velocity 
    // damping is a force operating in the oposite direction of the 
    // velocity vector

    frc.x = frc.x - vel.x * damping;
    frc.y = frc.y - vel.y * damping;
  }


  //------------------------------------------------------------
  public void setInitialCondition(float px, float py, float vx, float vy) {
    pos = new PVector(px, py);
    vel = new PVector(vx, vy);
  }

  //------------------------------------------------------------
  public void update() {	
    if (bFixed == false) {
      vel.add(frc);
      pos.add(vel);
    }
  }

  //------------------------------------------------------------
  public void draw() {
    ellipse(pos.x, pos.y, 6,6);
  }


  //------------------------------------------------------------
  public void bounceOffWalls() {

    // sometimes it makes sense to damped, when we hit
    boolean bDampedOnCollision = true;
    boolean bDidICollide = false;


    if (pos.y > groundY) {
      pos.y = groundY; // move to the edge, (important!)
      vel.y *= -1;
      bDidICollide = true;
    } 
    
    if (bDidICollide == true && bDampedOnCollision == true) {
      vel.mult(0.5f);
    }
    // what are the walls
//    float minx = 0;
//    float miny = 0;
//    float maxx = width;
//    float maxy = height;
//
//    if (pos.x > maxx) {
//      pos.x = maxx; // move to the edge, (important!)
//      vel.x *= -1;
//      bDidICollide = true;
//    } 
//    else if (pos.x < minx) {
//      pos.x = minx; // move to the edge, (important!)
//      vel.x *= -1;
//      bDidICollide = true;
//    }
//
//    if (pos.y > maxy) {
//      pos.y = maxy; // move to the edge, (important!)
//      vel.y *= -1;
//      bDidICollide = true;
//    } 
//    else if (pos.y < miny) {
//      pos.y = miny; // move to the edge, (important!)
//      vel.y *= -1;
//      bDidICollide = true;
//    }
//
//    if (bDidICollide == true && bDampedOnCollision == true) {
//      vel.mult(0.3);
//    }
  }

}

class spring {

  particle particleA;
  particle particleB;

  float originalDistance;
  float distance;
  float springiness;	 // this is the k, springiness constant

  boolean showing;
  
  float origAngle;
  float angleRange = PI*0.7f;


  //---------------------------------------------------------------------
  spring() {
    particleA = null;
    particleB = null;

    showing = false;
    
  }

  //---------------------------------------------------------------------
  public void setFromParticles(particle _particleA, particle _particleB) {
    particleA = _particleA;
    particleB = _particleB;

    distance = particleA.pos.dist(particleB.pos);
    originalDistance = distance;
    
    origAngle = atan2(particleA.pos.y-particleB.pos.y, particleA.pos.x-particleB.pos.y);
  }
  
    //---------------------------------------------------------------------
  public void setFromParticles(particle _particleA, particle _particleB, float _springiness, boolean _showing) {
    particleA = _particleA;
    particleB = _particleB;
    
    springiness = _springiness;
    showing = _showing;

    distance = particleA.pos.dist(particleB.pos);
    originalDistance = distance;
  }

  //---------------------------------------------------------------------
  public void update() {
    if ((particleA == null) || (particleB == null)) {
      return;
    }

    PVector pta = particleA.pos;
    PVector ptb = particleB.pos;
    
    PVector temp = pta.get();
    temp.sub(ptb);

    float theirDistance = temp.mag();
    float springForce = (springiness * (distance - theirDistance));
    
    temp.normalize();
    temp.mult(springForce);
    PVector frcToAdd = temp;

    particleA.addForce(frcToAdd.x, frcToAdd.y);
    particleB.addForce(-frcToAdd.x, -frcToAdd.y);
    
    //check the angle
//    float curAngle = atan2(particleA.pos.y-particleB.pos.y, particleA.pos.x-particleB.pos.y);
//    if ( abs(curAngle-origAngle) > angleRange){
//      
//    }
    
  }


  //---------------------------------------------------------------------
  public void draw() {

    if ((particleA == null) || (particleB == null)) {
      return;
    }

    line(particleA.pos.x, particleA.pos.y, particleB.pos.x, particleB.pos.y);
  }
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "molyjam" });
  }
}