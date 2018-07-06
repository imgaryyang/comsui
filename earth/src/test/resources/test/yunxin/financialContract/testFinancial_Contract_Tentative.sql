SET FOREIGN_KEY_CHECKS=0;

delete from `cash_flow`;

--delete from `ledger_book_shelf`;
delete from `company`;

delete from `account`;

insert into `cash_flow` (`id`,`cash_flow_uuid`,`company_uuid`,`host_account_uuid`,`host_account_no`,`host_account_name`,`counter_account_no`,`counter_account_name`,`transaction_amount`,`issued_amount`) values 
(1,'33fds32fsd324vxfdsf324','33333333tttttter33333','4444fdjskfndjks4232ifdskf','3274832786','zhongtaiyinhang','2131241233213','suidifugongsi',100000,999);

insert into company (`id`,`address`,`full_name`,`short_name`,`uuid`)
values(1,'上海','云南国际信托有限公司','云南信托','41231fkshfj4324');

insert into account (`id`,`account_name`,`account_no`,`bank_name`,`uuid`)

values (1,'银企直连专用账户1','3274832786','工商银行','347326sdfsdf'),
		(2,'银企直连专用账户2','2131241233213','测试','21321fdsfsd32432');








SET FOREIGN_KEY_CHECKS=1;