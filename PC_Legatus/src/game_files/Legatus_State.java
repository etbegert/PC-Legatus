package game_files;

import unit_classes.*;
import factions.*;
/**
 * @author Etienne
 *
 */
public class Legatus_State {
	
	public final static int PLAYER_1_TURN = 1;
	public final static int PLAYER_2_TURN = 2;
	
	//===========ACTION STATE CONSTANTS======//
		//Before a game is begun, state is unset, shown game start screen on animator
		public final static int PREGAME_STATE = -10;
		//state where nothing is selected
		public final static int DEFAULT_STATE = 0;
		//state where a unit on the board has been selected
		public final static int IN_PLAY_UNIT_SELECTED = 1;
		//state where a unit has been selected from the unit pool
		public final static int UNIT_IN_POOL_SELECTED = 2;
		//state where an empty space has been selected to deploy a unit
		public final static int EMPTY_SELECTED = 3;	
		// state entered after a unit from the pool as well as the space it is going into
		//have been selected
		public final static int READY_TO_DEPLOY = 4;
		//state where a command center has been destroyed and the game ends
		public final static int GAME_OVER = 5;
		//the integer representation of the current action state
	    private int currentState;
		//x and y coordinates of currently selected unit
		private int[] selectedXY = new int[2];
		//The array of all possible locations where a unit can be deployed (6x8 field)
		private Unit[][] battlefield = new Unit[5][7];
		
		
		private Legatus playerOne, playerTwo, currentPlayer;
		private int[] deploy_zone;
	
		
	/**
	 * @param factionOne: the faction that playerOne chose
	 * @param factionTwo: the faction that playerTwo chose
	 * Constructor for Legatus_State, sets up variables
	 */
	public Legatus_State(int factionOne, int factionTwo) 
	{
		currentState = DEFAULT_STATE;
		playerOne = new Legatus(factionOne, 1);
		playerTwo = new Legatus(factionTwo, 2);
		currentPlayer = playerOne;
		selectedXY[0] = 22;
		selectedXY[1] = 22;
	}
	
	
	public int getState()
	{
	    return currentState;
	}
	
	public void setState(int state)
	{
	    currentState = state;
	}
	
	/**
	 * @param deployedUnit
	 * Processes a deploy command from the driver and places the unit on the board
	 */
	public void processDeployedUnit(Unit deployedUnit)
	{
		//%%%TODO: STUB
	}
	
	/**
	 * updateUnitCounter()
	 * 
	 * decrements all of the unit's prepare timers
	 */
	public void updateUnitCounter()
	{
	    for (int x = 0; x<8;x++)
        {
            for (int y=0; y<9;y++)
            {
                
                //only decrement the prepare timers for the player whose turn it is
                if ((y < 4 && getCurrentPlayerID() == PLAYER_1_TURN)
                        || y > 4
                        && getCurrentPlayerID() == PLAYER_2_TURN)
                {
                    battlefield[x][y].countDown();
                }

            }//for
        }//for
	}
	//sets the selected space/unit
		public void selectSpace(int x, int y)
		{
		    selectedXY[0] = x;
		    selectedXY[1] = y;
		}
		
		//sets selected unit to way outside the bounds of the board, essentially deselecting anything
		public void deselect()
		{
		    selectedXY[0] = -1;
	        selectedXY[1] = -1;
		}
		
		public Unit getSelectedUnit()
		{
		    return getUnitAt(selectedXY[0],selectedXY[1]);
		}
		
		public int getSelectedX()
		{
		    return selectedXY[0];
		}
		
		public int getSelectedY()
	    {
	        return selectedXY[1];
	    }
		
		public void placeUnit(int x, int y, Unit unit)
		{
		    battlefield[x][y] = unit;
		}
		
		public int getCurrentPlayerID()
		{
			return currentPlayer.getID();
		}
		
		public Legatus getCurrentPlayer()
		{
		    return currentPlayer;
		}

		public Legatus getPlayerOne()
		{
		    return playerOne;
		}
		
		public Legatus getPlayerTwo()
	    {
	        return playerTwo;
	    }
		
		/**
		 * @param x coordinate
		 * @param y coordinate
		 * @return the unit on the battlefield at x-y coordinates
		 */
		public Unit getUnitAt(int x, int y)
		{
		    if (x<0||x>7||y>8||y<0)
		    {
		        return new Empty();
		    }
			return battlefield[x][y];
		}

