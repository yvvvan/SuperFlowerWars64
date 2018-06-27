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
     }

     public void paintComponent(Graphics g) {;
       g.drawLine(x1,y1,x2,y2);
       Graphics2D g2 = (Graphics2D) g;
       g2.setStroke(new BasicStroke(6));
       g2.setColor(Color.BLACK);
       g2.drawLine(x1,y1,x2,y2);
     }
}

/*
class DrawPolygon extends JPanel {

    private int[] xpoints;
    private int[] ypoints;
    private int npoints;

    public DrawPolygon(int[] xpoints, int[] ypoints, int npoints) {
        this.xpoints = xpoints;
        this.ypoints = ypoints;
        this.npoints = npoints;
        setVisible(true);
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawPolygon(xpoints, ypoints, npoints);
    }
}
*/
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
              throw new IllegalArgumentException("Die Größe des Spielbretts liegt nicht in [3,30]");
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
        int[] blabla = new int[size+1];
        int[] blablabla = new int[size+1];
        // Drawing points
        while (i > 0) {
            j = 0;
            while(j < i) {
                Dots d = new Dots(tmp + j*lenLineHor + k*lenLineHor/2, 999 - k*lenLineVer);
                dotsArr[cnt] = d;
                xpoints[cnt] = (dotsArr[cnt].getXCoord())+7;
                ypoints[cnt] = (dotsArr[cnt].getYCoord())+7;
                frame.add(d);
                cnt++;
                j++;
            }
            k++;
            i--;
        }

        // Drawing horizontal lines
        int counter = size;
        j = 0;
        i = 0;
        while(counter < npoints) {
            while(j < counter) {
                Lines l = new Lines(xpoints[j], ypoints[j], xpoints[j+1], ypoints[j+1]);
                frame.add(l);
                j++;
            }
            counter += size-i;
            i++;
            j++;
        }

        // Drawing diagonal lines (from left to right)
        cnt = 0;
        j = 0;
        counter = npoints;
        while (cnt < size) {
            int step = size + 1;
            while((j+step) < counter) {
                Lines l = new Lines(xpoints[j], ypoints[j], xpoints[j+step], ypoints[j+step]);
                System.out.println("j = " + j + "   j+step = " + (j+step));
                System.out.println("Counter = " + counter);
                System.out.println("Step = " + step);
                j += step;
                step--;
                frame.add(l);
            }
            cnt++;
            j = cnt;
            counter -= cnt;
         }
         // Drawing diagonal lines (from left to right)
         cnt = 0;
         j = size;
         counter = npoints;
         while (cnt < size) {
             int step = size;
             while((j+step) < counter && step > 0) {
                 Lines l = new Lines(xpoints[j], ypoints[j], xpoints[j+step], ypoints[j+step]);
                 j += step;
                 step--;
                 frame.add(l);
             }
             cnt++;
             j = size-cnt;
             counter -= cnt;
         }
    }

}
