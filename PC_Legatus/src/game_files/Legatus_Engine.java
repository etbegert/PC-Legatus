package game_files;

import unit_classes.*;
import factions.*;
import java.awt.*;


/**
 * @author Etienne
 * Legatus_Engine handles user interactions and changes the Legatus_State accordingly
 */
public class Legatus_Engine
{
	
	private Legatus_Animator anim;
	private Legatus_State state;
	private Unit unitDeploy;
	private GameCanvas gameCanvas;
	private Frame gameFrame;
	
	/**
	 * @param factionOne: first player's faction
	 * @param factionTwo: second player's faction
	 * Constructor sets up a new game animator and a new game state
	 */
	public Legatus_Engine(int factionOne, int factionTwo)
	{
		anim = new Legatus_Animator(this, gameFrame);
		state = new Legatus_State(factionOne, factionTwo);
	}
	
	/**
	 * @param x: x position on grid
	 * @param y: y position on grid
	 * determines what action to take when one of the grid spaces is clicked by the mouse
	 */
	public void processMouseClick(int x, int y)
	{
		//STUB
	}
	
	/**
	 * @param key: the alpha-numeric number assigned to a pressed key
	 * determines what action to take when a key is pressed during game play
	 */
	public void processKeyboardClick(int key)
	{
		//STUB
	}
	
	/**
	 * @param unit: the attacking unit
	 * Enacts a unit's attack once its attack counter has reached 0
	 */
	public void processUnitAttack(Unit unit)
	{
		int column = unit.getUnitPosition()[0];
		//TODO: Can probably make this switch statement determine the current player then use variables to keep the needed values,
		// and only use one for/while loop after the switch.
		switch (state.getCurrentPlayerID())
		{
			//Player 1 has an attacking unit
			case 1:
				for (int y = 4; y < 7; y++)
				{
					
					if(unit.getCurrentDmg() > 0)
					{
						//not an empty space or a debris unit
						if(state.getUnitAt(column, y).getUnitID() != Unit.EMPTY 
								&& state.getUnitAt(column, y).getUnitID() != Unit.DEBRIS)
						{
							Unit defender = state.getUnitAt(column, y);
							int defenderHP = defender.getCurrentHP();
							defender.takeDamage(unit.getCurrentDmg());
							unit.dealDamage(defenderHP);
							
	                        // remove the defending unit if it got killed
	                        if (defender.getCurrentHP() <= 0)
	                        {
	                            state.placeUnit(column, y, new Debris());
						}
					}
					//unit has dealt all of the damage it can, break the attack off
					else
					{
						break;
					}
				}
				//if unit still has damage to deal and it reaches the last row, the opponent's structure
					//in that column will be damaged
				state.getPlayerTwo().damageStructure(column, unit.getCurrentDmg());
				break;
				}
				
			//Player 2 has an attacking unit	
			case 2:
				for (int y = 3; y >= -1; y--)
				{
					
					if(unit.getCurrentDmg() > 0)
					{
						//not an empty space or a debris unit
						if(state.getUnitAt(column, y).getUnitID() != Unit.EMPTY 
								&& state.getUnitAt(column, y).getUnitID() != Unit.DEBRIS)
						{
							Unit defender = state.getUnitAt(column, y);
							int defenderHP = defender.getCurrentHP();
							defender.takeDamage(unit.getCurrentDmg());
							unit.dealDamage(defenderHP);
							
	                        // remove the defending unit if it got killed
	                        if (defender.getCurrentHP() <= 0)
	                        {
	                            state.placeUnit(column, y, new Debris());
						}
					}
					//unit has dealt all of the damage it can, break the attack off
					else
					{
						break;
					}
				}
				//if unit still has damage to deal and it reaches the last row, the opponent's structure
					//in that column will be damaged
				state.getPlayerOne().damageStructure(column, unit.getCurrentDmg());
				break;
			}
		}
		//remove attacking unit and replace with empty battlefield square
		state.placeUnit(column, unit.getUnitPosition()[1], new Empty());
	}
	
