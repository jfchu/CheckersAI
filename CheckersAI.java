import java.util.*;

// this class contains the code to run the AI
public class CheckersAI {

	public static Node[] findMyMoves(String[][] board, String color) {
		
		Node[] myMoves;
		ArrayList<int[]> jumpableList = listJumpPieces(board, color);

		if (jumpableList.size() > 0) {

			myMoves = new Node[jumpableList.size()];

			for (int i = 0; i < jumpableList.size(); i++) {

				myMoves[i] = getJumpTree(board, jumpableList.get(i));

			}
		} else {

			ArrayList<int[]> walkableList = listWalkPieces(board, color);

			if (walkableList.size() > 0) {

				myMoves = new Node[walkableList.size()];

				for (int i = 0; i < walkableList.size(); i++) {

					myMoves[i] = getWalkTree(board, walkableList.get(i));

				}
			} else {

				myMoves = new Node[0];

			}
		}

		return myMoves;

	}

	public static double getScore(String[][] board, String color) {

		double blackScore = 0;
		double redScore = 0;
		double score;

		for (int i = 0; i < 8; i++) {

			for (int j = 0; j < 8; j++) {

				if (board[i][j].equals("BR")) {

					blackScore += (1 + (7.0 - i) / 8.0);

				} else if (board[i][j].equals("BK")) {

					blackScore += 3;

				}
			}
		}

		for (int i = 0; i < 8; i++) {

			for (int j = 0; j < 8; j++) {

				if (board[i][j].equals("RR")) {

					redScore += (1 + i / 8.0);

				} else if (board[i][j].equals("RK")) {

					redScore += 3;

				}
			}
		}

		if (color.equals("B")) {

			score = blackScore - redScore;

		} else {

			score = redScore - blackScore;

		}

		return score;

	}

	// recursive method to return a jump tree for a checker
	public static Node getJumpTree(String[][] origBoard, int[] startPos) {

		ArrayList<int[]> nextStopList = new ArrayList<int[]>();
		listJumps(nextStopList, origBoard, startPos);
		Node pathTree;

		// used for leaf nodes
		if (nextStopList.size() == 0) {

			pathTree = new Node(startPos, origBoard);

		} else {

			pathTree = new Node(startPos, nextStopList.size());

			for (int i = 0; i < nextStopList.size(); i++) {

				int[] tempStop = nextStopList.get(i);
				// creates a copy of origBoard at new memory location to prevent
				// overwriting
				String[][] tempBoard = cloneBoard(origBoard);
				// creates a new matrix representing the board after taking the
				// ith
				// jump
				makeJump(tempBoard, startPos, tempStop);
				// recursively calls the method
				pathTree.children[i] = getJumpTree(tempBoard, tempStop);
				// sets pathTree as the parent of its ith child
				pathTree.children[i].parent = pathTree;

			}
		}

		return pathTree;

	}

	// edits board by jumping one piece
	public static void makeJump(String[][] origBoard, int[] startPos, int[] endPos) {

		origBoard[(startPos[0] + endPos[0]) / 2][(startPos[1] + endPos[1]) / 2] = "-";

		if (origBoard[startPos[0]][startPos[1]].equals("BR") && endPos[0] == 0) {

			origBoard[endPos[0]][endPos[1]] = "BK";

		} else if (origBoard[startPos[0]][startPos[1]].equals("RR")
				&& endPos[0] == 7) {

			origBoard[endPos[0]][endPos[1]] = "RK";

		} else {

			origBoard[endPos[0]][endPos[1]] = origBoard[startPos[0]][startPos[1]];

		}

		origBoard[startPos[0]][startPos[1]] = "-";

	}

	// non-recursive method to return a walk tree for a checker
	public static Node getWalkTree(String[][] origBoard, int[] startPos) {

		ArrayList<int[]> nextStopList = new ArrayList<int[]>();
		listWalks(nextStopList, origBoard, startPos);
		Node pathTree;

		if (nextStopList.size() == 0) {

			pathTree = new Node(startPos, origBoard);

		} else {

			pathTree = new Node(startPos, nextStopList.size());

			for (int i = 0; i < nextStopList.size(); i++) {

				int[] tempStop = nextStopList.get(i);
				// creates a copy of origBoard at new memory location to prevent
				// overwriting
				String[][] tempBoard = cloneBoard(origBoard);
				// creates a new matrix representing the board after taking the
				// ith
				// jump
				makeWalk(tempBoard, startPos, tempStop);
				// we know that the children of the root have no children
				// because of the 1-step length of the walks; therefore, the
				// root's children are all leaves
				pathTree.children[i] = new Node(tempStop, tempBoard);
				// sets pathTree as the parent of its ith child
				pathTree.children[i].parent = pathTree;

			}
		}

		return pathTree;

	}

