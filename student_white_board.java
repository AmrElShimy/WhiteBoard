package online_learning_system;

import Test.lectureEnded;

import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.List;
import java.util.Timer;

import javax.swing.*;

import static java.lang.Integer.parseInt;

public class student_white_board extends JPanel implements Runnable {
    static int interval;
    static Timer timer;
    static JLabel time = new JLabel("Time Left: 00:00:00", JLabel.RIGHT);
    static JLabel counter = new JLabel("                          Lines: 0   Circles: 0   Rectangles: 0   ");
    static JPanel timerPanel = new JPanel(new GridLayout(2, 1));

    private Socket socket = null;
    private final ServerSocket server = new ServerSocket(5001);
    private final DataInputStream in = null;

    private final ArrayList<Point> line_coordinates = new ArrayList<>();
    private final ArrayList<Point> circle_coordinates = new ArrayList<>();
    private final ArrayList<Point> rectangle_coordinates = new ArrayList<>();
    private final ArrayList<Point> line_start = new ArrayList<>();
    private final ArrayList<Point> circle_start = new ArrayList<>();
    private final ArrayList<Point> rectangle_start = new ArrayList<>();

    private final ArrayList<Point> circle_Pen = new ArrayList<>();
    private final ArrayList<Point> square_Pen = new ArrayList<>();
    private final ArrayList<Point> eraser_Pen = new ArrayList<>();

    int beginX_Line = 0;
    int endX_Line = 0;
    int endY_Line = 0;
    int beginY_Line = 0;
    int beginX_Circle = 0;
    int endX_Circle = 0;
    int endY_Circle = 0;
    int beginY_Circle = 0;

    int beginX_Rectangle = 0;
    int endX_Rectangle = 0;
    int endY_Rectangle = 0;
    int beginY_Rectangle = 0;

    public student_white_board() throws IOException, ClassNotFoundException {

    }

