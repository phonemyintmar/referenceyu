package mm.com.mytelpay.adapter.common.persistence.config;

import org.hibernate.EmptyInterceptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SafeSqlInterceptor extends EmptyInterceptor {

    private static final Pattern patternLike =
            Pattern.compile("[^\\s]+\\s+like\\s*\\?", Pattern.CASE_INSENSITIVE);
    private static final Pattern patternIn =
            Pattern.compile("[^\\s]+\\s+in\\s*\\(\\s*\\?[^\\(]*\\)", Pattern.CASE_INSENSITIVE);
    private static final int IN_CAUSE_LIMIT = 1000;

    @Override
    public String onPrepareStatement(String sql) {
        sql = this.rewriteSqlToAvoidOra01795(sql);
        sql = this.rewriteSqlLike(sql);
        return super.onPrepareStatement(sql);
    }

    private String rewriteSqlToAvoidOra01795(String sql) {
        Matcher matcher = patternIn.matcher(sql);
        while (matcher.find()) {
            String inExpression = matcher.group();
            long countOfParameters = inExpression.chars().filter(ch -> ch == '?').count();

            if (countOfParameters <= IN_CAUSE_LIMIT) {
                continue;
            }

            String fieldName = inExpression.substring(0, inExpression.indexOf(' '));
            StringBuilder transformedInExpression =
                    new StringBuilder(" ( ").append(fieldName).append(" in (");

            for (int i = 0; i < countOfParameters; i++) {
                if (i != 0 && i % IN_CAUSE_LIMIT == 0) {
                    transformedInExpression
                            .deleteCharAt(transformedInExpression.length() - 1)
                            .append(") or ")
                            .append(fieldName)
                            .append(" in (");
                }
                transformedInExpression.append("?,");
            }
            transformedInExpression
                    .deleteCharAt(transformedInExpression.length() - 1)
                    .append("))))");
            sql = sql.replaceFirst(Pattern.quote(inExpression), transformedInExpression.toString());
        }
        return sql;
    }

    private String rewriteSqlLike(String sql) {
        Matcher matcher = patternLike.matcher(sql);
        while (matcher.find()) {
            String likeExpression = matcher.group();
            String transformLikeExpression = likeExpression + " COLLATE BINARY_AI ESCAPE '/' ";
            sql = sql.replaceFirst(Pattern.quote(likeExpression), transformLikeExpression);
        }
        return sql;
    }
}
