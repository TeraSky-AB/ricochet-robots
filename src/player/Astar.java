package player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import elements.Move;
import game.RicochetRobots;


public class Astar {
    public Astar(RicochetRobots game) {
        this.game = game;
    }
    
    public ArrayList<Move> choosePath() // Main part of the astar algorithm
    {
        Move end = this.game.getFlag();
        Move startNeeded = new Move(end.getRobot(), this.game.getRobotPos(end.getRobot()), game.getRobotPositions(), null);
        Move start = new Move(end.getRobot(), this.game.getRobotPos(end.getRobot()), game.getRobotPositions(), startNeeded);
        this.openList.addAll(game.getAllMoves(this.game.getRobotPositions(), end, start));
        Move nextMove = this.openList.removeFirst();
        Move res;
        while(!openList.isEmpty())
        {
            res = this.handleMoves(nextMove, end);
            if(res != null)
                return this.createPath(res);
            nextMove = this.getBestMove();
        }
        System.out.println("No path found..");
        return null;
    }

    public ArrayList<Move> createPath(Move mv) // Recreate the path using node's parents 
    {
        ArrayList<Move> path = new ArrayList<Move>();
        path.add(mv);
        Move parent = mv.getParent();
        while(parent != null) 
        {
            path.add(parent);
            parent = parent.getParent();
        }
        Collections.reverse(path);
        path.remove(0);
        return path;
    }

    

    public Move handleMoves(Move mv, Move end) // Handle neighbor nodes
    {   
        this.openList.remove(mv);
        int[][] robotsState = mv.getRobotsState().clone();
        robotsState[mv.getRobot()] = mv.getPos().clone();
        ArrayList<Move> moves = this.game.getAllMoves(robotsState, end, mv);
        this.closedList.add(mv);
        for(Move move : moves)
        {
            if(move.equals(end, true))
            {
                return move;
            } else if(this.closedList.contains(move)) {
                continue;
            } else if(this.openList.contains(move)) {
                int index = this.containsGreater(this.openList, move);
                if (index != -1)
                {   
                    this.openList.set(index, move);
                } else {
                    this.openList.add(move);
                } 
            } else {
                this.openList.add(move);
            }
        }
        return null;
    }

    public int containsGreater(List<Move> moves, Move move) // Check which move is the best on the list given
    {
        Move mv;
        for(int i = 0; i < moves.size(); i++)
        {
            mv = moves.get(i);
            if(move.equals(mv, false) && move.isGreaterThan(mv))
                return i;
        }
        return -1;
    }

    public Move getBestMove() // return the best move 
    {
        if(this.openList.isEmpty()) 
        {
            System.out.println("Nothing in open list..");
            return null;
        }
        Move minMove = this.openList.get(0);
        for(Move mv : this.openList)
        {
            if(mv.isGreaterThan(minMove))
                minMove = mv;
        }
        return minMove;
    }
    protected RicochetRobots game;
    protected ArrayList<Move> closedList = new ArrayList<Move>();
    protected LinkedList<Move> openList = new LinkedList<Move>();
    protected int[] rookDistances;
}
