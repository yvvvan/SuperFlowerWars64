import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Dots extends JPanel {

  private int x;   // leftmost pixel in circle has this x-coordinate
  private int y;   // topmost  pixel in circle has this y-coordinate

  public Dots(int x, int y) {
      this.x = x;
      this.y = y;
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int height = screenSize.height;
      int width = screenSize.width * 2/3;
      setSize(new Dimension(width, height));
      setVisible(true);
      //repaint();
  }

  public String toString() {
      return "(" + x + "," + y + ")";
  }

  public int getXCoord() {
      return x;
  }

  public int getYCoord() {
      return y;
  }

  public void paint(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillOval(x,y,15,15);
  }
}

class Lines extends JPanel {
    private int x1, y1;
    private int x2, y2;

    public Lines(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width * 2/3;
        setSize(new Dimension(width, height));
        setVisible(true);
        //repaint();
     }

     public void paint(Graphics g) {
       g.setColor(Color.BLACK);
       g.drawLine(x1,y1,x2,y2);
     }
}


class DrawPolygon extends JPanel {

    private Polygon poly;

    public DrawPolygon(Polygon poly) {
        this.poly = poly;
        setVisible(true);
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillPolygon(poly);
    }
}

public class Spielbrett {
    public static void main(String args[]) {
        JFrame frame = new tmpFrame("HAHAHAHH");
        int h = frame.getHeight();
        int w = frame.getWidth();
        int size;
        if (args.length == 0)
          size = 3;
        else {
          size = Integer.parseInt(args[0]);
		  if (size < 3 || size > 30) {
              System.out.println("Wrong input!");
          }
		}
        int npoints = (size+1)*(size+2)/2;
        int lenLineHor = 1120/(size+1);
        int lenLineVer = 936/(size+1);
        int i = size + 1;
        int j = 0;
        int k = 0;
        int cnt = 0;
		int tmp = (w - lenLineHor*size)/2;
        Dots[] dotsArr = new Dots[npoints];
        int[] xpoints = new int[npoints];
        int[] ypoints = new int[npoints];
        while (i > 0) {
            j = 0;
            while(j < i) {
                Dots d = new Dots(tmp + j*lenLineHor + k*lenLineHor/2, 999 - k*lenLineVer);
                dotsArr[cnt] = d;
                xpoints[cnt] = dotsArr[cnt].getXCoord();
                ypoints[cnt] = dotsArr[cnt].getYCoord();
                frame.add(d);
                cnt++;
                j++;
            }
            k++;
            i--;
        }

         for (int l = 0; l < npoints; l++) {
             System.out.println(dotsArr[l]);
         }
         System.out.println(npoints);
        // Polygon poly = new Polygon(xpoints, ypoints, npoints);
         //DrawPolygon dPoly = new DrawPolygon(poly);
         //frame.add(dPoly);
   }
}
