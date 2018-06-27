import java.awt.event.*;

public class SpielbrettMouseEvent extends MouseAdapter {
    private int x_coord;
    private int y_coord;
    public void mouseClicked(MouseEvent e) {
        x_coord = e.getX();
        y_coord = e.getY();
        //System.out.println("x = " + x_coord);
        //System.out.println("y = " + y_coord);
    }
    public int getXCoordinate() {
        return x_coord;
    }
    public int getYCoordinate() {
        return y_coord;
    }
}
