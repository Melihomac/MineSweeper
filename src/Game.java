import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class Game extends JFrame {
    private final int GAME_SIZE;
    private final int[][] _mineLand;
    private final int MINE_AMOUNT;
    private final boolean[][] _revealed;
    private int _numberOfRevealed;
    private int _gameTime = 0;
    private static final int MAGIC_SIZE = 30;

    private Image _imgMine;
    private JButton[][] _buttons;
    private JLabel _labelTime;

    private TimeThread timer;

    public Game(int gameSize, int numberOfMines) {
        GAME_SIZE = gameSize;
        MINE_AMOUNT = numberOfMines;
        _mineLand = new int[gameSize][gameSize];
        _revealed = new boolean[gameSize][gameSize];
    }

    private void clearMineLand() {
        for (int i = 0; i < GAME_SIZE; i++) {
            for (int j = 0; j < GAME_SIZE; j++) {
                _mineLand[i][j] = 0;
            }
        }
    }

    private boolean isMine(int x) {
        return (x == -1);
    }

    private boolean isValidIndex(int x, int y) {
        return (x >= 0 && y >= 0 && x < GAME_SIZE && y < GAME_SIZE);
    }

    private void countMines(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (isValidIndex(x + i, y + j)) {
                    if (!isMine((_mineLand[x + i][y + j]))) {
                        _mineLand[x + i][y + j] += 1;
                    }
                }
            }
        }
    }

    private void setMines() {
        Random rand = new Random();
        int count = 0;
        int xPoint;
        int yPoint;

        while (count < MINE_AMOUNT) {
            xPoint = rand.nextInt(GAME_SIZE);
            yPoint = rand.nextInt(GAME_SIZE);
            if (!isMine(_mineLand[xPoint][yPoint])) {
                _mineLand[xPoint][yPoint] = -1;  // -1 represents bomb
                count++;
                countMines(xPoint, yPoint);
            }
        }
    }

    private void createForm() {
        GameEngine gameEngine = new GameEngine(this);
        JPanel panelMain = new JPanel();
        JPanel panelTop = new JPanel();
        JPanel panelMineLand = new JPanel();

        this.setTitle("MineSweeper");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setSize(GAME_SIZE * MAGIC_SIZE, GAME_SIZE * MAGIC_SIZE + 50);

        try {
            _imgMine = ImageIO.read(Objects.requireNonNull(getClass().getResource("images/mine.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        _imgMine = _imgMine.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);

        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));

        BoxLayout g1 = new BoxLayout(panelTop, BoxLayout.X_AXIS);
        panelTop.setLayout(g1);

        _labelTime = new JLabel("0 s");
        _labelTime.setAlignmentX(Component.RIGHT_ALIGNMENT);
        _labelTime.setHorizontalAlignment(JLabel.RIGHT);

        JLabel labelTimeText = new JLabel(" Time: ");
        panelTop.add(labelTimeText);
        panelTop.add(_labelTime);

        GridLayout g2 = new GridLayout(GAME_SIZE, GAME_SIZE);
        panelMineLand.setLayout(g2);

        _buttons = new JButton[GAME_SIZE][GAME_SIZE];

        for (int i = 0; i < GAME_SIZE; i++) {
            for (int j = 0; j < GAME_SIZE; j++) {
                _buttons[i][j] = new JButton();
                _buttons[i][j].setBorder(new LineBorder(Color.gray));
                _buttons[i][j].setName(i + " " + j);
                _buttons[i][j].addActionListener(gameEngine);
                panelMineLand.add(_buttons[i][j]);
            }
        }

        panelMain.add(panelTop);
        panelMain.add(panelMineLand);
        this.setContentPane(panelMain);
        this.setVisible(true);
    }

    private boolean gameWon() {
        return (_numberOfRevealed) == (Math.pow(GAME_SIZE, 2) - MINE_AMOUNT);
    }

    public void updateTime() {
        _gameTime++;
        _labelTime.setText(_gameTime + " s");
    }

    private void revealMines() {
        for (int i = 0; i < GAME_SIZE; i++) {
            for (int j = 0; j < GAME_SIZE; j++) {
                if (isMine(_mineLand[i][j])) {
                    _buttons[i][j].setIcon(new ImageIcon(_imgMine));
                }
            }
        }
    }

    public void buttonClicked(int x, int y) {
        if (!isValidIndex(x, y))
            return;

        if (!_revealed[x][y]) {
            _revealed[x][y] = true;
            _numberOfRevealed++;

            switch (_mineLand[x][y]) {
                case -1 -> {
                    _buttons[x][y].setBackground(Color.RED);
                    revealMines();
                    timer.stop();
                    JOptionPane.showMessageDialog(this, "Game Over !", null, JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
                case 0 -> {
                    _buttons[x][y].setBackground(Color.LIGHT_GRAY);

                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            buttonClicked(x + i, y + j);
                        }
                    }
                }
                default -> {
                    _buttons[x][y].setText(Integer.toString(_mineLand[x][y]));
                    _buttons[x][y].setBackground(Color.LIGHT_GRAY);
                }
            }

            if (gameWon()) {
                revealMines();
                timer.stop();
                JOptionPane.showMessageDialog(rootPane, "You Won !");
                System.exit(0);
            }
        }
    }

    public void play() {
        clearMineLand();
        setMines();
        createForm();

        timer = new TimeThread(this);
        timer.start();
    }
}
