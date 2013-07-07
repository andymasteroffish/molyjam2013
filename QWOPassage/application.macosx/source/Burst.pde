class Burst {

  

  PVector pos;
  PVector vel;

  float grav = 0.1;
  float fric = 0.97;

  color col;

  float colR, colG, colB, colA;
  boolean killMe;
  
  float startTime = 1.5;
  float timer;

  void setup(float startX, float startY) {
    pos = new PVector(startX, startY);
    
    killMe = false;

    colR = random(255);
    colG = random(255);
    colB = random(255);
    
    timer = startTime;
    
    //give it a random angle and power
    float power = random(2,7);
    float angle = random(TWO_PI);
    
    vel = new PVector(0,0);
    vel.x = cos(angle)*power;
    vel.y = sin(angle)*power;
  }
  
  void update(float deltaTime){
    
    timer -= deltaTime;
    
    if (timer<0){
      killMe = true; 
    }
    
    colA = map(timer, startTime, 0, 255, 0);  
    
    vel.y+=grav;
    vel.x *= fric;
    vel.y *= fric;
    
    pos.x += vel.x;
    pos.y += vel.y;
    
    
  }
  
  void draw(){
    float drawSize = 4;
    fill(colR, colG, colB, colA);
    noStroke();
    ellipse(pos.x, pos.y, drawSize, drawSize);
  }
  
  void scroll(float scrollX){
    pos.x+=scrollX;
  }
  
}

