-- DROP IDS
ALTER TABLE public.candidato_arquivos DROP CONSTRAINT arquivos_pkey;
ALTER TABLE public.candidatos DROP CONSTRAINT candidato_pkey;
ALTER TABLE public.candidato_eleicoes DROP CONSTRAINT eleicao_pkey;
ALTER TABLE public.partido DROP CONSTRAINT partido_pkey;
ALTER TABLE public.candidato_substitutos DROP CONSTRAINT substituto_pkey;

-- DROP FKS
ALTER TABLE public.candidato_arquivos DROP CONSTRAINT fk_arquivos_candidato;
ALTER TABLE public.bens DROP CONSTRAINT fk_bens_candidato;
ALTER TABLE public.candidato_emails DROP CONSTRAINT fk_email_candidato;
ALTER TABLE public.motivos DROP CONSTRAINT fk_motivos_candidato;
ALTER TABLE public.candidatos DROP CONSTRAINT fk_motivos_substituto;
ALTER TABLE public.candidato_sites DROP CONSTRAINT fk_sites_candidato;
ALTER TABLE public.candidato_vices DROP CONSTRAINT fk_vices_candidato;

-- CHANGE IDS TO BIGINT
ALTER TABLE public.candidato_arquivos ALTER COLUMN idarquivo TYPE BIGINT USING idarquivo::bigint;
ALTER TABLE public.candidatos ALTER COLUMN id TYPE BIGINT USING id::bigint;
ALTER TABLE public.candidato_eleicoes ALTER COLUMN id TYPE BIGINT USING id::bigint;
ALTER TABLE public.partidos ALTER COLUMN numero TYPE BIGINT USING numero::bigint;
ALTER TABLE public.candidato_substitutos ALTER COLUMN sqeleicao TYPE BIGINT USING sqeleicao::bigint;

-- REMOVE DUPLICATED IDS
delete from public.candidato_arquivos where public.candidato_arquivos.idarquivo in (select public.candidato_arquivos.idarquivo from public.candidato_arquivos GROUP BY public.candidato_arquivos.idarquivo HAVING count(*) > 1);
delete from public.candidatos where public.candidatos.id in (select public.candidatos.id from public.candidatos GROUP BY public.candidatos.id HAVING count(*) > 1);
delete from public.candidato_eleicoes where public.candidato_eleicoes.id in (select public.candidato_eleicoes.id from public.candidato_eleicoes GROUP BY public.candidato_eleicoes.id HAVING count(*) > 1);
delete from public.partidos where public.partidos.numero in (select public.partidos.numero from public.partidos GROUP BY public.partidos.numero HAVING count(*) > 1);
delete from public.candidato_substitutos where public.candidato_substitutos.sqeleicao in (select public.candidato_substitutos.sqeleicao from public.candidato_substitutos GROUP BY public.candidato_substitutos.sqeleicao HAVING count(*) > 1);

-- CREATE PKS
ALTER TABLE public.candidato_arquivos ADD CONSTRAINT arquivos_pkey PRIMARY KEY (idarquivo);
ALTER TABLE public.candidatos ADD CONSTRAINT candidato_pkey PRIMARY KEY (id);
ALTER TABLE public.candidato_eleicoes ADD CONSTRAINT eleicao_pkey PRIMARY KEY (id);
ALTER TABLE public.partidos ADD CONSTRAINT partido_pkey PRIMARY KEY (numero);
ALTER TABLE public.candidato_substitutos ADD CONSTRAINT substituto_pkey PRIMARY KEY (sqeleicao);

-- CREATE FKS
ALTER TABLE public.candidato_arquivos ADD fk_candidato VARCHAR(512);
ALTER TABLE public.candidato_bens ADD fk_candidato VARCHAR(512);
ALTER TABLE public.candidato_emails ADD fk_candidato VARCHAR(512);
ALTER TABLE public.candidato_motivos ADD fk_candidato VARCHAR(512);
ALTER TABLE public.candidatos ADD fk_substituto VARCHAR(512);
ALTER TABLE public.candidato_sites ADD fk_candidato VARCHAR(512);
ALTER TABLE public.vices ADD fk_candidato VARCHAR(512);