		/**
		 * @return the max x and the max/min y coordinates that the current turn's
		 * player can deploy a unit to (max for 1st player, min for 2nd).
		 */
		public int[] getDeployZone()
		{
			return deploy_zone;
		}
		
		public Unit[][] getBattlefield()
		{
			return battlefield;
		}
		
		/**
		 * endTurn takes care of ending the turn and updating anay units
		 */
		public void endTurn()
		{
		    //change the current player
			if(currentPlayer.getID() == PLAYER_1_TURN)
			{
				currentPlayer = playerTwo;
				//decrement the prepare time for the units on the board
		        updateUnitCounter();
			}
			else if(currentPlayer.getID() == PLAYER_2_TURN)
			{
				currentPlayer = playerOne;
				//decrement the prepare time for the units on the board
		        updateUnitCounter();
			}
			currentPlayer.UpdateWarFunding();
		}
		
	    /**
	     * determineUnit takes a number passed (the result of a button) and returns the proper unit for the faction
	     * @param unitNum the number of the unit to determine
	     * @return a unit of the class determined by the passed unitNum
	     */
	    public Unit determineUnit(int unitNum)
	    {
	        Unit theUnit = null;
	        /*TODO implementation should be changed to below once other factions are implemented
	        theUnit = state.getCurrentPlayer().getFaction.chooseUnit(unitNum);*/
	        if (currentPlayer.getFaction() == Faction.ANCIENT)
	        {
	            theUnit = AncientFaction.chooseUnit(unitNum);
	        }
	        
	        return theUnit;
	    }
		
	    /**
	     * saveState()saves the saved game's state to memory with an android bundle
	     * @return a driver with restored game state from the saved game file
	     */
	    public int[] saveState()
	    {
	        int[] arr = new int[141];
	        //store turn number and values from player class that matter
	        arr[0] = currentPlayer.getID();
	        int[] ComOne = playerOne.saveToArray();
	        int[] ComTwo = playerTwo.saveToArray();
	        arr[1] = ComOne[0];
	        arr[2] = ComOne[1];
	        arr[3] = ComOne[2];
	        arr[4] = ComOne[3];
	        arr[5] = ComOne[4];
	        arr[6] = ComOne[5];
	        arr[7] = ComTwo[0];
	        arr[8] = ComTwo[1];
	        arr[9] = ComTwo[2];
	        arr[10] = ComTwo[3];
	        arr[11] = ComTwo[4];
	        arr[12] = ComTwo[5];
	        //stores entire battlefield (will leave empty unit spots as 3 consecutive zeros in arr
	        int i = 13;
	        for (Unit[] row : battlefield)
	        {
	            for (Unit unit : row)
	            {
	                if(!(unit instanceof unit_classes.Empty))
	                {
	                    arr[i] = unit.getUnitID();
	                    arr[i+1] = unit.isFortified();
	                    arr[i+2] = unit.getCurrentHP();
	                    arr[i+3] = unit.getPrepareTime();
	                }
	                i += 4;
	            }
	        }
	        return arr;
	    }//saveState
	    
	    /**
	     * restoreState does the reverse of saveState. Literally.
	     * @param savedState the saved instance variables
	     */
	    public void restoreState(int[] savedState)
	    {
	        int[] ComOne = {savedState[1], savedState[2], savedState[3], savedState[4], savedState[5], savedState[6]};
	        int[] ComTwo = {savedState[7], savedState[8], savedState[9], savedState[10], savedState[11], savedState[12]};
	        playerOne.restorePlayer(ComOne);
	        playerTwo.restorePlayer(ComTwo);
	        if(savedState[0] == 1)
	        {
	            currentPlayer = playerOne;
	        }//if
	        else
	        {
	            currentPlayer = playerTwo;
	        }//else
	        int unitRestore = 13;
	        for (Unit[] row : battlefield)
	        {
	            for (Unit unit : row)
	            {
	                int unitType = savedState[unitRestore];
	                //if not an empty unit, restore unit to its correct place
	                if (unitType != Unit.EMPTY) {
	                    //restores unit of correct type with previous hp, prep timer, and fortification status
	                    unit = determineUnit(unitType);
	                    unitRestore++;
	                    unit.restoreFortified(savedState[unitRestore]);
	                    unitRestore++;
	                    unit.setHP(savedState[unitRestore]);
	                    unitRestore++;
	                    unit.restoreTimer(savedState[unitRestore]);
	                    unitRestore++;
	                }//if
	                else {
	                    unitRestore += 4;
	                }//else
	            }//for
	        }//for
	    }//restoreState
	
}
