package toqe.adventofcode;

import java.util.HashSet;

public class Day03 {
    public void run() throws Exception {
        // part 1
        assert countHouses(">") == 2;
        assert countHouses("^>v<") == 4;
        assert countHouses("^v^v^v^v^v") == 2;

        var input = InputFileHelper.readInput(3);
        var housesCount = countHouses(input);
        System.out.println("houses: " + housesCount);

        // part 2
        assert countHousesWithRobotSanta("^v") == 3;
        assert countHousesWithRobotSanta("^>v<") == 3;
        assert countHousesWithRobotSanta("^v^v^v^v^v") == 11;

        var housesCountWithRobotSanta = countHousesWithRobotSanta(input);
        System.out.println("houses with robot santa: " + housesCountWithRobotSanta);
    }

    public int countHouses(String directions) throws Exception {
        var set = new HashSet<XY>();
        var current = new XY(0, 0);
        set.add(current);

        for (var direction : directions.toCharArray()) {
            current = current.move(direction);
            set.add(current);
        }

        return set.size();
    }

    public int countHousesWithRobotSanta(String directions) throws Exception {
        var set = new HashSet<XY>();
        var currentSanta = new XY(0, 0);
        var currentRobotSanta = new XY(0, 0);
        set.add(currentSanta);
        var isSantaTurn = true;

        for (var direction : directions.toCharArray()) {
            if (isSantaTurn) {
                currentSanta = currentSanta.move(direction);
                set.add(currentSanta);
            } else {
                currentRobotSanta = currentRobotSanta.move(direction);
                set.add(currentRobotSanta);
            }

            isSantaTurn = !isSantaTurn;
        }

        return set.size();
    }

    private class XY {
        private final int x;
        private final int y;

        public XY(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public XY move(char direction) throws Exception {
            switch (direction) {
                case '>':
                    return new XY(this.x + 1, this.y);
                case '<':
                    return new XY(this.x - 1, this.y);
                case '^':
                    return new XY(this.x, this.y - 1);
                case 'v':
                    return new XY(this.x, this.y + 1);
            }

            throw new Exception("unknown direction " + direction);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + x;
            result = prime * result + y;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            XY other = (XY) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (x != other.x)
                return false;
            if (y != other.y)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        private Day03 getEnclosingInstance() {
            return Day03.this;
        }
    }
}
