class EndScene {

  PImage endBGPic;
  PImage[] endPics = new PImage[3];
  PVector[] endPicsOffsets = new PVector[3];
  int curEndPic;
  PImage bonusPic;


  //fade in/out timer for endgame + bonus screen
  float startMessageTimer;
  float intervalTimer = 10000;

  float fadeAlpha;
  float fadeTime = 7;
  float pauseTime = 10;
  float fadeTimer;
  boolean fadeIn;

  boolean showingBonus;


  void setup() {

    endBGPic = loadImage("data/EndPieces/EndBG.png");
    endPics[0] = loadImage("data/EndPieces/EndEmotional.png");
    endPics[1] = loadImage("data/EndPieces/EndSemiEmotional.png");
    endPics[2] = loadImage("data/EndPieces/EndUnemotional.png");

    endPicsOffsets[0] = new PVector(width/2-endPics[0].width/2, height/2-endPics[0].height/2);
    endPicsOffsets[1] = new PVector(width/2-endPics[1].width/2, height/2-endPics[1].height/2);
    endPicsOffsets[2] = new PVector(width/2-endPics[2].width/2, height/2-endPics[2].height/2);

    bonusPic = loadImage("data/Title2_full.png");
  }

  void start(float emotionalLevel) {
    curEndPic = 0; 

    fadeAlpha = 0;
    fadeTimer = 0;
    fadeIn = true;

    showingBonus = false;
  }


  void update(float deltaTime) {

    fadeTimer+=deltaTime;

    if (fadeIn) {
      fadeAlpha = map(fadeTimer, 0, fadeTime, 0, 255);
      if (fadeTimer >= fadeTime + pauseTime) {
        fadeIn = false;
        fadeTimer = 0;
        showingBonus = true;
      }
    }
    else {
      fadeAlpha = map(fadeTimer, 0, fadeTime, 255, 0);
    }
  }


  void draw() {

    if (showingBonus) {
      tint(255, 255);

      image(bonusPic, 0, 0);
    }


    tint(255, fadeAlpha);
    image(endBGPic, 0, 0);
    image(endPics[curEndPic], endPicsOffsets[curEndPic].x, endPicsOffsets[curEndPic].y);

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

