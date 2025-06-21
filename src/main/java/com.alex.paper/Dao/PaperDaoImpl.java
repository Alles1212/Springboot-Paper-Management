package com.alex.paper.Dao;

import com.alex.paper.Model.Paper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class PaperDaoImpl implements PaperDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper 來轉換 ResultSet 為 Paper 物件
    private static final class PaperRowMapper implements RowMapper<Paper> {
        @Override
        public Paper mapRow(ResultSet rs, int rowNum) throws SQLException {
            Paper paper = new Paper();
            paper.setId(rs.getLong("id"));
            paper.setTitle(rs.getString("title"));
            paper.setAuthor(rs.getString("author"));
            paper.setJournal(rs.getString("journal"));
            paper.setYear(rs.getObject("year") != null ? rs.getInt("year") : null);
            paper.setAbstractText(rs.getString("abstractText"));
            return paper;
        }
    }

    @Override
    public List<Paper> findAll() {
        String sql = "SELECT * FROM paper";
        return jdbcTemplate.query(sql, new PaperRowMapper());
    }

    @Override
    public Paper findById(Long id) {
        String sql = "SELECT * FROM paper WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new PaperRowMapper());
    }

    @Override
    public int save(Paper paper) {
        String sql = "INSERT INTO paper (title, author, journal, year, abstractText) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, paper.getTitle(), paper.getAuthor(), paper.getJournal(), paper.getYear(), paper.getAbstractText());
    }

    @Override
    public int update(Paper paper) {
        String sql = "UPDATE paper SET title = ?, author = ?, journal = ?, year = ?, abstractText = ? WHERE id = ?";
        return jdbcTemplate.update(sql, paper.getTitle(), paper.getAuthor(), paper.getJournal(), paper.getYear(), paper.getAbstractText(), paper.getId());
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM paper WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
