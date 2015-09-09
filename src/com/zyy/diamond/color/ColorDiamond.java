/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zyy.diamond.color;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @date 2011-10-10, 18:01:11
 * @author hualun-alan <alan.zeng@lonnesol.com>
 */
public class ColorDiamond extends JFrame implements ActionListener {

    private static int COLUMNS = 23;
    private static int ROWS = 15;
    private static Dimension d = new Dimension(30, 30);
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int TOP = 3;
    private static final int BOTTOM = 4;
    private static final Cursor CURSOR_HAND = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    private static final Cursor CURSOR_DEFAULT = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    public static final int WIDTH_ = 800;
    public static final int HEIGHT_ = 600;
    //
    private JPanel jpContext;
    private JSlider jSlider;
    private JLabel jlScore;
    //--
    private ArrayList<Integer> colorIndexs;
    private ArrayList<Color> colors;
    private int[][] points = new int[ROWS][COLUMNS];
    private JButton[][] buttons;
    private int count = ROWS * COLUMNS;
    private HashMap<Integer, Integer> tempMap = new HashMap<Integer, Integer>();
    private ArrayList<Point> tempList = new ArrayList<Point>();
    private int score = 0;
    private Timer timer;

    public ColorDiamond() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH_, HEIGHT_);

        initDatas();

        jpContext = new JPanel(new BorderLayout());
        initNorthComponents();
        initCenterComponents();

        timer = new Timer(100, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int value = jSlider.getValue();
                if (value > 0) {
                    jSlider.setValue(jSlider.getValue() - 1);
                } else {
                    timer.stop();
                }
            }
        });


        WelcomePanel welcomePanel = new WelcomePanel();
        this.getContentPane().add(welcomePanel);
        this.setVisible(true);
    }

    private void addPoint(Integer result, Point pLeft, Point pTop, Point pRight, Point pBottom) {
        switch (result) {
            case LEFT:
                if (!tempList.contains(pLeft)) {
                    tempList.add(pLeft);
                }
                break;
            case TOP:
                if (!tempList.contains(pTop)) {
                    tempList.add(pTop);
                }
                break;
            case RIGHT:
                if (!tempList.contains(pRight)) {
                    tempList.add(pRight);
                }
                break;
            case BOTTOM:
                if (!tempList.contains(pBottom)) {
                    tempList.add(pBottom);
                }
                break;
        }
    }

    private void initDatas() {
        initColorIndexs();
        initColors();
        initPoints();
    }

    private void initColors() {
        colors = new ArrayList<Color>();
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.select", Color.WHITE);
        colors.add(Color.WHITE);
        colors.add(Color.RED);
        colors.add(Color.BLACK);
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.MAGENTA);
        colors.add(new Color(11, 123, 22));
        colors.add(Color.PINK);
        colors.add(Color.YELLOW);
    }

    private void initColorIndexs() {
        colorIndexs = new ArrayList<Integer>();
        for (int i = 1; i < 11; i++) {
            for (int j = 0; j < 20; j++) {
                colorIndexs.add(i);
            }
        }
    }

    private void initPoints() {
        Random random = new Random();

        ArrayList<Integer> pointss = new ArrayList<Integer>();
        while (pointss.size() < 200) {
            int nextInt = random.nextInt(count);
            if (!pointss.contains(nextInt)) {
                pointss.add(nextInt);
            }
        }
        System.out.println("pointss.size = " + pointss.size());

        for (int i = 0; i < points.length; i++) {
            int[] is = points[i];
            for (int j = 0; j < is.length; j++) {
                is[j] = 0;
            }
        }

        for (int i = 0; i < pointss.size(); i++) {
            int idx = pointss.get(i);
            int row = idx / COLUMNS;
            int col = idx % COLUMNS;
            points[row][col] = colorIndexs.get(i);
        }

//        for (int i = 0; i < points.length; i++) {
//            int[] is = points[i];
//            for (int j = 0; j < is.length; j++) {
//                is[j] = 0;
//                int nextInt = random.nextInt(count - getPosition(i, j));
//                int size = colorIndexs.size();
//                if (size > 0 && nextInt < size) {
//                    is[j] = colorIndexs.remove(nextInt);
//                }
//            }
//        }
    }

    private void initNorthComponents() {
        jSlider = new JSlider(0, 1200);
        jSlider.setValue(1200);
        jSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                int value = jSlider.getValue();
                if (value == 0) {
                    WelcomePanel welcomePanel = new WelcomePanel(score);
                    ColorDiamond.this.getContentPane().removeAll();
                    ColorDiamond.this.getContentPane().add(welcomePanel);
                    ColorDiamond.this.repaint();
                    welcomePanel.updateUI();
                }
            }
        });
        jlScore = new JLabel(String.valueOf(score));
        jlScore.setPreferredSize(new Dimension(80, 30));

        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add(jSlider, BorderLayout.CENTER);
        jPanel.add(jlScore, BorderLayout.EAST);

        jpContext.add(jPanel, BorderLayout.NORTH);
    }

    private void initCenterComponents() {
        JPanel jPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(1, 1, 1, 1);
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        buttons = new JButton[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                constraints.gridx = j;
                constraints.gridy = i;
                JButton button = getButton(i, j);
                buttons[i][j] = button;
                jPanel.add(button, constraints);
            }
        }

        jpContext.add(jPanel, BorderLayout.CENTER);
    }

    private void newGame() {
        initColorIndexs();
        initPoints();

        score = 0;
        jlScore.setText(String.valueOf(score));
        jSlider.setValue(1200);
        for (int i = 0; i < points.length; i++) {
            int[] is = points[i];
            for (int j = 0; j < is.length; j++) {
                int k = is[j];
                buttons[i][j].setBackground(colors.get(k));
            }
        }
    }

    private JButton getButton(int row, int col) {
        JButton jButton = new JButton();
        int point = points[row][col];
        Color color = colors.get(point);
        jButton.setBackground(color);
        jButton.setPreferredSize(d);
        jButton.setActionCommand(row + "_" + col);
        jButton.addActionListener(this);
        jButton.setBorderPainted(false);
        jButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                JButton jButton = (JButton) e.getSource();
                String cmd = jButton.getActionCommand();
                int idx = cmd.indexOf("_");
                int row = Integer.parseInt(cmd.substring(0, idx));
                int col = Integer.parseInt(cmd.substring(idx + 1));
                if (points[row][col] == 0) {
                    jButton.setCursor(CURSOR_HAND);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                JButton jButton = (JButton) e.getSource();
                jButton.setCursor(CURSOR_DEFAULT);
            }
        });
        return jButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        int idx = cmd.indexOf("_");
        int row = Integer.parseInt(cmd.substring(0, idx));
        int col = Integer.parseInt(cmd.substring(idx + 1));

        int point = points[row][col];
        if (point > 0) {
            System.out.println("no effect");
        } else {
            int tempRow = row;
            int tempCol = col;

            int top = 0; //top
            Point pTop = new Point();
            tempCol--;
            while (tempCol >= 0 && top == 0) {
                top = points[tempRow][tempCol];
                pTop.x = tempRow;
                pTop.y = tempCol;
                tempCol--;
            }

            tempRow = row;
            tempCol = col;
            int right = 0;//right
            Point pRight = new Point();
            tempRow++;
            while (tempRow < ROWS && right == 0) {
                right = points[tempRow][tempCol];
                pRight.x = tempRow;
                pRight.y = tempCol;
                tempRow++;
            }

            tempRow = row;
            tempCol = col;
            int bottom = 0;//bottom 
            Point pBottom = new Point();
            tempCol++;
            while (tempCol < COLUMNS && bottom == 0) {
                bottom = points[tempRow][tempCol];
                pBottom.x = tempRow;
                pBottom.y = tempCol;
                tempCol++;
            }

            tempRow = row;
            tempCol = col;
            int left = 0;//left 
            Point pLeft = new Point();
            tempRow--;
            while (tempRow >= 0 && left == 0) {
                left = points[tempRow][tempCol];
                pLeft.x = tempRow;
                pLeft.y = tempCol;
                tempRow--;
            }

            tempMap.clear();
            tempList.clear();
            Integer result = null;
            tempMap.put(left, LEFT);
            result = tempMap.put(top, TOP);
            if (top > 0 && result != null) {
                tempList.add(pTop);
                addPoint(result, pLeft, pTop, pRight, pBottom);
            }
            result = tempMap.put(right, RIGHT);
            if (right > 0 && result != null) {
                tempList.add(pRight);
                addPoint(result, pLeft, pTop, pRight, pBottom);
            }
            result = tempMap.put(bottom, BOTTOM);
            if (bottom > 0 && result != null) {
                tempList.add(pBottom);
                addPoint(result, pLeft, pTop, pRight, pBottom);
            }

            int size = tempList.size();
            //System.out.println("size = " + size);
            if (size > 0) {
                for (Point point1 : tempList) {
                    points[point1.x][point1.y] = 0;
                    buttons[point1.x][point1.y].setBackground(colors.get(0));
                }
                score += size;
                jlScore.setText(String.valueOf(score));
                SoundTest.loadYes();
            } else {
                jSlider.setValue(jSlider.getValue() - 100);
                SoundTest.loadNo();
            }
            tempMap.clear();
            tempList.clear();
        }
    }

    public static void main(String[] args) {
        ColorDiamond testTimer = new ColorDiamond();
    }

    class WelcomePanel extends JPanel implements MouseListener, MouseMotionListener {

        private Image icon;
        private Rectangle rectangle;
        private int preScore = -1;

        public WelcomePanel() {
            this(-1);
        }

        public WelcomePanel(int score) {
            this.preScore = score;
            try {
                icon = ImageIO.read(this.getClass().getResourceAsStream("resources/background.jpg"));
            } catch (IOException ex) {
                Logger.getLogger(ColorDiamond.class.getName()).log(Level.SEVERE, null, ex);
            }
            initComponents();
        }

        private void initComponents() {
            this.setLayout(null);
            if (preScore > -1) {
                JLabel jLabel = new JLabel("上次得分：" + preScore + "分");
                jLabel.setBounds((ColorDiamond.this.getWidth() - icon.getWidth(null)) / 2 + 10, 5, 100, 30);
                this.add(jLabel);
            }


            this.addMouseListener(this);
            this.addMouseMotionListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int left = (ColorDiamond.this.getWidth() - icon.getWidth(null)) / 2;
            rectangle = new Rectangle(left + 300, 360, 90, 48);
            g.drawImage(icon, left, 0, null);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Point point = e.getPoint();
            if (rectangle.contains(point)) {
                ColorDiamond.this.getContentPane().removeAll();
                ColorDiamond.this.add(jpContext);
                ColorDiamond.this.repaint();
                newGame();
                jpContext.updateUI();
                timer.start();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Point point = e.getPoint();
            if (rectangle.contains(point)) {
                ColorDiamond.this.setCursor(CURSOR_HAND);
            } else {
                ColorDiamond.this.setCursor(CURSOR_DEFAULT);
            }
        }
    }
}
