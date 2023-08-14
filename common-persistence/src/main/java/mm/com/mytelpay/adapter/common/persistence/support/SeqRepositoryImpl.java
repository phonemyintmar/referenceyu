package mm.com.mytelpay.adapter.common.persistence.support;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class SeqRepositoryImpl implements SeqRepository {

    @PersistenceContext private EntityManager entityManager;

    @Override
    @Transactional
    public BigInteger nextValue(String seqName) {
        Query createSeqOrExistQuery =
                entityManager.createNativeQuery(
                        String.format(
                                ""
                                        + "DECLARE\n"
                                        + "    v_dummy NUMBER;\n"
                                        + "BEGIN\n"
                                        + "    SELECT\n"
                                        + "        1\n"
                                        + "    INTO v_dummy\n"
                                        + "    FROM\n"
                                        + "        user_sequences\n"
                                        + "    WHERE\n"
                                        + "        sequence_name = '%s';\n"
                                        + "\n"
                                        + "EXCEPTION\n"
                                        + "    WHEN no_data_found THEN\n"
                                        + "        EXECUTE IMMEDIATE 'create sequence %s';\n"
                                        + "END;",
                                seqName, seqName));
        createSeqOrExistQuery.executeUpdate();
        Query query =
                entityManager.createNativeQuery(
                        String.format("SELECT %s.nextVal FROM DUAL", seqName));
        BigDecimal value = (BigDecimal) query.getSingleResult();
        return value.toBigInteger();
    }
}
