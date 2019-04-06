package com.example.prady.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.GridLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Basic Tic Tac Toe game
 * This app uses basic widgets animations.
 * Developed by Prady. on 24/03/2019.
 */
public class GameActivity extends AppCompatActivity {

    private boolean mRedPlayer;      //mRedPlayer : true-> turn of Red Player : false-> turn of Yellow Player
    private int gameArray[][];      //This 3x3 2d array stores the positions filled by Red and Yellow Player: 0->unfilled : 1->Red : 2-> Yellow

    private int[] mPlayerWin;       //This array stores no. of times each player has won : mPlayerWin[0] ->Red Player : mRedPlayer[1] -> Yellow Player
    private String mPlayer[] = {"RED" , "YELLOW"}; //This array stores each player's name . :[0]-> RED : [1] -> YELLOW
    private boolean gameActive;  //this states whther game is active or not;

    private TextView[] mPlayerScoreTextView;  //This array stores textView references for each player's score : [0] -> Red : [1] -> Yellow

    private GridLayout gameGrid;            //This stores the reference for the Game Grid.
    private LinearLayout mCurrentPlayerLinearLayout;
    private ImageView mCurrentPlayerImageView;

    private byte mMoves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mRedPlayer = true; //First turn is of Red Player
         gameGrid = (GridLayout) findViewById(R.id.GameGrid); //Reference of game arena or grid
        mCurrentPlayerLinearLayout = findViewById(R.id.currentPlayerLinearLayout);
        mCurrentPlayerImageView = findViewById(R.id.currentPlayerImageView);
        mCurrentPlayerImageView.setImageResource(R.drawable.redcoin);
        mPlayerWin = new int[2];
        gameArray = new int[3][3];  // default is 0
        gameActive = true;        //game is active
        mPlayerScoreTextView = new TextView[2];  //TextView Score for each player
        mPlayerScoreTextView[0] = findViewById(R.id.redScoreTextView);    //For Red Player
        mPlayerScoreTextView[1] = findViewById(R.id.yellowScoreTextView); //For Yellow Player

