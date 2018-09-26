import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Runner {

	static String atr[];
	static int[][] feat;
	static int index;

	public static void main(String[] args) throws FileNotFoundException {
		Scanner file = new Scanner(new File("train.dat"));
		atr = file.nextLine().split("[ \t]+");

		feat = new int[100000][atr.length];
		index = 0;

		double initOne = 0;
		double initZero = 0;

		while (file.hasNextLine()) {

			String[] in = file.nextLine().split("[ \t]+");
			for (int i = 0; i < in.length; i++) {
				feat[index][i] = Integer.parseInt(in[i]);
			}

			initOne += feat[index][in.length - 1] == 1 ? 1 : 0;
			initZero += feat[index][in.length - 1] == 0 ? 1 : 0;

			index++;
		}

		double initEntropy = calcEntropy(initZero, initOne, initZero + initOne);

		Node init = new Node(index);
		init.entropy = Integer.MAX_VALUE;

		init = getNode(initEntropy, init.look, init.atri);

		buildTree(init);
		test(init);
		// printTree(init, "");

	}

	public static void test(Node init) {
		double cor = 0;
		double total = index - 1;
		for (int i = 0; i < index; i++) {
			Node temp = init;
			for (int j = 0; j < feat[0].length - 2; j++) {
				if (feat[i][j] == 1) {
					temp = temp.one;
				} else {
					temp = temp.zero;
				}
			}
			cor += (feat[i][feat[0].length - 1] == 1) == (temp.left[0] >= temp.left[1]) ? 1 : 0;
		}
		System.out.println(cor / total);
	}

	public static void printTree(Node init, String bar) {

		if (init != null) {

			System.out.println(
					bar + " " + init.name + " = 0 : " + (init.hasResult ? init.left[0] >= init.left[1] ? 1 : 0 : ""));
			printTree(init.zero, bar + "| ");
			System.out.println(
					bar + " " + init.name + " = 1 : " + (init.hasResult ? init.left[0] >= init.left[1] ? 1 : 0 : ""));
			printTree(init.one, bar + "| ");

		}
	}

	public static void buildTree(Node init) {
		if (init != null) {

			init.one = getNode(init.entropy, init.branch1, init.atri);
			init.zero = getNode(init.entropy, init.branch0, init.atri);

			if (init.one.atri.size() == atr.length - 1) {
				init.one.hasResult = true;
			}
			if (init.zero != null && init.zero.atri.size() == atr.length - 1) {
				init.zero.hasResult = true;
			}

			if (init.one != null) {
				init.one.parent = init;
			}
			if (init.zero != null) {
				init.zero.parent = init;
			}

			if (init.one != null && init.zero != null && !init.one.hasResult && !init.zero.hasResult) {
				buildTree(init.one);
				buildTree(init.zero);
			} else if (init.one != null && !init.one.hasResult) {
				buildTree(init.one);
			}

			else if (init.zero != null && !init.zero.hasResult) {
				buildTree(init.zero);
			}

		}

	}

	public static Node getNode(double initEntropy, ArrayList<Integer> look, ArrayList<Integer> atri) {
		Node init = new Node();
		init.entropy = Integer.MAX_VALUE;

		for (int i = 0; i < atr.length - 1; i++) {

			if (!atri.contains(i)) {
				Node temp = new Node();
				temp.name = atr[i];

				for (int xd = 0; xd < atri.size(); xd++) {

					temp.atri.add(atri.get(xd));
				}

				temp.atri.add(i);

				double oneone = 0; // when its one and it return 1
				double onezero = 0; // when its one and it returns 0
				double zeroone = 0; // when its zero and it returns 1
				double zerozero = 0; // when its zero and it returns 1

				for (int x = 0; x < index; x++) {
					if (look.contains(x)) {
						if (feat[x][i] == 1) {
							temp.branch1.add(x);
							if (feat[x][atr.length - 1] == 1) {
								oneone++;
							} else {
								onezero++;
							}
						} else {
							temp.branch0.add(x);
							if (feat[x][atr.length - 1] == 1) {
								zeroone++;
							} else {
								zerozero++;
							}
						}
					}
				}

				double var1 = onezero + oneone;
				double var2 = zerozero + zeroone;
				double total = var1 + var2;

				temp.entropy = (var1 / total) * calcEntropy(onezero, oneone, var1)
						+ (var2 / total) * calcEntropy(zerozero, zeroone, var2);

				if (temp.entropy < init.entropy) {
					if (temp.entropy == 0 || atri.size() == atr.length - 1) {
						temp.hasResult = true;
						temp.left = new double[] { oneone + zeroone, onezero + zerozero };

					}
					temp.left = new double[] { oneone + zeroone, onezero + zerozero };

					init = temp;

				}

			}
		}
		return init;
	}

	public static double informationGain(double oldEntropy, double newEntropy) {
		return oldEntropy - newEntropy;
	}

	public static double calcEntropy(double var1, double var2, double total) {
		return calcE(var1, total) + calcE(var2, total);
	}

	public static double calcE(double var1, double total) {
		if (var1 == 0) {
			return 0;
		}
		double a = (Math.log(var1 / total) / Math.log(2));
		return -1 * ((var1 / total) * (Math.log(var1 / total) / Math.log(2)));
	}

}

class Node {
	double entropy;
	Node zero;
	Node one;
	Node parent;
	boolean isResult;
	String name;

	double[] left = { -1, -1 }; // ones and zero

	boolean wasP = false;
	boolean hasResult;
	int result = -1;

	int branchL;

	int print;

	ArrayList<Integer> look = new ArrayList<>();

	ArrayList<Integer> branch0 = new ArrayList<>();
	ArrayList<Integer> branch1 = new ArrayList<>();

	ArrayList<Integer> atri = new ArrayList<>();

	public Node() {
		result = -1;
	}

	public Node(int x) {
		for (int i = 0; i < x; i++) {
			look.add(i);
		}
	}

}