INSERT INTO player (name, club, position, score, date_of_birth, profile_url)
VALUES ('원태인', 'SAMSUNG_LIONS', 'STARTING_PITCHER', 54.66, '2000-04-06',
        'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/middle/2025/69446.jpg'),
       ('김도영', 'KIA_TIGERS', 'MIDDLE_PITCHER', 40.12, '2002-07-15',
        'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/middle/2025/99901.jpg'),
       ('양의지', 'DOOSAN_BEARS', 'CLOSER_PITCHER', 65.23, '1987-06-05',
        'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/middle/2025/99902.jpg'),
       ('오재일', 'SAMSUNG_LIONS', 'CATCHER', 50.32, '1986-10-29',
        'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/middle/2025/99903.jpg'),
       ('안치홍', 'LOTTE_GIANTS', 'FIRST_BASE', 48.76, '1990-07-02',
        'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/middle/2025/99904.jpg'),
       ('허경민', 'DOOSAN_BEARS', 'SECOND_BASE', 52.14, '1990-11-15',
        'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/middle/2025/99905.jpg'),
       ('김하성', 'KIWOOM_HEROES', 'THIRD_BASE', 70.89, '1995-10-17',
        'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/middle/2025/99906.jpg'),
       ('이정후', 'KIWOOM_HEROES', 'SHORTSTOP', 72.35, '1998-08-20',
        'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/middle/2025/99907.jpg'),
       ('박건우', 'NC_DINOS', 'OUT_FIELD', 60.23, '1990-09-08',
        'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/middle/2025/99908.jpg'),
       ('홍창기', 'LG_TWINS', 'OUT_FIELD', 58.70, '1993-10-10',
        'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/middle/2025/99909.jpg'),
        ('김영웅', 'SAMSUNG_LIONS', 'OUT_FIELD', 0, '2003-08-24',
        'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/middle/2025/52430.jpg'),
       ('임종성', 'DOOSAN_BEARS', 'DESIGNATED_HITTER', 0, '2005-03-03',
        'https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/middle/2025/54236.jpg');

INSERT INTO player_choice_count (player_id, count, version)
VALUES (1, 0, 0),
       (2, 0, 0),
       (3, 0, 0),
       (4, 0, 0),
       (5, 0, 0),
       (6, 0, 0),
       (7, 0, 0),
       (8, 0, 0),
       (9, 0, 0),
       (10, 0, 0),
       (11, 0, 0),
       (12, 0, 0);

INSERT INTO team (name, total_play_count, win_play_count, created_at)
VALUES ('TestTeam', 0, 0, NOW());

INSERT INTO team_player (team_id, player_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10),
       (1, 11),
       (1, 12);


