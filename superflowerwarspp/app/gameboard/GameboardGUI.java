package app.gameboard;
import flowerwarspp.preset.*;
import app.superflowerwars64.*;
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
import java.lang.*;
// Royal blue 65 105 225
//
/**
    * Die Klasse GameboardGUI ist für die graphische Ausgabe des Spielbretts.
    * @author Viktoriya Pak
*/

public class GameboardGUI extends JPanel {
    /**
        * Die Höhe des Spielbrettpanels.
        * Dieser Wert ist veränderbar und hängt von der Größe des Fensters
    */
    private int panelHeight;
    /**
        * Die Breite des Spielbrettpanels.
        * Dieser Wert ist veränderbar und hängt von der Größe des Fensters
    */
    private int panelWidth;
    /**
        * Die Größe des Spielbretts (von 3 bis 30)
    */
    private int size;
    /**
        * Der Array der X-Koordinaten von allen Punkten auf dem Spielbrett
    */
    private int[] xpoints_g;
    /**
        * Der Array der Y-Koordinaten von allen Punkten auf dem Spielbrett
    */
    private int[] ypoints_g;
    /**
        * Die Anzahl von Punkten auf dem Spielbrett
    */
    private int npoints;
    /**
        * Der Array enthält alle Dreicke, die Felder des Spielbretts repräsentieren
    */
    private Triangle[] trianglesArray;
    /**
        * Collection enthält alle Polygone zu dementsprechendem Dreieck
    */
    private ArrayList<Polygon> polyArray = new ArrayList<Polygon>();
    /**
        * Frame für das Spielbrett
    */
    private JFrame frame;
    /**
        * Der Array enthält Farben von allen Dreicken
    */
    private Color[] colorTriangle;
    /**
        * Der Array enthält Farben von allen Linien (Gräben)
    */
    private Color[] colorLine;
    /**
        * Collection enthält alle Linien, die Gräben des Spielbretts repräsentieren
    */
    private ArrayList<Line> linesArray = new ArrayList<Line>();
    /**
        * Collection enthält Positionen von allen Feldern des Spielbretts
    */
    private final ArrayList<Position[]> trianglesPositionsArray = new ArrayList<Position[]>();
    /**
        * Collection enthält Positionen von allen Gräben des Spielbretts
    */
    private final ArrayList<Position[]> linesPositionsArray = new ArrayList<Position[]>();
    /**
        * Die erste Blume eines Zuges, die nach dem Mausklick engepflanzt werden kann
    */
    private Flower firstFlower;
    /**
        * Die zweite Blume eines Zuges, die nach dem Mausklick engepflanzt werden kann
    */
    private Flower secondFlower;
    /**
        * Ein Graben, der nach dem Mausklick engepflanzt werden kann
    */
    private Ditch ditch;
    /**
        * Ein Zug, den Spieler gemacht hat
    */
    private Move newMove;

    private MyViewer myViewer;

    private GUI gui;

    private int indexFirstFlower;

    private int indexSecondFlower;

    private int indexDitch;
    //private PlayerColor playerColor;
    /**
        * Fördert und liefert einen Zug
        * @return einen neuen Zug
    */

    /*@Override
    public synchronized Move request() {
       Collection<Move> posMoves = myViewer.getPossibleMoves();
       do {
           try{
             GameboardGUI.class.wait();
           } catch (Exception e) {
             e.printStackTrace();
           }
       } while (!posMoves.contains(getNewMove()));
      return getNewMove();
  }*/

    /**
        * Getter-Methode für den Move-Objekt
    */

    public Move getNewMove() {
        return newMove;
    }

    /**
        * Setter-Methode für die erste Blume in einem Zug
    */

    public void setFirstFlower(Flower firstFlower) {
        this.firstFlower = firstFlower;
    }

    /**
        * Setter-Methode für die zweite Blume in einem Zug
    */

    public void setSecondFlower(Flower secondFlower) {
        this.secondFlower = secondFlower;
    }

    /**
        * Setter-Methode für den Graben in einem Zug
    */

    public void setDitch(Ditch ditch) {
        this.ditch = ditch;
    }

