-- PostgreSQL Full Text Search (FTS) Index for Users Table

CREATE INDEX IF NOT EXISTS idx_userchans_fts
ON userchans
USING GIN (to_tsvector('english', COALESCE(username, '') || ' ' || COALESCE(email, '')));

