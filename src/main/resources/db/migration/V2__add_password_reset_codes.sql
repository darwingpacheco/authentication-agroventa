-- Password reset codes table (6-digit codes, hashed, TTL 15 min)
CREATE TABLE IF NOT EXISTS password_reset_codes (
    id            UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id       UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    code_hash     VARCHAR(255) NOT NULL,
    attempts      SMALLINT    NOT NULL DEFAULT 0,
    used          BOOLEAN     NOT NULL DEFAULT FALSE,
    expires_at    TIMESTAMPTZ NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_prc_user_id ON password_reset_codes (user_id);
CREATE INDEX IF NOT EXISTS idx_prc_expires_at ON password_reset_codes (expires_at);
