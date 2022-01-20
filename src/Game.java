import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class Game extends JFrame {

    private JButton[][] buttons;
    private JLabel timeLabel;

    private final int numberOfMines;
    private int[][] mineLand;
    private boolean[][] revealed;
    private int numberOfRevealed;

    private Image mine;

    public static final int MAGIC_SIZE = 30;

    public Game(int size) {
        numberOfMines = size;
        this.setSize(size * MAGIC_SIZE, size * MAGIC_SIZE + 50);
        this.setTitle("MineSweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void clearMineLand(int size) {
        mineLand = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                mineLand[i][j] = 0;
            }
        }
    }

    private void setMines(int size) {
        clearMineLand(size);

        Random rand = new Random();
        int count = 0;
        int xPoint;
        int yPoint;
        while (count < numberOfMines) {
            xPoint = rand.nextInt(size);
            yPoint = rand.nextInt(size);
            if (mineLand[xPoint][yPoint] != -1) {
                mineLand[xPoint][yPoint] = -1;  // -1 represents bomb
                count++;
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (mineLand[i][j] == -1) {
                    for (int k = -1; k <= 1; k++) {
                        for (int l = -1; l <= 1; l++) {
                            if (i + k >= 0 && j + l >= 0 && i + k < size && j + l < size) {
                                if (mineLand[i + k][j + l] != -1) {
                                    mineLand[i + k][j + l] += 1;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void main(Game frame, int size) {
        GameEngine gameEngine = new GameEngine(frame);
        JPanel mainPanel = new JPanel();
        JPanel panelTop = new JPanel();
        JPanel panelMineLand = new JPanel();

        revealed = new boolean[size][size];

        try {
            mine = ImageIO.read(Objects.requireNonNull(getClass().getResource("images/mine.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mine = mine.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        BoxLayout g1 = new BoxLayout(panelTop, BoxLayout.X_AXIS);
        panelTop.setLayout(g1);

        JLabel timeLabelText = new JLabel(" Time :");
        timeLabel = new JLabel("0");
        timeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        timeLabel.setHorizontalAlignment(JLabel.RIGHT);

        panelTop.add(timeLabelText);
        panelTop.add(timeLabel);

        GridLayout g2 = new GridLayout(size, size);
        panelMineLand.setLayout(g2);

        buttons = new JButton[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setBorder(new LineBorder(Color.gray));
                buttons[i][j].setName(i + " " + j);
                buttons[i][j].addActionListener(gameEngine);
                panelMineLand.add(buttons[i][j]);
            }
        }

        mainPanel.add(panelTop);
        mainPanel.add(panelMineLand);
        frame.setContentPane(mainPanel);
        this.setVisible(true);

        setMines(size);

        TimeThread timer = new TimeThread(this);
        timer.start();
    }

    public void timer() {
        String[] time = this.timeLabel.getText().split(" ");
        int time0 = Integer.parseInt(time[0]);
        ++time0;
        this.timeLabel.setText(time0 + " s");
    }

    private boolean gameWon() {
        return (this.numberOfRevealed) == (Math.pow(this.mineLand.length, 2) - this.numberOfMines);
    }

    public void buttonClicked(int x, int y) {
        if (x < 0 || y < 0 || x > revealed.length - 1 || y > revealed.length - 1)
            return;

        if (!revealed[x][y]) {
            revealed[x][y] = true;

            switch (mineLand[x][y]) {
                case -1 -> {
                    buttons[x][y].setIcon(new ImageIcon(mine));
                    buttons[x][y].setBackground(Color.RED);
                    JOptionPane.showMessageDialog(this, "Game Over !", null, JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
                case 0 -> {
                    buttons[x][y].setBackground(Color.lightGray);
                    ++this.numberOfRevealed;
                    if (gameWon()) {
                        JOptionPane.showMessageDialog(rootPane, "Congratulations! You've Won");

                        System.exit(0);
                    }
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            buttonClicked(x + i, y + j);
                        }
                    }
                }
                default -> {
                    buttons[x][y].setText(Integer.toString(mineLand[x][y]));
                    buttons[x][y].setBackground(Color.LIGHT_GRAY);
                    ++this.numberOfRevealed;
                    if (gameWon()) {
                        JOptionPane.showMessageDialog(rootPane, "You Won !");
                        System.exit(0);
                    }
                }
            }
        }
    }
}
