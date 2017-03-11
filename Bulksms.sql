
CREATE TABLE IF NOT EXISTS `bulksms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `names` varchar(30) NOT NULL,
  `phone` int(11) NOT NULL,
  `email` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;
