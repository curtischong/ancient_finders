import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class TitleScreen extends World
{
    public TitleScreen()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1000, 600, 1);
        Title title = new Title();
        addObject(title,500,200);
        TitleText titleText = new TitleText();
        addObject(titleText,450,450);
    }
    public void act(){
        if(Greenfoot.isKeyDown("enter")){
            Greenfoot.setWorld(new MyWorld());
        }
    }
}
