-- IMPORTANT: Requires MySQL version minimum: 5.6.5

DROP TABLE IF EXISTS user_authority;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS authority;
DROP TABLE IF EXISTS oauth_access_token;
DROP TABLE IF EXISTS oauth_refresh_token;
DROP TABLE IF EXISTS `pseudo_user`;

CREATE TABLE user (
  id int(11) NOT NULL AUTO_INCREMENT  PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(50) NOT NULL,
  password VARCHAR(500) NOT NULL,
  activated BOOLEAN DEFAULT FALSE,
  activationkey VARCHAR(50) DEFAULT NULL,
  resetpasswordkey VARCHAR(100) DEFAULT NULL,
  date_created_resetpasswordkey DATETIME DEFAULT NULL,
  enabled BOOLEAN DEFAULT FALSE,
  date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
  date_last_modified DATETIME ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
);

CREATE TABLE authority (
  id int(11) NOT NULL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
  date_last_modified DATETIME ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE user_authority (
  user_id int(11) NOT NULL,
  authority_id int(11) NOT NULL,
  date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
  date_last_modified DATETIME ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user (id),
  FOREIGN KEY (authority_id) REFERENCES authority (id),
  UNIQUE INDEX user_authority_idx_1 (user_id, authority_id)
);

CREATE TABLE oauth_access_token (
  token_id VARCHAR(256) DEFAULT NULL,
  token BLOB,
  authentication_id VARCHAR(256) DEFAULT NULL,
  user_name VARCHAR(256) DEFAULT NULL,
  client_id VARCHAR(256) DEFAULT NULL,
  authentication BLOB,
  refresh_token VARCHAR(256) DEFAULT NULL,
  date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
  date_last_modified DATETIME ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE oauth_refresh_token (
  token_id VARCHAR(256) DEFAULT NULL,
  token BLOB,
  authentication BLOB,
  date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
  date_last_modified DATETIME ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `pseudo_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(500) NOT NULL,
  `activated` tinyint(1) NOT NULL,
  `activationkey` varchar(500) NOT NULL,
  `date_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `activationkey` (`activationkey`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
);
