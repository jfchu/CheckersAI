import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

// this class defines and updates the Board component and includes rules executed by mouse events
public class Board extends JComponent implements MouseListener {

	private final static int dimension = 50;
	private final static int squareSize = (int) (dimension * 1.3);
	public String[][] checkerArray;
	private String[][] moveArray;
	private int mouseX; // mouse coordinates of the user's clicks
	private int mouseY;
	public boolean blackTurn = true;
	private int indexX = -1;
	private int indexY = -1;
	private boolean hasJump = false;
	private boolean falseJump = true;
	public boolean jumped = false; // used to check if user has jumped already
									// on this turn
	private boolean boardHasJump; // used to check if the user has a jump
	private int numberOfPieces; // used to check if a user has no pieces

	public Board() {

		super();

		checkerArray = new String[8][8];
		moveArray = new String[8][8];

		for (int i = 0; i < 3; i++) {

			for (int j = 0; j < 8; j++) {

				if ((i + j) % 2 != 0) {

					checkerArray[i][j] = "RR";

				} else {

					checkerArray[i][j] = "-";

				}
			}
		}

		for (int i = 3; i < 6; i++) {

			for (int j = 0; j < 8; j++) {

				checkerArray[i][j] = "-";

			}
		}

		for (int i = 5; i < 8; i++) {

			for (int j = 0; j < 8; j++) {

				if ((i + j) % 2 == 0) {

					checkerArray[i][j] = "-";

				} else {

					checkerArray[i][j] = "BR";

				}
			}
		}

		for (int i = 0; i < 8; i++) {

			for (int j = 0; j < 8; j++) {

				moveArray[i][j] = "-";

			}
		}

		this.addMouseListener(this);

	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		for (int i = 0; i < 8; i++) {

			for (int j = 0; j < 8; j++) {

				Color myRed = new Color(140, 0, 32);
				Color myBlue = new Color(0, 75, 255);

				if ((i + j) % 2 == 0) {
					g.setColor(Color.GRAY);
				} else {
					g.setColor(Color.WHITE);
				}

				if (moveArray[i][j] == "MOVE") {
					g.setColor(Color.GREEN);
				}

				if (moveArray[i][j] == "SELECTED") {
					g.setColor(myBlue);
				}

				g.fillRect(j * squareSize, i * squareSize, squareSize,
						squareSize);

				if (checkerArray[i][j] == "RR" || checkerArray[i][j] == "RK"
						|| checkerArray[i][j] == "RRJ"
						|| checkerArray[i][j] == "RKJ") {
					g.setColor(myRed);
				}

				if (checkerArray[i][j] == "BR" || checkerArray[i][j] == "BK"
						|| checkerArray[i][j] == "BRJ"
						|| checkerArray[i][j] == "BKJ") {
					g.setColor(Color.BLACK);
				}

				g.fillOval(j * squareSize + 7, i * squareSize + 7, dimension,
						dimension);

				BufferedImage img = null;

				try {
					img = ImageIO.read(new File(
							"/home/chutx/Downloads/crown.png"));
				} catch (IOException e) {

				}

				if (checkerArray[i][j] == "RK" || checkerArray[i][j] == "RKJ") {
					// adds image onto red king checkers
					g.drawImage(img, j * squareSize + 8, i * squareSize + 14,
							null);
				}

				if (checkerArray[i][j] == "BK" || checkerArray[i][j] == "BKJ") {
					// adds image onto black king checkers
					g.drawImage(img, j * squareSize + 8, i * squareSize + 14,
							null);
				}
			}
		}

	}

