package game;

import java.util.ArrayList;
import java.util.Arrays;

import java.lang.Math;
import java.util.Random;

import elements.*;
import player.Astar;

public class RicochetRobots
{
    public RicochetRobots()
    {
        for(int i = 0; i < 16; i++)
            for(int j = 0; j < 16; j++)
                this.board[i][j] = new Case(); // Creation of each cases of this grid
        for(int[] walls : this.wallsPos) 
            this.board[walls[0]][walls[1]].setWalls(Arrays.copyOfRange(walls,2,6)); // Creation of walls first inside then on the limit of the board.
        for(int i = 0; i < 16; i++) 
        {
            this.board[i][0].setWall(0,1);
            this.board[15][i].setWall(1,1);
            this.board[i][15].setWall(2,1);
            this.board[0][i].setWall(3,1); 
        } // Creation of delimitation of the grid by walls
        this.placeRobots(this.seed); // Place where robots are placed
    }

    public ArrayList<Move> getPath() // To get a list of movement
    {
        Astar pathfinder = new Astar(this);
        return pathfinder.choosePath();
    }

    public void moveRobot(Move mv){this.robotsPos[mv.getRobot()] = mv.getPos();} // Position of robots

    public ArrayList<Move> getAllMoves(int[][] state, Move end, Move parent) // Study all moves possibles by the robot
    {
        ArrayList<Move> moves = new ArrayList<Move>();
        for(int i = 0; i < 4; i++) // for each robot
        {
            for(int j = 0; j < 4; j++) // for each directions
            {
                Move res = this.getDirectionPos(i, state[i], state, j, end, parent);
                if(res != null)
                    moves.add(res);
            }
        }
        return moves;
    }

    public Move getDirectionPos(int robot, int[] pos, int[][] state, int direction, Move end, Move parent) // Create a move in function between start position and direction
    {
        int x = pos[0], y = pos[1];
        int[] newPos = new int[2];
        int[] v = {(direction%2)*(direction == 3 ? -1 : 1), ((direction+1)%2)*(direction == 0 ? -1 : 1)};
        while(this.board[x][y].isCrossable(direction) && isOccupied(state, x+v[0], y+v[1]))
        { 
            x += v[0];
            y += v[1];
        }
        newPos[0] = x;
        newPos[1] = y;
        return state[robot][0] != newPos[0] || state[robot][1] != newPos[1] ? new Move(robot, newPos, parent.getCost()+this.manhattanDist(newPos, end.getPos()), this.rookDistances[robot == end.getRobot() ? newPos[1]*16+newPos[0] : state[end.getRobot()][1]*16+state[end.getRobot()][0]], this.updatedRobotsState(robot, state, parent.getPos()), parent) : null;
    }

    public int manhattanDist(int pos[], int pos1[])
    {
        return Math.abs(pos1[0]-pos[0]) + Math.abs(pos1[1]-pos[1]);
    }

    public boolean isOccupied(int[][] positions, int x, int y) // To return if the position is occupied or void
    {
        boolean isOccupied = true;
        for(int i = 0; i < 4; i++)
            isOccupied = isOccupied && (positions[i][0] != x || positions[i][1] != y);
        return isOccupied;
    }

    public int[][] updatedRobotsState(int robot, int[][] state, int[] parentPos)
    {
        int[][] newRobotsState = new int[4][2];
        for(int i = 0; i < 4; i++)
        {
            newRobotsState[i] = Arrays.copyOf(state[i], 2);
        }
        newRobotsState[robot] = parentPos.clone();
        return newRobotsState;
    }

    public int[] getRobotPos(int robot) {return this.robotsPos[robot];} // Return the number of robots

    public Case[][] getBoard(){return this.board;} // Return the board

    public boolean in(int[][] tab, int x, int y) //
    {
        boolean res = true;
        for(int[] i: tab)
            res = res && (i[0] != x || i[1] != y);
        return res;
    }

    public void placeRobots(long seed) // To place again robots at one specific positions 
    {
        Random generator = new Random(seed);
        int[][] forbiddenCoords2 = new int[4][2];
        ArrayList<Integer> forbiddenCoords = new ArrayList<Integer>();
        forbiddenCoords.add(7);
        forbiddenCoords.add(8);
        for(int i = 0; i < 4; i++)
        {
            int x, y;
            do {
                x =Math.abs(generator.nextInt()%16);
                y =Math.abs(generator.nextInt()%16);               
            } while(forbiddenCoords.contains(x) && forbiddenCoords.contains(y) && in(forbiddenCoords2, x, y));
            this.robotsPos[i] = new int[]{x,y};
        }
    }

