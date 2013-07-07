import ddf.minim.*;

class SoundManager {

  AudioPlayer songDull;
  AudioPlayer songEmotional;
  
  AudioPlayer emotionGet;
  AudioPlayer grunt;
  AudioPlayer startGame;
  
  Minim minim;
  
  
  void setup(Minim _minim){
    minim = _minim;
    
    //music
    songDull = minim.loadFile("audio/dull.mp3", 2048);
    songDull.loop();
    
    songEmotional = minim.loadFile("audio/emotional.mp3", 2048);
    songEmotional.loop();
    
    songDull.pause();
    songEmotional.pause();
    
    //start game
    emotionGet = minim.loadFile("audio/emotionGetCut.wav");
    grunt = minim.loadFile("audio/grunt.wav");
    startGame = minim.loadFile("audio/startGame.wav");
    
    
    
  }
  
  void update(){
//    if (!emotionGet.isPlaying() && emotionGet.position() != 0){
//      emotionGet.rewind();
//      println(emotionGet.position());
//      println("rewind that ish");
//    } 
  }
  
  void playGrunt(){
    grunt.play(); 
  }
  void playemotionGet(){
    
   emotionGet.play(); 
  }
  void playStartGame(){
   startGame.play(); 
  }
}

