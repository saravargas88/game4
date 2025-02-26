package project4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This class is responsible for parsing and validating the command line
 * arguments, reading and parsing the input file, producing any error messages,
 * handling any exceptions thrown by other classes, and producing output.
 * 
 * @author Sara Vargas
 */

public class MountainClimb {

	public static void main(String[] args) {

		if (args.length == 0) {
			System.err.println("Usage Error: the program expects file name as an argument.\n");
			System.exit(1);
		}

		// verify that command line argument contains a name of an existing file File
		File file = new File(args[0]);
		if (!file.exists()) {
			System.err.println("Error: the file " + file.getAbsolutePath() + " does not exist.\n");
			System.exit(1);
		}
		if (!file.canRead()) {
			System.err.println("Error: the file " + file.getAbsolutePath() + " cannot be opened for reading.\n");
			System.exit(1);
		}

		// open the file for reading
		Scanner inFile = null;
		try {
			inFile = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.err.println("Error: the file " + file.getAbsolutePath() + " cannot be opened for reading.\n");
			System.exit(1);
		}

		// read the content of the file and save the data in a list of named colors
		BSTMountain mountain = new BSTMountain();
		String line = null;
		Scanner parseLine = null;

		RestStop current = null;

		while (inFile.hasNextLine()) {
			String label = null;
			int food = 0;
			int raft = 0;
			int axe = 0;
			int fallenTree = 0;
			int river = 0;

			try {
				line = inFile.nextLine();
				parseLine = new Scanner(line);
				parseLine.useDelimiter(" ");
				label = parseLine.next();

				ArrayList<String> parsed = new ArrayList();

				while (parseLine.hasNext()) {
					parsed.add(parseLine.next());
				}

				for (int x = 0; x < parsed.size(); x++) {

					if (parsed.get(x).equals("food") && fallenTree == 0 && river == 0) {
						food++;

					}

					if (parsed.get(x).equals("raft") && fallenTree == 0 && river == 0) {
						raft++;

					}

					if (parsed.get(x).equals("axe") && fallenTree == 0 && river == 0) {
						axe++;

					}

					// check for obstacles

					if (parsed.get(x).equals("fallen")) {
						if (x + 1 < parsed.size() && parsed.get(x + 1).equals("tree")) {
							fallenTree++;

						}

					} else if (parsed.get(x).equals("river")) {
						river++;

					}

				}

			}

			catch (NoSuchElementException ex) {
				// caused by an incomplete or miss-formatted line in the input file
				System.err.println(line);
				continue;
			}

			try {
				current = new RestStop(label, food, raft, axe, fallenTree, river);
				mountain.add(current);
			} catch (IllegalArgumentException ex) {
				System.err.println(ex);
			}
		}

		try {
			mountain.hike();

			for (int i = 0; i < mountain.getPaths().size(); i++) {

				for (int x = 0; x < mountain.getPaths().get(i).size(); x++) {

					System.out.print(mountain.getPaths().get(i).get(x).data + " ");

				}

				System.out.println();
			}
		} catch (Exception e) {
			System.err.println(e);
		}

	}
}
