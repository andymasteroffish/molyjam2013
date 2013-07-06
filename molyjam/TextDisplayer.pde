class TextDisplayer {
  int NUM_TEXT = 1;

  String[] allText = new String[NUM_TEXT];
  String[] parsed;

  float[] triggerTimes;
  float[] alphas;
  boolean[] shouldFadeOut;

  boolean shouldFadeAllOut;

  float interval;

  void setup() {
    interval = 6000;

    loadText();
    parsed = parseString(allText[0]);
    triggerText();
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
          }
      } 
    }
    
    
    
  }


  void triggerText() {
    triggerTimes = new float[parsed.length]; // array holds trigger times for each line
    alphas = new float[parsed.length]; // holds alphas
//    shouldFadeIn = new boolean[parsed.length];

    int startTime = millis();
    for (int i = 0; i < triggerTimes.length; i++) {
      triggerTimes[i] = startTime + (i * interval); 
      alphas[i] = 0;
      println(triggerTimes[i]);
    }
  }

  // not really gonna change
  void loadText() {
    // where we/ezra will put the text to be loaded in
    allText[0] = "You begin to think of how a child must feel when they are separated from their mother. Children, you realize, have much to teach us.";
  }

  String[] parseString(String currentString) {
    return currentString.split("(?<=[.?!]) ");
  }
}

