class TitleScene {
 float startTime; // when the game's loaded
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
 
 boolean titleShouldMove;
 boolean subtitleShouldMove;
 
 // let's make the control buttons be all wiggly
 float[] xPositions = new float[6];
 float[] yPositions = new float[6];
 
 float[] newXPositions = new float[6];
 float[] newYPositions = new float[6];
 
 float[] offsets = new float[6];
  
 void setup() {
  startTime = millis(); // since it never REALLY starts on millis()
  delayTitle = 1000;
  delaySubtitle = 2000;
   
  title = loadImage("1 copy.jpeg");
  subtitle = loadImage("1 copy.jpeg");
  
  startPosXTitle = titleX = -title.width;
  startPosXSubtitle = subtitleX = width + subtitle.width;
  
  endPosXTitle = width/2 - title.width/2;
  endPosXSubtitle = width/2 - subtitle.width/2;
  
  titleShouldMove = false;
  subtitleShouldMove = false;
  
  for (int i = 0; i < 6; i++) {
    float random = random(10);
    offsets[i] = random;
    xPositions[i] = i * 50 + 50;
    yPositions[i] = 400;
  }
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
     newYPositions[i] = yPositions[i] + 5.0 * sin(float(millis())/500.0 + offsets[i]) * sin(float(millis())/350.0 + offsets[i]);
   }
   
 }
 
 void drawTitle() {
   image(title, titleX, 100);
   image(subtitle, subtitleX, 300);

  smooth();
  for (int i = 0; i < xPositions.length; i++) {
    image(title, newXPositions[i], newYPositions[i]); 
  }
 }
  
}
