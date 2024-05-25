CREATE DATABASE `c1ctf` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci';
CREATE USER `c1ctf`@`%` IDENTIFIED WITH mysql_native_password BY 'c1ctf';

GRANT Alter, Alter Routine, Create, Create Routine, Create Temporary Tables, Create View, Delete, Drop, Event, Execute, Grant Option, Index, Insert, Lock Tables, References, Select, Show View, Trigger, Update ON `c1game2021`.* TO `c1game2021`@`%`;
