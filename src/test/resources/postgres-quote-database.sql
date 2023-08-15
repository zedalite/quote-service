-- Role: quote
-- DROP ROLE IF EXISTS quote;

CREATE ROLE quote WITH
    LOGIN
    NOSUPERUSER
    INHERIT
    NOCREATEDB
    NOCREATEROLE
    NOREPLICATION
    ENCRYPTED PASSWORD 'SCRAM-SHA-256$4096:SzY0QBIJIy8MoCXotxK2cw==$+hvlfBw85fSYQZA/RJEBnVyghgMlQCfSQZ20RN3y1nM=:xeMxvAxZvmXZozfnc4eSMO2ULFLukG15mvISHOqhbQk=';

--
-- PostgreSQL database dump
--

-- Dumped from database version 15.3
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

--
-- Name: quote; Type: DATABASE; Schema: -; Owner: quote
--

CREATE DATABASE quote WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'C';


ALTER DATABASE quote OWNER TO quote;

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: quotes; Type: TABLE; Schema: public; Owner: quote
--

CREATE TABLE public.quotes (
                               id integer NOT NULL,
                               author character varying(64) NOT NULL,
                               datetime timestamp without time zone NOT NULL,
                               text text NOT NULL,
                               context text,
                               creator_id integer
);


ALTER TABLE public.quotes OWNER TO quote;

--
-- Name: quotes_id_seq; Type: SEQUENCE; Schema: public; Owner: quote
--

ALTER TABLE public.quotes ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.quotes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    );


--
-- Name: quotes_of_the_day; Type: TABLE; Schema: public; Owner: quote
--

CREATE TABLE public.quotes_of_the_day (
                                          id integer NOT NULL,
                                          quote_id integer NOT NULL,
                                          datetime timestamp without time zone NOT NULL
);


ALTER TABLE public.quotes_of_the_day OWNER TO quote;

--
-- Name: quotes_of_the_day_id_seq; Type: SEQUENCE; Schema: public; Owner: quote
--

ALTER TABLE public.quotes_of_the_day ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.quotes_of_the_day_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    );


--
-- Name: users; Type: TABLE; Schema: public; Owner: quote
--

CREATE TABLE public.users (
                              id integer NOT NULL,
                              name character varying(32) NOT NULL,
                              password character varying(128) NOT NULL,
                              creation_date timestamp without time zone DEFAULT '1970-01-01 00:00:00'::timestamp without time zone NOT NULL,
                              display_name character varying(32) NOT NULL
);


ALTER TABLE public.users OWNER TO quote;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: quote
--

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    );


--
-- Data for Name: quotes; Type: TABLE DATA; Schema: public; Owner: quote
--



--
-- Data for Name: quotes_of_the_day; Type: TABLE DATA; Schema: public; Owner: quote
--



--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: quote
--



--
-- Name: quotes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: quote
--

SELECT pg_catalog.setval('public.quotes_id_seq', 1, false);


--
-- Name: quotes_of_the_day_id_seq; Type: SEQUENCE SET; Schema: public; Owner: quote
--

SELECT pg_catalog.setval('public.quotes_of_the_day_id_seq', 1, false);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: quote
--

SELECT pg_catalog.setval('public.users_id_seq', 1, false);


--
-- Name: quotes_of_the_day quotes_of_the_day_pkey; Type: CONSTRAINT; Schema: public; Owner: quote
--

ALTER TABLE ONLY public.quotes_of_the_day
    ADD CONSTRAINT quotes_of_the_day_pkey PRIMARY KEY (id);


--
-- Name: quotes quotes_pkey; Type: CONSTRAINT; Schema: public; Owner: quote
--

ALTER TABLE ONLY public.quotes
    ADD CONSTRAINT quotes_pkey PRIMARY KEY (id);


--
-- Name: users users_name_key; Type: CONSTRAINT; Schema: public; Owner: quote
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_name_key UNIQUE (name);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: quote
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: quotes quotes_creator_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: quote
--

ALTER TABLE ONLY public.quotes
    ADD CONSTRAINT quotes_creator_id_fkey FOREIGN KEY (creator_id) REFERENCES public.users(id) NOT VALID;


--
-- Name: quotes_of_the_day quotes_of_the_day_quote_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: quote
--

ALTER TABLE ONLY public.quotes_of_the_day
    ADD CONSTRAINT quotes_of_the_day_quote_id_fkey FOREIGN KEY (quote_id) REFERENCES public.quotes(id) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


--
-- PostgreSQL database dump complete
--

