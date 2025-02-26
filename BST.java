package project4;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;

/**
 * An implementation of a binary search tree. The elements are ordered using
 * their natural ordering. This implementation provides guaranteed O(H) time
 * cost for the basic operations (add, remove and contains).
 * 
 * @author Sara Vargas
 */
public class BST<E extends Comparable<E>> extends Object implements Iterable<E> {

	protected Node root;

	private int size;

	// Helper data fields for different methods
	private boolean found; // used in remove()
	private int counter; // helper for get()
	private Node gotten; // helper for get()
	private E floor;
	private E ceiling;
	private E lower;
	private E higher;

	/**
	 * Constructs a new, empty tree, sorted according to the natural ordering of its
	 * elements. All elements inserted into the tree must implement the Comparable
	 * interface. This operation is O(1).
	 */

	public BST() {
		root = null;
		size = 0;

	}

	/**
	 * Constructs a new tree containing the elements in the specified collection,
	 * sorted according to the natural ordering of its elements. All elements
	 * inserted into the tree must implement the Comparable interface. This
	 * operation is O(N logN) where N is the number of elements in the collection.
	 * This implies, that the tree that is constructed has to have the high that is
	 * approximately logN, not N.
	 * 
	 * @param collection - collection whose elements will comprise the new tree
	 * @throws NullPointerException - if the specified collection is null
	 */
	public BST(E[] collection) throws NullPointerException {

		if (collection == null) {
			throw new NullPointerException("The collection cant be null");
		}
		Arrays.sort(collection);

		addtoBST(collection, 0, collection.length - 1);

	}

	/**
	 * Recursive method called in BST(E[] collection) constructor adding nodes from
	 * the array collection ensuring the tree is balanced.
	 * 
	 * @param collection an array of generic and comparable data.
	 * @param first      index of array being examined to add.
	 * @param last       index of array being examined.
	 */

	public void addtoBST(E[] collection, int first, int last) {

		if (last >= first) {

			int mid = (first + last) / 2;
			this.add(collection[mid]);

			addtoBST(collection, first, mid - 1);

			addtoBST(collection, mid + 1, last);

		}

	}

	/**
	 * Adds the specified element to this set if it is not already present. More
	 * formally, adds the specified element e to this tree if the set contains no
	 * element e2 such that Objects.equals(e, e2). If this set already contains the
	 * element, the call leaves the set unchanged and returns false. This operation
	 * is O(H).
	 * 
	 * @param data element to be added to this tree
	 * @return true if this tree did not already contain the specified element
	 * @throws NullPointerException if the specified element is null and this set
	 *                              uses natural ordering, or its comparator does
	 *                              not permit null elements
	 *
	 */
	public boolean add(E data) throws NullPointerException {
		Stack<Node> stack = new Stack();
		if (data == null) {
			throw new NullPointerException("element to add cannot be null");
		}

		if (root == null) {// create the first node
			Node n = new Node(data);
			root = n;
			size++;
			n.height = 1;
			return true;
		}
		Node current = root;
		stack.push(root);
		while (current != null) {
			int comp = current.data.compareTo(data);
			if (comp > 0) { // add in the left subtree
				if (current.left == null) {
					Node leaf = new Node(data);
					current.left = leaf;
					leaf.height = 1;
					size++;
					updateHeight(stack);
					return true;

				} else {

					current = current.left;
					stack.push(current);
				}
			} else if (comp < 0) {// add in the right subtree

				if (current.right == null) {
					Node leaf = new Node(data);
					current.right = leaf;
					leaf.height = 1; // the added leaf node's height is
					size++;
					updateHeight(stack);
					return true;
				} else {
					current = current.right;
					stack.push(current);
				}
			}

			else { // duplicate
				return false;
			}
		}
		// we should never get to this line
		return false;
	}

	/**
	 * Method that updates the node's height data field. It's performance is O(1)
	 * Called in add().
	 * 
	 * @param Satck of nodes that have been explored to get to the position of
	 *              attaching the new leaf node
	 */

