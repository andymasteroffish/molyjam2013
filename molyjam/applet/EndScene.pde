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


  void setup() {

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

  void start(float emotionalLevel) {
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


  void update(float deltaTime) {

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


  void draw() {

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



  void fade(String whichWay) {
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

