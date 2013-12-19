package game_files;
/**
 * 
 */


import com.example.mobileappsproject.R;

import factions.AncientFaction;
import factions.Faction;
import unit_classes.*;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.*;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;

/**
 * @author Carl Lulay
 * 
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class LegatusAnimator extends SurfaceView
{
    // the ideal button size
    public static final int BUTTONSIZE = 99;
    
    
    private Button upgradeDepotButton;
    
    private Button endTurnButton;
    
    private Button deployButton;
    
    private Button clearSpaceButton;
    
    private Button fortifyUnitButton;
    
    private Button comanderOneBase;
    
    private Button comanderTwoBase;
    
    private Button comanderOneDepot;
    
    private Button comanderTwoDepot;
    
    private TextView statsTable;
    
    private TextView comanderOneCreditsView;
    
    private TextView comanderTwoCreditsView;
    
    private TextView errorText;
    private int errorAlpha=0;
    private Handler fadeHandler;
    // the grid of buttons, each button represents a unit on the board
    private Button[][] gameGridButtons;
    
    LayoutParams[][] params;
    
    LegatusDriver driver;
    
    // the view that hold our board
    GridLayout boardGrid;
    //holds our control buttons
    GridLayout controlGrid;
    //holds our unit pool
    GridLayout poolGrid;
    //holds boardGrid and controlGrid
    GridLayout masterGrid;
    
    //the context to use
    Context ourContext;
    
    // the view that holds our action buttons
    GridLayout actionBar;
    
    private Button[] unitButtons;
    
    // %%%TODO get rid of this eventually
    int index = 0;
    
    // this variable keeps track of which unit button was last pressed
    // so that it can show it as selected
    private int LastUnitButtonPressed;
    
    public LegatusAnimator(Context context, LegatusDriver ourDriver)
    {
        super(context);
        ourContext = context;
        statsTable = new TextView(context);
        boardGrid = new GridLayout(context);
        controlGrid = new GridLayout(context);
        poolGrid = new GridLayout(context);
        masterGrid = new GridLayout(context);
        index = 0;
        driver = ourDriver;

        masterGrid.addView(boardGrid, new GridLayout.LayoutParams(
                GridLayout.spec(0, 1), GridLayout.spec(0, 1)));
        masterGrid.addView(controlGrid, new GridLayout.LayoutParams(
                GridLayout.spec(1, 1), GridLayout.spec(0, 1)));
        masterGrid.addView(poolGrid, new GridLayout.LayoutParams(
                GridLayout.spec(2, 1), GridLayout.spec(0, 1)));
        masterGrid.setBackgroundColor(Color.BLACK);
        
        createActionButtons(context);
        createBoardGrid(context);
        createUnitButtons(context);
        createStructures(context);
        
        //initialize the handler to handle fading of the error message
        fadeHandler = new Handler();
        //Kludge to fix issue with starting layout having starting player appear at the top of the screen
        updateRotations(driver.getState());
        updateRotations(driver.getState());
    }
    
    // =================initialize visual component methods============
    
    // initializes the battle field
    private void createBoardGrid(Context context)
    {
        gameGridButtons = new Button[8][9];
        params = new LayoutParams[8][9];
        
        Space[][] space = new Space[8][9];
        LayoutParams[][] spaceParams = new GridLayout.LayoutParams[8][9];
        
        // set up the cash display button
        comanderOneCreditsView = new TextView(context);
        comanderTwoCreditsView = new TextView(context);
        GridLayout.LayoutParams creditParam = new GridLayout.LayoutParams(
                GridLayout.spec(10, 1), GridLayout.spec(0, 8));
        GridLayout.LayoutParams creditParam2 = new GridLayout.LayoutParams(
                GridLayout.spec(10, 1), GridLayout.spec(10, 8));
        boardGrid.addView(comanderOneCreditsView, creditParam);
        boardGrid.addView(comanderTwoCreditsView, creditParam2);
        
        comanderOneCreditsView.setTextColor(Color.BLUE);
        comanderOneCreditsView.setTextSize((float) 25);

        comanderTwoCreditsView.setTextColor(Color.RED);
        comanderTwoCreditsView.setTextSize((float) 25);
 
        GridLayout.LayoutParams errorParam = new GridLayout.LayoutParams(
                GridLayout.spec(10, 4), GridLayout.spec(6, 10));
        errorText = new TextView(context);
        errorText.setText("Insufficient funds!!!");
        errorText.setBackgroundColor(Color.GRAY);
        errorText.setTextSize(30);
        errorText.setAlpha(0);
        errorText.setTextColor(Color.RED);
        errorText.setGravity(Gravity.RIGHT);
        boardGrid.addView(errorText, errorParam);
        
        //set up the grid of buttons for the playing board
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 9; y++)
            {
                gameGridButtons[x][y] = new Button(context);
                // used to set the location and size of the buttons in the grid
                params[x][y] = new GridLayout.LayoutParams(
                        GridLayout.spec(2*y+2, 1), GridLayout.spec(2*x, 1));
                spaceParams[x][y] = new GridLayout.LayoutParams(
                        GridLayout.spec(2*y+3, 1), GridLayout.spec(2*x+1, 1));
                gameGridButtons[x][y].setMinWidth(BUTTONSIZE - 50);
                gameGridButtons[x][y].setMinHeight(BUTTONSIZE - 50);
                gameGridButtons[x][y].setHeight(BUTTONSIZE);
                gameGridButtons[x][y].setWidth(BUTTONSIZE);
                // make the buttons send their coordinates to the driver when
                // pressed
                if (y>4)
                {
                    gameGridButtons[x][y].setBackgroundColor(Color.RED);
                }
                if (y<4)
                {
                    gameGridButtons[x][y].setBackgroundColor(Color.BLUE);
                }
                //add the click functionality to let the driver know it needs
                //to do stuff
                gameGridButtons[x][y].setOnClickListener(new OnClickListener()
                {
                    
                    public void onClick(View v)
                    {
                        for (int x = 0; x < 8; x++)
                        {
                            for (int y = 0; y < 9; y++)
                            {
                                if (v == gameGridButtons[x][y])
                                {
                                    driver.processGridTouch(x, y);
                                }
                            }
                        }
                        
                    }
                    
                });
                
                
                // make the middle row of buttons invisible to create a divider
                // between
                // the two sides of the field. Make them shorter so the gap
                // isn't so huge
                if (y == 4)
                {
                    gameGridButtons[x][y].setAlpha(0);
                    gameGridButtons[x][y].setEnabled(false);
                    gameGridButtons[x][y].setHeight(30);
                }
                // %%%TODO get rid of the index variable and use a different
                // constructor
                space[x][y] = new Space(context);
                space[x][y].setMinimumHeight(1);
                space[x][y].setMinimumWidth(1);
                
                
                boardGrid.addView(gameGridButtons[x][y], index, params[x][y]);
                index++;
                
                boardGrid.addView(space[x][y], spaceParams[x][y]);
            }// for
        }// for
     //set the background color
     boardGrid.setBackgroundColor(Color.BLACK);
     

    }
    
    //adds the visual representation of the structures to the board
    private void createStructures(Context context)
    {
        //add the Buttons (which are not enabled) to show the players
        //health and Depot
        comanderOneBase = new Button(context);
        comanderTwoBase = new Button(context);
        comanderOneDepot = new Button(context);
        comanderTwoDepot = new Button(context);
        //set height and width
        comanderOneBase.setWidth((BUTTONSIZE+1)*5-1);
        comanderTwoBase.setWidth((BUTTONSIZE+1)*5-1);
        comanderOneDepot.setWidth((BUTTONSIZE+1)*3);
        comanderTwoDepot.setWidth((BUTTONSIZE+1)*3-1);
        
        comanderOneBase.setHeight(BUTTONSIZE/2);
        comanderTwoBase.setHeight(BUTTONSIZE/2);
        comanderOneDepot.setHeight(BUTTONSIZE/2);
        comanderTwoDepot.setHeight(BUTTONSIZE/2);
        
        //set color (text will be set in the update)
        comanderTwoBase.setBackgroundColor(Color.argb(255, 200, 0, 0));
        comanderOneBase.setBackgroundColor(Color.argb(255, 0, 0, 200));
        comanderTwoDepot.setBackgroundColor(Color.argb(255, 200, 0, 0));
        comanderOneDepot.setBackgroundColor(Color.argb(255, 0, 0, 200));
        
        //add a spaces to visually separate the board and the structures
        Space space1 = new Space(context);
        Space space2 = new Space(context);
        space1.setMinimumHeight(1);
        space2.setMinimumHeight(1);
        boardGrid.addView(space1,new GridLayout.LayoutParams(
                      GridLayout.spec(1, 1), GridLayout.spec(0, 9)));
        boardGrid.addView(space2,new GridLayout.LayoutParams(
                GridLayout.spec(19, 1), GridLayout.spec(0, 9)));
        
        boardGrid.addView(comanderOneBase, new GridLayout.LayoutParams(
                   GridLayout.spec(0, 1), GridLayout.spec(0, 9)));
        boardGrid.addView(comanderOneDepot, new GridLayout.LayoutParams(
                GridLayout.spec(0, 1), GridLayout.spec(10, 6)));
        
        boardGrid.addView(comanderTwoBase, new GridLayout.LayoutParams(
                GridLayout.spec(20, 1), GridLayout.spec(6, 10)));
        boardGrid.addView(comanderTwoDepot, new GridLayout.LayoutParams(
                GridLayout.spec(20, 1), GridLayout.spec(0, 5)));
    }
    
    // initializes prepare,deploy,endTurn, etc buttons; the buttons
    // which perform actions
    private void createActionButtons(Context context)
    {  
        // set up the deploy button
        deployButton = new Button(context);
      //layout params for buttons, second spec is set to 4 so that when it is
        // added to the grid it does not push the column to the right away
        GridLayout.LayoutParams deployParam = new GridLayout.LayoutParams(
                GridLayout.spec(0, 1), GridLayout.spec(0, 1));
        deployButton.setText("DEPLOY UNIT");
        deployButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                driver.processDeployButton();
            }
        });
        controlGrid.addView(deployButton, deployParam);
        deployButton.setTextColor(Color.WHITE);
        // set up the prepare button
        clearSpaceButton = new Button(context);        
        GridLayout.LayoutParams clearSpaceParam = new GridLayout.LayoutParams(
                GridLayout.spec(0, 1), GridLayout.spec(1, 1));
        clearSpaceButton.setText("CLEAR SPACE");
        clearSpaceButton.setOnClickListener(new OnClickListener()
        {
            
            public void onClick(View v)
            {
               driver.processClearSpaceButton();
            }
            
        });
        controlGrid.addView(clearSpaceButton, clearSpaceParam);
        clearSpaceButton.setTextColor(Color.WHITE);
        // set up the upgrade depot button
        upgradeDepotButton = new Button(context);        
        GridLayout.LayoutParams upgradeDepotParam = new GridLayout.LayoutParams(
                GridLayout.spec(0, 1), GridLayout.spec(2, 1));
        upgradeDepotButton.setText("UPGRADE DEPOT");
        upgradeDepotButton.setOnClickListener(new OnClickListener()
        {
            
            public void onClick(View v)
            {
               driver.processUpgradeDepotButton();
            }
            
        });
        controlGrid.addView(upgradeDepotButton, upgradeDepotParam);
        upgradeDepotButton.setTextColor(Color.WHITE);
      //set up the fortify unit button
        fortifyUnitButton = new Button(context);
        GridLayout.LayoutParams fortifyUnitParam = new GridLayout.LayoutParams(
                GridLayout.spec(0, 1), GridLayout.spec(3, 1));
        fortifyUnitButton.setText("Fortify Unit");
        fortifyUnitButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                driver.processFortifyUnitButton();
            }
        });
        controlGrid.addView(fortifyUnitButton, fortifyUnitParam);
        fortifyUnitButton.setTextColor(Color.WHITE);
        
        // set up the end turn button
        endTurnButton = new Button(context);
        GridLayout.LayoutParams endTurnParam = new GridLayout.LayoutParams(
                GridLayout.spec(0, 1), GridLayout.spec(4, 1));
        endTurnButton.setText("HOLD TO END TURN");
        endTurnButton.setOnLongClickListener(new OnLongClickListener()
        {
            
            public boolean onLongClick(View v)
            {
                driver.processEndTurnButton();
                return false;
            }
        });
        controlGrid.addView(endTurnButton, endTurnParam);
        endTurnButton.setTextColor(Color.WHITE);
        
        
        
        
    }
    
    // initializes the unit buttons
    private void createUnitButtons(Context context)
    {
        // set up the unit selection buttons
        
        // %%%TODO Later on we'll need to change updateboard so that it changes
        // these buttons displays
        // based on the faction
        Space[] spaces=new Space[6];
        unitButtons = new Button[6];
        
        // x and y variables control the placement of the unit buttons
        int x = 0;
        int y = 0;
        for (int i = 0; i < 6; i++)
        {
            //add a space between each button
            spaces[i]= new Space(context);
            spaces[i].setMinimumHeight(1);
            spaces[i].setMinimumWidth(1);
            poolGrid.addView(spaces[i], new GridLayout.LayoutParams(
                    GridLayout.spec(y+1, 1), GridLayout.spec(2*x+1, 1)));
            
            // put the last 3 buttons down a row and reset the x value
            if (i == 3)
            {
                y=2;
                x = 0;
            }
            
            unitButtons[i] = new Button(context);
            unitButtons[i].setText(i + "");
            GridLayout.LayoutParams unitParam = new GridLayout.LayoutParams(
                    GridLayout.spec(y, 1), GridLayout.spec(2*x, 1));
            
            unitButtons[i].setMinWidth(BUTTONSIZE - 50);
            unitButtons[i].setMinHeight(BUTTONSIZE - 50);
            unitButtons[i].setHeight(BUTTONSIZE);
            unitButtons[i].setWidth(BUTTONSIZE);
            // it seems that setting text messes with the gravity of the button,
            // so we have to reset it
            unitButtons[i].setGravity(Gravity.CENTER_HORIZONTAL);
            
            // have each button tell the driver that it has been pressed
            // the driver will figure out what to do with it
            unitButtons[i].setOnClickListener(new OnClickListener()
            {
                
                public void onClick(View v)
                {
                    for (int i = 0; i < 6; i++)
                    {
                        if (v == unitButtons[i])
                        {
                            // update LastUnitButtonPressed to show which unit
                            // button
                            // was pressed
                            LastUnitButtonPressed = i;
                            // tell the driver to handle this button press
                            driver.processUnitButton(i);
                        }
                    }
                }
            });
            
            // add the button
            poolGrid.addView(unitButtons[i], unitParam);
            x++;
        }

        
        
        GridLayout.LayoutParams statsTableParam = new GridLayout.LayoutParams(
                GridLayout.spec(0, 6), GridLayout.spec(6, 1));
       
        //add in the stats table and set up its appearance
        poolGrid.addView(statsTable, statsTableParam);
        statsTable.setTextColor(Color.WHITE);
        statsTable.setTextSize((float) 15);
        statsTable.setGravity(Gravity.LEFT);
    }

    //====================update visual component methods========================
    
    /**
     * updateBoard
     * 
     * @param state
     * @param newTurn: this signifies if a turn has just been ended
     * because we don't want to update colors if it is
     * 
     * @return the newly updated board %%%TODO may not need to do
     * this if we move all this to the activity
     */
    public GridLayout updateBoard(LegatusState state, boolean newTurn)
    {
        //update each of the different view components
        updateBattleField(state, newTurn);     
        updateActionButtons(state);
        updateUnitButtons(state);        
        updateStatsView(state);
        updateStructures(state);
        
        comanderOneCreditsView.setText("BLUE'S CREDITS: " + driver.getState().getCommanderOne().getCurrentWallet());
        comanderTwoCreditsView.setText("RED'S CREDITS: " + driver.getState().getCommanderTwo().getCurrentWallet());
        
        return masterGrid;
    }// getBoard
    
    //sets the text for the structures to display the current players HP
    //and the depots generation rate
    private void updateStructures(LegatusState state)
    {
        int c1depotHP = state.getCommanderOne().getDepot().getDepotHP();
        int c2depotHP = state.getCommanderTwo().getDepot().getDepotHP();
        int c1depotLvl = state.getCommanderOne().getDepot().getDepotLevel();
        int c2depotLvl = state.getCommanderTwo().getDepot().getDepotLevel();
        
        
        comanderOneBase.setText("COMMAND CENTER HP:" + state.getCommanderOne().getCurrHP());
        comanderOneDepot.setText("DEPOT HP:" + c1depotHP + " (Lvl:" + c1depotLvl + ")");
        comanderTwoBase.setText("COMMAND CENTER HP:" + state.getCommanderTwo().getCurrHP());
        comanderTwoDepot.setText("DEPOT HP:" + c2depotHP + " (Lvl:" + c2depotLvl + ")");
    }
    
    //%%%%TODO COMMENT
    private void updateBattleField(LegatusState state, boolean newTurn)
    {
        // UPDATE THE BATTLEFIELD
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 9; y++)
            {
                //set the text for the unit at the space %%%TODO replace with sprites
                gameGridButtons[x][y].setText(state.getUnitAt(x, y).getText());
 
                // setting text messes with the gravity of the button, so we have to reset it
                gameGridButtons[x][y].setGravity(Gravity.CENTER_HORIZONTAL);
                
                // alter the alpha value of the selected space to give
                //the appearance of being selected
                if (x == state.getSelectedX() && y == state.getSelectedY())
                {
                    gameGridButtons[x][y].setAlpha((float) .7);

                } else if (y!=4)
                {
                    // set all other buttons to not appear selected
                    gameGridButtons[x][y].setAlpha((float) 1);
                    //return all grid squares to enabled, since some were 
                    //disabled to keep track of spaces that units died in
                    gameGridButtons[x][y].setEnabled(true);
                }
                
                //make sure it is not a new turn, if it is a new turn, then shit will be
                //set to a different color and stuff, and ya'll don'ts wanna overrides that
                if (!newTurn)
                {
                    //reset the board colors
                    if (y>4)
                    {
                        gameGridButtons[x][y].setBackgroundColor(Color.RED);
                    }
                    if (y<4)
                    {
                        gameGridButtons[x][y].setBackgroundColor(Color.BLUE);
                    }
                }//if not new turn   
            }// for
        }// for
    }
    //%%%%TODO COMMENT
    private void updateActionButtons(LegatusState state)
    {
        //start with these buttons disabled, we'll enable them based on state
        deployButton.setEnabled(false);
        clearSpaceButton.setEnabled(false);
        fortifyUnitButton.setEnabled(false);
        
        // set the buttons to be enabled based on what the state is.
        if (state.getState() == LegatusState.IN_PLAY_UNIT_SELECTED)
        {
            Unit selectedUnit = state.getSelectedUnit();
            if(selectedUnit.getUnitID()==Unit.DEBRIS)
            {
                clearSpaceButton.setEnabled(true);
            }
            else if(selectedUnit.getUnitID() != Unit.EMPTY && selectedUnit.isFortified() == 0)
            {
                fortifyUnitButton.setEnabled(true);
            }
        }
        if(driver.getState().getCurrentCommander().getDepot().getDepotLevel() == 6)
        {
            upgradeDepotButton.setEnabled(false);
        }
        if (state.getState() == LegatusState.READY_TO_DEPLOY)
        {
            deployButton.setEnabled(true);
            
        }
    }
    
    //updates the unit buttons to reflect the state
    private void updateUnitButtons(LegatusState state)
    {
        //determine the color that the selected unit will be set to
        int color;
        if (state.getCurrentCommander().getID()==LegatusState.COMMANDER_1_TURN)
        {
            color = Color.BLUE;
        }
        else
        {
            color = Color.RED;
        }
        
        // UPDATE THE UNIT BUTTONS
        // %%%TODO set this to images
        for (int i = 0; i < 6; i++)
        {
            //check the faction, set units to display units accordingly
            if (state.getCurrentCommander().getFaction() == Faction.ANCIENT)
            {
                unitButtons[i].setText(AncientFaction.unitName(i));
                // it seems that setting text messes with the gravity of the
                // button, so we have to reset it
                unitButtons[i].setGravity(Gravity.CENTER_HORIZONTAL);
                
            }
            
            // if a unit has been chosen from the pool, make it reflect that
            if ((state.getState() == LegatusState.READY_TO_DEPLOY || state
                    .getState() == LegatusState.UNIT_IN_POOL_SELECTED)
                    && (i == LastUnitButtonPressed))
            {
              //make it the it appear selected
                unitButtons[i].setAlpha((float).7);
            } 
            else
            {
              //make it the it appear not selected
                unitButtons[i].setAlpha((float)1);
              //make it the team color
                unitButtons[i].setBackgroundColor(color);
            }
            
            
            
        }//updating unit buttons
    }
    
    //updates stats table
    private void updateStatsView(LegatusState state)
    {
        if(!(state.getSelectedUnit() instanceof unit_classes.Empty))
        {
            statsTable.setText(state.getSelectedUnit().createStatTable());
        }
        else if(state.getState() == LegatusState.UNIT_IN_POOL_SELECTED || state.getState() == LegatusState.READY_TO_DEPLOY)
        {
            statsTable.setText(driver.getUnitInPool().createStatTable());
        }
        else
        {
            statsTable.setText("No unit selected.");
        }
    }

    //makes sure all the views are facing the right direction
    private void updateRotations(LegatusState state)
    {

        if (driver.getState().getCurrentCommanderID()==LegatusState.COMMANDER_2_TURN)
        {
            masterGrid.setRotation((float)180);
            boardGrid.setRotation((float)0);
            comanderOneCreditsView.setRotation((float)0);
            comanderTwoCreditsView.setRotation((float)0);
            comanderOneBase.setRotation((float)0);
            comanderOneDepot.setRotation((float)0);
            comanderTwoBase.setRotation((float)0);
            comanderTwoDepot.setRotation((float)0);
          //make sure that player 2's credit view is aligned with the edge of the screen
            comanderTwoCreditsView.setPadding(75,0,0,0);
            
            for (int x = 0; x < 8; x++)
            {
                for (int y = 0; y < 9; y++)
                {
                    gameGridButtons[x][y].setRotation((float)0);
                }
            }
        }
        
        if (driver.getState().getCurrentCommanderID()==LegatusState.COMMANDER_1_TURN)
        {
            masterGrid.setRotation((float)0);
            boardGrid.setRotation((float)180);
            comanderOneCreditsView.setRotation((float)180);
            comanderTwoCreditsView.setRotation((float)180);
            comanderOneBase.setRotation((float)180);
            comanderOneDepot.setRotation((float)180);
            comanderTwoBase.setRotation((float)180);
            comanderTwoDepot.setRotation((float)180);
            //make sure that player 2's credit view is aligned with the edge of the screen
            comanderTwoCreditsView.setPadding(0,0,80,0);
            for (int x = 0; x < 8; x++)
            {
                for (int y = 0; y < 9; y++)
                {
                    gameGridButtons[x][y].setRotation((float)180);
                }
            }
        }  
    }
    //====================ANIMATION METHODS======================
    
    //animates the attacking unit
    public void launchAttack(int x, int y)
    {
        //if the unit launching the attack is debris, animate it as if
        //it were dying
        if(driver.getState().getUnitAt(x, y).getUnitID()==Unit.DEBRIS)
        {
            registerHitSquare(x, y, true, 0);
        }
        
        //make sure the button appears on TOP of the other buttons and then
        //set it to do the animation
        gameGridButtons[x][y].bringToFront();
        Animation unitAttack;
        if (y<4)
        {
            unitAttack = AnimationUtils.loadAnimation(ourContext, R.animator.unit_attack_top);
        }
        else
        {
            unitAttack = AnimationUtils.loadAnimation(ourContext, R.animator.unit_attack_bottom);
        }
        gameGridButtons[x][y].startAnimation(unitAttack);
        gameGridButtons[x][y].setBackgroundColor(Color.argb(255, 120, 0, 120));
    }
    
    /**
     * registerHitSquare()
     * 
     * @param x: x coordinate of the attack
     * @param y: y coordinate of the attack
     * @param unitKilledHere: whether a unit died here or not
     * @param sequence: the order in which this square will be attacked over.
     */
    public void registerHitSquare(int x, int y, boolean unitKilledHere, int sequence)
    {
        //create a handler to delay the color changing and animation of squares that
        //are being passed over as an attack
            Handler handles = new Handler();
            handles.postDelayed(new attackOverSquare(x,y,unitKilledHere), sequence*300);
            handles.postDelayed(new attackOverSquareDone(x,y), sequence*300+300);
    }
    
    //called at the end of a turn, this rotates the view by 180*
    public void rotateView()
    {
        Animation spin180 = AnimationUtils.loadAnimation(ourContext, R.animator.spin180);
        masterGrid.startAnimation(spin180);
        Animation spin180opposite = AnimationUtils.loadAnimation(ourContext, R.animator.spin180opposite);
        boardGrid.startAnimation(spin180opposite);
        //make the views stay rotated
        updateRotations(driver.getState());
    }
    
    /**
     * 
     * 
     */
    public void showError()
    {
        errorText.setAlpha(255);
        if(driver.getState().getCurrentCommanderID()==LegatusState.COMMANDER_1_TURN)
        {
            errorText.setRotation(180);
        }
        else
        {
            errorText.setRotation(0);
            
        }

        fadeHandler.postDelayed(new hideErrorText(),1500);
        updateBoard(driver.getState(),false);

    }
    
    private class hideErrorText implements Runnable
    {
        public void run()
        {
            errorText.setAlpha(0);
        }
    }
    
    //when run, this class changes the color of the square, and if a unit was killed there
    //begins the animation to show its death
     private class attackOverSquare implements Runnable
    {
        int col;
        int row;
        boolean death;
        
        public attackOverSquare(int x, int y, boolean unitKilledHere)
        {
            col = x;
            row = y;
            death = unitKilledHere;
        }
        
        public void run()
        {
            //if a unit was killed at this location, set it to a dark purple
            gameGridButtons[col][row].setBackgroundColor(Color.argb(255, 170, 0, 170));
            if (death)
            {
                //if a unit was killed at this location, set it to a dark purple
                gameGridButtons[col][row].setBackgroundColor(Color.argb(255, 170, 0, 170));
                Animation unitDeath = AnimationUtils.loadAnimation(ourContext, R.animator.unit_death);
                gameGridButtons[col][row].startAnimation(unitDeath);

            }
        }
    }
    
    //run immediately after attackOverSquare, this just resets the color back to normal
    private class attackOverSquareDone implements Runnable
    {
        int col;
        int row;
        boolean death;
        
        public attackOverSquareDone(int x, int y)
        {
            col = x;
            row = y;
        }
        
        public void run()
        {
            //return the square to its original color
            if (row<4)
            {
                gameGridButtons[col][row].setBackgroundColor(Color.BLUE);
            }
            else
            {
                gameGridButtons[col][row].setBackgroundColor(Color.RED);
            }


        }
    }
}// class