CREATE DATABASE IF NOT EXISTS 'movies';

DROP TABLE IF EXISTS album_scheduler_task;

CREATE TABLE album_scheduler_task (started_at TIMESTAMP NULL DEFAULT NULL);

INSERT INTO album_scheduler_task (started_at) VALUES (CURRENT_TIMESTAMP);

SELECT * from album_scheduler_task where started_at<NOW() - INTERVAL 3 MINUTE;

UPDATE album_scheduler_task SET started_at = CURRENT_TIMESTAMP WHERE started_at<NOW() - INTERVAL 3 MINUTE;
