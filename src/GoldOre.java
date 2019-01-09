import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
public class GoldOre extends Actor
{
    //initilise how much gold can be mined
    private int goldAmount = 30;
    //this method is called when an ally or enemy miner comes into contact with a gold ore.
    //there is a parameter for true or false to determine whether the unit touching it is an ally or enemy
    public void mineMe(boolean type){
        //check to see if it is all used up
        if(goldAmount == 0){
            getWorld().removeObject(this);
            //else, minus 1 from gold amount and add 1 gold to the respective team depending if an ally or enemy minded it.
        }else{
            MyWorld m = (MyWorld)getWorld();
            if(type == true){//this is an ally mining it
                goldAmount--;
                m.addAllyGold(1);
            }else{//this is an allyminer mining it.
                goldAmount--;
                m.addEnemyGold(1);
            }
        }
    }
}
