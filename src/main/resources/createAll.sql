CREATE TABLE IF NOT EXISTS patogeno (
  tipo VARCHAR(255) NOT NULL UNIQUE,
  id int auto_increment NOT NULL ,
  cantidadDeEspecies int NOT NULL,
  PRIMARY KEY (id)
);