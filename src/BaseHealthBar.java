import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
// import java.awt.Color;

public class BaseHealthBar extends Actor
{
    //declare health bar colour and the image.
    private Color myGreen = new Color (0, 255, 0);    
    private GreenfootImage myImage;

    public void setHealth(int amount){
        
        //this variable represents the percentage of health left
        double percentageRemaining = (double)amount/800.0;
        //this variable represents the actual number of health the base has
        int amountLeft = (int)(percentageRemaining*100);
        //I check to see if the health is over 1 (beucase if it's 0 or below, you can't draw the bar)
        if(amountLeft > 1){
            //sets the width and height ofthe bar
            myImage = new GreenfootImage(amountLeft,5);
            //give it colour
            myImage.setColor(myGreen);
            myImage.clear();
            //fill the bar and set the image
            myImage.fill();
            this.setImage(myImage);
        }
    }
}
