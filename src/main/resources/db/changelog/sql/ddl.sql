create table user_roles
(
    id           integer generated always as identity
        constraint user_roles_pk
            primary key,
    name         varchar(25) default 'ROLE_USER'::character varying not null,
    display_name varchar(25) default '사용자'::character varying       not null
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
    created_at timestamp with time zone default now() not null,
    updated_at timestamp with time zone,
    role_id    integer                  default 1
        constraint users_user_roles_id_fk
            references user_roles
            on delete set null
);

create table user_images
(
    id                 bigint generated always as identity
        constraint user_images_pk
            primary key,
    public_id          varchar(50)                            not null,
    original_file_name varchar(500)                           not null,
    url                varchar(500)                           not null,
    created_at         timestamp with time zone default now() not null,
    updated_at         timestamp with time zone default now(),
    user_id            bigint                                 not null
        constraint users_images_users_id_fk
            references users
            on delete set null
);

create table article_categories
(
    id            integer generated always as identity
        constraint article_categories_pk
            primary key,
    name          varchar(10)      not null,
    article_count bigint default 0 not null
);

create table articles
(
    id            bigint generated always as identity
        constraint articles_pk
            primary key,
    title         text                                   not null,
    content       text                                   not null,
    created_at    timestamp with time zone default now() not null,
    updated_at    timestamp with time zone,
    comment_count integer                  default 0     not null,
    vote_count    integer                  default 0     not null,
    user_id       bigint
        constraint articles_users_id_fk
            references users
            on delete set null,
    category_id   integer                  default 1
        constraint articles_article_categories_id_fk
            references article_categories
            on delete set default
);

create index articles_title_index
    on articles (title);

create index articles_user_id_index
    on articles (user_id);

create index articles_vote_count_index
    on articles (vote_count desc);

create index articles_comment_count_index
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
    created_at timestamp with time zone default now() not null,
    active     boolean                  default true  not null,
    constraint article_voters_pk
        primary key (user_id, article_id)
);

create table comments
(
    id                bigint generated always as identity
        constraint comments_pk
            primary key,
    content           text                                   not null,
    depth             integer                  default 0     not null,
    created_at        timestamp with time zone default now() not null,
    updated_at        timestamp with time zone,
    user_id           bigint
        constraint comments_users_id_fk
            references users
            on delete set null,
    article_id        bigint
        constraint comments_articles_id_fk
            references articles
            on delete set null,
    parent_comment_id bigint
        constraint comments_comments_id_fk
            references comments
            on delete set null
);
