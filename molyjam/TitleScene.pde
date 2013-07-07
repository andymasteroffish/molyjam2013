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
   
 }
 
 void drawTitle() {
   image(title, titleX, 100);
   image(subtitle, subtitleX, 300);
 }
  
}
