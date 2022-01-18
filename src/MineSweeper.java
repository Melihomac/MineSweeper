public class MineSweeper {

    public void proceed(int size) {
        Game newGame = new Game(size);
        newGame.main(newGame, size);
    }

    public static void main(String[] args) {
        MineSweeper mineSweeper = new MineSweeper();
        mineSweeper.proceed(10);
    }
}
