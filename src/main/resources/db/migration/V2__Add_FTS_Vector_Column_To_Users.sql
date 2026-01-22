-- Add a tsvector column to the userchans table
ALTER TABLE userchans ADD COLUMN fts_document tsvector;

-- Create a function to update the fts_document column
CREATE OR REPLACE FUNCTION userchan_fts_document_trigger() RETURNS trigger AS $$
BEGIN
    NEW.fts_document :=
        to_tsvector('english', COALESCE(NEW.username, '') || ' ' || COALESCE(NEW.email, ''));
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

-- Create a trigger to automatically update the fts_document column
CREATE TRIGGER tsvectorupdate BEFORE INSERT OR UPDATE
ON userchans FOR EACH ROW EXECUTE PROCEDURE userchan_fts_document_trigger();

-- Populate the new fts_document column for existing rows
UPDATE userchans SET fts_document = to_tsvector('english', COALESCE(username, '') || ' ' || COALESCE(email, ''));

-- Drop the old index
DROP INDEX IF EXISTS idx_userchans_fts;

-- Create a new GIN index on the fts_document column
CREATE INDEX idx_userchans_fts ON userchans USING GIN (fts_document);

