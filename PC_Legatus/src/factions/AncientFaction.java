package factions;

import unit_classes.*;

public class AncientFaction extends Faction
{
    static String[] unitNames = {"knight","archer","viking","chariot","catapult", "trireme"};
    
    
    public static String unitName(int n)
    {
        return unitNames[n];
    }
    
    public static Unit chooseUnit(int unitChosen)
    {
        switch(unitChosen)
        {
        case(0):
            return new Knight();
        case(1):
            return new Archer();
        case(2):
            return new Viking();
        case(3):
            return new Chariot();
        case(4):
            return new Catapult();
        case(5):
            return new Trireme();
        }
        
        return new Empty();
        
        
    }
    
}
