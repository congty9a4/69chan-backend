-- Thêm cột is_verified vào bảng userchans
ALTER TABLE userchans ADD COLUMN is_verified BOOLEAN DEFAULT FALSE NOT NULL;