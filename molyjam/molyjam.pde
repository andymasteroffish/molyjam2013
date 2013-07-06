
//timing
float prevFrameTime;

//player
Person guy = new Person();

//emotions
ArrayList<Emotion> emotions = new ArrayList<Emotion>();

// background
Background bg = new Background();
float playerTargetX;

// text displayer
TextDisplayer textDisplayer = new TextDisplayer();

int groundY;

boolean showHidden;


void setup() {

  size(900, 500);

  frameRate(60);
  smooth();

  groundY = height - 50;

  guy.setup(groundY);
  playerTargetX = width/2;


  showHidden = false;

  prevFrameTime = millis();
  
  bg.setup();
  textDisplayer.setup();
}

void update() {
  float deltaTime = ((float)millis()-prevFrameTime)/1000.0;
  prevFrameTime = millis();

  guy.resetForces();

  guy.update(deltaTime);
  
  //update emotion pick ups
  for (int i=emotions.size()-1; i>=0; i--) {
    Emotion thisEmotion = emotions.get(i);
    thisEmotion.update(deltaTime, guy);
    
    if (thisEmotion.killMe){
      emotions.remove(i); 
    }
  }
  
  //bg.updateBackground();
  
  //check for scrolling (on his pelvis)
  float playerDistFromCenter = guy.particles[3].pos.x - playerTargetX;
  if (playerDistFromCenter > 0 || bg.pos.x <= bg.startPos){
    scroll(-playerDistFromCenter*0.1);  //xeno to make it smoother
  }
}

void draw() {
  update();

  background(255);
  
  bg.draw(playerTargetX);

  guy.draw(showHidden);
  
  textDisplayer.draw();

  stroke(0);
  line(0, groundY, width, groundY);

  //draw the emotion pick ups
  for (int i=0; i<emotions.size(); i++) {
    Emotion thisEmotion = emotions.get(i);
    thisEmotion.draw();
  }
}

void keyPressed() {
  guy.checkKeyDown(key);

  if (key == 'd') {
    showHidden = !showHidden;
  }

  if (keyCode == ENTER) {
    guy.setup(groundY);
  }

  //tetsing EMOTIONS
  if (key == '5') {
    spawnEmotion();
  }
}

void keyReleased() {
  guy.checkKeyUp(key);
}


void scroll(float scrollX){
  guy.scroll(scrollX);
  bg.scroll(scrollX);
  for (int i=0; i<emotions.size(); i++) {
    Emotion thisEmotion = emotions.get(i);
    thisEmotion.scroll(scrollX);
  }
}

void spawnEmotion() {
  Emotion newEmotion = new Emotion();
  newEmotion.setup();
  emotions.add(newEmotion);
}

