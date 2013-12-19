package unit_classes;

public class Archer extends Unit
{
    public Archer()
    {
        unitID = Unit.ARCHER;
        iconText = "archer";
        abilityName = "Undecided";
        currentHp = 7;
        maxHp = 7;
        currentDmg = 5;
        woundedDmg = 3;
        fatalDmg = 2;
        unitSize = 1;
        recruitCost = 8;
        prepareTime = 1;
        fortifyCost = 5;
        fortifyBonus = 4;
    }
}
