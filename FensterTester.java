import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

class Scores extends JPanel {

	//private String scoreRed = String.parseInt(getPoints(PlayerColor.red));
	//private String scoreBlue = String.parseInt(getPoints(PlayerColor.blue));
	private String scoreRed = String.valueOf(2);
	private String scoreBlue = String.valueOf(1);

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.RED);
		g.fillRect(15, 25, 100, 100);
		g.setColor(Color.WHITE);
		g.setFont(new Font ("Cooper Black", 1, 50));
		g.drawString(scoreRed, 48, 93);
		g.setColor(Color.BLUE);
		g.fillRect(125, 25, 100, 100);
		g.setColor(Color.WHITE);
		g.drawString(scoreBlue, 158, 93);
	}
}

class WithdrawButton extends JButton {
	public WithdrawButton() {
		JButton withdrawButton = new JButton("Aufgeben");
		withdrawButton.setPreferredSize(new Dimension(120, 60));
	}
}


public class FensterTester {

    public static void main(String[] args) {
		int size;
		if (args.length == 0)
			size = 3;
		else
        	size = Integer.parseInt(args[0]);

        JFrame frame = new tmpFrame("SuperFlowerWars64");
		JPanel mainPanel = new JPanel();
		frame.add(mainPanel);
		mainPanel.setSize(frame.getWidth(), frame.getHeight());
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel scoresPanel = new Scores();
		JPanel gameboardPanel = new JPanel();
		JPanel withdrawPanel = new JPanel();
		//scoresPanel.setSize(new Dimension(frame.getWidth(), 140));

		JButton withdrawButton = new JButton("Aufgeben");
		try {
			Image img = ImageIO.read(new File("aufgeben.png"));
			withdrawButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		withdrawButton.setPreferredSize(new Dimension(150, 40));
		withdrawPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		withdrawPanel.add(withdrawButton, BorderLayout.EAST);
		withdrawPanel.setSize(frame.getWidth(), 10);
		//withdrawPanel.setSize(withdrawPanel.getPreferredSize());
		//withdrawPanel.validate();
		scoresPanel.setSize(frame.getWidth(), 180);
		JButton game = new JButton("GAME");
		gameboardPanel.add(game);
		mainPanel.add(scoresPanel);
		scoresPanel.add(withdrawPanel);
		mainPanel.add(gameboardPanel);
		System.out.println(withdrawPanel.getHeight());
		System.out.println(withdrawPanel.getWidth());
		System.out.println(gameboardPanel.getHeight());
		System.out.println(gameboardPanel.getWidth());

		//Spielbrett s = new Spielbrett(20, 20);
		//gameboardPanel.add(s);
		//frame.pack();
		frame.setVisible(true);

    }
}
