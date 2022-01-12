public class Minesweeper {
    private static Minesweeper minesweeper;
    private static game newGame;

    public void proceed(int size) {
        newGame = new game(size);
        newGame.main(newGame, size);
    }

    public static void main(String[] args) {
        minesweeper = new Minesweeper();
        minesweeper.proceed(10);
    }
}