package com.bird.maru.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessResourceFailureException;

@SpringBootTest
class NamedLockExecutorTest {

    @Autowired
    private NamedLockExecutor namedLockExecutor;

    @Test
    @DisplayName("락 획득/해제 성공 테스트")
    void lockSuccessTest() {
        assertThat(
                namedLockExecutor.executeWithLock(
                        "test",
                        10,
                        () -> true
                )
        ).isTrue();
    }

    @Test
    @DisplayName("락 획득/해제 실패 테스트")
    void lockFailTest() {
        String lockName = "test";
        int timeoutSeconds = 10;

        assertThatThrownBy(
                () -> namedLockExecutor.executeWithLock(
                        lockName,
                        timeoutSeconds,
                        () -> namedLockExecutor.executeWithLock(lockName, timeoutSeconds, () -> true)
                )
        ).isInstanceOf(DataAccessResourceFailureException.class);
    }

}