--
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--
CREATE TABLE IF NOT EXISTS "role"
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL
);
INSERT INTO "role" (name) VALUES ('ADMIN');
INSERT INTO "role" (name) VALUES ('USER');
ALTER TABLE "users" ADD COLUMN role_id BIGSERIAL NOT NULL;
ALTER TABLE "users" ADD FOREIGN KEY (role_id) REFERENCES role(id);
INSERT INTO "users" (first_name, last_name, username, password, created_at, updated_at, role_id)
VALUES ('Artiom', 'Spac', 'admin', '$2a$12$ceqF3VmReI5.k7aClaNYUOetptHf9a28qWeCMEEFnO5Z4lTCk5HW2', NOW(), NOW(), 1),
('Ivan', 'Ivanov', 'user', '$2a$12$fbuWEv86Ha1YHcQZDYDB6OxOFB2RVkGvrOcVlY0BWsE2pb26RAmqe', NOW(), NOW(), 2);