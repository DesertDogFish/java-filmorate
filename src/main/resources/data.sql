delete from users;
delete from films;

alter table users alter column id restart with 1;
alter table films alter column id restart with 1;

merge into ratings_mpa (id, name)
    values (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

merge into genres (id, name)
    values (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');