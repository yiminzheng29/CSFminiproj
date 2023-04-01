package sg.edu.nus.pafworkshop21.workshop21.repositories;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.pafworkshop21.workshop21.models.Comment;
import static sg.edu.nus.pafworkshop21.workshop21.repositories.Queries.*;


@Repository
public class CommentRepository {
    
    @Autowired
    private JdbcTemplate template;

    public void saveComment(Comment c) {
        String commentId = UUID.randomUUID().toString().substring(0,8);
        c.setCommentId(commentId);

        template.update(SQL_SAVE_COMMENT, commentId, c.getFirstname(), c.getLastname(),
        c.getComment(), c.getUsername(),c.getNewsId(), new Timestamp(c.getPublishedAt().toDateTime().getMillis()));
    }

    public List<Comment> getComments() {
        List<Comment> allComments = new LinkedList<>();
        SqlRowSet rs = template.queryForRowSet(SQL_GET_ALL_COMMENTS);

        while (rs.next())
            allComments.add(Comment.create(rs));

        return allComments;
    }
}
