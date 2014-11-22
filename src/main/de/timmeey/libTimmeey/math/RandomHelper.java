package de.timmeey.libTimmeey.math;

public class RandomHelper {

	/**
	 * Gives a random Number between min and max
	 * 
	 * @param min
	 *            Min value of the returned number
	 * @param max
	 *            max Value of the returned number
	 * @return random number between min and max
	 */
	public static int getRandom(int min, int max) {
		if (max < min || max == min) {
			throw new ArithmeticException("Max cant be smaller/equal min");
		}
		double rand = Math.random();
		while (rand * max < min || rand * max > max) {
			rand = Math.random();
		}
		return (int) (rand * max);

	}
}
