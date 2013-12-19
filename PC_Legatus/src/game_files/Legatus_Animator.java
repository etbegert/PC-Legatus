package game_files;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Legatus_Animator {
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
    
    Legatus_Engine gameEngine;
    private Button[][] gameGridButtons;
    private Button[] unitButtons;
    
    /**
     * @param ourDriver
     */
    public Legatus_Animator(Legatus_Engine gameEngine, Frame gameFrame)
    {
       
    }
    
    private void createBoardGrid(Frame gameFrame) {
    	gameGridButtons = new Button[5][8];
    	for(int column = 0; column < 5; column++)
    	{
    		for(int row = 0; row < 8; row++)
    		{
    			if(y != 4)
    			{
    				gameGridButtons[column][row] = new Button();
    				gameGridButtons[column][row]].setMinimumSize(new Dimension(BUTTONSIZE - 50, BUTTONSIZE - 50));
    				gameGridButtons[column][row].setSize(BUTTONSIZE, BUTTONSIZE);
    				if(row > 4)
    				{
    					gameGridButtons[column][row].setBackground(Color.RED);                	
    				}
    				if(row < 4)
    				{
    					gameGridButtons[column][row].setBackground(Color.BLUE);
    				}

    				gameGridButtons[column][row].addMouseListener(new MouseListener()
    				{
    					@Override
    					public void mouseClicked(MouseEvent arg0) {
    						// TODO Auto-generated method stub
    						gameEngine.processGridTouch(column, row):						
    					}

    					//TODO: UNNECESSARY?
    					@Override
    					public void mouseEntered(MouseEvent arg0) {
    						// TODO Auto-generated method stub

    					}

    					@Override
    					public void mouseExited(MouseEvent arg0) {
    						// TODO Auto-generated method stub

    					}

    					@Override
    					public void mousePressed(MouseEvent arg0) {
    						// TODO Auto-generated method stub

    					}

    					@Override
    					public void mouseReleased(MouseEvent arg0) {
    						// TODO Auto-generated method stub

    					}
    				});
    			}
    			else
    			{
    				gameGridButtons[column][row].setVisible(false);
    				gameGridButtons[column][row].setEnabled(false);
    				gameGridButtons[column][row].setSize(BUTTONSIZE, 30);
    			}
    			
    			gameFrame.add(gameGridButtons[column][row]);
        		
    		}//for row
    	}//for column
    	
    }
}
