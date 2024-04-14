--
-- PostgreSQL database dump
--

-- Dumped from database version 14.8 (Ubuntu 14.8-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 15.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

-- Role: postgres

CREATE ROLE postgres WITH
    LOGIN
    SUPERUSER
    INHERIT
    CREATEDB
    CREATEROLE
    REPLICATION
    ENCRYPTED PASSWORD 'SCRAM-SHA-256$4096:Jv5Go78HF5sAuo+4TZVr7g==$Z3cK+C81UOTEFRLxr4gLCobgk41wKaLbHbtkOy47IOQ=:+K/iioNuLswz7iDWqe6bmBC60kMFg87wShE5RQeaEEU=';

-- Role: quote

CREATE ROLE quote WITH
    LOGIN
    NOSUPERUSER
    INHERIT
    NOCREATEDB
    NOCREATEROLE
    NOREPLICATION
    ENCRYPTED PASSWORD 'SCRAM-SHA-256$4096:SzY0QBIJIy8MoCXotxK2cw==$+hvlfBw85fSYQZA/RJEBnVyghgMlQCfSQZ20RN3y1nM=:xeMxvAxZvmXZozfnc4eSMO2ULFLukG15mvISHOqhbQk=';


--
-- Name: quote; Type: DATABASE; Schema: -; Owner: quote
--

CREATE DATABASE "quote" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc;


ALTER DATABASE "quote" OWNER TO "quote";

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: quotes; Type: SCHEMA; Schema: -; Owner: quote
--

CREATE SCHEMA quotes;


ALTER SCHEMA quotes OWNER TO "quote";

--
-- Name: users; Type: SCHEMA; Schema: -; Owner: quote
--

CREATE SCHEMA users;


ALTER SCHEMA users OWNER TO "quote";

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: group_quotes; Type: TABLE; Schema: quotes; Owner: quote
--

CREATE TABLE quotes.group_quotes (
                                     group_id integer NOT NULL,
                                     quote_id integer NOT NULL
);


ALTER TABLE quotes.group_quotes OWNER TO "quote";

--
-- Name: group_users; Type: TABLE; Schema: quotes; Owner: quote
--

CREATE TABLE quotes.group_users (
                                    group_id integer NOT NULL,
                                    user_id integer NOT NULL
);


ALTER TABLE quotes.group_users OWNER TO "quote";

--
-- Name: groups; Type: TABLE; Schema: quotes; Owner: quote
--

CREATE TABLE quotes.groups (
                               id integer NOT NULL,
                               name character varying(32) NOT NULL,
                               display_name character varying(32) NOT NULL,
                               creation_date timestamp without time zone DEFAULT '1970-01-01 00:00:00'::timestamp without time zone NOT NULL,
                               creator_id integer
);


ALTER TABLE quotes.groups OWNER TO "quote";

--
-- Name: groups_id_seq; Type: SEQUENCE; Schema: quotes; Owner: quote
--

ALTER TABLE quotes.groups ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME quotes.groups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    );


--
-- Name: quotes; Type: TABLE; Schema: quotes; Owner: quote
--

CREATE TABLE quotes.quotes (
                               id integer NOT NULL,
                               author character varying(64) NOT NULL,
                               creation_date timestamp without time zone NOT NULL,
                               text text NOT NULL,
                               context text,
                               creator_id integer
);


ALTER TABLE quotes.quotes OWNER TO "quote";

--
-- Name: quotes_id_seq; Type: SEQUENCE; Schema: quotes; Owner: quote
--

ALTER TABLE quotes.quotes ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME quotes.quotes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    );


--
-- Name: quotes_of_the_day; Type: TABLE; Schema: quotes; Owner: quote
--

CREATE TABLE quotes.quotes_of_the_day (
                                          id integer NOT NULL,
                                          quote_id integer NOT NULL,
                                          creation_date date NOT NULL,
                                          group_id integer NOT NULL
);


ALTER TABLE quotes.quotes_of_the_day OWNER TO "quote";

--
-- Name: quotes_of_the_day_id_seq; Type: SEQUENCE; Schema: quotes; Owner: quote
--

ALTER TABLE quotes.quotes_of_the_day ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME quotes.quotes_of_the_day_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    );


--
-- Name: users; Type: TABLE; Schema: users; Owner: quote
--

CREATE TABLE users.users (
                             id integer NOT NULL,
                             name character varying(32) NOT NULL,
                             email character varying(64) NOT NULL,
                             display_name character varying(32) NOT NULL,
                             creation_date timestamp without time zone DEFAULT '1970-01-01 00:00:00'::timestamp without time zone NOT NULL
);


