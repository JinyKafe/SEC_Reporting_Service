/* JOB */
create table JOB
(
    id char(36) not null
        constraint REPORTING_JOB_pk
            primary key nonclustered,
    state varchar(10) not null,
    error_message nvarchar(255),
    execution_date date not null,
    created_time timestamp not null
)
go

create index REPORTING_JOB_reporting_date_index
    on JOB (execution_date desc)
go

create index REPORTING_JOB_state_index
    on JOB (state)
go


/* REPORT */
create table REPORT
(
    id char(36) not null,
    job_id char(36) not null
        constraint REPORT_REPORTING_JOB_id_fk
            references JOB,
    type varchar(10) not null,
    filename nvarchar(255) not null,
    state varchar(10) not null,
    error_message nvarchar(255),
    xbrl xml not null,
    timestamp null,
    constraint REPORT_pk
        primary key nonclustered (id, job_id, type)
)
go

create index REPORT_filename_index
    on REPORT (filename)
go

create index REPORT_state_index
    on REPORT (state)
go

/* RELEASE (for zip archives) */
create table RELEASE
(
    id char(36) not null,
    job_id char(36) not null
        constraint RELEASE_REPORTING_JOB_id_fk
            references JOB,
    archive_file_name nvarchar(255) not null,
    archive_file varbinary not null,
    release_date date not null,
    created_time timestamp not null,
    state varchar(10) not null,
    constraint RELEASE_pk
        primary key nonclustered (id, job_id)
)
go

create index RELEASE_release_date_index
    on RELEASE (release_date desc)
go

create index RELEASE_state_index
    on RELEASE (state)
go

/* USERS */
create table USERS
(
    email varchar(36) not null
        constraint USER_pk
            primary key nonclustered,
    first_name varchar(36) not null,
    last_name varchar(36) not null,
    password varchar(255) not null
)
go

create unique index USER_email_uindex
    on USERS (email)
go

create unique index USER_password_uindex
    on USERS (password)
go

/* JOB_HISTORY */
create table JOB_HISTORY
(
    job_id char(36) not null
        constraint JOB_HISTORY_REPORTING_JOB_id_fk
            references JOB,
    originator varchar(36) not null
        constraint JOB_HISTORY_USERS_email_fk
            references USERS,
    action varchar(10) not null,
    state varchar(10) not null,
    execution_time datetime2 not null,
    constraint JOB_HISTORY_pk
        primary key nonclustered (job_id, originator, action)
)
go
