package mm.com.mytelpay.adapter.common.validator;

public interface ValidateConstraint {

    final class LENGTH {
        public static final int CODE_MAX_LENGTH = 50;
        public static final int NAME_MAX_LENGTH = 100;
        public static final int TITLE_MAX_LENGTH = 200;
        public static final int DESC_MAX_LENGTH = 1000;
        public static final int NOTE_MAX_LENGTH = 1000;
        public static final int ENUM_MAX_LENGTH = 20;
        public static final int ID_MIN_LENGTH = 1;
        public static final int ID_MAX_LENGTH = 36;
        public static final int PASSWORD_MIN_LENGTH = 3;
        public static final int PASSWORD_MAX_LENGTH = 20;
        public static final int CONTENT_MAX_LENGTH = 2000;
        public static final int VALUE_MAX_LENGTH = 200;
        public static final int PHONE_MAX_LENGTH = 20;
        public static final int EMAIL_MAX_LENGTH = 50;
        public static final int METHOD_MAX_LENGTH = 10;
        public static final int URL_MAX_LENGTH = 2000;
        public static final int HOSTNAME_MAX_LENGTH = 100;
        public static final int SCOPE_MAX_LENGTH = 1000;
        private LENGTH() {
            // default constructor
        }
    }

    final class FORMAT {
        public static final String PHONE_NUMBER_PATTERN =
                "^(\\+[0-9]+[\\- \\.]*)?(\\([0-9]+\\)[\\- \\.]*)?([0-9][0-9\\- \\.]+[0-9])$";
        public static final String EMAIL_PATTERN =
                "^(\\s){0,}[a-zA-Z][a-zA-Z0-9-_\\.]{1,50}@[a-zA-Z0-9_-]{2,}(\\.[a-zA-Z0-9]{2,4}){1,2}(\\s){0,}$";
        public static final String CODE_PATTERN = "^[A-Za-z0-9_.]{4,50}$";
        private FORMAT() {
            // default constructor
        }
    }
}
