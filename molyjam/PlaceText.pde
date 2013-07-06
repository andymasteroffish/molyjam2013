class PlaceText{

 ArrayList<String>  dullText = new ArrayList();  
 ArrayList<String>  emotionalText = new ArrayList<String>();

 
 String area;
 
 void setup(String _area){
  area = _area;
  
  //fill it up!
  checkTextRefill();
   
 }
 
 void checkTextRefill(){
   
   if (area.equals("field")){
     if (dullText.size() == 0){
       refillDullFieldText();
     }
     if (emotionalText.size() == 0){
       refillEmotionalFieldText(); 
     }
   }
   
 }
 
 String getDullText(){
   int randNum = int(random(dullText.size()));
   String returnText = dullText.get(randNum);
   //remove it
   dullText.remove(randNum);
   //see if we need more
   checkTextRefill();
   
   return returnText;
 }
 
 String getEmotionalText(){
   int randNum = int(random(emotionalText.size()));
   String returnText = emotionalText.get(randNum);
   //remove it
   emotionalText.remove(randNum);
   //see if we need more
   checkTextRefill();
   
   return returnText;
 }
 
 void refillDullFieldText(){
  dullText.clear();
  
  dullText.add("I am an dull test"); 
  dullText.add("I feel nothing"); 
   
 }
 
 void refillEmotionalFieldText(){
  emotionalText.clear();
  
  emotionalText.add("I 'am an exciting' test"); 
  emotionalText.add("You begin to think of 'how a child' must feel when they are separated from their mother. Children, you realize, have much to teach us.");  
   
 }
 
 
  
  
}