    public void updateRookDistances(int[] flag)
    {
        int[] distances = new int[256], helper = new int[]{-16, 1, 16, -1};
        boolean[] caseDone = new boolean[256];
        int depth, index;
        boolean done = false;
        for(int i = 0; i < 256; i++)
        {
            distances[i] = 1000;
            caseDone[i] = false;
        }
        distances[16*flag[1]+flag[0]] = 0;
        caseDone[16*flag[1]+flag[0]] = true;
        while(!done)
        {
            done = true;
            for(int i = 0; i < 256; i++)
            {
                if(!caseDone[i])
                    continue;
                caseDone[i] = false;
                depth = distances[i] + 1;
                for(int direction = 0; direction < 4; direction++)
                {
                    index = i;
                    while(this.board[index%16][(int) (index/16)].isCrossable(direction))
                    {
                        index += helper[direction];
                        if(distances[index] > depth)
                        {
                            distances[index] = depth;
                            caseDone[index] = true;
                            done = false;
                        }
                    }
                }

            }
        }
        this.rookDistances = distances;
    }

    public void setFlag(int[] pos, int robot)
    {
        if(this.flag != null){
            this.board[this.flag[0]][this.flag[1]].removeFlag();
        }
        this.flag = pos;
        this.board[this.flag[0]][this.flag[1]].setFlag(robot);
        this.updateRookDistances(pos);
    }

    public void changeFlagColor(int robot){ // Change the flag's color in function of the robot's colors choosen 
        this.board[this.flag[0]][this.flag[1]].setFlag(robot);
    }

    public long getSeed(){return this.seed;} 

    public int[][] getRobotPositions(){return this.robotsPos;} // Return the prosition of robots

    public Move getFlag(){return new Move(this.board[this.flag[0]][this.flag[1]].getFlagRobot(), this.flag);}

    protected Case board[][] = new Case[16][16]; // Number of cases of the grid
    protected int[][] wallsPos = {{10,2,1,0,0,0},{9,1,0,1,0,0},{0,6,1,0,0,0},{5,0,0,1,0,0},{6,0,0,0,0,1},{8,0,0,1,0,0},{9,0,0,0,0,1},{10,1,0,0,1,1},{1,2,0,0,1,0},{12,2,0,1,1,0},{0,3,0,1,0,0},{1,3,1,0,0,1},{3,6,0,0,1,1},{3,10,0,0,0,1},{11,9,0,0,0,1},{14,6,0,0,0,1},{12,3,1,0,0,1},{6,5,1,0,0,0},{11,3,0,1,0,0},{6,4,0,1,1,0},{7,4,0,0,0,1},{15,4,0,0,1,0},{0,5,0,0,1,0},{13,2,0,0,0,1},{2,5,0,0,1,0},{7,5,0,1,0,0},{8,5,0,0,1,1},{13,5,0,0,1,0},{15,5,1,0,0,0},{0,6,1,0,0,0},{2,6,1,1,0,0},{7,6,0,0,1,0},{8,6,1,0,1,0},{13,6,1,1,0,0},{3,7,1,0,0,0},{6,7,0,1,0,0},{9,7,0,0,0,1},{6,8,0,1,0,0},{9,8,0,0,0,1},{1,9,0,1,0,0},{2,9,0,0,1,1},{7,9,1,0,0,0},{8,9,1,0,0,0},{10,9,0,1,1,0},{2,10,1,1,0,0},{7,10,0,0,1,0},{10,10,1,0,0,0},{12,10,0,0,1,0},{6,11,0,1,0,0},{7,11,1,0,0,1},{5,15,1,0,0,0},{12,11,1,1,0,0},{13,11,0,0,1,1},{0,12,0,0,1,0},{9,12,0,0,1,0},{13,12,1,0,0,0},{15,12,0,0,1,0},{0,13,1,0,0,0},{8,13,0,1,0,0},{9,13,1,0,0,1},{15,13,1,0,0,0},{5,14,0,1,1,0},{6,14,0,0,0,1},{6,15,0,1,0,0},{7,15,0,0,0,1},{10,15,0,1,0,0},{11,15,0,0,0,1}};
    protected int[][] robotsPos = new int[4][2];
    protected int[] flag;
    protected int[] rookDistances;
    protected long seed = (long) (Math.random()*1500000);
}