	// edits board by walking one piece
	public static void makeWalk(String[][] origBoard, int[] startPos, int[] endPos) {

		if (origBoard[startPos[0]][startPos[1]].equals("BR") && endPos[0] == 0) {

			origBoard[endPos[0]][endPos[1]] = "BK";

		} else if (origBoard[startPos[0]][startPos[1]].equals("RR")
				&& endPos[0] == 7) {

			origBoard[endPos[0]][endPos[1]] = "RK";

		} else {

			origBoard[endPos[0]][endPos[1]] = origBoard[startPos[0]][startPos[1]];

		}

		origBoard[startPos[0]][startPos[1]] = "-";

	}
	
	public static String[][] cloneBoard(String[][] origBoard) {
		
		String[][] newBoard = new String[origBoard.length][];
		
		for (int i = 0; i < newBoard.length; i++) {
			
			newBoard[i] = new String[origBoard[i].length];
			
			for(int j = 0; j < newBoard[i].length; j++){
				
				newBoard[i][j] = origBoard[i][j];
				
			}
		}
		
		return newBoard;
		
	}

	public static ArrayList<int[]> listJumpPieces(String[][] board, String color) {

		ArrayList<int[]> list = new ArrayList<int[]>();

		if (color.equals("B")) {

			for (int i = 0; i < 8; i++) {

				for (int j = 0; j < 8; j++) {

					if (board[i][j].equals("BR") || board[i][j].equals("BK")) {

						int[] startPos = new int[2];
						startPos[0] = i;
						startPos[1] = j;

						if (checkPieceForJump(board, startPos)) {

							list.add(startPos);

						}
					}
				}
			}
		} else if (color.equals("R")) {

			for (int i = 0; i < 8; i++) {

				for (int j = 0; j < 8; j++) {

					if (board[i][j].equals("RR") || board[i][j].equals("RK")) {

						int[] startPos = new int[2];
						startPos[0] = i;
						startPos[1] = j;

						if (checkPieceForJump(board, startPos)) {

							list.add(startPos);

						}
					}
				}
			}
		}

		return list;

	}

	public static ArrayList<int[]> listWalkPieces(String[][] board, String color) {

		ArrayList<int[]> list = new ArrayList<int[]>();

		if (color.equals("B")) {

			for (int i = 0; i < 8; i++) {

				for (int j = 0; j < 8; j++) {

					if (board[i][j].equals("BR") || board[i][j].equals("BK")) {

						int[] startPos = new int[2];
						startPos[0] = i;
						startPos[1] = j;

						if (checkPieceForWalk(board, startPos)) {

							list.add(startPos);

						}
					}
				}
			}
		} else if (color.equals("R")) {

			for (int i = 0; i < 8; i++) {

				for (int j = 0; j < 8; j++) {

					if (board[i][j].equals("RR") || board[i][j].equals("RK")) {

						int[] startPos = new int[2];
						startPos[0] = i;
						startPos[1] = j;

						if (checkPieceForWalk(board, startPos)) {

							list.add(startPos);

						}
					}
				}
			}
		}

		return list;

	}

	public static boolean checkBoardForJump(String[][] board, String color) {

		int[] startPos = new int[2];

		if (color.equals("B")) {

			for (int i = 0; i < 8; i++) {

				for (int j = 0; j < 8; j++) {

					if (board[i][j].equals("BR") || board[i][j].equals("BK")) {

						startPos[0] = i;
						startPos[1] = j;

						if (checkPieceForJump(board, startPos)) {

							return true;

						}
					}
				}
			}
		} else if (color.equals("R")) {

			for (int i = 0; i < 8; i++) {

				for (int j = 0; j < 8; j++) {

					if (board[i][j].equals("RR") || board[i][j].equals("RK")) {

						startPos[0] = i;
						startPos[1] = j;

						if (checkPieceForJump(board, startPos)) {

							return true;

						}
					}
				}
			}
		}

		return false;

	}

