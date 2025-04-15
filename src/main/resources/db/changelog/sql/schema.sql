-- auto-generated definition
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

alter table users
    owner to postgres;

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

alter table article_categories
    owner to postgres;


-- auto-generated definition
create table articles
(
    id                  bigint generated always as identity
        constraint articles_pk
            primary key,
    title               text                                   not null,
    content             text                                   not null,
    created_at          timestamp with time zone default now() not null,
    updated_at          timestamp with time zone,
    user_id             bigint
        constraint articles_users_id_fk
            references users
            on delete set null,
    comment_count       integer                  default 0     not null,
    vote_count          integer                  default 0     not null,
    article_category_id integer                  default 1
        constraint articles__id_fk
            references article_categories
            on delete set null
);

alter table articles
    owner to postgres;

create index articles_title_index
    on articles (title);

create index articles_user_id_index
    on articles (user_id);

-- auto-generated definition
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
    created_at timestamp with time zone default now() not null,
    active     boolean                  default true  not null,
    constraint article_voters_pk
        primary key (user_id, article_id)
);

alter table article_votes
    owner to postgres;

-- auto-generated definition
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

alter table comments
    owner to postgres;
