
DROP SEQUENCE node_seq;
CREATE SEQUENCE node_seq START WITH 20 INCREMENT BY 5;
DROP SEQUENCE user_seq;
CREATE SEQUENCE user_seq START WITH 20 INCREMENT BY 5;


delete from noteusers;
delete from notenodes;

insert into notenodes (id,tag,created,iconurl,name,parentId,status,updated,text) values (1,'ROOT(invisible) of userid [1]',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft1.gif','ROOT',NULL,'a',NULL,'hi there 1');
insert into notenodes (id,tag,created,iconurl,name,parentId,status,updated,text) values (2,'hi there',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft2.gif','second topic ',1,'a',NULL,'hi there 2');
insert into notenodes (id,tag,created,iconurl,name,parentId,status,updated,text) values (3,'lovely day',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft3.gif','third topic',1,'a',NULL,'hi there 3');
insert into notenodes (id,tag,created,iconurl,name,parentId,status,updated,text) values (4,'this is a demo',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft4.gif','another topic',2,'a',NULL,'hi there 4');
insert into notenodes (id,tag,created,iconurl,name,parentId,status,updated,text) values (5,'Hello Alan',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft1.gif','my topic',3,'a',NULL,'hi there 5');
insert into notenodes (id,tag,created,iconurl,name,parentId,status,updated,text) values (6,'6demo',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft2.gif','a topic ',2,'a',NULL,'hi there 6');
insert into notenodes (id,tag,created,iconurl,name,parentId,status,updated,text) values (7,'7demo',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft3.gif','b topic',4,'a',NULL,'hi there 7');
insert into notenodes (id,tag,created,iconurl,name,parentId,status,updated,text) values (8,'8demo',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft4.gif','c topic',3,'a',NULL,'hi there 8');

insert into notenodes (id,tag,created,iconurl,name,parentId,status,updated,text) values (20,'ROOT(invisible) of userid [2]',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft1.gif','ROOT',NULL,'a',NULL,'hi there 20');

insert into noteusers (id, name, password, role, description, rootid) values (1, 'zk', 'zk', 'admin', 'funny guy', 1);
insert into noteusers (id, name, password, role, description, rootid) values (2, 'luke', 'feng', 'admin', 'cat lover', 20);

