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
PImage[] emotionPics = new PImage[4];

ArrayList<Burst> bursts = new ArrayList<Burst>();

//emotion warning
WarningText warningText = new WarningText();

// background
Background bg = new Background();
float playerTargetX;

// text displayer
TextDisplayer textDisplayer = new TextDisplayer();

// title
TitleScene titleScene = new TitleScene();

//end
EndScene endScene = new EndScene();
float gameTimer;
float gameTime = 120;  //number of seconds in the game

//sound manager
SoundManager SM = new SoundManager();
Minim minim;




int groundY;

boolean showHidden;

//title stuff
String gameState;
PImage titlePic;



public void setup() {

  size(900, 500);

  frameRate(60);
  smooth();

  groundY = height - 50;

  guy.setup(groundY, SM);
  playerTargetX = width/2;


  showHidden = false;

  prevFrameTime = millis();

  bg.setup();
  textDisplayer.setup();

  minim = new Minim(this);
  SM.setup(minim);

  //setup the title
  gameState = "title";
  titlePic = loadImage("/data/TitlePieces/EmptyBackground.png");
  titleScene.setup(SM);

  endScene.setup();
  
  warningText.setup();

  //set emotion pick ups
  for (int i=0; i<emotionPics.length; i++) {
    String picName = "data/Pickups/pickup"+i+".png";
    emotionPics[i] = loadImage(picName);
  }
}

public void startGame() {
  //start with some time before the first emotion spawn
  emotionSpawnTimer = emotionMaxNextSpawnTime;

  bg.reset();
  guy.resetPlayer();

  tint(255, 255);

  gameTimer = 0;

  gameState = "game";
}

public void endGame() {
  gameState = "end";
  endScene.start(guy.emotionalLevel);
  guy.collapse();
  textDisplayer.shouldShowAnyText = false;
}

public void update() {
  float deltaTime = ((float)millis()-prevFrameTime)/1000.0f;
  prevFrameTime = millis();

  if (gameState.equals("title")) {
    //fucking nothing right now goddamn
    titleScene.updateTitle();
  }
  else if (gameState.equals("game") || gameState.equals("end")) {
    guy.resetForces();

    guy.update(deltaTime, gameState.equals("game"));
    
    if (guy.emotionalLevel < guy.emotionalLevelCutOff+20 && guy.emotionalLevel > guy.emotionalLevelCutOff && !warningText.active){
      warningText.trigger();
    }
    else if (guy.emotionalLevel > guy.emotionalLevelCutOff+20){
     warningText.active = false; 
    }

    //check what kind of text we should be showing
    boolean showEmotionalText = guy.emotionalLevel > 50;
    textDisplayer.updateShowEmotionalText( showEmotionalText, guy.emotionalLevel, guy.emotionalLevelCutOff );

    //update emotion pick ups
    for (int i=emotions.size()-1; i>=0; i--) {
      Emotion thisEmotion = emotions.get(i);
      thisEmotion.update(deltaTime, guy);

      if (thisEmotion.killMe) {
        //play sound
        SM.playemotionGet();

        //create burtsts
        for (int k=0; k<80; k++) {
          Burst newBurst = new Burst();
          newBurst.setup(thisEmotion.xPos, thisEmotion.yPos);
          bursts.add(newBurst);
        }

        //actuall kill it
        emotions.remove(i);
      }
    }

    //update the bursts
    for (int i=bursts.size()-1; i>=0; i--) {
      Burst thisBurst = bursts.get(i);
      thisBurst.update(deltaTime);

      if (thisBurst.killMe) {
        bursts.remove(i);
      }
    }
    
    //update the warning
    warningText.update();

    //is it time for a new emotion?
    emotionSpawnTimer -= deltaTime;
    if (emotionSpawnTimer <= 0) {
      spawnEmotion();
      emotionSpawnTimer = random(emotionMinNextSpawnTime, emotionMaxNextSpawnTime);
    }

    //chekc sounds
    SM.update(showEmotionalText);

    //bg.updateBackground();

    //check for scrolling (on his pelvis)
    float playerDistFromCenter = guy.particles[3].pos.x - playerTargetX;
    if ( (playerDistFromCenter > 0 || bg.pos.x <= bg.startPos) && ( playerDistFromCenter < 0 || bg.pos.x >= bg.endPos)) {
      scroll(-playerDistFromCenter*0.1f);  //xeno to make it smoother
    }

    //is it time to die?
    if (gameState.equals("game")) {
      gameTimer += deltaTime;
      if ( (playerDistFromCenter > 0 && bg.pos.x < bg.endPos) || gameTimer >= gameTime) {
        endGame();
      }
    }
  } 


  if (gameState.equals("end")) {
    endScene.update(deltaTime);
  }
}

