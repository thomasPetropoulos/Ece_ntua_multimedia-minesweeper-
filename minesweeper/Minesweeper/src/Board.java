import javax.swing.*;
import java.awt.*;
import java.util.TimerTask;
import java.util.Timer;
import java.util.*;


public class Board extends JPanel {
    public static int numOfRevealed=0;
    public int numOfFlagged=0;
    public String victoryMes="nothing";
    public static int smileyX = 605;
    public static int smileyY = 5;
    public int timeX = 1120;
    public int timeY = 5;
    public int vicMesX=710;
    public int vicMesY=70;
    public static boolean happy = true;
    public static Move mymove = null;

    public void setMove(Move m){
        mymove= m;
    }
    public int sec=0;
    public int timeLeft=0;
    static boolean victory=false;
    static boolean defeat=false;
    public static int gameBounds;

    static int spacing =5;
    public void paintComponent(Graphics g) {
        numOfFlagged=0;
        numOfRevealed=0;
        //zwgrafizw to fonto kai to plegma
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,0,1500,1500);
        for (int i=0; i<gameBounds ; i++) {
            for (int j = 0; j < gameBounds; j++) {
                g.setColor(Color.pink);
                //aspra oi mh narkes
                if (GUI.revealed[i][j]) {
                    g.setColor(Color.white);
                }
                //kitrino an vriskomai panw sto plakidio
                if (mymove.mx >= spacing + i * 42 && mymove.mx < spacing + i * 42 + 40 && mymove.my >= spacing + j * 42 + 80+40 && mymove.my < spacing + j * 42 + 80 + 40+40) {
                    g.setColor(Color.yellow);
                }
                //dhmiourgw to plegma
                g.fillRect(spacing + i * 42, spacing + j * 42 + 80, 40, 40);

                //an pathsw click
                if (GUI.revealed[i][j]) {
                    numOfRevealed++;
                    //an den einai narkh kai exei geitones pou exoun narkh exei diaforetiko xrwma text se kathe periptwsh
                    if (GUI.mines[i][j] == 0 && GUI.neighbours[i][j] != 0) {
                        if (GUI.neighbours[i][j] == 1) {
                            g.setColor(Color.RED);
                        } else if (GUI.neighbours[i][j] == 2) {
                            g.setColor(Color.blue);
                        } else if (GUI.neighbours[i][j] == 3) {
                            g.setColor(Color.green);
                        } else if (GUI.neighbours[i][j] == 4) {
                            g.setColor(Color.darkGray);
                        } else if (GUI.neighbours[i][j] == 5) {
                            g.setColor(Color.yellow);
                        } else if (GUI.neighbours[i][j] == 6) {
                            g.setColor(new Color(150, 80, 10));
                        } else if (GUI.neighbours[i][j] == 7) {
                            g.setColor(new Color(100, 20, 100));
                        } else if (GUI.neighbours[i][j] == 8) {
                            g.setColor(new Color(230, 200, 200));
                        }

                        g.setFont(new Font("Tahoma", Font.BOLD, 20));
                        g.drawString(Integer.toString(GUI.neighbours[i][j]), i * 42 + 20, j * 42 + 80+30 );
                    }
                    //an einai narkh
                    else if (GUI.mines[i][j] == 1) {
                        g.setColor(Color.black);
                        g.fillRect(i * 42 + 16, j * 42 + 80 + 10, 15, 35);
                        g.fillRect(i * 42 + 6, j * 42 + 80 + 15 + 5, 35, 15);
                        g.fillRect(i * 42 + 11, j * 42 + 80 + 15 , 25, 25);
                    }
                    // an den einai narkh kai den exei narkh sth geitonia tou
                    else if (GUI.neighbours[i][j] == 0 && GUI.mines[i][j] == 0) {
                        int a = i;
                        int b = j;
                        for (int k = a - 1; k < a + 2; k++) {
                            for (int l = b - 1; l < b + 2; l++) {
                                if (k >= 0 && k < gameBounds && l >= 0 && l < gameBounds) {
                                    GUI.revealed[k][l] = true;
                                }
                            }
                        }
                    }
                }
                else if(GUI.flagged[i][j] && numOfFlagged<GUI.min){
                    numOfFlagged++;
                    g.setColor(Color.black);
                    g.drawLine(i * 42 + 25, j * 42 + 80 + 20, i*42+25+32, j*42+80+20);
                    g.drawLine(i * 42 + 25, j * 42 + 80 + 20, i*42+25, j*42+80+20+32);
                    g.drawLine(i*42+30+10,j*42+80+20+10,i*42+30+10+10,j*42+80+20+10);

                }
            }
        }
        // smiley painting
        g.setColor(Color.yellow);
        g.fillOval(smileyX,smileyY,70,70);
        g.setColor(Color.black);
        g.fillOval(smileyX+15,smileyY+20,10,10);
        g.fillOval(smileyX+45,smileyY+20,10,10);

