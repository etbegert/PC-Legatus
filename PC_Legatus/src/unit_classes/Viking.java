package unit_classes;




// The Viking class gets stronger as it takes more damage



public class Viking extends Unit
{
  public Viking()
  { 
    unitID = Unit.VIKING;
    
    iconText = "Viking";
    abilityName = "Undecided";
    currentHp = 10;
    maxHp = 10;
    currentDmg = 4;
    woundedDmg = 7;
    fatalDmg = 13;
    unitSize = 1;
    recruitCost = 10;
    prepareTime = 4;
    fortifyCost = 5;
    fortifyBonus = 10;
    
  }
}