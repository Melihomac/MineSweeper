public class MineSweeper {

    public static void proceed(int size) {
        Game newGame = new Game(size);
        newGame.main(newGame, size);
    }

    public static void main(String[] args) {
        proceed(10);
    }
}
