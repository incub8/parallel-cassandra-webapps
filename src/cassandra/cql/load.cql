create keyspace greetingkeyspace
with replication = {
    'class': 'SimpleStrategy',
    'replication_factor': 1
};

create table greetingkeyspace.greetings (
    id text primary key,
    greeting text
);

insert into greetingkeyspace.greetings (
    id,
    greeting
) values (
    'greetingId',
    'Hello world!'
);