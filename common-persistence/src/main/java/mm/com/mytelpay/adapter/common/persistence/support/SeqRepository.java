package mm.com.mytelpay.adapter.common.persistence.support;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

public interface SeqRepository {

    BigInteger nextValue(String seqName);

    @Transactional
    default String nextValue(String prefix, String seqName) {
        return prefix + nextValue(seqName);
    }

    @Transactional
    default String generatePaymentRequestId() {
        return nextValue(
                Const.PAYMENT_REQUEST_ID.getPrefix(), Const.PAYMENT_REQUEST_ID.getSeqName());
    }
}
