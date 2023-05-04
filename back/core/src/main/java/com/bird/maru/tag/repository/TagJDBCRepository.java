package com.bird.maru.tag.repository;

import com.bird.maru.spot.controller.dto.TagRequestDto;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagJDBCRepository {

    private final JdbcTemplate jdbcTemplate;

    public void bulkInsertTags(List<TagRequestDto> tags) {
        jdbcTemplate.batchUpdate("INSERT INTO tags (name) VALUES (?)",
                                 new BatchPreparedStatementSetter() {
                                     @Override
                                     public void setValues(PreparedStatement ps, int i) throws SQLException {
                                         ps.setString(1, tags.get(i).getName());
                                     }

                                     @Override
                                     public int getBatchSize() {
                                         return tags.size();
                                     }
                                 });
    }

    public void bulkInsertSpotHasTags(Long spotId, List<Long> tagIds) {
        jdbcTemplate.batchUpdate("INSERT INTO spot_has_tag (spot_id, tag_id)"
                                         + "VALUES (?, ?)",
                                 new BatchPreparedStatementSetter() {

                                     @Override
                                     public void setValues(PreparedStatement ps, int i) throws SQLException {
                                         ps.setLong(1, spotId);
                                         ps.setLong(2, tagIds.get(i));
                                     }

                                     @Override
                                     public int getBatchSize() {
                                         return tagIds.size();
                                     }
                                 });
    }

}
