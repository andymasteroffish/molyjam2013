class TextDisplayer {
  int NUM_TEXT = 1;
  
  String[] allText = new String[NUM_TEXT];
  
  void setup() {
    loadText();
    println(parseString(allText[0]));
  }

  void updateText(String location) {
    

  }

  void draw() {
  }

  void loadText() {
    // where we/ezra will put the text to be loaded in
    allText[0] = "You begin to think of how a child must feel when they are separated from their mother. Children, you realize, have much to teach us.";  
  }
  
  String[] parseString(String currentString) {
    String parsed[] = currentString.split("(?<=[.?!]) ");
    
    return parsed;
  }
}

