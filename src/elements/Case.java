package elements;

public class Case 
{
    public Case(){} // Represent the board's cases 

    public void setWalls(int[] config)
    {
        this.walls = config;
    }

    public void setWall(int direction, int state)
    {
        this.walls[direction] = state;
    }

    public void removeFlag()
    {
        this.flagRobot = -1;
    }

    public void setFlag(int robot)
    {
        this.flagRobot = robot;
    }

    public int getFlagRobot(){return this.flagRobot;}

    public boolean hasFlag(){return this.flagRobot != -1;}

    public boolean isCrossable(int dir) {return this.walls[dir]==0;}

    public int[] getWalls() {return this.walls;}

    protected int[] walls = {0,0,0,0}; // NORTH, EAST, SOUTH, WEST
    protected int flagRobot = -1;
}