public void draw() {
  update();

  background(255);

  if (gameState.equals("title")) {
    image(titlePic, 0, 0);
    titleScene.drawTitle();
  }
  else if (gameState.equals("game") || gameState.equals("end")) {
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
    //and their bursts
    for (int i=0; i<bursts.size(); i++) {
      Burst thisBurst = bursts.get(i);
      thisBurst.draw();
    }
    
    //emotions warning
    warningText.draw();
  } 

  if (gameState.equals("end")) {
    endScene.draw();
  }
}

public void keyPressed() {

  if (gameState.equals("title") &&  millis() > titleScene.startTime + titleScene.delaySubtitle + 1000) {
    startGame();
  }
  else if (gameState.equals("game")) {
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

  if (key == 'k') {
    endGame();
  }

  if (key == '-') {
    guy.emotionalLevel = 0;
  }
  if (key=='=') {
    guy.emotionalLevel = 100;
  }
  
  if (key=='t') {
    warningText.trigger();
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
  for (int i=0; i<bursts.size(); i++) {
    Burst thisBurst = bursts.get(i);
    thisBurst.scroll(scrollX);
  }
}

public void spawnEmotion() {
  Emotion newEmotion = new Emotion();
  int randNum = (int)random(emotionPics.length);
  newEmotion.setup(emotionPics[randNum]);
  emotions.add(newEmotion);
}


class Background {
  PVector pos;

  PImage[] backgroundPics = new PImage[6];
  String[] places = new String[6];

  //PImage myBackground;  //kill me

  //the backgrounds for this round
  //  PImage[] theseBackgrounds = new PImage[6];
  //  String[] thesePlaces = new String[6];



  String backgroundsUsed = "";

  PImage output;

  int currentIndex;

  float startPos;
  float endPos;


  // 700 x 600

  public void setup() {

    loadImages();

    startPos = -15;

    endPos = -backgroundPics[0].width* (backgroundPics.length-1) + 30;
  }

  public void reset() {
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
    for (int i=0; i<backgroundPics.length; i++) {
      int xPos = (int)pos.x + curWidth;

      if (xPos > -backgroundPics[i].width && xPos < width) {
        image(backgroundPics[i], xPos, pos.y);
      }


      //if this is the one the player is on, give that title to the text display
      if (xPos < playerTargetX && xPos+backgroundPics[i].width > playerTargetX) {
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
    backgroundPics[5] = loadImage("graveyard.png");

    places[0] = "childhood";
    places[1] = "fastFood";
    places[2] = "graduation";
    places[3] = "church";
    places[4] = "hospital";
    places[5] = "graveyard";
  }
}

class Burst {

  

  PVector pos;
  PVector vel;

  float grav = 0.1f;
  float fric = 0.97f;

  int col;

  float colR, colG, colB, colA;
  boolean killMe;
  
  float startTime = 1.5f;
  float timer;

  public void setup(float startX, float startY) {
    pos = new PVector(startX, startY);
    
    killMe = false;

    colR = random(255);
    colG = random(255);
    colB = random(255);
    
    timer = startTime;
    
    //give it a random angle and power
    float power = random(2,7);
    float angle = random(TWO_PI);
    
    vel = new PVector(0,0);
    vel.x = cos(angle)*power;
    vel.y = sin(angle)*power;
  }
  
  public void update(float deltaTime){
    
    timer -= deltaTime;
    
    if (timer<0){
      killMe = true; 
    }
    
    colA = map(timer, startTime, 0, 255, 0);  
    
    vel.y+=grav;
    vel.x *= fric;
    vel.y *= fric;
    
    pos.x += vel.x;
    pos.y += vel.y;
    
    
  }
  
  public void draw(){
    float drawSize = 4;
    fill(colR, colG, colB, colA);
    noStroke();
    ellipse(pos.x, pos.y, drawSize, drawSize);
  }
  
  public void scroll(float scrollX){
    pos.x+=scrollX;
  }
  
}

class Emotion {

  float xPos, yPos;
  float speed;
  float hitSize;

  boolean killMe;
  
  float value;
  
  PImage pic;

  public void setup(PImage _pic) {
    pic = _pic;

    xPos = width+hitSize*2;
    yPos = 140 ;

    speed = 100;

    hitSize = 38;
    
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

    image(pic, xPos-pic.width/2, yPos-pic.height/2);
    fill(20, 20, 180);
    //ellipse(xPos, yPos, hitSize, hitSize);
    
  }
  
  public void scroll(float scrollX){
    xPos+=scrollX;
  }
}

class EndScene {

  PImage endBGPic;
  
  PImage[] endPics = new PImage[3];
  PVector[] endPicsOffsets = new PVector[3];
  int curEndPic;
  
  
  PImage creditsPic, attributionPic;


  //fade in/out timer for endgame + bonus screen
  float startMessageTimer;
  float intervalTimer = 10000;

  float fadeAlpha;
  float fadeTime = 7;
  float pauseTime = 10;
  float fadeTimer;
  
  boolean fadeIn;
  
  int phase;


  public void setup() {

    endBGPic = loadImage("data/EndPieces/EndBG.png");
    endPics[0] = loadImage("data/EndPieces/EndEmotional.png");
    endPics[1] = loadImage("data/EndPieces/EndSemiEmotional.png");
    endPics[2] = loadImage("data/EndPieces/EndUnemotional.png");

    endPicsOffsets[0] = new PVector(114,171);
    endPicsOffsets[1] = new PVector(114, 171);
    endPicsOffsets[2] = new PVector(114, 183);

    creditsPic = loadImage("data/EndPieces/Credits.png");
    attributionPic = loadImage("data/EndPieces/Attribution.png");
  }

  public void start(float emotionalLevel) {
    curEndPic = 0; 
    
    phase = 0;
    
    if (emotionalLevel < 70){
      curEndPic = 1; 
    }
    if (emotionalLevel < 30){
      curEndPic = 2;
    }

    fadeAlpha = 0;
    fadeTimer = 0;
    fadeIn = true;

    //showingBonus = false;
  }


  public void update(float deltaTime) {

    fadeTimer+=deltaTime;

    if (fadeIn) {
      fadeAlpha = map(fadeTimer, 0, fadeTime, 0, 255);
      fadeAlpha = constrain(fadeAlpha, 0, 255);
      if (fadeTimer >= fadeTime + pauseTime && phase<4) {
        pauseTime = 5;
        phase++;
        fadeTimer = 0;
        fadeAlpha = 0;
      }
    }
//    else {
//      fadeAlpha = map(fadeTimer, 0, fadeTime, 255, 0);
//    }
  }


  public void draw() {

    if (phase == 0){
      tint(255, fadeAlpha);
    }else{
      tint(255, 255);
    }
    image(endBGPic, 0, 0);
    
    
    if (phase == 0 || phase == 1){
      float thisAlpha = phase == 0 ? fadeAlpha : 255-fadeAlpha;
      tint(255, thisAlpha);
      image(endPics[curEndPic], endPicsOffsets[curEndPic].x, endPicsOffsets[curEndPic].y);
    }
    
    
    if (phase == 1 || phase ==2){
      float thisAlpha = phase==1 ? fadeAlpha : 255-fadeAlpha;
      tint(255, thisAlpha);
      image(creditsPic, 142, 154);
    }
    
    if (phase == 2 || phase ==3){
      println("this phase: "+phase);
      float thisAlpha = phase==2 ? fadeAlpha : 255-fadeAlpha;
      tint(255, thisAlpha);
      image(attributionPic, 142, 154);
    }

    tint(255, 255);
  }



  public void fade(String whichWay) {
    if (whichWay.equals("in")) {
      if (millis() > startMessageTimer + intervalTimer) {
        fadeAlpha++;
      }
    } 
    else if (whichWay.equals("out")) {
      fadeAlpha--;
    }
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
  
  float pull = 0.3f;  //how much to pull the player each frame


  MuscleKey[] muscleKeys = new MuscleKey[6];


  int groundY;
  boolean onTheGround;

  //emotions
  float emotionalLevel;
  float emotionalDrainPerSec;
  float emotionalLevelCutOff = 50;

  SoundManager SM;

  boolean collapsed;

  public void setup(int _groundY, SoundManager _SM) {
    facePics[0] = loadImage("pic/pmneuxSad.png");
    facePics[1] = loadImage("pic/pmneux1.png");

    SM = _SM;

    thisScale = 35;   //how much to multiply everything by


    groundY = _groundY;

    emotionalLevel = 100;
    emotionalDrainPerSec = 1;

    //particle thisParticle;

    for (int i=0; i<particles.length; i++) {
      particles[i] = new particle();
    }
    for (int i=0; i<springs.length; i++) {
      springs[i] = new spring();
    }
    for (int i=0; i<muscleKeys.length; i++) {
      muscleKeys[i] = new MuscleKey();
    }

    resetPlayer();
  }

  public void resetPlayer() {
    collapsed = false;

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
    for (int i=0; i<particles.length; i++) {
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

  public void update(float deltaTime, boolean stillInGame) {
    float stretchDist = 50;

    //deal with the emotional drain
    if (stillInGame){
      emotionalLevel -= emotionalDrainPerSec*deltaTime;
      emotionalLevel = constrain(emotionalLevel, 0, 100);
      curFacePic = (emotionalLevel < emotionalLevelCutOff) ? facePics[0] : facePics[1];
      //println("emotion "+emotionalLevel);
    }


    //check the keys
    boolean anyKeyIsDown = false;
    for (int i=0; i<muscleKeys.length; i++) {
      muscleKeys[i].update(deltaTime);
      
      if (muscleKeys[i].isDown){
       anyKeyIsDown = true; 
      }
    }


    for (int i = 0; i < particles.length; i++) {
      //because of the different orientation, x and y need to be flipped
      particles[i].addForce(0, 0.3f);
      //particles[i].addRepulsionForce(mouseX, mouseY, 300, 0.7f);
    }

    if (!collapsed) {
      for (int i = 0; i < springs.length; i++) {
        springs[i].update();
      }
    }

    for (int i = 0; i < particles.length; i++) {
      particles[i].bounceOffWalls();
      particles[i].addDampingForce();
      particles[i].update();
      
      //pull the player
      if (!collapsed && anyKeyIsDown){
       particles[i].pos.x += pull; 
      }
    }

    //set the face
    PVector mathVector = particles[0].pos;
    mathVector.mult(2);
    mathVector.add(particles[1].pos);
    mathVector.div(3);
    facePoint = mathVector;
    //facePoint = (particles[0].pos.mult(2) +particles[1].pos)/3;    //favoring point 0
    faceAngle = atan2(particles[2].pos.y-particles[0].pos.y, particles[2].pos.x-particles[0].pos.x)-PI/2;


    if (!collapsed) {
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

      if (onTheGround) {
        resetPlayer();
        SM.playGrunt();
      }
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

    if (showDebug) {
      fill(0, 0, 255);
      for (int i = 0; i < particles.length; i++) {
        String words = ""+i;
        text (words, particles[i].pos.x, particles[i].pos.y);
      }
    }
  }

  public void earnEmotion(float val) {
    emotionalLevel += val;
  }

  public void scroll(float scrollX) {
    for (int i=0; i<particles.length; i++) {
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

  public void collapse() {
    collapsed = true;
    
    for (int i=0; i<particles.length; i++){
     particles[i].vel = new PVector(0,0); 
     particles[i].frc = new PVector(0,0);
    }
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

    if (area.equals("graveyard")) {
      if (dullText.size() == 0) {
        refillDullGraveyardText();
      }
      if (emotionalText.size() == 0) {
        refillEmotionalGraveyardText();
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
    emotionalText.add("In school, you thought about how grades could possibly be the measure of a person. What matters more, a person\u2019s intelligence, or their emotions? You weep into your desk.");
    emotionalText.add("A bully beats you up after school. But what really hurts is thinking about the pain he must feel on the inside. Who, really, is suffering more?");
    emotionalText.add("When you are toilet trained, you think of how lonely your potty will feel, replaced and obsolete. The despair keeps you up for three nights.");
    emotionalText.add("Your mother tells you that she will love you forever. You hug her for hours, refusing to let go, tears in your eyes.");
  }


  public void refillDullChildhoodText() {
    dullText.clear();

    dullText.add("Your dad gives you a red ball for your birthday. You are happy that you can utilize it for exercise, as well as appreciating the difficulty in manufacturing a perfectly circular ball.");
    dullText.add("As a butterfly lands on a flower, you don't know whether to crush in order to rid the world of bugs or let it be devoured by some other insect. Bugs have no place in a civilized society.");
    dullText.add("The children around you are drawing pictures of animals and houses. You have constructed a spreadsheet of your allowance for 20 weeks.");
    dullText.add("For the first time, you experience pain as you trip and scrape your knee. You vow to never feel anything ever again.");
    dullText.add("While others have real and imaginary friends, you have something better. Solitude.");
    dullText.add("Your mother tells you that she will love you forever. No one can live forever, so she must be lying.");
  }


  public void refillEmotionalHospitalText() {
    emotionalText.clear();

    emotionalText.add("Hundreds of millions of years from now, the sun will die out, and there will be no more sunsets. You weep for the people of the future.");
    emotionalText.add("You wonder if everyone else can feel more than you can. This brings tears to your eyes.");
    emotionalText.add("A doctor is giving a lollipop to a sick little girl. The psychiatrist examines you after you begin crying hysterically in the lobby.");
  }

  public void refillDullHospitalText() {
    dullText.clear();

    dullText.add("You wonder where the property of all the dead patients go after they die. Does the hospital get it or do they return it to the family? These are important things to consider.");
    dullText.add("You see a mother weeping over the body of her child. If you had the strength, you would go up to her, put your hand on her shoulder, and tell her to compose herself in public.");
    dullText.add("You consider how many elderly people are in this hospital. You wish that money could be spent on something important, like building a strip mall.");
  }



  public void refillEmotionalChurchText() {
    emotionalText.clear();

    emotionalText.add("You begin to think of how a child must feel when they are separated from their mother. Children, you realize, have much to teach us.");
    emotionalText.add("You watch as the bride and groom before you exchange vows. You wail loudly at the beauty of two people happily together forever.");
    emotionalText.add("How many emotions can a person feel at a wedding, a funeral, or both at the same time? Part of you hopes to never find out, but part of you does.");
  }

  public void refillDullChurchText() {
    dullText.clear();

    dullText.add("Entering the vast cathedral, you give a sigh and think of how much money they could have been saved if it were a simple concrete building. Spacial efficiency is next to godliness, after all.");
    dullText.add("When you hear about your best friend getting a divorce, you become concerned. Only one of them will be able to claim their child as a dependent now.");
    dullText.add("The bride is slow dancing with her father, both crying into the other's shoulder. You are ashamed of their inability to keep their feelings to themselves.");
  }



  public void refillEmotionalFastFoodText() {
    emotionalText.clear();

    emotionalText.add("As you watch a boy play with a puppy, you become stricken with grief.");
    emotionalText.add("Each one of these hamburgers is a unique hamburger, enjoyed by their own unique person, in a unique location, all across the world. Your hamburger is now filled with tears.");
    emotionalText.add("You wonder how you will spend your first paycheck. Tears in your eyes, you decide it should be donated to a rainbow preservation society.");
    emotionalText.add("Someone likes you enough to hire you as a fry cook. This knowledge becomes overwhelming and induces a combination of elation and dread.");
    emotionalText.add("You consider the noble sacrifice a potato makes to become a french fry. You feel guilty for enjoying your lunch.");
  }

  public void refillDullFastFoodText() {
    dullText.clear();
    dullText.add("You see children laughing and playing without a care in the world as the warm sun shines down. You are sickened.");
    dullText.add("You decide to purchase a game because the graphics look next-gen. You\u2019re sure the gameplay will follow.");
    dullText.add("You receive your first paycheck and decide to spend it on a sheet of stamps. Forever stamps will only become more valuable over time.");
    dullText.add("All the food you eat came from some animal or plant, slaughtered just for you. Their sacrifice pleases you.");
    dullText.add("As you walk down the street, you see a couple holding hands. Their obstruction of the sidewalk makes you furious.");
  }


  public void refillEmotionalGraduationText() {
    emotionalText.clear();

    emotionalText.add("As you microwave your dinner for the evening, you see your rotating food as a representation for man's struggle to find good in the world. You are not sure you can eat such a perfect metaphor now.");
    emotionalText.add("After completing a game with cutting edge graphics, you feel overwhelmed with sadness. The artists of the game will never match the beauty of a rainbow.");
    emotionalText.add("You have earned your degree in psychology, the study of emotions. But you never studied just how touching a family hugging could be. You diploma becomes wet with tears.");
  }

  public void refillDullGraduationText() {
    dullText.clear();

    dullText.add("A bright rainbow shines over a grazing fawn on a hill. You shut the blinds to play another game of solitaire.");
    dullText.add("As you receive your degree in accounting, you think about your job prospects. There is no time for friendship in this world.");
    dullText.add("Your graduating class cheers for their accomplishments. You refuse to engage in any merriment, content to nod solemnly.");
  }

  public void refillEmotionalGraveyardText() {
    emotionalText.clear();

    emotionalText.add("Looking at all the tombstones, you realize the dead outnumber the living. You vow to have as many emotions as they did before you die.");
    emotionalText.add("Grass and colorful flowers grow above the dead in the ground. Flowers, you realize, have much to teach us.");
    emotionalText.add("If there were a zombie apocalypse, you\u2019re not sure you would fight back. After all, aren\u2019t zombies people too?");
  }

  public void refillDullGraveyardText() {
    dullText.clear();

    dullText.add("This graveyard would be the perfect spot for a Walmart. The waste disappoints you.");
    dullText.add("Funerals are very upsetting to you. All the work being missed right now, it fills you with disgust.");
    dullText.add("If you were immortal, you know exactly what you would do. Buy an island and live alone forever.");
  }
}



class SoundManager {

  AudioPlayer songDull;
  AudioPlayer songEmotional;

  AudioPlayer emotionGet;
  AudioPlayer grunt;
  //AudioPlayer startGame;
  
  AudioPlayer typewriter;
  AudioPlayer whoosh1;
  AudioPlayer whoosh2;
  AudioPlayer thunder;

  Minim minim;


  public void setup(Minim _minim) {
    minim = _minim;

    //music
    songDull = minim.loadFile("audio/dull.mp3", 2048);
    songDull.loop();

    songEmotional = minim.loadFile("audio/emotional.mp3", 2048);
    songEmotional.loop();

    songDull.pause();
    songEmotional.pause();

    //sound effects
    emotionGet = minim.loadFile("audio/emotionGetCut.mp3");

    grunt = minim.loadFile("audio/gruntSnap.mp3");
    
    typewriter = minim.loadFile("audio/typewriter2.wav");
    whoosh1 = minim.loadFile("audio/whoosh.aif");
    whoosh2 = minim.loadFile("audio/whoosh.aif");
    thunder = minim.loadFile("audio/thunder.wav");
    
  }

  public void update(boolean playEmotional) {
    
    if (!emotionGet.isPlaying() && emotionGet.position() != 0) {
      emotionGet = minim.loadFile("audio/emotionGetCut.mp3");
    } 
    if (!grunt.isPlaying() && grunt.position() != 0) {
      grunt = minim.loadFile("audio/gruntSnap.mp3");
    }


    if (playEmotional && !songEmotional.isPlaying()) {
      songDull.pause();
      songEmotional.play();
    }
    if (!playEmotional && !songDull.isPlaying() ) {
      songEmotional.pause();
      songDull.play();
    }
  }

  public void playGrunt() {
    grunt.play();
  }
  public void playemotionGet() {
    emotionGet.play();
  }
  
  public void playTypewriter() {
    typewriter.play();
  }
  
  public void playWhoosh1() {
   whoosh1.play(); 
  }
  
  public void playWhoosh2() {
   whoosh2.play(); 
  }
  
  public void playThunder() {
   thunder.play(); 
  }
  
}

class TextDisplayer {
  int NUM_AREAS = 6;
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

    emotional = loadFont("JosefinSans-Bold-48.vlw");
    textFont(emotional, 24);
    textAlign(CENTER);

    //get our text
    for (int i=0; i<NUM_AREAS; i++)  allText[i] = new PlaceText();
    allText[0].setup("childhood");
    allText[1].setup("hospital");
    allText[2].setup("church");
    allText[3].setup("fastFood");
    allText[4].setup("graduation");
    allText[5].setup("graveyard");

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

class TitleScene {
 float startTime; // when the game's loaded
 
 // peter molyneux is peter molyneux in...
 float[] delaysTop = new float[4];
 PImage[] imagesTop = new PImage[4];
// float delayFirstLine;
// float delaySecondLine;
// float delayThirdLine;
// float delayFourthLine;
 
 // main title delays
 float delayTitle; // when the title should slide in
 float delaySubtitle; // when the subtitle should slide in

 
 float startPosXTitle;
 float titleX;
 float endPosXTitle;
 
 float startPosXSubtitle;
 float subtitleX;
 float endPosXSubtitle;
 
 PImage title;
 PImage subtitle;
 PImage controls;
 
 boolean titleShouldMove;
 boolean subtitleShouldMove;
 
 // let's make the control buttons be all wiggly
 float[] xPositions = {47, 124, 209, 620, 698, 784};
 float[] yPositions = {410, 417, 413, 411, 408, 408};
 float[] newXPositions = new float[6];
 float[] newYPositions = new float[6];
 float[] offsets = new float[6];
 PImage[] letters = new PImage[6];
 float controlAlpha;
 
 SoundManager SM;
  
  
 public void setup(SoundManager _SM) {
   SM = _SM;
   
  startTime = millis();
  for (int i = 0; i < delaysTop.length; i++) {
    delaysTop[i] = i * 500 + 500;  
  }
  
  imagesTop[0] = loadImage("data/TitlePieces/PeterMolyneux.png");
  imagesTop[1] = loadImage("data/TitlePieces/Is.png");
  imagesTop[2] = loadImage("data/TitlePieces/PeterMolyneux.png");
  imagesTop[3] = loadImage("data/TitlePieces/In.png");
  
  delayTitle = delaysTop[delaysTop.length-1] + 1000;
  delaySubtitle = delayTitle + 1500;
   
  title = loadImage("data/TitlePieces/QWOPassages.png");
  subtitle = loadImage("data/TitlePieces/TheMarathonOfLife.png");
  controls = loadImage("data/TitlePieces/Controls.png");
  
  letters[0] = loadImage("data/TitlePieces/Q.png");
  letters[1] = loadImage("data/TitlePieces/W.png");
  letters[2] = loadImage("data/TitlePieces/E.png");
  letters[3] = loadImage("data/TitlePieces/I.png");
  letters[4] = loadImage("data/TitlePieces/O.png");
  letters[5] = loadImage("data/TitlePieces/P.png");
  
  startPosXTitle = titleX = -title.width;
  startPosXSubtitle = subtitleX = width + subtitle.width;
  
  endPosXTitle = width/2 - title.width/2;
  endPosXSubtitle = width/2 - subtitle.width/2;
  
  titleShouldMove = false;
  subtitleShouldMove = false;
  
  for (int i = 0; i < 6; i++) {
    float random = random(10);
    offsets[i] = random;
  }
  controlAlpha = 0;
 }

 public void updateTitle() {
   float xeno = 0.9f;
   
   if (millis() > startTime + delayTitle) {
     titleShouldMove = true; 
   }
   
   if (millis() > startTime + delaySubtitle) {
     subtitleShouldMove = true; 
   }
   
   if (titleShouldMove) {
     titleX = xeno*titleX + (1-xeno)*endPosXTitle;
   }
   
   if (subtitleShouldMove) {
     subtitleX = xeno*subtitleX + (1-xeno)*endPosXSubtitle;
   }
   
   for (int i = 0; i < xPositions.length; i++) {
     newXPositions[i] = xPositions[i] + 5.0f * cos(PApplet.parseFloat(millis())/500.0f + offsets[i]) * cos(PApplet.parseFloat(millis())/200.0f + offsets[i]);
     newYPositions[i] = yPositions[i] + 5.0f * sin(PApplet.parseFloat(millis())/400.0f + offsets[i]) * sin(PApplet.parseFloat(millis())/350.0f + offsets[i]);
   }
   
 }
 
 public void drawTitle() {
   for (int i = 0; i < delaysTop.length; i++) {
    if (millis() > startTime + delaysTop[i]) {
      if (!SM.typewriter.isPlaying() && SM.typewriter.position() < 100) {
       SM.typewriter.play(); 
      }
     image(imagesTop[i], width/2 - imagesTop[i].width/2, 38 + (i*30));
    } 
   }
   
   if (titleShouldMove && SM.whoosh1.position() < 100) {
    SM.whoosh1.play(); 
   }
   
   if (subtitleShouldMove && SM.whoosh2.position() < 100) {
    SM.whoosh2.play(); 
   }
   
   image(title, titleX, 194);
   image(subtitle, subtitleX, 339);

  smooth();
  if (millis() > startTime + delaySubtitle + 1000) {
    controlAlpha++;
    for (int i = 0; i < xPositions.length; i++) {
      tint(255, controlAlpha);
      image(letters[i], newXPositions[i], newYPositions[i]); 
//      tint(255, 255); // uncomment if you want a less thunderous title
    }
    image(controls, width/2 - controls.width/2, 450);
    
    if (SM.thunder.position() < 100) {
     SM.thunder.play(); 
    }
  }
 }
  
}
class WarningText {
  float xPos,      yPos;
  float startXPos, startYPos;
  float endXPos,   endYPos, endYPosSafe;
  
  String warningText;
  
  float startTime;
  float intervalUp;
  
  boolean shouldStartTimer;
  boolean startedTimer;
  
  boolean active;
  
  public void setup() {
    warningText = "COLLECT MORE EMOTIONS";

    startXPos = xPos = endXPos = width/2;
    
    startYPos = yPos = height + 30; // or something~
    endYPos = endYPosSafe = height - 20; // or something~
    
    intervalUp = 4000;
    
    active = false;
    
  }
  
  public void trigger(){
    active = true; 
    shouldStartTimer = false;
    startedTimer = false;
    endYPos = endYPosSafe;
    yPos = startYPos;
  }
  
  public void update() {
    if (active){
      float xeno = .9f;
      
      // go back down
      if (abs(endYPos - yPos) < 2 && !shouldStartTimer) {
        shouldStartTimer = true;
      }
      
      if (shouldStartTimer && !startedTimer) {
         startedTimer = true;
         startTime = millis();
      }
        
      if (startedTimer && millis() > startTime + intervalUp) {
        endYPos = startYPos;
        
        if ( abs(yPos-startYPos) < 0.5f){
         active = false; 
        }
      }
      
      yPos = xeno*yPos + (1-xeno)*endYPos;
    }
    
  }
  
  public void draw() {
    if (!active)return;
    
    if (millis()%1000 > 500) {
      fill(100, 0, 0);
    } else {
      fill(255, 0, 0); 
    }
    text(warningText, xPos, yPos);
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
