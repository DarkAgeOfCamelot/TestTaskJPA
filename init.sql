CREATE SCHEMA test AUTHORIZATION testuser;
CREATE TABLE test.counter (counter int8 NOT NULL, timestamp_upd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, CONSTRAINT counter_pkey PRIMARY KEY (counter));
GRANT ALL ON TABLE test.counter TO testuser;
INSERT INTO test.counter (counter, timestamp_upd) VALUES(0, CURRENT_TIMESTAMP);