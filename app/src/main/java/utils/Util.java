package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;


public class Util {
    public static String readFile(String file_path) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(new File(file_path)));
        String str =  fileReader.lines().collect(Collectors.joining(System.lineSeparator()));
        fileReader.close();
        return str;
    }

    public static String GetMessage(boolean isHold, String expression, String stateID)
    {

        return String.format("Property %s %s in state %s"
                , expression
                , isHold ? "holds" : "does not hold"
                , stateID);
    }

    public static String cleanText(String text)
    {
        text = text.replaceAll("[^\\x00-\\x7F]", "");

        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        text = text.replaceAll("\\p{C}", "");

        return text.trim();
    }

}
