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
  
  
 void setup() {
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

 void updateTitle() {
   float xeno = 0.9;
   
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
     newXPositions[i] = xPositions[i] + 5.0 * cos(float(millis())/500.0 + offsets[i]) * cos(float(millis())/200.0 + offsets[i]);
     newYPositions[i] = yPositions[i] + 5.0 * sin(float(millis())/400.0 + offsets[i]) * sin(float(millis())/350.0 + offsets[i]);
   }
   
 }
 
 void drawTitle() {
   for (int i = 0; i < delaysTop.length; i++) {
    if (millis() > startTime + delaysTop[i]) {
     image(imagesTop[i], width/2 - imagesTop[i].width/2, 38 + (i*30));
    } 
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
  }
 }
  
}
