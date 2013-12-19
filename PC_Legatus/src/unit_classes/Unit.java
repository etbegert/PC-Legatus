//Unit abstract class with methods that are similar to all units
package unit_classes;

public abstract class Unit
{
    
    // %%%TODO not sure any of these are necessary
    // constant variables to represent what type of unit it is
    public final static int EMPTY = 0;
    public final static int DEBRIS = 5;
    
    // Ancient
    public final static int KNIGHT = 10;
    public final static int ARCHER = 11;
    public final static int VIKING = 12;
    public final static int CHARIOT = 13;
    public final static int CATAPULT = 14;
    public final static int TRIREME = 15;
    
    // unit type will be set to one of the constants defined above
    protected int unitID;
    
    // These variables are set in the classes that inherit the Unit class.
    protected String iconText; // %%%TODO to be replaced with an image
    protected String abilityName;
    protected int[] deploymentPosition;
    protected int currentHp;
    protected int maxHp;
    protected int currentDmg;
    protected int woundedDmg;
    protected int fatalDmg;
    protected int unitSize;
    protected int fortifyBonus;
    protected int prepareTime;
    protected int recruitCost;
    protected int fortifyCost;
    protected int abilityCost;
    protected String abilityDescr;
    
    // booleans to show if the unit been prepared or has been fortified
    protected boolean isPreparing = false;
    protected int hasFortified = 0;
    
    // These variables only need to be seen in this class, subclasses can call
    // super
    // methods for their use.
    private String statsTable = new String();
    
    public String getText()
    {
        
        if (this.unitID != Unit.EMPTY && isPreparing)
        {
            if (this.unitID == Unit.DEBRIS)
            {
                return iconText + "\nclearing";
            }
            
            return iconText + "\n HP:" + currentHp + "\n time:" + prepareTime;
        }
        
        if (this.unitID != Unit.EMPTY && this.unitID != Unit.DEBRIS)
        {
            return iconText + "\n HP:" + currentHp;
        }
        
        return iconText;
    }
    
    /**
     * Creates an String of the current/selected unit's statistics (i.e.
     * instance variables)
     * 
     * @return the String of unit statistics that will be displayed at the
     *         bottom right-hand corner of the game display.
     */
    public String createStatTable()
    {
        statsTable = "Unit Type: " + iconText + " \nDescription: Unfinished\nCurrent HP/Max HP: "
                + this.currentHp + "/" + this.maxHp + " \nCurrent Damage: " + this.currentDmg
                + "\nRecruitment Cost: " + this.recruitCost + "\nTime: " + this.prepareTime + "\n" +
                		"Fortification Cost: " + this.fortifyCost + "\nFortify HP Boost: "
                + this.fortifyBonus + "\n Ability Cost: " + this.abilityCost + "\nAbility Description: "
                + this.abilityDescr;
        return statsTable;
    }
    
    /**
     * prepare
     * Sets the selected unit to a preparing state.
     */
    public void prepare()
    {
        isPreparing = true;
    }
    
    /**
     * countDown()
     * 
     * decrements the unit's prepare timer if it is preparing
     */
    public void countDown()
    {
        // only count down the prepare timer if the unit is preparing
        if (isPreparing)
        {
            prepareTime--;
        }
    }
    
    /**
     * isReadyToAttack()
     * 
     * returns whether or not the unit is ready to attack
     * 
     * @return true if it is ready to attack
     */
    public boolean isReadyToAttack()
    {
        return (prepareTime == 0);
    }
    
    //get methods for all the needed statistics of units
    public int[] getUnitPosition()
    {
        return deploymentPosition;
    }
    
    public int getCurrentHP()
    {
        return currentHp;
    }
    
    public void setHP(int HP)
    {
        currentHp = HP;
    }
    
    public void restoreTimer(int Time)
    {
        prepareTime = Time;
    }
    
    public void restoreFortified(int Fortified)
    {
        if(Fortified == 1)
        {
            fortifyUnit();
        }
        
    }
    
    public int getCurrentDmg()
    {
        return currentDmg;
    }
    
    public int getRecruitCost()
    {
        return recruitCost;
    }
    
    public int getPrepareTime()
    {
        return prepareTime;
    }
    
    public int getFortifyCost()
    {
        return fortifyCost;
    }
    
    public int getUnitID()
    {
        return unitID;
    }
    
    /**
     * Deploys the new unit to the grid
     * 
     * @param x
     *            is the x coordinate that the unit will be deployed at on the
     *            grid
     * @param y
     *            is the y coordinate that the unit will be deployed at on the
     *            grid
     */
    public void setPosition(int x, int y)
    {
        this.deploymentPosition[0] = x;
        this.deploymentPosition[1] = y;
        
    }
    
    /**
     * fortifyUnit increases a unit's current and maximum HP by a set amount (varies between
     * units), sets hasFortified to true so cannot be fortified multiple times.
     */
    public void fortifyUnit()
    {
        this.currentHp += fortifyBonus;
        this.maxHp += fortifyBonus;
        hasFortified = 1;
    }
    
    /**
     * isFortified
     * returns whether or not the selected unit has fortified.
     */
    public int isFortified()
    {
        return hasFortified;
    }
    
    /**
     * takeDamage(int damage)
     * 
     * If unit is hit by an attack, it will take damage to its HP, the driver
     * will determine if it needs to be removed from the board because it has
     * been killed
     * 
     * @param damage
     *            is the amount of damage being dealt to this unit
     */
    public void takeDamage(int damage)
    {
        this.currentHp -= damage;
        
        if (currentHp < (int) (0.7 * maxHp))
        {
            currentDmg = woundedDmg;
        }
        if (currentHp <= (int) (0.3 * maxHp))
        {
            currentDmg = fatalDmg;
        }
        
    }
    
    
    //%%%%TODO IS THIS UNUSED???
    /**
     * dealDamage(int damage) decrements the damage being dealt by this unit
     * 
     * @param damage
     *            : the amount it is being decremented by
     */
    public void dealDamage(int damage)
    {
        this.currentDmg -= damage;
    }
    
}
