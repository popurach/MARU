package com.bird.maru.common.util;

import com.bird.maru.common.exception.LockException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessResourceFailureException;

@Slf4j
public class NamedLockExecutor {

    private static final String GET_LOCK = "SELECT GET_LOCK(?, ?)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(?)";

    private final DataSource dataSource;

    public NamedLockExecutor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Named Lock을 획득하여 로직을 수행한 후 Lock을 해제합니다.
     *
     * @param lockName       Lock의 이름
     * @param timeoutSeconds Lock을 획득하기 위해 대기하는 최대 시간
     * @param supplier       수행하려는 비즈니스 로직
     * @return 로직 수행 후 반환
     */
    public <T> T executeWithLock(String lockName, int timeoutSeconds, Supplier<T> supplier) {
        try (Connection connection = dataSource.getConnection()) {
            try {
                getLock(connection, lockName, timeoutSeconds);
                return supplier.get();
            } finally {
                releaseLock(connection, lockName);
            }
        } catch (SQLException e) {
            throw new DataAccessResourceFailureException(e.getMessage(), e);
        }
    }

    /**
     * Named Lock을 획득하여 로직을 수행한 후 Lock을 해제합니다.
     *
     * @param lockName       Lock의 이름
     * @param timeoutSeconds Lock을 획득하기 위해 대기하는 최대 시간
     * @param runnable       수행하려는 비즈니스 로직
     */
    public void executeWithLock(String lockName, int timeoutSeconds, Runnable runnable) {
        try (Connection connection = dataSource.getConnection()) {
            try {
                getLock(connection, lockName, timeoutSeconds);
                runnable.run();
            } finally {
                releaseLock(connection, lockName);
            }
        } catch (SQLException e) {
            throw new DataAccessResourceFailureException(e.getMessage(), e);
        }
    }

    private void getLock(Connection connection, String lockName, int timeoutSeconds) throws SQLException {
        log.debug("Get Lock :: lockName = [{}], timeoutSeconds = [{}]", lockName, timeoutSeconds);
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_LOCK)) {
            preparedStatement.setString(1, lockName);
            preparedStatement.setInt(2, timeoutSeconds);
            checkResultSet("GET_LOCK", preparedStatement, lockName);
        }
    }

    private void releaseLock(Connection connection, String lockName) throws SQLException {
        log.debug("Release Lock :: lockName = [{}]", lockName);
        try (PreparedStatement preparedStatement = connection.prepareStatement(RELEASE_LOCK)) {
            preparedStatement.setString(1, lockName);
            checkResultSet("RELEASE_LOCK", preparedStatement, lockName);
        }
    }

    private void checkResultSet(String type, PreparedStatement preparedStatement, String lockName) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (!resultSet.next()) {
                log.error("LOCK 쿼리 결과 값이 없습니다 :: type = [{}], lockName = [{}]", type, lockName);
                throw new LockException();
            }

            if (resultSet.getInt(1) != 1) {
                log.error("LOCK 쿼리 결과 값이 1이 아닙니다 :: type = [{}], lockName = [{}]", type, lockName);
                throw new LockException();
            }
        }
    }

}
