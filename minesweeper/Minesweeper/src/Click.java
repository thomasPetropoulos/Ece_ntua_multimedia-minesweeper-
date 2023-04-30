import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Click implements MouseListener {
    GUI gui;
    public Click(GUI g) {
        gui = g;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!Board.defeat && !Board.victory) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (Board.getInBoxX() != -1 && Board.getInBoxY() != -1) {
                    GUI.revealed[Board.getInBoxX()][Board.getInBoxY()] = true;
                    if(GUI.mines[Board.getInBoxX()][Board.getInBoxY()]==1){
                        Board.happy=false;
                        Board.defeat=true;
                    }
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                if (Board.getInBoxX() != -1 && Board.getInBoxY() != -1 && !GUI.flagged[Board.getInBoxX()][Board.getInBoxY()]) {
                    GUI.flagged[Board.getInBoxX()][Board.getInBoxY()] = true;
                    if (GUI.flagged[Board.getInBoxX()][Board.getInBoxY()] &&  Board.getInBoxX()== GUI.hm/Board.gameBounds && Board.getInBoxY() == GUI.hm%Board.gameBounds && Board.numOfRevealed<=4){
                        for(int p=0;p<Board.gameBounds; p++){
                            GUI.revealed[p][Board.getInBoxY()]=true;
                            GUI.revealed[Board.getInBoxX()][p]=true;
                        }
                    }

                } else if (Board.getInBoxX() != -1 && Board.getInBoxY() != -1 && GUI.flagged[Board.getInBoxX()][Board.getInBoxY()]) {
                    GUI.flagged[Board.getInBoxX()][Board.getInBoxY()] = false;
                }
            }
        }
        gui.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
