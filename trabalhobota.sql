CREATE TABLE IF NOT EXISTS candidato (
	id bigint PRIMARY KEY,
	nomeUrna varchar(256),
	numero bigint,
	idCandidatoSuperior bigint,
	nomeCompleto varchar(256),
	descricaoSexo varchar(128),
	dataDeNascimento date,
	tituloEleitor varchar(256),
	cpf varchar(256),
	descricaoEstadoCivil varchar(128),
	descricaoCorRaca varchar(128),
	descricaoSituacao varchar(128),
	nacionalidade varchar(128),
	grauInstrucao varchar(128),
	ocupacao varchar(128),
	gastoCampanha1T float,
	gastoCampanha2T float,
	sgUfNascimento varchar(64),
	nomeMunicipioNascimento varchar(256),
	localCandidatura varchar(256),
	ufCandidatura varchar(128),
	ufSuperiorCandidatura varchar(64),
	dataUltimaAtualizacao date,
	fotoUrl varchar(512),
	fotoDataUltimaAtualizacao varchar(256),
	descricaoTotalizacao varchar(128),
	nomeColigacao varchar(256),
	composicaoColigacao varchar(256),
	numeroProcessoDrap varchar(256),
	numeroProcessoDrapEncrypt varchar(256),
	numeroProcesso varchar(256),
	numeroProcessoEncrypt varchar(256),
	numeroProcessoPrestContas varchar(256),
	numeroProcessoPrestContasEncrypt varchar(256),
	numeroProtocolo varchar(256),
	totalDeBens float,
	FK_cargo bigint,
	FK_partido bigint,
	FK_eleicao bigint,
	substituto varchar(256),
	motivos varchar(256),
	codigoSituacaoCandidato bigint,
	descricaoSituacaoCandidato varchar(128),
	st_SUBSTITUIDO boolean,
	descricaoNaturalidade varchar(128),
	st_MOTIVO_AUSENCIA_REQUISITO boolean,
	st_MOTIVO_CONDUTA_VEDADA boolean,
	cnpjcampanha varchar(128),
	gastoCampanha float,
	st_MOTIVO_ABUSO_PODER boolean,
    st_MOTIVO_COMPRA_VOTO boolean,
    ds_MOTIVO_OUTROS varchar(256),
    st_MOTIVO_GASTO_ILICITO boolean,
    st_MOTIVO_IND_PARTIDO boolean,
    st_MOTIVO_FICHA_LIMPA boolean,
    st_DIVULGA_ARQUIVOS boolean,
    st_DIVULGA_BENS boolean,
    st_DIVULGA boolean,
    st_REELEICAO boolean
);

CREATE TABLE IF NOT EXISTS cargo (
	codigo integer PRIMARY KEY,
	sigla varchar(64),
	nome varchar(124),
	codSuperior bigint,
	titular boolean,
	contagem bigint
);

CREATE TABLE IF NOT EXISTS bens (
	ordem bigint,
	descricao varchar(512),
	descricaoDeTipoDeBem varchar(256),
	valor float,
	dataUltimaAtualizacao date,
	FK_candidato bigint
);

CREATE TABLE IF NOT EXISTS vices (
	nomeColigacao varchar(256),
    composicaoColigacao varchar(256),
    stRegistro varchar(256),
    situacaoCandidato varchar(256),
    urlFoto varchar(512),
    sg_PARTIDO varchar(64),
    nm_URNA varchar(256),
    nm_CANDIDATO varchar(256),
    sq_ELEICAO bigint,
    sq_CANDIDATO_SUPERIOR bigint,
    nr_CANDIDATO bigint,
    ds_CARGO varchar(64),
    nm_PARTIDO varchar(124),
    sq_CANDIDATO bigint,
    sg_UE bigint,
    dt_ULTIMA_ATUALIZACAO date,
	FK_candidato bigint
);

CREATE TABLE IF NOT EXISTS partido (
	numero bigint PRIMARY KEY,
	sigla varchar(64),
	nome varchar(124)
);

CREATE TABLE IF NOT EXISTS eleicao (
	id bigint PRIMARY KEY,
    siglaUF varchar(64),
    localidadeSgUe varchar(128),
    ano bigint,
    codigo bigint,
    nomeEleicao varchar(256),
    tipoEleicao varchar(256),
    turno varchar(128),
    tipoAbrangencia varchar(128),
    dataEleicao date,
    codSituacaoEleicao bigint,
    descricaoSituacaoEleicao varchar(256),
    descricaoEleicao varchar(128)
);

CREATE TABLE IF NOT EXISTS emails (
	descricao varchar(512),
	FK_candidato bigint
);

CREATE TABLE IF NOT EXISTS sites (
	descricao varchar(512),
	FK_candidato bigint
);

CREATE TABLE IF NOT EXISTS arquivos (
	idArquivo bigint PRIMARY KEY,
    nome varchar(256),
    url varchar(512),
    tipo varchar(64),
    codTipo varchar(64),
    fullFilePath varchar(128),
    fileInputStream varchar(128),
    fileByteArray varchar(128),
	FK_candidato bigint
);

CREATE TABLE IF NOT EXISTS eleicoesAnteriores (
	nrAno bigint,
	id integer PRIMARY KEY,
    nomeUrna varchar(256),
    nomeCandidato varchar(512),
    idEleicao varchar(128),
    sgUe varchar(128),
    local varchar(128),
    cargo varchar(128),
    partido varchar(64),
    situacaoTotalizacao varchar(128),
    txLink varchar(512),
	FK_candidato bigint
);

ALTER TABLE bens
ADD CONSTRAINT FK_candidato FOREIGN KEY(FK_candidato) REFERENCES candidato(id);

ALTER TABLE vices
ADD CONSTRAINT FK_candidato FOREIGN KEY(FK_candidato) REFERENCES candidato(id);

ALTER TABLE emails
ADD CONSTRAINT FK_candidato FOREIGN KEY(FK_candidato) REFERENCES candidato(id);

ALTER TABLE sites
ADD CONSTRAINT FK_candidato FOREIGN KEY(FK_candidato) REFERENCES candidato(id);

ALTER TABLE arquivos
ADD CONSTRAINT FK_candidato FOREIGN KEY(FK_candidato) REFERENCES candidato(id);

ALTER TABLE eleicoesAnteriores
ADD CONSTRAINT FK_candidato FOREIGN KEY(FK_candidato) REFERENCES candidato(id);