	public void updateHeight(Stack<Node> stack) {

		while (!stack.isEmpty()) {
			Node node = stack.pop();

			if (node.left == null && node.right == null) {
				node.height = 1;
			} else if (node.left == null)
				node.height = node.right.height + 1;
			else if (node.right == null)
				node.height = node.left.height + 1;
			else
				node.height = 1 + Math.max(node.left.height, node.right.height);
		}
	}

	/**
	 * Method updates the Node's particular height. Performance O(1) Called in
	 * remove method.
	 * 
	 * @param node
	 */
	public void updateHeight(Node node) {

		if (node.left == null && node.right == null) {
			node.height = 1;
		} else if (node.left == null)
			node.height = node.right.height + 1;
		else if (node.right == null)
			node.height = node.left.height + 1;
		else
			node.height = 1 + Math.max(node.left.height, node.right.height);
	}

	/**
	 * Remove method serves as a wrapper for the Recursive remove method.
	 * 
	 * @param o object to be removed from this set, if present
	 * @return true if this set contained the specified element
	 * @throws ClassCastException   if the specified object cannot be compared with
	 *                              the elements currently in this tree
	 * @throws NullPointerException - if the specified element is null
	 */
	public boolean remove(Object o) throws ClassCastException, NullPointerException {
		// replace root with a reference to the tree after target was removed

		root = recRemove(o, root);
		if (found) {
			size--;
		}
		return found;
	}

	/**
	 * Actual recursive implementation of remove method: find the node to remove.
	 * This function recursively finds and eventually removes the node with the
	 * target element and returns the reference to the modified tree to the caller.
	 * 
	 * @param o    object to be removed from this tree, if present
	 * @param node node at which the recursive call is made
	 */
	private Node recRemove(Object o, Node node) {
		if (node == null) { // value not found
			found = false;
			return node;
		}
		int comp = ((Comparable<E>) o).compareTo(node.data);
		if (comp < 0) {// target might be in a left subtree
			node.left = recRemove(o, node.left);
			if (found) {
				updateHeight(node);
			}
		} else if (comp > 0) { // target might be in a right subtree
			node.right = recRemove(o, node.right);
			if (found) {
				updateHeight(node);
			}
		} else { // target found, now remove it
			node = removeNode(node);
			found = true;
		}
		return node;
	}

	/**
	 * Actual recursive implementation of remove method: perform the removal.
	 * 
	 * @param node the item to be removed from this tree
	 * @return a reference to the node itself, or to the modified subtree
	 */
	private Node removeNode(Node node) {
		E data;
		if (node.left == null) // handle the leaf and one child node with right subtree
			return node.right;
		else if (node.right == null) // handle one child node with left subtree
			return node.left;
		else { // handle nodes with two children
			data = getPredecessor(node.left);
			node.data = data;
			node.left = recRemove(data, node.left);
			return node;
		}
	}

	/**
	 * Method given the root of left subtree of the node being removed to find its
	 * predecessor and replacement.
	 * 
	 * @param subtree
	 * @return predecessor element element
	 */
	private E getPredecessor(Node subtree) {
		if (subtree == null)
			throw new NullPointerException("getPredecessor called with an empty subtree");
		Node temp = subtree;
		while (temp.right != null)
			temp = temp.right;
		return temp.data;
	}

	/**
	 * Removes all of the elements from this set. The set will be empty after this
	 * call returns. This operation is O(1).
	 */
	public void clear() {
		root = null;
		size = 0;
		

	}

	/**
	 * Returns true if this set contains the specified element. More formally,
	 * returns true if and only if this set contains an element e such that
	 * Objects.equals(o, e). This operation is O(H)
	 * 
	 * @param o - object to be checked for containment in this set
	 * @return true if this set contains the specified element
	 * @throws ClassCastException   - if the specified object cannot be compared
	 *                              with the elements currently in the set
	 * @throws NullPointerException - if the specified element is null and this set
	 *                              uses natural ordering, or its comparator does
	 *                              not permit null elements
	 */
	public boolean contains​(Object o) throws ClassCastException, NullPointerException {

		if (o == null)
			throw new NullPointerException("object cannot be null");

		return recContains(o, root);

	}

