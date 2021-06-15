CREATE KEYSPACE console
WITH replication = {'class' : 'SimpleStrategy', 'replication_factor' : 3}
AND durable_writes = true;

use console;

CREATE TABLE console(
    id uuid,
    nome text,
    marca text,
    data_lancamento date,
    data_cadastro date,
    PRIMARY KEY (id)
);