	// implements MouseListener interface to handle mouse events
	// first 4 methods are not invoked
	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	// invoked whenever the user clicks in the Swing window
	@Override
	public void mouseClicked(MouseEvent e) {

		this.mouseX = e.getX();
		this.mouseY = e.getY();

		if (mouseX > 0 && mouseX < 520 && mouseY > 0 && mouseY < 520) {

			int arrayX = (int) (Math.ceil(mouseX / squareSize));
			int arrayY = (int) (Math.ceil(mouseY / squareSize));

			if (blackTurn) {

				if (isGameOver()) {

					System.out.println("RED WINS");
					return;

				}

				checkBoardForJump();

				if (!jumped) {

					if (searchArray("SELECTED") && searchArray("MOVE")) {

						if (moveArray[arrayY][arrayX].equals("MOVE")) {

							indexSearch(moveArray, "SELECTED");

							if (checkJumpWithoutPaint(indexX, indexY)) {

								if (checkerArray[indexX][indexY].equals("BR")) {

									if (arrayX < indexY) {

										if (arrayY != 0) {

											moveArray[arrayY][arrayX] = "SELECTED";
											checkerArray[arrayY + 1][arrayX + 1] = "-";
											checkerArray[indexX][indexY] = "-";
											checkerArray[arrayY][arrayX] = "BR";
											moveArray[indexX][indexY] = "-";

										} else {

											moveArray[arrayY][arrayX] = "SELECTED";
											checkerArray[arrayY + 1][arrayX + 1] = "-";
											checkerArray[indexX][indexY] = "-";
											checkerArray[arrayY][arrayX] = "BK";
											moveArray[indexX][indexY] = "-";

										}
									} else {

										if (arrayY != 0) {

											moveArray[arrayY][arrayX] = "SELECTED";
											checkerArray[arrayY + 1][arrayX - 1] = "-";
											checkerArray[indexX][indexY] = "-";
											checkerArray[arrayY][arrayX] = "BR";
											moveArray[indexX][indexY] = "-";

										} else {

											moveArray[arrayY][arrayX] = "SELECTED";
											checkerArray[arrayY + 1][arrayX - 1] = "-";
											checkerArray[indexX][indexY] = "-";
											checkerArray[arrayY][arrayX] = "BK";
											moveArray[indexX][indexY] = "-";

										}
									}
								} else {

									if (arrayX < indexY) {

										if (arrayY < indexX) {

											moveArray[arrayY][arrayX] = "SELECTED";
											checkerArray[arrayY + 1][arrayX + 1] = "-";
											checkerArray[indexX][indexY] = "-";
											checkerArray[arrayY][arrayX] = "BK";
											moveArray[indexX][indexY] = "-";

										} else {

											moveArray[arrayY][arrayX] = "SELECTED";
											checkerArray[arrayY - 1][arrayX + 1] = "-";
											checkerArray[indexX][indexY] = "-";
											checkerArray[arrayY][arrayX] = "BK";
											moveArray[indexX][indexY] = "-";

										}

									} else {

										if (arrayY < indexX) {

											moveArray[arrayY][arrayX] = "SELECTED";
											checkerArray[arrayY + 1][arrayX - 1] = "-";
											checkerArray[indexX][indexY] = "-";
											checkerArray[arrayY][arrayX] = "BK";
											moveArray[indexX][indexY] = "-";

										} else {

											moveArray[arrayY][arrayX] = "SELECTED";
											checkerArray[arrayY - 1][arrayX - 1] = "-";
											checkerArray[indexX][indexY] = "-";
											checkerArray[arrayY][arrayX] = "BK";
											moveArray[indexX][indexY] = "-";

										}
									}
								}

								jumped = true;
								hasJump = false;
								checkJumpWithoutPaint(arrayY, arrayX);

								if (!checkJumpWithoutPaint(arrayY, arrayX)) {

									for (int i = 0; i < 8; i++) {

										for (int j = 0; j < 8; j++) {

											moveArray[i][j] = "-";

										}
									}
								}

							} else if (!boardHasJump
									&& !checkJumpWithoutPaint(indexX, indexY)) {

								if (checkerArray[indexX][indexY].equals("BR")) {

									if (arrayY != 0) {

										checkerArray[arrayY][arrayX] = "BR";
										checkerArray[indexX][indexY] = "-";

									} else {

										checkerArray[arrayY][arrayX] = "BK";
										checkerArray[indexX][indexY] = "-";

									}
								} else {

									checkerArray[arrayY][arrayX] = "BK";
									checkerArray[indexX][indexY] = "-";

								}

								for (int i = 0; i < 8; i++) {

									for (int j = 0; j < 8; j++) {

										moveArray[i][j] = "-";

									}
								}

								hasJump = false;
								blackTurn = false;

							}

						} else {

							for (int i = 0; i < 8; i++) {

								for (int j = 0; j < 8; j++) {

									moveArray[i][j] = "-";

								}
							}

						}
					} else {

						if (!checkerArray[arrayY][arrayX].equals("BR")
								&& !checkerArray[arrayY][arrayX].equals("BK")) {

							for (int i = 0; i < 8; i++) {

								for (int j = 0; j < 8; j++) {

									moveArray[i][j] = "-";

								}
							}
						} else {

							if (searchArray("SELECTED")) {

								indexSearch(moveArray, "SELECTED");
								moveArray[arrayY][arrayX] = "SELECTED";
								moveArray[indexX][indexY] = "-";
								findNextStep(arrayY, arrayX);

							} else {

								moveArray[arrayY][arrayX] = "SELECTED";
								findNextStep(arrayY, arrayX);

							}
						}
					}

					hasJump = false;

				} else {

					indexSearch(moveArray, "SELECTED");

					if (checkForJump(indexX, indexY)) {

						if (moveArray[arrayY][arrayX].equals("MOVE")) {

							if (checkerArray[indexX][indexY].equals("BR")) {

								if (arrayX < indexY) {

									if (arrayY != 0) {

										moveArray[arrayY][arrayX] = "SELECTED";
										checkerArray[arrayY + 1][arrayX + 1] = "-";
										checkerArray[indexX][indexY] = "-";
										checkerArray[arrayY][arrayX] = "BR";
										moveArray[indexX][indexY] = "-";

									} else {

										moveArray[arrayY][arrayX] = "SELECTED";
										checkerArray[arrayY + 1][arrayX + 1] = "-";
										checkerArray[indexX][indexY] = "-";
										checkerArray[arrayY][arrayX] = "BK";
										moveArray[indexX][indexY] = "-";

									}
								} else {

									if (arrayY != 0) {

										moveArray[arrayY][arrayX] = "SELECTED";
										checkerArray[arrayY + 1][arrayX - 1] = "-";
										checkerArray[indexX][indexY] = "-";
										checkerArray[arrayY][arrayX] = "BR";
										moveArray[indexX][indexY] = "-";

									} else {

										moveArray[arrayY][arrayX] = "SELECTED";
										checkerArray[arrayY + 1][arrayX - 1] = "-";
										checkerArray[indexX][indexY] = "-";
										checkerArray[arrayY][arrayX] = "BK";
										moveArray[indexX][indexY] = "-";

									}
								}
							} else if (checkerArray[indexX][indexY]
									.equals("BK")) {

								if (arrayX < indexY) {

									if (arrayY < indexX) {

										moveArray[arrayY][arrayX] = "SELECTED";
										checkerArray[arrayY + 1][arrayX + 1] = "-";
										checkerArray[indexX][indexY] = "-";
										checkerArray[arrayY][arrayX] = "BK";
										moveArray[indexX][indexY] = "-";

									} else {

										moveArray[arrayY][arrayX] = "SELECTED";
										checkerArray[arrayY - 1][arrayX + 1] = "-";
										checkerArray[indexX][indexY] = "-";
										checkerArray[arrayY][arrayX] = "BK";
										moveArray[indexX][indexY] = "-";

									}

								} else {

									if (arrayY < indexX) {

										moveArray[arrayY][arrayX] = "SELECTED";
										checkerArray[arrayY + 1][arrayX - 1] = "-";
										checkerArray[indexX][indexY] = "-";
										checkerArray[arrayY][arrayX] = "BK";
										moveArray[indexX][indexY] = "-";

									} else {

										moveArray[arrayY][arrayX] = "SELECTED";
										checkerArray[arrayY - 1][arrayX - 1] = "-";
										checkerArray[indexX][indexY] = "-";
										checkerArray[arrayY][arrayX] = "BK";
										moveArray[indexX][indexY] = "-";

									}
								}
							}
						}
					} else {

						for (int i = 0; i < 8; i++) {

							for (int j = 0; j < 8; j++) {

								moveArray[i][j] = "-";

							}
						}

						jumped = false;
						hasJump = false;
						blackTurn = false;
					}

					jumped = false;
					hasJump = false;
					checkJumpWithoutPaint(arrayY, arrayX);

				}
			} else {

				if (isGameOver()) {

					System.out.println("BLACK WINS");
					return;

				}
				checkBoardForJump();

				if (!jumped) {

					if (searchArray("MOVE") && searchArray("SELECTED")) {

						if (moveArray[arrayY][arrayX].equals("MOVE")) {

							indexSearch(moveArray, "SELECTED");

							if (checkJumpWithoutPaint(indexX, indexY)) {

								if (checkerArray[indexX][indexY].equals("RR")) {

									if (arrayX < indexY) {

										if (arrayY != 7) {

											moveArray[arrayY][arrayX] = "SELECTED";
											checkerArray[arrayY - 1][arrayX + 1] = "-";
											checkerArray[indexX][indexY] = "-";
											checkerArray[arrayY][arrayX] = "RR";
											moveArray[indexX][indexY] = "-";

										} else {

											moveArray[arrayY][arrayX] = "SELECTED";
											checkerArray[arrayY - 1][arrayX + 1] = "-";
											checkerArray[indexX][indexY] = "-";
											checkerArray[arrayY][arrayX] = "RK";
											moveArray[indexX][indexY] = "-";

										}
									} else {

										if (arrayY != 7) {

											moveArray[arrayY][arrayX] = "SELECTED";
											checkerArray[arrayY - 1][arrayX - 1] = "-";
											checkerArray[indexX][indexY] = "-";
											checkerArray[arrayY][arrayX] = "RR";
											moveArray[indexX][indexY] = "-";

										} else {

											moveArray[arrayY][arrayX] = "SELECTED";
											checkerArray[arrayY - 1][arrayX - 1] = "-";
											checkerArray[indexX][indexY] = "-";
											checkerArray[arrayY][arrayX] = "RK";
											moveArray[indexX][indexY] = "-";

										}
									}
								} else {

									if (arrayX < indexY) {

										if (arrayY < indexX) {

											moveArray[arrayY][arrayX] = "SELECTED";
											checkerArray[arrayY + 1][arrayX + 1] = "-";
											checkerArray[indexX][indexY] = "-";
											checkerArray[arrayY][arrayX] = "RK";
											moveArray[indexX][indexY] = "-";

										} else {

											moveArray[arrayY][arrayX] = "SELECTED";
											checkerArray[arrayY - 1][arrayX + 1] = "-";
											checkerArray[indexX][indexY] = "-";
											checkerArray[arrayY][arrayX] = "RK";
											moveArray[indexX][indexY] = "-";

										}

									} else {

										if (arrayY < indexX) {

											moveArray[arrayY][arrayX] = "SELECTED";
											checkerArray[arrayY + 1][arrayX - 1] = "-";
											checkerArray[indexX][indexY] = "-";
											checkerArray[arrayY][arrayX] = "RK";
											moveArray[indexX][indexY] = "-";

										} else {

											moveArray[arrayY][arrayX] = "SELECTED";
											checkerArray[arrayY - 1][arrayX - 1] = "-";
											checkerArray[indexX][indexY] = "-";
											checkerArray[arrayY][arrayX] = "RK";
											moveArray[indexX][indexY] = "-";

										}
									}
								}

								jumped = true;
								hasJump = false;
								checkJumpWithoutPaint(arrayY, arrayX);

								if (!checkJumpWithoutPaint(arrayY, arrayX)) {

									for (int i = 0; i < 8; i++) {

										for (int j = 0; j < 8; j++) {

											moveArray[i][j] = "-";

										}
									}
								}
							} else if (!checkJumpWithoutPaint(indexX, indexY)
									&& !boardHasJump) {

								if (checkerArray[indexX][indexY].equals("RR")) {

									if (arrayY != 7) {

										checkerArray[arrayY][arrayX] = "RR";
										checkerArray[indexX][indexY] = "-";

									} else {

										checkerArray[arrayY][arrayX] = "RK";
										checkerArray[indexX][indexY] = "-";

									}
								} else {

									checkerArray[arrayY][arrayX] = "RK";
									checkerArray[indexX][indexY] = "-";

								}

								for (int i = 0; i < 8; i++) {

									for (int j = 0; j < 8; j++) {

										moveArray[i][j] = "-";

									}
								}

								hasJump = false;
								blackTurn = true;

							}
						} else {

							for (int i = 0; i < 8; i++) {

								for (int j = 0; j < 8; j++) {

									moveArray[i][j] = "-";

								}
							}
						}
					} else {

						if (!checkerArray[arrayY][arrayX].equals("RR")
								&& !checkerArray[arrayY][arrayX].equals("RK")) {

							for (int i = 0; i < 8; i++) {

								for (int j = 0; j < 8; j++) {

									moveArray[i][j] = "-";

								}
							}
						} else {

							if (searchArray("SELECTED")) {

								indexSearch(moveArray, "SELECTED");
								moveArray[arrayY][arrayX] = "SELECTED";
								moveArray[indexX][indexY] = "-";
								findNextStep(arrayY, arrayX);

							} else {

								moveArray[arrayY][arrayX] = "SELECTED";
								findNextStep(arrayY, arrayX);

							}
						}
					}

					hasJump = false;

				} else {

					indexSearch(moveArray, "SELECTED");

					if (checkForJump(indexX, indexY)) {

						if (moveArray[arrayY][arrayX].equals("MOVE")) {

							if (checkerArray[indexX][indexY].equals("RR")) {

								if (arrayX < indexY) {

									if (arrayY != 7) {

										moveArray[arrayY][arrayX] = "SELECTED";
										checkerArray[arrayY - 1][arrayX + 1] = "-";
										checkerArray[indexX][indexY] = "-";
										checkerArray[arrayY][arrayX] = "RR";

									} else {

										moveArray[arrayY][arrayX] = "SELECTED";
										checkerArray[arrayY - 1][arrayX + 1] = "-";
										checkerArray[indexX][indexY] = "-";
										checkerArray[arrayY][arrayX] = "RK";

									}
								} else {

									if (arrayY != 7) {

										moveArray[arrayY][arrayX] = "SELECTED";
										checkerArray[arrayY - 1][arrayX - 1] = "-";
										checkerArray[indexX][indexY] = "-";
										checkerArray[arrayY][arrayX] = "RR";

									} else {

										moveArray[arrayY][arrayX] = "SELECTED";
										checkerArray[arrayY - 1][arrayX - 1] = "-";
										checkerArray[indexX][indexY] = "-";
										checkerArray[arrayY][arrayX] = "RK";

									}
								}
							} else if (checkerArray[indexX][indexY]
									.equals("RK")) {

								if (arrayX < indexY) {

									if (arrayY < indexX) {

										moveArray[arrayY][arrayX] = "SELECTED";
										checkerArray[arrayY + 1][arrayX + 1] = "-";
										checkerArray[indexX][indexY] = "-";
										checkerArray[arrayY][arrayX] = "RK";

									} else {

										moveArray[arrayY][arrayX] = "SELECTED";
										checkerArray[arrayY - 1][arrayX + 1] = "-";
										checkerArray[indexX][indexY] = "-";
										checkerArray[arrayY][arrayX] = "RK";

									}

								} else {

									if (arrayY < indexX) {

										moveArray[arrayY][arrayX] = "SELECTED";
										checkerArray[arrayY + 1][arrayX - 1] = "-";
										checkerArray[indexX][indexY] = "-";
										checkerArray[arrayY][arrayX] = "RK";

									} else {

										moveArray[arrayY][arrayX] = "SELECTED";
										checkerArray[arrayY - 1][arrayX - 1] = "-";
										checkerArray[indexX][indexY] = "-";
										checkerArray[arrayY][arrayX] = "RK";

									}
								}
							}
						}

					} else {

						for (int i = 0; i < 8; i++) {

							for (int j = 0; j < 8; j++) {

								moveArray[i][j] = "-";

							}
						}

						jumped = false;
						hasJump = false;
						blackTurn = true;
						
					}

					jumped = false;
					hasJump = false;
					checkJumpWithoutPaint(arrayY, arrayX);

				}
			}

			repaint();

		}
	}

