package unit_classes;

public class Catapult extends Unit
{
    
    //=====================not done==========/
  public Catapult()
  { 
    unitID = Unit.CATAPULT;
    
    iconText = "Catapult";
    abilityName = "Undecided";
    currentHp =5;
    maxHp =5;
    currentDmg = 25;
    woundedDmg = 18;
    fatalDmg = 10;
    unitSize = 1;
    recruitCost = 17;
    prepareTime = 5;
    fortifyCost = 0;
    fortifyBonus = 0;
    
  }
}