	public static boolean checkPieceForJump(String[][] board, int[] startPos) {

		if (board[startPos[0]][startPos[1]].equals("BR")
				|| board[startPos[0]][startPos[1]].equals("BK")) {

			if (startPos[0] - 2 >= 0 && startPos[0] - 2 < 8
					&& startPos[1] + 2 >= 0 && startPos[1] + 2 < 8) {

				if (board[startPos[0] - 2][startPos[1] + 2].equals("-")) {

					if (board[startPos[0] - 1][startPos[1] + 1].equals("RR")
							|| board[startPos[0] - 1][startPos[1] + 1]
									.equals("RK")) {

						return true;

					}
				}
			}

			if (startPos[0] - 2 >= 0 && startPos[0] - 2 < 8
					&& startPos[1] - 2 >= 0 && startPos[1] - 2 < 8) {

				if (board[startPos[0] - 2][startPos[1] - 2].equals("-")) {

					if (board[startPos[0] - 1][startPos[1] - 1].equals("RR")
							|| board[startPos[0] - 1][startPos[1] - 1]
									.equals("RK")) {

						return true;

					}
				}
			}

			if (board[startPos[0]][startPos[1]].equals("BK")) {

				if (startPos[0] + 2 >= 0 && startPos[0] + 2 < 8
						&& startPos[1] + 2 >= 0 && startPos[1] + 2 < 8) {

					if (board[startPos[0] + 2][startPos[1] + 2].equals("-")) {

						if (board[startPos[0] + 1][startPos[1] + 1]
								.equals("RR")
								|| board[startPos[0] + 1][startPos[1] + 1]
										.equals("RK")) {

							return true;

						}
					}
				}

				if (startPos[0] + 2 >= 0 && startPos[0] + 2 < 8
						&& startPos[1] - 2 >= 0 && startPos[1] - 2 < 8) {

					if (board[startPos[0] + 2][startPos[1] - 2].equals("-")) {

						if (board[startPos[0] + 1][startPos[1] - 1]
								.equals("RR")
								|| board[startPos[0] + 1][startPos[1] - 1]
										.equals("RK")) {

							return true;

						}
					}
				}
			}
		} else if (board[startPos[0]][startPos[1]].equals("RR")
				|| board[startPos[0]][startPos[1]].equals("RK")) {

			if (startPos[0] + 2 >= 0 && startPos[0] + 2 < 8
					&& startPos[1] + 2 >= 0 && startPos[1] + 2 < 8) {

				if (board[startPos[0] + 2][startPos[1] + 2].equals("-")) {

					if (board[startPos[0] + 1][startPos[1] + 1].equals("BR")
							|| board[startPos[0] + 1][startPos[1] + 1]
									.equals("BK")) {

						return true;

					}
				}
			}

			if (startPos[0] + 2 >= 0 && startPos[0] + 2 < 8
					&& startPos[1] - 2 >= 0 && startPos[1] - 2 < 8) {

				if (board[startPos[0] + 2][startPos[1] - 2].equals("-")) {

					if (board[startPos[0] + 1][startPos[1] - 1].equals("BR")
							|| board[startPos[0] + 1][startPos[1] - 1]
									.equals("BK")) {

						return true;

					}
				}
			}

			if (board[startPos[0]][startPos[1]].equals("RK")) {

				if (startPos[0] - 2 >= 0 && startPos[0] - 2 < 8
						&& startPos[1] + 2 >= 0 && startPos[1] + 2 < 8) {

					if (board[startPos[0] - 2][startPos[1] + 2].equals("-")) {

						if (board[startPos[0] - 1][startPos[1] + 1]
								.equals("BR")
								|| board[startPos[0] - 1][startPos[1] + 1]
										.equals("BK")) {

							return true;

						}
					}
				}

				if (startPos[0] - 2 >= 0 && startPos[0] - 2 < 8
						&& startPos[1] - 2 >= 0 && startPos[1] - 2 < 8) {

					if (board[startPos[0] - 2][startPos[1] - 2].equals("-")) {

						if (board[startPos[0] - 1][startPos[1] - 1]
								.equals("BR")
								|| board[startPos[0] - 1][startPos[1] - 1]
										.equals("BK")) {

							return true;

						}
					}
				}
			}
		}

		return false;

	}