        mMoves = 0;
    }

    public void dropIn(View view)
    {
        ImageView coin = (ImageView)view;  //Gets the ImageView object that called this function
        String tag = coin.getTag().toString();  // Gets Tag of imageView : Tag -> row and col no. of the object
        int i = tag.charAt(0) - '0';           // Row no.
        int j = tag.charAt(1) -'0';            //Col. no.
        if(gameActive && gameArray[i][j] == 0)  // If game is Active and clicked space is un-filled i.e., gameArray[i][j] = 0
        {
            mMoves++;
            if(mRedPlayer)     //if Red player was playing
            {
                mRedPlayer = false; // Next move will be played by Yellow player
                coin.setImageResource(R.drawable.redcoin); //Image Resource for red player is set to imageView
                gameArray[i][j] = 1; //Grid position is set to RED PLAYER i.e., 1
                //Log.i("GameMove",tag+" : "+1);  //A log msg for debugging
            }
            else //IF Yellow player was playing
            {
                mRedPlayer = true;  // Now Red Will play
                coin.setImageResource(R.drawable.yellowcoin); //Image Resource for Yellow Player is set to imageView
                gameArray[i][j] = 2; //GRid position is set to Yeloow Player i.e., 2
                Log.i("GameMove", tag+" : "+2); // A log msg for debugging
            }

            coin.setTranslationY(-1500);  //ImageView is translated by -1500Y ; To translate it back .ANIMATION
            coin.animate().translationYBy(1500).rotation(3600).setDuration(400); // First Translate back with rotation in 400 milliseconds.

            checkWin(); //Check if Game is won by someone or not.
            if(gameActive)
            {
                if(mRedPlayer)
                    mCurrentPlayerImageView.setImageResource(R.drawable.redcoin);
                else
                    mCurrentPlayerImageView.setImageResource(R.drawable.yellowcoin);

            }
        }
    }

    private void checkWin()
    {
        /**
         * This method checks whether game is won by any player or not.
         * Checks all possible Winning patterns i.e., 8 patterns are checked
         * winner holds 0 if noone won, 1 if Red has won and 2 if Yellow has won
         */
        int winner = 0;  //sets to 0 for checking
        for(int i=0;i<3;i++) // Checks row-wise and columns -wise winning patterns
        {
            if((gameArray[i][0] == gameArray[i][1])&& (gameArray[i][0] == gameArray[i][2]) )
            {  // if Row-wise win
                winner = gameArray[i][0]; //sets winner to Winner value
                break; //gets out of the loop
            }
            else if((gameArray[0][i] == gameArray[1][i])&& (gameArray[0][i] == gameArray[2][i]))
            {  //if col- wise win
                winner = gameArray[0][i];  // sets winner to Winner value
                break;  //gets out of the loop
            }
        }
        if(winner == 0)  //If no win row/col wise thenc heck diagonally
        {
            if( ((gameArray[0][0] == gameArray[1][1])&& (gameArray[0][0] == gameArray[2][2])) || ((gameArray[0][2] == gameArray[1][1])&& (gameArray[0][2] == gameArray[2][0])) )
            {
                winner = gameArray[1][1]; //sets winner to Winner value
            }
        }
        if(winner != 0)  //If someone has won :
        {
            mCurrentPlayerLinearLayout.setVisibility(View.INVISIBLE);
            mPlayerWin[winner-1]++; // winner : 1-> RED :2 -> YELLOW & this[0] ->RED : this[1] -> YELLOW
            //Log.i("Game","Player: "+winner);  // For debugging
            Toast.makeText(getApplicationContext(),mPlayer[winner-1]+" has won", Toast.LENGTH_SHORT).show(); //Toast appears showing who won
            gameActive = false;  //sets game to inactive state
            TextView winnerTextView = findViewById(R.id.WinnerView);       //gets Reference to textView showing who won
            Button newGameButton = findViewById(R.id.newGameButton);       //gets reference to new game Button
            winnerTextView.setVisibility(View.VISIBLE);                    //sets winnerView to visible
            newGameButton.setVisibility(View.VISIBLE);                     // sets newGameButton to visible
            winnerTextView.setText(mPlayer[winner - 1]+ " has won");       //sets text stating winner
            mPlayerScoreTextView[winner-1].setText(""+mPlayerWin[winner-1]);  // updates Winner's score
        }
        else
        {
            // check if all positions are filled and noone won
            if(mMoves == 9)
            {
                mCurrentPlayerLinearLayout.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"T I E :)(:", Toast.LENGTH_SHORT).show(); //Toast appears showing who won
                gameActive = false;  //sets game to inactive state
                TextView winnerTextView = findViewById(R.id.WinnerView);       //gets Reference to textView showing who won
                Button newGameButton = findViewById(R.id.newGameButton);       //gets reference to new game Button
                winnerTextView.setVisibility(View.VISIBLE);                    //sets winnerView to visible
                newGameButton.setVisibility(View.VISIBLE);                     // sets newGameButton to visible
                winnerTextView.setText("T.I.E");
            }
        }

    }

    public void newGame(View view)
    {
        /**
         * This method is called by new game button.
         * Sets game state to active
         * Fills gameArray to zero. i.e., makes grid positions unfilled by setting them to 0
         * Sets imageView's image to null
         * makes nemGameButton and WinnerView to Invisible
         */
        gameActive = true; //sets game state to active
        mMoves = 0;     //sets moves to 0
        gameArray = new int[3][3];  //fills gameArray with 0's
        mRedPlayer = true;         //sets turn of Red player
        TextView winnerTextView = findViewById(R.id.WinnerView);
        Button newGameButton = findViewById(R.id.newGameButton);
        winnerTextView.setVisibility(View.INVISIBLE);
        newGameButton.setVisibility(View.INVISIBLE);

        mCurrentPlayerLinearLayout.setTranslationX(25);
        for(int i = 0; i<gameGrid.getChildCount();i++)
        {
            ImageView im = (ImageView) gameGrid.getChildAt(i);
            im.setImageDrawable(null);
        }
        mCurrentPlayerLinearLayout.setVisibility(View.VISIBLE);
        mCurrentPlayerImageView.setImageResource(R.drawable.redcoin);
    }
}
