import game.*;
import window.MainWindow;

public class Main
{
    public static void main(String[] args) {
        RicochetRobots game = new RicochetRobots();
        game.setFlag(new int[]{3,6}, 0);
        MainWindow win = new MainWindow(game);
        win.setVisible(true);
    }
}