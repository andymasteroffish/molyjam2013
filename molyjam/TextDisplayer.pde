class TextDisplayer {
  int NUM_TEXT = 2;

  String[] allText = new String[NUM_TEXT];
  String currentLine;

  ArrayList<String> parsed = new ArrayList<String>();
  ArrayList<Float> triggerTimes;
  ArrayList<Float> alphas;

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
    // fade out if last trigger happened + interval
    if (millis() > triggerTimes.get(triggerTimes.size() - 1) + interval) {
      shouldFadeAllOut = true;
    }

    if (!shouldFadeAllOut) { // if we're not fading all of them out
      for (int i = 0; i < triggerTimes.size(); i++) {
        if (millis() > triggerTimes.get(i)) {
          fill(0, alphas.get(i));
          text(parsed.get(i), 0, i*50 + 50);
          if (alphas.get(i) < 255) {
            float tempAlpha = alphas.get(i);
            tempAlpha++;
            alphas.set(i, tempAlpha);
          }
        }
      }
    } 
    else {
      for (int i = 0; i < triggerTimes.size(); i++) {
        fill(0, alphas.get(i));
        text(parsed.get(i), 0, i*50 + 50);
        if (alphas.get(i) > 0) {
          float tempAlpha = alphas.get(i);
          tempAlpha--;
          alphas.set(i, tempAlpha);
        } 
        else {
          reset();
        }
      }
    }
  }

  void reset() {
    parsed.clear();
    triggerTimes.clear();
    alphas.clear();
    
    selectString();
    parsed = parseString(currentLine);
    triggerText(parsed)
  }

  void triggerText(ArrayList<String> parsed) {
    triggerTimes = new ArrayList<Float>();
    alphas = new ArrayList<Float>();

    int startTime = millis();
    for (int i = 0; i < parsed.size(); i++) {
      triggerTimes.add(startTime + (i * interval)); 
      alphas.add(float(0));
    }
  }

  // not really gonna change
  void loadText() {
    // where we/ezra will put the text to be loaded in
    allText[0] = "You begin to think of how a child must feel when they are separated from their mother. Children, you realize, have much to teach us.";
    allText[1] = "What if everyone can feel more than I can? This brings tears to your eyes.";
  }

  void selectString() {
    int random = int(random(allText.length));
    currentLine = allText[random];
  }

  ArrayList<String> parseString(String currentLine) {
    String tempLine[] = currentLine.split("(?<=[.?!]) ");
    for (int i = 0; i < tempLine.length; i++) {
      parsed.add(tempLine[i]);
    }
    return parsed;
  }
}

