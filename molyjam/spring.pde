class spring {

  particle particleA;
  particle particleB;

  float originalDistance;
  float distance;
  float springiness;	 // this is the k, springiness constant

  boolean showing;
  
  float origAngle;
  float angleRange = PI*0.7;


  //---------------------------------------------------------------------
  spring() {
    particleA = null;
    particleB = null;

    showing = false;
  }

  //---------------------------------------------------------------------
  void setFromParticles(particle _particleA, particle _particleB) {
    particleA = _particleA;
    particleB = _particleB;

    distance = particleA.pos.dist(particleB.pos);
    originalDistance = distance;
    
    origAngle = atan2(particleA.pos.y-particleB.pos.y, particleA.pos.x-particleB.pos.y);
  }
  
    //---------------------------------------------------------------------
  void setFromParticles(particle _particleA, particle _particleB, float _springiness, boolean _showing) {
    particleA = _particleA;
    particleB = _particleB;
    
    springiness = _springiness;
    showing = _showing;

    distance = particleA.pos.dist(particleB.pos);
    originalDistance = distance;
    
    origAngle = atan2(particleA.pos.y-particleB.pos.y, particleA.pos.x-particleB.pos.y);
    println("orig "+origAngle);
  }

  //---------------------------------------------------------------------
  void update() {
    if ((particleA == null) || (particleB == null)) {
      return;
    }

    PVector pta = particleA.pos;
    PVector ptb = particleB.pos;
    
    PVector temp = pta.get();
    temp.sub(ptb);

    float theirDistance = temp.mag();
    float springForce = (springiness * (distance - theirDistance));
    
    temp.normalize();
    temp.mult(springForce);
    PVector frcToAdd = temp;

    particleA.addForce(frcToAdd.x, frcToAdd.y);
    particleB.addForce(-frcToAdd.x, -frcToAdd.y);
    
    //check the angle
    float curAngle = atan2(particleA.pos.y-particleB.pos.y, particleA.pos.x-particleB.pos.y);
    
    float curAngleDist1 = abs(curAngle-origAngle);
    float curAngleDist2 = abs(curAngle+TWO_PI -origAngle);
    float curAngleDist3 = abs(curAngle-TWO_PI -origAngle);
    
    float curAngleDist = min(curAngleDist1, min(curAngleDist2, curAngleDist3));
    
    if ( curAngleDist > angleRange){
      float thisDist = particleA.pos.dist(particleB.pos);
      particleB.pos.x = particleA.pos.x+ cos(origAngle+angleRange)*thisDist; 
      particleB.pos.y = particleA.pos.y+ sin(origAngle+angleRange)*thisDist; 
      println("orig "+origAngle + "   cur "+curAngle+"   dist "+curAngleDist);
    }
    
  }


  //---------------------------------------------------------------------
  void draw() {

    if ((particleA == null) || (particleB == null)) {
      return;
    }

    line(particleA.pos.x, particleA.pos.y, particleB.pos.x, particleB.pos.y);
  }
}

