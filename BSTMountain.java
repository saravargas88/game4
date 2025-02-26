package project4;

import java.util.ArrayList;

/**
 * Description of a mountain and its Nodes containing RestStops. It's extending
 * the BST class.
 * 
 * @author Sara Vargas
 *
 */

public class BSTMountain extends BST<RestStop> {

	private ArrayList<Node> path;
	private ArrayList<ArrayList<Node>> paths;
	private Hiker hiker;

	/**
	 * Constructor invoking the super class constructor and initializing BSTMountain
	 */
	public BSTMountain() {
		super();
		path = new ArrayList<Node>();
		paths = new ArrayList<ArrayList<Node>>();
	}

	/*
	 * Get method for other classes like the MountainClimb to access paths
	 * 
	 * @returns paths ArrayList of ArrayLists of nodes
	 */
	public ArrayList<ArrayList<Node>> getPaths() {
		return paths;
	}

	/*
	 * This method traverses through the mountain's nodes and paths backtracks when
	 * it cannot continue due to insufficient supplies or because it reacher a
	 * cliff. This method populates the ArrayList paths with valid paths.
	 */
	public void hike() {

		hiker = new Hiker();

		advance(root);

	}

	private void advance(Node curr) {

		if (curr == null) {

			return;
		}

		if (curr != null) {

			Hiker previousHikerState = new Hiker(hiker);
			hiker.setLevel(hiker.getLevel() + 1);
			addSupplies(curr);

			if (passObstacles(curr) == true) {

				path.add(curr);

				if (hiker.getLevel() == height()) {

					if (!paths.contains(path)) {
						paths.add(path);
						path = (ArrayList<BST<RestStop>.Node>) path.clone();

					}
				} else {

					if (hiker.getFood() > 0) {
						hiker.setFood(hiker.getFood() - 1);
						advance(curr.left);

						advance(curr.right);
					}

				}
			}

			path.remove(curr);
			hiker = previousHikerState;
			return;
		}

	}

	/**
	 * Method adding supplies to the hiker given a certain node.
	 * 
	 * @param current Node containing a RestStop
	 */
	public void addSupplies(Node current) {

		hiker.setFood(hiker.getFood() + current.data.getFood());
		hiker.setAxe(hiker.getAxe() + current.data.getAxe());
		hiker.setRaft(hiker.getRaft() + current.data.getRaft());

	}

	/**
	 * Method that checks if the hiker can surpass the obstacles in a particular
	 * node.
	 * 
	 * @param current
	 * @return true if hiker has sufficient supplies to surpass obstacles, false
	 *         otherwise.
	 */
	public boolean passObstacles(Node current) {

		hiker.setAxe(hiker.getAxe() - current.data.getFallenTree());
		hiker.setRaft(hiker.getRaft() - current.data.getRiver());

		if (hiker.getAxe() >= 0 && hiker.getRaft() >= 0)
			return true;
		else
			return false;
	}

}

/*
 * while the hiker has food it can continue it cannot continue if it falls off a
 * cliff in a leaf node that isnt at the height of the tree and if the hiker
 * doesnt have necessary supplies to continue then it will rewind, how do i make
 * this rewind?
 * 
 */
