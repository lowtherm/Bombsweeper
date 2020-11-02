import java.util.*;

/**
 * This class extends GameSquare and calls methods when the user left clicks, right clicks or reveals blanks squares.
 * This class completes the Bombsweeper game and so is final. 
 * 
 * 
 * @author Michael Lowther
 */

public final class BombSquare extends GameSquare 
{ 
    private GameBoard board;                            // Object reference to the GameBoard this square is part of.
    private boolean hasBomb;                            // True if this squre contains a bomb. False otherwise.

    public static final int MINE_PROBABILITY = 10;
	
    private int noOfMines = 0;					//Variable which stores the number of mines surrounding this square.
	private boolean visible = false;			//Refers to whether or not the square is visible to the user.
    private boolean flagged =false;             //Reders to whether or not the square has been flagged by the user.

	
	public BombSquare(int x, int y, GameBoard board) 
    {
		super(x, y, "images/blank.png");

        this.board = board;
		this.hasBomb = ((int) (Math.random() * MINE_PROBABILITY)) == 0;
	}

    /**This method is used to count the number of mines adjacent to this square and display it on the square as an image.
     *It first runs a loop from -1 to 1 which checks a 3x3 square around this square for bombs. If there's a bomb we increment the counter.
     *It then sets the image to be the number of mines and if there are 0 mines then it triggers the blank finder to reveal all other blanks connected.
     */
	private int minesNearby() 
    {
		for (int i = -1; i <= 1; i++) 
        {
			for (int j = -1; j <= 1; j++) 
            {
				if (board.getSquareAt(getXLocation() + i, getYLocation() + j) != null) 
                {
					BombSquare nearbySquare = (BombSquare) board.getSquareAt(getXLocation() + i, getYLocation() + j);
					if (nearbySquare.hasBomb) 
                    {
						noOfMines++;
					}
				}
			}
		}
		if(noOfMines == 0)
        {
			blankFinder();
		}
		setImage("images/" + noOfMines + ".png");
		return noOfMines;
	}

    /**This runs a loop from -1 to 1 that checks all adjacent squares to this one. 
     *The method uses an object called nearbySquare to store each nearbySquare and if they are not visible and have 
     *no bomb then they become visible and mines nearby is called by that square which calls blankFinder if another blank is 
     *found thus revealing all connected blanks and all numbered squares adjacent to those blanks.
     */
	public void blankFinder() 
    {
		for (int i = -1; i <= 1; i++) 
        {
			for (int j = -1; j <= 1; j++) 
            {
				if (board.getSquareAt(getXLocation() + i, getYLocation() + j) != null) 
                {
					BombSquare nearbySquare = (BombSquare) board.getSquareAt(getXLocation() + i, getYLocation() + j);
					if (!nearbySquare.visible && !nearbySquare.hasBomb) 
                    {
						nearbySquare.visible = true;
						nearbySquare.minesNearby();
					}
				}
			}
		}
	}

    /**This Overrides the leftClicked method in GameSquare.
     *When the user left clicks this makes sure the square clicked is both not yet visible and not flagged.
     *If both conditions are met then it checks if the square has a bomb.
     *If the square has a bomb it reveals it by setting the image to the bomb.
     *If it isn't a bomb it runs the minesNearby method to change the image to display the number of adjacent bombs.
     *It then makes the visibe variable true as the square has been made visible.
     */
	public void leftClicked()
    {
		if (visible == false && flagged == false) 
        {
			if (hasBomb) 
            {
				setImage("images/bomb.png");
			} 
            else 
            {
				minesNearby();
			}
			visible = true;
		}
	}

    /**This Overrides the rightClicked method in GameSquare.
     *This flags a square if it both has not been revealed and is not already flagged.
     *If it is already flagged then the flag is removed.
     */
    public void rightClicked()
    {
        
		if (visible == false) 
        {  
         if (flagged == false)
         {
		    setImage("images/flag.png");
            flagged = true;
         }
         else
         {
            setImage("images/blank.png");
            flagged = false;
         }	
		}
	}
}