package project4;

/**
 * Class representing a RestStop object, its label, the supplies it contains and
 * the obstacles it contains.
 * 
 * @author Sara Vargas
 *
 */

public class RestStop implements Comparable<RestStop> {

	private String label;
	private int food;
	private int raft;
	private int axe;
	private int fallenTree;
	private int river;

	/**
	 * Constructor of RestStop object
	 * 
	 * @param label
	 * @param food
	 * @param raft
	 * @param axe
	 * @param fallenTree
	 * @param river
	 */
	public RestStop(String label, int food, int raft, int axe, int fallenTree, int river) {
		super();
		this.label = label;
		this.food = food;
		this.raft = raft;
		this.axe = axe;
		this.fallenTree = fallenTree;
		this.river = river;
	}

	/**
	 * Getter for food in the RestStop
	 * 
	 * @return
	 */

	public int getFood() {
		return food;
	}

	/**
	 * Getter for raft
	 * 
	 * @return number of rafts in RestStop
	 */
	public int getRaft() {
		return raft;
	}

	/**
	 * Getter for axes in RestStop
	 * 
	 * @return number of axes
	 */
	public int getAxe() {
		return axe;
	}

	/**
	 * Getter for number of fallen trees in RestStop
	 * 
	 * @return number of fallen trees in RestStop
	 */
	public int getFallenTree() {
		return fallenTree;
	}

	/**
	 * Getter of number of rivers in RestStop
	 * 
	 * @return number of river obstacles in restStop
	 */
	public int getRiver() {
		return river;
	}

	/**
	 * Compare to method comparing the labels of the RestSops in alphanumeric
	 * comparison.
	 */
	@Override
	public int compareTo(RestStop o) {
		return (this.label.compareTo(o.label));

	}

	/**
	 * printing the RestStop's label
	 */

	public String toString() {
		return label;
	}

}
