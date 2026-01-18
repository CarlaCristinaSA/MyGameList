DROP TABLE IF EXISTS `game`;

CREATE TABLE `game` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `developer` varchar(80) NOT NULL,
  `name` varchar(80) NOT NULL,
  `year` bigint NOT NULL,
  `star_rating` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
)
