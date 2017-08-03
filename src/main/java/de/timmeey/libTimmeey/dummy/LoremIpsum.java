package de.timmeey.libTimmeey.dummy;

public class LoremIpsum {

    private final static com.thedeanda.lorem.LoremIpsum src = new com
        .thedeanda.lorem.LoremIpsum();

    /**
     * Just gives a number of word for placeholder purposes. Uses Lorem Ipsum
     * @param words Number of words
     * @return The string with the desired amount of words
     */
    public static String getLoremIpsumWords(int words) {
        return src.getWords(words);
    }

    /**
     * Just gives a numer of paragraphs of lorem ipsum placeholder text
     * @param count Number of paragraphs
     * @return A String containing thedesired amount of paragraphs from
     * loremIpsum
     */
    public static String getLoremIpsumParagraphs(int count) {
        return src.getParagraphs(count, count);
    }
}
