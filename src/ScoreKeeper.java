import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
//import java.awt.Color;
//import java.awt.Font;

//this class basically updates the gold counts for both players

public class ScoreKeeper extends Actor
{
    //create the scoreboard
    private GreenfootImage scoreBoard;
    //iniitalise the colors of the image and the font
    private Color backgroundColor = new Color (108, 108, 108);
    private Color textColor = Color.WHITE;
    private Font textFont = new Font (true, true, 18);
    
    public ScoreKeeper()
    {
        //state size of image
        scoreBoard = new GreenfootImage(1000, 30);
        //set font
        scoreBoard.setFont (textFont);
        //set background color
        scoreBoard.setColor (backgroundColor);
        //fill the board up
        scoreBoard.fill();
        //place the image of the scoreboard ontop of the object
        this.setImage(scoreBoard);
    }

    //this method is used by external functions to reload the score of the gold count in the top bar
    public void setScore (int allyGold,int enemyGold)
    {
        //initiate look of the strings
        String allyGoldCount = "Your Gold: " + allyGold;
        String enemyGoldCount = "Enemy Gold: " + enemyGold;
        // erase everything in score and start again
        scoreBoard.clear();
        //set the colours
        scoreBoard.setColor (backgroundColor);
        scoreBoard.fill();
        scoreBoard.setColor (textColor);
        //insert the strings into the correct locations on the top bar
        scoreBoard.drawString (allyGoldCount, 50, 20);
        scoreBoard.drawString (enemyGoldCount, 800, 20);
    }
}