package app.testing;
import app.gameboard.*;
import java.awt.EventQueue;

public class TestGameboard /*implements Requestable*/{
    public static void main(String[] args) {
        int size;
        if (args.length == 0)
          size = 3;
        else {
          size = Integer.parseInt(args[0]);
          if (size < 3 || size > 30) {
              throw new IllegalArgumentException("Die Größe des Spielbretts liegt nicht in [3,30]");
          }
        }

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Gameboard.display(size);
            }
        });

        //public Move request() { }
    }
}
