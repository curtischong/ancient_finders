import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
//import java.awt.Color;
//import java.awt.Font;

//this class creates the text in the title screen that tells the user to press enter to start the game

public class TitleText extends Actor
{
    //set image variables
    private GreenfootImage myImage = new GreenfootImage(500,100);
    private Font textFont = new Font (true, true, 25);
    public TitleText (){
        //bind the variables to the image
        myImage.setFont (textFont);    
        myImage.setColor(Color.WHITE);
        //this is the image text
        myImage.drawString ("Press Enter to Start", 180,50);
        //set the image properties to the image
        this.setImage(myImage);
    }
}
