import ddf.minim.*;

class SoundManager {

  AudioPlayer songDull;
  AudioPlayer songEmotional;

  AudioPlayer emotionGet;
  AudioPlayer grunt;
  //AudioPlayer startGame;
  
  AudioPlayer typewriter;
  AudioPlayer whoosh1;
  AudioPlayer whoosh2;
  AudioPlayer thunder;

  Minim minim;


  void setup(Minim _minim) {
    minim = _minim;

    //music
    songDull = minim.loadFile("audio/dull.mp3", 2048);
    songDull.loop();

    songEmotional = minim.loadFile("audio/emotional.mp3", 2048);
    songEmotional.loop();

    songDull.pause();
    songEmotional.pause();

    //sound effects
    emotionGet = minim.loadFile("audio/emotionGetCut.mp3");

    grunt = minim.loadFile("audio/gruntSnap.mp3");
    
    typewriter = minim.loadFile("audio/typewriter2.wav");
    whoosh1 = minim.loadFile("audio/whoosh.aif");
    whoosh2 = minim.loadFile("audio/whoosh.aif");
    thunder = minim.loadFile("audio/thunder.wav");
    
  }

  void update(boolean playEmotional) {
    
    if (!emotionGet.isPlaying() && emotionGet.position() != 0) {
      emotionGet = minim.loadFile("audio/emotionGetCut.mp3");
    } 
    if (!grunt.isPlaying() && grunt.position() != 0) {
      grunt = minim.loadFile("audio/gruntSnap.mp3");
    }


    if (playEmotional && !songEmotional.isPlaying()) {
      songDull.pause();
      songEmotional.play();
    }
    if (!playEmotional && !songDull.isPlaying() ) {
      songEmotional.pause();
      songDull.play();
    }
  }

  void playGrunt() {
    grunt.play();
  }
  void playemotionGet() {
    emotionGet.play();
  }
  
  void playTypewriter() {
    typewriter.play();
  }
  
  void playWhoosh1() {
   whoosh1.play(); 
  }
  
  void playWhoosh2() {
   whoosh2.play(); 
  }
  
  void playThunder() {
   thunder.play(); 
  }
  
}

