package timmeeyLib.dummy;

public class LoremIpsum {
	/**
	 * Just gives a number of word for placeholder purposes. Uses Lorem Ipsum
	 * 
	 * @param words
	 *            Number of words
	 * @return The string with the desired amount of words
	 */
	public static String getLoremIpsumWords(int words) {
		return com.thedeanda.lorem.Lorem.getWords(words);
	}

	/**
	 * Just gives a numer of paragraphs of lorem ipsum placeholder text
	 * 
	 * @param count
	 *            Number of paragraphs
	 * @return A String containing thedesired amount of paragraphs from
	 *         loremIpsum
	 */
	public static String getLoremIpsumParagraphs(int count) {
		return com.thedeanda.lorem.Lorem.getParagraphs(count, count);
	}
}
