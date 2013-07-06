class Emotion{
  
  float xPos, yPos;
  float speed;
  float hitSize;
  
  void setup(){
    
    xPos = width+hitSize*2;
    yPos = 300;
    
    hitSize = 30;
    
  }
  
  void update(float deltaTime){
    
    xPos -= speed*deltaTime;
    
  }
  
  void draw(){
    
    fill(20,20,180);
    ellipse(xPos, yPos, hitSize, hitSize);
  }
  
  
}
