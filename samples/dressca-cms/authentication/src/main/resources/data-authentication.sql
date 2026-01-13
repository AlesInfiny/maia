-- Insert test user with BCrypt hashed password
-- Password: password
-- BCrypt hash generated with strength 10
INSERT INTO APPLICATION_USERS (ID, NAME, EMAIL, PASSWORD) VALUES
(1, 'Test User', 'user@example.com', '$2a$10$rflcmgr1HKWsZQ7M7qmWN.9GmbRhFKughgCIE3XGkNPVbDMO23ECu');