	/**
	 * End's the current player's turn, checks if new player has any unit's that
	 * are ready to attack, and executes an attack if there exists any
	 */
	public void processEndTurnButton()
    {
        // set the board/state to be ready for the next player to start a new
        // turn
        state.deselect();
        state.setState(Legatus_State.DEFAULT_STATE);
        
        // tell the state to do the actions associated with ending a turn
        // end turn already decrements unit attack timer
        state.endTurn();
        
        // check to see if any of the new current player's units are ready to 
        // attack, if they are ready, then execute an attack
        for (Unit[] units : state.getBattlefield())
        {
        	for (Unit unit : units)
        	{
        		if (unit.isReadyToAttack())
        		{
        			processUnitAttack(unit);
        		}
        	}
        }
    }
	
	/**
	 * Deploys a unit to the current player's deployment zone given they have enough money to do so
	 */
	public void processDeployButton()
	{
		//check if commander has enough credits, if so deploy
        if (state.getCurrentPlayer().getCurrentWallet() >= unitDeploy.getRecruitCost())
        {
            state.getCurrentPlayer().spendsCredits(unitDeploy.getRecruitCost());
            state.placeUnit(state.getSelectedX(), state.getSelectedY(), unitDeploy);
            unitDeploy.prepare();
            //(Better way above)state.getUnitAt(state.getSelectedX(), state.getSelectedY()).prepare();
            state.setState(Legatus_State.DEFAULT_STATE);
            state.deselect();
        }
	}
	
	/**
	 * @param unitNum
	 * Determines the unit that was selected by the current player and displays that unit in the
	 * lower right corner of the screen
	 */
	public void processUnitButton(int unitNum)
	{
		 // this will handle presses from the buttons for our available units.
        // the number represents which unit it is
		
        // %%%TODO based on the id number of the button pressed, and the faction
        // of the current player, determine what the new unit that will be deployed
        // is.
        
        if(state.getState() == Legatus_State.EMPTY_SELECTED
                || state.getState() == Legatus_State.READY_TO_DEPLOY)
        {
            state.setState(Legatus_State.READY_TO_DEPLOY);
            unitDeploy = state.determineUnit(unitNum);
        }      
        else
        {
            state.setState(Legatus_State.UNIT_IN_POOL_SELECTED);
            unitDeploy = state.determineUnit(unitNum);
        }
	}
	
	/**
	 * When a debris unit is cleared by the current player, it will remove the unit from the battlefield
	 * given the player has enough money to do so
	 */
	public void processClearSpaceButton()
	{
		if (state.getSelectedUnit().getUnitID() == Unit.DEBRIS)
        {
            // prepare the selected unit
            state.getSelectedUnit().prepare();
            // make it so nothing is selected
            state.deselect();
            // reset the state
            state.setState(Legatus_State.DEFAULT_STATE);
        }
	}
	
	/**
	 *  Upgrades the current player's supply depot given they have enough money to do so
	 */
	public void processUpgradeDepotButton()
	{
		 if(state.getCurrentPlayer().getDepot().getUpgradeCost() <= state.getCurrentPlayer().getCurrentWallet())
	        {
	            state.getCurrentPlayer().spendsCredits(state.getCurrentPlayer().getDepot().getUpgradeCost());
	            state.getCurrentPlayer().getDepot().upgradeDepot();
	        }
	}
	
	/**
	 * called when the fortify unit button is pressed, tells the selected unit to fortify
     * @param toFortify the unit being fortified
	 */
	public void processFortifyUnitButton()
	{
		Unit toFortify = state.getSelectedUnit();
        if(state.getCurrentPlayer().getCurrentWallet() >= toFortify.getFortifyCost())
        {
            state.getCurrentPlayer().spendsCredits(toFortify.getFortifyCost());
            toFortify.fortifyUnit();
            state.setState(Legatus_State.DEFAULT_STATE);
            state.deselect();
        }
	}
	
	/**
	 * @return: the unit that is selected in the recruitment pool
	 */
	public Unit getUnitInPool()
    {
        return unitDeploy;
    }
	
	/**
	 * @return: the array of saved values from the game's current state
	 */
	public int[] saveGameState()
    {
        return state.saveState();
    }
    /**
     * @param savedState: the state being restored/loaded into game memory
     */
    public void restoreGameState(int[] savedState)
    {
        state.restoreState(savedState);
    }	
}
