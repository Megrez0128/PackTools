CREATE TABLE IF NOT EXISTS flow(record_id int primary key, flow_id int, version int, committed boolean, commit_message varchar(511),last_build varchar(511), meta_id int, graph_data varchar(1023), blackboard varchar(1023));
CREATE TABLE IF NOT EXISTS flow_summary(flow_id int primary key,name varchar(255), des varchar(511), last_build varchar(511), last_commit varchar(511), last_version int);
CREATE TABLE IF NOT EXISTS pack_meta (meta_id int primary key, version int, group_id int, version_display varchar(255), data varchar(255));
CREATE TABLE IF NOT EXISTS pack_group (group_id int primary key, group_name varchar(255) unique);
CREATE TABLE IF NOT EXISTS instance (uuid varchar(255) primary key, flow_record_id int, build_time varchar(255), complete boolean, has_error boolean);
CREATE TABLE IF NOT EXISTS node (instance_id varchar(255), node_id int primary key, start_time varchar(255), end_time varchar(255), options varchar(8191));
CREATE TABLE IF NOT EXISTS pack_user (user_id varchar(255) primary key, admin boolean);
CREATE TABLE IF NOT EXISTS administration (update_allowed boolean, delete_allowed boolean, user_id varchar(255), group_id int, primary key(user_id ,group_id));
CREATE TABLE IF NOT EXISTS group_flow (group_id int, flow_id int, primary key(group_id, flow_id));
