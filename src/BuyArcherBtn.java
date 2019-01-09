import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
//import java.awt.Color;
//import java.awt.Font;
public class BuyArcherBtn extends Actor
{
    // Declare variables that set the overall look of the button

    private GreenfootImage myImage;
    private Color backgroundColor = new Color (0, 0, 0);
    private Color textColor = new Color (176,0,0);
    private Font textFont = new Font (true, true, 12);

    //initilise the button's background color, then change it,then fill in the text. I also change the button's background color here.
    public BuyArcherBtn(){
        myImage = new GreenfootImage(60, 30);
        myImage.setFont (textFont);
        myImage.setColor (backgroundColor);    
        myImage.clear();
        myImage.fill();
        myImage.setColor(Color.WHITE);
        myImage.drawString ("Buy Archer \n    (150g)", 0, 12);
        this.setImage(myImage);

    }

    //every act the button checks if it has been clicked. if it has then it'll tell the world that it needs to buy a miner
    public void act ()
    {
        if (Greenfoot.mousePressed(this))
        {
            MyWorld m = (MyWorld)getWorld();
            m.buyAllyArcher();
        }
    }   
}
