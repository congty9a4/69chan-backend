
INSERT INTO userchans (id, username, password, email, is_active, created_at, updated_at) VALUES
                                                                                             (gen_random_uuid(), 'user1', 'password', 'user1@example.com', true, NOW(), NOW()),
                                                                                             (gen_random_uuid(), 'user2', 'password', 'user2@example.com', true, NOW(), NOW()),
                                                                                             (gen_random_uuid(), 'user3', 'password', 'user3@example.com', true, NOW(), NOW()),
                                                                                             (gen_random_uuid(), 'user4', 'password', 'user4@example.com', true, NOW(), NOW()),
                                                                                             (gen_random_uuid(), 'user5', 'password', 'user5@example.com', true, NOW(), NOW());

DO $$
    DECLARE
        user1_id UUID;
        user2_id UUID;
        user3_id UUID;
        user4_id UUID;
        user5_id UUID;
    BEGIN
        SELECT DISTINCT id INTO user1_id FROM userchans WHERE email = 'guest@69chan.com';
        SELECT id INTO user2_id FROM userchans WHERE email = 'user2@example.com';
        SELECT id INTO user3_id FROM userchans WHERE email = 'user3@example.com';
        SELECT id INTO user4_id FROM userchans WHERE email = 'user4@example.com';
        SELECT id INTO user5_id FROM userchans WHERE email = 'user5@example.com';

        INSERT INTO relationships (object_name, object_id, relation, subject_name, subject_id) VALUES
                                                                                                                     ('user', user1_id, 'follower', 'user', user2_id),
                                                                                                                     ('user', user1_id, 'follower', 'user', user3_id),
                                                                                                                     ('user', user1_id, 'follower', 'user', user4_id),
                                                                                                                     ('user', user1_id, 'follower', 'user', user5_id);
    END $$;

