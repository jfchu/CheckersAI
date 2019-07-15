import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class AIButton extends JButton implements ActionListener {

	public final int scoreNum = 10;
	public Board AIBoard = null;

	public AIButton() {

		super(new ImageIcon("/home/chutx/Downloads/android.png"));
		this.addActionListener(this);

	}

	public void printBoard(String[][] board) {

		for (int i = 0; i < 8; i++) {

			for (int j = 0; j < 8; j++) {

				System.out.print(board[i][j]);

			}

			System.out.println();

		}
	}

	public int[] getScoreIndices(double[] scoreArray, int num) {

		double[] sortedScores = scoreArray.clone();
		Arrays.sort(sortedScores);
		int[] indexArray;

		if (scoreArray.length <= num) {

			indexArray = new int[scoreArray.length];

			for (int i = 0; i < scoreArray.length; i++) {

				indexArray[i] = i;

			}
		} else {

			indexArray = new int[num];
			double cutOffScore = sortedScores[sortedScores.length - num];
			int addedIndices = 0;

			if (sortedScores[sortedScores.length - 1] > cutOffScore) {

				for (int i = 0; i < scoreArray.length; i++) {

					if (scoreArray[i] > cutOffScore) {

						indexArray[addedIndices] = i;
						addedIndices++;

					}
				}

				ArrayList<Integer> tieIndices = new ArrayList<Integer>();

				for (int i = 0; i < scoreArray.length; i++) {

					if (scoreArray[i] == cutOffScore) {

						tieIndices.add(new Integer(i));

					}
				}

				double[] randomArray = new double[tieIndices.size()];
				for (int i = 0; i < tieIndices.size(); i++) {

					randomArray[i] = Math.random();

				}

				double[] sortedRandoms = randomArray.clone();
				Arrays.sort(sortedRandoms);
				double randomCutOff = sortedRandoms[num - addedIndices - 1];

				int[] randomIndices = new int[num - addedIndices];
				int addedTies = 0;

				for (int i = 0; i < randomArray.length; i++) {

					if (randomArray[i] <= randomCutOff) {

						randomIndices[addedTies] = i;
						addedTies++;

						if (addedTies == num - addedIndices) {

							break;

						}
					}
				}

				for (int i = 0; i < randomIndices.length; i++) {

					indexArray[addedIndices] = tieIndices.get(randomIndices[i])
							.intValue();

				}
			}
		}

		return indexArray;

	}

	public void actionPerformed(ActionEvent e) {

		if (AIBoard == null) {

			System.out.println("Board not initialized!");

		} else {

			String color;

			if (AIBoard.blackTurn) {

				color = "B";

			} else {

				color = "R";

			}

			Node[] AIMoves = CheckersAI
					.findMyMoves(AIBoard.checkerArray, color);

			if (AIMoves.length == 0) {

				if (color.equals("B")) {

					System.out.println("RED WINS");

				} else {

					System.out.println("BLACK WINS");

				}
			} else {

				ArrayList<Node> nodeList = new ArrayList<Node>();

				for (int i = 0; i < AIMoves.length; i++) {

					CheckersAI.leafCounter(nodeList, AIMoves[i]);

				}

				double[] scoreArrayOne = new double[nodeList.size()];

				for (int i = 0; i < scoreArrayOne.length; i++) {

					scoreArrayOne[i] = CheckersAI.getScore(
							nodeList.get(i).leafBoard, color);

				}

				int[] indexArrayOne = getScoreIndices(scoreArrayOne, scoreNum);
				double[] scoreArrayTwo = new double[indexArrayOne.length];
				String nextColor;

				if (color.equals("B")) {

					nextColor = "R";

				} else {

					nextColor = "B";

				}

				for (int i = 0; i < indexArrayOne.length; i++) {

					Node[] userMoves = CheckersAI
							.findMyMoves(
									nodeList.get(indexArrayOne[i]).leafBoard,
									nextColor);

					if (userMoves.length == 0) {

						scoreArrayTwo[i] = -1000000.0;

					} else {

						ArrayList<Node> tempNodeList = new ArrayList<Node>();

						for (int j = 0; j < userMoves.length; j++) {

							CheckersAI.leafCounter(tempNodeList, userMoves[j]);

						}

						double[] tempScoreArray = new double[tempNodeList
								.size()];

						for (int j = 0; j < tempScoreArray.length; j++) {

							tempScoreArray[j] = CheckersAI.getScore(
									tempNodeList.get(j).leafBoard, nextColor);

						}

						Arrays.sort(tempScoreArray);
						scoreArrayTwo[i] = tempScoreArray[tempScoreArray.length - 1];

					}
				}

				double[] negScoreArrayTwo = new double[scoreArrayTwo.length];

				for (int i = 0; i < negScoreArrayTwo.length; i++) {

					negScoreArrayTwo[i] = 0.0 - scoreArrayTwo[i];

				}

				int miniMax = getScoreIndices(negScoreArrayTwo, 1)[0];
				AIBoard.checkerArray = nodeList.get(miniMax).leafBoard;
				AIBoard.blackTurn = !AIBoard.blackTurn;
				AIBoard.repaint();

			}
		}
	}
}
