import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
public class AllyKnight extends Ally
{
    private GreenfootImage myImage;
    //variable for attack cooldown
    private int attackState;
    public AllyKnight(){
        //health of knight
        health = 60;
        //set its mining state (0 = attack on once it colloids if inbetween 1-10 then it's on cooldown and doesn't do anything)
        attackState = 0;
        //movement speed
        speed = 0.45;
    }

    public void act() 
    {
        //check if the user has selected/deselected it
        clickCheck();
        //tell it to move to it's assigned coords
        moveTo();
        //locate nearest emeny to attack (internal ai)
        findNearestEnemy();
        //see if it is alive
        checkAlive("AllyKnight");
    }

    //sets its image to the selected image and adds it to the global "selected units" arraylist
    public void selectMyself(){
        myImage = new GreenfootImage ("ally-knight-selected.png");
        this.setImage(myImage);
        MyWorld m = (MyWorld)getWorld();
        m.pushSelectedUnits(this);
    }

    //sets its image to the default unselected image and removes it from the global "selected units" arraylist
    public void deselectMyself(){
        myImage = new GreenfootImage ("ally-knight.png");
        this.setImage(myImage);
        MyWorld m = (MyWorld)getWorld();
        m.deselectUnits(this);
    }

    private void findNearestEnemy(){
        //see if it is touching the enemy
        Enemy currentEnemy = (Enemy)getOneIntersectingObject (Enemy.class);
        //if an enemy is found
        if (currentEnemy != null){

            //tell the miner that it is being attacked if it's a miner
            if(currentEnemy instanceof EnemyMiner){
                //I see the miner is already running away so I can avoid stacking the speeds of the miner (miner will move twice as fast away from enemies if the target method is called twice)
                if(((EnemyMiner)currentEnemy).returnTargeted() == false){
                    ((EnemyMiner)currentEnemy).isTargetted();
                }
            }

            //if the attack state is 0 then mine one gold
            if(attackState == 0){
                //damage the enemy by 1 health point
                currentEnemy.damageMe(1);
                //start the cooldown
                attackState++;
                //otherwise increase the attack attack state (this is used to delay the interaval between attacks)
            }else{
                //the attack state is "on cooldown"
                if(attackState < 10){
                    attackState++;

                    //if the arrack stte is 10 then set it back to zero where the cycle will restart
                }else{
                    attackState = 0;
                }
            }
        }else{
            //the knight looks for an enemy that is within 50 px from it
            ArrayList closeEnemy  = (ArrayList)getObjectsInRange(120, Enemy.class);
            //the knight looks for boffs that is within 50 px from it
            ArrayList closeBuff = (ArrayList)getObjectsInRange(50, Buff.class);
            if(closeEnemy.size() > 0 && this.selectedMove == false){
                //set the first one as my target
                MyWorld m = (MyWorld)getWorld();
                Enemy targetEnemy = (Enemy)m.getClosest(this,closeEnemy);

                //tell the miner that it is being attacked
                if(targetEnemy instanceof EnemyMiner){
                    //I see the miner is already running away so I can avoid stacking the speeds of the miner (miner will move twice as fast away from enemies if the target method is called twice)
                    if(((EnemyMiner)targetEnemy).returnTargeted() == false){
                        ((EnemyMiner)targetEnemy).isTargetted();
                    }
                }
                //the knight moves towards the enemy if the user hasn't commanded it to go somewhere else or it it is currently attacking an enemy.
                this.turnTowards(targetEnemy.getX(),targetEnemy.getY());
                this.move(0.45);
            }//the knight looks for buffs that is within 50 px from it
            else if(closeBuff.size() > 0 && this.selectedMove == false){
                //turn towards the coord of the the buff
                this.turnTowards(((Buff)closeBuff.get(0)).getX(),((Buff)closeBuff.get(0)).getY());
                //move towards the buff
                this.move(0.4);
            }
        }
    }
}