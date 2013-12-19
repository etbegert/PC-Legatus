package unit_classes;

public class Knight extends Unit
{
  public Knight()
  { 
    unitID = Unit.KNIGHT;
    
    iconText = "knight";
    abilityName = "Undecided";
    currentHp = 13;
    maxHp = 13;
    currentDmg = 10;
    woundedDmg = 7;
    fatalDmg = 6;
    unitSize = 1;
    recruitCost = 10;
    prepareTime = 3;
    fortifyCost = 5;
    fortifyBonus = 10;
    
  }
}