    public void setMyViewer(MyViewer myViewer) {
        this.myViewer = myViewer;
    }

    public void paintFlowerMove(int indTriangle) {
        Polygon poly = polyArray.get(indTriangle);
        trianglesArray[indTriangle].setTriangleClicked(true);
        colorTriangle[indTriangle] = new Color(204,51,51);
        Graphics gr = getGraphics();
        gr.setColor(new Color(204,51,51));

        int[] tmpX = trianglesArray[polyArray.indexOf(poly)].getXpoints();
        int[] tmpY = trianglesArray[polyArray.indexOf(poly)].getYpoints();

        if (tmpY[0] == tmpY[1]) {
            tmpX[0] -= 1;
            tmpX[2] -= 1;
        }
        else {
            tmpX[2] -= 2;
            tmpX[0] -= 8;
            tmpY[0] -= 9;
            tmpY[2] += 2;
            tmpY[1] += 1;
            }
        gr.fillPolygon(tmpX, tmpY, 3);
    }

    public void paintDitchMove(int indLine) {
        Line l = linesArray.get(indLine);
        l.setLineClicked();
        colorLine[indLine] = new Color(222, 49, 99);
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
    }

    public void handleLastMove(Move lastMove) {
        //Collection<Move> posMoves = myViewer.getPossibleMoves();
        //System.out.println("SIZE OF COLLECTION: " + posMoves.size());
        /*do {
            try{
              this.wait();
            } catch (Exception e) {
              e.printStackTrace();
            }
        } while (!posMoves.contains(getNewMove()));*/
        System.out.println("\nMOVE TYPE: " + lastMove.getType() + "\n");
        ArrayList<Position> pos = new ArrayList<Position>();
        Flower flower1;
        Flower flower2;
        Ditch ditch;
        Position[] posArr;
        if (lastMove.getType() == MoveType.Flower) {
            flower1 = lastMove.getFirstFlower();
            pos.add(flower1.getFirst());
            pos.add(flower1.getSecond());
            pos.add(flower1.getThird());
            Collections.sort(pos);
            posArr = pos.toArray(new Position[pos.size()]);
            for (Position[] posCnt : trianglesPositionsArray) {
                if (posCnt[0] == posArr[0] && posCnt[1] == posArr[1] && posCnt[2] == posArr[2]) {
                    indexFirstFlower = trianglesPositionsArray.indexOf(posCnt);
                    colorTriangle[indexFirstFlower] = ((myViewer.getTurn() == PlayerColor.Red) ? (new Color(204,51,51)) : (new Color(65, 105, 225)));
                    System.out.println("First Flower Clicked: " + indexFirstFlower);
                    break;
                }
            }
            pos.clear();
            if (lastMove.getSecondFlower() != null) {
                flower2 = lastMove.getSecondFlower();
                pos.add(flower2.getFirst());
                pos.add(flower2.getSecond());
                pos.add(flower2.getThird());
                Collections.sort(pos);
                posArr = pos.toArray(new Position[pos.size()]);
                for (Position[] posCnt : trianglesPositionsArray) {
                    if (posCnt[0] == posArr[0] && posCnt[1] == posArr[1] && posCnt[2] == posArr[2]) {
                        indexSecondFlower = trianglesPositionsArray.indexOf(posCnt);
                        colorTriangle[indexSecondFlower] = ((myViewer.getTurn() == PlayerColor.Red) ? (new Color(204,51,51)) : (new Color(65, 105, 225)));
                        System.out.println("Second Flower Clicked: " + indexSecondFlower);
                        paintFlowerMove(indexSecondFlower);
                        break;
                    }
                }
                pos.clear();
            }

        }
        else if (lastMove.getType() == MoveType.Ditch) {
            ditch = lastMove.getDitch();
            pos.add(ditch.getFirst());
            pos.add(ditch.getSecond());
            Collections.sort(pos);
            posArr = pos.toArray(new Position[pos.size()]);
            for(int z = 0; z < posArr.length; z++) {
                System.out.println(posArr[z]);
            }
            for (Position[] posCnt : linesPositionsArray) {
                if ((posCnt[0] == posArr[0]) && (posCnt[1] == posArr[1])) {
                    indexDitch = linesPositionsArray.indexOf(posCnt);
                    System.out.println("\nDITCH INDEX = " + indexDitch + "\n");
                    //colorLine[indexDitch] = ((myViewer.getTurn() == PlayerColor.Red) ? (new Color(222, 49, 99)) : (new Color(31, 117, 254)));
                    paintDitchMove(indexDitch);
                    break;
                }
            }
            pos.clear();
        }
        else if (lastMove.getType() == MoveType.Surrender) {
            Graphics gr = getGraphics();
            gr.setFont(new Font("Arial Black", Font.BOLD, 60));
            gr.drawString("Surrender", 100, 500);
        }
        else {
            Graphics gr = getGraphics();
            gr.drawString("Someone won", 100, 100);
            System.out.println("Won");
        }
    }
    /**
        * Konstruktor der Klasse.
        * Enthält {@link WindowStateListener}, der neue Werte für {@link #panelWidth} und {@link #panelHeight} einsetzt,
        * wenn das Fenster maximiert oder minimiert wird.
        * Enthält {@link ComponentListener}, der neue Werte für {@link #panelWidth} und {@link #panelHeight} einsetzt,
        * wenn die Größe des Fensters veränert wird.
        * Enthält {@link MouseListener}, der auf Klicke auf Feler/Buttons des Spielbretts reagiert (die entsprechende Zugtypen werden erzeugt)
        * Wenn ein Feld/Graben angeklickt wird, werden den entsprechneden Objekten neue Farben zugewiesen und das Spielbrett wird
        * von neuem gezeichnet.
        * @param frame Ein Frame, an den das WindowEvent angehängt wird
        * @param size Die Größe des Spielbretts
    */

