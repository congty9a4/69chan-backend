-- Social Network Database Schema (PostgreSQL)
-- MVP for Facebook-like application

-- Drop Functions if they exist
DROP FUNCTION IF EXISTS update_updated_at() CASCADE;

-- Drop Tables if they exist (use CASCADE to drop dependent objects like indexes, constraints, triggers, etc.)
DROP TABLE IF EXISTS notifications ;
DROP TABLE IF EXISTS messages ;
DROP TABLE IF EXISTS likes ;
DROP TABLE IF EXISTS comments ;
DROP TABLE IF EXISTS posts ;
DROP TABLE IF EXISTS friendships ;
DROP TABLE IF EXISTS profiles ;
DROP TABLE IF EXISTS users ;


-- Drop ENUM Types if they exist
DROP TYPE IF EXISTS friendship_status;
DROP TYPE IF EXISTS post_visibility;
DROP TYPE IF EXISTS notification_type;
DROP TYPE IF EXISTS likeable_type;

-- ===========================
-- ENUMS
-- ===========================

CREATE TYPE friendship_status AS ENUM ('pending', 'accepted', 'blocked');
CREATE TYPE post_visibility AS ENUM ('public', 'friends', 'private');
CREATE TYPE notification_type AS ENUM ('like', 'comment', 'friend_request', 'message');
CREATE TYPE likeable_type AS ENUM ('post', 'comment');


-- ===========================
-- USERS TABLE
-- ===========================

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       email VARCHAR(255) UNIQUE NOT NULL,
                       username VARCHAR(100) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       is_active BOOLEAN DEFAULT true,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);


-- ===========================
-- PROFILES TABLE
-- ===========================

CREATE TABLE profiles (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          user_id UUID UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          full_name VARCHAR(255),
                          bio TEXT,
                          avatar_url VARCHAR(500),
                          cover_photo_url VARCHAR(500),
                          location VARCHAR(255),
                          website VARCHAR(255),
                          birth_date DATE,
                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_profiles_user_id ON profiles(user_id);


-- ===========================
-- FRIENDSHIPS TABLE
-- ===========================

CREATE TABLE friendships (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             requester_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                             recipient_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                             status friendship_status DEFAULT 'pending',
                             created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                             UNIQUE(requester_id, recipient_id),
                             CONSTRAINT no_self_friendship CHECK (requester_id != recipient_id)
    );

CREATE INDEX idx_friendships_requester ON friendships(requester_id, status);
CREATE INDEX idx_friendships_recipient ON friendships(recipient_id, status);
CREATE INDEX idx_friendships_status ON friendships(status) WHERE status = 'accepted';


-- ===========================
-- POSTS TABLE
-- ===========================

CREATE TABLE posts (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       author_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                       content TEXT NOT NULL,
                       image_urls TEXT[] DEFAULT '{}',
                       video_url VARCHAR(500),
                       visibility post_visibility DEFAULT 'public',
                       is_deleted BOOLEAN DEFAULT false,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_posts_author_created ON posts(author_id, created_at DESC);
CREATE INDEX idx_posts_created ON posts(created_at DESC);
CREATE INDEX idx_posts_visibility ON posts(visibility);


-- ===========================
-- COMMENTS TABLE
-- ===========================

CREATE TABLE comments (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          post_id UUID NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
                          author_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          parent_comment_id UUID REFERENCES comments(id) ON DELETE CASCADE,
                          content TEXT NOT NULL,
                          is_deleted BOOLEAN DEFAULT false,
                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_comments_post_created ON comments(post_id, created_at DESC);
CREATE INDEX idx_comments_author ON comments(author_id);
CREATE INDEX idx_comments_parent ON comments(parent_comment_id);


-- ===========================
-- LIKES TABLE
-- ===========================

CREATE TABLE likes (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                       post_id UUID REFERENCES posts(id) ON DELETE CASCADE,
                       comment_id UUID REFERENCES comments(id) ON DELETE CASCADE,
                       likeable_type likeable_type NOT NULL,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT like_has_subject CHECK (
                           (post_id IS NOT NULL AND comment_id IS NULL) OR
                           (post_id IS NULL AND comment_id IS NOT NULL)
                           ),
                       CONSTRAINT like_type_matches CHECK (
                           (likeable_type = 'post' AND post_id IS NOT NULL) OR
                           (likeable_type = 'comment' AND comment_id IS NOT NULL)
                           )
);

CREATE UNIQUE INDEX idx_likes_user_post ON likes(user_id, post_id) WHERE post_id IS NOT NULL;
CREATE UNIQUE INDEX idx_likes_user_comment ON likes(user_id, comment_id) WHERE comment_id IS NOT NULL;
CREATE INDEX idx_likes_post ON likes(post_id);
CREATE INDEX idx_likes_comment ON likes(comment_id);


-- ===========================
-- MESSAGES TABLE
-- ===========================

CREATE TABLE messages (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          sender_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          recipient_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          content TEXT NOT NULL,
                          is_read BOOLEAN DEFAULT false,
                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT no_self_message CHECK (sender_id != recipient_id)
    );

CREATE INDEX idx_messages_recipient_unread ON messages(recipient_id, is_read, created_at DESC);
CREATE INDEX idx_messages_sender ON messages(sender_id, created_at DESC);
CREATE INDEX idx_messages_conversation ON messages(sender_id, recipient_id, created_at DESC);


-- ===========================
-- NOTIFICATIONS TABLE
-- ===========================

CREATE TABLE notifications (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                               actor_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                               type notification_type NOT NULL,
                               related_post_id UUID REFERENCES posts(id) ON DELETE CASCADE,
                               related_comment_id UUID REFERENCES comments(id) ON DELETE CASCADE,
                               is_read BOOLEAN DEFAULT false,
                               created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT no_self_notification CHECK (user_id != actor_id)
    );

CREATE INDEX idx_notifications_user_unread ON notifications(user_id, is_read);
CREATE INDEX idx_notifications_user_created ON notifications(user_id, created_at DESC);
CREATE INDEX idx_notifications_type ON notifications(type);


-- ===========================
-- TRIGGERS (Optional - for updated_at)
-- ===========================
CREATE OR REPLACE FUNCTION update_updated_at() RETURNS TRIGGER AS '
BEGIN
  NEW.updated_at = CURRENT_TIMESTAMP;
  RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at();

CREATE TRIGGER profiles_updated_at BEFORE UPDATE ON profiles
    FOR EACH ROW EXECUTE FUNCTION update_updated_at();

CREATE TRIGGER friendships_updated_at BEFORE UPDATE ON friendships
    FOR EACH ROW EXECUTE FUNCTION update_updated_at();

CREATE TRIGGER posts_updated_at BEFORE UPDATE ON posts
    FOR EACH ROW EXECUTE FUNCTION update_updated_at();

CREATE TRIGGER comments_updated_at BEFORE UPDATE ON comments
    FOR EACH ROW EXECUTE FUNCTION update_updated_at();
