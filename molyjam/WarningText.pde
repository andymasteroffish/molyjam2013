class WarningText {
  float xPos,      yPos;
  float startXPos, startYPos;
  float endXPos,   endYPos;
  
  String warningText;
  
  float startTime;
  float intervalUp;
  
  boolean shouldStartTimer;
  boolean startedTimer;
  
  void setup() {
    warningText = "COLLECT MORE EMOTIONS";

    startXPos = xPos = endXPos = width/2;
    
    startYPos = yPos = height + 30; // or something~
    endYPos = height - 30; // or something~
    
    intervalUp = 4000;
    
    
    
  }
  
  void update() {
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
      println("go");
      endYPos = startYPos;
    }
    
    yPos = xeno*yPos + (1-xeno)*endYPos;
    
  }
  
  void draw() {
    if (millis()%1000 > 500) {
      fill(100, 0, 0);
    } else {
      fill(255, 0, 0); 
    }
    text(warningText, xPos, yPos);
  }
}
