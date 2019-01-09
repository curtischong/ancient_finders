    import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
public class EnemyKnight extends Enemy
{
    private GreenfootImage myImage;
    private int attackState = 3;

    //270 means to tell the knight to face upwards when patrolling
    private int patrolDirection = 270;
    //0 = stay in one spot
    //1 = hunt miners
    //2 = patrol up and down
    private int action = 0;
    //this boolean states whether if the knight is guarding a miner or not
    private boolean guardingMiner = false;
    public EnemyKnight(){
        //initiate the health for the knight
        health = 60;
        //set the attack state to 0 (this determines whether if the knight is arracking (they attack in intervals not always))
        attackState = 0;
        //this is the speed that the knight travels at
        speed = 0.45;
    }

    public void act() 
    {
        //tell it to move to coords that the ai has told it to move to
        moveTo();
        //if it is patrolling
        findNearestAlly();
        //updates the variable that keeps track to see if they are guarding a miner
        updateGuardState();
        //the check alive function keeps track of the amount of enemy hunters by seeing it one died. The ai uses this to determine how should it use its knights (as hunters or not)
        if( action == 1){
            //tells the enemy fountain that a hunter has died
            checkAlive("EnemyKnightHunter");
        }else{
            //tells the enemy fountain that a knight has died
            checkAlive("EnemyKnight");
        }
    }

    //this method allows the ai to tell the knight to go and track ally miners to hunt them down
    public void goAttackMiners(){
        action = 1;
    }
    //this method sets a variable that tells the knight to move up and down the map and patrol land
    public void goPatrol(){
        action = 2;
    }
    //this method give the knight permission to go near any unprotected miners that were busy mining
    public void stayWithEnemyMiners(){
        action = 3;
    }
    //returns the current action of the knight
    public int getState(){
        return action;
    }
    //change its state so that it's guarding a miner
    public void isGuarding(){
        guardingMiner = true;
    }    
    //tells the miner if it is currently guarding something
    public boolean returnIsGuarding(){
        return guardingMiner;
    }

    //sees if it is still in the range of a miner to be considered "guarding it"
    private void updateGuardState(){
        ArrayList closeMiners  = (ArrayList)getObjectsInRange(120, EnemyMiner.class);
        //if these are no close miners and it isn't instructed to go somehwere then it is considered not guarding anyhting
        if(closeMiners.size() !=0 && moveX !=0){
            //this will aloow the miner to go and request this knight to guard it
            guardingMiner = false;
        }
    }
    
    //this method will look for the nearest Ally and will try to approach it. if it is already there, then deal damage to the ally unit
    private void findNearestAlly(){
        //see if the knight is touching an ally
        Ally currentAlly = (Ally)getOneIntersectingObject (Ally.class);
        //if it is touching na ally
        if (currentAlly != null){//if hte attack state is set to 0 (cooldown is over)
            if(attackState == 0){
                //deal 1 damage
                currentAlly.damageMe(1);
                attackState++;
            }else{
                //increase the cooldown variable
                if(attackState < 10){
                    attackState++;
                    //if it is at it's max, reset the cooldown variable
                }else{
                    attackState = 0;
                }
            }
            //the knight is not attacking and is therefore travelling
        }else{
            //the knight looks for an Ally that is within 50 px from it
            ArrayList closeAlly  = (ArrayList)getObjectsInRange(120, Ally.class);

            //finds all ally miners in the world
            MyWorld m = (MyWorld)getWorld();
            ArrayList allAllyMiners  = (ArrayList)getWorld().getObjects(AllyMiner.class);
            if(closeAlly.size() > 0 && this.selectedMove == false){
                //finds the closest ally and then tries to move towards it (confront and attack it)
                Ally targetAlly = (Ally)m.getClosest(this,closeAlly);
                //the knight moves towards the Ally if the ai hasn't commanded it to go somehwere else or it it is currently attacking and Ally.
                this.turnTowards(targetAlly.getX(),targetAlly.getY());
                this.move(0.45);
                //if it is turned to hunt miners, it will find the closest miner if there is no immediate threat
            }else if(allAllyMiners.size() > 0 && action == 1){

                Ally targetAlly = (Ally)m.getClosest(this,allAllyMiners);
                //the knight moves towards the Ally if the ai hasn't commanded it to go somehwere else or it it is currently attacking and Ally.
                this.turnTowards(targetAlly.getX(),targetAlly.getY());
                this.move(0.45);

                //if there is noone close by and this knight isn't hunting and if it is set to patrol (not stay)then move up and down the x and y
            }else if(action == 2 && selectedMove == false){
                this.setRotation(patrolDirection);
                if(this.getY() == 110){
                    //goes downward
                    patrolDirection = 90;
                }else if(this.getY() == 520){
                    //moves upward
                    patrolDirection = 270;
                }

                //give it momentum
                this.move(0.45);
                //if the knight hasn't been commanded to go somewhere and it has been told to follow and portect their miners, then they will stay with them
            }
        }
    }
}