
insert into uaa_ii_auth_collection (system_id,name,description) values('PCMS-ADMIN','gpt流管理-只读','gpt流管理-只读');
insert into uaa_ii_auth_collection (system_id,name,description) values('PCMS-ADMIN','gpt流管理-读写','gpt流管理-读写');


INSERT INTO uaa_ii_authorization (system_id,TYPE,resource,description) VALUES('PCMS-ADMIN','REQUEST_URL','/flow/manageFlow.do','gpt流管理');
INSERT INTO uaa_ii_authorization (system_id,TYPE,resource,description) VALUES('PCMS-ADMIN','REQUEST_URL','/flow/queryFlow.do','gpt流查询');
INSERT INTO uaa_ii_authorization (system_id,TYPE,resource,description) VALUES('PCMS-ADMIN','REQUEST_URL','/flow/queryFlowJson.do','gpt流查询Json');
INSERT INTO uaa_ii_authorization (system_id,TYPE,resource,description) VALUES('PCMS-ADMIN','REQUEST_URL','/flow/saveFlow.do','gpt流保存');
INSERT INTO uaa_ii_authorization (system_id,TYPE,resource,description) VALUES('PCMS-ADMIN','REQUEST_URL','/flow/deleteFlow.do','gpt流删除');
INSERT INTO uaa_ii_authorization (system_id,TYPE,resource,description) VALUES('PCMS-ADMIN','REQUEST_URL','/flow/expandFlow.do','gpt流扩展查询');
INSERT INTO uaa_ii_authorization (system_id,TYPE,resource,description) VALUES('PCMS-ADMIN','REQUEST_URL','/flow/batchDeleteFlow.do','gpt流批量删除');
INSERT INTO uaa_ii_authorization (system_id,TYPE,resource,description) VALUES('PCMS-ADMIN','REQUEST_URL','/flow/uploadFlow.do','gpt流上传');
INSERT INTO uaa_ii_authorization (system_id,TYPE,resource,description) VALUES('PCMS-ADMIN','REQUEST_URL','/flow/exportFlow.do','gpt流导出');


INSERT INTO uaa_ii_auth_collection_mapping (collection_id,auth_id) values (
(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-只读'),
(SELECT MIN(ID) FROM uaa_ii_authorization WHERE resource='/flow/manageFlow.do')
);
INSERT INTO uaa_ii_auth_collection_mapping (collection_id,auth_id) values (
(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-只读'),
(SELECT MIN(ID) FROM uaa_ii_authorization WHERE resource='/flow/queryFlow.do')
);
INSERT INTO uaa_ii_auth_collection_mapping (collection_id,auth_id) values (
(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-只读'),
(SELECT MIN(ID) FROM uaa_ii_authorization WHERE resource='/flow/queryFlowJson.do')
);
INSERT INTO uaa_ii_auth_collection_mapping (collection_id,auth_id) values (
(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-读写'),
(SELECT MIN(ID) FROM uaa_ii_authorization WHERE resource='/flow/saveFlow.do')
);
INSERT INTO uaa_ii_auth_collection_mapping (collection_id,auth_id) values (
(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-读写'),
(SELECT MIN(ID) FROM uaa_ii_authorization WHERE resource='/flow/deleteFlow.do')
);
INSERT INTO uaa_ii_auth_collection_mapping (collection_id,auth_id) values (
(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-读写'),
(SELECT MIN(ID) FROM uaa_ii_authorization WHERE resource='/flow/expandFlow.do')
);
INSERT INTO uaa_ii_auth_collection_mapping (collection_id,auth_id) values (
(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-读写'),
(SELECT MIN(ID) FROM uaa_ii_authorization WHERE resource='/flow/batchDeleteFlow.do')
);
INSERT INTO uaa_ii_auth_collection_mapping (collection_id,auth_id) values (
(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-读写'),
(SELECT MIN(ID) FROM uaa_ii_authorization WHERE resource='/flow/uploadFlow.do')
);
INSERT INTO uaa_ii_auth_collection_mapping (collection_id,auth_id) values (
(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-只读'),
(SELECT MIN(ID) FROM uaa_ii_authorization WHERE resource='/flow/exportFlow.do')
);


insert into uaa_ii_role_authcollection_mapping (role_code,authcollection_id,mapping_type) values ('ROLE_ADMIN',(SELECT MIN(ID) FROM uaa_ii_auth_collection WHERE NAME='gpt流管理-只读'),'C');
insert into uaa_ii_role_authcollection_mapping (role_code,authcollection_id,mapping_type) values ('ROLE_ADMIN',(SELECT MIN(ID) FROM uaa_ii_auth_collection WHERE NAME='gpt流管理-读写'),'C');



INSERT INTO md_t_menu(NAME,parent_id,LEVEL,url,state,remarks) VALUES ('gpt流管理',0,1,NULL,'0','00');

INSERT INTO md_t_role_menu(role_id,menu_id) VALUES('ROLE_ADMIN',IFNULL((SELECT MIN(id) AS menu_id FROM md_t_menu WHERE NAME='gpt流管理'),0));


INSERT INTO md_t_menu(NAME,parent_id,LEVEL,url,state,remarks) VALUES ('gpt流管理',(SELECT MIN(t.id) FROM  md_t_menu AS t WHERE NAME ='gpt流管理' LIMIT 1),2,NULL,'0','00');

INSERT INTO md_t_role_menu(role_id,menu_id) VALUES('ROLE_ADMIN',IFNULL((SELECT MIN(id) AS menu_id FROM md_t_menu WHERE NAME='gpt流管理'),0));


INSERT INTO md_t_menu(NAME,parent_id,LEVEL,url,state,remarks) VALUES ('gpt流管理',(SELECT MIN(t.id) FROM  md_t_menu AS t WHERE NAME ='gpt流管理' LIMIT 1),3,'flow/manageFlow.do','0','00');

INSERT INTO md_t_role_menu(role_id,menu_id) VALUES('ROLE_ADMIN',IFNULL((SELECT MIN(id) AS menu_id FROM md_t_menu WHERE NAME='gpt流管理'),0));

