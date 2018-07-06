SET FOREIGN_KEY_CHECKS=0;
	update t_file_repository set status=3 where status in (0,1,2); 

SET FOREIGN_KEY_CHECKS=1;