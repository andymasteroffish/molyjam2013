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

import ddf.minim.*;

//timing
float prevFrameTime;

//player
Person guy = new Person();

//emotions
ArrayList<Emotion> emotions = new ArrayList<Emotion>();
float emotionSpawnTimer;
float emotionMaxNextSpawnTime = 10;
float emotionMinNextSpawnTime = 3;
PImage[] emotionPics = new PImage[1];

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



void setup() {

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
  titleScene.setup();

  endScene.setup();
  
  //set emotion pick ups
  for (int i=0; i<emotionPics.length; i++){
    String picName = "data/Pickups/pickup"+i+".png";
    emotionPics[i] = loadImage(picName);
  }
}

void startGame() {
  //start with some time before the first emotion spawn
  emotionSpawnTimer = emotionMaxNextSpawnTime;

  bg.reset();
  guy.resetPlayer();

  tint(255, 255);

  gameTimer = 0;

  gameState = "game";
}

void endGame() {
  gameState = "end";
  endScene.start(guy.emotionalLevel);
  guy.collapse();
}

void update() {
  float deltaTime = ((float)millis()-prevFrameTime)/1000.0;
  prevFrameTime = millis();

  if (gameState.equals("title")) {
    //fucking nothing right now goddamn
    titleScene.updateTitle();
  }
  else if (gameState.equals("game") || gameState.equals("end")) {
    guy.resetForces();

    guy.update(deltaTime, gameState.equals("game"));

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

    //chekc sounds
    SM.update(showEmotionalText);

    //bg.updateBackground();

    //check for scrolling (on his pelvis)
    float playerDistFromCenter = guy.particles[3].pos.x - playerTargetX;
    if ( (playerDistFromCenter > 0 || bg.pos.x <= bg.startPos) && ( playerDistFromCenter < 0 || bg.pos.x >= bg.endPos)) {
      scroll(-playerDistFromCenter*0.1);  //xeno to make it smoother
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

void draw() {
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
  } 

  if (gameState.equals("end")) {
    endScene.draw();
  }
}

void keyPressed() {

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

void keyReleased() {
  guy.checkKeyUp(key);
}


void scroll(float scrollX) {
  guy.scroll(scrollX);
  bg.scroll(scrollX);
  for (int i=0; i<emotions.size(); i++) {
    Emotion thisEmotion = emotions.get(i);
    thisEmotion.scroll(scrollX);
  }
}

void spawnEmotion() {
  Emotion newEmotion = new Emotion();
  int randNum = (int)random(emotionPics.length);
  newEmotion.setup(emotionPics[randNum]);
  emotions.add(newEmotion);
}

