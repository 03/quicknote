
DROP SEQUENCE node_seq;
CREATE SEQUENCE node_seq START WITH 100 INCREMENT BY 5;
DROP SEQUENCE user_seq;
CREATE SEQUENCE user_seq START WITH 100 INCREMENT BY 5;

delete from note_users;
delete from note_nodes;

insert into note_nodes (id,tag,created,icon_url,name,pid,status,text) values (1,'ROOT(invisible) of userid [1]',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft1.gif','ROOT',NULL,'a','hi there 1');
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text) values (2,'',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft2.gif','second topic ',1,'a','hi there 2');
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text) values (3,'',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft3.gif','third topic',1,'a','hi there 3');
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text) values (4,'',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft4.gif','another topic',2,'a','hi there 4');
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text) values (5,'',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft1.gif','my topic',3,'a','hi there 5');
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text) values (6,'',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft2.gif','a topic ',2,'a','hi there 6');
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text) values (7,'',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft3.gif','b topic',4,'a','hi there 7');
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text) values (8,'',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft4.gif','c topic',3,'a','hi there 8');

insert into note_nodes (id,tag,created,icon_url,name,pid,status,text) values (20,'ROOT(invisible) of userid [2]',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft1.gif','ROOT',NULL,'a','hi there 20');
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text) values (30,'ROOT(invisible) of userid [3]',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft1.gif','ROOT',NULL,'a','hi there 30');

insert into note_users (id, login_name, first_name, last_name, password, role, description, root_id) values (1, 'zk', 'Oliver', 'Queen', 'zk', 'admin', 'funny guy', 1);
insert into note_users (id, login_name, first_name, last_name, password, role, description, root_id) values (2, 'luke', 'Luke', 'Feng', 'still', 'admin', 'cat lover', 20);
insert into note_users (id, login_name, first_name, last_name, password, role, description, root_id) values (3, 'Shado', 'Celina', 'Jade', 'Speedy', 'admin', 'meat lover', 30);
