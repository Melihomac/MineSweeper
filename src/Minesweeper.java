public class Minesweeper {

    public void proceed(int size) {
        Game newGame = new Game(size);
        newGame.main(newGame, size);
    }

    public static void main(String[] args) {
        Minesweeper minesweeper = new Minesweeper();
        minesweeper.proceed(10);
    }
}
