package factions;
import unit_classes.*;

/**
 * Abstract class Faction - write a description of the class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public abstract class Faction
{
	// instance variables - replace the example below with your own
	protected int factionID;

	   
    public static final int MODERN= 0;
    public static final int ANCIENT= 1;
    public static final int DRUNK= 2;
    public static final int SCIFI= 3;
	
	private String[] unitNames;
	
	public String getUnitText(int n)
	{
	    return unitNames[n];
	}
    
	public int getFactionId()
	{
		return factionID;
	}
	
}
