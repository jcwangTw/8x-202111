DROP TABLE IF EXISTS order_storage;

create table order_storage (
    id bigint not null ,
    storage_id varchar(255),
    order_number varchar(255),
    primary key (id)
);

INSERT INTO order_storage (id, storage_id, order_number)
VALUES (1, '1', 'orderNumber');