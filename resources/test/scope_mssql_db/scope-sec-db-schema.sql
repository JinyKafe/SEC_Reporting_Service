/* prune DB before seeding new data */
-- data for INSTRUMENT XBRL reports
DROP TABLE IF EXISTS INSTRUMENT_RATING_ACTION;
DROP TABLE IF EXISTS INSTRUMENT_IDENTIFIER;
DROP TABLE IF EXISTS INSTRUMENT;
DROP TABLE IF EXISTS ISSUER_IDENTIFIER;
DROP TABLE IF EXISTS ISSUER;
-- data for OBLIGOR XBRL reports
DROP TABLE IF EXISTS OBLIGOR_RATING_ACTION;
DROP TABLE IF EXISTS OBLIGOR_IDENTIFIER;
DROP TABLE IF EXISTS OBLIGOR;
-- rating agency (Scope in our case) metadata for OBLIGOR and INSTRUMENT XBRL reports
DROP TABLE IF EXISTS CREDIT_RATING_AGENCY_INFO;
-- Scope internal constants (these tables serve for data validation only)
DROP TABLE IF EXISTS OTHER_RATING_ACTION_TYPE;
DROP TABLE IF EXISTS WATCH_REVIEW;
DROP TABLE IF EXISTS DEBT_CATEGORY;
DROP TABLE IF EXISTS OUTLOOK_TREND;
DROP TABLE IF EXISTS INDUSTRY_GROUP;
DROP TABLE IF EXISTS RATING_TYPE;
DROP TABLE IF EXISTS RATING_TYPE_TERM;
-- SEC XBL enumerations (these tables serve for data validation only)
DROP TABLE IF EXISTS SEC_INSTRUMENT_IDENTIFIER_SCHEME;
DROP TABLE IF EXISTS SEC_ISSUER_OR_OBLIGOR_IDENTIFIER_SCHEME;
DROP TABLE IF EXISTS SEC_RATING_ACTION_TYPE;
DROP TABLE IF EXISTS SEC_RATED_OBJECT_TYPE;
DROP TABLE IF EXISTS SEC_CATEGORY;

/**********************************************************************
 Scope internal constants (these tables serve for data validation only)
***********************************************************************/
create table sec_category
(
    code varchar(100) not null,
    constraint PK_sec_category
        primary key (code)
);

create table sec_rated_object_type
(
    code varchar(30) not null,
    constraint PK_sec_rated_object_type
        primary key (code)
);

create table sec_rating_action_type
(
    code          nchar(2) not null,
    description   varchar(255),
    classified_by varchar(255),
    constraint PK_sec_rating_action_type
        primary key (code)
);

create table sec_instrument_identifier_scheme
(
    code        varchar(30) not null,
    description varchar(255),
    constraint PK_sec_instrument_identifier_scheme
        primary key (code)
);

create table sec_issuer_or_obligor_identifier_scheme
(
    code        varchar(30) not null,
    description varchar(255),
    constraint PK_sec_issuer_or_obligor_identifier_scheme
        primary key (code)
);

/**********************************************************************
 Scope internal constants (these tables serve for data validation only)
***********************************************************************/

create table rating_type
(
    code varchar(100) not null,
    constraint PK_rating_type
        primary key (code)
);

create table rating_type_term
(
    code varchar(100) not null,
    constraint PK_rating_type_term
        primary key (code)
);

create table industry_group
(
    code          varchar(100) not null,
    classified_by varchar(100),
    constraint PK_industry_group
        primary key (code)
);

create table outlook_trend
(
    code          varchar(100) not null,
    description   varchar(255),
    classified_by varchar(255),
    constraint PK_outlook_trend
        primary key (code)
);

create table debt_category
(
    code          varchar(100) not null,
    classified_by varchar(100),
    constraint PK_debt_category
        primary key (code)
);

create table watch_review
(
    code          varchar(100) not null,
    description   varchar(255),
    classified_by varchar(255),
    constraint PK_watch_review
        primary key (code)
);

create table other_rating_action_type
(
    code          varchar(100) not null,
    description   varchar(255),
    classified_by varchar(255),
    constraint PK_other_rating_action_type
        primary key (code)
);

-- *********************************************
-- *********************************************
-- * REPORT DATA - tables to produce XBRL file *
-- *********************************************
-- *********************************************

create table credit_rating_agency_info
(
    id           nchar(36) not null,
    name         varchar(255),
    lei          nchar(20),
    created_date timestamp,
    constraint PK_credit_rating_agency_info
        primary key (id)
);

-- *******************************
-- * OBLIGOR XBRL report data    *
-- *******************************

create table obligor
(
    id                           int not null,
    name                         varchar(255),
    sec_category                 varchar(100),
    industry_group               varchar(100),
    lei                          nchar(20),
    cik                          nchar(10),
    credit_rating_agency_info_id nchar(36),
    created_date                 timestamp,
    constraint PK_obligor
        primary key (id),
    constraint FK_obligor_credit_rating_agency_info
        foreign key (credit_rating_agency_info_id) references credit_rating_agency_info,
    constraint FK_obligor_industry_group
        foreign key (industry_group) references industry_group,
    constraint FK_obligor_sec_category
        foreign key (sec_category) references sec_category
);

