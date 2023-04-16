package csf.miniproject.server.csfminiproject.repositories;

public class Queries {
    
    // public static final String SQL_CREATE_USER = """
    //     insert into user(username, password, firstname, lastname, dob, email, phone) values(?,sha1(?),?,?,?,?,?)
    //         """;

    public static final String SQL_CREATE_USER = """
        insert into user(username, password, firstname, lastname, email, profileImage, profileImageUrl) values(?,sha1(?),?,?,?,?,?)
            """;
        

    public static final String SQL_AUTHENTICATE_USER = """
        select count(*) > 0 as auth_state from user where 
        username = ? and password = sha1(?)
            """;

    // public static final String SQL_AUTHENTICATE_USER = """
    //     select count(*) > 0 as auth_state from user where 
    //     username = ?
    //         """;

    public static final String SQL_AUTHENTICATE_USER_0 = """
        select * from user where username = ? and password = sha1(?)
        """;


    public static final String SQL_SEARCH_USER = """
        select * from user where username = ?
            """;

    public static final String SQL_GET_USER = """
        select * from user where username = ? and password = sha1(?)
        """;
        
    public static final String SQL_UPDATE_USER = """
        update user set password = sha1(?), firstname = ?, lastname = ?, email = ?, profileImage =?, profileImageUrl = ? where
        username = ?""";

    public static final String SQL_DELETE_USER = """
        delete from user where username = ?
            """;

    public static final String SQL_SAVE_ISSUE="""
        insert into issues(issueNo, title, description, priority, status, timestamp, user)
            """;

    public static final String SQL_SAVE_NEWS = """
        insert into news(newsId, title, author, sourceName, publishedAt, url, urlImage, description, content)
        values (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public static final String SQL_SAVE_NEWS_RECORD = """
        insert into newsRecord(newsId, username)
        values (?, ?)
            """;

    // public static final String SQL_CHECK_IF_USER_LIKED_NEWS = """
    //     select * from newsRecord where newsId = ? and username = ?
    //         """;

    public static final String SQL_SAVE_LIKES_RECORD = """
        insert into likesRecord(newsId, likes)
        values (?, ?)
        """;

    public static final String SQL_UPDATE_LIKES_RECORD = """
        update likesRecord set likes = ? where newsId = ?
            """;

    public static final String SQL_GET_LIKES = """
        select * from likesRecord where newsId = ?
            """;

    public static final String SQL_CHECK_IF_NEWS_EXIST = """
        select * from news where title = ? 
            """;

    public static final String SQL_CHECK_POSTS_LIKED_BY_USER = """
        select * from newsRecord where username = ?
            """;

    public static final String SQL_DELETE_NEWS_RECORD = """
        delete from newsRecord where username = ? and newsId = ?
            """;

    public static final String SQL_GET_USER_NEWS = """
            select newsId, title, author, sourceName, DATE_FORMAT(publishedAt, \"%d/%m/%Y\") as publishedAt,
            url, urlImage, description, content from news where newsId = ?
            """;

    public static final String SQL_SAVE_COMMENT = """
            insert into comments(commentId, firstname, lastname, comment, username, newsId, publishedAt)
            values(?,?,?,?,?,?,?)
            """;

    public static final String SQL_GET_ALL_COMMENTS = """
        select * from comments;
            """;

    public static final String SQL_INSERT_TOKEN = """
        insert into notifications(username, token) values(?,?)""";

    public static final String SQL_GET_TOKEN = """
        select token from notifications where username=?""";
        
}
