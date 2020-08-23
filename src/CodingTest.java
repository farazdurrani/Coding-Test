import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author faraz
 * 
 *         A program that accepts as input a filename of common words and a
 *         filename of text that outputs the word counts of the text after the
 *         common words are removed sorted by count descending and formatted
 *         nicely.
 *
 */
public class CodingTest {

    public static void main(String[] args) {

	verifyFiles(args);

	List<String> commonWordsList = readCommonWords(args[0]);

	Map<String, Integer> ebookMap = readEbook(args[1]);

	Map<String, Integer> wordsWithCount = getCount(commonWordsList, ebookMap);

	printNicely(wordsWithCount);

    }

    /**
     * Print map nicely
     * 
     * @param wordsWithCount - This map contains words with counts in desc order
     */
    private static void printNicely(Map<String, Integer> wordsWithCount) {
	wordsWithCount.forEach((word, count) -> {
	    System.out.printf("%-20s %d%n", word, count);
	});
    }

    /**
     * This method removes the common words from map and then sends it off to
     * sortByValue method
     * 
     * @param commonWordsList - common words read from a text file
     * @param ebookMap        - a map with words as keys and number of occurence as
     *                        values.
     * @return
     */
    private static Map<String, Integer> getCount(List<String> commonWordsList,
            Map<String, Integer> ebookMap) {
	// common words are removed as per Gist's instructions
	ebookMap.keySet().removeAll(commonWordsList);
	return sortByValue(ebookMap);
    }

    /**
     * This method sorts the map in desc order of values.
     * 
     * @param wordsWithCountUnsorted
     * @param order
     * @return
     */
    private static Map<String, Integer> sortByValue(
            Map<String, Integer> wordsWithCountUnsorted) {
	boolean DESC = false;
	List<Entry<String, Integer>> list = new LinkedList<>(
	        wordsWithCountUnsorted.entrySet());
	list.sort((o1, o2) -> DESC
	        ? o1.getValue().compareTo(o2.getValue()) == 0
	                ? o1.getKey().compareTo(o2.getKey())
	                : o1.getValue().compareTo(o2.getValue())
	        : o2.getValue().compareTo(o1.getValue()) == 0
	                ? o2.getKey().compareTo(o1.getKey())
	                : o2.getValue().compareTo(o1.getValue()));
	return list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue,
	        (a, b) -> b, LinkedHashMap::new));

    }

    /**
     * This method reads thru the file that contains common words.
     * 
     * @param filename - name of the file with path
     * @return a list with words.
     */
    private static List<String> readCommonWords(String filename) {
	File file = new File(filename);
	Scanner input = null;
	try {
	    input = new Scanner(file);
	} catch (FileNotFoundException e) {
	    throw new RuntimeException("File: " + filename + " not found.");
	}

	List<String> list = new ArrayList<>();
	while (input.hasNext()) {
	    list.add(input.next().replaceAll("[^a-zA-Z0-9]+", ""));
	}
	input.close();
	return list;
    }

    /**
     * This method reads the ebook and stores each word with count in a map.
     * 
     * @param filename - a name of file with path
     * @return each word in a Map form where word is key and value is number of
     *         occurence
     */
    private static Map<String, Integer> readEbook(String filename) {
	File file = new File(filename);
	Scanner input = null;
	try {
	    input = new Scanner(file);
	} catch (FileNotFoundException e) {
	    throw new RuntimeException("File: " + filename + " not found.");
	}

	Map<String, Integer> map = new HashMap<>();
	while (input.hasNext()) {
	    String word = input.next().replaceAll("[^a-zA-Z0-9]+", "");
	    if (!word.trim().isEmpty()) {
		if (map.containsKey(word)) {
		    map.put(word, map.get(word) + 1);
		} else {
		    map.put(word, 1);
		}
	    }
	}
	input.close();
	return map;
    }

    /**
     * This method checks if program is run with command line arguments, and file
     * names passed as CMD args are actual files.
     * 
     * @param args - file names at position 0 and 1 with paths
     */
    private static void verifyFiles(String[] args) {
	if (args == null || args.length != 2 || !(new File(args[0]).exists())
	        || !(new File(args[1]).exists())) {
	    throw new RuntimeException(
	            "File names must be provided.\nMake sure file names are full file names with full path\nAnd make sure 2 file names are provided, first being the file that contains common words and second being the one that contains the whole ebook.");
	}
    }
}