	private boolean recContains(Object o, Node node) {
		if (node == null) {
			return false;
		}
		int comp = ((Comparable<E>) o).compareTo(node.data);

		if (comp == 0) {
			return true;
		}

		if (comp < 0) {// target might be in a left subtree
			return recContains(o, node.left);
		} else if (comp > 0) { // target might be in a right subtree
			return recContains(o, node.right);
		}
		// target found
		return false;

	}

	/**
	 * Returns the number of elements in this tree. This operation is O(1).
	 * 
	 * @return the number of elements in this tree
	 */
	public int size() {
		return size;

	}

	/**
	 * Returns true if this set contains no elements. This operation is O(1).
	 * 
	 * @return true if this set contains no elements
	 */
	public boolean isEmpty() {
		if (root == null) {
			return true;
		}
		return false;

	}

	/**
	 * Returns the height of this tree. The height of a leaf is 1. The height of the
	 * tree is the height of its root node. This operation is O(1).
	 * 
	 * @return height of the tree
	 */
	public int height() { // the height of the root node
		return root.height;
	}

	/**
	 * Returns the element at the specified position in this tree. The order of the
	 * indexed elements is the same as provided by this tree's iterator. The
	 * indexing is zero based (i.e., the smallest element in this tree is at index 0
	 * and the largest one is at index size()-1). This operation is O(H).
	 * 
	 * @param index of node to output in the tree
	 * @return the element at the specified position in this tree
	 * @throws IndexOutOfBoundsException - if the index is out of range (index < 0
	 *                                   || index >= size())
	 */

	public E get​(int index) throws IndexOutOfBoundsException {

		if (index > size() - 1 || index < 0) {
			throw new IndexOutOfBoundsException("out of bounds!");
		}

		gotten = null;
		counter = 0;
		getRec(index, root);
		return gotten.data;

	}

	/**
	 * Recursive method traverses tree to find the Node at the specified index.
	 * 
	 * @param index   of the node to find
	 * @param current Node during the traversal
	 */

	public void getRec(int index, Node current) {
		if (current == null) {
			return;
		}

		getRec(index, current.left);

		if (counter < index + 1) {
			counter++;
			if (counter == index + 1) {
				gotten = current;
				return;
			}
		}

		getRec(index, current.right);

	}

	/**
	 * Internal private Node class specifying the data contained by each node and
	 * the memory address of the next and previous nodes in the list.
	 */
	protected class Node implements Comparable<Node> {

		protected E data;
		protected Node right;
		protected Node left;
		protected int height;

		/**
		 * Creates a node pointing to no other nodes and with specified data.
		 * 
		 * @param generic data Object (denoted by E)
		 */
		public Node(E data) {
			this.data = data;
			right = null;
			left = null;
			height = 0;

		}

		@Override
		public int compareTo(Node o) {
			return (this.data.compareTo(o.data));

		}

	}

	/**
	 * iterator method from the implemented interface.
	 * 
	 * @return an instance of an inorderIterator.
	 */
	public Iterator<E> iterator() {
		return inorderIterator();
	}

	/**
	 * Method creating a new inorderIterator object
	 * 
	 * @return
	 */
	public Iterator<E> inorderIterator() {
		inorderIterator<E> in = new inorderIterator<E>();
		return in;

	}

	/**
	 * Private iterator class for inorder traversal of the tree.
	 * 
	 */
	private class inorderIterator<E> implements Iterator<E> {

		private Queue<Node> queue;

		/**
		 * inorder Iterator constructor populates the queue of nodes from the inorder
		 * traversal.
		 */
		public inorderIterator() {
			queue = new LinkedList<Node>();
			// to make the stack
			linearize(root);
		}

