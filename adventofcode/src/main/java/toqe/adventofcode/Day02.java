package toqe.adventofcode;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Day02 {
    private final Pattern dimensionsPattern = Pattern.compile("([0-9]+)x([0-9]+)x([0-9]+)");

    public void run() throws Exception {
        // part 1
        assert getWrappingPaperSize("2x3x4") == 58;
        assert getWrappingPaperSize("1x1x10") == 43;

        var input = InputFileHelper.readInput(2);
        var sum = 0L;

        for (var line : input.split("\n")) {
            sum += getWrappingPaperSize(line);
        }

        System.out.println("wrapping paper needed: " + sum);

        // part 2
        assert getRibbonLength("2x3x4") == 34;
        assert getRibbonLength("1x1x10") == 14;

        sum = 0L;

        for (var line : input.split("\n")) {
            sum += getRibbonLength(line);
        }

        System.out.println("ribbon needed: " + sum);
    }

    public int GetRibbonLength() {
        return 0;
    }

    public int getWrappingPaperSize(String dimensions) throws Exception {
        return new Dimensions(dimensions).getWrappingPaperSize();
    }

    public int getRibbonLength(String dimensions) throws Exception {
        return new Dimensions(dimensions).getRibbonLength();
    }

    private class Dimensions {
        private final int length;
        private final int width;
        private final int height;

        public Dimensions(String dimensions) throws Exception {
            var matcher = dimensionsPattern.matcher(dimensions);

            if (matcher.find()) {
                this.length = Integer.parseInt(matcher.group(1));
                this.width = Integer.parseInt(matcher.group(2));
                this.height = Integer.parseInt(matcher.group(3));
            } else {
                throw new Exception("dimensions not found");
            }
        }

        public int getWrappingPaperSize() {
            var sideA = this.length * this.width;
            var sideB = this.length * this.height;
            var sideC = this.width * this.height;
            var minSide = Math.min(Math.min(sideA, sideB), sideC);
            return 2 * sideA + 2 * sideB + 2 * sideC + minSide;
        }

        public int getRibbonLength() {
            int[] sides = { this.length, this.width, this.height };

            var shortestSides = Arrays.stream(sides)
                .sorted()
                .limit(2)
                .toArray();

            return shortestSides[0] + shortestSides[0] + shortestSides[1] + shortestSides[1]
                + (this.length * this.width * this.height);
        }
    }
}
