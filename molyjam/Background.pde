class Background {
  PVector pos;
  PVector increment;
  
  PImage[] backgrounds = new PImage[4];
  String[] places = new String[4];
  PImage myBackground;
  
  String backgroundsUsed = "";
  
  PImage output;
  
  int currentIndex;
  
  
  
  // 700 x 600
  
  void setup() {
    increment = new PVector(2, 0);
    pos = new PVector(0, 0);
    
    loadImages();

  }
  
  void updateBackground() {
    if (mouseX > width/2) { 
      PVector tempValue = pos;
      tempValue.add(increment);
      pos = tempValue;
    } else {
      PVector tempValue = pos;
      tempValue.sub(increment);
      pos = tempValue;
    }
    
    
    if (pos.x + myBackground.width < width) {
      newBackground();
    }
  }
  
  void draw() {
    image(myBackground, pos.x, pos.y);
    //image(myBackground, 10, 10, myBackground.width/8, myBackground.height/8); 
  }
  
  
  void loadImages() {
    backgrounds[0] = loadImage("0.jpeg");
    backgrounds[1] = loadImage("1.jpeg");
    backgrounds[2] = loadImage("2.jpeg");
    backgrounds[3] = loadImage("3.jpeg");
    
    places[0] = "yawning";
    places[1] = "exasperated";
    places[2] = "alert";
    places[3] = "aghast";
    
    myBackground = backgrounds[0];
    
    int totalWidth = 0;
    for (int i = 0; i < backgrounds.length; i++) {
       totalWidth+= backgrounds[i].width;
    }
    
  }
  
  void newBackground() {
    int randomChoice = int(random(backgrounds.length-1));
    PImage newBG = backgrounds[randomChoice];
    
    PGraphics temp = createGraphics(myBackground.width + newBG.width, 600, JAVA2D);
    temp.image(myBackground, 0, 0);
    temp.image(newBG, myBackground.width, 0);
    myBackground = temp.get();
    
    textDisplayer.updateText(places[randomChoice]);
  }
  
}