		/**
		 * Method actually traversing the tree and adding nodes explored to the
		 * iterator's queue.
		 * 
		 * @param node being traversed
		 */
		private void linearize(Node node) {

			if (node != null) {
				if (node.left != null) {
					linearize(node.left);
				}
				queue.add(node);
				if (node.right != null) {
					linearize(node.right);
				}
			}
		}

		/**
		 * Returns the data from the next node
		 * 
		 * @returns data in next node
		 * @throws NoSuchElementException if iterator does not have a next node to
		 *                                return.
		 */
		public E next() throws NoSuchElementException {
			if (this.hasNext() == false) {
				throw new NoSuchElementException();
			}
			return (E) queue.remove().data;
		}

		/**
		 * Returns a true if the current node points to another node, and false if it
		 * points to null.
		 * 
		 * @returns true if next node exists, false if current node points to null.
		 */
		public boolean hasNext() {
			if (!queue.isEmpty())
				return true;

			else
				return false;
		}
	}

	/**
	 * Method returns a preorderIterator instance
	 * 
	 * @return Iterator in preorder traversal
	 */

	public Iterator<E> preorderIterator() {

		preorderIterator<E> pre = new preorderIterator<E>();
		return pre;
	}

	/**
	 * Method returns a preorderIterator instance
	 * 
	 * @return Iterator in postorder traversal
	 */
	public Iterator<E> postorderIterator() {

		postorderIterator<E> post = new postorderIterator<E>();
		return post;
	}

	/**
	 * Internal private class representing an iterator that iterates from the front
	 */
	private class preorderIterator<E> implements Iterator {

		private Queue<Node> queue;

		/**
		 * constructor of a preorderIterator
		 */
		public preorderIterator() {
			queue = new LinkedList<Node>();
			// to make the stack
			linearize(root);

		}

		/**
		 * Recursive method actually traversing the tree in preorder traversal and
		 * populating the iterator's queue as it explores the nodes.
		 * 
		 * @param node
		 */
		private void linearize(Node node) {

			if (node != null) {
				queue.add(node);
				linearize(node.left);
				linearize(node.right);
			}
		}

		/**
		 * Returns the data from the next node
		 * 
		 * @returns data in next node (next from current)
		 */
		public E next() throws NoSuchElementException {
			if (this.hasNext() == false) {
				throw new NoSuchElementException();
			}
			return (E) queue.remove().data;
		}

		/**
		 * Returns a true if the current node points to another node, and false if
		 * it points to null
		 * 
		 * @returns true if next node exists, false if current node points to null.
		 */
		public boolean hasNext() {
			if (!queue.isEmpty())
				return true;

			else
				return false;
		}

	}

	/**
	 * Private postorderIterator class representing an iterator traversing the tree
	 * in postorder.
	 */
	private class postorderIterator<E> implements Iterator {
		private Queue<Node> queue;

		/**
		 * Postorder iterator constructor initializing the iterator's queue.
		 */
		public postorderIterator() {
			queue = new LinkedList<Node>();
			// to make the stack
			linearize(root);

		}

		/**
		 * Recursive method traversing the tree in postorder and populating the
		 * itertaor's queue.
		 * 
		 * @param node
		 */

		private void linearize(Node node) {

			if (node != null) {
				linearize(node.left);
				linearize(node.right);
				queue.add(node);
			}
		}

		/**
		 * Returns the data from the next node in the list.
		 * 
		 * @returns data in next node
		 */
		public E next() throws NoSuchElementException {
			if (this.hasNext() == false) {
				throw new NoSuchElementException();
			}
			return (E) queue.remove().data;
		}

		/**
		 * Returns a true if the current node points to another node, and false if it
		 * points to null
		 * 
		 * @returns true if next node exists, false if current node points to null.
		 */
		public boolean hasNext() {
			if (!queue.isEmpty())
				return true;

			else
				return false;
		}
	}

