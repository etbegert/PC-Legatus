package game_files;


//Legatus class
public class Legatus {
    private int playerID;
    private int myFaction;
    private int currentWallet;
    private int commandCenterHP;
    private SupplyDepot depot;
    
    /**
     * Constructor for Legatus class, sets variables to starting values.
     * @param chosenFaction is the new player's chosen faction.
     * @param ID is the player's ID for in-game purposes.
     */
    public Legatus(int chosenFaction, int ID)
    {
        playerID = ID;
        currentWallet = 10;
        myFaction = chosenFaction;
        commandCenterHP = 100; // subject to change
        depot = new SupplyDepot();
    }
    
    //Instance variables and methods dealing with this player's structures (command center and supply depot)
    private int maxWallet = 100; //subject to change
    
    //returns the HP of the command center
    public int getCurrHP()
    {
        return commandCenterHP;
    }
    
    /**
     * getID
     * @return the ID of this player
     */
    public int getID()
    {
        return playerID;
    }
    
    public SupplyDepot getDepot()
    {
        return depot;
    }
    
    /**
     * damageStructure
     * @param x is the column of the attacking unit
     * @param attackDamage the amount of damage being done to the structure
     */
    public boolean damageStructure(int x, int attackDamage)
    {
        //figure out which player this is and based on that
        //figure out whether the command center
        if (playerID == Legatus_State.PLAYER_1_TURN && x < 5)
        {
            commandCenterHP -= attackDamage;
            if(commandCenterHP <= 0)
            {
                return true;
            }
            return false;
        }
        if (playerID == Legatus_State.PLAYER_2_TURN && x > 2)
        {
            commandCenterHP -= attackDamage;
            if(commandCenterHP <= 0)
            {
                return true;
            }
            return false;
        }
        
        depot.damaged(attackDamage);
        return false;
    }
    
    /**
     * warFunding
     * Both players gets a flat amount of credits per turn so that they will
     *  never be unable to do anything (i.e. if their supply depot is destroyed)
     */
    public void UpdateWarFunding()
    {
        currentWallet += 2; // subject to change
        currentWallet += depot.produceCredits();
        if(currentWallet > maxWallet)
        {
            currentWallet = maxWallet;
        }
    }
    
    /**
     * getCurrentWallet
     * @return the amount of credits this player currently has
     */
    public int getCurrentWallet()
    {
        return currentWallet;
    }
    
    /**
     * getMaxWallet
     * @return the maximum amount of credits this player can have at one time
     */
    public int getMaxWallet()
    {
        return maxWallet;
    }
    
    /**
     * @param increaseBy: the amount to increase the wallet
     * setMaxWallet: set the maximum amount of credits this player can have
     */
    public void setMaxWallet(int increaseBy)
    {
        maxWallet += increaseBy;
    }
    
    /**
     * spendsCredits
     * @param actionCost
     */
    public void spendsCredits(int actionCost)
    {
        currentWallet -= actionCost;
    }
    
    /**
     * getFaction
     * @return the faction number of this player
     */
    public int getFaction()
    {
        return myFaction;
    }
    
    
    
    
    //TODO: MAY STILL USE TO SAVE GAME, BUT POSSIBLY NOT
    
    /**
     * saveToArray puts the important values into an array so they can be saved with an android bundle
     * to use if game is resumed later.
     * @return array of the important variables to save
     */
    public int[] saveToArray()
    {
        int[] arr = new int[6];
        arr[0] = playerID;
        arr[1] = myFaction;
        arr[2] = currentWallet;
        arr[3] = maxWallet;
        arr[4] = commandCenterHP;
        arr[5] = depot.getDepotSI();
        return arr;
    }
    
    /**
     * This is a reversed version of saveToArray. Literally.
     * @param savedCommander the saved commander instance variables
     */
    public void restorePlayer(int[] savedCommander)
    {
        playerID = savedCommander[0];
        myFaction = savedCommander[1];
        currentWallet = savedCommander[2];
        maxWallet = savedCommander[3];
        commandCenterHP = savedCommander[4];
        depot.setDepotSI(savedCommander[5]);
    }
}