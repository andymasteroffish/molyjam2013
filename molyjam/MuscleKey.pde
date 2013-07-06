class MuscleKey {

  char myKey;
  boolean isDown;
  spring muscle;

  float speed;
  float minPrc;
  float maxPrc;

  float curPrc;
  
  boolean isGrowing;
  
  particle liftPoint;

  void setup(char _key, spring _muscle, boolean setToGrow, particle _liftPoint) {
    myKey = _key;
    muscle = _muscle;
    curPrc = 1;

    isDown = false;

    speed = 3;
    maxPrc = 1;
    minPrc = 0.5;

    liftPoint = _liftPoint;

    if (setToGrow) {
      speed*=-1;
      maxPrc = 1.5;
      minPrc = 1;
    }
  }


  void checkKeyDown(char thisKey) {
    if (thisKey == myKey) {
      isDown = true;
    }
  }

  void checkKeyUp(char thisKey) {
    if (thisKey == myKey) {
      isDown = false;
    }
  }

  void update(float deltaTime) {
    if (isDown) {
      curPrc -= speed*deltaTime;
    }
    else {
      curPrc += speed*deltaTime;
    }

    curPrc = constrain(curPrc, minPrc, maxPrc);
    
    muscle.distance = muscle.originalDistance * curPrc;
    
    if (liftPoint != null){
      
      if (myKey == 'i'){
        liftPoint.vel.x += map(curPrc, minPrc, maxPrc, 0, 1)*1.5;
      }
      else if (myKey == 'e'){
        liftPoint.vel.x -= map(curPrc, minPrc, maxPrc, 0, 1)*1.5;
      }else{
       liftPoint.vel.y -= map(curPrc, minPrc, maxPrc, 1, 0)*2;
      }
    }
  }
}

