// this class defines the Node object which is used to construct trees
public class Node {

	public Node parent;
	public Node[] children;
	int[] coord;
	public String[][] leafBoard;

	public Node(int[] pos, int childNum) {

		coord = pos;
		parent = null;
		children = new Node[childNum];

	}

	public Node(int[] pos, String[][] board) {

		coord = pos;
		parent = null;
		children = new Node[0];
		leafBoard = board;

	}
}
