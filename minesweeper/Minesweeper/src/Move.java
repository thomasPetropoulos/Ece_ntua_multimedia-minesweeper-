import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Move implements MouseMotionListener {
    public int  mx = -100;
    public int my = -100;
    GUI gui;
    public Move(GUI g) {
        gui = g;
    }
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
        gui.repaint();
    }
}
