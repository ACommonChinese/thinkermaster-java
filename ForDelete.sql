# 往 user 中插入数据
INSERT INTO user VALUES(null, '老王', '2018-02-27 17:23:09 ', '1', '河南郑州');
INSERT INTO user VALUES (null, '老王', '2018-02-27 17:23:09', '1', '河南郑州');
INSERT INTO user VALUES (null, '大刘', '1998-02-27 09:23:09', '1', '中国香港');
INSERT INTO user VALUES (null, '张三', '2018-02-27 17:23:09 ', '0', '辽宁沈阳');
INSERT INTO user VALUES (null, '李四', '2018-01-27 17:23:09 ', '0', '山东济南');
INSERT INTO user VALUES (null, '李八皮', '2013-02-27 17:23:09 ', '1', '湖北武汉');
UPDATE user SET address='美国阿拉斯加' WHERE username='李八皮';

# Logs表格创建
CREATE TABLE Logs(
    id int(3) NOT NULL AUTO_INCREMENT,
    message varchar(10) DEFAULT NULL,
    PRIMARY KEY(id)
);