	public boolean searchArray(String x) {

		for (int i = 0; i < 8; i++) {

			for (int j = 0; j < 8; j++) {

				if (moveArray[i][j].equals(x)) {

					return true;

				}
			}
		}

		return false;

	}

	public void indexSearch(String[][] sampleArray, String x) {

		for (int i = 0; i < 8; i++) {

			for (int j = 0; j < 8; j++) {

				if (sampleArray[i][j].equals(x)) {

					indexX = i;
					indexY = j;

					return;

				}
			}
		}
	}

	public void searchBoard(int x, int y) {

		if (blackTurn) {

			if (checkerArray[x][y].equals("BR")) {

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x - 1][y + 1].equals("RR") || checkerArray[x - 1][y + 1]
								.equals("RK"))) {

					if (checkerArray[x - 2][y + 2].equals("-")) {

						hasJump = true;
						falseJump = false;

					}
				}

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x - 1][y - 1].equals("RR") || checkerArray[x - 1][y - 1]
								.equals("RK"))) {

					if (checkerArray[x - 2][y - 2].equals("-")) {

						hasJump = true;
						falseJump = false;

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y + 1 >= 0 && y + 1 < 8) {

					if (checkerArray[x - 1][y + 1].equals("-") && !hasJump) {

						falseJump = true;

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y - 1 >= 0 && y - 1 < 8) {
					if (checkerArray[x - 1][y - 1].equals("-") && !hasJump) {

						falseJump = true;

					}
				}
			} else if (checkerArray[x][y].equals("BK")) {

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x - 1][y + 1].equals("RR") || checkerArray[x - 1][y + 1]
								.equals("RK"))) {

					if (checkerArray[x - 2][y + 2].equals("-")) {

						hasJump = true;
						falseJump = false;

					}
				}

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x - 1][y - 1].equals("RR") || checkerArray[x - 1][y - 1]
								.equals("RK"))) {

					if (checkerArray[x - 2][y - 2].equals("-")) {

						hasJump = true;
						falseJump = false;

					}
				}

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x + 1][y + 1].equals("RR") || checkerArray[x + 1][y + 1]
								.equals("RK"))) {

					if (checkerArray[x + 2][y + 2].equals("-")) {

						hasJump = true;
						falseJump = false;

					}
				}

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x + 1][y - 1].equals("RR") || checkerArray[x + 1][y - 1]
								.equals("RK"))) {

					if (checkerArray[x + 2][y - 2].equals("-")) {

						hasJump = true;
						falseJump = false;

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y + 1 >= 0 && y + 1 < 8) {
					if (checkerArray[x - 1][y + 1].equals("-") && !hasJump) {

						falseJump = true;

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y - 1 >= 0 && y - 1 < 8) {
					if (checkerArray[x - 1][y - 1].equals("-") && !hasJump) {

						falseJump = true;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y + 1 >= 0 && y + 1 < 8) {
					if (checkerArray[x + 1][y + 1].equals("-") && !hasJump) {

						falseJump = true;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y - 1 >= 0 && y - 1 < 8) {
					if (checkerArray[x + 1][y - 1].equals("-") && !hasJump) {

						falseJump = true;

					}
				}
			}
		} else {

			if (checkerArray[x][y].equals("RR")) {

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x + 1][y + 1].equals("BR") || checkerArray[x + 1][y + 1]
								.equals("BK"))) {

					if (checkerArray[x + 2][y + 2].equals("-")) {

						hasJump = true;
						falseJump = false;

					}
				}

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x + 1][y - 1].equals("BR") || checkerArray[x + 1][y - 1]
								.equals("BK"))) {

					if (checkerArray[x + 2][y - 2].equals("-")) {

						hasJump = true;
						falseJump = false;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y + 1 >= 0 && y + 1 < 8) {
					if (checkerArray[x + 1][y + 1].equals("-") && !hasJump) {

						falseJump = true;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y - 1 >= 0 && y - 1 < 8) {
					if (checkerArray[x + 1][y - 1].equals("-") && !hasJump) {

						falseJump = true;

					}
				}
			} else if (checkerArray[x][y].equals("RK")) {

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x - 1][y + 1].equals("BR") || checkerArray[x - 1][y + 1]
								.equals("BK"))) {

					if (checkerArray[x - 2][y + 2].equals("-")) {

						hasJump = true;
						falseJump = false;

					}
				}

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x - 1][y - 1].equals("BR") || checkerArray[x - 1][y - 1]
								.equals("BK"))) {

					if (checkerArray[x - 2][y - 2].equals("-")) {

						hasJump = true;
						falseJump = false;

					}
				}

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x + 1][y + 1].equals("BR") || checkerArray[x + 1][y + 1]
								.equals("BK"))) {

					if (checkerArray[x + 2][y + 2].equals("-")) {

						hasJump = true;
						falseJump = false;

					}
				}

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x + 1][y - 1].equals("BR") || checkerArray[x + 1][y - 1]
								.equals("BK"))) {

					if (checkerArray[x + 2][y - 2].equals("-")) {

						hasJump = true;
						falseJump = false;

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y + 1 >= 0 && y + 1 < 8) {
					if (checkerArray[x - 1][y + 1].equals("-") && !hasJump) {

						falseJump = true;

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y - 1 >= 0 && y - 1 < 8) {
					if (checkerArray[x - 1][y - 1].equals("-") && !hasJump) {

						falseJump = true;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y + 1 >= 0 && y + 1 < 8) {
					if (checkerArray[x + 1][y + 1].equals("-") && !hasJump) {

						falseJump = true;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y - 1 >= 0 && y - 1 < 8) {
					if (checkerArray[x + 1][y - 1].equals("-") && !hasJump) {

						falseJump = true;

					}
				}
			}
		}

		if (falseJump) {

			hasJump = false;

		}
	}

	// arguments are the matrix coordinates of the selected square
	public void findNextStep(int x, int y) {

		if (blackTurn) {

			if (checkerArray[x][y].equals("BR")) {

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x - 1][y + 1].equals("RR") || checkerArray[x - 1][y + 1]
								.equals("RK"))) {

					if (checkerArray[x - 2][y + 2].equals("-")) {

						hasJump = true;
						falseJump = false;
						moveArray[x - 2][y + 2] = "MOVE";

					}
				}

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x - 1][y - 1].equals("RR") || checkerArray[x - 1][y - 1]
								.equals("RK"))) {

					if (checkerArray[x - 2][y - 2].equals("-")) {

						hasJump = true;
						falseJump = false;
						moveArray[x - 2][y - 2] = "MOVE";

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y + 1 >= 0 && y + 1 < 8) {

					if (checkerArray[x - 1][y + 1].equals("-") && !hasJump) {

						moveArray[x - 1][y + 1] = "MOVE";
						falseJump = true;

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y - 1 >= 0 && y - 1 < 8) {

					if (checkerArray[x - 1][y - 1].equals("-") && !hasJump) {

						moveArray[x - 1][y - 1] = "MOVE";
						falseJump = true;

					}
				}
			} else if (checkerArray[x][y].equals("BK")) {

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x - 1][y + 1].equals("RR") || checkerArray[x - 1][y + 1]
								.equals("RK"))) {

					if (checkerArray[x - 2][y + 2].equals("-")) {

						hasJump = true;
						falseJump = false;
						moveArray[x - 2][y + 2] = "MOVE";

					}
				}

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x - 1][y - 1].equals("RR") || checkerArray[x - 1][y - 1]
								.equals("RK"))) {

					if (checkerArray[x - 2][y - 2].equals("-")) {

						hasJump = true;
						falseJump = false;
						moveArray[x - 2][y - 2] = "MOVE";

					}
				}

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x + 1][y + 1].equals("RR") || checkerArray[x + 1][y + 1]
								.equals("RK"))) {

					if (checkerArray[x + 2][y + 2].equals("-")) {

						hasJump = true;
						falseJump = false;
						moveArray[x + 2][y + 2] = "MOVE";

					}
				}

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x + 1][y - 1].equals("RR") || checkerArray[x + 1][y - 1]
								.equals("RK"))) {

					if (checkerArray[x + 2][y - 2].equals("-")) {

						hasJump = true;
						falseJump = false;
						moveArray[x + 2][y - 2] = "MOVE";

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y + 1 >= 0 && y + 1 < 8) {

					if (checkerArray[x - 1][y + 1].equals("-") && !hasJump) {

						moveArray[x - 1][y + 1] = "MOVE";
						falseJump = true;

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y - 1 >= 0 && y - 1 < 8) {

					if (checkerArray[x - 1][y - 1].equals("-") && !hasJump) {

						moveArray[x - 1][y - 1] = "MOVE";
						falseJump = true;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y + 1 >= 0 && y + 1 < 8) {

					if (checkerArray[x + 1][y + 1].equals("-") && !hasJump) {

						moveArray[x + 1][y + 1] = "MOVE";
						falseJump = true;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y - 1 >= 0 && y - 1 < 8) {

					if (checkerArray[x + 1][y - 1].equals("-") && !hasJump) {

						moveArray[x + 1][y - 1] = "MOVE";
						falseJump = true;

					}
				}
			}
		} else {

			if (checkerArray[x][y].equals("RR")) {

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x + 1][y + 1].equals("BR") || checkerArray[x + 1][y + 1]
								.equals("BK"))) {

					if (checkerArray[x + 2][y + 2].equals("-")) {

						hasJump = true;
						falseJump = false;
						moveArray[x + 2][y + 2] = "MOVE";

					}
				}

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x + 1][y - 1].equals("BR") || checkerArray[x + 1][y - 1]
								.equals("BK"))) {

					if (checkerArray[x + 2][y - 2].equals("-")) {

						hasJump = true;
						falseJump = false;
						moveArray[x + 2][y - 2] = "MOVE";

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y + 1 >= 0 && y + 1 < 8) {

					if (checkerArray[x + 1][y + 1].equals("-") && !hasJump) {

						moveArray[x + 1][y + 1] = "MOVE";
						falseJump = true;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y - 1 >= 0 && y - 1 < 8) {

					if (checkerArray[x + 1][y - 1].equals("-") && !hasJump) {

						moveArray[x + 1][y - 1] = "MOVE";
						falseJump = true;

					}
				}
			} else if (checkerArray[x][y].equals("RK")) {

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x - 1][y + 1].equals("BR") || checkerArray[x - 1][y + 1]
								.equals("BK"))) {

					if (checkerArray[x - 2][y + 2].equals("-")) {

						hasJump = true;
						falseJump = false;
						moveArray[x - 2][y + 2] = "MOVE";

					}
				}

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x - 1][y - 1].equals("BR") || checkerArray[x - 1][y - 1]
								.equals("BK"))) {

					if (checkerArray[x - 2][y - 2].equals("-")) {

						hasJump = true;
						falseJump = false;
						moveArray[x - 2][y - 2] = "MOVE";

					}
				}

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x + 1][y + 1].equals("BR") || checkerArray[x + 1][y + 1]
								.equals("BK"))) {

					if (checkerArray[x + 2][y + 2].equals("-")) {

						hasJump = true;
						falseJump = false;
						moveArray[x + 2][y + 2] = "MOVE";

					}
				}

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x + 1][y - 1].equals("BR") || checkerArray[x + 1][y - 1]
								.equals("BK"))) {

					if (checkerArray[x + 2][y - 2].equals("-")) {

						hasJump = true;
						falseJump = false;
						moveArray[x + 2][y - 2] = "MOVE";

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y + 1 >= 0 && y + 1 < 8) {

					if (checkerArray[x - 1][y + 1].equals("-") && !hasJump) {

						moveArray[x - 1][y + 1] = "MOVE";
						falseJump = true;

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y - 1 >= 0 && y - 1 < 8) {

					if (checkerArray[x - 1][y - 1].equals("-") && !hasJump) {

						moveArray[x - 1][y - 1] = "MOVE";
						falseJump = true;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y + 1 >= 0 && y + 1 < 8) {

					if (checkerArray[x + 1][y + 1].equals("-") && !hasJump) {

						moveArray[x + 1][y + 1] = "MOVE";
						falseJump = true;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y - 1 >= 0 && y - 1 < 8) {

					if (checkerArray[x + 1][y - 1].equals("-") && !hasJump) {

						moveArray[x + 1][y - 1] = "MOVE";
						falseJump = true;

					}
				}
			}
		}

		if (falseJump || !searchArray("MOVE")) {

			hasJump = false;

		}
	}

	public boolean checkForJump(int x, int y) {

		findNextStep(x, y);

		if (hasJump) {

			return true;

		} else {

			return false;

		}
	}

	public boolean checkJumpWithoutPaint(int x, int y) {

		searchBoard(x, y);

		if (hasJump) {

			return true;

		} else {

			return false;

		}
	}

	public void checkBoardForJump() {

		boardHasJump = false;

		if (blackTurn) {

			for (int i = 0; i < 8; i++) {

				for (int j = 0; j < 8; j++) {

					if (checkerArray[i][j].equals("BR")
							|| checkerArray[i][j].equals("BK")) {

						if (checkJumpWithoutPaint(i, j)) {

							boardHasJump = true;

							return;

						}
					}
				}
			}
		} else {

			for (int i = 0; i < 8; i++) {

				for (int j = 0; j < 8; j++) {

					if (checkerArray[i][j].equals("RR")
							|| checkerArray[i][j].equals("BK")) {

						if (checkJumpWithoutPaint(i, j)) {

							boardHasJump = true;

							return;

						}
					}
				}
			}
		}
	}

	public boolean searchForMoves(int x, int y) {

		if (blackTurn) {

			if (checkerArray[x][y].equals("BR")) {

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x - 1][y + 1].equals("RR") || checkerArray[x - 1][y + 1]
								.equals("RK"))) {

					if (checkerArray[x - 2][y + 2].equals("-")) {

						return true;

					}
				}

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x - 1][y - 1].equals("RR") || checkerArray[x - 1][y - 1]
								.equals("RK"))) {

					if (checkerArray[x - 2][y - 2].equals("-")) {

						return true;

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y + 1 >= 0 && y + 1 < 8) {

					if (checkerArray[x - 1][y + 1].equals("-") && !hasJump) {

						return true;

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y - 1 >= 0 && y - 1 < 8) {

					if (checkerArray[x - 1][y - 1].equals("-") && !hasJump) {

						return true;

					}
				}
			} else if (checkerArray[x][y].equals("BK")) {

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x - 1][y + 1].equals("RR") || checkerArray[x - 1][y + 1]
								.equals("RK"))) {

					if (checkerArray[x - 2][y + 2].equals("-")) {

						return true;

					}
				}

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x - 1][y - 1].equals("RR") || checkerArray[x - 1][y - 1]
								.equals("RK"))) {

					if (checkerArray[x - 2][y - 2].equals("-")) {

						return true;

					}
				}

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x + 1][y + 1].equals("RR") || checkerArray[x + 1][y + 1]
								.equals("RK"))) {

					if (checkerArray[x + 2][y + 2].equals("-")) {

						return true;

					}
				}

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x + 1][y - 1].equals("RR") || checkerArray[x + 1][y - 1]
								.equals("RK"))) {

					if (checkerArray[x + 2][y - 2].equals("-")) {

						return true;

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y + 1 >= 0 && y + 1 < 8) {

					if (checkerArray[x - 1][y + 1].equals("-") && !hasJump) {

						return true;

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y - 1 >= 0 && y - 1 < 8) {

					if (checkerArray[x - 1][y - 1].equals("-") && !hasJump) {

						return true;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y + 1 >= 0 && y + 1 < 8) {

					if (checkerArray[x + 1][y + 1].equals("-") && !hasJump) {

						return true;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y - 1 >= 0 && y - 1 < 8) {

					if (checkerArray[x + 1][y - 1].equals("-") && !hasJump) {

						return true;

					}
				}
			}
		} else {

			if (checkerArray[x][y].equals("RR")) {

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x + 1][y + 1].equals("BR") || checkerArray[x + 1][y + 1]
								.equals("BK"))) {

					if (checkerArray[x + 2][y + 2].equals("-")) {

						return true;

					}
				}

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x + 1][y - 1].equals("BR") || checkerArray[x + 1][y - 1]
								.equals("BK"))) {

					if (checkerArray[x + 2][y - 2].equals("-")) {

						return true;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y + 1 >= 0 && y + 1 < 8) {

					if (checkerArray[x + 1][y + 1].equals("-") && !hasJump) {

						return true;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y - 1 >= 0 && y - 1 < 8) {

					if (checkerArray[x + 1][y - 1].equals("-") && !hasJump) {

						return true;

					}
				}
			} else if (checkerArray[x][y].equals("RK")) {

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x - 1][y + 1].equals("BR") || checkerArray[x - 1][y + 1]
								.equals("BK"))) {

					if (checkerArray[x - 2][y + 2].equals("-")) {

						return true;

					}
				}

				if (x - 2 >= 0
						&& x - 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x - 1][y - 1].equals("BR") || checkerArray[x - 1][y - 1]
								.equals("BK"))) {

					if (checkerArray[x - 2][y - 2].equals("-")) {

						return true;

					}
				}

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y + 2 >= 0
						&& y + 2 < 8
						&& (checkerArray[x + 1][y + 1].equals("BR") || checkerArray[x + 1][y + 1]
								.equals("BK"))) {

					if (checkerArray[x + 2][y + 2].equals("-")) {

						return true;

					}
				}

				if (x + 2 >= 0
						&& x + 2 < 8
						&& y - 2 >= 0
						&& y - 2 < 8
						&& (checkerArray[x + 1][y - 1].equals("BR") || checkerArray[x + 1][y - 1]
								.equals("BK"))) {

					if (checkerArray[x + 2][y - 2].equals("-")) {

						return true;

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y + 1 >= 0 && y + 1 < 8) {

					if (checkerArray[x - 1][y + 1].equals("-") && !hasJump) {

						return true;

					}
				}

				if (x - 1 >= 0 && x - 1 < 8 && y - 1 >= 0 && y - 1 < 8) {

					if (checkerArray[x - 1][y - 1].equals("-") && !hasJump) {

						return true;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y + 1 >= 0 && y + 1 < 8) {

					if (checkerArray[x + 1][y + 1].equals("-") && !hasJump) {

						return true;

					}
				}

				if (x + 1 >= 0 && x + 1 < 8 && y - 1 >= 0 && y - 1 < 8) {

					if (checkerArray[x + 1][y - 1].equals("-") && !hasJump) {

						return true;

					}
				}
			}
		}

		return false;

	}

	public boolean isGameOver() {

		numberOfPieces = 0;

		if (blackTurn) {

			for (int i = 0; i < 8; i++) {

				for (int j = 0; j < 8; j++) {

					if (checkerArray[i][j].equals("BR")
							|| checkerArray[i][j].equals("BK")) {

						numberOfPieces++;

						if (searchForMoves(i, j)) {

							return false;

						}
					}
				}
			}
		} else {

			for (int i = 0; i < 8; i++) {

				for (int j = 0; j < 8; j++) {

					if (checkerArray[i][j].equals("RR")
							|| checkerArray[i][j].equals("RK")) {

						numberOfPieces++;

						if (searchForMoves(i, j)) {

							return false;

						}
					}
				}
			}
		}

		if (numberOfPieces != 0) {

			return false;

		} else {

			return true;

		}
	}
}