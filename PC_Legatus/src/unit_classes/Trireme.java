package unit_classes;

public class Trireme extends Unit
{
    
    //=====================not done==========/
  public Trireme()
  { 
    unitID = Unit.TRIREME;
    
    iconText = "Trireme";
    abilityName = "Undecided";
    currentHp =20;
    maxHp =20;
    currentDmg = 20;
    woundedDmg = 18;
    fatalDmg = 15;
    unitSize = 1;
    recruitCost = 20;
    prepareTime = 6;
    fortifyCost = 0;
    fortifyBonus = 0;
    
  }
}