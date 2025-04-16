create table user_roles
(
    id   int                             not null,
    name varchar(25) default 'ROLE_USER' not null
);

create table users
(
    id         bigint generated always as identity
        constraint users_pk
            primary key,
    username   varchar(25)                            not null
        constraint users_username_uq
            unique,
    nickname   varchar(25)                            not null,
    password   varchar(100)                           not null,
    role       varchar(25)                            not null,
    created_at timestamp with time zone default now() not null,
    updated_at timestamp with time zone
);

create table article_categories
(
    id            integer generated always as identity
        constraint article_categories_pk_2
            primary key,
    name          varchar(10)      not null
        constraint article_categories_pk
            unique,
    article_count bigint default 0 not null
);

create table articles
(
    id                  bigint generated always as identity
        constraint articles_pk
            primary key,
    title               text                                   not null,
    content             text                                   not null,
    created_at          timestamp with time zone default now() not null,
    updated_at          timestamp with time zone,
    comment_count       integer                  default 0     not null,
    vote_count          integer                  default 0     not null,
    user_id             bigint
        constraint articles_users_id_fk
            references users
            on delete set null,
    article_category_id integer                  default 1
        constraint articles__id_fk
            references article_categories
            on delete set null
);

create index articles_title_index
    on articles (title);

create index articles_user_id_index
    on articles (user_id);

create index articles_vote_count_index
    on articles (vote_count desc);

create index articles_vote_count_index
    on articles (comment_count desc);

create table article_votes
(
    article_id bigint                                 not null
        constraint article_voters_articles_id_fk
            references articles
            on delete cascade,
    user_id    bigint                                 not null
        constraint article_voters_users_id_fk
            references users
            on delete set null,
    active     boolean                  default true  not null,
    constraint article_voters_pk
        primary key (user_id, article_id),
    created_at timestamp with time zone default now() not null
);

create table comments
(
    id         bigint generated always as identity
        constraint comments_pk
            primary key,
    content    text                                   not null,
    created_at timestamp with time zone default now() not null,
    updated_at timestamp with time zone,
    user_id    bigint
        constraint comments_users_id_fk
            references users
            on delete set null,
    article_id bigint
        constraint comments_articles_id_fk
            references articles
            on delete set null,
    parent_id  bigint
        constraint comments_comments_id_fk
            references comments
            on delete set null,
    depth      integer                  default 0     not null
);