    public GameboardGUI(JFrame frame, int size, GUI gui) {
        this.gui = gui;
        //this.myViewer = myViewer;
        WindowStateListener listener = new WindowAdapter() {

            public void windowStateChanged(WindowEvent evt) {
                int oldState = evt.getOldState();
                int newState = evt.getNewState();

                if ((oldState & Frame.MAXIMIZED_BOTH) == 0 && (newState & Frame.MAXIMIZED_BOTH) != 0) {
                    panelHeight = frame.getHeight();
                    panelWidth = frame.getWidth();
                }
                else if ((oldState & Frame.MAXIMIZED_BOTH) != 0 && (newState & Frame.MAXIMIZED_BOTH) == 0) {
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

        init_position();

        repaintGameboard();
        /*int mkl = 0;
        for (Position[] l : linesPositionsArray) {
            System.out.println((mkl++) + " : " + l[0] + "," + l[1]);
        }*/

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panelHeight = frame.getHeight();
                panelWidth = frame.getWidth();
            }
        });
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("Here");
                boolean alreadyClickedLine = false;
                double shapeSize = 5;
                double xClick = e.getX()-shapeSize/2;
                double yClick = e.getY()-shapeSize/2;
                for (Line l : linesArray) {
                  if (l.getLine().intersects(xClick, yClick, shapeSize, shapeSize) && (!linesArray.get(linesArray.indexOf(l)).isLineClicked())) {
                    System.out.println("Clicked Line");
                    alreadyClickedLine = true;
                    int indexLine = linesArray.indexOf(l);
                    synchronized(this) {
                          if (ditch == null) {
                              Ditch d = new Ditch(linesPositionsArray.get(indexLine)[0], linesPositionsArray.get(indexLine)[1]);
                              setDitch(d);
                              newMove = new Move(d);
                              ditch = null;
                              handleLastMove(newMove);
                              notify();
                          }
                    }
                    break;
                  }
                }

                Point p = new Point(e.getX(), e.getY());

                for (Polygon poly : polyArray) {
                    if (!alreadyClickedLine && poly.contains(p) && (!trianglesArray[polyArray.indexOf(poly)].isTriangleClicked())) {

                        System.out.println("Clicked triangle");
                        //int indexTriangle = polyArray.indexOf(poly);
                        synchronized(gui) {
                            if (firstFlower == null) {
                              Flower fstFl = new Flower(trianglesPositionsArray.get(indexFirstFlower)[0], trianglesPositionsArray.get(indexFirstFlower)[1], trianglesPositionsArray.get(indexFirstFlower)[2]);
                              setFirstFlower(fstFl);
                              newMove = new Move(firstFlower, null);
                              handleLastMove(newMove);
                              System.out.println("Flowers Clicked: " + indexFirstFlower );
                            }
                            else if (secondFlower == null) {
                              Flower sndFl = new Flower(trianglesPositionsArray.get(indexSecondFlower)[0], trianglesPositionsArray.get(indexSecondFlower)[1], trianglesPositionsArray.get(indexSecondFlower)[2]);
                              setSecondFlower(sndFl);
                              newMove = new Move(firstFlower, secondFlower);
                              firstFlower = null;
                              secondFlower = null;
                              handleLastMove(newMove);
                              System.out.println("Flowers Clicked: " + indexFirstFlower + "   " + indexSecondFlower );
                              //notify();
                            }
                        }
                        //paintFlowerMove(indexSecondFlower);
                        break;
                    }
                }

                int cnt = size + 1;
                int cnt1 = size + 1;
                int k = 0;
                int l = 0;
                Graphics g = getGraphics();
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
        }); // END mouseClicked
    } // END GAMEBOARD KONSTRUKTOR

    /**
        * Initialisiert {@link Position}-Arrays für alle Felder und Gräben.
        * Wird nur einmal im Konstruktior der Klasse aufgerufen
    */
    protected void init_position() {
        System.out.println("Initialized positions");
        int lim = size;
        int cnt = size+1;
        int k = 0;
        int i = 0;
        int col = 1;
        int row = 1;
        for (int z = 0; z < size; z++) {
            while (k < lim) {
                Position[] posArrayTriangle = new Position[3];
                Position pos = new Position(col, row);
                posArrayTriangle[0] = pos;
                pos = new Position(col+1, row);
                posArrayTriangle[1] = pos;
                pos = new Position(col,row+1);
                posArrayTriangle[2] = pos;
                col++;
                trianglesPositionsArray.add(posArrayTriangle);
                if (k < lim-1) {
                    Position[] posArrayTriangle1 = new Position[3];
                    Position pos1 = new Position(col, row);
                    posArrayTriangle1[0] = pos1;
                    pos1 = new Position(col-1, row+1);
                    posArrayTriangle1[1] = pos1;
                    pos1 = new Position(col,row+1);
                    posArrayTriangle1[2] = pos1;
                    trianglesPositionsArray.add(posArrayTriangle1);
                }
                k++;
            }
            col = 1;
            row++;
            cnt--;
            lim = k + cnt;
            k++;
        }

        k = 0;
        lim = size;
        cnt = size + 1;
        col = 1;
        row = 1;
       for (int z = 0; z < size; z++) {
            while (k < lim) {
                Position[] posArrayLine = new Position[2];
                Position pos = new Position(col, row);
                posArrayLine[0] = pos;
                pos = new Position (col+1, row);
                posArrayLine[1] = pos;
                Position[] posArrayLine1 = new Position[2];
                linesPositionsArray.add(posArrayLine);
                posArrayLine1[0] = new Position(col+1, row);
                posArrayLine1[1] = new Position(col, row+1);
                linesPositionsArray.add(posArrayLine1);
                Position[] posArrayLine2 = new Position[2];
                posArrayLine2[0] = new Position(col, row);
                posArrayLine2[1] = new Position(col, row+1);
                linesPositionsArray.add(posArrayLine2);
                k++;
                col++;
            }
            col = 1;
            row++;
            cnt--;
            lim = k + cnt;
            k++;
        }
    }


    /**
        * Aktualisiert die Koordinten für alle Objekte auf dem Spielbrett (nach Window-, Component- oder Mouse-Ereignissen)
        * für das weitere Zeichnen von {@link #paintComponent}
    */

    protected void repaintGameboard() {
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

    /**
        * Stellt Spielbrett-Objekte (Punkte, Linien für Gräben und Polygone für Dreieckfelder) graphisch dar.
    */

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
}
