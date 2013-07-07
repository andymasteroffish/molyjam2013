class TextDisplayer {
  int NUM_AREAS = 5;
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

    emotional = loadFont("JosefinSans-24.vlw");
    textFont(emotional, 24);
    textAlign(CENTER);

    //get our text
    for (int i=0; i<NUM_AREAS; i++)  allText[i] = new PlaceText();
    allText[0].setup("childhood");
    allText[1].setup("hospital");
    allText[2].setup("church");
    allText[3].setup("fastFood");
    allText[4].setup("graduation");

    selectString();
    parsed = parseString(currentLine);
    triggerText(parsed);
    shouldShowAnyText = true;

    showEmotionalText = true;
  }

  void updateText(String location) {
    placeNum = 0;  //default in case nothing else triggers
    //println("I'm in "+location);
    for (int i=0; i<allText.length; i++) {
      if (allText[i].area.equals(location)) {  
        placeNum = i;
        //println("that's number "+i);
      }
    }
  }

  void updateShowEmotionalText(boolean _showEmotionalText) {
    showEmotionalText = _showEmotionalText;
  }

  void draw() {
    if (shouldShowAnyText) {      
      // fade out if last trigger happened + interval
      if (millis() > triggerTimes.get(triggerTimes.size() - 1) + interval) {
        shouldFadeAllOut = true;
      }

      int curY = 50;
      int spacePerLine = 33;
      if (!shouldFadeAllOut) { // if we're not fading all of them out
        for (int i = 0; i < triggerTimes.size(); i++) {
          if (millis() > triggerTimes.get(i)) {
            fill(0, alphas.get(i));
            text(parsed.get(i), width/2, curY);
            if (alphas.get(i) < 255) {
              float tempAlpha = alphas.get(i);
              tempAlpha++;
              alphas.set(i, tempAlpha);
            }
            curY += spacePerLine;
            //add the spacing again if it has a line break
            if (parsed.get(i).contains("\n")) {
              curY += spacePerLine;
            }
          }
        }
      } 
      else {
        for (int i = 0; i < triggerTimes.size(); i++) {
          fill(0, alphas.get(i));
          text(parsed.get(i), width/2, curY);
          curY += spacePerLine;
          //add the spacing again if it has a line break
          if (parsed.get(i).contains("\n")) {
            curY += spacePerLine;
          }
          if (alphas.get(i) > 0) {
            float tempAlpha = alphas.get(i);
            tempAlpha--;
            alphas.set(i, tempAlpha);
          } 
          else {
            reset();
            break;
          }

        }
      }
    }
  }

  void reset() {
    println("reset snoopy balls");
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
    if (showEmotionalText) {
      println("get it exciting");
      currentLine = allText[placeNum].getEmotionalText();
    }
    else {
      println("get it dull");
      currentLine = allText[placeNum].getDullText();
    }
  }

  ArrayList<String> parseString(String currentLine) {

    ArrayList<String> broken = new ArrayList<String>();

    String tempLine[] = currentLine.split("(?<=[.?!]) ");
    for (int i = 0; i < tempLine.length; i++) {
      broken.add(tempLine[i]);
    }

    //go through each parsed line and make sure it will fit on screen
    ArrayList<String> trimmed = new ArrayList<String>();
    for (int i=0; i<broken.size(); i++) {
      String thisLine = broken.get(i);
      String withBreaks = addLineBreaks(thisLine);

      trimmed.add(withBreaks);
    }

    return trimmed;
  }

  String addLineBreaks(String rawLine) {

    String cleanLine = ""; 

    int curChar = -1;


    while (curChar < rawLine.length ()-1) {

      boolean wentOver = false;
      while (curChar < rawLine.length ()-1 && !wentOver) {

        curChar++;
        cleanLine += rawLine.charAt(curChar);


        //println("cur width "+textWidth(cleanLine));
        if (textWidth(cleanLine) > width*0.98) {
          wentOver = true;
        }
      }

      if (wentOver) {
        //go back until we hit a space
        int spaceChar = curChar;
        if (wentOver) {
          while (cleanLine.charAt (spaceChar) != ' ') {
            //println("space char "+spaceChar);
            spaceChar--;
          } 
          cleanLine = cleanLine.substring(0, spaceChar) +"\n" + cleanLine.substring(spaceChar+1, cleanLine.length() );
        }
      }
    }
    
    return cleanLine;
 }
 
 
}

