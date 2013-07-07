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

// background
Background bg = new Background();
float playerTargetX;

// text displayer
TextDisplayer textDisplayer = new TextDisplayer();

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
}

void startGame(){
    //start with some time before the first emotion spawn
  emotionSpawnTimer = emotionMaxNextSpawnTime;
  
  bg.reset();
  guy.resetPlayer();
  
  gameState = "game";
}

void update() {
  float deltaTime = ((float)millis()-prevFrameTime)/1000.0;
  prevFrameTime = millis();

  if (gameState.equals("title")){
    //fucking nothing right now goddamn
  }else if (gameState.equals("game")){
    guy.resetForces();
  
    guy.update(deltaTime);
  
    //check what kind of text we should be showing
    boolean showEmotionalText = guy.emotionalLevel > 50;
    textDisplayer.updateShowEmotionalText( showEmotionalText );
  
    //update emotion pick ups
    for (int i=emotions.size()-1; i>=0; i--) {
      Emotion thisEmotion = emotions.get(i);
      thisEmotion.update(deltaTime, guy);
  
      if (thisEmotion.killMe) {
        emotions.remove(i);
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
      scroll(-playerDistFromCenter*0.1);  //xeno to make it smoother
    }
  }
  
}

void draw() {
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
  }
}

void keyPressed() {
  
  if (gameState.equals("title")){
    println("fuck it");
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
  newEmotion.setup();
  emotions.add(newEmotion);
}

