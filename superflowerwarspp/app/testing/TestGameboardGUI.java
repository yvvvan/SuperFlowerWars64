package app.testing;
import app.gameboard.*;
import java.awt.EventQueue;

public class TestGameboardGUI /*implements Requestable*/{

    public static void main(String[] args) {
        int size;
        if (args.length == 0)
          size = 3;
        else {
          size = Integer.parseInt(args[0]);
          if (size < 3 || size > 30) {
              System.out.println("MAIN");
              throw new IllegalArgumentException("Die Größe des Spielbretts liegt nicht in [3,30]");
          }
        }

        //gggui.request();
        // GUI gggui = new GUI(size);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               // GameboardGUI.display(size);
               GUI gggui = new GUI(size);
            }
        });
    }
}
