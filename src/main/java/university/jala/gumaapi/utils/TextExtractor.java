package university.jala.gumaapi.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextExtractor {
    public static String[] extractThinkContent(String text) {
        Pattern pattern = Pattern.compile("<think>(.*?)</think>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        String thinkContent = "";
        if (matcher.find()) {
            thinkContent = matcher.group(1).trim(); // Captura o conteúdo dentro de <think>
        }

        // Criar um novo texto removendo apenas a primeira ocorrência de <think>...</think>
        String remainingContent = matcher.replaceFirst("").trim();

        // Retornar um array com as duas partes
        return new String[]{thinkContent, remainingContent};
    }

}