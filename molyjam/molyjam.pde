
//timing
float prevFrameTime;

//player
Person guy = new Person();

//emotions
ArrayList<Emotion> emotions = new ArrayList<Emotion>();

int groundY;

boolean showHidden;


void setup() {

  size(900, 500);

  frameRate(60);
  smooth();

  groundY = height - 50;

  guy.setup(groundY);


  showHidden = false;

  prevFrameTime = millis();
}

void update() {
  float deltaTime = ((float)millis()-prevFrameTime)/1000.0;
  prevFrameTime = millis();

  guy.resetForces();

  guy.update(deltaTime);
  
  //update emotion pick ups
  for (int i=0; i<emotions.size(); i++) {
    Emotion thisEmotion = emotions.get(i);
    thisEmotion.update(deltaTime);
  }
}

void draw() {
  update();

  background(255);

  guy.draw(showHidden);

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

void spawnEmotion() {
  Emotion newEmotion = new Emotion();
  newEmotion.setup();
  emotions.add(newEmotion);
}

