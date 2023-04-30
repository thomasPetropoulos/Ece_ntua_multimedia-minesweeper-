public class Main implements Runnable {

    public static void main(String[] args) {
        (new Thread(new Main())).start();
    }

    @Override
    public void run() {
        GUI gui = new GUI();
        while (true) {
            if (gui.ready) gui.repaint();
        }
    }
}