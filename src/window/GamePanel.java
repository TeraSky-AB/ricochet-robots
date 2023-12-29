package window;

import javax.swing.JPanel;

import elements.Case;
import game.RicochetRobots;

import java.awt.Color;
import java.awt.Graphics;

public class GamePanel extends JPanel 
{
    private static final long serialVersionUID = 8473977696188518610L;

    public GamePanel(RicochetRobots game)
    {
        super();
        this.game = game;
    }

    public void paintComponent(Graphics g) 
    {
        Case[][] gameBoard = this.game.getBoard(); // Game board
        int[] walls;
        int x, y;
        Color[] colors = {new Color(240,128,128), new Color(154,205,50), new Color(173,216,230), new Color(238,232,170)}; // Flag's colors
        Color[] colors2 = {new Color(220, 20, 60), new Color(124,252,0), new Color(30,144,255), new Color(255,215,0)}; // Robot's color
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 16; j++)
            {
                x = i*(this.caseSide+this.margin)+10;
                y = j*(this.caseSide+this.margin)+10;
                int occupied = this.in(i,j,this.game.getRobotPositions()); // case occupied by the position of robots

                if(gameBoard[i][j].hasFlag()) {
                    g.setColor(colors2[gameBoard[i][j].getFlagRobot()]); // Color selection of the flag if present 

                } else {
                    g.setColor(new Color(227, 228, 228)); // Color selection if case is voi
                }
                g.fillRect(x, y, this.caseSide, this.caseSide); // Representation of one case
                if(occupied != -1)
                {
                    g.setColor(colors[occupied]); 
                    g.fillOval(x,y,this.caseSide,this.caseSide); // Representation of robots
                }
                walls = gameBoard[i][j].getWalls();
                g.setColor(Color.BLACK); // Color of walls
                for(int k = 0; k < 4; k++) // Creation of walls on the grid
                {
                    if(walls[k] == 1)
                    {
                        if(k == 0) {
                            g.drawLine(x, y, x+this.caseSide, y);
                        } else if(k == 1) {
                            g.drawLine(x+this.caseSide, y, x+this.caseSide, y+this.caseSide);
                        } else if(k == 2) {
                            g.drawLine(x, y+this.caseSide, x+this.caseSide, y+this.caseSide);
                        } else {
                            g.drawLine(x, y, x, y+this.caseSide);
                        }
                    }
                }
                if(gameBoard[i][j].hasFlag())
                    g.drawString("F", x+this.caseSide/2-3, y+this.caseSide/2+3); // To spot the flag of one robot
            }
        }
    }

    public int in(int x, int y, int[][] positions) // To know which robot is placed on the case
    {
        for(int i = 0; i < 4; i++)
            if(positions[i][0] == x && positions[i][1] == y)
                return i;
        return -1;
    }

    protected int caseSide = 30;
    protected int margin = 3;
    protected RicochetRobots game;
}
