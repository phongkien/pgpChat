create table pgpchat.users (
    user_id		int not null,
    username	varchar(32) not null,
    password	varchar(100) not null,
    email		varchar(100) not null,
    first_name	varchar(32) not null,
    last_name	varchar(32) not null,
    confirmed	char(1) not null default 'N',
    verification_code	varchar(32),
    primary key(user_id),
    unique(email)
);

create table pgpchat.roles (
	role	varchar(12) not null,
	primary key (role)
);

create table pgpchat.user_role (
	user_id		int not null,
	role		varchar(12),
	unique(user_id, role),
	foreign key (user_id) references pgpchat.users(user_id) on delete cascade,
	foreign key (role) references pgpchat.roles(role) on delete cascade
);

create table pgpchat.white_list (
	email	varchar(100) not null,
	primary key(email)
);

grant insert, delete, select, update on pgpchat.users to pgpchat;
grant insert, delete, select, update on pgpchat.user_role to pgpchat;
grant select on pgpchat.white_list to pgpchat;

insert into pgpchat.roles(role) values('USER_ROLE');
insert into pgpchat.roles(role) values('ADMIN_ROLE');
insert into pgpchat.roles(role) values('ROLE_USER');
insert into pgpchat.roles(role) values('ROLE_ADMIN');

insert into pgpchat.user_role(user_id, role) values(1, 'ROLE_USER');
insert into pgpchat.user_role(user_id, role) values(1, 'USER_ROLE');
insert into pgpchat.white_list(email) values('phongkien@gmail.com');
insert into pgpchat.white_list(email) values('city728@gmail.com');

commit;