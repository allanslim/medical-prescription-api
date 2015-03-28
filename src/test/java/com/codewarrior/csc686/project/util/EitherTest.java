package com.codewarrior.csc686.project.util;

import org.junit.Test;

import java.util.Random;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class EitherTest {


    private enum ERROR_CODE {  TITLE_NOT_FOUND, TRANASCTION_NOT_FOUND }


    private Either<ERROR_CODE, String> getTitleIdWithError() {

        return Either.left(ERROR_CODE.TITLE_NOT_FOUND);
    }

    private Either<ERROR_CODE, String> getTransactionIdByTitleIdWithError(String titleId) {
        return Either.left(ERROR_CODE.TRANASCTION_NOT_FOUND);
    }

    private Either<ERROR_CODE, String> getTitleId() {
        return Either.right("ABC_TITLE_ID");
    }

    private Either<ERROR_CODE, String> getTransactionIdByTitleId(String titleId) {
        return Either.right(titleId + "_XYZ_TRANSACTION_ID");
    }

    private Either< ERROR_CODE, String> getConsumerByTransactionId(String transactionId) {
        return Either.right(transactionId + "_YZZ_CONSUMER_ID");
    }

    private Either<ERROR_CODE, String> getProviderIdByConsumerId( String consumerId) {

        return Either.right(consumerId + "_XYZPROVIDER_ID");

    }

    @Test
    public void testMapRight() {

        Either<ERROR_CODE, String> titleEither = getTitleId().mapRight(titleId -> {
            assertThat(titleId, is("ABC_TITLE_ID"));
            return titleId;
        });

        assertThat(titleEither.isLeft(), is(false));

        Either<ERROR_CODE, String> error_codeStringEither = titleEither.flatMapRight(tid -> getTransactionIdByTitleId(tid).flatMapRight(tnId -> getConsumerByTransactionId(tnId)));

        System.out.println(error_codeStringEither.right());
    }

    @Test
    public void testMapLeft() {

        Either<ERROR_CODE, String> titleWithErrorEither = getTitleIdWithError().mapLeft(err -> {

            assertThat(err, is(ERROR_CODE.TITLE_NOT_FOUND));

            return err;
        });

        assertThat(titleWithErrorEither.left(), is(ERROR_CODE.TITLE_NOT_FOUND));

        Either<ERROR_CODE, String> transactionIdEither = titleWithErrorEither.flatMapLeft(err -> {
            if (err == ERROR_CODE.TITLE_NOT_FOUND) {
                return getTransactionIdByTitleIdWithError("another_title_id");
            }
            return null;
        });

        assertThat(transactionIdEither.left(), is(ERROR_CODE.TRANASCTION_NOT_FOUND));

    }

    @Test
    public void testNestedMapRight() {

        Either<ERROR_CODE, String> titleEither = getTitleId();

        Either<ERROR_CODE, Either<ERROR_CODE, String>> transactionEither = titleEither.mapRight(titleId -> getTransactionIdByTitleId(titleId));

        transactionEither.apply( err -> System.out.print(err.name()),
                txnId -> {
                    String transactionId = txnId.right();
                    assertThat(transactionId, is("ABC_TITLE_ID_XYZ_TRANSACTION_ID"));
                }
        );

    }


    @Test
    public void testMap() {
        new Random().
                ints(20, 0, 2).
                mapToObj(i -> (Either<String, ERROR_CODE>) (i == 0 ? Either.left("ABC_TITLE_ID") : Either.right(ERROR_CODE.TITLE_NOT_FOUND))).
                forEach(result -> result.apply(left -> { assertThat(left, is("ABC_TITLE_ID")); }, right -> {
                    assertThat(right, is(ERROR_CODE.TITLE_NOT_FOUND));
                }));

    }
}