import greenfoot.*;

public class AllyArrow extends SmoothMover
{
    //after 150 acts, the arrow will die
    private int lifespan = 150;
    public AllyArrow(int direction){
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
        Enemy enemy = (Enemy)getOneIntersectingObject (Enemy.class);
        if(enemy!= null){
            //deal 25 damage to the enemy that it hits
            enemy.damageMe(25);
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
