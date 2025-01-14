package toqe.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day05 {
    public void run() throws Exception {
        // part 1
        assert this.isNicePart1("ugknbfddgicrmopn");
        assert this.isNicePart1("aaa");
        assert !this.isNicePart1("jchzalrnumimnmhp");
        assert !this.isNicePart1("haegwjzuvuyypxyu");
        assert !this.isNicePart1("dvszwmarrgswjxmb");

        var input = InputFileHelper.readInput(5);
        var niceStringsCount = 0;

        for (var line : input.split("\n")) {
            if (this.isNicePart1(line)) {
                niceStringsCount++;
            }
        }

        System.out.println("nice strings count part 1: " + niceStringsCount);

        // part 2
        assert this.isNicePart2("qjhvhtzxzqqjkmpb");
        assert this.isNicePart2("xxyxx");
        assert !this.isNicePart2("uurcxstgmygtbstg");
        assert !this.isNicePart2("ieodomkazucvgmuy");

        niceStringsCount = 0;

        for (var line : input.split("\n")) {
            if (this.isNicePart2(line)) {
                niceStringsCount++;
            }
        }

        System.out.println("nice strings count part 2: " + niceStringsCount);
    }

    public boolean isNicePart1(String text) {
        int[] vowels = { 'a', 'e', 'i', 'o', 'u' };
        var vowelsCount = text.chars().filter(x -> Arrays.stream(vowels).anyMatch(y -> y == x)).count();
        var containsMinThreeVowels = vowelsCount >= 3;

        if (!containsMinThreeVowels) {
            return false;
        }

        var containsLetterTwiceInRow = false;

        for (var i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) == text.charAt(i + 1)) {
                containsLetterTwiceInRow = true;
                break;
            }
        }

        if (!containsLetterTwiceInRow) {
            return false;
        }

        String[] blacklist = { "ab", "cd", "pq", "xy" };
        var blacklistStream = Arrays.stream(blacklist);

        if (blacklistStream.anyMatch(x -> text.contains(x))) {
            return false;
        }

        return true;
    }

    public boolean isNicePart2(String text) {
        var pairIndices = new HashMap<String, List<Integer>>();
        var hasRule2 = false;

        for (var i = 0; i < text.length() - 1; i++) {
            if (!hasRule2 && i < text.length() - 2) {
                if (text.charAt(i) == text.charAt(i + 2)) {
                    hasRule2 = true;
                }
            }

            var pair = text.substring(i, i + 2);

            if (pairIndices.containsKey(pair)) {
                pairIndices.get(pair).add(i);
            } else {
                pairIndices.put(pair, new ArrayList<Integer>(Arrays.asList(i)));
            }
        }

        var hasRule1 = pairIndices.entrySet()
                .stream()
                .filter(e -> e.getValue().size() > 2
                        || (e.getValue().size() == 2 && (e.getValue().get(1) - e.getValue().get(0) > 1)))
                .anyMatch(e -> true);

        return hasRule1 && hasRule2;
    }
}
