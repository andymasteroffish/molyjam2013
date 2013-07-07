class Person {

  particle[] particles = new particle[12];
  spring[] springs = new spring[27];
  float thisScale;

  PImage[] facePics = new PImage[2];
  PImage curFacePic;
  PVector facePoint;
  float faceAngle;


  MuscleKey[] muscleKeys = new MuscleKey[6];


  int groundY;
  boolean onTheGround;

  //emotions
  float emotionalLevel;
  float emotionalDrainPerSec;
  float emotionalLevelCutOff = 50;

  SoundManager SM;

  boolean collapsed;

  void setup(int _groundY, SoundManager _SM) {
    facePics[0] = loadImage("pic/pmneuxSad.png");
    facePics[1] = loadImage("pic/pmneux1.png");

    SM = _SM;

    thisScale = 35;   //how much to multiply everything by


    groundY = _groundY;

    emotionalLevel = 100;
    emotionalDrainPerSec = 2;

    //particle thisParticle;

    for (int i=0; i<particles.length; i++) {
      particles[i] = new particle();
    }
    for (int i=0; i<springs.length; i++) {
      springs[i] = new spring();
    }
    for (int i=0; i<muscleKeys.length; i++) {
      muscleKeys[i] = new MuscleKey();
    }

    resetPlayer();
  }

  void resetPlayer() {
    collapsed = false;

    float xOffset = 200;
    float yOffset = 100;

    float defaultSpringiness = 0.2f;

    onTheGround = false;

    int curParticle = 0;
    int curSpring  = 0;

    //head
    particles[curParticle++].setInitialCondition(xOffset+4* thisScale, yOffset+1* thisScale, 0, 0);
    particles[curParticle++].setInitialCondition(xOffset+4* thisScale, yOffset+2* thisScale, 0, 0);

    //body
    particles[curParticle++].setInitialCondition(xOffset+4* thisScale, yOffset+3* thisScale, 0, 0);
    particles[curParticle++].setInitialCondition(xOffset+4* thisScale, yOffset+5* thisScale, 0, 0);

    //left arm
    particles[curParticle++].setInitialCondition(xOffset+2* thisScale, yOffset+4* thisScale, 0, 0);
    particles[curParticle++].setInitialCondition(xOffset+2* thisScale, yOffset+2* thisScale, 0, 0);

    //right arm
    particles[curParticle++].setInitialCondition(xOffset+6* thisScale, yOffset+0* thisScale, 0, 0);
    particles[curParticle++].setInitialCondition(xOffset+6* thisScale, yOffset+2* thisScale, 0, 0);

    //left leg
    particles[curParticle++].setInitialCondition(xOffset+2* thisScale, yOffset+6* thisScale, 0, 0);
    particles[curParticle++].setInitialCondition(xOffset+2* thisScale, yOffset+8* thisScale, 0, 0);

    //right leg
    particles[curParticle++].setInitialCondition(xOffset+6* thisScale, yOffset+6* thisScale, 0, 0);
    particles[curParticle++].setInitialCondition(xOffset+6* thisScale, yOffset+8* thisScale, 0, 0);

    //set the ground for all springs
    for (int i=0; i<particles.length; i++) {
      particles[i].groundY = groundY;
    }

    //put in the visible springs
    //spring visibleSpring;
    //visibleSpring.springiness = defaultSpringiness;
    //visibleSpring.showing = true;

    //head and body
    springs[curSpring++].setFromParticles(particles[0], particles[1], defaultSpringiness, true);
    springs[curSpring++].setFromParticles(particles[1], particles[2], defaultSpringiness*4, true);
    springs[curSpring++].setFromParticles(particles[2], particles[3], defaultSpringiness, true);

    //left arm
    springs[curSpring++].setFromParticles(particles[4], particles[5], defaultSpringiness, true);
    springs[curSpring++].setFromParticles(particles[5], particles[2], defaultSpringiness, true);

    //right arm
    springs[curSpring++].setFromParticles(particles[6], particles[7], defaultSpringiness, true);
    springs[curSpring++].setFromParticles(particles[7], particles[2], defaultSpringiness, true);

    //left leg
    springs[curSpring++].setFromParticles(particles[9], particles[8], defaultSpringiness, true);
    springs[curSpring++].setFromParticles(particles[8], particles[3], defaultSpringiness, true);

    //right leg
    springs[curSpring++].setFromParticles(particles[11], particles[10], defaultSpringiness, true);
    springs[curSpring++].setFromParticles(particles[10], particles[3], defaultSpringiness, true);

    //secret support springs
    //spring supportSpring;
    //supportSpring.springiness = defaultSpringiness;
    //supportSpring.showing = false;

    //left head and arms
    springs[curSpring++].setFromParticles(particles[4], particles[0], defaultSpringiness*4, false);
    springs[curSpring++].setFromParticles(particles[5], particles[1], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[5], particles[0], defaultSpringiness, false);

    //right head and arms
    springs[curSpring++].setFromParticles(particles[6], particles[0], defaultSpringiness*4, false);
    springs[curSpring++].setFromParticles(particles[7], particles[1], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[7], particles[0], defaultSpringiness, false);

    //left body
    springs[curSpring++].setFromParticles(particles[5], particles[8], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[8], particles[2], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[5], particles[3], defaultSpringiness, false);

    //right body
    springs[curSpring++].setFromParticles(particles[7], particles[10], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[10], particles[2], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[3], particles[7], defaultSpringiness, false);

    //legs
    springs[curSpring++].setFromParticles(particles[8], particles[10], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[9], particles[11], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[9], particles[10], defaultSpringiness, false);
    springs[curSpring++].setFromParticles(particles[8], particles[11], defaultSpringiness, false);


    //set up keys
    int curMuscleKey = 0;
    muscleKeys[curMuscleKey++].setup('q', springs[17], false, particles[5]);
    muscleKeys[curMuscleKey++].setup('w', springs[8], false, null);
    muscleKeys[curMuscleKey++].setup('e', springs[25], true, particles[9] );

    muscleKeys[curMuscleKey++].setup('i', springs[26], true, particles[11]);
    muscleKeys[curMuscleKey++].setup('o', springs[10], false, null);
    muscleKeys[curMuscleKey++].setup('p', springs[20], false, particles[7]);
  }

  void resetForces() {
    for (int i = 0; i < particles.length; i++) {
      particles[i].resetForce();
    }
  }

  void update(float deltaTime, boolean stillInGame) {
    float stretchDist = 50;

    //deal with the emotional drain
    if (stillInGame){
      emotionalLevel -= emotionalDrainPerSec*deltaTime;
      emotionalLevel = constrain(emotionalLevel, 0, 100);
      curFacePic = (emotionalLevel < emotionalLevelCutOff) ? facePics[0] : facePics[1];
      println("emotion "+emotionalLevel);
    }


    //check the keys
    for (int i=0; i<muscleKeys.length; i++) {
      muscleKeys[i].update(deltaTime);
    }


    for (int i = 0; i < particles.length; i++) {
      //because of the different orientation, x and y need to be flipped
      particles[i].addForce(0, 0.3);
      //particles[i].addRepulsionForce(mouseX, mouseY, 300, 0.7f);
    }

    if (!collapsed) {
      for (int i = 0; i < springs.length; i++) {
        springs[i].update();
      }
    }

    for (int i = 0; i < particles.length; i++) {
      particles[i].bounceOffWalls();
      particles[i].addDampingForce();
      particles[i].update();
    }

    //set the face
    PVector mathVector = particles[0].pos;
    mathVector.mult(2);
    mathVector.add(particles[1].pos);
    mathVector.div(3);
    facePoint = mathVector;
    //facePoint = (particles[0].pos.mult(2) +particles[1].pos)/3;    //favoring point 0
    faceAngle = atan2(particles[2].pos.y-particles[0].pos.y, particles[2].pos.x-particles[0].pos.x)-PI/2;


    if (!collapsed) {
      //checking for neck snap
      PVector[] neckCheckPoints = new PVector[4];
      neckCheckPoints[0] = particles[0].pos;
      neckCheckPoints[1] = particles[5].pos;
      neckCheckPoints[2] = particles[2].pos;
      neckCheckPoints[3] = particles[7].pos;
      if (!checkInPolygon(neckCheckPoints, particles[1].pos.x, particles[1].pos.y)) {
        println("you broke his neck");
        //put it back, honky
        //reset the two head particles based on the bottom of the neck
        particles[0].setInitialCondition(particles[2].pos.x, particles[2].pos.y- 2*thisScale, 0, 0);
        particles[1].setInitialCondition(particles[2].pos.x, particles[2].pos.y- 1*thisScale, 0, 0);
      }

      //check if he is on the ground
      float groundDistToCount = 1;
      if ( particles[5].pos.y >= groundY-groundDistToCount)  onTheGround = true;
      if ( particles[7].pos.y >= groundY-groundDistToCount)  onTheGround = true;

      if (onTheGround) {
        resetPlayer();
        SM.playGrunt();
      }
    }
  }


  void checkKeyDown(char thisKey) {
    for (int i=0; i<muscleKeys.length; i++) {
      muscleKeys[i].checkKeyDown(thisKey);
    }
  }

  void checkKeyUp(char thisKey) {
    for (int i=0; i<muscleKeys.length; i++) {
      muscleKeys[i].checkKeyUp(thisKey);
    }
  }


  void draw(boolean showDebug) {



    fill(0);
    particles[4].draw();
    particles[6].draw();
    particles[9].draw();
    particles[11].draw();

    strokeWeight(2);
    for (int i = 0; i < springs.length; i++) {
      if (springs[i].showing) {
        strokeWeight(2);
        stroke(10);
        springs[i].draw();
      }
      else if (showDebug) {
        strokeWeight(1);
        stroke(100);
        springs[i].draw();
      }

      //in dbeug, show numbers
      if (showDebug) {
        fill(200, 0, 0);
        float xPos = (springs[i].particleA.pos.x + springs[i].particleB.pos.x)/2;
        float yPos = (springs[i].particleA.pos.y + springs[i].particleB.pos.y)/2; 
        String words = ""+i;
        text (words, xPos, yPos);
      }
    }
    strokeWeight(1);

    fill(255);
    pushMatrix();
    translate(facePoint.x, facePoint.y);
    rotate(faceAngle);
    image(curFacePic, -curFacePic.width/2, -curFacePic.height/2);
    popMatrix();

    if (showDebug) {
      fill(0, 0, 255);
      for (int i = 0; i < particles.length; i++) {
        String words = ""+i;
        text (words, particles[i].pos.x, particles[i].pos.y);
      }
    }
  }

  void earnEmotion(float val) {
    emotionalLevel += val;
  }

  void scroll(float scrollX) {
    for (int i=0; i<particles.length; i++) {
      particles[i].pos.x += scrollX;
    }
  }


  boolean checkInPolygon(PVector[] p, float x, float y)
  {
    int i =0;
    int j = 0;

    boolean c = false;

    for (i = 0, j = p.length-1; i < p.length; j = i++) {
      if ((((p[i].y <= y) && (y < p[j].y)) ||
        ((p[j].y <= y) && (y < p[i].y))) &&
        (x < (p[j].x - p[i].x) * (y - p[i].y) / (p[j].y - p[i].y) + p[i].x))
        c = !c;
    }
    return c;
  }

  void collapse() {
    collapsed = true;
    
    for (int i=0; i<particles.length; i++){
     particles[i].vel = new PVector(0,0); 
     particles[i].frc = new PVector(0,0);
    }
  }
}