-- UPDATE VALUES TO FKS TO -1 WHERE VALUE IS NULL
UPDATE public.candidato_arquivos SET fk_candidato = '-1' WHERE fk_candidato is NULL OR fk_candidato LIKE 'null';
UPDATE public.candidato_bens SET fk_candidato = '-1' WHERE fk_candidato is NULL OR fk_candidato LIKE 'null';
UPDATE public.candidato_emails SET fk_candidato = '-1' WHERE fk_candidato is NULL OR fk_candidato LIKE 'null';
UPDATE public.candidato_motivos SET fk_candidato = '-1' WHERE fk_candidato is NULL OR fk_candidato LIKE 'null';
UPDATE public.candidatos SET fk_substituto = '-1' WHERE fk_substituto is NULL OR fk_substituto LIKE 'null';
UPDATE public.candidato_sites SET fk_candidato = '-1' WHERE fk_candidato is NULL OR fk_candidato LIKE 'null';
UPDATE public.vices SET fk_candidato = '-1' WHERE fk_candidato is NULL OR fk_candidato LIKE 'null';

-- CHANGE PKS TO BIGINT
ALTER TABLE public.candidato_arquivos ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;
ALTER TABLE public.candidato_bens ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;
ALTER TABLE public.candidato_emails ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;
ALTER TABLE public.candidato_motivos ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;
ALTER TABLE public.candidatos ALTER COLUMN fk_substituto TYPE BIGINT USING fk_substituto::bigint;
ALTER TABLE public.candidato_sites ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;
ALTER TABLE public.vices ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;

-- CHANGE PKS TO NULL WHERE IS LESS THEN ZERO
UPDATE public.candidato_arquivos SET fk_candidato = null WHERE fk_candidato < 0;
UPDATE public.candidato_bens SET fk_candidato = null WHERE fk_candidato < 0;
UPDATE public.candidato_emails SET fk_candidato = null WHERE fk_candidato < 0;
UPDATE public.candidato_motivos SET fk_candidato = null WHERE fk_candidato < 0;
UPDATE public.candidatos SET fk_substituto = null WHERE fk_substituto < 0;
UPDATE public.candidato_sites SET fk_candidato = null WHERE fk_candidato < 0;
UPDATE public.vices SET fk_candidato = null WHERE fk_candidato < 0;

-- ADD FKS
ALTER TABLE public.candidato_arquivos ADD CONSTRAINT fk_arquivos_candidato FOREIGN KEY (fk_candidato) REFERENCES public.candidatos(id);
ALTER TABLE public.candidato_bens ADD CONSTRAINT fk_bens_candidato FOREIGN KEY (fk_candidato) REFERENCES public.candidatos(id);
ALTER TABLE public.candidato_emails ADD CONSTRAINT fk_email_candidato FOREIGN KEY (fk_candidato) REFERENCES public.candidatos(id);
ALTER TABLE public.candidato_motivos ADD CONSTRAINT fk_motivos_candidato FOREIGN KEY (fk_candidato) REFERENCES public.candidatos(id);
ALTER TABLE public.candidatos ADD CONSTRAINT fk_motivos_substituto FOREIGN KEY (fk_substituto) REFERENCES public.candidato_substitutos(sqeleicao);
ALTER TABLE public.candidato_sites ADD CONSTRAINT fk_sites_candidato FOREIGN KEY (fk_candidato) REFERENCES public.candidatos(id);
ALTER TABLE public.vices ADD CONSTRAINT fk_vices_candidato FOREIGN KEY (fk_candidato) REFERENCES public.candidatos(id);

SELECT c.nomecompleto, c.id, count(*)
FROM candidatos c
GROUP BY c.id, c.nomecompleto
HAVING count(*) > 1

SELECT e.id, e.nomecandidato, count(*)
FROM candidato_eleicoes e
GROUP BY e.id, e.nomecandidato
HAVING count(*) > 1

SELECT e.id FROM candidato_eleicoes e GROUP BY e.id HAVING count(*) > 1


UPDATE public.candidato_arquivos SET fk_candidato = 0 WHERE fk_candidato = NULL;
UPDATE public.candidato_bens SET fk_candidato = 0 WHERE fk_candidato = NULL;
UPDATE public.candidato_emails SET fk_candidato = 0 WHERE fk_candidato = NULL;
UPDATE public.candidato_motivos SET fk_candidato = 0 WHERE fk_candidato = NULL;
UPDATE public.candidatos SET fk_substituto = 0 WHERE fk_substituto = NULL;
UPDATE public.candidato_sites SET fk_candidato = 0 WHERE fk_candidato = NULL;
UPDATE public.vices SET fk_candidato = 0 WHERE fk_candidato = NULL;


ALTER TABLE public.candidato_arquivos ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;
ALTER TABLE public.candidato_bens ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;
ALTER TABLE public.candidato_emails ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;
ALTER TABLE public.candidato_motivos ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;
ALTER TABLE public.candidatos ALTER COLUMN fk_substituto TYPE BIGINT USING fk_substituto::bigint;
ALTER TABLE public.candidato_sites ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;
ALTER TABLE public.vices ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;