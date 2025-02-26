package project4;

/**
 * Class represents the hiker and holds its supplies.
 * 
 * @author Sara Vargas
 *
 */
public class Hiker {

	private int food;
	private int raft;
	private int axe;
	private int level;

	/**
	 * Empty hiker constructor
	 */
	public Hiker() {
		food = 0;
		raft = 0;
		axe = 0;
		level = 0;
	}

	/**
	 * Hiker copy constructor
	 * 
	 * @param hiker
	 */
	public Hiker(Hiker hiker) {
		this.food = hiker.food;
		this.raft = hiker.raft;
		this.axe = hiker.axe;
		this.level = hiker.level;
	}

	/**
	 * Getter for food
	 * 
	 * @return number of food
	 */
	public int getFood() {
		return food;
	}

	/**
	 * Setter for food
	 * 
	 * @param food amount
	 */
	public void setFood(int food) {
		this.food = food;
	}

	/**
	 * Getter for raft number
	 * 
	 * @return number of raft
	 */
	public int getRaft() {
		return raft;
	}

	/**
	 * Setter for raft number
	 * 
	 * @param raft
	 */

	public void setRaft(int raft) {
		this.raft = raft;
	}

	/**
	 * Getter for axe number
	 * 
	 * @return
	 */
	public int getAxe() {
		return axe;
	}

	/**
	 * Setter for axe number
	 * 
	 * @param axe
	 */

	public void setAxe(int axe) {
		this.axe = axe;
	}

	/**
	 * Getter for level of the hiker in mountain
	 * 
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Setter of level in tree
	 * 
	 * @param level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

}