	/**
	 * Produces tree like string representation of this tree. Returns a string
	 * representation of this tree in a tree-like format. The string representation
	 * consists of a tree-like representation of this tree. Each node is shown in
	 * its own line with the indentation showing the depth of the node in this tree.
	 * The root is printed on the first line, followed by its left subtree, followed
	 * by its right subtree.
	 * 
	 * @returns String representation of tree
	 * 
	 */
	public String toStringTreeFormat() {
		StringBuffer sb = new StringBuffer();
		toStringTree(sb, root, 0);
		return sb.toString();
	}

	private void toStringTree(StringBuffer sb, Node node, int level) {
		// display the node
		if (level > 0) {
			for (int i = 0; i < level - 1; i++) {
				sb.append("   ");
			}
			sb.append("|--");
		}
		if (node == null) {
			sb.append("null \n");
			return;
		} else {
			sb.append(node.data + " " + node.height + "\n");
		}

		// display the left subtree
		toStringTree(sb, node.left, level + 1);
		// display the right subtree
		toStringTree(sb, node.right, level + 1);

	}

	/**
	 * Returns a string representation of this tree. The string representation
	 * consists of a list of the tree's elements in the order they are returned by
	 * its iterator (inorder traversal), enclosed in square brackets ("[]").
	 * Adjacent elements are separated by the characters ", " (comma and space).
	 * Elements are converted to strings as by String.valueOf(Object). This
	 * operation is O(N).
	 * 
	 * @return String representation of the tree in inorder traversal
	 * 
	 */

	public String toString() {

		Iterator<E> itr = this.iterator();
		String tree = "";
		;
		while (itr.hasNext()) {
			tree += " " + itr.next();

		}
		return tree;

	}

	/**
	 * Returns the least element in this tree greater than or equal to the given
	 * element, or null if there is no such element. This operation is O(H).
	 * 
	 * @param e - the value to match
	 * @return the least element greater than or equal to e, or null if there is no
	 *         such element
	 * @throws ClassCastException   - if the specified element cannot be compared
	 *                              with the elements currently in the set
	 * @throws NullPointerException - if the specified element is null
	 */

	public E ceiling​(E e) throws NullPointerException {

		if (e == null) {
			throw new NullPointerException("element cannot be null");
		}
		ceiling = null;
		ceilingRec(e, root);
		return ceiling;
	}

	/**
	 * Recursive method traversing the tree and updating the ceiling data field when
	 * value to match or the least element in this tree greater than the value to
	 * that value.
	 * 
	 * @param data given
	 * @param curr node
	 */

	public void ceilingRec(E data, Node curr) {
		if (curr == null) {
			return;
		}

		else if (data.compareTo(curr.data) < 0) {
			ceilingRec(data, curr.left);
			if (ceiling == null) {
				ceiling = curr.data;
			}
		}

		else if (data.compareTo(curr.data) > 0) {
			ceilingRec(data, curr.right);

		} else {
			ceiling = curr.data;
		}
	}

	/**
	 * Returns the greatest element in this set less than or equal to the given
	 * element, or null if there is no such element. This operation is O(H).
	 * 
	 * @param e - the value to match
	 * @return the greatest element less than or equal to e, or null if there is no
	 *         such element
	 * @throws ClassCastException   - if the specified element cannot be compared
	 *                              with the elements currently in the set
	 * @throws NullPointerException - if the specified element is null
	 */
	public E floor​(E e) throws NullPointerException {
		if (e == null) {
			throw new NullPointerException("element cannot be null");

		}
		floor = null;
		floorRec(e, root);
		return floor;

	}

	/**
	 * Recursive method updating the floor data field to find the greatest element
	 * in this set less than or equal to the given element.
	 * 
	 * @param data
	 * @param curr
	 */

