package game_files;


public class SupplyDepot
{
    private int depotCurrHP; //subject to change
    private int depotMaxHP; //subject to change
    private int depotLevel;
    private int upgradeCost; //subject to change
    
    /**
     * This method implements the commander's credit production per turn from their
     *  supply depot.
     */
    public SupplyDepot()
    {
        depotCurrHP = 10;
        depotMaxHP = 50;
        depotLevel = 1;
        upgradeCost = depotLevel*20;
    }
    
    public int getDepotSI()
    {
        return depotCurrHP;
    }
    
    public void setDepotSI(int depotHP)
    {
        depotCurrHP = depotHP;
    }
    
    
    /**
     * @return the cost to upgrade the depot to a new level
     */
    public int getUpgradeCost()
    {
        return upgradeCost;
    }
    
    /**
     * @return the depot's current level
     */
    public int getDepotLevel()
    {
        return depotLevel;
    }
    
    /**
     * @param attackDamage the amount of damage being done to the depot
     */
    public void damaged(int attackDamage)
    {
        if(depotCurrHP >= attackDamage)
        {
            depotCurrHP -= attackDamage;
        }
    }
    
    /**
     * @return the number of credits the depot currently produces
     */
    public int produceCredits()
    {       
        if(depotCurrHP % 2 == 0)
        {
            return (int)(0.5 * depotCurrHP);
        }
        else
        {
            return (int)(0.5* (depotCurrHP - 1));
        }
    }
    
    //returns current depot health
    public int getDepotHP()
    {
        return depotCurrHP;
    }
    
    /**
     * Upgrades a supply depot's structural integrity so as to increase
     *  its credit production.
     */
    public void upgradeDepot()
    {
        if(depotLevel <= 6)
        {
            depotCurrHP = depotCurrHP+ 20; //subject to change
            depotMaxHP = depotMaxHP + 20; //subject to change
            depotLevel++;
            upgradeCost = depotLevel*10;
        }
    }
}//SupplyDepot class