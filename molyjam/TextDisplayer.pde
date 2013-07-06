class TextDisplayer {
  int NUM_TEXT = 1;
  
  String[] allText = new String[NUM_TEXT];
  String[] parsed;
  
  float[] triggerTimes;
  float[] alphas;
  
  float interval;
  
  void setup() {
    interval = 1000;
    
    loadText();
    parsed = parseString(allText[0]);
    triggerText();
  }

  void updateText(String location) {
    
  }

  void draw() {
    for (int i = 0; i < triggerTimes.length; i++) {
      if (millis() > triggerTimes[i]) {
        if (i < triggerTimes.length) {
          fill(0, alphas[i]);
          text(parsed[i], 0, i*50 + 50);
          alphas[i]++;
        }
      } 
    }
    
  }


  void triggerText() {
    triggerTimes = new float[parsed.length]; // array holds trigger times for each line
    alphas = new float[parsed.length]; // forget this last value
    
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