	public void floorRec(E data, Node curr) {
		if (curr == null) { //base case
			return;
		}

		else if (data.compareTo(curr.data) < 0) { //if data is less go left
			floorRec(data, curr.left); 

		}

		else if (data.compareTo(curr.data) > 0) { // if  our data is greater than nodes go right
			floorRec(data, curr.right);
			if (floor == null) { //when it returns because it will have hit the null at a leaf node
				floor = curr.data; //if we didnt get 0 for any comparacents we returned w floor is zero
			}

		} else {
			floor = curr.data;
			return;
		}
	}

	/**
	 * Returns the first (lowest) element currently in this tree. This operation is
	 * O(H).
	 * 
	 * @return the lowest element in the current tree
	 * @throws NoSuchElementException - if this set is empty
	 */
	public E first() throws NoSuchElementException {
		if (root == null) {
			throw new NoSuchElementException("The set cannot be empty");
		}

		Node current = root;
		while (current.left != null) {

			current = current.left;
		}
		return current.data;

	}

	/**
	 * Returns the last (highest) element currently in this tree. This operation is
	 * O(H).
	 * 
	 * @return highest element in tree
	 * @throws NoSuchElementException - if this set is empty
	 */
	public E last() throws NoSuchElementException {
		if (root == null) {
			throw new NoSuchElementException("The set cannot be empty");
		}
		Node current = root;
		while (current.right != null) {
			current = current.right;
		}
		return current.data;
	}

	/**
	 * Returns the greatest element in this set strictly less than the given
	 * element, or null if there is no such element. This operation is O(H).
	 * 
	 * @param e - the value to match
	 * @returns the greatest element less than e, or null if there is no such
	 *          element
	 * @throws ClassCastException   - if the specified element cannot be compared
	 *                              with the elements currently in the set
	 * @throws NullPointerException - if the specified element is null
	 */

	public E lower​(E e) throws NullPointerException {
		if (e == null) {
			throw new NullPointerException("element cant be null");
		}

		lower = null;
		lower(e, root);
		return lower;
	}

	/**
	 * Recursive method updating the lower data field to find the the greatest
	 * element in this set strictly less than the given element.
	 * 
	 * @param data
	 * @param curr
	 */
	private void lower(E data, Node curr) {
		if (curr == null) {
			return;
		}

		int comp = data.compareTo(curr.data);
		if (comp <= 0) {
			lower(data, curr.left);
		} else {
			lower = curr.data;
			lower(data, curr.right);
		}
	}

	/**
	 * Returns the least element in this tree strictly greater than the given
	 * element, or null if there is no such element. This operation is O(H).
	 * 
	 * @return the least element greater than e, or null if there is no such element
	 * @param e - the value to match
	 * @throws ClassCastException   - if the specified element cannot be compared
	 *                              with the elements currently in the set
	 * @throws NullPointerException - if the specified element is null
	 */

	// or some reason my higher wont work but it seems o work when i test it
	public E higher​(E e) {
		if (e == null) {
			throw new NullPointerException("element cant be null");
		}

		higher = null;
		higher(e, root);
		return higher;
	}

	/**
	 * Recursive method updating the higher data field to the least element in this
	 * tree strictly greater than the given element or null.
	 * 
	 * @param data
	 * @param curr
	 */
	private void higher(E data, Node curr) {
		if (curr == null) {
			return;
		}

		int comp = data.compareTo(curr.data);
		if (comp >= 0) {
			higher(data, curr.right);
		}

		else {
			higher = curr.data;
			higher(data, curr.left);
		}

	}

	/**
	 * Compares the specified object with this tree for equality. Returns true if
	 * the given object is also a tree, the two trees have the same size, and every
	 * member of the given tree is contained in this tree. This operation is O(N).
	 * 
	 * @param obj - object to be compared for equality with this tree
	 * @return true if the specified object is equal to this tree
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BST))
			return false;

		BST<E> given = (BST<E>) obj;
		if (given.size() != this.size()) {
			return false;
		}

		Iterator<E> theirItr = given.iterator();

		while (theirItr.hasNext()) {
			if (!this.contains​(theirItr.next())) {
				return false;
			}

		}
		return true;
	}
	
	

}
