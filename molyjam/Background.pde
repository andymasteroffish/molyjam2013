class Background {
  PVector pos;
  PVector increment;

  PImage[] backgroundPics = new PImage[4];
  String[] places = new String[4];

  //PImage myBackground;  //kill me

  //the backgrounds for this round
  PImage[] theseBackgrounds = new PImage[6];
  String[] thesePlaces = new String[6];



  String backgroundsUsed = "";

  PImage output;

  int currentIndex;

  float startPos = -200;


  // 700 x 600

  void setup() {
    increment = new PVector(2, 0);
    pos = new PVector(startPos, 0);

    loadImages();

    for (int i=0; i<theseBackgrounds.length; i++) {
      int randomChoice = int(random(backgroundPics.length-1));
      theseBackgrounds[i] = backgroundPics[randomChoice];
      thesePlaces[i] = places[randomChoice];
    }
  }


  void scroll(float scrollX) {
    pos.x += scrollX;
  }

  void draw(float playerTargetX) {
    //image(myBackground, pos.x, pos.y);
    //image(myBackground, 10, 10, myBackground.width/8, myBackground.height/8);
    
    //draw the ones on screen
    int curWidth = 0;
    for (int i=0; i<theseBackgrounds.length; i++){
      int xPos = (int)pos.x + curWidth;
      
      if (xPos > -theseBackgrounds[i].width && xPos < width){
        image(theseBackgrounds[i], xPos, pos.y); 
      }
      
      
      //if this is the one the player is on, give that title to the text display
      if (xPos < playerTargetX && xPos+theseBackgrounds[i].width > playerTargetX){
        textDisplayer.updateText(thesePlaces[i]);
      }
      
      
      curWidth += theseBackgrounds[i].width;
      
    }
  }


  void loadImages() {
    backgroundPics[0] = loadImage("0.jpeg");
    backgroundPics[1] = loadImage("1.jpeg");
    backgroundPics[2] = loadImage("2.jpeg");
    backgroundPics[3] = loadImage("3.jpeg");

    places[0] = "yawning";
    places[1] = "exasperated";
    places[2] = "alert";
    places[3] = "aghast";
  }

}

