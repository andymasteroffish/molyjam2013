class PlaceText {

  ArrayList<String>  dullText = new ArrayList<String>();  
  ArrayList<String>  emotionalText = new ArrayList<String>();


  String area;

  void setup(String _area) {
    area = _area;

    //fill it up!
    checkTextRefill();
  }

  void checkTextRefill() {

    if (area.equals("childhood")) {
      if (dullText.size() == 0) {
        refillDullChildhoodText();
      }
      if (emotionalText.size() == 0) {
        refillEmotionalChildhoodText();
      }
    }

    if (area.equals("hospital")) {
      if (dullText.size() == 0) {
        refillDullHospitalText();
      }
      if (emotionalText.size() == 0) {
        refillEmotionalHospitalText();
      }
    }

    if (area.equals("church")) {
      if (dullText.size() == 0) {
        refillDullChurchText();
      }
      if (emotionalText.size() == 0) {
        refillEmotionalChurchText();
      }
    }

    if (area.equals("fastFood")) {
      if (dullText.size() == 0) {
        refillDullFastFoodText();
      }
      if (emotionalText.size() == 0) {
        refillEmotionalFastFoodText();
      }
    }
    if (area.equals("graduation")) {
      if (dullText.size() == 0) {
        refillDullGraduationText();
      }
      if (emotionalText.size() == 0) {
        refillEmotionalGraduationText();
      }
    }
  }

  String getDullText() {
    int randNum = int(random(dullText.size()));
    String returnText = dullText.get(randNum);
    //remove it
    dullText.remove(randNum);
    //see if we need more
    println("you check it");
    checkTextRefill();

    return returnText;
  }

  String getEmotionalText() {
    int randNum = int(random(emotionalText.size()));
    String returnText = emotionalText.get(randNum);
    //remove it
    emotionalText.remove(randNum);
    //see if we need more
    checkTextRefill();

    return returnText;
  }
  
  
  
  void refillEmotionalChildhoodText() {
    dullText.clear();

    emotionalText.add("A butterfly lands on a flower, the colors of the two combining to form the most gorgeous thing you've ever seen. For the first time, you know what true sadness is.");

    emotionalText.add("Your father has purchased you a red ball for your birthday. You begin to realize just how many emotions there really are in the world, and how much you have to learn.");

    emotionalText.add("When you got an A on an assignment, you immediately think, 'Am I really as good or better than everyone else here? Who can make that decision? Can anyone?' You ponder this, lost in thought as you accidentally get locked in school again.");

    emotionalText.add("You see a car drive by your house. Are they on their way home? To a wedding? A funeral? Both? Could anyone handle the number of emotions a person must feel to attend a wedding/funeral? Part of you hopes to never find out, but part of you does.");

    emotionalText.add("A bully beats you up after school. But what hurts more than the punches is not your pain, but thinking about the pain he must feel that he became a bully. Who, really, is suffering more?");

    emotionalText.add("As you successfully use the toilet for the first time, you consider how lonely your potty will feel. How sad it will be that no one will ever use it again, that it will be a relic of your, and only your, childhood. This keeps you up for three nights.");
  }


  void refillDullChildhoodText() {
    emotionalText.clear();
    
    println("fill it fuck face");

    dullText.add("Your dad gives you a red ball for your birthday. You are happy that you can utilize it for exercise, as well as appreciating the difficulty in manufacturing a completely circular ball. You nod to your father.");
    dullText.add("As a butterfly lands on a flower, you don't know whether to crush in order to rid the world of bugs or let it be devoured by some other insect. Bugs have no place in a civilized society.");
  }


  void refillEmotionalHospitalText() {
    emotionalText.clear();

    emotionalText.add("Hundreds of millions of years from now, the sun will die out, and there will be no more sunsets. \"The people of the future will never be as emotionally fulfilled as us,\" you think.");
    emotionalText.add("'What if everyone can feel more than I can?' This brings tears to your eyes.");
  }

  void refillDullHospitalText() {
    dullText.clear();

    dullText.add("You stare at the dead and dying in the hospital, wondering just where all their stuff goes after they die. Does the hospital get it? Do they return it to the family. These are important things to consider.");
    dullText.add("You see a mother weeping over the body of her child. If you had the strength, you would go up to her, put your hand on her shoulder, and tell her to compose herself in public.");
  }



  void refillEmotionalChurchText() {
    emotionalText.clear();

    emotionalText.add("You begin to think of how a child must feel when they are separated from their mother. Children, you realize, have much to teach us.");
    emotionalText.add("You watch as the couple before you exchange vows. Through your life, you thought that you had potentially cried all the tears you ever could. You now know that you have, if ");
  }

  void refillDullChurchText() {
    dullText.clear();

    dullText.add("As you walk by a funeral and see dozens of people weeping, you think about watering your cactus. You then remember you let it wither from neglect. This does not bother you.");
    dullText.add("When you hear about your best friend getting a divorce, you are sad to think of how only one of them will be able to claim their child as a dependent.");
  }



  void refillEmotionalFastFoodText() {
    emotionalText.clear();

    emotionalText.add("As you watch a boy play with a puppy, you become stricken with grief.");
    emotionalText.add("You know that each one of these hamburgers is mostly the same, but some part of them is slightly different. They are each their own, unique hamburger, enjoyed by their own unique person, in a unique location, all across the world. Your hamburger is now filled with tears and must be sent back.");
  }

  void refillDullFastFoodText() {
    dullText.clear();

    dullText.add("You see children laughing and playing without a care in the world, free from the oppression of responsibilities as the warm sun shines down. 'Good,' you think, 'my daily Vitamin D requirements have been met. Back inside.'");
    dullText.add("You write a review for the last video game you played and end it with 'but the graphics were next-gen, so it's worth purchasing.'");
  }


  void refillEmotionalGraduationText() {
    emotionalText.clear();

    emotionalText.add("As you microwave your dinner for the evening, you see your rotating food as a representation for man's struggle to find good in the world. You are not sure you can eat such a perfect metaphor now.");
    emotionalText.add("After completing a game with cutting edge graphics, you feel overwhelmed with the sadness thinking about how the artists of the game will never match the beauty of a rainbow.");
  }

  void refillDullGraduationText() {
    dullText.clear();

    dullText.add("As a bright rainbow shines over the a hill, creating a picturesque view of a nature, you shut the blinds and play another game of solitaire.");
    dullText.add("As you step up to the podium to receive your degree in accounting, you nod your head in acknowledgment to your teachers and friends, happy you never formed a single connection to anyone. There is no time for friendship in this world.");
  }
  

}

