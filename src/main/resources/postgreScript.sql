CREATE TABLE IF NOT EXISTS sale (
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ,
    date date NOT NULL DEFAULT CURRENT_DATE ,
    customer_name varchar(50) NOT NULL,
    amount numeric NOT NULL,
    CONSTRAINT id_pk PRIMARY KEY(id)
);