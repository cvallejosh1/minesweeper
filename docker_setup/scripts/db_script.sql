CREATE TABLE `game` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `board` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cols` int(11) NOT NULL,
  `mines` int(11) NOT NULL,
  `rows` int(11) NOT NULL,
  `game_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjvp5ja1iics8qkf8de4wdrsvg` (`game_id`),
  CONSTRAINT `FKjvp5ja1iics8qkf8de4wdrsvg` FOREIGN KEY (`game_id`) REFERENCES `game` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `cell` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `col_index` int(11) NOT NULL,
  `flagged` bit(1) NOT NULL,
  `mined` bit(1) NOT NULL,
  `mines_around` int(11) NOT NULL,
  `revealed` bit(1) NOT NULL,
  `row_index` int(11) NOT NULL,
  `board_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKffulw2tvhohjxm8qxh7hko046` (`board_id`),
  CONSTRAINT `FKffulw2tvhohjxm8qxh7hko046` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


