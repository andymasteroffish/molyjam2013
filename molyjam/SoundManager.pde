import ddf.minim.*;

class SoundManager {

  AudioPlayer songDull;
  AudioPlayer songEmotional;
  Minim minim;
  
  
  void setup(Minim _minim){
    minim = _minim;
    
    songDull = minim.loadFile("audio/dull.mp3", 2048);
    songDull.loop();
    
    songEmotional = minim.loadFile("audio/emotional.mp3", 2048);
    songEmotional.loop();
    
    songDull.pause();
    songEmotional.pause();
    
    
    
    
  }
  
  void update(){
    
    
  }
  
}

