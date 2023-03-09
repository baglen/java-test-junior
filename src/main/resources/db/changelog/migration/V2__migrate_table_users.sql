--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--
CREATE TABLE IF NOT EXISTS "users"
(
    id          BIGSERIAL PRIMARY KEY,
    first_name  VARCHAR(255)   NOT NULL,
    last_name   VARCHAR(255)   NOT NULL,
    username    VARCHAR(255)   NOT NULL UNIQUE,
    password    VARCHAR(255)   NOT NULL,
    created_at  TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP      NOT NULL DEFAULT NOW()
);
ALTER TABLE "product" ADD FOREIGN KEY (user_id) REFERENCES users(id);