package toqe.adventofcode;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class InputFileHelper {
    public static String readInput(int day) throws IOException {
        File inputsPath = new File("inputs");
        File dayInputPath = new File(inputsPath, "Day" + String.format("%02d", day));
        File file = new File(dayInputPath, "input.txt").getAbsoluteFile();
        String fileData = FileUtils.readFileToString(file, "UTF-8");
        return fileData;
    }
}
