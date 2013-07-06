class TextDisplayer {
  int NUM_TEXT = 2;

  String[] allText = new String[NUM_TEXT];
  String currentLine;
  String[] parsed;

  float[] triggerTimes;
  float[] alphas;
  boolean[] shouldFadeOut;

  boolean shouldFadeAllOut;

  float interval;

  void setup() {
    interval = 6000;

    loadText();
    selectString();
    parsed = parseString(currentLine);
    triggerText(parsed);
  }

  void updateText(String location) {
  }

  void draw() {
    
    if (millis() > triggerTimes[triggerTimes.length - 1] + interval) {
     shouldFadeAllOut = true; 
    }

    if (!shouldFadeAllOut) { // if we're not fading all of them out
      for (int i = 0; i < triggerTimes.length; i++) { // for each trigger time
        if (millis() > triggerTimes[i]) { // see if we're bigger
          //shouldFadeIn[i] = true; // and if so, fade in
          
          fill(0, alphas[i]);
          text(parsed[i], 0, i*50 + 50);
          if (alphas[i] < 255) {
            alphas[i]++;
          }
        }
      }
    } else {
      for (int i = 0; i < triggerTimes.length; i++) {
        //shouldFadeIn[i] = false;
        
          fill(0, alphas[i]);
          text(parsed[i], 0, i*50 + 50);
          if (alphas[i] > 0) {
            alphas[i]--;
          } else {
            reset();
          }
      }
    }
    
    
    
  }

  void selectString() {
    int random = int(random(allText.length));
    currentLine = allText[random];
  }

  void triggerText(String[] parsed) {
    triggerTimes = new float[parsed.length]; // array holds trigger times for each line
    alphas = new float[parsed.length]; // holds alphas

    int startTime = millis();
    for (int i = 0; i < triggerTimes.length; i++) {
      triggerTimes[i] = startTime + (i * interval); 
      alphas[i] = 0;
    }
  }
  
  void reset() {
    
  }

  // not really gonna change
  void loadText() {
    // where we/ezra will put the text to be loaded in
    allText[0] = "You begin to think of how a child must feel when they are separated from their mother. Children, you realize, have much to teach us.";
    allText[1] = "What if everyone can feel more than I can? This brings tears to your eyes.";
  }

  String[] parseString(String currentString) {
    return currentString.split("(?<=[.?!]) ");
  }
}

