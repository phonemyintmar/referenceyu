package mm.com.mytelpay.adapter.common.persistence.support;

import java.util.Objects;

public class SqlUtils {
    public static final String PERCENT = "%";

    private SqlUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String replaceSpecialCharacter(String param) {
        if (Objects.isNull(param)) {
            return "";
        }
        return param.replace("%", "/%").replace("_", "/_").replace("!", "/!").trim().toLowerCase();
    }

    public static String encodeKeyword(String param) {
        if (Objects.isNull(param)) {
            return null;
        }
        StringBuilder sb = new StringBuilder(5);
        sb.append(PERCENT);
        sb.append(replaceSpecialCharacter(param));
        sb.append(PERCENT);
        return sb.toString();
    }
}
