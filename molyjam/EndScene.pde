class EndScene {

  PImage[] endPics = new PImage[3];
  int curEndPic;
  PImage bonusPic;


  //fade in/out timer for endgame + bonus screen
  float startMessageTimer;
  float intervalTimer = 10000;

  float fadeAlpha;
  float fadeTime = 4;
  float pauseTime = 3;
  float fadeTimer;
  boolean fadeIn;

  boolean showingBonus;


  void setup() {

    endPics[0] = loadImage("data/endScrenTemp.png");
    endPics[1] = loadImage("data/endScrenTemp.png");
    endPics[2] = loadImage("data/endScrenTemp.png");

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

    image(endPics[curEndPic], 0, 0);

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

