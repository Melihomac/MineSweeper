public class TimeThread implements Runnable {
    private final Thread t;
    private final Game newGame;

    TimeThread(Game newGame) {
        this.newGame = newGame;
        this.t = new Thread(this);
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
        t.start();
    }

    public void stop() {
        t.stop();
    }
}