	public static boolean checkPieceForWalk(String[][] board, int[] startPos) {

		if (board[startPos[0]][startPos[1]].equals("BR")
				|| board[startPos[0]][startPos[1]].equals("BK")) {

			if (startPos[0] - 1 >= 0 && startPos[0] - 1 < 8
					&& startPos[1] + 1 >= 0 && startPos[1] + 1 < 8) {

				if (board[startPos[0] - 1][startPos[1] + 1].equals("-")) {

					return true;

				}
			}

			if (startPos[0] - 1 >= 0 && startPos[0] - 1 < 8
					&& startPos[1] - 1 >= 0 && startPos[1] - 1 < 8) {

				if (board[startPos[0] - 1][startPos[1] - 1].equals("-")) {

					return true;

				}
			}

			if (board[startPos[0]][startPos[1]].equals("BK")) {

				if (startPos[0] + 1 >= 0 && startPos[0] + 1 < 8
						&& startPos[1] + 1 >= 0 && startPos[1] + 1 < 8) {

					if (board[startPos[0] + 1][startPos[1] + 1].equals("-")) {

						return true;

					}
				}

				if (startPos[0] + 1 >= 0 && startPos[0] + 1 < 8
						&& startPos[1] - 1 >= 0 && startPos[1] - 1 < 8) {

					if (board[startPos[0] + 1][startPos[1] - 1].equals("-")) {

						return true;

					}
				}
			}
		} else if (board[startPos[0]][startPos[1]].equals("RR")
				|| board[startPos[0]][startPos[1]].equals("RK")) {

			if (startPos[0] + 1 >= 0 && startPos[0] + 1 < 8
					&& startPos[1] + 1 >= 0 && startPos[1] + 1 < 8) {

				if (board[startPos[0] + 1][startPos[1] + 1].equals("-")) {

					return true;

				}
			}

			if (startPos[0] + 1 >= 0 && startPos[0] + 1 < 8
					&& startPos[1] - 1 >= 0 && startPos[1] - 1 < 8) {

				if (board[startPos[0] + 1][startPos[1] - 1].equals("-")) {

					return true;

				}
			}

			if (board[startPos[0]][startPos[1]].equals("RK")) {

				if (startPos[0] - 1 >= 0 && startPos[0] - 1 < 8
						&& startPos[1] + 1 >= 0 && startPos[1] + 1 < 8) {

					if (board[startPos[0] - 1][startPos[1] + 1].equals("-")) {

						return true;

					}
				}

				if (startPos[0] - 1 >= 0 && startPos[0] - 1 < 8
						&& startPos[1] - 1 >= 0 && startPos[1] - 1 < 8) {

					if (board[startPos[0] - 1][startPos[1] - 1].equals("-")) {

						return true;

					}
				}
			}
		}

		return false;

	}

