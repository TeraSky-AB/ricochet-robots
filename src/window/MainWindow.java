package window;

import java.awt.*;
import javax.swing.*;

import elements.Move;

import java.awt.event.*;
import java.util.ArrayList;

import game.RicochetRobots;

public class MainWindow extends JFrame implements ActionListener, MouseListener
{
    public MainWindow(RicochetRobots game)
    {
        // Settings of the window
        super("Ricochet Robots solver");
        this.game = game;
        this.setSize(800, 585);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.contentPane.setLayout(null);

        this.addGameFrame();

        // Size and settings of the ComboBox
        colorComboBox.setBounds(580, 65, 100, 20);

        // Size and setting of buttons
        this.astarButton.setBounds(580, 95, 150, 40);
        this.nextButton.setBounds(660, 160, 90, 35);
        this.lastButton.setBounds(560, 160, 90, 35);
        this.rezButton.setBounds(605, 200, 100, 35);
        this.okSeedButton.setBounds(705,512,45,25);
        this.okSeedButton.setFont(new Font("Arial", Font.PLAIN, 9));
        

        // Creation texts
        JLabel textColor1 = new JLabel();
        textColor1.setText("Flag's color:");
        textColor1.setBounds(580, 45, 200, 20);
        this.textPath.setText("No path generated..");
        this.textPath.setBounds(600, 130, 150, 30);
        this.textMoves.setText("Moves count: 0");
        this.textMoves.setBounds(610, 235, 150, 30);
        this.textSeed.setText("Current seed: "+this.game.getSeed());
        this.textSeed.setBounds(560,485,200,30);
        this.textMovesLeft.setText("Moves left: 0");
        this.textMovesLeft.setBounds(623, 255, 150, 30);
        this.textTime.setText("Time spent: 0ms");
        this.textTime.setBounds(615, 310, 3000, 20);

        this.enterSeedTextField.setBounds(560, 512, 140, 25);

        // Add of Listener
        colorComboBox.addActionListener(this);
        this.astarButton.addActionListener(this);
        this.nextButton.addActionListener(this);
        this.rezButton.addActionListener(this);
        this.okSeedButton.addActionListener(this);
        this.lastButton.addActionListener(this);

        // Add of contentPane
        this.contentPane.add(this.colorComboBox);
        this.contentPane.add(textColor1);
        this.contentPane.add(this.astarButton);
        this.contentPane.add(this.nextButton);
        this.contentPane.add(this.textPath);
        this.contentPane.add(this.textMoves);
        this.contentPane.add(this.rezButton);
        this.contentPane.add(this.textSeed);
        this.contentPane.add(this.enterSeedTextField);
        this.contentPane.add(this.okSeedButton);
        this.contentPane.add(this.textMovesLeft);
        this.contentPane.add(this.textTime);
        this.contentPane.add(this.lastButton);
    }

    public void addGameFrame()
    {
        this.gameFrame = new GamePanel(this.game);
        this.gameFrame.setBounds(0,0,540,548);
        this.gameFrame.addMouseListener(this);
        this.contentPane.add(this.gameFrame);
    }

