DO $$
BEGIN
	IF NOT EXISTS (
		SELECT 1 FROM information_schema.columns 
		WHERE table_name='invoices' AND column_name='type'
	) THEN
		ALTER TABLE invoices ADD COLUMN type VARCHAR(50);
	END IF;
END$$;
UPDATE invoices SET type = 'WATER';
ALTER TABLE invoices ALTER COLUMN type SET NOT NULL;
