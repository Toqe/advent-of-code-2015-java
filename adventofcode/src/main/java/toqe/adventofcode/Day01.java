package toqe.adventofcode;

public class Day01 {
    public void run() throws Exception {
        // part 1
        assert getFloor("(())") == 0;
        assert getFloor("()()") == 0;
        assert getFloor("(()(()(") == 3;
        assert getFloor("))(((((") == 3;
        assert getFloor("))(") == -1;
        assert getFloor(")())())") == -3;

        var input = InputFileHelper.readInput(1);
        System.out.println("floor: " + getFloor(input));

        // part 2
        assert getBasementPosition(")") == 1;
        assert getBasementPosition("()())") == 5;

        System.out.println("basement position: " + getBasementPosition(input));
    }

    public int getFloor(String s) {
        var ups = (int)(s.chars().filter(x -> x == '(').count());
        var downs = (int)(s.chars().filter(x -> x == ')').count());
        return ups - downs;
    }

    public int getBasementPosition(String s) throws Exception {
        int currentFloor = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                currentFloor++;
            } else {
                currentFloor--;
            }

            if (currentFloor == -1) {
                return i + 1;
            }
        }

        throw new Exception("basement not found");
    }
}