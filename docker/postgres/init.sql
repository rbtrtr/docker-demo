create schema jpa_sample;

create table jpa_sample.player (
id int,
name varchar(50),
num int,
position varchar(50),
team_id int);

create table jpa_sample.team (
	id int,
	name varchar(50)
);

create sequence jpa_sample.TEAM_SEQ;
create sequence jpa_sample.PLAYER_SEQ;

insert into jpa_sample.team values(1, 'sample_team');
insert into jpa_sample.player values(1, 'foo bar', 99, 'keeper', 1);