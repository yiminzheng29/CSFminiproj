CREATE DATABASE csf;
CREATE USER 'csfuser'@'localhost' IDENTIFIED BY 'Password@123456';
GRANT ALL PRIVILEGES ON *.* TO 'csfuser'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;

-- for user table
create table user (
	# primary key
    username varchar(32) not null,
    password varchar(256) not null,
    firstname varchar(32) not null,
    lastname varchar(32) not null,
    dob date not null,
    email varchar(128) not null,
    phone varchar(16),

    primary key(username)
);

-- for news table
create table news (
	# primary key
    newsId char(6) not null,
    title varchar(256) not null,
    author varchar(256),
    sourceName varchar(256),
    publishedAt datetime, 
    url varchar(300) not null,
    urlImage varchar(300) not null,
    description varchar(500),
    content varchar(1000) not null,

    primary key(newsId)
);

-- for newsRecord table
create table newsRecord (
	newsId char(6) not null,
    username varchar(32) not null
);

create table likesRecord (
	newsId char(6) not null,
    likes int not null,
    
    primary key(newsId)
);

create table comments (
	commentId char(8) not null,
    username varchar(32) not null,
    firstname varchar(32) not null,
    lastname varchar(32) not null,
    comment varchar(1000) not null,
    newsId char(6) not null,
    publishedAt datetime,
    
    primary key(commentId)
);

create table notifications (
	username varchar(32) not null,
    token varchar(1000) not null
);