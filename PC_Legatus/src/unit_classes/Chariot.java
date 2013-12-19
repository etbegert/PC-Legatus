package unit_classes;

public class Chariot extends Unit
{
    
    //=====================not done==========/
  public Chariot()
  { 
    unitID = Unit.CHARIOT;
    
    iconText = "Chariot";
    abilityName = "Undecided";
    currentHp =8;
    maxHp =8;
    currentDmg = 12;
    woundedDmg = 10;
    fatalDmg = 8;
    unitSize = 1;
    recruitCost = 12;
    prepareTime = 3;
    fortifyCost = 0;
    fortifyBonus = 0;
    
  }
}