ALTER TABLE users.users OWNER TO "quote";

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: users; Owner: quote
--

ALTER TABLE users.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME users.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    );


--
-- Name: group_quotes group_quotes_pkey; Type: CONSTRAINT; Schema: quotes; Owner: quote
--

ALTER TABLE ONLY quotes.group_quotes
    ADD CONSTRAINT group_quotes_pkey PRIMARY KEY (group_id, quote_id);


--
-- Name: group_users group_users_pkey; Type: CONSTRAINT; Schema: quotes; Owner: quote
--

ALTER TABLE ONLY quotes.group_users
    ADD CONSTRAINT group_users_pkey PRIMARY KEY (group_id, user_id);


--
-- Name: groups groups_pkey; Type: CONSTRAINT; Schema: quotes; Owner: quote
--

ALTER TABLE ONLY quotes.groups
    ADD CONSTRAINT groups_pkey PRIMARY KEY (id);


--
-- Name: quotes_of_the_day quotes_of_the_day_pkey; Type: CONSTRAINT; Schema: quotes; Owner: quote
--

ALTER TABLE ONLY quotes.quotes_of_the_day
    ADD CONSTRAINT quotes_of_the_day_pkey PRIMARY KEY (id);


--
-- Name: quotes quotes_pkey; Type: CONSTRAINT; Schema: quotes; Owner: quote
--

ALTER TABLE ONLY quotes.quotes
    ADD CONSTRAINT quotes_pkey PRIMARY KEY (id);


--
-- Name: users users_name_key; Type: CONSTRAINT; Schema: users; Owner: quote
--

ALTER TABLE ONLY users.users
    ADD CONSTRAINT users_name_key UNIQUE (name);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: users; Owner: quote
--

ALTER TABLE ONLY users.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: group_quotes group_quotes_group_id_fkey; Type: FK CONSTRAINT; Schema: quotes; Owner: quote
--

ALTER TABLE ONLY quotes.group_quotes
    ADD CONSTRAINT group_quotes_group_id_fkey FOREIGN KEY (group_id) REFERENCES quotes.groups(id) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


--
-- Name: group_quotes group_quotes_quote_id_fkey; Type: FK CONSTRAINT; Schema: quotes; Owner: quote
--

ALTER TABLE ONLY quotes.group_quotes
    ADD CONSTRAINT group_quotes_quote_id_fkey FOREIGN KEY (quote_id) REFERENCES quotes.quotes(id) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


--
-- Name: group_users group_users_group_id_fkey; Type: FK CONSTRAINT; Schema: quotes; Owner: quote
--

ALTER TABLE ONLY quotes.group_users
    ADD CONSTRAINT group_users_group_id_fkey FOREIGN KEY (group_id) REFERENCES quotes.groups(id) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


--
-- Name: group_users group_users_user_id_fkey; Type: FK CONSTRAINT; Schema: quotes; Owner: quote
--

ALTER TABLE ONLY quotes.group_users
    ADD CONSTRAINT group_users_user_id_fkey FOREIGN KEY (user_id) REFERENCES users.users(id) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


--
-- Name: groups groups_creator_id_fkey; Type: FK CONSTRAINT; Schema: quotes; Owner: quote
--

ALTER TABLE ONLY quotes.groups
    ADD CONSTRAINT groups_creator_id_fkey FOREIGN KEY (creator_id) REFERENCES users.users(id) ON UPDATE CASCADE ON DELETE SET NULL NOT VALID;


--
-- Name: quotes quotes_creator_id_fkey; Type: FK CONSTRAINT; Schema: quotes; Owner: quote
--

ALTER TABLE ONLY quotes.quotes
    ADD CONSTRAINT quotes_creator_id_fkey FOREIGN KEY (creator_id) REFERENCES users.users(id) ON UPDATE CASCADE ON DELETE SET NULL NOT VALID;


--
-- Name: quotes_of_the_day quotes_of_the_day_group_id_fkey; Type: FK CONSTRAINT; Schema: quotes; Owner: quote
--

ALTER TABLE ONLY quotes.quotes_of_the_day
    ADD CONSTRAINT quotes_of_the_day_group_id_fkey FOREIGN KEY (group_id) REFERENCES quotes.groups(id) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


--
-- Name: quotes_of_the_day quotes_of_the_day_quote_id_fkey; Type: FK CONSTRAINT; Schema: quotes; Owner: quote
--

ALTER TABLE ONLY quotes.quotes_of_the_day
    ADD CONSTRAINT quotes_of_the_day_quote_id_fkey FOREIGN KEY (quote_id) REFERENCES quotes.quotes(id) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--
