package app.gameboard;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Float;
import java.awt.geom.Point2D;
// Royal blue 65 105 225
//

public class Gameboard extends JPanel{
    private int panelHeight;
    private int panelWidth;
    private int size;
    private int[] xpoints_g;
    private int[] ypoints_g;
    private int npoints;
    private Triangle[] trianglesArray;
    private ArrayList<Polygon> polyArray = new ArrayList<Polygon>();
    private JFrame frame;
    private int indexTriangle = -1;
    private int indexLine = -1;
    private Color[] colorTriangle;
    private Color[] colorLine;
    private ArrayList<Line> linesArray = new ArrayList<Line>();
    //private PlayerColor playerColor;

    public Gameboard(JFrame frame, int size) {

        WindowStateListener listener = new WindowAdapter() {
            public void windowStateChanged(WindowEvent evt) {
                int oldState = evt.getOldState();
                int newState = evt.getNewState();

                if ((oldState & Frame.MAXIMIZED_BOTH) == 0 && (newState & Frame.MAXIMIZED_BOTH) != 0) {
                    System.out.println("Frame was maximized");
                    panelHeight = frame.getHeight();
                    panelWidth = frame.getWidth();
                }
                else if ((oldState & Frame.MAXIMIZED_BOTH) != 0 && (newState & Frame.MAXIMIZED_BOTH) == 0) {
                    System.out.println("Frame was minimized");
                    panelHeight = frame.getHeight();
                    panelWidth = frame.getWidth();
                }
            }
         };
         frame.addWindowStateListener(listener);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        colorTriangle = new Color[size*size];
        for (int l = 0; l < colorTriangle.length; l++) {
            colorTriangle[l] = (new Color(255,239,213));
        }
        colorLine = new Color[3*((size+1)*(size))/2];
        for (int l = 0; l < colorLine.length; l++) {
            colorLine[l] = Color.BLACK;
        }
        this.frame = frame;
        this.size = size;
        this.npoints = (size + 1)*(size + 2)/2;
        panelHeight = frame.getHeight();
        panelWidth = frame.getWidth();
        repaintGameboard();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panelHeight = frame.getHeight();
                panelWidth = frame.getWidth();
                //System.out.println("Width: " + panelWidth + "\nHeight: " + panelHeight);
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                boolean alreadyClickedLine = false;
                double shapeSize = 5;
                double xClick = e.getX()-shapeSize/2;
                double yClick = e.getY()-shapeSize/2;

                for (Line l : linesArray) {
                  if (l.getLine().intersects(xClick, yClick, shapeSize, shapeSize) && (!linesArray.get(linesArray.indexOf(l)).isLineClicked())) {
                    System.out.println("Clicked Line");
                    alreadyClickedLine = true;
                    indexLine = linesArray.indexOf(l);
                    linesArray.get(indexLine).setLineClicked();
                    colorLine[indexLine] = new Color(222, 49, 99);
                    Graphics g = getGraphics();
                    Graphics2D gr = (Graphics2D) g;
                    gr.setColor(new Color(222, 49, 99));
                    gr.setStroke(new BasicStroke(3));
                    System.out.println(l);
                    if (l.getY1() == l.getY2()) {
                        gr.drawLine(l.getX1()+2, l.getY1()+1, l.getX2()+2, l.getY2()+1);
                    }
                    else if (l.getY1() < l.getY2()){
                        gr.drawLine(l.getX1()-2, l.getY1(), l.getX2()-2, l.getY2());
                    }
                    else {
                         gr.drawLine(l.getX1()+1, l.getY1(), l.getX2()-1, l.getY2());
                    }
                    break;
                  }
                }

                Point p = new Point(e.getX(), e.getY());

                    for (Polygon poly : polyArray) {
                        if (!alreadyClickedLine && poly.contains(p) && (!trianglesArray[polyArray.indexOf(poly)].isTriangleClicked())) {
                            System.out.println("Clicked triangle");
                            indexTriangle = polyArray.indexOf(poly);
                            trianglesArray[indexTriangle].setTriangleClicked(true);

                            colorTriangle[indexTriangle] = new Color(204,51,51);
                            Graphics gr = getGraphics();
                            gr.setColor(new Color(204,51,51));

                            int[] tmpX = trianglesArray[polyArray.indexOf(poly)].getXpoints();
                            int[] tmpY = trianglesArray[polyArray.indexOf(poly)].getYpoints();

                            if (tmpY[0] == tmpY[1]) {
                                tmpX[0] -= 1;
                                //tmpX[1] -= 3;
                                tmpX[2] -= 1;
                                //tmpY[2] -= 1;
                                //tmpY[0] -= 1;
                                //tmpY[1] -= 1;
                                //tmpY[2] += 5;
                            }
                            else {
                                tmpX[2] -= 2;
                                //tmpX[1] += 2;
                                tmpX[0] -= 8;
                                tmpY[0] -= 9;
                                tmpY[2] += 2;
                                tmpY[1] += 1;
                            }

                            gr.fillPolygon(tmpX, tmpY, 3);
                            break;
                        }
                    }

                int cnt = size + 1;
                int cnt1 = size + 1;
                int k = 0;
                int l = 0;
                Graphics g = getGraphics();
                for (int i = 0; i < npoints; i++) {
                    //System.out.println("l = " + l);
                    g.setColor(new Color(119,136,153));
                    if (i == k + cnt || i == 0) {
                        if (i == npoints-1) {
                            g.fillOval(xpoints_g[i]-4, ypoints_g[i]-10, size > 20 ? 17:22, size > 20 ? 17:22);
                            continue;
                        }
                        g.fillOval(xpoints_g[i]-10, ypoints_g[i]-3, size > 20 ? 17:22, size > 20 ? 17:22);

                        k += cnt;
                        cnt--;
                    }
                    else if (i == cnt1 + l - 1 || i == size) {
                        g.fillOval(xpoints_g[i]+3, ypoints_g[i]-3, size > 20 ? 17:22, size > 20 ? 17:22);
                        l += cnt1;
                        cnt1--;

                    }
                    else {

                        g.fillOval(xpoints_g[i]-6, ypoints_g[i]-3, size > 20 ? 17:22, size > 20 ? 17:22);
                   }
                }
            }
        });
    }


    public void repaintGameboard() {
        int lenLineHor = (panelWidth - 160)/(size+1);
        int lenLineVer = (panelHeight - 144)/(size+1);
        int i = size + 1;
        int j = 0;
        int k = 0;
        int cnt = 0;
	    int tmpW = (panelWidth - lenLineHor*size)/2;
        int var = (size > 10)? -25 : 0;
        int tmpH = (panelHeight - lenLineVer/2 + var);
        Dots[] dotsArr = new Dots[npoints];
        Triangle[] trianglesArr = new Triangle[size*size];
        ArrayList<Polygon> polyArr = new ArrayList<Polygon>();
        int[] xpoints = new int[npoints];
        int[] ypoints = new int[npoints];
        ArrayList<Line> linesArr = new ArrayList<Line>();

        // Drawing points
        while (i > 0) {
            j = 0;
            while(j < i) {
                Dots d = new Dots(tmpW + j*lenLineHor + k*lenLineHor/2, tmpH - k*lenLineVer);
                dotsArr[cnt] = d;
                xpoints[cnt] = (dotsArr[cnt].getXCoord())+7;
                ypoints[cnt] = (dotsArr[cnt].getYCoord())+7;
                cnt++;
                j++;
            }
            k++;
            i--;
        }
        xpoints_g = xpoints;
        ypoints_g = ypoints;

        int[] triangleX = new int[3];
		int[] triangleY = new int[3];
        int lim = size;
        cnt = size+1;
        k = 0;
        i = 0;
        for (int z = 0; z < size; z++) {
            while (k < lim) {
                triangleX[0] = xpoints_g[k]+7;
                triangleX[1] = xpoints_g[k+1]+7;
                triangleX[2] = xpoints_g[k+cnt]+7;
                triangleY[0] = ypoints_g[k]+7;
                triangleY[1] = ypoints_g[k+1]+7;
                triangleY[2] = ypoints_g[k+cnt]+7;
                Triangle tr = new Triangle(triangleX, triangleY, 3);
                trianglesArr[i++] = tr;
                polyArr.add(trianglesArr[i-1].getPolygon());

                if (k < lim-1) {
                    triangleX[0] = xpoints_g[k+1]+7;
                    triangleX[1] = xpoints_g[k+cnt]+7;
                    triangleX[2] = xpoints_g[k+cnt+1]+7;
                    triangleY[0] = ypoints_g[k+1]+7;
                    triangleY[1] = ypoints_g[k+cnt]+7;
                    triangleY[2] = ypoints_g[k+cnt+1]+7;
                    Triangle tr_rev = new Triangle(triangleX, triangleY, 3);
                    trianglesArr[i++] = tr_rev;
                    polyArr.add(trianglesArr[i-1].getPolygon());
                }
                k++;
            }
            cnt--;
            lim = k + cnt;
            k++;
        }

        k = 0;
        lim = size;
        cnt = size + 1;

       for (int z = 0; z < size; z++) {
            while (k < lim) {
                triangleX[0] = xpoints_g[k]+7;
                triangleX[1] = xpoints_g[k+1]+7;
                triangleX[2] = xpoints_g[k+cnt]+7;
                triangleY[0] = ypoints_g[k]+7;
                triangleY[1] = ypoints_g[k+1]+7;
                triangleY[2] = ypoints_g[k+cnt]+7;

                Line line1 = new Line(triangleX[0], triangleY[0], triangleX[1], triangleY[1]);
                Line line2 = new Line(triangleX[1], triangleY[1], triangleX[2], triangleY[2]);
                Line line3 = new Line(triangleX[2], triangleY[2], triangleX[0], triangleY[0]);
                linesArr.add(line1);
                linesArr.add(line2);
                linesArr.add(line3);
                k++;
            }
            cnt--;
            lim = k + cnt;
            k++;
        }

        trianglesArray = trianglesArr;
        polyArray = polyArr;
        linesArray = linesArr;

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        repaintGameboard();

        for (int i = 0; i < trianglesArray.length; i++) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(11));
            g.setColor(Color.BLACK);
            g.drawPolygon(trianglesArray[i].getXpoints(), trianglesArray[i].getYpoints(), 3);
            int[] tmpX = trianglesArray[i].getXpoints();
            int[] tmpY = trianglesArray[i].getYpoints();
            g.setColor(colorTriangle[i]);
            if (tmpY[0] == tmpY[1]) {
                tmpX[0] += 6;
                tmpX[1] -= 6;
                tmpY[0] -= 4;
                tmpY[1] -= 4;
                tmpY[2] += 6.5;
            }
            else {
                tmpX[1] += 7;
                tmpX[2] -= 8;
                tmpX[0] += 7;
                //tmpY[0] -= 1;
                tmpY[1] += 4;
                tmpY[2] += 4;
            }
            g.fillPolygon(tmpX, tmpY, 3);

        }

        for (Line l : linesArray) {
          g.setColor(colorLine[linesArray.indexOf(l)]);
          Graphics2D g2 = (Graphics2D) g;
          g2.setStroke(new BasicStroke(3));
          if (l.getY1() == l.getY2()) {
              g2.drawLine(l.getX1()+2, l.getY1()+1, l.getX2()+2, l.getY2()+1);
          }
          else if (l.getY1() < l.getY2()){
              g2.drawLine(l.getX1()-2, l.getY1(), l.getX2()-2, l.getY2());
          }
          else {
               g2.drawLine(l.getX1()+1, l.getY1(), l.getX2()-1, l.getY2());
          }
      }

        int cnt = size + 1;
        int cnt1 = size + 1;
        int k = 0;
        int l = 0;
        for (int i = 0; i < npoints; i++) {
            g.setColor(new Color(119,136,153));
            if (i == k + cnt || i == 0) {
                if (i == npoints-1) {
                    g.fillOval(xpoints_g[i]-4, ypoints_g[i]-10, size > 20 ? 17:22, size > 20 ? 17:22);
                    continue;
                }
                g.fillOval(xpoints_g[i]-10, ypoints_g[i]-3, size > 20 ? 17:22, size > 20 ? 17:22);

                k += cnt;
                cnt--;
            }
            else if (i == cnt1 + l - 1 || i == size) {
                g.fillOval(xpoints_g[i]+3, ypoints_g[i]-3, size > 20 ? 17:22, size > 20 ? 17:22);
                l += cnt1;
                cnt1--;

            }
            else {

                g.fillOval(xpoints_g[i]-6, ypoints_g[i]-3, size > 20 ? 17:22, size > 20 ? 17:22);
            }
        }
    }

    public static void display(int size) {
        JFrame f = new JFrame("SuperFlowerWars64");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1280, 1080);
        JPanel gameboardPanel = new Gameboard(f, size);
        JPanel scores = new Scores();

        gameboardPanel.add(scores);
        gameboardPanel.add(new Buttons());
        gameboardPanel.setBackground(new Color(255,140,0));
        scores.setBackground(new Color(255,140,0));

        f.add(gameboardPanel);
        f.setVisible(true);
    }
}
