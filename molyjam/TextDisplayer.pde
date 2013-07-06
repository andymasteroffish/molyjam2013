class TextDisplayer {
  int NUM_AREAS = 1;

  PlaceText[] allText = new PlaceText[NUM_AREAS];
  String currentLine;
  
  //sorting it all by place/emotion
  int placeNum;
  boolean showEmotionalText = true;
  
  
  ArrayList<String> parsed = new ArrayList<String>();
  ArrayList<Float> triggerTimes;
  ArrayList<Float> alphas;

  boolean shouldFadeAllOut;
  boolean shouldShowAnyText;

  float interval;

  void setup() {
    interval = 6000;

    //get our text
    for (int i=0; i<NUM_AREAS; i++)  allText[i] = new PlaceText();
    allText[0].setup("field");

    selectString();
    parsed = parseString(currentLine);
    triggerText(parsed);
    shouldShowAnyText = true;
    
    showEmotionalText = true;
    
  }

  void updateText(String location) {
    placeNum = 0;  //default in case nothing else triggers
    
    if (location == "field")  placeNum = 0;
  }
  void updateShowEmotionalText(boolean _showEmotionalText){
    showEmotionalText = _showEmotionalText;
  }

  void draw() {
    if (shouldShowAnyText) {
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
  }

  void reset() {
    parsed.clear();
    triggerTimes.clear();
    alphas.clear();
    shouldFadeAllOut = false;

    selectString();
    parsed = parseString(currentLine);
    triggerText(parsed);
    shouldShowAnyText = true;
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
//  void loadText() {
//    // where we/ezra will put the text to be loaded in
//    //allText[0] = "You begin to think of how a child must feel when they are separated from their mother. Children, you realize, have much to teach us.";
//    //allText[1] = "What if everyone can feel more than I can? This brings tears to your eyes.";
//    
//    for (int i=0; i<NUM_AREAS; i++){
//      
//    }
//  }

  void selectString() {
    //int random = int(random(allText.length));
    
    if (showEmotionalText){
      currentLine = allText[placeNum].getEmotionalText();
      println("get it exciting");
    }else{
      currentLine = allText[placeNum].getDullText();
      println("get it dull");
    }
    
    
  }

  ArrayList<String> parseString(String currentLine) {
    String tempLine[] = currentLine.split("(?<=[.?!]) ");
    for (int i = 0; i < tempLine.length; i++) {
      parsed.add(tempLine[i]);
    }
    return parsed;
  }
}

