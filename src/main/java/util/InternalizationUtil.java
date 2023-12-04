package util;

import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class InternalizationUtil {

    private static final Pattern LANG_CODE_PATTERN = Pattern.compile("/[a-zA-Z]{2}/");
    private static final Set<String> ISO_LANGUAGES = Set.of(Locale.getISOLanguages());

    private static final Set<String> CODES = ISO_LANGUAGES.stream()
        .map(code -> "(" + code + ")")
        .collect(Collectors.toSet());

    private static final String JOINED_ISO_LANGUAGES = "/(" + String.join("|", CODES) + ")/";

    private InternalizationUtil() {
        // hide public constructor
    }

    public static boolean hasInternalizationStatic(String link) {
        Pattern pattern = Pattern.compile(JOINED_ISO_LANGUAGES);
        Matcher matcher = pattern.matcher(link);
        return matcher.find();
    }

    public static boolean hasInternalizationDynamic(String link) {
        Matcher matcher = LANG_CODE_PATTERN.matcher(link);
        if (matcher.find()) {
            String foundMatch = matcher.group();
            String languageCode = foundMatch.replaceAll("/", "");
            return ISO_LANGUAGES.contains(languageCode);
        }
        return false;
    }
}
