import greenfoot.*;

public class EnemyArrow extends SmoothMover
{
    //after 150 acts, the enemy arrow will die
    private int lifespan = 150;
    public EnemyArrow(int direction){
        //this initialises the direction the arrow will travelling in
        this.setRotation(direction);   
    }
    public void act() 
    {
        //this tells the arrow how fast it should be travelling at
        move(1.5);
        //every act, it reduces the lifespan of the arrow
        lifespan --;
        
        //see if the arrow is hitting an ally.
        Ally ally = (Ally)getOneIntersectingObject (Ally.class);
        if(ally!= null){
            //deal 25 damage to the ally that it hits
            ally.damageMe(25);
            //set lifespan to zero which will kill off arrow
            lifespan = 0;
        }
        
        //when the lifespan of the arrow is zero, the arrow has "died" (reached its range)
        //or it has touched an enemy and must be removed from the world
        if(lifespan == 0){
            //remove the arrow from the world
            getWorld().removeObject(this);
        }
    }    
}