create table obligor_identifier
(
    obligor_id        int          not null,
    identifier        varchar(100) not null,
    identifier_scheme varchar(30)  not null,
    constraint PK_obligor_identifier
        primary key (obligor_id, identifier_scheme),
    constraint FK_obligor_identifier_obligor
        foreign key (obligor_id) references obligor,
    constraint FK_obligor_identifier_sec_issuer_or_obligor_identifier_scheme
        foreign key (identifier_scheme) references sec_issuer_or_obligor_identifier_scheme
);

create table obligor_rating_action
(
    obligor_id               int          not null,
    rating_action_id         varchar(100) not null,
    rating_type              varchar(255),
    rating_type_term         varchar(100),
    rating_sub_type_scheme   varchar(255),
    version_no               int,
    rating_action_type       nchar(2),
    other_rating_action_type varchar(100),
    rating                   varchar(50),
    outlook_trend            varchar(100),
    watch_review             varchar(100),
    rating_date              date,
    issuer_paid              bit,
    created_date             timestamp,
    constraint PK_obligor_rating_action
        primary key (obligor_id, rating_action_id),
    constraint FK_obligor_rating_action_obligor
        foreign key (obligor_id) references obligor,
    constraint FK_obligor_rating_action_other_rating_action_type
        foreign key (other_rating_action_type) references other_rating_action_type,
    constraint FK_obligor_rating_action_outlook_trend
        foreign key (outlook_trend) references outlook_trend,
    constraint FK_obligor_rating_action_sec_rating_action_type
        foreign key (rating_action_type) references sec_rating_action_type,
    constraint FK_obligor_rating_action_watch_review
        foreign key (watch_review) references watch_review
);

-- *******************************
-- * INSTRUMENT XBRL report data *
-- *******************************

create table issuer
(
    id                           int not null,
    name                         varchar(255),
    sec_category                 varchar(100),
    industry_group               varchar(100),
    lei                          nchar(20),
    cik                          nchar(10),
    credit_rating_agency_info_id nchar(36),
    created_date                 timestamp,
    constraint PK_issuer
        primary key (id),
    constraint FK_issuer_credit_rating_agency_info
        foreign key (credit_rating_agency_info_id) references credit_rating_agency_info,
    constraint FK_issuer_industry_group
        foreign key (industry_group) references industry_group,
    constraint FK_issuer_sec_category
        foreign key (sec_category) references sec_category
);

create table issuer_identifier
(
    issuer_id         int          not null,
    identifier        varchar(100) not null,
    identifier_scheme varchar(30)  not null,
    constraint PK_IssuerIdentifier
        primary key (issuer_id, identifier_scheme),
    constraint FK_issuer_identifier_issuer
        foreign key (issuer_id) references issuer,
    constraint FK_issuer_identifier_sec_issuer_or_obligor_identifier_scheme
        foreign key (identifier_scheme) references sec_issuer_or_obligor_identifier_scheme
);

create table instrument
(
    id                      int      not null,
    issuer_id               int      not null,
    rated_object_type       varchar(30),
    name                    varchar(255),
    cusip                   nchar(9),
    coupon_rate             decimal(10, 4),
    issuance_date           date,
    maturity_date           date,
    par_value               decimal(30),
    par_value_currency_code nchar(3) not null,
    debt_category           varchar(100),
    created_date            timestamp,
    constraint PK_instrument
        primary key (id),
    constraint FK_instrument_issuer
        foreign key (issuer_id) references issuer,
    constraint FK_instrument_sec_rated_object_type
        foreign key (rated_object_type) references sec_rated_object_type
);

create table instrument_identifier
(
    instrument_id     int          not null,
    identifier        varchar(100) not null,
    identifier_scheme varchar(30)  not null,
    constraint PK_instrument_identifier
        primary key (instrument_id, identifier_scheme),
    constraint FK_instrument_identifier_instrument
        foreign key (instrument_id) references instrument,
    constraint FK_instrument_identifier_sec_instrument_identifier_scheme
        foreign key (identifier_scheme) references sec_instrument_identifier_scheme
);

create table instrument_rating_action
(
    instrument_id            int          not null,
    rating_action_id         varchar(100) not null,
    rating_type              varchar(255),
    rating_type_term         varchar(100),
    rating_sub_type_scheme   varchar(255),
    version_no               int,
    rating_action_type       nchar(2),
    other_rating_action_type varchar(100),
    rating                   varchar(50),
    outlook_trend            varchar(100),
    watch_review             varchar(100),
    rating_date              date,
    issuer_paid              bit,
    created_date             timestamp,
    constraint PK_instrument_rating_action
        primary key (instrument_id, rating_action_id),
    constraint FK_instrument_rating_action_instrument
        foreign key (instrument_id) references instrument,
    constraint FK_instrument_rating_action_other_rating_action_type
        foreign key (other_rating_action_type) references other_rating_action_type,
    constraint FK_instrument_rating_action_outlook_trend
        foreign key (outlook_trend) references outlook_trend,
    constraint FK_instrument_rating_action_sec_rating_action_type
        foreign key (rating_action_type) references sec_rating_action_type,
    constraint FK_instrument_rating_action_watch_review
        foreign key (watch_review) references watch_review
);
