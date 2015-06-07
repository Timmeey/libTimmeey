package de.timmeey.libTimmeey.math;

import java.util.Random;

import de.timmeey.libTimmeey.dummy.LoremIpsum;

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

	public static String[] generateRandomWords(int numberOfWords) {
		String[] randomStrings = new String[numberOfWords];
		Random random = new Random();
		for (int i = 0; i < numberOfWords; i++) {
			char[] word = new char[random.nextInt(8) + 3]; // words of length 3
															// through 10. (1
															// and 2 letter
															// words are
															// boring.)
			for (int j = 0; j < word.length; j++) {
				word[j] = (char) ('a' + random.nextInt(26));
			}
			randomStrings[i] = new String(word);
		}
		return randomStrings;
	}
}
