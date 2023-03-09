--
-- Name: product_like; Type: TABLE; Schema: public; Owner: postgres
--
CREATE TABLE IF NOT EXISTS "product_like"
(
    id          BIGSERIAL PRIMARY KEY,
    product_id  BIGSERIAL   NOT NULL,
    user_id     BIGSERIAL   NOT NULL,
    is_liked    BOOLEAN     NOT NULL
);
ALTER TABLE "product_like" ADD FOREIGN KEY (product_id) REFERENCES product(id);
ALTER TABLE "product_like" ADD FOREIGN KEY (user_id) REFERENCES users(id);