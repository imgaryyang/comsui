SET FOREIGN_KEY_CHECKS = 0;

USE galaxy_autotest_yunxin;
DELETE FROM contract;
DELETE FROM contract_account;
DELETE FROM company;
DELETE FROM app;
DELETE FROM house;
DELETE FROM customer;
DELETE FROM province;
DELETE FROM city;
DELETE FROM bank;
DELETE FROM principal;
DELETE FROM t_interface_repayment_information_log;

INSERT INTO t_api_config (api_url, fn_code, description, api_status)
VALUES
  ('/api/v3/modifyRepaymentInformation', NULL, '变更还款信息', 1);

INSERT INTO principal (id, authority, name, password, start_date, thru_date, t_user_id, created_time, creator_id, modify_password_time)
VALUES (1, 'ROLE_SUPER_USER', 'zhanghongbing', '376c43878878ac04e05946ec1dd7a55f', NULL, NULL, 2, NULL, NULL, 1);
INSERT INTO company (id, address, full_name, short_name, uuid)
VALUES
  (1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839');
INSERT INTO app (id, app_id, app_secret, is_disabled, host, name, company_id, addressee)
VALUES
  (1, 'qyb', NULL, 1, NULL, '测试商户yqb', 1, NULL);
INSERT INTO house (id, address, app_id)
VALUES
  (1, 'newyork', '1');
INSERT INTO customer (id, app_id)
VALUES
  (1, '1');
INSERT INTO contract (id, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest)
VALUES
  (323, '12345678', '2016-04-17', '云信信2016-78-DK(ZQ2016042522479)', NULL, '1', '0.00', '1', '1', '1', NULL,
   '2016-06-15 22:04:59', '0.1560000000', '0', '0', '3', '2', '1200.00', '0.0005000000');

INSERT INTO contract_account (id, pay_ac_no, bankcard_type, contract_id, payer_name, bank, bind_id, id_card_num, bank_code, province, city, from_date, thru_date)
VALUES
  (323, '6217000000000003006', NULL, '323', '测试用户1', '中国邮政储蓄银行', NULL, NULL, '403', '安徽省', '亳州', '2016-07-13 10:03:25',
   '2016-07-13 10:21:26'),
  (348, 'bankAccount', NULL, '323', '测试用户1', '中国邮政储蓄银行', NULL, NULL, '604', '安徽省', '亳州', '2016-07-13 10:21:26',
   '2900-01-01 00:00:00');

INSERT INTO province (id, code, is_deleted, name) VALUES ('1', '330000', b'0', '浙江省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('2', '120000', b'0', '天津市');
INSERT INTO province (id, code, is_deleted, name) VALUES ('3', '130000', b'0', '河北省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('4', '140000', b'0', '山西省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('5', '150000', b'0', '内蒙古');
INSERT INTO province (id, code, is_deleted, name) VALUES ('6', '210000', b'0', '辽宁省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('7', '220000', b'0', '吉林省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('8', '230000', b'0', '黑龙江');
INSERT INTO province (id, code, is_deleted, name) VALUES ('9', '310000', b'0', '上海市');
INSERT INTO province (id, code, is_deleted, name) VALUES ('10', '320000', b'0', '江苏省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('11', '110000', b'0', '北京市');
INSERT INTO province (id, code, is_deleted, name) VALUES ('12', '340000', b'0', '安徽省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('13', '350000', b'0', '福建省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('14', '360000', b'0', '江西省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('15', '370000', b'0', '山东省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('16', '410000', b'0', '河南省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('17', '420000', b'0', '湖北省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('18', '430000', b'0', '湖南省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('19', '440000', b'0', '广东省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('20', '450000', b'0', '广西');
INSERT INTO province (id, code, is_deleted, name) VALUES ('21', '460000', b'0', '海南省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('22', '500000', b'0', '重庆市');
INSERT INTO province (id, code, is_deleted, name) VALUES ('23', '510000', b'0', '四川省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('24', '520000', b'0', '贵州省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('25', '530000', b'0', '云南省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('26', '540000', b'0', '西藏');
INSERT INTO province (id, code, is_deleted, name) VALUES ('27', '610000', b'0', '陕西省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('28', '620000', b'0', '甘肃省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('29', '630000', b'0', '青海省');
INSERT INTO province (id, code, is_deleted, name) VALUES ('30', '640000', b'0', '宁夏');
INSERT INTO province (id, code, is_deleted, name) VALUES ('31', '650000', b'0', '新疆');

INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('1', '110100', b'0', '北京市', '110000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('2', '130100', b'0', '石家庄市', '130000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('3', '130200', b'0', '唐山市', '130000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('4', '130300', b'0', '秦皇岛市', '130000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('5', '130400', b'0', '邯郸市', '130000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('6', '130500', b'0', '邢台市', '130000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('7', '130600', b'0', '保定市', '130000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('8', '130700', b'0', '张家口市', '130000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('9', '130800', b'0', '承德市', '130000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('10', '130900', b'0', '沧州市', '130000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('11', '131000', b'0', '廊坊市', '130000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('12', '131100', b'0', '衡水市', '130000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('13', '140100', b'0', '太原市', '140000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('14', '140200', b'0', '大同市', '140000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('15', '140300', b'0', '阳泉市', '140000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('16', '140400', b'0', '长治市', '140000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('17', '140500', b'0', '晋城市', '140000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('18', '140600', b'0', '朔州市', '140000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('19', '140700', b'0', '晋中市', '140000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('20', '140800', b'0', '运城市', '140000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('21', '140900', b'0', '忻州市', '140000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('22', '141000', b'0', '临汾市', '140000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('23', '141100', b'0', '吕梁市', '140000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('24', '150100', b'0', '呼和浩特市', '150000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('25', '150200', b'0', '包头市', '150000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('26', '150300', b'0', '乌海市', '150000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('27', '150400', b'0', '赤峰市', '150000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('28', '150500', b'0', '通辽市', '150000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('29', '150600', b'0', '鄂尔多斯市', '150000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('30', '150700', b'0', '呼伦贝尔市', '150000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('31', '150800', b'0', '巴彦淖尔市', '150000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('32', '150900', b'0', '乌兰察布市', '150000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('33', '152200', b'0', '兴安盟', '150000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('34', '152500', b'0', '锡林郭勒盟', '150000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('35', '152900', b'0', '阿拉善盟', '150000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('36', '210100', b'0', '沈阳市', '210000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('37', '210200', b'0', '大连市', '210000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('38', '210300', b'0', '鞍山市', '210000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('39', '210400', b'0', '抚顺市', '210000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('40', '210500', b'0', '本溪市', '210000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('41', '210600', b'0', '丹东市', '210000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('42', '210700', b'0', '锦州市', '210000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('43', '210800', b'0', '营口市', '210000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('44', '210900', b'0', '阜新市', '210000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('45', '211000', b'0', '辽阳市', '210000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('46', '211100', b'0', '盘锦市', '210000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('47', '211200', b'0', '铁岭市', '210000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('48', '211300', b'0', '朝阳市', '210000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('49', '211400', b'0', '葫芦岛市', '210000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('50', '220100', b'0', '长春市', '220000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('51', '220200', b'0', '吉林市', '220000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('52', '220300', b'0', '四平市', '220000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('53', '220400', b'0', '辽源市', '220000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('54', '220500', b'0', '通化市', '220000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('55', '220600', b'0', '白山市', '220000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('56', '220700', b'0', '松原市', '220000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('57', '220800', b'0', '白城市', '220000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('58', '222400', b'0', '延边州', '220000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('59', '230100', b'0', '哈尔滨市', '230000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('60', '230200', b'0', '齐齐哈尔市', '230000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('61', '230300', b'0', '鸡西市', '230000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('62', '230400', b'0', '鹤岗市', '230000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('63', '230500', b'0', '双鸭山市', '230000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('64', '230600', b'0', '大庆市', '230000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('65', '230700', b'0', '伊春市', '230000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('66', '230800', b'0', '佳木斯市', '230000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('67', '230900', b'0', '七台河市', '230000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('68', '231000', b'0', '牡丹江市', '230000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('69', '231100', b'0', '黑河市', '230000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('70', '231200', b'0', '绥化市', '230000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('71', '232700', b'0', '大兴安岭地区', '230000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('72', '310100', b'0', '上海市', '310000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('74', '320100', b'0', '南京市', '320000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('75', '320200', b'0', '无锡市', '320000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('76', '320300', b'0', '徐州市', '320000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('77', '320400', b'0', '常州市', '320000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('78', '320500', b'0', '苏州市', '320000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('79', '320600', b'0', '南通市', '320000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('80', '320700', b'0', '连云港市', '320000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('81', '320800', b'0', '淮安市', '320000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('82', '320900', b'0', '盐城市', '320000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('83', '321000', b'0', '扬州市', '320000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('84', '321100', b'0', '镇江市', '320000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('85', '321200', b'0', '泰州市', '320000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('86', '321300', b'0', '宿迁市', '320000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('87', '330100', b'0', '杭州市', '330000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('88', '330200', b'0', '宁波市', '330000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('89', '330300', b'0', '温州市', '330000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('90', '330400', b'0', '嘉兴市', '330000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('91', '330500', b'0', '湖州市', '330000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('92', '330600', b'0', '绍兴市', '330000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('93', '330700', b'0', '金华市', '330000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('94', '330800', b'0', '衢州市', '330000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('95', '330900', b'0', '舟山市', '330000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('96', '331000', b'0', '台州市', '330000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('97', '331100', b'0', '丽水市', '330000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('98', '340100', b'0', '合肥市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code) VALUES ('99', '340200', b'0', '芜湖市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('100', '340300', b'0', '蚌埠市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('101', '340400', b'0', '淮南市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('102', '340500', b'0', '马鞍山市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('103', '340600', b'0', '淮北市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('104', '340700', b'0', '铜陵市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('105', '340800', b'0', '安庆市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('106', '341000', b'0', '黄山市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('107', '341100', b'0', '滁州市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('108', '341200', b'0', '阜阳市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('109', '341300', b'0', '宿州市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('110', '341400', b'1', '巢湖市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('111', '341500', b'0', '六安市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('112', '341600', b'0', '亳州市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('113', '341700', b'0', '池州市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('114', '341800', b'0', '宣城市', '340000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('115', '350100', b'0', '福州市', '350000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('116', '350200', b'0', '厦门市', '350000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('117', '350300', b'0', '莆田市', '350000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('118', '350400', b'0', '三明市', '350000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('119', '350500', b'0', '泉州市', '350000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('120', '350600', b'0', '漳州市', '350000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('121', '350700', b'0', '南平市', '350000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('122', '350800', b'0', '龙岩市', '350000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('123', '350900', b'0', '宁德市', '350000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('124', '360100', b'0', '南昌市', '360000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('125', '360200', b'0', '景德镇市', '360000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('126', '360300', b'0', '萍乡市', '360000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('127', '360400', b'0', '九江市', '360000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('128', '360500', b'0', '新余市', '360000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('129', '360600', b'0', '鹰潭市', '360000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('130', '360700', b'0', '赣州市', '360000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('131', '360800', b'0', '吉安市', '360000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('132', '360900', b'0', '宜春市', '360000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('133', '361000', b'0', '抚州市', '360000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('134', '361100', b'0', '上饶市', '360000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('135', '370100', b'0', '济南市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('136', '370200', b'0', '青岛市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('137', '370300', b'0', '淄博市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('138', '370400', b'0', '枣庄市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('139', '370500', b'0', '东营市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('140', '370600', b'0', '烟台市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('141', '370700', b'0', '潍坊市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('142', '370800', b'0', '济宁市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('143', '370900', b'0', '泰安市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('144', '371000', b'0', '威海市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('145', '371100', b'0', '日照市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('146', '371200', b'0', '莱芜市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('147', '371300', b'0', '临沂市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('148', '371400', b'0', '德州市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('149', '371500', b'0', '聊城市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('150', '371600', b'0', '滨州市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('151', '371700', b'0', '菏泽市', '370000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('152', '410100', b'0', '郑州市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('153', '410200', b'0', '开封市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('154', '410300', b'0', '洛阳市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('155', '410400', b'0', '平顶山市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('156', '410500', b'0', '安阳市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('157', '410600', b'0', '鹤壁市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('158', '410700', b'0', '新乡市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('159', '410800', b'0', '焦作市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('160', '410900', b'0', '濮阳市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('161', '411000', b'0', '许昌市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('162', '411100', b'0', '漯河市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('163', '411200', b'0', '三门峡市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('164', '411300', b'0', '南阳市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('165', '411400', b'0', '商丘市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('166', '411500', b'0', '信阳市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('167', '411600', b'0', '周口市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('168', '411700', b'0', '驻马店市', '410000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('169', '420100', b'0', '武汉市', '420000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('170', '420200', b'0', '黄石市', '420000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('171', '420300', b'0', '十堰市', '420000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('172', '420500', b'0', '宜昌市', '420000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('173', '420600', b'0', '襄阳市', '420000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('174', '420700', b'0', '鄂州市', '420000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('175', '420800', b'0', '荆门市', '420000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('176', '420900', b'0', '孝感市', '420000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('177', '421000', b'0', '荆州市', '420000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('178', '421100', b'0', '黄冈市', '420000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('179', '421200', b'0', '咸宁市', '420000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('180', '421300', b'0', '随州市', '420000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('181', '422800', b'0', '恩施州', '420000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('183', '430100', b'0', '长沙市', '430000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('184', '430200', b'0', '株洲市', '430000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('185', '430300', b'0', '湘潭市', '430000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('186', '430400', b'0', '衡阳市', '430000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('187', '430500', b'0', '邵阳市', '430000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('188', '430600', b'0', '岳阳市', '430000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('189', '430700', b'0', '常德市', '430000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('190', '430800', b'0', '张家界市', '430000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('191', '430900', b'0', '益阳市', '430000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('192', '431000', b'0', '郴州市', '430000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('193', '431100', b'0', '永州市', '430000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('194', '431200', b'0', '怀化市', '430000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('195', '431300', b'0', '娄底市', '430000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('196', '433100', b'0', '湘西州', '430000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('197', '440100', b'0', '广州市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('198', '440200', b'0', '韶关市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('199', '440300', b'0', '深圳市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('200', '440400', b'0', '珠海市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('201', '440500', b'0', '汕头市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('202', '440600', b'0', '佛山市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('203', '440700', b'0', '江门市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('204', '440800', b'0', '湛江市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('205', '440900', b'0', '茂名市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('206', '441200', b'0', '肇庆市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('207', '441300', b'0', '惠州市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('208', '441400', b'0', '梅州市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('209', '441500', b'0', '汕尾市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('210', '441600', b'0', '河源市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('211', '441700', b'0', '阳江市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('212', '441800', b'0', '清远市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('213', '441900', b'0', '东莞市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('214', '442000', b'0', '中山市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('215', '445100', b'0', '潮州市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('216', '445200', b'0', '揭阳市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('217', '445300', b'0', '云浮市', '440000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('218', '450100', b'0', '南宁市', '450000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('219', '450200', b'0', '柳州市', '450000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('220', '450300', b'0', '桂林市', '450000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('221', '450400', b'0', '梧州市', '450000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('222', '450500', b'0', '北海市', '450000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('223', '450600', b'0', '防城港市', '450000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('224', '450700', b'0', '钦州市', '450000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('225', '450800', b'0', '贵港市', '450000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('226', '450900', b'0', '玉林市', '450000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('227', '451000', b'0', '百色市', '450000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('228', '451100', b'0', '贺州市', '450000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('229', '451200', b'0', '河池市', '450000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('230', '451300', b'0', '来宾市', '450000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('231', '451400', b'0', '崇左市', '450000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('232', '460100', b'0', '海口市', '460000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('233', '460200', b'0', '三亚市', '460000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('235', '500100', b'0', '重庆市', '500000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('238', '510100', b'0', '成都市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('239', '510300', b'0', '自贡市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('240', '510400', b'0', '攀枝花市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('241', '510500', b'0', '泸州市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('242', '510600', b'0', '德阳市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('243', '510700', b'0', '绵阳市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('244', '510800', b'0', '广元市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('245', '510900', b'0', '遂宁市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('246', '511000', b'0', '内江市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('247', '511100', b'0', '乐山市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('248', '511300', b'0', '南充市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('249', '511400', b'0', '眉山市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('250', '511500', b'0', '宜宾市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('251', '511600', b'0', '广安市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('252', '511700', b'0', '达州市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('253', '511800', b'0', '雅安市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('254', '511900', b'0', '巴中市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('255', '512000', b'0', '资阳市', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('256', '513200', b'0', '阿坝州', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('257', '513300', b'0', '甘孜州', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('258', '513400', b'0', '凉山州', '510000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('259', '520100', b'0', '贵阳市', '520000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('260', '520200', b'0', '六盘水市', '520000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('261', '520300', b'0', '遵义市', '520000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('262', '520400', b'0', '安顺市', '520000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('263', '522200', b'1', '铜仁地区', '520000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('264', '522300', b'0', '黔西南州', '520000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('265', '522400', b'1', '毕节地区', '520000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('266', '522600', b'0', '黔东南州', '520000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('267', '522700', b'0', '黔南州', '520000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('268', '530100', b'0', '昆明市', '530000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('269', '530300', b'0', '曲靖市', '530000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('270', '530400', b'0', '玉溪市', '530000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('271', '530500', b'0', '保山市', '530000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('272', '530600', b'0', '昭通市', '530000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('273', '530700', b'0', '丽江市', '530000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('274', '530800', b'0', '普洱市', '530000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('275', '530900', b'0', '临沧市', '530000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('276', '532300', b'0', '楚雄州', '530000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('277', '532500', b'0', '红河州', '530000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('278', '532600', b'0', '文山州', '530000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('279', '532800', b'0', '西双版纳州', '530000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('280', '532900', b'0', '大理州', '530000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('281', '533100', b'0', '德宏州', '530000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('282', '533300', b'0', '怒江州', '530000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('283', '533400', b'0', '迪庆州', '530000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('284', '540100', b'0', '拉萨市', '540000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('285', '542100', b'0', '昌都地区', '540000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('286', '542200', b'0', '山南地区', '540000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('287', '542300', b'0', '日喀则地区', '540000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('288', '542400', b'0', '那曲地区', '540000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('289', '542500', b'0', '阿里地区', '540000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('290', '542600', b'0', '林芝地区', '540000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('291', '610100', b'0', '西安市', '610000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('292', '610200', b'0', '铜川市', '610000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('293', '610300', b'0', '宝鸡市', '610000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('294', '610400', b'0', '咸阳市', '610000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('295', '610500', b'0', '渭南市', '610000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('296', '610600', b'0', '延安市', '610000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('297', '610700', b'0', '汉中市', '610000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('298', '610800', b'0', '榆林市', '610000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('299', '610900', b'0', '安康市', '610000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('300', '611000', b'0', '商洛市', '610000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('301', '620100', b'0', '兰州市', '620000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('302', '620200', b'0', '嘉峪关市', '620000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('303', '620300', b'0', '金昌市', '620000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('304', '620400', b'0', '白银市', '620000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('305', '620500', b'0', '天水市', '620000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('306', '620600', b'0', '武威市', '620000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('307', '620700', b'0', '张掖市', '620000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('308', '620800', b'0', '平凉市', '620000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('309', '620900', b'0', '酒泉市', '620000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('310', '621000', b'0', '庆阳市', '620000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('311', '621100', b'0', '定西市', '620000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('312', '621200', b'0', '陇南市', '620000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('313', '622900', b'0', '临夏州', '620000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('314', '623000', b'0', '甘南州', '620000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('315', '630100', b'0', '西宁市', '630000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('316', '632100', b'1', '海东地区', '630000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('317', '632200', b'0', '海北州', '630000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('318', '632300', b'0', '黄南州', '630000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('319', '632500', b'0', '海南州', '630000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('320', '632600', b'0', '果洛州', '630000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('321', '632700', b'0', '玉树州', '630000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('322', '632800', b'0', '海西州', '630000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('323', '640100', b'0', '银川市', '640000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('324', '640200', b'0', '石嘴山市', '640000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('325', '640300', b'0', '吴忠市', '640000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('326', '640400', b'0', '固原市', '640000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('327', '640500', b'0', '中卫市', '640000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('328', '650100', b'0', '乌鲁木齐市', '650000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('329', '650200', b'0', '克拉玛依市', '650000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('330', '652100', b'0', '吐鲁番地区', '650000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('331', '652200', b'0', '哈密地区', '650000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('332', '652300', b'0', '昌吉州', '650000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('333', '652700', b'0', '博尔塔拉州', '650000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('334', '652800', b'0', '巴音郭楞州', '650000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('335', '652900', b'0', '阿克苏地区', '650000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('336', '653000', b'0', '克孜勒苏州', '650000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('337', '653100', b'0', '喀什地区', '650000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('338', '653200', b'0', '和田地区', '650000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('339', '654000', b'0', '伊犁州', '650000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('340', '654200', b'0', '塔城地区', '650000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('341', '654300', b'0', '阿勒泰地区', '650000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('346', '120100', b'0', '天津市', '120000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('347', '429000', b'1', '直辖县区', '420000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('348', '469000', b'1', '直辖县区', '460000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('349', '500200', b'1', '直辖县区', '500000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('350', '500300', b'1', '直辖市', '500000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('351', '659000', b'1', '直辖市县', '650000');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES ('352', '310200', b'1', '直辖市县', '310000');

INSERT INTO bank (id, bank_code, bank_name) VALUES ('1', '800001', '银行间市场清算所');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('2', '800002', '中央结算公司');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('3', '900002', '中山古镇南粤村镇银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('4', '900003', '深圳南山宝生村镇银行股份有限公司');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('5', '900004', '禹州新民生村镇银行股份有限公司');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('6', '900005', '日照九银村镇银行股份有限公司');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('7', '900006', '重庆巴南浦发村镇银行股份有限公司');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('8', '9000AA', '甘肃银行股份有限公司');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('9', 'A10001', '中国人民银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('10', 'C10102', '中国工商银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('11', 'C10103', '中国农业银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('12', 'C10104', '中国银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('13', 'C10105', '中国建设银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('14', 'C10201', '国家开发银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('15', 'C10202', '中国进出口银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('16', 'C10203', '中国农业发展银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('17', 'C10301', '交通银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('18', 'C10302', '中信银行股份有限公司');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('19', 'C10303', '中国光大银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('20', 'C10304', '华夏银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('21', 'C10305', '中国民生银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('22', 'C10306', '广发银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('23', 'C10307', '深圳发展银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('24', 'C10308', '招商银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('25', 'C10309', '兴业银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('26', 'C10310', '上海浦东发展银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('27', 'C10315', '恒丰银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('28', 'C10316', '浙商银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('29', 'C10318', '渤海银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('30', 'C10319', '徽商银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('31', 'C10321', '重庆三峡银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('32', 'C10403', '邮政储蓄 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('33', 'C10501', '汇丰银行（中国）有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('34', 'C10502', '东亚银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('35', 'C10503', '南洋商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('36', 'C10504', '恒生银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('37', 'C10505', '中银香港 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('38', 'C10506', '集友银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('39', 'C10507', '创兴银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('40', 'C10508', '亚洲商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('41', 'C10509', '道亨银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('42', 'C10510', '永亨银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('43', 'C10511', '上海商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('44', 'C10512', '永隆银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('45', 'C10513', '大新银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('46', 'C10514', '中信嘉华银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('47', 'C10531', '花旗银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('48', 'C10532', '美国银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('49', 'C10533', '摩根大通银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('50', 'C10534', '建东银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('51', 'C10535', '美一银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('52', 'C10536', '纽约银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('53', 'C10561', '东京三菱银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('54', 'C10562', '日联银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('55', 'C10563', '三井住友银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('56', 'C10564', '瑞穗实业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('57', 'C10565', '山口银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('58', 'C10591', '韩国外换银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('59', 'C10592', '朝兴银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('60', 'C10593', '友利银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('61', 'C10594', '韩国产业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('62', 'C10595', '新韩银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('63', 'C10596', '韩国中小企业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('64', 'C10597', '韩亚银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('65', 'C10611', '马来亚银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('66', 'C10616', '首都银行及信托公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('67', 'C10621', '华侨银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('68', 'C10622', '大华银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('69', 'C10623', '新加坡星展银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('70', 'C10631', '盘古银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('71', 'C10632', '泰京银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('72', 'C10633', '泰华农民银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('73', 'C10641', '奥地利中央合作银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('74', 'C10651', '比利时联合银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('75', 'C10652', '比利时富通银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('76', 'C10661', '苏格兰皇家银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('77', 'C10662', '荷兰安智银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('78', 'C10663', '荷兰万贝银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('79', 'C10671', '渣打银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('80', 'C10672', '苏格兰皇家银行(不用 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('81', 'C10673', '英国巴克莱银行有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('82', 'C10681', '瑞典商业银行公共有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('83', 'C10682', '瑞典北欧斯安银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('84', 'C10691', '法国兴业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('85', 'C10692', '法国巴黎银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('86', 'C10693', '东方汇理银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('87', 'C10694', '法国里昂信贷银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('88', 'C10695', '法国外贸银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('89', 'C10711', '德累斯顿银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('90', 'C10712', '德意志银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('91', 'C10713', '德国商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('92', 'C10714', '西德银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('93', 'C10715', '巴伐利亚银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('94', 'C10716', '德国北德意志州银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('95', 'C10731', '罗马银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('96', 'C10732', '意大利联合商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('97', 'C10741', '瑞士信贷银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('98', 'C10742', '瑞士银行有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('99', 'C10751', '丰业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('100', 'C10752', '蒙特利尔银行（中国）有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('101', 'C10761', '澳新银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('102', 'C10766', '葡国储蓄信贷银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('103', 'C10771', '珠海南通银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('104', 'C10772', '宁波国际银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('105', 'C10773', '新联商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('106', 'C10774', '协和银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('107', 'C10775', '华美银行(中国有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('108', 'C10776', '荷兰合作银行（中国）有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('109', 'C10781', '厦门国际银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('110', 'C10782', '上海一巴黎国际银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('111', 'C10784', '浙江商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('112', 'C10785', '华商银行测试 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('113', 'C10786', '青岛国际银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('114', 'C10787', '华一银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('115', 'C10788', '成都银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('116', 'C10789', '自贡市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('117', 'C10790', '攀枝花市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('118', 'C10791', '泸州市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('119', 'C10792', '宜宾市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('120', 'C10793', '乐山市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('121', 'C10794', '德阳银行股份有限公司');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('122', 'C10795', '绵阳市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('123', 'C10796', '南充市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('124', 'C10797', '达州市商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('125', 'C10798', '雅安市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('126', 'C10799', '凉山州商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('127', 'C10800', '遂宁市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('128', 'C10801', '四川省新龙县信用合作社联合社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('129', 'C10802', '北京银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('130', 'C10803', '天津银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('131', 'C10804', '徽商银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('132', 'C10805', '贵阳银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('133', 'C10806', '六盘水市商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('134', 'C10807', '贵州银行股份有限公司');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('135', 'C10808', '安顺市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('136', 'C10809', '盛京银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('137', 'C10810', '大连银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('138', 'C10811', '鞍山银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('139', 'C10812', '抚顺市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('140', 'C10813', '丹东市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('141', 'C10814', '锦州银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('142', 'C10815', '营口银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('143', 'C10816', '阜新市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('144', 'C10817', '辽阳银行股份有限公司');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('145', 'C10818', '盘锦市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('146', 'C10819', '铁岭市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('147', 'C10820', '朝阳市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('148', 'C10821', '葫芦岛市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('149', 'C10822', '福建海峡银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('150', 'C10823', '泉州银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('151', 'C10824', '厦门银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('152', 'C10825', '兰州银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('153', 'C10826', '甘肃银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('154', 'C10827', '广州银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('155', 'C10828', '平安银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('156', 'C10829', '珠海华润银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('157', 'C10830', '广东南粤银行股份有限公司');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('158', 'C10831', '东莞银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('159', 'C10832', '广西北部湾银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('160', 'C10833', '柳州市商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('161', 'C10834', '沧州银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('162', 'C10835', '廊坊银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('163', 'C10836', '衡水市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('164', 'C10837', '新乡银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('165', 'C10838', '焦作市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('166', 'C10839', '信阳市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('167', 'C10840', '驻马店市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('168', 'C10841', '三门峡市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('169', 'C10842', '南阳市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('170', 'C10843', '漯河市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('171', 'C10844', '安阳市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('172', 'C10845', '周口市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('173', 'C10846', '洛阳银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('174', 'C10847', '平顶山市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('175', 'C10848', '鹤壁市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('176', 'C10849', '许昌银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('177', 'C10850', '开封市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('178', 'C10851', '郑州银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('179', 'C10852', '齐齐哈尔市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('180', 'C10853', '牡丹江市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('181', 'C10854', '大庆市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('182', 'C10855', '哈尔滨银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('183', 'C10856', '汉口银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('184', 'C10857', '黄石市商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('185', 'C10858', '宜昌市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('186', 'C10859', '襄樊市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('187', 'C10860', '孝感市商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('188', 'C10861', '荆州市商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('189', 'C10862', '长沙银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('190', 'C10863', '株洲市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('191', 'C10864', '湘潭市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('192', 'C10865', '衡阳市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('193', 'C10866', '岳阳市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('194', 'C10867', '吉林银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('195', 'C10868', '江苏银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('196', 'C10869', '南京银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('197', 'C10870', '江苏长江商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('198', 'C10871', '九江银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('199', 'C10872', '上饶银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('200', 'C10873', '赣州银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('201', 'C10874', '南昌银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('202', 'C10875', '内蒙古银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('203', 'C10876', '包商银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('204', 'C10877', '乌海市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('205', 'C10878', '鄂尔多斯市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('206', 'C10879', '宁夏银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('207', 'C10880', '石嘴山银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('208', 'C10881', '齐鲁银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('209', 'C10882', '青岛银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('210', 'C10883', '齐商银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('211', 'C10884', '桂林市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('212', 'C10885', '河北银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('213', 'C10886', '唐山市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('214', 'C10887', '秦皇岛市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('215', 'C10888', '邯郸市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('216', 'C10889', '邢台市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('217', 'C10890', '保定市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('218', 'C10891', '张家口市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('219', 'C10892', '承德银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('220', 'C10893', '枣庄市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('221', 'C10894', '东营市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('222', 'C10895', '烟台银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('223', 'C10896', '潍坊银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('224', 'C10897', '济宁市商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('225', 'C10898', '泰安市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('226', 'C10899', '威海市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('227', 'C10900', '日照银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('228', 'C10901', '莱商银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('229', 'C10902', '临商银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('230', 'C10903', '德州市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('231', 'C10904', '晋商银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('232', 'C10905', '大同市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('233', 'C10906', '阳泉市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('234', 'C10907', '晋城市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('235', 'C10908', '长治市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('236', 'C10909', '晋中市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('237', 'C10910', '长安银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('238', 'C10911', '西安市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('239', 'C10912', '上海银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('240', 'C10913', '乌鲁木齐市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('241', 'C10914', '克拉玛依市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('242', 'C10915', '库尔勒市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('243', 'C10916', '奎屯市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('244', 'C10917', '富滇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('245', 'C10918', '曲靖市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('246', 'C10919', '玉溪市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('247', 'C10920', '杭州银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('248', 'C10921', '宁波银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('249', 'C10922', '温州银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('250', 'C10923', '嘉兴市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('251', 'C10924', '湖州市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('252', 'C10925', '绍兴银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('253', 'C10926', '金华银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('254', 'C10927', '浙江稠州商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('255', 'C10928', '台州银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('256', 'C10929', '浙江泰隆商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('257', 'C10930', '浙江民泰商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('258', 'C10931', '重庆银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('259', 'C10932', '重庆三峡银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('260', 'C10933', '青海银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('261', 'C10934', '大邑交银兴民村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('262', 'C10935', '双流诚民村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('263', 'C10936', '都江堰金都村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('264', 'C10937', '彭州民生村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('265', 'C10938', '邛崃国民村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('266', 'C10939', '泸县元通村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('267', 'C10940', '隆昌县兴隆村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('268', 'C10941', '什邡思源村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('269', 'C10942', '绵竹浦发村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('270', 'C10943', '四川北川羌族自治县富民村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('271', 'C10944', '四川仪陇惠民村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('272', 'C10945', '宣汉诚民村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('273', 'C10946', '广元市包商贵民村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('274', 'C10947', '仁寿民富村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('275', 'C10948', '北京密云汇丰村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('276', 'C10949', '北京延庆村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('277', 'C10950', '天津市蓟县村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('278', 'C10951', '天津市北辰村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('279', 'C10952', '安徽凤阳利民村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('280', 'C10953', '安徽长丰科源村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('281', 'C10954', '贵阳花溪建设村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('282', 'C10955', '毕节发展村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('283', 'C10956', '龙里国丰村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('284', 'C10957', '沈阳市沈北富民村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('285', 'C10958', '瓦房店长兴村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('286', 'C10959', '庄河汇通村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('287', 'C10960', '辽宁大石桥隆丰村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('288', 'C10961', '辽宁彰武金通村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('289', 'C10962', '铁岭新星村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('290', 'C10963', '福建永安汇丰村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('291', 'C10964', '福建建瓯石狮村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('292', 'C10965', '会宁会师村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('293', 'C10966', '天水市秦安众信村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('294', 'C10967', '民勤融信村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('295', 'C10968', '平凉市泾川县汇通村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('296', 'C10969', '平凉市静宁县成纪村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('297', 'C10970', '敦煌市金盛村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('298', 'C10971', '庆阳市西峰瑞信村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('299', 'C10972', '陇南市武都金桥村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('300', 'C10973', '广东恩平汇丰村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('301', 'C10974', '中山小榄村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('302', 'C10975', '广西柳江兴柳村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('303', 'C10976', '广西兴安民兴村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('304', 'C10977', '田东北部湾村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('305', 'C10978', '平果国民村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('306', 'C10979', '张北信达村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('307', 'C10980', '巩义浦发村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('308', 'C10981', '固始天骄村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('309', 'C10982', '河南方城凤裕村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('310', 'C10983', '河南栾川民丰村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('311', 'C10984', '郏县广天村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('312', 'C10985', '巴彦融兴村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('313', 'C10986', '依安国民村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('314', 'C10987', '东宁远东村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('315', 'C10988', '大庆市杜尔伯特润生村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('316', 'C10989', '汉川农银村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('317', 'C10990', '嘉鱼吴江村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('318', 'C10991', '恩施常农商村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('319', 'C10992', '咸丰常农商村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('320', 'C10993', '湖北仙桃北农商村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('321', 'C10994', '湘乡市村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('322', 'C10995', '韶山市光大村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('323', 'C10996', '湖南桃江建信村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('324', 'C10997', '资兴浦发村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('325', 'C10998', '祁阳村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('326', 'C10999', '磐石融丰村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('327', 'C11000', '东丰诚信村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('328', 'C11001', '前郭县阳光村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('329', 'C11002', '敦化江南村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('330', 'C11003', '宜兴阳羡村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('331', 'C11004', '江苏铜山锡州村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('332', 'C11005', '金坛常农商村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('333', 'C11006', '溧阳浦发村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('334', 'C11007', '江苏通州华商村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('335', 'C11008', '江苏东海张农商村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('336', 'C11009', '江苏沭阳东吴村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('337', 'C11010', '修水九银村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('338', 'C11011', '南康赣商村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('339', 'C11012', '内蒙古和林格尔渣打村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('340', 'C11013', '固阳包商惠农村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('341', 'C11014', '克什克腾农银村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('342', 'C11015', '达拉特国开村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('343', 'C11016', '鄂温克族自治旗包商村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('344', 'C11017', '平罗沙湖村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('345', 'C11018', '吴忠市滨河村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('346', 'C11019', '青岛即墨北农商村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('347', 'C11020', '青岛胶南海汇村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('348', 'C11021', '寿光张农商村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('349', 'C11022', '盂县汇民村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('350', 'C11023', '陵川县太行村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('351', 'C11024', '宝鸡岐山硕丰村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('352', 'C11025', '陕西洛南阳光村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('353', 'C11026', '上海崇明长江村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('354', 'C11027', '上海奉贤浦发村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('355', 'C11028', '五家渠国民村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('356', 'C11029', '文山民丰村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('357', 'C11030', '玉溪红塔区兴和村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('358', 'C11031', '昭通昭阳富滇村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('359', 'C11032', '浙江建德湖商村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('360', 'C11033', '象山国民村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('361', 'C11034', '慈溪民生村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('362', 'C11035', '浙江永嘉恒升村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('363', 'C11036', '浙江苍南建信村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('364', 'C11037', '浙江长兴联合村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('365', 'C11038', '浙江嵊州瑞丰村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('366', 'C11039', '浙江玉环永兴村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('367', 'C11040', '重庆大足汇丰村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('368', 'C11041', '大通国开村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('369', 'C11042', '资阳雁江农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('370', 'C11043', '北京农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('371', 'C11044', '天津农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('372', 'C11045', '天津东丽农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('373', 'C11046', '天津津南农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('374', 'C11047', '天津西青农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('375', 'C11048', '天津北辰农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('376', 'C11049', '天津滨海农村商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('377', 'C11050', '安徽长丰农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('378', 'C11051', '安徽肥东农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('379', 'C11052', '合肥科技农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('380', 'C11053', '安徽马鞍山农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('381', 'C11054', '淮南通商农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('382', 'C11055', '安徽绩溪农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('383', 'C11056', '安徽广德农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('384', 'C11057', '安徽泗县农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('385', 'C11058', '池州九华农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('386', 'C11059', '安徽青阳农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('387', 'C11060', '安徽东至农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('388', 'C11061', '安徽天长农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('389', 'C11062', '安徽舒城农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('390', 'C11063', '安徽霍山农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('391', 'C11064', '安徽叶集农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('392', 'C11065', '安徽怀远农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('393', 'C11066', '安徽休宁农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('394', 'C11067', '黟县农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('395', 'C11068', '安徽繁昌农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('396', 'C11069', '芜湖扬子农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('397', 'C11070', '安徽桐城农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('398', 'C11071', '铜陵皖江农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('399', 'C11072', '铜陵铜都农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('400', 'C11073', '贵州贵阳云岩农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('401', 'C11074', '贵州花溪农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('402', 'C11075', '贵州湄潭农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('403', 'C11076', '贵州兴义农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('404', 'C11077', '大连甘井子农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('405', 'C11078', '辽宁葫芦岛连山农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('406', 'C11079', '福建南安农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('407', 'C11080', '福建晋江农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('408', 'C11081', '福建石狮农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('409', 'C11082', '甘肃榆中农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('410', 'C11083', '景泰县龙湾村石林农村资金互助社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('411', 'C11084', '天水秦州农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('412', 'C11085', '天水麦积农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('413', 'C11086', '张掖甘州农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('414', 'C11087', '甘肃临泽农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('415', 'C11088', '甘肃高台农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('416', 'C11089', '酒泉肃州农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('417', 'C11090', '甘肃敦煌农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('418', 'C11091', '甘肃陇西农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('419', 'C11092', '甘肃临洮农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('420', 'C11093', '岷县岷鑫农村资金互助社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('421', 'C11094', '陇南武都农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('422', 'C11095', '甘肃西和农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('423', 'C11096', '深圳农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('424', 'C11097', '广西崇左桂南农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('425', 'C11098', '广西鹿寨农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('426', 'C11099', '广西融安农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('427', 'C11100', '广西柳江农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('428', 'C11101', '广西象州农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('429', 'C11102', '广西来宾桂中农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('430', 'C11103', '广西兴安农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('431', 'C11104', '广西灌阳农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('432', 'C11105', '广西恭城农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('433', 'C11106', '广西平乐农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('434', 'C11107', '广西荔浦农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('435', 'C11108', '广西永福农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('436', 'C11109', '广西桂林漓江农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('437', 'C11110', '广西资源农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('438', 'C11111', '广西临桂农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('439', 'C11112', '广西阳朔农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('440', 'C11113', '广西龙胜农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('441', 'C11114', '广西贺州桂东农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('442', 'C11115', '广西昭平农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('443', 'C11116', '广西百色右江农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('444', 'C11117', '广西田东农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('445', 'C11118', '广西平果农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('446', 'C11119', '田东县祥周镇鸿祥农村资金互助社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('447', 'C11120', '石家庄汇融农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('448', 'C11121', '晋州市周家庄农村资金互助社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('449', 'C11122', '沧州融信农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('450', 'C11123', '河北南皮农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('451', 'C11124', '河南固始农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('452', 'C11125', '河南新郑农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('453', 'C11126', '河南伊川农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('454', 'C11127', '黑龙江虎林农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('455', 'C11128', '武汉农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('456', 'C11129', '黄石滨江农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('457', 'C11130', '宜昌夷陵农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('458', 'C11131', '湖南浏阳农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('459', 'C11132', '湖南望城农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('460', 'C11133', '长沙雨花农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('461', 'C11134', '长沙天心农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('462', 'C11135', '长沙芙蓉农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('463', 'C11136', '长沙岳麓农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('464', 'C11137', '长沙开福农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('465', 'C11138', '湖南洞口农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('466', 'C11139', '永州冷水滩潇湘农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('467', 'C11140', '长春农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('468', 'C11141', '吉林九台农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('469', 'C11142', '梨树县闫家村百信农村资金互助社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('470', 'C11143', '吉林公主岭农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('471', 'C11144', '延边农村合作银行有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('472', 'C11145', '江苏高淳农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('473', 'C11146', '无锡农村商业银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('474', 'C11147', '江苏江阴农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('475', 'C11148', '江苏宜兴农村商业银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('476', 'C11149', '江苏新沂农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('477', 'C11150', '江苏睢宁农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('478', 'C11151', '江苏武进农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('479', 'C11152', '江苏溧阳农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('480', 'C11153', '苏州银行股份有限公司');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('481', 'C11154', '江苏常熟农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('482', 'C11155', '江苏昆山农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('483', 'C11156', '江苏张家港农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('484', 'C11157', '江苏太仓农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('485', 'C11158', '江苏吴江农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('486', 'C11159', '江苏海安农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('487', 'C11160', '江苏连云港东方农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('488', 'C11161', '江苏灌云农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('489', 'C11162', '江苏盱眙农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('490', 'C11163', '江苏涟水农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('491', 'C11164', '江苏阜宁农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('492', 'C11165', '江苏大丰农村商业银行');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('493', 'C11166', '江苏射阳农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('494', 'C11167', '江苏仪征农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('495', 'C11168', '江苏丹阳农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('496', 'C11169', '江苏扬中农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('497', 'C11170', '江苏姜堰农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('498', 'C11171', '泰州海阳农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('499', 'C11172', '江苏靖江农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('500', 'C11173', '江苏兴化农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('501', 'C11174', '江苏宿迁民丰农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('502', 'C11175', '江苏沭阳农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('503', 'C11176', '江苏泗洪农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('504', 'C11177', '江苏泗阳农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('505', 'C11178', '江西德安农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('506', 'C11179', '江西新余农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('507', 'C11180', '江西安义农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('508', 'C11181', '洪都农村商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('509', 'C11182', '江西遂川农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('510', 'C11183', '江西安福农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('511', 'C11184', '江西广丰农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('512', 'C11185', '江西德兴农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('513', 'C11186', '呼和浩特金谷农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('514', 'C11187', '赤峰元宝山农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('515', 'C11188', '通辽市辽河镇融达农村资金互助社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('516', 'C11189', '通辽奈曼农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('517', 'C11190', '鄂尔多斯东胜农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('518', 'C11191', '满洲里市农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('519', 'C11192', '二连浩特农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('520', 'C11193', '阿拉善左旗农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('521', 'C11194', '黄河农村商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('522', 'C11195', '山东济南润丰农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('523', 'C11196', '青岛华丰农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('524', 'C11197', '青岛城阳农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('525', 'C11198', '青岛即墨农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('526', 'C11199', '青岛黄岛农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('527', 'C11200', '山东张店农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('528', 'C11201', '山东枣庄恒泰农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('529', 'C11202', '山东东营胜利农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('530', 'C11203', '山东东营河口农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('531', 'C11204', '山东广饶农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('532', 'C11205', '山东莱州农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('533', 'C11206', '山东龙口农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('534', 'C11207', '山东寿光农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('535', 'C11208', '山东诸城农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('536', 'C11209', '山东圣泰农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('537', 'C11210', '山东东平农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('538', 'C11211', '山东博兴农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('539', 'C11212', '山东邹平农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('540', 'C11213', '山东临沂兰山农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('541', 'C11214', '山东临沂河东农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('542', 'C11215', '山西河津农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('543', 'C11216', '陕西西乡农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('544', 'C11217', '陕西榆林榆阳农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('545', 'C11218', '陕西神木农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('546', 'C11219', '陕西府谷农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('547', 'C11220', '陕西镇安农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('548', 'C11221', '上海农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('549', 'C11222', '新疆石河子农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('550', 'C11223', '昆明官渡农村合作银行营业部 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('551', 'C11224', '云南红塔农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('552', 'C11225', '大理市农村合作银行营业部 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('553', 'C11226', '云南昭通昭阳农村合作银行营业部 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('554', 'C11227', '杭州联合农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('555', 'C11228', '浙江萧山农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('556', 'C11229', '浙江杭州余杭农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('557', 'C11230', '浙江富阳农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('558', 'C11231', '浙江桐庐农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('559', 'C11232', '宁波慈溪农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('560', 'C11233', '宁波余姚农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('561', 'C11234', '宁波鄞州农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('562', 'C11235', '浙江温州鹿城农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('563', 'C11236', '浙江温州龙湾农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('564', 'C11237', '浙江温州瓯海农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('565', 'C11238', '浙江乐清农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('566', 'C11239', '浙江永嘉农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('567', 'C11240', '浙江瑞安农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('568', 'C11241', '浙江苍南农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('569', 'C11242', '浙江平湖农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('570', 'C11243', '浙江禾城农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('571', 'C11244', '湖州吴兴农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('572', 'C11245', '浙江南浔农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('573', 'C11246', '浙江德清农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('574', 'C11247', '浙江长兴农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('575', 'C11248', '浙江绍兴县农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('576', 'C11249', '浙江上虞农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('577', 'C11250', '浙江嵊州农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('578', 'C11251', '浙江新昌农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('579', 'C11252', '浙江诸暨农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('580', 'C11253', '浙江绍兴恒信农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('581', 'C11254', '浙江金华成泰农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('582', 'C11255', '浙江兰溪农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('583', 'C11256', '浙江义乌农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('584', 'C11257', '浙江台州椒江农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('585', 'C11258', '浙江台州黄岩农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('586', 'C11259', '浙江台州路桥农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('587', 'C11260', '浙江玉环农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('588', 'C11261', '浙江天台农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('589', 'C11262', '浙江温岭农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('590', 'C11263', '浙江丽水莲都农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('591', 'C11264', '浙江省舟山市定海农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('592', 'C11265', '浙江省舟山市普陀农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('593', 'C11266', '浙江江山农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('594', 'C11267', '重庆农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('595', 'C11268', '乐都县雨润镇兴乐农村资金互助社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('596', 'C11269', '称多县清水河富民农村资金互助社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('597', 'C11270', '韶山光大村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('598', 'C11271', '东兴国民村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('599', 'C11272', '田东县思林镇竹海农村资金互助社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('600', 'C11273', '临桂国民村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('601', 'C11274', '灵山泰业村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('602', 'C11275', '武陟射阳村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('603', 'C11276', '广西灵川农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('604', 'C11277', '平顶山市卫东农村商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('605', 'C11278', '商丘华商农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('606', 'C11279', '商丘市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('607', 'C11280', '三门峡市湖滨农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('608', 'C11281', '许昌魏都农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('609', 'C11282', '广西宜州农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('610', 'C11283', '广西大新农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('611', 'C11284', '浙江安吉交银村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('612', 'C11285', '九江共青村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('613', 'C11286', '吉安稠州村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('614', 'C11287', '江苏江南农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('615', 'C11288', '龙江银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('616', 'C11289', '汾西县汾河村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('617', 'C11290', '汾西县亿通村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('618', 'C11291', '泽州浦发村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('619', 'C11292', '中牟郑银村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('620', 'C11293', '大连普兰店汇丰村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('621', 'C11294', '濮阳市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('622', 'C11295', '河南罗山农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('623', 'C11296', '河南新县农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('624', 'C11297', '潢川珠江村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('625', 'C11298', '井冈山九银村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('626', 'C11299', '中德住房储蓄银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('627', 'C11300', '上海松江民生村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('628', 'C11301', '正信银行有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('629', 'C11302', '澳大利亚西太平洋银行有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('630', 'C11303', '晋中市榆次融信村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('631', 'C11304', '深圳龙岗鼎业村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('632', 'C11305', '安徽和县农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('633', 'C11306', '安徽当涂新华村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('634', 'C11307', '安庆独秀农村商业银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('635', 'C11308', '安徽怀宁农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('636', 'C11309', '安徽宿松农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('637', 'C11310', '安徽岳西农村合作银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('638', 'C11311', '东莞长安村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('639', 'C11312', '广州农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('640', 'C11313', '佛山顺德农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('641', 'C11314', '东莞农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('642', 'C11315', '韩国国民银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('643', 'C11316', '印度巴鲁达银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('644', 'C11317', '江苏靖江润丰村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('645', 'C11318', '意大利西雅那银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('646', 'C11319', '上海浦东江南村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('647', 'C11320', '印度国家银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('648', 'C11321', '俄罗斯外贸银行公开股份公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('649', 'C11322', '美国富国银行有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('650', 'C11323', '加拿大丰业银行有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('651', 'C11324', '四川长宁竹海农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('652', 'C11325', '美国纽约梅隆银行有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('653', 'C11326', '挪威银行公共有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('654', 'C11327', '日本横滨银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('655', 'C11360', '华融湘江银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('656', 'C11399', '外换银行（中国）有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('657', 'C11400', '深圳宝安融兴村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('658', 'C11521', '江苏省姜堰农商行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('659', 'C11558', '湖北银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('660', 'C11589', '江苏紫金农村商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('661', 'C11709', '广东华兴银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('662', 'C12588', '成都农商银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('663', 'C12589', '浙江平湖工银村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('664', 'C12590', '西昌金信村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('665', 'C12591', '浙江嘉善联合村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('666', 'C12592', '浙江青田建信华侨村镇银行有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('667', 'C12593', '浙江三门银座村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('668', 'C12594', '浙江乐清联合村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('669', 'C12595', '浙江岱山稠州村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('670', 'C12596', '余姚通济村镇银行 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('671', 'C12597', '哈密天山村镇银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('672', 'C20026', '本溪市城市信用社股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('673', 'C20030', '盖州市城市建设信用合作社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('674', 'C20031', '盖州市辰州城市信用合作社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('675', 'C20032', '盖州市城市信用合作社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('676', 'C20033', '营口经济技术开发区熊岳城市信用合作社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('677', 'C20035', '阜新蒙古族自治县东楼城市信用合作社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('678', 'C20036', '阜新蒙古族自治县亨通城市信用合作社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('679', 'C20037', '阜新蒙古族自治县繁荣城市信用合作社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('680', 'C20038', '阜新蒙古族自治县南环城市信用合作社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('681', 'C20039', '彰武县光大城市信用社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('682', 'C20040', '彰武县兴华城市信用合作社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('683', 'C20050', '白银市城市信用社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('684', 'C20077', '商丘市城市信用社股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('685', 'C20081', '濮阳市城市信用社股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('686', 'C20088', '七台河市城市信用社股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('687', 'C20103', '邵阳市城市信用社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('688', 'C20111', '景德镇市城市信用社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('689', 'C20145', '哈密市商业银行股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('690', 'C20153', '象山县绿叶城市信用社有限责任公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('691', 'C20154', '测试 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('692', 'C30015', '成都市农村信用合作联社股份有限公司 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('693', 'C30016', '荣县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('694', 'C30017', '富顺县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('695', 'C30018', '自贡市沿滩区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('696', 'C30019', '自贡市大安区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('697', 'C30020', '自贡市贡井区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('698', 'C30021', '米易县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('699', 'C30022', '攀枝花市仁和区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('700', 'C30023', '盐边县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('701', 'C30024', '泸州市江阳区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('702', 'C30025', '泸县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('703', 'C30026', '合江县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('704', 'C30027', '泸州市纳溪区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('705', 'C30028', '叙永县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('706', 'C30029', '泸州市古蔺县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('707', 'C30030', '泸州市龙马潭区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('708', 'C30031', '资中县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('709', 'C30032', '威远县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('710', 'C30033', '内江市东兴区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('711', 'C30034', '内江市市中区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('712', 'C30035', '隆昌县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('713', 'C30036', '宜宾市翠屏区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('714', 'C30037', '宜宾县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('715', 'C30038', '南溪县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('716', 'C30039', '江安县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('717', 'C30040', '长宁县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('718', 'C30041', '高县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('719', 'C30042', '筠连县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('720', 'C30043', '珙县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('721', 'C30044', '兴文县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('722', 'C30045', '屏山县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('723', 'C30046', '沙湾区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('724', 'C30047', '金口河区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('725', 'C30048', '犍为县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('726', 'C30049', '井研县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('727', 'C30050', '夹江县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('728', 'C30051', '沐川县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('729', 'C30052', '峨边彝族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('730', 'C30053', '马边彝族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('731', 'C30054', '五通桥区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('732', 'C30055', '峨眉山市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('733', 'C30056', '中江县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('734', 'C30057', '什邡市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('735', 'C30058', '绵竹市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('736', 'C30059', '广汉市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('737', 'C30060', '罗江县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('738', 'C30061', '德阳市旌阳区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('739', 'C30062', '绵阳市涪城区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('740', 'C30063', '绵阳市游仙区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('741', 'C30064', '北川羌族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('742', 'C30065', '江油市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('743', 'C30066', '梓潼县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('744', 'C30067', '平武县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('745', 'C30068', '三台县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('746', 'C30069', '盐亭县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('747', 'C30070', '南充市顺庆区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('748', 'C30071', '南充市高坪区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('749', 'C30072', '南充市嘉陵区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('750', 'C30073', '南部县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('751', 'C30074', '营山县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('752', 'C30075', '蓬安县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('753', 'C30076', '仪陇县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('754', 'C30077', '西充县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('755', 'C30078', '阆中市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('756', 'C30079', '达州市通川区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('757', 'C30080', '达县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('758', 'C30081', '宣汉县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('759', 'C30082', '开江县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('760', 'C30083', '万源市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('761', 'C30084', '大竹县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('762', 'C30085', '渠县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('763', 'C30086', '雅安市雨城区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('764', 'C30087', '名山县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('765', 'C30088', '荥经县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('766', 'C30089', '汉源县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('767', 'C30090', '石棉县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('768', 'C30091', '天全县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('769', 'C30092', '芦山县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('770', 'C30093', '宝兴县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('771', 'C30094', '四川省道孚县农村信用合作社联合社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('772', 'C30095', '壤塘县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('773', 'C30096', '茂县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('774', 'C30097', '黑水县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('775', 'C30098', '旺苍县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('776', 'C30099', '剑阁县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('777', 'C30100', '广元市元坝区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('778', 'C30101', '广元市朝天区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('779', 'C30102', '苍溪县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('780', 'C30103', '广元市利州区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('781', 'C30104', '青川县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('782', 'C30105', '射洪县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('783', 'C30106', '蓬溪县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('784', 'C30107', '遂宁市遂州农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('785', 'C30108', '大英县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('786', 'C30109', '岳池县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('787', 'C30110', '华蓥市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('788', 'C30111', '邻水县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('789', 'C30112', '武胜县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('790', 'C30113', '巴中市巴州区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('791', 'C30114', '平昌县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('792', 'C30115', '通江县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('793', 'C30116', '南江县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('794', 'C30117', '眉山市东坡区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('795', 'C30118', '彭山县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('796', 'C30119', '洪雅县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('797', 'C30120', '青神县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('798', 'C30121', '丹棱县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('799', 'C30122', '简阳市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('800', 'C30123', '安岳县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('801', 'C30124', '乐至县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('802', 'C30127', '天津市宝坻区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('803', 'C30128', '天津市蓟县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('804', 'C30129', '天津市武清区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('805', 'C30130', '天津市静海县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('806', 'C30131', '天津市宁河县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('807', 'C30134', '肥西县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('808', 'C30135', '淮南市潘集区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('809', 'C30136', '凤台县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('810', 'C30137', '宣城市宣州区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('811', 'C30138', '宁国市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('812', 'C30139', '旌德县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('813', 'C30140', '郎溪县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('814', 'C30141', '泾县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('815', 'C30142', '宿州市区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('816', 'C30143', '砀山县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('817', 'C30144', '萧县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('818', 'C30145', '灵璧县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('819', 'C30146', '石台县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('820', 'C30147', '滁州市市郊农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('821', 'C30148', '来安县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('822', 'C30149', '全椒县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('823', 'C30150', '定远县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('824', 'C30151', '凤阳县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('825', 'C30152', '明光市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('826', 'C30153', '六安市郊区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('827', 'C30154', '寿县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('828', 'C30155', '霍邱县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('829', 'C30156', '金寨县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('830', 'C30157', '亳州市谯城区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('831', 'C30158', '蒙城县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('832', 'C30159', '涡阳县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('833', 'C30160', '利辛县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('834', 'C30161', '蚌埠市市区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('835', 'C30162', '五河县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('836', 'C30163', '固镇县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('837', 'C30164', '黄山市屯溪区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('838', 'C30165', '黄山市黄山区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('839', 'C30166', '黄山市徽州区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('840', 'C30167', '祁门县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('841', 'C30168', '歙县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('842', 'C30169', '淮北市区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('843', 'C30170', '濉溪县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('844', 'C30171', '南陵县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('845', 'C30172', '芜湖县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('846', 'C30173', '安庆市城郊农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('847', 'C30174', '怀宁县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('848', 'C30175', '枞阳县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('849', 'C30176', '潜山县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('850', 'C30177', '太湖县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('851', 'C30178', '宿松县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('852', 'C30179', '望江县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('853', 'C30180', '岳西县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('854', 'C30181', '临泉县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('855', 'C30182', '太和县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('856', 'C30183', '阜南县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('857', 'C30184', '颍上县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('858', 'C30185', '界首市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('859', 'C30186', '阜阳市颍州区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('860', 'C30187', '阜阳市颍泉区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('861', 'C30188', '阜阳市颍东区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('862', 'C30189', '巢湖市居巢区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('863', 'C30190', '无为县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('864', 'C30191', '庐江县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('865', 'C30192', '含山县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('866', 'C30193', '和县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('867', 'C30197', '贵阳市南明区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('868', 'C30198', '贵阳市乌当区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('869', 'C30199', '贵阳市白云区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('870', 'C30200', '清镇市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('871', 'C30201', '开阳县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('872', 'C30202', '修文县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('873', 'C30203', '息烽县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('874', 'C30204', '贵阳市小河区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('875', 'C30205', '遵义市红花岗区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('876', 'C30206', '遵义市汇川区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('877', 'C30207', '遵义县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('878', 'C30208', '桐梓县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('879', 'C30209', '凤冈县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('880', 'C30210', '务川仡佬族苗族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('881', 'C30211', '余庆县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('882', 'C30212', '绥阳县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('883', 'C30213', '道真仡佬族苗族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('884', 'C30214', '仁怀市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('885', 'C30215', '习水县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('886', 'C30216', '赤水市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('887', 'C30217', '正安县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('888', 'C30218', '安顺市西秀区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('889', 'C30219', '平坝县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('890', 'C30220', '普定县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('891', 'C30221', '镇宁布依族苗族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('892', 'C30222', '关岭布依族苗族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('893', 'C30223', '紫云苗族布依族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('894', 'C30224', '都匀市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('895', 'C30225', '独山县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('896', 'C30226', '平塘县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('897', 'C30227', '荔波县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('898', 'C30228', '三都水族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('899', 'C30229', '福泉市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('900', 'C30230', '瓮安县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('901', 'C30231', '贵定县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('902', 'C30232', '龙里县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('903', 'C30233', '惠水县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('904', 'C30234', '长顺县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('905', 'C30235', '罗甸县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('906', 'C30236', '凯里市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('907', 'C30237', '麻江县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('908', 'C30238', '雷山县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('909', 'C30239', '丹寨县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('910', 'C30240', '黄平县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('911', 'C30241', '施秉县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('912', 'C30242', '镇远县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('913', 'C30243', '岑巩县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('914', 'C30244', '三穗县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('915', 'C30245', '锦屏县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('916', 'C30246', '天柱县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('917', 'C30247', '剑河县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('918', 'C30248', '台江县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('919', 'C30249', '黎平县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('920', 'C30250', '榕江县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('921', 'C30251', '从江县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('922', 'C30252', '铜仁市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('923', 'C30253', '松桃苗族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('924', 'C30254', '江口县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('925', 'C30255', '石阡县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('926', 'C30256', '玉屏侗族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('927', 'C30257', '思南县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('928', 'C30258', '印江土家族苗族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('929', 'C30259', '沿河土家族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('930', 'C30260', '德江县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('931', 'C30261', '万山特区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('932', 'C30262', '毕节市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('933', 'C30263', '大方县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('934', 'C30264', '黔西县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('935', 'C30265', '金沙县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('936', 'C30266', '织金县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('937', 'C30267', '纳雍县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('938', 'C30268', '威宁县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('939', 'C30269', '赫章县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('940', 'C30270', '六枝特区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('941', 'C30271', '盘县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('942', 'C30272', '水城县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('943', 'C30273', '六盘水市钟山区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('944', 'C30274', '普安县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('945', 'C30275', '晴隆县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('946', 'C30276', '兴仁县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('947', 'C30277', '贞丰县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('948', 'C30278', '安龙县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('949', 'C30279', '册亨县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('950', 'C30280', '望谟县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('951', 'C30284', '沈阳市城区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('952', 'C30285', '新民市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('953', 'C30286', '辽中县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('954', 'C30287', '法库县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('955', 'C30288', '康平县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('956', 'C30289', '沈阳市沈北新区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('957', 'C30290', '沈阳市苏家屯区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('958', 'C30291', '沈阳市东陵区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('959', 'C30292', '沈阳市于洪区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('960', 'C30293', '庄河市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('961', 'C30294', '瓦房店市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('962', 'C30295', '普兰店市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('963', 'C30296', '长海县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('964', 'C30297', '大连市金州区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('965', 'C30298', '大连经济技术开发区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('966', 'C30299', '大连市旅顺口区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('967', 'C30302', '海城市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('968', 'C30303', '台安县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('969', 'C30304', '岫岩满族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('970', 'C30305', '鞍山市千山区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('971', 'C30306', '抚顺县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('972', 'C30307', '新宾满族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('973', 'C30308', '抚顺市顺城区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('974', 'C30309', '清原满族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('975', 'C30310', '抚顺市望花区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('976', 'C30311', '本溪市市区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('977', 'C30312', '本溪满族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('978', 'C30313', '桓仁满族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('979', 'C30314', '本溪市南芬区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('980', 'C30315', '东港市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('981', 'C30316', '凤城市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('982', 'C30317', '宽甸满族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('983', 'C30318', '丹东市城区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('984', 'C30319', '凌海市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('985', 'C30320', '北镇市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('986', 'C30321', '黑山县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('987', 'C30322', '锦州市太和区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('988', 'C30323', '锦州经济技术开发区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('989', 'C30325', '大石桥市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('990', 'C30326', '盖州市农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('991', 'C30327', '营口市老边区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('992', 'C30328', '营口经济技术开发区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('993', 'C30329', '阜新市城郊农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('994', 'C30330', '阜新市清河门区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('995', 'C30331', '阜新蒙古族自治县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('996', 'C30332', '彰武县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('997', 'C30334', '辽阳市弓长岭区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('998', 'C30335', '辽阳市宏伟区农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('999', 'C30336', '盘山县农村信用合作联社 ');
INSERT INTO bank (id, bank_code, bank_name) VALUES ('1000', 'C30337', '大洼县农村信用合作联社 ');


SET FOREIGN_KEY_CHECKS = 1;