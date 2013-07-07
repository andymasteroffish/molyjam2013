class Emotion {

  float xPos, yPos;
  float speed;
  float hitSize;

  boolean killMe;
  
  float value;

  void setup() {

    xPos = width+hitSize*2;
    yPos = 145;

    speed = 100;

    hitSize = 30;
    
    value = 20;

    killMe = false;
  }

  void update(float deltaTime, Person player) {

    xPos -= speed*deltaTime;

    //check all points in the person to see if any are touching
    for (int i=0; i<player.particles.length; i++) {
      if ( dist(xPos, yPos, player.particles[i].pos.x, player.particles[i].pos.y) < hitSize) {
        killMe = true;
        player.earnEmotion(value);
        return;  //no need to keep checking
      }
    }
  }

  void draw() {

    fill(20, 20, 180);
    ellipse(xPos, yPos, hitSize, hitSize);
  }
  
  void scroll(float scrollX){
    xPos+=scrollX;
  }
}

