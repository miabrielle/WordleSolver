import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static ArrayList<String> getInput() throws IOException {
        System.out.println("Please input the letters");
        System.out.println("For a green letter, add a plus after the letter, for a yellow letter use a minus");
        System.out.println("After all guesses have been submitted, enter an empty line");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> guesses = new ArrayList<>();
        String guess = reader.readLine();
        while (!Objects.equals(guess, "")) {
            guesses.add(guess);
            guess = reader.readLine();
        }
        return guesses;
    }

    public static void processInput(ArrayList<String> guesses, List<String> words) {
        Map<Character, List<Integer>> yellowLetters = new HashMap<>();
        Map<Integer, Character> greenLetters = new HashMap<>();
        List<Character> notLetters = new ArrayList<>();
        int it = 1;

        for (String i : guesses) {
            for (int j = 0; j < i.length(); j++) {
                i = i.toLowerCase();
                Character letter = i.charAt(j);
                Character next = ((j + 1) < i.length()) ? i.charAt(j + 1) : '!';

                if (next == '+') {                                  // GREEN
                    if (!greenLetters.containsKey(it)) {
                        greenLetters.put(it, letter);
                    }
                    j++; // get past +

                } else if (next == '-') {
                    if (yellowLetters.containsKey(letter)) {   // YELLOW FOUND BEFORE
                        yellowLetters.get(letter).add(it);
                    } else {                                         // YELLOW NOT FOUND BEFORE
                        yellowLetters.put(letter, new ArrayList<>(Arrays.asList(it)));
                    }
                    j++; // get past -

                } else {                                    // NOT A LETTER
                    notLetters.add(i.charAt(j));
                }
                it++;
            }
            it = 1;
        }
        findWords(words, notLetters, greenLetters, yellowLetters);

    }

    public static void findWords(List<String> words, List<Character> notLetters, Map<Integer, Character> greenLetters, Map<Character, List<Integer>> yellowLetters) {
        for (Character n : notLetters) {
            words = words.stream().filter(w -> w.indexOf(n) == -1).collect(Collectors.toList());
        }

        for (Integer gr : greenLetters.keySet()) {
            Character g = greenLetters.get(gr);
            words = words.stream().filter(w -> w.indexOf(g) == (gr - 1)).collect(Collectors.toList());
        }

        for (Character yel : yellowLetters.keySet()) {
            for (Integer y : yellowLetters.get(yel)) { // != -1 || != y
                words = words.stream().filter(w -> (w.indexOf(yel) != -1) && (w.indexOf(yel) != (y - 1))).collect(Collectors.toList());
            }
        }

        System.out.println(words);

    }

    public static void main(String[] args) throws IOException {
        List<String> words = readFile();
        ArrayList<String> guesses = getInput();
        processInput(guesses, words);
    }

    public static List<String> readFile() throws IOException {
        File file = new File("./resources/possibleWords");
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> list = new ArrayList<>();
        String st;
        while ((st = br.readLine()) != null) {
            list.add(st);
        }
        return list;
    }

}