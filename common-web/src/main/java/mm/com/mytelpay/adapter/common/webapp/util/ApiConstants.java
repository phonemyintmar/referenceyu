package mm.com.mytelpay.adapter.common.webapp.util;

public interface ApiConstants {
    public interface HttpHeaders {
        public static final String LINK_FORMAT = "<{0}>; rel=\"{1}\"";

        public static final String X_ACTION_MESSAGE = "X-Action-Message";

        public static final String X_ACTION_MESSAGE_KEY = "X-Action-Message-Key";

        public static final String X_ACTION_PARAMS = "X-Action-Params";

        public static final String X_FORWARDED_FOR = "X-Forwarded-For";

        public static final String X_SIGNATURE = "X-SIGNATURE";

        public static final String X_TOTAL_COUNT = "X-Total-Count";

        public static final String X_TRANSACTION_ID = "X-TRANSACTION-ID";

        public static final String CLIENT_MESSAGE_ID = "clientMessageId";

        public static final String TRANSACTION_ID = "transactionId";
    }

    public interface Pagination {
        public static final String FIRST = "first";

        public static final String LAST = "last";

        public static final String NEXT = "next";

        public static final String PAGE = "page";

        public static final String PREV = "prev";

        public static final String SIZE = "size";
    }

    public interface Header {
        public static final String AUTHORIZATION_HEADER = "Authorization";

        public static final String BEARER_START = "Bearer ";

        public static final String BASIC_START = "Basic ";

        public static final String PRIVILEGES = "privileges";

        public static final String HASHKEY = "hash-key";

        public static final String LOCALE = "locale";

        public static final String REFRESH_TOKEN = "refresh-token";

        public static final String USER = "user";
    }
}