        if(happy){
            g.fillRect(smileyX+20,smileyY+50,30,5);
            g.fillRect(smileyX+17,smileyY+45,5,5);
            g.fillRect(smileyX+48,smileyY+45,5,5);
        }
        else{
            g.fillRect(smileyX+20,smileyY+45,30,5);
            g.fillRect(smileyX+17,smileyY+50,5,5);
            g.fillRect(smileyX+48,smileyY+50,5,5);
        }

        //timer
        g.setColor(Color.black);
        g.fillRect(timeX-300,timeY,150,72);
        if(!victory && !defeat) {
            Date endDate = new Date();
            sec = (int) ((endDate.getTime() - GUI.startDate.getTime()) / 1000);
            timeLeft = GUI.tim - sec;
            if (timeLeft > 0) {
                g.setColor(Color.white);
                g.setFont(new Font(("Tahoma"), Font.PLAIN, 70));
                g.drawString(Integer.toString(timeLeft), timeX-300, timeY + 70);
            }
            else if (timeLeft == 0) {
                defeat = true;
                happy = false;
            }
        }

        //Revealed tracker
        g.setColor(new Color(9, 216, 248, 138));
        g.fillRect(timeX-1100,timeY,100,72);
        if(!victory && !defeat) {
            g.setColor(Color.white);
            g.setFont(new Font(("Tahoma"), Font.PLAIN, 70));
            g.drawString(Integer.toString(GUI.min), timeX-1100, timeY+70);
        }

        //Flagged tracker
        g.setColor(new Color(241, 143, 255, 138));
        g.fillRect(timeX-900,timeY,100,72);
        if(!victory && !defeat) {
            g.setColor(Color.white);
            g.setFont(new Font(("Tahoma"), Font.PLAIN, 70));
            g.drawString(Integer.toString(numOfFlagged), timeX-900, timeY+70);
        }

        if(numOfRevealed==gameBounds*gameBounds-GUI.min) victory=true;




        //victory message painting
        if(victory){
            g.setColor(Color.GREEN);
            victoryMes="YOU WIN";
        }
        else if(defeat) {
            g.setColor(Color.red);
            victoryMes="YOU LOSE";
        }

        if(victory || defeat){

            g.drawString(victoryMes,vicMesX,vicMesY);
        }

    }

    public static int getInBoxX(){
        for (int i=0; i<gameBounds ; i++) {
            for (int j = 0; j < gameBounds; j++) {
                if (mymove.mx >= spacing + i * 42 && mymove.mx < spacing + i * 42 + 40 && mymove.my >= spacing + j * 42 + 80+40 && mymove.my < spacing + j * 42 + 80 + 40+40) {
                    return i;
                }
            }
        }
        return -1;
    }
    public static   int getInBoxY() {
        for (int i = 0; i < gameBounds; i++) {
            for (int j = 0; j < gameBounds; j++) {
                if (mymove.mx >= spacing + i * 42 && mymove.mx < spacing + i * 42 + 40 && mymove.my >= spacing + j * 42 + 80+40 && mymove.my < spacing + j * 42 + 80 + 40+40) {
                    return j;
                }
            }
        }
        return -1;
    }

    public static boolean isNeighbour(int mX, int mY, int cX, int cY){
        return mX - cX < 2 && mX - cX > -2 && mY - cY < 2 && mY - cY > -2 && GUI.mines[cX][cY] == 1;
    }
}