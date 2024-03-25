---删除权限集合映射
DELETE FROM uaa_ii_auth_collection_mapping WHERE collection_id =(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-只读')
AND auth_id= (SELECT MIN(ID) FROM uaa_ii_authorization WHERE URL='/flow/exportFlow.do');
DELETE FROM uaa_ii_auth_collection_mapping WHERE collection_id =(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-只读')
AND auth_id= (SELECT MIN(ID) FROM uaa_ii_authorization WHERE URL='/flow/uploadFlow.do');
DELETE FROM uaa_ii_auth_collection_mapping WHERE collection_id =(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-只读')
AND auth_id= (SELECT MIN(ID) FROM uaa_ii_authorization WHERE URL='/flow/batchDeleteFlow.do');
DELETE FROM uaa_ii_auth_collection_mapping WHERE collection_id =(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-只读')
AND auth_id= (SELECT MIN(ID) FROM uaa_ii_authorization WHERE URL='/flow/expandFlow.do');
DELETE FROM uaa_ii_auth_collection_mapping WHERE collection_id =(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-只读')
AND auth_id= (SELECT MIN(ID) FROM uaa_ii_authorization WHERE URL='/flow/deleteFlow.do');
DELETE FROM uaa_ii_auth_collection_mapping WHERE collection_id =(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-只读')
AND auth_id= (SELECT MIN(ID) FROM uaa_ii_authorization WHERE URL='/flow/saveFlow.do');
DELETE FROM uaa_ii_auth_collection_mapping WHERE collection_id =(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-只读')
AND auth_id= (SELECT MIN(ID) FROM uaa_ii_authorization WHERE URL='/flow/queryFlowJson.do');
DELETE FROM uaa_ii_auth_collection_mapping WHERE collection_id =(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-只读')
AND auth_id= (SELECT MIN(ID) FROM uaa_ii_authorization WHERE URL='/flow/queryFlow.do');
DELETE FROM uaa_ii_auth_collection_mapping WHERE collection_id =(SELECT MIN(id) FROM uaa_ii_auth_collection WHERE system_id='PCMS-ADMIN' AND NAME='gpt流管理-只读')
AND auth_id= (SELECT MIN(ID) FROM uaa_ii_authorization WHERE URL='/flow/manageFlow.do');


---删除权限集合
DELETE FROM uaa_ii_auth_collection where system_id='PCMS-ADMIN' AND name='gpt流管理-只读' AND description='gpt流管理-只读';
DELETE FROM uaa_ii_auth_collection where system_id='PCMS-ADMIN' AND name='gpt流管理-读写' AND description='gpt流管理-读写';

---删除权限
DELETE FROM uaa_ii_authorization WHERE system_id='PCMS-ADMIN' AND TYPE='REQUEST_URL' AND resource='/flow/manageFlow.do' AND description='gpt流管理';
DELETE FROM uaa_ii_authorization WHERE system_id='PCMS-ADMIN' AND TYPE='REQUEST_URL' AND resource='/flow/queryFlow.do' AND description='gpt流查询';
DELETE FROM uaa_ii_authorization WHERE system_id='PCMS-ADMIN' AND TYPE='REQUEST_URL' AND resource='/flow/queryFlowJson.do' AND description='gpt流查询';
DELETE FROM uaa_ii_authorization WHERE system_id='PCMS-ADMIN' AND TYPE='REQUEST_URL' AND resource='/flow/saveFlow.do' AND description='gpt流保存';
DELETE FROM uaa_ii_authorization WHERE system_id='PCMS-ADMIN' AND TYPE='REQUEST_URL' AND resource='/flow/deleteFlow.do' AND description='gpt流删除';
DELETE FROM uaa_ii_authorization WHERE system_id='PCMS-ADMIN' AND TYPE='REQUEST_URL' AND resource='/flow/expandFlow.do' AND description='gpt流扩展查询';
DELETE FROM uaa_ii_authorization WHERE system_id='PCMS-ADMIN' AND TYPE='REQUEST_URL' AND resource='/flow/batchDeleteFlow.do' AND description='gpt流批量删除';
DELETE FROM uaa_ii_authorization WHERE system_id='PCMS-ADMIN' AND TYPE='REQUEST_URL' AND resource='/flow/uploadFlow.do' AND description='gpt流上传';
DELETE FROM uaa_ii_authorization WHERE system_id='PCMS-ADMIN' AND TYPE='REQUEST_URL' AND resource='/flow/exportFlow.do' AND description='gpt流导出';


