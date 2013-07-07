class Background {
  PVector pos;

  PImage[] backgroundPics = new PImage[5];
  String[] places = new String[5];

  //PImage myBackground;  //kill me

  //the backgrounds for this round
  PImage[] theseBackgrounds = new PImage[6];
  String[] thesePlaces = new String[6];



  String backgroundsUsed = "";

  PImage output;

  int currentIndex;

  float startPos;


  // 700 x 600

  void setup() {
    startPos = -200;
    

    loadImages();

    
  }
  
  void reset(){
    pos = new PVector(startPos, 0);
    
    for (int i=0; i<theseBackgrounds.length; i++) {
      int randomChoice = int(random(backgroundPics.length));
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
    backgroundPics[0] = loadImage("brickwall.png");
    backgroundPics[1] = loadImage("fastfood.png");
    backgroundPics[2] = loadImage("graduation.png");
    backgroundPics[3] = loadImage("church_interior.png");
    backgroundPics[4] = loadImage("hospital.png");

    places[0] = "childhood";
    places[1] = "fastFood";
    places[2] = "graduation";
    places[3] = "church";
    places[4] = "hospital";
    
  }

}

