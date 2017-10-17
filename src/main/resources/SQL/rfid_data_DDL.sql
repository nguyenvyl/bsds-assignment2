SELECT count(*) FROM rfid_data_day_1;CREATE TABLE `rfid_data_day_1` (
  `resortID` int(11) NOT NULL,
  `dayNum` int(11) NOT NULL,
  `skierID` int(11) NOT NULL,
  `liftID` int(11) NOT NULL,
  `time` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
