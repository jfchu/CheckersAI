import java.awt.*;
import javax.swing.*;

public class driver extends JComponent {

	public static void main(String[] args) {

		// sets look and feel as the operating system's
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {

		}

		JFrame frame = new JFrame("Checkers");
		JPanel panel = new JPanel();

		AIButton button = new AIButton();
		panel.add(button);
		frame.add(panel, BorderLayout.SOUTH);

		JPanel consolePanel = new JPanel();
		frame.add(consolePanel, BorderLayout.EAST);
		JTextArea consoleOutput = new JTextArea(5, 20);
		consoleOutput.setMargin(new Insets(5, 5, 5, 5));
		consoleOutput.setEditable(false);
		consolePanel.add(new JScrollPane(consoleOutput), BorderLayout.CENTER);

		MessageConsole mc = new MessageConsole(consoleOutput);
		mc.redirectOut(Color.BLUE, null);
		mc.redirectErr(Color.RED, null);
		mc.setMessageLines(200);
		System.out.println("CONSOLE");

		Board board = new Board();
		button.AIBoard = board;
		frame.add(board, BorderLayout.CENTER);
		frame.setSize(850, 660);
		frame.setVisible(true);
		frame.setResizable(false); // frame size is not adjustable to prevent
									// bugs
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allows window
																// to be closed
																// by pressing
																// exit button

	}
}