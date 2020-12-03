--
-- PostgreSQL database dump
--

-- Dumped from database version 13.0 (Debian 13.0-1.pgdg100+1)
-- Dumped by pg_dump version 13.0 (Debian 13.0-1.pgdg100+1)

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
-- Name: candidato_arquivos; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.candidato_arquivos (
    tipo character varying(512),
    fileinputstream character varying(512),
    codtipo character varying(512),
    fullfilepath character varying(512),
    nome character varying(512),
    filebytearray character varying(512),
    url character varying(512),
    idarquivo character varying(512)
);


ALTER TABLE public.candidato_arquivos OWNER TO root;

--
-- Name: candidato_bens; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.candidato_bens (
    ordem character varying(512),
    valor character varying(512),
    dataultimaatualizacao character varying(512),
    descricao character varying(512),
    descricaodetipodebem character varying(512)
);


ALTER TABLE public.candidato_bens OWNER TO root;

--
-- Name: candidato_eleicoes; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.candidato_eleicoes (
    codigo character varying(512),
    ano character varying(512),
    nomecandidato character varying(512),
    nomeurna character varying(512),
    codsituacaoeleicao character varying(512),
    turno character varying(512),
    dataeleicao character varying(512),
    ideleicao character varying(512),
    local character varying(512),
    descricaoeleicao character varying(512),
    txlink character varying(512),
    sgue character varying(512),
    descricaosituacaoeleicao character varying(512),
    localidadesgue character varying(512),
    id character varying(512),
    nrano character varying(512),
    cargo character varying(512),
    nomeeleicao character varying(512),
    partido character varying(512),
    situacaototalizacao character varying(512),
    tipoeleicao character varying(512),
    tipoabrangencia character varying(512),
    siglauf character varying(512)
);


ALTER TABLE public.candidato_eleicoes OWNER TO root;

--
-- Name: candidato_emails; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.candidato_emails (
    email character varying(512)
);


ALTER TABLE public.candidato_emails OWNER TO root;

--
-- Name: candidato_sites; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.candidato_sites (
    site character varying(512)
);


ALTER TABLE public.candidato_sites OWNER TO root;

--
-- Name: candidatos; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.candidatos (
    fotodataultimaatualizacao character varying(512),
    composicaocoligacao character varying(512),
    nomeurna character varying(512),
    nomecoligacao character varying(512),
    st_motivo_ind_partido character varying(512),
    descricaosituacaocandidato character varying(512),
    st_divulga_bens character varying(512),
    cnpjcampanha character varying(512),
    ocupacao character varying(512),
    cpf character varying(512),
    dataultimaatualizacao character varying(512),
    id character varying(512),
    descricaoestadocivil character varying(512),
    st_motivo_ficha_limpa character varying(512),
    nomecompleto character varying(512),
    gastocampanha character varying(512),
    localcandidatura character varying(512),
    numeroprocessoencrypt character varying(512),
    grauinstrucao character varying(512),
    st_motivo_conduta_vedada character varying(512),
    st_divulga_arquivos character varying(512),
    st_substituido character varying(512),
    st_reeleicao character varying(512),
    ufsuperiorcandidatura character varying(512),
    ds_motivo_outros character varying(512),
    descricaocorraca character varying(512),
    descricaosexo character varying(512),
    datadenascimento character varying(512),
    sgufnascimento character varying(512),
    numeroprocesso character varying(512),
    numero character varying(512),
    gastocampanha1t character varying(512),
    numeroprocessoprestcontas character varying(512),
    descricaototalizacao character varying(512),
    st_motivo_ausencia_requisito character varying(512),
    descricaonaturalidade character varying(512),
    numeroprocessoprestcontasencrypt character varying(512),
    st_motivo_gasto_ilicito character varying(512),
    idcandidatosuperior character varying(512),
    numeroprocessodrapencrypt character varying(512),
    gastocampanha2t character varying(512),
    st_motivo_compra_voto character varying(512),
    ufcandidatura character varying(512),
    codigosituacaocandidato character varying(512),
    st_divulga character varying(512),
    totaldebens character varying(512),
    fotourl character varying(512),
    substituto character varying(512),
    descricaosituacao character varying(512),
    nomemunicipionascimento character varying(512),
    numeroprotocolo character varying(512),
    tituloeleitor character varying(512),
    nacionalidade character varying(512),
    st_motivo_abuso_poder character varying(512),
    numeroprocessodrap character varying(512)
);


