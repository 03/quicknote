
DROP SEQUENCE node_seq;
CREATE SEQUENCE node_seq START WITH 20 INCREMENT BY 5;
DROP SEQUENCE content_seq;
CREATE SEQUENCE content_seq START WITH 20 INCREMENT BY 5;
DROP SEQUENCE user_seq;
CREATE SEQUENCE user_seq START WITH 20 INCREMENT BY 5;
  
delete from noteusers;
insert into noteusers (id, name, password, role, description) values (1, 'zk', 'zk', 'admin', 'funny guy');
insert into noteusers (id, name, password, role, description) values (2, 'luke', 'feng', 'admin', 'cat lover');

delete from notecontents;
delete from notenodes;

insert into notenodes (id,content,created,iconurl,name,ownerId,parentId,status,updated) values (1,'ROOT(invisible) of user [1]',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft1.gif','ROOT','1',NULL,'a',NULL);
insert into notenodes (id,content,created,iconurl,name,ownerId,parentId,status,updated) values (2,'hi there',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft2.gif','second topic ','1',1,'a',NULL);
insert into notenodes (id,content,created,iconurl,name,ownerId,parentId,status,updated) values (3,'lovely day',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft3.gif','third topic','1',1,'a',NULL);
insert into notenodes (id,content,created,iconurl,name,ownerId,parentId,status,updated) values (4,'this is a demo',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft4.gif','another topic','1',2,'a',NULL);
insert into notenodes (id,content,created,iconurl,name,ownerId,parentId,status,updated) values (5,'Hello Alan',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft1.gif','my topic','1',3,'a',NULL);
insert into notenodes (id,content,created,iconurl,name,ownerId,parentId,status,updated) values (6,'6demo',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft2.gif','a topic ','1',2,'a',NULL);
insert into notenodes (id,content,created,iconurl,name,ownerId,parentId,status,updated) values (7,'7demo',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft3.gif','b topic','1',4,'a',NULL);
insert into notenodes (id,content,created,iconurl,name,ownerId,parentId,status,updated) values (8,'8demo',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft4.gif','c topic','1',3,'a',NULL);

insert into notecontents (id,noteid,text,created) values (1, 2, 'hi there 2',CURRENT_TIMESTAMP);
insert into notecontents (id,noteid,text,created) values (2, 3, 'hi there 3',CURRENT_TIMESTAMP);
insert into notecontents (id,noteid,text,created) values (3, 4, 'hi there 4',CURRENT_TIMESTAMP);
insert into notecontents (id,noteid,text,created) values (4, 5, 'hi there 5',CURRENT_TIMESTAMP);
insert into notecontents (id,noteid,text,created) values (5, 6, 'hi there 6',CURRENT_TIMESTAMP);
insert into notecontents (id,noteid,text,created) values (6, 7, 'hi there 7',CURRENT_TIMESTAMP);
insert into notecontents (id,noteid,text,created) values (7, 8, 'hi there 8',CURRENT_TIMESTAMP);
