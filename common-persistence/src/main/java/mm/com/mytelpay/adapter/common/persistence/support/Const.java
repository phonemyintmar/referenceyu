package mm.com.mytelpay.adapter.common.persistence.support;

public enum Const {

    PAYMENT_REQUEST_ID("SK", "PAYMENT_REQUEST_ID_SEQ");

    private final String prefix;

    // must upper case
    private final String seqName;

    Const(String prefix, String seqName) {
        this.prefix = prefix;
        this.seqName = seqName.toUpperCase();
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSeqName() {
        return seqName;
    }

}
