
class particle
{
  PVector pos;
  PVector vel;
  PVector frc;   // frc is also know as acceleration (newton says "f=ma")

  boolean  bFixed;


  float groundY;  //set in Person
  
  float damping;

  //------------------------------------------------------------
  particle() {
    setInitialCondition(0, 0, 0, 0);
    damping = 0.05f;
    bFixed = false;
  }

  //------------------------------------------------------------
  void resetForce() {
    // we reset the forces every frame
    frc = new PVector(0,0);
  }

  //------------------------------------------------------------
  void addForce(float x, float y) {
    // add in a fPVectororce in X and Y for this frame.
    frc.x = frc.x + x;
    frc.y = frc.y + y;
  }
  
  //------------------------------------------------------------
  void addDampingForce() {

    // the usual way to write this is  vel *= 0.99
    // basically, subtract some part of the velocity 
    // damping is a force operating in the oposite direction of the 
    // velocity vector

    frc.x = frc.x - vel.x * damping;
    frc.y = frc.y - vel.y * damping;
  }


  //------------------------------------------------------------
  void setInitialCondition(float px, float py, float vx, float vy) {
    pos = new PVector(px, py);
    vel = new PVector(vx, vy);
  }

  //------------------------------------------------------------
  void update() {	
    if (bFixed == false) {
      vel.add(frc);
      pos.add(vel);
    }
  }

  //------------------------------------------------------------
  void draw() {
    ellipse(pos.x, pos.y, 9,9);
  }


  //------------------------------------------------------------
  void bounceOffWalls() {

    // sometimes it makes sense to damped, when we hit
    boolean bDampedOnCollision = true;
    boolean bDidICollide = false;


    if (pos.y > groundY) {
      pos.y = groundY; // move to the edge, (important!)
      vel.y *= -1;
      bDidICollide = true;
    } 
    
    if (bDidICollide == true && bDampedOnCollision == true) {
      vel.mult(0.5);
    }
    // what are the walls
//    float minx = 0;
//    float miny = 0;
//    float maxx = width;
//    float maxy = height;
//
//    if (pos.x > maxx) {
//      pos.x = maxx; // move to the edge, (important!)
//      vel.x *= -1;
//      bDidICollide = true;
//    } 
//    else if (pos.x < minx) {
//      pos.x = minx; // move to the edge, (important!)
//      vel.x *= -1;
//      bDidICollide = true;
//    }
//
//    if (pos.y > maxy) {
//      pos.y = maxy; // move to the edge, (important!)
//      vel.y *= -1;
//      bDidICollide = true;
//    } 
//    else if (pos.y < miny) {
//      pos.y = miny; // move to the edge, (important!)
//      vel.y *= -1;
//      bDidICollide = true;
//    }
//
//    if (bDidICollide == true && bDampedOnCollision == true) {
//      vel.mult(0.3);
//    }
  }

}

