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
  
  void setup() {
    startXPos = xPos = endXPos = width/2;
    
    startYPos = yPos = height + 30; // or something~
    endYPos = endYPosSafe = height - 20; // or something~
    
    intervalUp = 4000;
    
    active = false;
    
  }
  
  void trigger(){
    warningText = "COLLECT MORE EMOTIONS";
    active = true; 
    shouldStartTimer = false;
    startedTimer = false;
    endYPos = endYPosSafe;
    yPos = startYPos;
  }
  
  void triggerSpecial(String newText){
    trigger();
    
    warningText = newText;
  }
  
  void update() {
    if (active){
      float xeno = .9;
      
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
        
        if ( abs(yPos-startYPos) < 0.5){
         active = false; 
        }
      }
      
      yPos = xeno*yPos + (1-xeno)*endYPos;
    }
    
  }
  
  void draw() {
    if (!active)return;
    
    if (millis()%1000 > 500) {
      fill(100, 0, 0);
    } else {
      fill(255, 0, 0); 
    }
    text(warningText, xPos, yPos);
  }
}
