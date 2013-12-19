package game_files;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;
import android.widget.GridLayout;
import unit_classes.*;
import factions.AncientFaction;
import factions.Faction;

public class LegatusDriver
{
    
    private LegatusAnimator anim;
    private LegatusState state;
    private Toast errorToast;
    
    //This could instead be
    private Unit unitDeploy;
    
    /**
     * LegatusDriver constructor (normal constructor)
     * 
     * @param faction1
     * @param faction2
     * @param context
     */
    
    public LegatusDriver(int faction1, int faction2, Activity context)
    {
        state = new LegatusState(Faction.ANCIENT, Faction.ANCIENT);
        anim = new LegatusAnimator(context, this);
        errorToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        errorToast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, -143);
    }
    
    // ==================METHODS FOR HANDLING BUTTON PRESSES================//
    
    // %%%%%%%%%%%%%TODO: COMMENT %%%%%%%%%%%%%%%%%%%
    
    /**
     * processGridTouch: this method determines what action should be taken when
     * 
     * one of the game board grid spaces is touched.
     * 
     * @param x
     *            :the x position of the button
     * @param y
     *            :the y position of the button
     */
    public void processGridTouch(int x, int y)
    {
        // check to see if the side of the board selected is on the commanding
        // players side.
        if ((y < 4 && state.getCurrentCommanderID() == LegatusState.COMMANDER_1_TURN) || y > 4
                && state.getCurrentCommanderID() == LegatusState.COMMANDER_2_TURN)
        
        {
            
            // action for touching a square after choosing a unit to place
            if (state.getState() == LegatusState.UNIT_IN_POOL_SELECTED)
            {
                // action for touching an empty space
                if (state.getUnitAt(x, y).getUnitID() == Unit.EMPTY)
                {
                    state.setState(LegatusState.READY_TO_DEPLOY);
                    state.selectSpace(x, y);
                    anim.updateBoard(state, false);
                    return;
                }
                
            }
            
            // if state is ready to deploy and a new empty space is selected
            // select that space
            if (state.getState() == LegatusState.READY_TO_DEPLOY)
            {
                if (state.getUnitAt(x, y).getUnitID() == Unit.EMPTY)
                {
                    state.selectSpace(x, y);
                    anim.updateBoard(state, false);
                    
                    return;
                }
            }
            
            // set the state to reflect whether the clicked space is a unit or
            // not
            // only done if NO unit has been selected from the pool
            if (state.getUnitAt(x, y).getUnitID() == Unit.EMPTY)
            {
                state.setState(LegatusState.EMPTY_SELECTED);
                state.selectSpace(x, y);
                anim.updateBoard(state, false);
                return;
            }
            // if the space selected contains a unit
            else
            {
                state.setState(LegatusState.IN_PLAY_UNIT_SELECTED);
                state.selectSpace(x, y);
                anim.updateBoard(state, false);// %%%TODO unneccesary
            }
            
            // update the board
            anim.updateBoard(state, false);
        }
    }
    
    public void processDeployButton()
    {
        //check if commander has enough credits, if so deploy
        if (state.getCurrentCommander().getCurrentWallet() >= unitDeploy.getRecruitCost())
        {
            state.getCurrentCommander().spendsCredits(unitDeploy.getRecruitCost());
            state.placeUnit(state.getSelectedX(), state.getSelectedY(), unitDeploy);
            unitDeploy.prepare();
            //(Better way above)state.getUnitAt(state.getSelectedX(), state.getSelectedY()).prepare();
            state.setState(LegatusState.DEFAULT_STATE);
            state.deselect();
        } 
        //otherwise insufficient credits message
        else
        {
            anim.showError();
//            errorToast.setText("Insufficient credits to deploy unit.");
//            errorToast.show();
        }
        
        // update the board
        anim.updateBoard(state, false);
    }
    
    public void processEndTurnButton()
    {
        // set the board/state to be ready for the next player to start a new
        // turn
        state.deselect();
        state.setState(LegatusState.DEFAULT_STATE);
        
        // tell the state to do the actions associated with ending a turn
        // end turn already decrements unit attack timer
        state.endTurn();
        
        // check to see if any units are ready to attack, if they are then
        // execute an attack
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 9; y++)
            {
                if (state.getUnitAt(x, y).isReadyToAttack())
                {
                    launchAttack(state.getUnitAt(x, y), x, y);
                }
            }
        }
        
        // update board, tell animator that this is the end of the turn
        anim.updateBoard(state, true);
        
        anim.rotateView();
        
    }
    
    public void processUnitButton(int unitNum)
    {
        // this will handle presses from the buttons for our available units.
        // the number represents which unit it is
        // %%%TODO based on the id number of the button pressed, and the faction
        // of
        // the current player, determine what the new unit that will be deployed
        // is.
        
        if(state.getState() == LegatusState.EMPTY_SELECTED
                || state.getState() == LegatusState.READY_TO_DEPLOY)
        {
            state.setState(LegatusState.READY_TO_DEPLOY);
            unitDeploy = determineUnit(unitNum);
        }      
        else
        {
            state.setState(LegatusState.UNIT_IN_POOL_SELECTED);
            unitDeploy = determineUnit(unitNum);
        }
        
        // update the board
        anim.updateBoard(state, false);
    }
    
    public void processClearSpaceButton()
    {
        if (state.getSelectedUnit().getUnitID() == Unit.DEBRIS)
        {
            // prepare the selected unit
            state.getSelectedUnit().prepare();
            // make it so nothing is selected
            state.deselect();
            // reset the state
            state.setState(LegatusState.DEFAULT_STATE);
            
            // update the board
            anim.updateBoard(state, false);
        }
    }
    
    public void processUpgradeDepotButton()
    {
        if(state.getCurrentCommander().getDepot().getUpgradeCost() <= state.getCurrentCommander().getCurrentWallet())
        {
            state.getCurrentCommander().spendsCredits(state.getCurrentCommander().getDepot().getUpgradeCost());
            state.getCurrentCommander().getDepot().upgradeDepot();
        }
        else
        {
            errorToast.setText("Insufficient credits to upgrade supply depot.");
            errorToast.show();
        }
        anim.updateBoard(state, false);
        
    }
    
    /**
     * called when the fortify unit button is pressed, tells the selected unit to fortify
     * @param toFortify the unit being fortified
     */
    public void processFortifyUnitButton()
    {
        Unit toFortify = state.getSelectedUnit();
         if(state.getCurrentCommander().getCurrentWallet() >= toFortify.getFortifyCost())
         {
             state.getCurrentCommander().spendsCredits(toFortify.getFortifyCost());
             toFortify.fortifyUnit();
             state.setState(LegatusState.DEFAULT_STATE);
             state.deselect();
             anim.updateBoard(state, false);
         }
         else
         {
             errorToast.setText("Insufficient credits to fortify this unit.");
             errorToast.show();
         }
    }
    // =============================other shit=============================//
    
    //this takes a number passed (the result of a button) and returns the proper unit for
    //the faction
    private Unit determineUnit(int unitNum)
    {
        Unit theUnit = null;
        /*TODO implementation should be changed to
        theUnit = state.getCurrentCommander().getFaction90.chooseUnit(unitNum);*/
        if (state.getCurrentCommander().getFaction() == Faction.ANCIENT)
        {
            theUnit = AncientFaction.chooseUnit(unitNum);
        }
        
        return theUnit;
    }
    
    /**
     * 
     * 
     * @param attackingUnit
     *            is the unit that is currently attacking.
     * @return a unit if one is found in the column, or null, meaning the column
     *         has no opposing units; damages structure at end of column.
     */
    public void launchAttack(Unit attackingUnit, int column, int row)
    {
        int sequence;
        // make sure the attacking unit belongs to the current commander
        if (state.getCurrentCommander().getID() == LegatusState.COMMANDER_1_TURN)
        {
            // do damage from the middle row down
            for (int y = 5; y < 10; y++)
            {
                
                // make sure the attacking unit still has some damage left to
                // deal
                if (attackingUnit.getCurrentDmg() > 0)
                {
                    sequence = y-4;
                    // if y==9, we know the attack has reached a structure, so
                    // damage it.
                    if (y == 9)
                    {
                      //checks if command center was destroyed, if it was ends the game.
                        if(state.getCommanderTwo().damageStructure(column, attackingUnit.getCurrentDmg()))
                        {
                            state.setState(LegatusState.GAME_OVER);
                            closeActivity();
                        }
                    }
                    // if there is a unit in that space, do damage to it
                    else if (state.getUnitAt(column, y).getUnitID() != Unit.EMPTY
                            && state.getUnitAt(column, y).getUnitID() != Unit.DEBRIS)
                    {
                        int defenderHP = state.getUnitAt(column, y).getCurrentHP();
                        // damage the unit
                        state.getUnitAt(column, y).takeDamage(attackingUnit.getCurrentDmg());
                        // decrement the attack power by the defenders HP
                        attackingUnit.dealDamage(defenderHP);

                        // remove the defending unit if it got killed
                        if (state.getUnitAt(column, y).getCurrentHP() <= 0)
                        {
                            state.placeUnit(column, y, new Debris());
                            // tell the animator that a unit was killed here
                            anim.registerHitSquare(column, y,true, sequence);
                        }else
                        {
                            
                            // tell the animator that an attack passed over this
                            // square
                            anim.registerHitSquare(column, y,false,sequence);
                        }
                        
                        
                    }// if not empty
                    else
                    // if it is empty
                    {
                        // tell the animator that an attack passed over this
                        // square
                        anim.registerHitSquare(column, y,false,sequence);
                    }
                }// if current damage>0
            }// for
        }// if commander 1
        
        // this is exactly the same as the above code, but for the opposite side
        // of the board
        if (state.getCurrentCommander().getID() == LegatusState.COMMANDER_2_TURN)
        {
            
            //do damage from the middle row up
            for (int y = 3; y >= -1; y--)
            {
                // make sure the attacking unit still has some damage left to
                // deal
                sequence = 4-y;
                if (attackingUnit.getCurrentDmg() > 0)
                {
                    // if y==0, we know the attack has reached a structure, so
                    // damage it.
                    if (y < 0)
                    {
                        //checks if command center was destroyed, if it was ends the game.
                        if(state.getCommanderOne().damageStructure(column, attackingUnit.getCurrentDmg()))
                        {
                            state.setState(LegatusState.GAME_OVER);
                            closeActivity();
                        }
                    }
                    // if there is a unit in that space, do damage to it
                    else if (state.getUnitAt(column, y).getUnitID() != Unit.EMPTY
                            && state.getUnitAt(column, y).getUnitID() != Unit.DEBRIS)
                    {
                        int defenderHP = state.getUnitAt(column, y).getCurrentHP();
                        // damage the unit
                        state.getUnitAt(column, y).takeDamage(attackingUnit.getCurrentDmg());
                        // decrement the attack power by the defenders HP
                        attackingUnit.dealDamage(defenderHP);
                        

                        // remove the defending unit if it got killed
                        if (state.getUnitAt(column, y).getCurrentHP() <= 0)
                        {
                            
                            // tell the animator that an attack killed the unit
                            // at this square
                            anim.registerHitSquare(column, y,true, sequence);
                            state.placeUnit(column, y, new Debris());
                        } 
                        else
                        {
                                                    // tell the animator that an attack hit the unit at this
                        // square
                        anim.registerHitSquare(column, y,false, sequence);
                        
                        }
                    }// if not empty
                    else
                    {
                        
                        // tell the animator that an attack passed over this
                        // square
                        anim.registerHitSquare(column, y,false, sequence);
                    }
                }// if current damage>0
            }// for
        }// if commander 2
        
        // remove the attacking unit and tell the animator it was removed
        state.placeUnit(column, row, new Empty());
        anim.launchAttack(column, row);
    }
    
    // %%%TODO once we move the animator to the activity, this won't be
    // necesarury
    public GridLayout getBoard()
    {
        return anim.updateBoard(state, false);
    }
    
    public LegatusState getState()
    {
        return state;
    }
    
    public Unit getUnitInPool()
    {
        return unitDeploy;
    }
    
    private List<FinishListener> finishListeners = new ArrayList<FinishListener>();
    public void addFinishListener(FinishListener fl)
    {
        this.finishListeners.add(fl);
    }
    
    public void closeActivity()
    {
        for(FinishListener fl :finishListeners)
        {
            fl.onFinishCallback();
        }
    }

    public int[] saveGameState()
    {
        return state.saveState();
    }
    public void restoreGameState(int[] savedState)
    {
        state.restoreState(savedState);
    }

}//LegatusDriver class
