import javax.imageio.ImageIO;
import javax.management.InvalidAttributeValueException;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GUI extends JFrame {
    static int hm=-1;

    String det = "";
    public int lev;
    public static int min;
    public static int hyp;
    public static int tim;

    static int neigbs=0;
    int totalBoxesRevealed=0;
    static int [][] mines;

    static int [][] neighbours;
    static boolean [][] flagged;
    static boolean [][] revealed;

    static Random random = new Random();
    public boolean resetter = false;
    public boolean ready = false;

    public static Date startDate;


    public GUI(){
        this.setTitle("Minesweeper");
        MenuListener listener = new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {

            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        };

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Application");
        JMenuItem menuItem1 = new JMenuItem("Create");
        menu.add(menuItem1);
        menu.addSeparator();
        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f;
                f=new JFrame();
                JTextField scenario = new JTextField();
                JTextField level = new JTextField();
                JTextField mines = new JTextField();
                JTextField hyperMine = new JTextField();
                JTextField time = new JTextField();
                Object [] fields = {
                        "SCENARIO-ID",scenario,
                        "Level",level,
                        "Number Of Mines",mines,
                        "HyperMine",hyperMine,
                        "Time",time
                };
                int option = JOptionPane.showConfirmDialog(null, fields, "Create a new SCENARIO-ID", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String sce= scenario.getText();
                    lev= Integer.parseInt(level.getText());
                    min= Integer.parseInt(mines.getText());
                    hyp= Integer.parseInt(hyperMine.getText());
                    tim= Integer.parseInt(time.getText());
                    if((lev==1 && min>=9 && min<=11 && hyp==0 && tim>=120 && tim<=180)||(lev==2 && min>=35 && min<=45 && hyp==1 && tim>=240 && tim<=360)){
                        if(lev==1){Board.gameBounds=9;}
                        else {Board.gameBounds=16;}
                    }
                    File file = new File("C:\\Users\\00tpe\\OneDrive\\Υπολογιστής\\Σειρές Ασκήσεων\\Πολυμέσα\\minesweeper\\examples\\"+sce +".txt");
                    try {
                        FileWriter myWriter = new FileWriter(file);
                        myWriter.write(level.getText() + '\n' + mines.getText() + '\n' + hyperMine.getText() + '\n' + time.getText());
                        myWriter.close();
                    }
                    catch (IOException p) {
                        System.out.println("Error occured when submitting Scenario-ID");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else {
                    System.out.println("Operation canceled");
                }
            }
        });
        JMenuItem menuItem2 = new JMenuItem("Load");
        menu.add(menuItem2);
        menu.addSeparator();
        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f;
                f=new JFrame();
                String name=JOptionPane.showInputDialog(f,"SCENARIO-ID");
                File file = new File("C:\\Users\\00tpe\\OneDrive\\Υπολογιστής\\Σειρές Ασκήσεων\\Πολυμέσα\\minesweeper\\examples\\"+name +".txt" );
                Scanner sc = null;
                try {
                    sc = new Scanner(file);
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

                lev = Integer.parseInt(sc.nextLine());
                min = Integer.parseInt(sc.nextLine());
                tim = Integer.parseInt(sc.nextLine());
                hyp = Integer.parseInt(sc.nextLine());
                if(lev==1)Board.gameBounds=9;
                else if(lev==2) Board.gameBounds=16;
            }
        });
        JMenuItem menuItem3 = new JMenuItem("Start");
        menu.add(menuItem3);
        menu.addSeparator();
        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mines = new int[Board.gameBounds][Board.gameBounds];
                flagged = new boolean[Board.gameBounds][Board.gameBounds];
                neighbours = new int[Board.gameBounds][Board.gameBounds];
                revealed = new boolean[Board.gameBounds][Board.gameBounds];

                Set<Integer> randommines = new HashSet<Integer>();
                // finding random spots for mines
                if (hyp > 0) hm = random.nextInt(min);
                for (int i = 0; i < min; i++) {
                    Integer x;
                    do {
                        x = random.nextInt(Board.gameBounds*Board.gameBounds);
                    }
                    while (randommines.contains(x));
                    randommines.add(x);
                    if (i == hm) hm = x;
                }

                // building the board
                for (int i = 0; i < Board.gameBounds; i++) {
                    for (int j = 0; j < Board.gameBounds; j++) {
                        if (randommines.contains(i*Board.gameBounds + j)) {
                            mines[i][j]=1;
                        det =   det + Integer.toString(i) + ',' + Integer.toString(j) +
                            ((i == hm/Board.gameBounds && j == hm%Board.gameBounds) ? ",1\n" : ",0\n");
                        }
                        else mines[i][j]=0;
                        revealed[i][j]= false;
                        flagged[i][j] = false;
                    }
                }
                File mine_file = new File("C:\\Users\\00tpe\\OneDrive\\Υπολογιστής\\Σειρές Ασκήσεων\\Πολυμέσα\\minesweeper\\details" + getTitle()+".txt");
                FileWriter myWriter = null;
                try {
                    myWriter = new FileWriter(mine_file);
                }
                catch (IOException ex) {
                    System.out.println("Cannot write to file mines.txt");
                }
                try {
                    assert myWriter != null;
                    myWriter.write(det);
                    myWriter.close();
                }
                catch (IOException ex) {
                    System.out.println("Cannot write to file mines.txt");
                }


                for (int k=0; k<Board.gameBounds ; k++) {
                    for (int l = 0; l < Board.gameBounds; l++) {
                        neigbs=0;
                        for (int m = 0; m < Board.gameBounds; m++) {
                            for (int n = 0; n < Board.gameBounds; n++) {
                                if(k!=m || l!=n) {
                                    if (Board.isNeighbour(k, l, m, n)) {
                                        neigbs++;
                                    }
                                }
                            }
                        }
                        neighbours[k][l] = neigbs;
                    }
                }
                ready = true;
                startDate = new Date();
                arxh();
            }
        });
        JMenuItem menuItem4 = new JMenuItem("Exit");
        menu.add(menuItem4);
        menuItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              System.exit(0);
            }
        });
        JMenu menu2 = new JMenu("Details");
        JMenuItem menuItem5 = new JMenuItem("Rounds");
        menu2.add(menuItem5);
        menu2.addSeparator();
        JMenuItem menuItem6 = new JMenuItem("Solution");
        menu2.add(menuItem6);
        menuItem6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i=0; i <Board.gameBounds; i++){
                    for(int j=0; j< Board.gameBounds; j++){
                        revealed[i][j]=true;
                    }
                }
            }
        });
        menuBar.add(menu);
        menuBar.add(menu2);
        menu.addMenuListener(listener);
        menu2.addMenuListener(listener);
        this.setJMenuBar(menuBar);
        this.setSize(1000,700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("C:\\Users\\00tpe\\OneDrive\\Υπολογιστής\\Σειρές Ασκήσεων\\Πολυμέσα\\minesweeper\\Minesweeper\\src\\logo.png")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //f.pack();
        this.setVisible(true);
        this.setResizable(true);

        Click click = new Click(this);
        this.addMouseListener(click);
    }

    private void arxh() {
        Board board = new Board();
        this.setContentPane(board);
        Move move = new Move(this);
        this.addMouseMotionListener(move);
        board.setMove(move);
    }
}