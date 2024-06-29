-- Para la tabla `users`
UPDATE users
SET active = 1;

-- Para la tabla `topics`
ALTER TABLE topics
ADD COLUMN active TINYINT(1) DEFAULT 1 NOT NULL;

-- No es necesario el UPDATE aquí, ya que la columna active se inicializa automáticamente con el valor DEFAULT 1

-- Para la tabla `responses`
ALTER TABLE responses
ADD COLUMN active TINYINT(1) DEFAULT 1 NOT NULL;

UPDATE responses
SET active = 1;