    public void receive() throws IOException, ClassNotFoundException {
        time.setFont(new Font(time.getName(), Font.PLAIN, 40));
        time.setHorizontalTextPosition(JLabel.CENTER);
        counter.setHorizontalTextPosition(JLabel.CENTER);
        timerPanel.add(time);
        timerPanel.add(counter);
        this.add(timerPanel, BorderLayout.NORTH);
        Thread t = new Thread() {
            @Override
            public void run() {
                int secs = 3610;
                int delay = 1000;
                int period = 1000;

                timer = new Timer();
                interval = secs;
                timer.scheduleAtFixedRate(new TimerTask() {

                    public void run() {
                        int interval = setInterval();
                        int hoursToDisplay = interval / 3600;
                        int minutesToDisplay = (interval % 3600) / 60;
                        int secsToDisplay = interval % 60;
                        String timeString = "";
                        timeString = String.format("%02d:%02d:%02d", hoursToDisplay, minutesToDisplay, secsToDisplay);
                        time.setText("Time Left: " + timeString);
                        counter.setText("                          Lines: " + line_coordinates.size() + "   Circles: " + circle_coordinates.size()
                                + "   Rectangles: " + rectangle_coordinates.size());
                    }
                }, delay, period);
            }
        };
        System.out.println("Entered class!");
        System.out.println("Waiting for a teacher ...");
        socket = server.accept();
        System.out.println("Teacher joined!");
        t.start();
        while (true) {
            try {
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                List<String> listOfMessages = (List<String>) objectInputStream.readObject();
                System.out.println("Received [" + listOfMessages.size() + "] messages from: " + socket);

                // print out the text of every message
                System.out.println(listOfMessages);

                if (listOfMessages.get(0).equals("DrawLine")) {
                    beginX_Line = parseInt(listOfMessages.get(1));
                    beginY_Line = parseInt(listOfMessages.get(2));
                    endX_Line = parseInt(listOfMessages.get(3));
                    endY_Line = parseInt(listOfMessages.get(4));
                    Point begin = new Point();
                    begin.x = beginX_Line;
                    begin.y = beginY_Line;
                    line_start.add(begin);

                    Point end = new Point();
                    end.x = endX_Line;
                    end.y = endY_Line;
                    line_coordinates.add(end);
                    repaint();

                } else if (listOfMessages.get(0).equals("DrawCircle")) {
                    beginX_Circle = parseInt(listOfMessages.get(1));
                    beginY_Circle = parseInt(listOfMessages.get(2));
                    endX_Circle = parseInt(listOfMessages.get(3));
                    endY_Circle = parseInt(listOfMessages.get(4));
                    Point begin = new Point();
                    begin.x = beginX_Circle;
                    begin.y = beginY_Circle;
                    circle_start.add(begin);

                    Point end = new Point();
                    end.x = endX_Circle;
                    end.y = endY_Circle;
                    circle_coordinates.add(end);
                    repaint();

                } else if (listOfMessages.get(0).equals("DrawRectangle")) {
                    beginX_Rectangle = parseInt(listOfMessages.get(1));
                    beginY_Rectangle = parseInt(listOfMessages.get(2));
                    endX_Rectangle = parseInt(listOfMessages.get(3));
                    endY_Rectangle = parseInt(listOfMessages.get(4));
                    Point begin = new Point();
                    begin.x = beginX_Rectangle;
                    begin.y = beginY_Rectangle;
                    rectangle_start.add(begin);

                    Point end = new Point();
                    end.x = endX_Rectangle;
                    end.y = endY_Rectangle;
                    rectangle_coordinates.add(end);
                    repaint();

                } else if (listOfMessages.get(0).equals("CirclePen")) {
                    for (int i = 1; i < listOfMessages.size(); i += 2) {
                        Point point = new Point();
                        point.x = parseInt(listOfMessages.get(i));
                        point.y = parseInt(listOfMessages.get(i + 1));
                        circle_Pen.add(point);
                    }
                    repaint();
                } else if (listOfMessages.get(0).equals("SquarePen")) {
                    for (int i = 1; i < listOfMessages.size(); i += 2) {
                        Point point = new Point();
                        point.x = parseInt(listOfMessages.get(i));
                        point.y = parseInt(listOfMessages.get(i + 1));
                        square_Pen.add(point);
                    }
                    repaint();
                }

            } catch (SocketException e) {
                System.out.println("No Connection!");
                break;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
//        //LINE DRAWING
            for (int i = 0; i < line_coordinates.size(); i++) {
                g.drawLine(line_start.get(i).x, line_start.get(i).y, line_coordinates.get(i).x, line_coordinates.get(i).y);
            }

            for (int i = 0; i < circle_coordinates.size(); i++) {
                if (circle_start.get(i).x > circle_coordinates.get(i).x && circle_start.get(i).y > circle_coordinates.get(i).y)
                    g.drawOval(circle_coordinates.get(i).x, circle_coordinates.get(i).y, circle_start.get(i).x - circle_coordinates.get(i).x, circle_start.get(i).y - circle_coordinates.get(i).y);
                if (circle_start.get(i).x > circle_coordinates.get(i).x && circle_start.get(i).y < circle_coordinates.get(i).y)
                    g.drawOval(circle_coordinates.get(i).x, circle_start.get(i).y, circle_start.get(i).x - circle_coordinates.get(i).x, circle_coordinates.get(i).y - circle_start.get(i).y);
                if (circle_start.get(i).x < circle_coordinates.get(i).x && circle_start.get(i).y > circle_coordinates.get(i).y)
                    g.drawOval(circle_start.get(i).x, circle_coordinates.get(i).y, circle_coordinates.get(i).x - beginX_Circle, circle_start.get(i).y - circle_coordinates.get(i).y);
                else
                    g.drawOval(circle_start.get(i).x, circle_start.get(i).y, circle_coordinates.get(i).x - circle_start.get(i).x, circle_coordinates.get(i).y - circle_start.get(i).y);
            }

            for (int i = 0; i < rectangle_coordinates.size(); i++) {
                if (rectangle_start.get(i).x > rectangle_coordinates.get(i).x && rectangle_start.get(i).y > rectangle_coordinates.get(i).y)
                    g.drawRect(rectangle_coordinates.get(i).x, rectangle_coordinates.get(i).y, rectangle_start.get(i).x - rectangle_coordinates.get(i).x, rectangle_start.get(i).y - rectangle_coordinates.get(i).y);
                if (rectangle_start.get(i).x > rectangle_coordinates.get(i).x && rectangle_start.get(i).y < rectangle_coordinates.get(i).y)
                    g.drawRect(rectangle_coordinates.get(i).x, rectangle_start.get(i).y, rectangle_start.get(i).x - rectangle_coordinates.get(i).x, rectangle_coordinates.get(i).y - rectangle_start.get(i).y);
                if (rectangle_start.get(i).x < rectangle_coordinates.get(i).x && rectangle_start.get(i).y > rectangle_coordinates.get(i).y)
                    g.drawRect(rectangle_start.get(i).x, rectangle_coordinates.get(i).y, rectangle_coordinates.get(i).x - beginX_Rectangle, rectangle_start.get(i).y - rectangle_coordinates.get(i).y);
                else
                    g.drawRect(rectangle_start.get(i).x, rectangle_start.get(i).y, rectangle_coordinates.get(i).x - rectangle_start.get(i).x, rectangle_coordinates.get(i).y - rectangle_start.get(i).y);
            }
//
            for (Point point : circle_Pen) {
                g.fillOval(point.x, point.y, 100, 100);
            }
            for (Point point : square_Pen) {
                g.fillRect(point.x, point.y, 100, 100);
            }
        } catch (ConcurrentModificationException e) {
        }
    }

    @Override
    public void run() {
        student_white_board runner = this;
    }

    private static final int setInterval() {
        if (interval == 1) {
            lectureEnded Le = new lectureEnded();
            timer.cancel();
        }
        return --interval;
    }
}