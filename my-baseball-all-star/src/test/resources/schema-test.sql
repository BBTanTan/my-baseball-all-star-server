DROP TABLE IF EXISTS test_entity;
DROP TABLE IF EXISTS play_share;
DROP TABLE IF EXISTS play_result;
DROP TABLE IF EXISTS team_player;
DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS team;

-- player (선수)
CREATE TABLE player
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    club          VARCHAR(255) NOT NULL,
    position      VARCHAR(255) NOT NULL,
    date_of_birth DATE         NOT NULL,
    score DOUBLE,
    choice_count  BIGINT
);

-- team (게임에 참여한 팀 정보)
CREATE TABLE team
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    total_play_count BIGINT,
    win_play_count   BIGINT,
    created_at       TIMESTAMP(6)
);

-- team_player (팀마다 선수 정보)
CREATE TABLE team_player
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_id   BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    FOREIGN KEY (team_id) REFERENCES team (id) ON DELETE CASCADE,
    FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE CASCADE
);

-- play_result (경기 결과 정보)
CREATE TABLE play_result
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_id            BIGINT       NOT NULL,
    opposing_team_name VARCHAR(255) NOT NULL,
    own_team_score     INTEGER      NOT NULL,
    opposing_team      INTEGER      NOT NULL,
    created_at         TIMESTAMP(6),
    FOREIGN KEY (team_id) REFERENCES team (id) ON DELETE CASCADE
);

-- play_share (공유 정보)
CREATE TABLE play_share
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_id        BIGINT       NOT NULL,
    url            VARCHAR(512) NOT NULL,
    owner_password VARCHAR(255) NOT NULL,
    FOREIGN KEY (team_id) REFERENCES team (id) ON DELETE CASCADE
);

-- test_entity (테스트용 엔티티)
CREATE TABLE test_entity
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    modified_at TIMESTAMP(6) NOT NULL,
    deleted     BOOLEAN
);
