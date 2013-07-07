class TextDisplayer {
  int NUM_AREAS = 1;
  PFont emotional;
  PFont unemotional;

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
  
  float delayBetweenPassages;

  void setup() {
    interval = 6000;
    delayBetweenPassages = 5000;
    

    //get our text
    for (int i=0; i<NUM_AREAS; i++)  allText[i] = new PlaceText();
    allText[0].setup("field");

    selectString();
    parsed = parseString(currentLine);
    triggerText(parsed);
    shouldShowAnyText = true;
    
    showEmotionalText = true;
    
    emotional = loadFont("JosefinSans-24.vlw");
    textFont(emotional, 24);
    
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
            text(parsed.get(i), width/2 - textWidth(parsed.get(i))/2, i*50 + 50);
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
          text(parsed.get(i), width/2 - textWidth(parsed.get(i))/2, i*50 + 50);
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
    delayBetweenPassages = int(random(4000, 7000));
  }

  void triggerText(ArrayList<String> parsed) {
    triggerTimes = new ArrayList<Float>();
    alphas = new ArrayList<Float>();

    int startTime = millis();
    for (int i = 0; i < parsed.size(); i++) {
      triggerTimes.add(startTime + delayBetweenPassages + (i * interval)); 
      alphas.add(float(0));
    }
  }

  void selectString() {
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

