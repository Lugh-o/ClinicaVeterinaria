DROP TABLE IF EXISTS proprietario CASCADE;
DROP TABLE IF EXISTS animal CASCADE;
DROP TABLE IF EXISTS veterinario CASCADE;
DROP TABLE IF EXISTS telefone CASCADE;
DROP TABLE IF EXISTS especialidade CASCADE;
DROP TABLE IF EXISTS consulta CASCADE;

CREATE TABLE proprietario (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(255),
  cpf VARCHAR(15) UNIQUE,
  email VARCHAR(63),
  id_telefone INTEGER NOT NULL
);

CREATE TABLE animal (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(255),
  especie VARCHAR(255),
  raca VARCHAR(255),
  data_nascimento date,
  peso decimal,
  id_proprietario INTEGER NOT NULL
);

CREATE TABLE veterinario (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(255),
  crmv VARCHAR(63) UNIQUE,
  id_especialidade INTEGER NOT NULL,
  id_telefone INTEGER NOT NULL
);

CREATE TABLE telefone (
  id SERIAL PRIMARY KEY,
  numero VARCHAR(15)
);

CREATE TABLE especialidade (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(255)
);

CREATE TABLE consulta (
  id SERIAL PRIMARY KEY,
  datetime TIMESTAMP,
  id_animal INTEGER NOT NULL,
  id_veterinario INTEGER NOT NULL,
  diagnostico text,
  valor decimal
);

ALTER TABLE proprietario ADD FOREIGN KEY (id_telefone) REFERENCES telefone(id);
ALTER TABLE animal ADD FOREIGN KEY (id_proprietario) REFERENCES proprietario(id);
ALTER TABLE veterinario ADD FOREIGN KEY (id_especialidade) REFERENCES especialidade(id);
ALTER TABLE veterinario ADD FOREIGN KEY (id_telefone) REFERENCES telefone(id);
ALTER TABLE consulta ADD FOREIGN KEY (id_animal) REFERENCES animal(id);
ALTER TABLE consulta ADD FOREIGN KEY (id_veterinario) REFERENCES veterinario(id);