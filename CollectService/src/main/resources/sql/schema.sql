drop database if exists litemall;
drop user if exists 'litemallUser'@'%';
create database litemall default character set utf8mb4 collate utf8mb4_unicode_ci;
use litemall;
create user 'litemallUser'@'%' identified by '123456';
grant all privileges on litemall.* to 'litemallUser'@'%';
flush privileges;