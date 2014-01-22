
DROP SEQUENCE node_seq;
CREATE SEQUENCE node_seq START WITH 100 INCREMENT BY 5;
DROP SEQUENCE user_seq;
CREATE SEQUENCE user_seq START WITH 100 INCREMENT BY 5;

delete from note_users;
delete from note_nodes;

insert into note_nodes (id,tag,created,icon_url,name,pid,status,text,sorting) values (1,'ROOT(invisible) of userid [1]',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft1.gif','ROOT',NULL,'a','hi there 1',0);
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text,sorting) values (2,'',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft2.gif','second topic ',1,'a','hi there 2',100);
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text,sorting) values (3,'',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft3.gif','third topic',1,'a','hi there 3',200);
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text,sorting) values (4,'',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft4.gif','another topic',2,'a','hi there 4',100);
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text,sorting) values (5,'',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft1.gif','my topic',3,'a','hi there 5',100);
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text,sorting) values (6,'',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft2.gif','a topic ',2,'a','hi there 6',200);
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text,sorting) values (7,'',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft3.gif','b topic',4,'a','hi there 7',100);
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text,sorting) values (8,'',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft4.gif','c topic',3,'a','hi there 8 (2)',300);
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text,sorting) values (9,'',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft1.gif','c topic',3,'a','hi there 8 (1)',200);

insert into note_nodes (id,tag,created,icon_url,name,pid,status,text,sorting) values (20,'ROOT(invisible) of userid [2]',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft1.gif','ROOT',NULL,'a','hi there 20',0);
insert into note_nodes (id,tag,created,icon_url,name,pid,status,text,sorting) values (30,'ROOT(invisible) of userid [3]',CURRENT_TIMESTAMP,'/assets/images/filetypes/ft1.gif','ROOT',NULL,'a','hi there 30',0);

insert into note_users (id,login_name,first_name,last_name,password,role,description,root_id,icon_url) values (1,'zk','Oliver','Queen','zk','Premium','funny guy',1,'/assets/images/m2.gif');
insert into note_users (id,login_name,first_name,last_name,password,role,description,root_id,icon_url) values (2,'luke','Luke','Feng','feng','Admin','cat lover',20,'/assets/images/m1.gif');
insert into note_users (id,login_name,first_name,last_name,password,role,description,root_id,icon_url) values (3,'lv','Peter','','vl','Standard','meat lover',30,'/assets/images/user.png');
