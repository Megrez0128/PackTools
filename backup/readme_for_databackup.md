### 项目数据库维护说明
数据库初始化命令参考`init.sql`。

#### 数据备份
本项目没有设置默认的数据备份，需要备份与恢复时请参考下述操作：

本地备份指令：`mysqldump -h 127.0.0.1 -P 3306 -u root -p test_user < test.sql`

其中：127.0.0.1表示在后端运行的机器上进行备份操作，3306为MySQL默认接口，登录后用户拥有所有权限。`test_user`为暂时使用的数据库名，`test.sql`为用于存储数据备份的文件名，可以添加相对/绝对路径，默认存储位置为该指令运行的根目录。

用户名：root

用户密码：root

备份恢复：对待恢复的文件，打开`MySQL`命令行，切换到目标数据库，使用指令`SOURCE /path/test.sql`；注意，需要使用绝对路径。

#### sql文件优化
直接使用`mysqldump`工具导出的`.sql`文件语法格式存在一定问题，可以在备份之后使用`sed`修改，方法如下：

原代码：
```mysql
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` varchar(20) NOT NULL,
  `admin` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
)
```
目标代码：
```mysql
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` varchar(20) NOT NULL,
  `admin` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
)
```

使用sed指令（假设待处理文件为当前根目录下的`test.sql`）：
```shell
sed -i '/DROP TABLE/d' test.sql
sed -i 's/CREATE TABLE/CREATE TABLE IF NOT EXISTS/g' test.sql
```
完成批量替换。