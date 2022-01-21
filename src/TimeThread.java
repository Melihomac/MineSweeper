public class TimeThread implements Runnable {
    private Thread t;
    private final Game newGame;

    TimeThread(Game newGame) {
        this.newGame = newGame;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                newGame.updateTime();
            } catch (InterruptedException e) {
                System.exit(0);
            }
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}