	public static void listJumps(ArrayList<int[]> list, String[][] board,
			int[] startPos) {

		if (board[startPos[0]][startPos[1]].equals("BR")) {

			if (startPos[0] - 2 >= 0
					&& startPos[1] + 2 < 8
					&& (board[startPos[0] - 1][startPos[1] + 1].equals("RR") || board[startPos[0] - 1][startPos[1] + 1]
							.equals("RK"))
					&& board[startPos[0] - 2][startPos[1] + 2].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] - 2;
				coord[1] = startPos[1] + 2;
				list.add(coord);

			} else if (startPos[0] - 2 >= 0
					&& startPos[1] - 2 >= 0
					&& (board[startPos[0] - 1][startPos[1] - 1].equals("RR") || board[startPos[0] - 1][startPos[1] - 1]
							.equals("RK"))
					&& board[startPos[0] - 2][startPos[1] - 2].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] - 2;
				coord[1] = startPos[1] - 2;
				list.add(coord);

			}
		} else if (board[startPos[0]][startPos[1]].equals("BK")) {

			if (startPos[0] - 2 >= 0
					&& startPos[1] + 2 < 8
					&& (board[startPos[0] - 1][startPos[1] + 1].equals("RR") || board[startPos[0] - 1][startPos[1] + 1]
							.equals("RK"))
					&& board[startPos[0] - 2][startPos[1] + 2].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] - 2;
				coord[1] = startPos[1] + 2;
				list.add(coord);

			} else if (startPos[0] - 2 >= 0
					&& startPos[1] - 2 >= 0
					&& (board[startPos[0] - 1][startPos[1] - 1].equals("RR") || board[startPos[0] - 1][startPos[1] - 1]
							.equals("RK"))
					&& board[startPos[0] - 2][startPos[1] - 2].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] - 2;
				coord[1] = startPos[1] - 2;
				list.add(coord);

			} else if (startPos[0] + 2 < 8
					&& startPos[1] + 2 < 8
					&& (board[startPos[0] + 1][startPos[1] + 1].equals("RR") || board[startPos[0] + 1][startPos[1] + 1]
							.equals("RK"))
					&& board[startPos[0] + 2][startPos[1] + 2].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] + 2;
				coord[1] = startPos[1] + 2;
				list.add(coord);

			} else if (startPos[0] + 2 < 8
					&& startPos[1] - 2 >= 0
					&& (board[startPos[0] + 1][startPos[1] - 1].equals("RR") || board[startPos[0] + 1][startPos[1] - 1]
							.equals("RK"))
					&& board[startPos[0] + 2][startPos[1] - 2].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] + 2;
				coord[1] = startPos[1] - 2;
				list.add(coord);

			}
		}

		if (board[startPos[0]][startPos[1]].equals("RR")) {

			if (startPos[0] + 2 < 8
					&& startPos[1] + 2 < 8
					&& (board[startPos[0] + 1][startPos[1] + 1].equals("BR") || board[startPos[0] + 1][startPos[1] + 1]
							.equals("BK"))
					&& board[startPos[0] + 2][startPos[1] + 2].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] + 2;
				coord[1] = startPos[1] + 2;
				list.add(coord);

			} else if (startPos[0] + 2 < 8
					&& startPos[1] - 2 >= 0
					&& (board[startPos[0] + 1][startPos[1] - 1].equals("BR") || board[startPos[0] + 1][startPos[1] - 1]
							.equals("BK"))
					&& board[startPos[0] + 2][startPos[1] - 2].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] + 2;
				coord[1] = startPos[1] - 2;
				list.add(coord);

			}
		} else if (board[startPos[0]][startPos[1]].equals("RK")) {

			if (startPos[0] - 2 >= 0
					&& startPos[1] + 2 < 8
					&& (board[startPos[0] - 1][startPos[1] + 1].equals("BR") || board[startPos[0] - 1][startPos[1] + 1]
							.equals("BK"))
					&& board[startPos[0] - 2][startPos[1] + 2].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] - 2;
				coord[1] = startPos[1] + 2;
				list.add(coord);

			} else if (startPos[0] - 2 >= 0
					&& startPos[1] - 2 >= 0
					&& (board[startPos[0] - 1][startPos[1] - 1].equals("BR") || board[startPos[0] - 1][startPos[1] - 1]
							.equals("BK"))
					&& board[startPos[0] - 2][startPos[1] - 2].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] - 2;
				coord[1] = startPos[1] - 2;
				list.add(coord);

			} else if (startPos[0] + 2 < 8
					&& startPos[1] + 2 < 8
					&& (board[startPos[0] + 1][startPos[1] + 1].equals("BR") || board[startPos[0] + 1][startPos[1] + 1]
							.equals("BK"))
					&& board[startPos[0] + 2][startPos[1] + 2].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] + 2;
				coord[1] = startPos[1] + 2;
				list.add(coord);

			} else if (startPos[0] + 2 < 8
					&& startPos[1] - 2 >= 0
					&& (board[startPos[0] + 1][startPos[1] - 1].equals("BR") || board[startPos[0] + 1][startPos[1] - 1]
							.equals("BK"))
					&& board[startPos[0] + 2][startPos[1] - 2].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] + 2;
				coord[1] = startPos[1] - 2;
				list.add(coord);

			}
		}
	}

	public static void listWalks(ArrayList<int[]> list, String[][] board,
			int[] startPos) {

		if (board[startPos[0]][startPos[1]].equals("BR")) {

			if (startPos[0] - 1 >= 0 && startPos[1] + 1 < 8
					&& board[startPos[0] - 1][startPos[1] + 1].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] - 1;
				coord[1] = startPos[1] + 1;
				list.add(coord);

			} else if (startPos[0] - 1 >= 0 && startPos[1] - 1 >= 0
					&& board[startPos[0] - 1][startPos[1] - 1].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] - 1;
				coord[1] = startPos[1] - 1;
				list.add(coord);

			}
		} else if (board[startPos[0]][startPos[1]].equals("BK")) {

			if (startPos[0] - 1 >= 0 && startPos[1] + 1 < 8
					&& board[startPos[0] - 1][startPos[1] + 1].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] - 1;
				coord[1] = startPos[1] + 1;
				list.add(coord);

			} else if (startPos[0] - 1 >= 0 && startPos[1] - 1 >= 0
					&& board[startPos[0] - 1][startPos[1] - 1].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] - 1;
				coord[1] = startPos[1] - 1;
				list.add(coord);

			} else if (startPos[0] + 1 < 8 && startPos[1] + 1 < 8
					&& board[startPos[0] + 1][startPos[1] + 1].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] + 1;
				coord[1] = startPos[1] + 1;
				list.add(coord);

			} else if (startPos[0] + 1 < 8 && startPos[1] - 1 >= 0
					&& board[startPos[0] + 1][startPos[1] - 1].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] + 1;
				coord[1] = startPos[1] - 1;
				list.add(coord);

			}
		}

		if (board[startPos[0]][startPos[1]].equals("RR")) {

			if (startPos[0] + 1 < 8 && startPos[1] + 1 < 8
					&& board[startPos[0] + 1][startPos[1] + 1].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] + 1;
				coord[1] = startPos[1] + 1;
				list.add(coord);

			} else if (startPos[0] + 1 < 8 && startPos[1] - 1 >= 0
					&& board[startPos[0] + 1][startPos[1] - 1].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] + 1;
				coord[1] = startPos[1] - 1;
				list.add(coord);

			}
		} else if (board[startPos[0]][startPos[1]].equals("RK")) {

			if (startPos[0] - 1 >= 0 && startPos[1] + 1 < 8
					&& board[startPos[0] - 1][startPos[1] + 1].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] - 1;
				coord[1] = startPos[1] + 1;
				list.add(coord);

			} else if (startPos[0] - 1 >= 0 && startPos[1] - 1 >= 0
					&& board[startPos[0] - 1][startPos[1] - 1].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] - 1;
				coord[1] = startPos[1] - 1;
				list.add(coord);

			} else if (startPos[0] + 1 < 8 && startPos[1] + 1 < 8
					&& board[startPos[0] + 1][startPos[1] + 1].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] + 1;
				coord[1] = startPos[1] + 1;
				list.add(coord);

			} else if (startPos[0] + 1 < 8 && startPos[1] - 1 >= 0
					&& board[startPos[0] + 1][startPos[1] - 1].equals("-")) {

				int[] coord = new int[2];
				coord[0] = startPos[0] + 1;
				coord[1] = startPos[1] - 1;
				list.add(coord);

			}
		}
	}

	public static int checkerCounter(String[][] board, String color) {

		int checkerNumber = 0;

		if (color.equals("B")) {

			for (int i = 0; i < 8; i++) {

				for (int j = 0; j < 8; j++) {

					if (board[i][j].equals("BR") || board[i][j].equals("BK")) {

						checkerNumber++;

					}
				}
			}
		} else if (color.equals("R")) {

			for (int i = 0; i < 8; i++) {

				for (int j = 0; j < 8; j++) {

					if (board[i][j].equals("RR") || board[i][j].equals("RK")) {

						checkerNumber++;

					}
				}
			}
		}

		return checkerNumber;

	}

	public static void leafCounter(ArrayList<Node> list, Node n) {

		if (n.children.length == 0) {

			list.add(n);
			return;

		} else {

			for (int i = 0; i < n.children.length; i++) {

				leafCounter(list, n.children[i]);

			}
		}
	}

	public static ArrayList<int[]> listCheckers(String[][] board, String color) {

		ArrayList<int[]> list = new ArrayList<int[]>();

		if (color.equals("B")) {

			for (int i = 0; i < 8; i++) {

				for (int j = 0; j < 8; j++) {

					if (board[i][j].equals("BR") || board[i][j].equals("BK")) {

						int[] coord = { i, j };
						list.add(coord);

					}
				}
			}
		} else if (color.equals("R")) {

			for (int i = 0; i < 8; i++) {

				for (int j = 0; j < 8; j++) {

					if (board[i][j].equals("RR") || board[i][j].equals("RK")) {

						int[] coord = { i, j };
						list.add(coord);

					}
				}
			}
		}

		return list;

	}
}