ALTER TABLE public.candidatos OWNER TO root;

--
-- Name: cargos; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.cargos (
    sigla character varying(512),
    nome character varying(512),
    codsuperior character varying(512),
    contagem character varying(512),
    titular character varying(512),
    codigo character varying(512)
);


ALTER TABLE public.cargos OWNER TO root;

--
-- Name: partidos; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.partidos (
    numero character varying(512),
    nome character varying(512),
    sigla character varying(512)
);


ALTER TABLE public.partidos OWNER TO root;

--
-- Name: vices; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.vices (
    situacaocandidato character varying(512),
    ds_cargo character varying(512),
    stregistro character varying(512),
    composicaocoligacao character varying(512),
    nr_candidato character varying(512),
    sq_candidato character varying(512),
    urlfoto character varying(512),
    nm_partido character varying(512),
    sg_partido character varying(512),
    nomecoligacao character varying(512),
    sq_eleicao character varying(512),
    dt_ultima_atualizacao character varying(512),
    sq_candidato_superior character varying(512),
    nm_candidato character varying(512),
    sg_ue character varying(512),
    nm_urna character varying(512)
);


ALTER TABLE public.vices OWNER TO root;

--
-- Data for Name: candidato_arquivos; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.candidato_arquivos (tipo, fileinputstream, codtipo, fullfilepath, nome, filebytearray, url, idarquivo) FROM stdin;
\.


--
-- Data for Name: candidato_bens; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.candidato_bens (ordem, valor, dataultimaatualizacao, descricao, descricaodetipodebem) FROM stdin;
\.


--
-- Data for Name: candidato_eleicoes; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.candidato_eleicoes (codigo, ano, nomecandidato, nomeurna, codsituacaoeleicao, turno, dataeleicao, ideleicao, local, descricaoeleicao, txlink, sgue, descricaosituacaoeleicao, localidadesgue, id, nrano, cargo, nomeeleicao, partido, situacaototalizacao, tipoeleicao, tipoabrangencia, siglauf) FROM stdin;
\.


--
-- Data for Name: candidato_emails; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.candidato_emails (email) FROM stdin;
\.


--
-- Data for Name: candidato_sites; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.candidato_sites (site) FROM stdin;
\.


--
-- Data for Name: candidatos; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.candidatos (fotodataultimaatualizacao, composicaocoligacao, nomeurna, nomecoligacao, st_motivo_ind_partido, descricaosituacaocandidato, st_divulga_bens, cnpjcampanha, ocupacao, cpf, dataultimaatualizacao, id, descricaoestadocivil, st_motivo_ficha_limpa, nomecompleto, gastocampanha, localcandidatura, numeroprocessoencrypt, grauinstrucao, st_motivo_conduta_vedada, st_divulga_arquivos, st_substituido, st_reeleicao, ufsuperiorcandidatura, ds_motivo_outros, descricaocorraca, descricaosexo, datadenascimento, sgufnascimento, numeroprocesso, numero, gastocampanha1t, numeroprocessoprestcontas, descricaototalizacao, st_motivo_ausencia_requisito, descricaonaturalidade, numeroprocessoprestcontasencrypt, st_motivo_gasto_ilicito, idcandidatosuperior, numeroprocessodrapencrypt, gastocampanha2t, st_motivo_compra_voto, ufcandidatura, codigosituacaocandidato, st_divulga, totaldebens, fotourl, substituto, descricaosituacao, nomemunicipionascimento, numeroprotocolo, tituloeleitor, nacionalidade, st_motivo_abuso_poder, numeroprocessodrap) FROM stdin;
\.


--
-- Data for Name: cargos; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.cargos (sigla, nome, codsuperior, contagem, titular, codigo) FROM stdin;
\.


--
-- Data for Name: partidos; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.partidos (numero, nome, sigla) FROM stdin;
\.


--
-- Data for Name: vices; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.vices (situacaocandidato, ds_cargo, stregistro, composicaocoligacao, nr_candidato, sq_candidato, urlfoto, nm_partido, sg_partido, nomecoligacao, sq_eleicao, dt_ultima_atualizacao, sq_candidato_superior, nm_candidato, sg_ue, nm_urna) FROM stdin;
\.


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: root
--

GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

