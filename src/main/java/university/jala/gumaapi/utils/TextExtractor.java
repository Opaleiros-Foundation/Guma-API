package university.jala.gumaapi.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextExtractor {
    public static String[] extractThinkContent(String text) {
        Pattern pattern = Pattern.compile("<think>(.*?)</think>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        String thinkContent = "";
        if (matcher.find()) {
            thinkContent = matcher.group(1).trim();
        }

        String remainingContent = matcher.replaceFirst("").trim();

        return new String[]{thinkContent, remainingContent};
    }

}