    public int colorToInt(String color)
    {
        switch(color) // Color's setting of the flag objective
        {
            case "Red": 
                return 0;
            case "Blue": 
                return 2;
            case "Green": 
                return 1;
            case "Yellow": 
                return 3;
            default:
                return 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) { // Actions with evenements
        Object source = e.getSource();
        if(source == this.colorComboBox)
        {
            JComboBox src = (JComboBox) e.getSource();
            String boxSelected = (String) src.getSelectedItem();
            game.changeFlagColor(colorToInt(boxSelected));
            this.refresh();
        } else if(source == this.astarButton) {
            if(this.reinitialized) {
                this.path = null;
                long startTime = System.currentTimeMillis();
                this.path = this.game.getPath();
                this.textTime.setText("Time spent: "+((System.currentTimeMillis()-startTime))+"ms");
                this.textPath.setText(this.path == null ? "Path not found.." : "Path generated !");
                this.textMoves.setText("Moves count: "+ (this.path == null ? 0 : (this.path.size()-1)));
                this.textMovesLeft.setText("Moves left: "+ (this.path == null ? 0 : (this.path.size()-this.pathIndex)));
                this.reinitialized = this.path == null ? true : false;
            } else {
                this.textPath.setText("Path not reinitialized !");
            }
        } else if(source == this.nextButton) {
            if(this.path != null) {
                if(this.pathIndex == this.path.size())
                {
                    this.textPath.setText("End of path");
                    return;
                }
                Move mv = this.path.get(this.pathIndex);
                this.game.moveRobot(mv);
                this.textMovesLeft.setText("Moves left: "+ (this.path == null ? 0 : (this.path.size()-this.pathIndex-1)));
                this.pathIndex += this.path.size()-1 >= this.pathIndex ? 1 : 0;
                this.refresh();
            }   
        } 
        else if(source == this.lastButton) { 
            if(this.path != null) {
                if(this.pathIndex == 0)
                {
                    this.textPath.setText("Start of path");
                    return;
                }
                this.pathIndex -= this.path.size() >= this.pathIndex ? 1 : 0;
                Move mv = this.path.get(this.pathIndex);
                this.game.moveRobot(mv.getParent());
                this.textMovesLeft.setText("Moves left: "+ (this.path == null ? 0 : (this.path.size()-this.pathIndex-1)));
                
                this.refresh();
            }
        }else if(source == this.rezButton) {
            if(!this.reinitialized) {
                this.pathIndex = 1;
                this.path = null;
                this.textMovesLeft.setText("Moves left: 0");
                this.reinitialized = true;
                this.textPath.setText("Pathfinder reinitialized");
                this.textMoves.setText("Moves count: 0");
                this.textTime.setText("Time spent: 0ms");
            }
        } else if(source == this.okSeedButton) {
            long seedValue;
            try {
                seedValue = Long.parseLong(this.enterSeedTextField.getText());
                System.out.println("New seed: "+ seedValue);
                this.game.placeRobots(seedValue);
                this.refresh();
            } catch(NumberFormatException err) {;}
            
            
        }
    }

    public void refresh(){ 
        this.invalidate();
        this.revalidate();
        this.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) // Function for click evenement 
    {
        int x, y;
        x = e.getX();
        y = e.getY();
        int[] casePos = {(x-10)/33%16, (y-10)/33%16};
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            System.out.println("Flag changed to: ("+casePos[0]+","+casePos[1]+")");
            int color = this.colorToInt((String) this.colorComboBox.getSelectedItem());
            game.setFlag(casePos, color);
            this.refresh();
        } else if( e.getButton() == MouseEvent.BUTTON3) {
            System.out.println("("+casePos[0]+","+casePos[1]+")");
        }
        
    }

    // Use for our use but needed for the compilation(interface)
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    
    private static final long serialVersionUID = 1L;

    protected GamePanel gameFrame;
    protected String[] couleurs = {"Red", "Green", "Blue", "Yellow"}; // Choice the color in the drop down menu
    protected JComboBox colorComboBox = new JComboBox(this.couleurs); // Add drop down menu
    protected JButton astarButton = new JButton("FIND PATH !"); // Add new Button
    protected JLabel textPath = new JLabel();
    protected JLabel textMoves = new JLabel();
    protected JLabel textMovesLeft = new JLabel();
    protected JLabel textSeed = new JLabel();
    protected JLabel textTime = new JLabel();
    protected JButton nextButton = new JButton("Next step"); // Add new Button
    protected JButton lastButton = new JButton("Last step"); // Add new Button
    protected JButton rezButton = new JButton("Reinitialize"); // Add new Button
    protected JButton okSeedButton = new JButton("Ok"); // Add new Button
    protected JTextField enterSeedTextField = new JTextField("Enter seed here"); // Add new text zone
    protected RicochetRobots game;
    protected Container contentPane = this.getContentPane();
    protected ArrayList<Move> path;
    protected int pathIndex = 1;
    protected boolean reinitialized = true;
}
