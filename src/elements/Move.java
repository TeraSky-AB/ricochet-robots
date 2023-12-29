package elements;


public class Move
{
    public Move(int robot, int[] move)  
    {
        this.coords = move;
        this.robot = robot;
    }

    public Move(int robot, int[] move, int[][] robotsState, Move parent) 
    {
        this.coords = move;
        this.robot = robot;
        this.robotsState = robotsState;
        this.parent = parent;
    }

    public Move(int robot, int[] pos, int cost, int heuristic, int[][] robotsState, Move parent)
    {
        this.robot = robot;
        this.coords = pos;
        this.cost = cost;
        this.heuristic = heuristic;
        this.robotsState = robotsState;
        this.parent = parent;
    }

    public int[] getPos() // return the current pos
    {
        return this.coords;
    }

    public int getRobot() {return this.robot;} // return the current robot

    public String toString()
    {
        return new String("Move to pos: ("+this.coords[0]+","+this.coords[1]+") by robot:"+this.robot)+" from pos: ("+this.robotsState[this.robot][0]+","+this.robotsState[this.robot][1]+") Cost: "+this.cost;
    }

    public boolean equals(Move mv, Boolean approx) // check if the both move are equals
    {
        return approx ? this.coords[0] == mv.getPos()[0] && this.coords[1] == mv.getPos()[1] && this.robot == mv.getRobot() : this.cost == mv.getCost() && this.robot == mv.getRobot() && this.robotsState[this.robot] == mv.getRobotsState()[mv.getRobot()] && this.coords == mv.getPos();
    }

    public boolean isGreaterThan(Move mv) // return true if the current move if better than other move
    {
        return this.cost+this.heuristic < mv.getCost()+mv.getHeuristic();
    }

    public int getCost(){return this.cost;} // return the cost of the move

    public int getHeuristic(){return this.heuristic;} // return the heuristic of the move 

    public Move getParent(){return this.parent;} // return parent of the move

    public int[][] getRobotsState(){return this.robotsState;} // return the State of the all robot

    protected int[] coords;
    protected int robot;
    protected int cost = 0;
    protected int heuristic = 0;
    protected Move parent= null;
    protected int[][] robotsState = null;
}
