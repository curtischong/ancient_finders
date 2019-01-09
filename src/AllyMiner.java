import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
public class AllyMiner extends Ally
{
    private GreenfootImage myImage;
    //variable for mining cooldown
    private int miningState;
    public AllyMiner(){
        //health of miner
        health = 40;
        //set its mining state (0 = attack on once it colloids if inbetween 1-10 then it's on cooldown and doesn't do anything)
        miningState = 0;
        //movement speed
        speed = 0.4;
    }

    public void act() 
    {
        //check if the user has selected/deselected it
        clickCheck();
        //tell it to move to it's assigned coords
        moveTo();
        //locate nearest emeny to attack (internal ai)
        findNearestGold();
        //see if it is alive
        checkAlive("AllyMiner");
    }

    //sets its image to the selected image and adds it to the global "selected units" arraylist
    public void selectMyself(){
        miningState = 0;
        myImage = new GreenfootImage ("ally-miner-selected.png");
        this.setImage(myImage);
        MyWorld m = (MyWorld)getWorld();
        m.pushSelectedUnits(this);
    }

    //sets its image to the default unselected image and removes it from the global "selected units" arraylist
    public void deselectMyself(){
        myImage = new GreenfootImage ("ally-miner.png");
        this.setImage(myImage);
        MyWorld m = (MyWorld)getWorld();
        m.deselectUnits(this);
    }

    private void findNearestGold(){
        //see if the miner is touching gold
        GoldOre currentGold = (GoldOre)getOneIntersectingObject (GoldOre.class);
        //if it is touching gold
        if (currentGold != null){
            //if the mining state is 0 then mine one gold
            if(miningState == 0){
                //it will call the gold's mineme method which willremove 1 health from the gold ore and give the ally team 1 gold
                currentGold.mineMe(true);
                //mining state is increased so there's seperation between each mining operation
                miningState++;
            }else{
                //if the mining state is not 0 or 10, then increase its mining state and wait until it is ready to mine
                if(miningState < 10){
                    miningState++;
                    
                    //if the mining state is over 9 then set it back to zero to repeat the mining cycle
                }else{
                    miningState = 0;
                }
            }
        }else{
            //the miner looks for gold ore that is within 50 px from it
            ArrayList closeGoldOre = (ArrayList)getObjectsInRange(50, GoldOre.class);
            //the miner looks for boffs that is within 50 px from it
            ArrayList closeBuff = (ArrayList)getObjectsInRange(50, Buff.class);
            if(closeGoldOre.size() > 0 && this.selectedMove == false){
                //find closest enemy
                 MyWorld m = (MyWorld)getWorld();
                GoldOre targetGold = (GoldOre)m.getClosest(this,closeGoldOre);
                //the miner moves towards the gold ore if the user hasn't commanded it to go somewhere else or it it is currently mining the gold.
                this.turnTowards(targetGold.getX(),targetGold.getY());
                this.move(0.4);
                
            //the miner looks for buffs that is within 50 px from it
            }else if(closeBuff.size() > 0 && this.selectedMove == false){
                //turn towards the coord of the the buff
                this.turnTowards(((Buff)closeBuff.get(0)).getX(),((Buff)closeBuff.get(0)).getY());
                //move towards the buff
                this.move(0.4);
            }
        }
    }
}