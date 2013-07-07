
class Background {
  PVector pos;

  PImage[] backgroundPics = new PImage[6];
  String[] places = new String[6];

  //PImage myBackground;  //kill me

  //the backgrounds for this round
//  PImage[] theseBackgrounds = new PImage[6];
//  String[] thesePlaces = new String[6];



  String backgroundsUsed = "";

  PImage output;

  int currentIndex;

  float startPos;
  float endPos;


  // 700 x 600

  void setup() {
    
    loadImages();
    
    startPos = -15;
    
    endPos = -backgroundPics[0].width* (backgroundPics.length-1) + 30;
    

    

    
  }
  
  void reset(){
    pos = new PVector(startPos, 0);
    
  }


  void scroll(float scrollX) {
    pos.x += scrollX;
  }

  void draw(float playerTargetX) {
    //image(myBackground, pos.x, pos.y);
    //image(myBackground, 10, 10, myBackground.width/8, myBackground.height/8);
    
    //draw the ones on screen
    int curWidth = 0;
    for (int i=0; i<backgroundPics.length; i++){
      int xPos = (int)pos.x + curWidth;
      
      if (xPos > -backgroundPics[i].width && xPos < width){
        image(backgroundPics[i], xPos, pos.y); 
      }
      
      
      //if this is the one the player is on, give that title to the text display
      if (xPos < playerTargetX && xPos+backgroundPics[i].width > playerTargetX){
        textDisplayer.updateText(places[i]);
      }
      
      
      curWidth += backgroundPics[i].width;
      
    }
  }


  void loadImages() {
    backgroundPics[0] = loadImage("brickwall.png");
    backgroundPics[1] = loadImage("fastfood.png");
    backgroundPics[2] = loadImage("graduation.png");
    backgroundPics[3] = loadImage("church_interior.png");
    backgroundPics[4] = loadImage("hospital.png");
backgroundPics[5] = loadImage("graveyard.png");

    places[0] = "childhood";
    places[1] = "fastFood";
    places[2] = "graduation";
    places[3] = "church";
    places[4] = "hospital";
    places[5] = "graveyard";
    
  }

}
