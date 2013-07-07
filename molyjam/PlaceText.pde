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
        //println("refill dull childhood now");
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

    if (area.equals("graveyard")) {
      if (dullText.size() == 0) {
        refillDullGraveyardText();
      }
      if (emotionalText.size() == 0) {
        refillEmotionalGraveyardText();
      }
    }
  }

  String getDullText() {
    //println("get dull for "+area);
    int randNum = int(random(dullText.size()));
    String returnText = dullText.get(randNum);
    //remove it
    dullText.remove(randNum);
    //see if we need more
    //println("you check it");
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
    emotionalText.clear();

    emotionalText.add("A butterfly lands on a flower, the colors of the two combining to form the most gorgeous thing you've ever seen. For the first time, you know what true sadness is.");
    emotionalText.add("Your father has purchased you a red ball for your birthday. You begin to realize just how many emotions there really are in the world, and how much you have to learn.");
    emotionalText.add("In school, you thought about how grades could possibly be the measure of a person. What matters more, a person’s intelligence, or their emotions? You weep into your desk.");
    emotionalText.add("A bully beats you up after school. But what really hurts is thinking about the pain he must feel on the inside. Who, really, is suffering more?");
    emotionalText.add("When you are toilet trained, you think of how lonely your potty will feel, replaced and obsolete. The despair keeps you up for three nights.");
  }


  void refillDullChildhoodText() {
    dullText.clear();

    //println("fill it you dull child");

    dullText.add("Your dad gives you a red ball for your birthday. You are happy that you can utilize it for exercise, as well as appreciating the difficulty in manufacturing a perfectly circular ball.");
    dullText.add("As a butterfly lands on a flower, you don't know whether to crush in order to rid the world of bugs or let it be devoured by some other insect. Bugs have no place in a civilized society.");
  }


  void refillEmotionalHospitalText() {
    emotionalText.clear();

    emotionalText.add("Hundreds of millions of years from now, the sun will die out, and there will be no more sunsets. You weep for the people of the future.");
    emotionalText.add("You wonder if everyone else can feel more than you can. This brings tears to your eyes.");
  }

  void refillDullHospitalText() {
    dullText.clear();

    dullText.add("You wonder where the property of all the dead patients go after they die. Does the hospital get it or do they return it to the family? These are important things to consider.");
    dullText.add("You see a mother weeping over the body of her child. If you had the strength, you would go up to her, put your hand on her shoulder, and tell her to compose herself in public.");
  }



  void refillEmotionalChurchText() {
    emotionalText.clear();

    emotionalText.add("You begin to think of how a child must feel when they are separated from their mother. Children, you realize, have much to teach us.");
    emotionalText.add("You watch as the bride and groom before you exchange vows. You wail loudly at the beauty of two people happily together forever.");
    emotionalText.add("How many emotions can a person feel at a wedding, a funeral, or both at the same time? Part of you hopes to never find out, but part of you does.");
  }

  void refillDullChurchText() {
    dullText.clear();

    dullText.add("Entering the vast cathedral, you give a sigh and think of how much money they could have been saved if it were a simple concrete building. Spacial efficiency is next to godliness, after all.");
    dullText.add("When you hear about your best friend getting a divorce, you become concerned. Only one of them will be able to claim their child as a dependent now.");
  }



  void refillEmotionalFastFoodText() {
    emotionalText.clear();

    emotionalText.add("As you watch a boy play with a puppy, you become stricken with grief.");
    emotionalText.add("Each one of these hamburgers is a unique hamburger, enjoyed by their own unique person, in a unique location, all across the world. Your hamburger is now filled with tears.");
  }

  void refillDullFastFoodText() {
    dullText.clear();

    dullText.add("You see children laughing and playing without a care in the world as the warm sun shines down. You are sickened.");
    dullText.add("You decide to purchase a game because the graphics look next-gen. You’re sure the gameplay will follow.");
  }


  void refillEmotionalGraduationText() {
    emotionalText.clear();

    emotionalText.add("As you microwave your dinner for the evening, you see your rotating food as a representation for man's struggle to find good in the world. You are not sure you can eat such a perfect metaphor now.");
    emotionalText.add("After completing a game with cutting edge graphics, you feel overwhelmed with sadness. The artists of the game will never match the beauty of a rainbow.");
  }

  void refillDullGraduationText() {
    dullText.clear();

    dullText.add("A bright rainbow shines over a grazing fawn on a hill. You shut the blinds to play another game of solitaire.");
    dullText.add("As you receive your degree in accounting, you think about your job prospects. There is no time for friendship in this world.");
  }

  void refillEmotionalGraveyardText() {
    emotionalText.clear();

    emotionalText.add("Looking at all the tombstones, you realize the dead outnumber the living. You vow to have as many emotions as they did before you die.");
    emotionalText.add("Grass and colorful flowers grow above the dead in the ground. Flowers, you realize, have much to teach us.");
  }

  void refillDullGraveyardText() {
    dullText.clear();

    dullText.add("This graveyard would be the perfect spot for a strip mall. The waste disappoints you.");
    dullText.add("Funerals are very upsetting to you. All the work being missed right now, it fills you with disgust.");
  }
}

