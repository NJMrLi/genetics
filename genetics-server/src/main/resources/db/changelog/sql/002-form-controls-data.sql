-- =============================================
-- 表单控件初始化数据 - 基于实体类生成
-- =============================================

-- ========== Company 公司信息 ==========
INSERT INTO `form_control` (`control_name`, `control_key`, `control_type`, `placeholder`, `tips`, `required`, `min_length`, `max_length`, `sort`, `enabled`) VALUES
('公司编码', 'Company.code', 'INPUT', '请输入公司编码', '公司唯一编码', 0, NULL, 50, 100, 1),
('公司名称', 'Company.name', 'INPUT', '请输入公司名称', '请填写营业执照上的完整公司名称', 1, 2, 100, 101, 1),
('公司英文名称', 'Company.enname', 'INPUT', '请输入公司英文名称', '公司英文全称', 0, NULL, 200, 102, 1),
('公司简称', 'Company.abbr', 'INPUT', '请输入公司简称', '公司简称或缩写', 0, NULL, 50, 103, 1),
('公司邮箱', 'Company.email', 'INPUT', '请输入公司邮箱', '公司官方邮箱地址', 0, NULL, 100, 104, 1),
('公司联系电话', 'Company.fixedTelephone', 'INPUT', '请输入公司联系电话', '公司固定电话', 0, NULL, 30, 105, 1),
('国家地区ID', 'Company.regionId', 'NUMBER', '请输入国家地区ID', NULL, 0, NULL, NULL, 106, 1),
('国家地区编码(3位)', 'Company.regionCode', 'INPUT', '请输入国家地区编码', '3位国家编码，如DEU', 0, NULL, 10, 107, 1),
('国家地区编码(2位)', 'Company.regionCode2', 'INPUT', '请输入国家地区编码', '2位国家编码，如DE', 0, NULL, 5, 108, 1),
('国家地区名称', 'Company.regionName', 'INPUT', '请输入国家地区名称', NULL, 0, NULL, 50, 109, 1),
('证照类型', 'Company.typeId', 'SELECT', NULL, '1营业执照 2注册证书', 0, NULL, NULL, 110, 1),
('证照类型名称', 'Company.typeName', 'INPUT', '请输入证照类型名称', NULL, 0, NULL, 50, 111, 1),
('注册资本', 'Company.registeredCapital', 'INPUT', '请输入注册资本', '公司注册资本金额', 0, NULL, 50, 112, 1),
('注册资本币种名称', 'Company.currencyName', 'INPUT', '请输入币种名称', '如：人民币、美元', 0, NULL, 20, 113, 1),
('注册资本币种代码', 'Company.currencyCode', 'INPUT', '请输入币种代码', '如：CNY、USD', 0, NULL, 10, 114, 1),
('公司注册地', 'Company.registeredArea', 'INPUT', '请输入公司注册地', NULL, 0, NULL, 200, 115, 1),
('公司注册代码', 'Company.registerCode', 'INPUT', '请输入公司注册代码', NULL, 0, NULL, 50, 116, 1),
('统一社会信用代码', 'Company.socialCreditCode', 'INPUT', '请输入统一社会信用代码', '18位统一社会信用代码', 0, 18, 18, 117, 1),
('企业类型ID', 'Company.econKindId', 'SELECT', NULL, '企业类型', 0, NULL, NULL, 118, 1),
('企业类型名称', 'Company.econKindName', 'INPUT', '请输入企业类型名称', NULL, 0, NULL, 50, 119, 1),
('企业类型英文名称', 'Company.econKindEnname', 'INPUT', '请输入企业类型英文名称', NULL, 0, NULL, 100, 120, 1),
('成立日期', 'Company.startDate', 'DATE', '请选择成立日期', '公司成立日期', 0, NULL, NULL, 121, 1),
('发证日期', 'Company.issuedDate', 'DATE', '请选择发证日期', '营业执照发证日期', 0, NULL, NULL, 122, 1),
('德国本土税号', 'Company.gerLocalTaxNumber', 'INPUT', '请输入德国本土税号', '德国本土税号', 0, NULL, 50, 123, 1),
('本国VAT税号', 'Company.registryCountryTax', 'INPUT', '请输入本国VAT税号', NULL, 0, NULL, 50, 124, 1),
('公司银行账号', 'Company.companyBankAccount', 'INPUT', '请输入公司银行账号', '德国独有字段', 0, NULL, 50, 125, 1),
('公司IBAN', 'Company.companyIban', 'INPUT', '请输入公司IBAN', '国际银行账号', 0, NULL, 50, 126, 1),
('公司SWIFT/BIC', 'Company.companySwiftBic', 'INPUT', '请输入SWIFT/BIC代码', NULL, 0, NULL, 20, 127, 1),
('经营范围', 'Company.businessScope', 'TEXTAREA', '请输入经营范围', '公司经营范围描述', 0, NULL, 2000, 128, 1),
('是否启用', 'Company.enabled', 'SWITCH', NULL, '1启用 0停用', 0, NULL, NULL, 129, 1),
('公司银行英文名称', 'Company.companyBankEnName', 'INPUT', '请输入公司银行英文名称', NULL, 0, NULL, 100, 130, 1),
('公司银行英文地址', 'Company.companyBankEnAddress', 'TEXTAREA', '请输入公司银行英文地址', NULL, 0, NULL, 500, 131, 1),
('联系人姓名', 'Company.contactPerson', 'INPUT', '请输入联系人姓名', NULL, 0, NULL, 50, 132, 1),
('联系人电话区域代码', 'Company.phoneRegionCode', 'INPUT', '请输入电话区域代码', '如：+86', 0, NULL, 10, 133, 1),
('联系人电话', 'Company.contactPhone', 'INPUT', '请输入联系人电话', NULL, 0, NULL, 30, 134, 1),
('联系人邮箱', 'Company.contactEmail', 'INPUT', '请输入联系人邮箱', NULL, 0, NULL, 100, 135, 1),
('联系人职务', 'Company.contactPosition', 'INPUT', '请输入联系人职务', NULL, 0, NULL, 50, 136, 1),
('欧盟EORI注册国', 'Company.eoriCountryCode', 'INPUT', '请输入欧盟EORI注册国', NULL, 0, NULL, 10, 137, 1),
('欧盟EORI', 'Company.eoriEu', 'INPUT', '请输入欧盟EORI', NULL, 0, NULL, 50, 138, 1),
('英国EORI', 'Company.eoriEn', 'INPUT', '请输入英国EORI', NULL, 0, NULL, 50, 139, 1),
('法国SEPA', 'Company.sepa', 'SELECT', NULL, '法国SEPA授权', 0, NULL, NULL, 140, 1),
('进口记录类型', 'Company.importOfRecord', 'SELECT', NULL, 'BUYER或SELLER', 0, NULL, NULL, 141, 1),
('财年结束日期', 'Company.fiscalYearEndDate', 'DATE', '请选择财年结束日期', NULL, 0, NULL, NULL, 142, 1);

-- ========== CompanyAddress 公司地址 ==========
INSERT INTO `form_control` (`control_name`, `control_key`, `control_type`, `placeholder`, `tips`, `required`, `min_length`, `max_length`, `sort`, `enabled`) VALUES
('公司ID', 'CompanyAddress.companyId', 'NUMBER', NULL, '关联的公司ID', 0, NULL, NULL, 200, 1),
('省份ID', 'CompanyAddress.provinceId', 'NUMBER', NULL, NULL, 0, NULL, NULL, 201, 1),
('省份编码', 'CompanyAddress.provinceCode', 'INPUT', '请输入省份编码', NULL, 0, NULL, 20, 202, 1),
('省份名称', 'CompanyAddress.provinceName', 'INPUT', '请输入省份名称', NULL, 0, NULL, 50, 203, 1),
('省份英文名称', 'CompanyAddress.provinceEnname', 'INPUT', '请输入省份英文名称', NULL, 0, NULL, 100, 204, 1),
('城市ID', 'CompanyAddress.cityId', 'NUMBER', NULL, NULL, 0, NULL, NULL, 205, 1),
('城市编码', 'CompanyAddress.cityCode', 'INPUT', '请输入城市编码', NULL, 0, NULL, 20, 206, 1),
('城市名称', 'CompanyAddress.cityName', 'INPUT', '请输入城市名称', NULL, 0, NULL, 50, 207, 1),
('城市英文名称', 'CompanyAddress.cityEnname', 'INPUT', '请输入城市英文名称', NULL, 0, NULL, 100, 208, 1),
('区域ID', 'CompanyAddress.areaId', 'NUMBER', NULL, NULL, 0, NULL, NULL, 209, 1),
('区域编码', 'CompanyAddress.areaCode', 'INPUT', '请输入区域编码', NULL, 0, NULL, 20, 210, 1),
('区域名称', 'CompanyAddress.areaName', 'INPUT', '请输入区域名称', NULL, 0, NULL, 50, 211, 1),
('区域英文名称', 'CompanyAddress.areaEnname', 'INPUT', '请输入区域英文名称', NULL, 0, NULL, 100, 212, 1),
('详细地址', 'CompanyAddress.detailAddress', 'TEXTAREA', '请输入详细地址', '详细的街道地址', 0, NULL, 500, 213, 1),
('详细英文地址1', 'CompanyAddress.detailEnaddress1', 'INPUT', '请输入详细英文地址1', NULL, 0, NULL, 200, 214, 1),
('详细英文地址2', 'CompanyAddress.detailEnaddress2', 'INPUT', '请输入详细英文地址2', NULL, 0, NULL, 200, 215, 1),
('详细英文地址3', 'CompanyAddress.detailEnaddress3', 'INPUT', '请输入详细英文地址3', NULL, 0, NULL, 200, 216, 1),
('完整地址', 'CompanyAddress.completeAddress', 'TEXTAREA', '请输入完整地址', NULL, 0, NULL, 500, 217, 1),
('完整英文地址', 'CompanyAddress.completeEnaddress', 'TEXTAREA', '请输入完整英文地址', NULL, 0, NULL, 500, 218, 1),
('邮编', 'CompanyAddress.zipCode', 'INPUT', '请输入邮编', NULL, 0, NULL, 20, 219, 1),
('座机', 'CompanyAddress.fixedTelephone', 'INPUT', '请输入座机号码', NULL, 0, NULL, 30, 220, 1),
('地址类型ID', 'CompanyAddress.addressTypeId', 'SELECT', NULL, '1.注册地址', 0, NULL, NULL, 221, 1),
('地址类型名称', 'CompanyAddress.addressTypeName', 'INPUT', '请输入地址类型名称', NULL, 0, NULL, 50, 222, 1);

-- ========== CompanyBank 公司银行 ==========
INSERT INTO `form_control` (`control_name`, `control_key`, `control_type`, `placeholder`, `tips`, `required`, `min_length`, `max_length`, `sort`, `enabled`) VALUES
('公司ID', 'CompanyBank.companyId', 'NUMBER', NULL, '关联的公司ID', 0, NULL, NULL, 300, 1),
('银行账户', 'CompanyBank.bankAccount', 'INPUT', '请输入银行账户', '公司银行账户号码', 0, NULL, 50, 301, 1),
('IBAN账户', 'CompanyBank.companyIban', 'INPUT', '请输入IBAN账户', '国际银行账号', 0, NULL, 50, 302, 1),
('SWIFT/BIC代码', 'CompanyBank.companySwiftBic', 'INPUT', '请输入SWIFT/BIC代码', NULL, 0, NULL, 20, 303, 1),
('银行英文地址', 'CompanyBank.companyBankEnAddress', 'TEXTAREA', '请输入银行英文地址', NULL, 0, NULL, 500, 304, 1);

-- ========== CompanyContact 公司联系人 ==========
INSERT INTO `form_control` (`control_name`, `control_key`, `control_type`, `placeholder`, `tips`, `required`, `min_length`, `max_length`, `sort`, `enabled`) VALUES
('公司ID', 'CompanyContact.companyId', 'NUMBER', NULL, '关联的公司ID', 0, NULL, NULL, 400, 1),
('联系人姓名', 'CompanyContact.contactPerson', 'INPUT', '请输入联系人姓名', NULL, 1, 2, 50, 401, 1),
('联系电话', 'CompanyContact.contactPhone', 'INPUT', '请输入联系电话', NULL, 0, NULL, 30, 402, 1),
('联系人邮箱', 'CompanyContact.contactEmail', 'INPUT', '请输入联系人邮箱', NULL, 0, NULL, 100, 403, 1),
('联系人职务', 'CompanyContact.contactPosition', 'INPUT', '请输入联系人职务', NULL, 0, NULL, 50, 404, 1),
('第三方联系方式类型', 'CompanyContact.otherContactType', 'SELECT', NULL, '第三方联系方式类型', 0, NULL, NULL, 405, 1),
('第三方联系方式', 'CompanyContact.otherContact', 'INPUT', '请输入第三方联系方式', NULL, 0, NULL, 100, 406, 1),
('是否主要联系人', 'CompanyContact.main', 'SWITCH', NULL, '1是 0否', 0, NULL, NULL, 407, 1),
('电话区域号码', 'CompanyContact.phoneRegionCode', 'INPUT', '请输入电话区域号码', '如：+86', 0, NULL, 10, 408, 1);

-- ========== CompanyUser 公司用户 ==========
INSERT INTO `form_control` (`control_name`, `control_key`, `control_type`, `placeholder`, `tips`, `required`, `min_length`, `max_length`, `sort`, `enabled`) VALUES
('公司ID', 'CompanyUser.companyId', 'NUMBER', NULL, '关联的公司ID', 0, NULL, NULL, 500, 1),
('用户ID', 'CompanyUser.userId', 'NUMBER', NULL, '用户数据ID', 0, NULL, NULL, 501, 1),
('客户账号', 'CompanyUser.userAccount', 'INPUT', '请输入客户账号', NULL, 0, NULL, 50, 502, 1),
('手机号码', 'CompanyUser.userMobile', 'INPUT', '请输入手机号码', NULL, 0, NULL, 20, 503, 1),
('邮箱', 'CompanyUser.userEmail', 'INPUT', '请输入邮箱', NULL, 0, NULL, 100, 504, 1);

-- ========== CompanyPersonnel 公司人员 ==========
INSERT INTO `form_control` (`control_name`, `control_key`, `control_type`, `placeholder`, `tips`, `required`, `min_length`, `max_length`, `sort`, `enabled`) VALUES
('公司ID', 'CompanyPersonnel.companyId', 'NUMBER', NULL, '关联的公司ID', 0, NULL, NULL, 600, 1),
('人员类型ID', 'CompanyPersonnel.typeId', 'SELECT', NULL, '1.法人 2.联系人', 0, NULL, NULL, 601, 1),
('人员类型名称', 'CompanyPersonnel.typeName', 'INPUT', '请输入人员类型名称', NULL, 0, NULL, 20, 602, 1),
('姓名', 'CompanyPersonnel.name', 'INPUT', '请输入姓名', NULL, 1, 2, 50, 603, 1),
('英文名', 'CompanyPersonnel.enname', 'INPUT', '请输入英文名', NULL, 0, NULL, 100, 604, 1),
('名(拼音/英文)', 'CompanyPersonnel.firstName', 'INPUT', '请输入名', '姓名首拼音或英文', 0, NULL, 50, 605, 1),
('姓(拼音/英文)', 'CompanyPersonnel.secondName', 'INPUT', '请输入姓', '姓名剩余拼音或英文', 0, NULL, 50, 606, 1),
('证件类型', 'CompanyPersonnel.certificatesNoType', 'SELECT', NULL, '身份证、护照', 0, NULL, NULL, 607, 1),
('证件号', 'CompanyPersonnel.certificatesNo', 'INPUT', '请输入证件号', NULL, 0, NULL, 30, 608, 1),
('职位ID', 'CompanyPersonnel.jobId', 'SELECT', NULL, NULL, 0, NULL, NULL, 609, 1),
('职位名称', 'CompanyPersonnel.jobName', 'INPUT', '请输入职位名称', NULL, 0, NULL, 50, 610, 1),
('联系电话', 'CompanyPersonnel.telephone', 'INPUT', '请输入联系电话', NULL, 0, NULL, 30, 611, 1),
('联系邮箱', 'CompanyPersonnel.email', 'INPUT', '请输入联系邮箱', NULL, 0, NULL, 100, 612, 1),
('性别', 'CompanyPersonnel.sex', 'SELECT', NULL, '1男 2女', 0, NULL, NULL, 613, 1),
('称谓', 'CompanyPersonnel.salutation', 'SELECT', NULL, 'MR先生 MRS女士', 0, NULL, NULL, 614, 1),
('法人英文地址', 'CompanyPersonnel.legalPersonEnAddress', 'TEXTAREA', '请输入法人英文地址', NULL, 0, NULL, 500, 615, 1),
('法人中文地址', 'CompanyPersonnel.legalPersonAddress', 'TEXTAREA', '请输入法人中文地址', NULL, 0, NULL, 500, 616, 1),
('法人生日', 'CompanyPersonnel.legalPersonBirth', 'DATE', '请选择法人生日', NULL, 0, NULL, NULL, 617, 1),
('法人地址邮编', 'CompanyPersonnel.legalPersonZipCode', 'INPUT', '请输入法人地址邮编', NULL, 0, NULL, 20, 618, 1);

-- ========== CompanySales 公司店铺 ==========
INSERT INTO `form_control` (`control_name`, `control_key`, `control_type`, `placeholder`, `tips`, `required`, `min_length`, `max_length`, `sort`, `enabled`) VALUES
('公司ID', 'CompanySales.companyId', 'NUMBER', NULL, '关联的公司ID', 0, NULL, NULL, 700, 1),
('第三方ID', 'CompanySales.saleItem', 'NUMBER', NULL, NULL, 0, NULL, NULL, 701, 1),
('销售平台ID', 'CompanySales.salePlatformId', 'SELECT', NULL, '销售平台类型', 0, NULL, NULL, 702, 1),
('销售平台名称', 'CompanySales.salePlatformName', 'INPUT', '请输入销售平台名称', NULL, 0, NULL, 50, 703, 1),
('店铺ID', 'CompanySales.shopId', 'INPUT', '请输入店铺ID', NULL, 0, NULL, 50, 704, 1),
('店铺名称', 'CompanySales.shopName', 'INPUT', '请输入店铺名称', NULL, 0, NULL, 100, 705, 1),
('店铺URL', 'CompanySales.shopUrl', 'INPUT', '请输入店铺URL', NULL, 0, NULL, 500, 706, 1),
('经营范围', 'CompanySales.businessScope', 'TEXTAREA', '请输入经营范围', NULL, 0, NULL, 500, 707, 1),
('经营范围ID', 'CompanySales.businessScopeId', 'SELECT', NULL, NULL, 0, NULL, NULL, 708, 1),
('经营范围代码', 'CompanySales.businessScopeCode', 'INPUT', '请输入经营范围代码', NULL, 0, NULL, 20, 709, 1),
('预计年销售额(万元)', 'CompanySales.saleYearAmount', 'NUMBER', '请输入预计年销售额', NULL, 0, NULL, NULL, 710, 1),
('数据来源', 'CompanySales.source', 'SELECT', NULL, '0服务单 1手动创建', 0, NULL, NULL, 711, 1),
('授权状态', 'CompanySales.authStatus', 'SELECT', NULL, '0未授权 1已授权 2已失效', 0, NULL, NULL, 712, 1),
('授权时间', 'CompanySales.authTime', 'DATE', '请选择授权时间', NULL, 0, NULL, NULL, 713, 1),
('授权预计过期时间', 'CompanySales.authExpectedExpiry', 'DATE', '请选择授权预计过期时间', NULL, 0, NULL, NULL, 714, 1),
('当地仓库地址', 'CompanySales.stockAddress', 'TEXTAREA', '请输入当地仓库地址', NULL, 0, NULL, 500, 715, 1);

-- ========== CompanyOverseaManage 海外经营方式 ==========
INSERT INTO `form_control` (`control_name`, `control_key`, `control_type`, `placeholder`, `tips`, `required`, `min_length`, `max_length`, `sort`, `enabled`) VALUES
('公司ID', 'CompanyOverseaManage.companyId', 'NUMBER', NULL, '关联的公司ID', 0, NULL, NULL, 800, 1),
('公司注册号', 'CompanyOverseaManage.registrationNumber', 'INPUT', '请输入公司注册号', NULL, 0, NULL, 50, 801, 1),
('一级运营类目编码', 'CompanyOverseaManage.operationCategoryCode1', 'INPUT', '请输入一级运营类目编码', NULL, 0, NULL, 20, 802, 1),
('一级运营类目名称', 'CompanyOverseaManage.operationCategoryName1', 'INPUT', '请输入一级运营类目名称', NULL, 0, NULL, 50, 803, 1),
('二级运营类目编码', 'CompanyOverseaManage.operationCategoryCode2', 'INPUT', '请输入二级运营类目编码', NULL, 0, NULL, 20, 804, 1),
('二级运营类目名称', 'CompanyOverseaManage.operationCategoryName2', 'INPUT', '请输入二级运营类目名称', NULL, 0, NULL, 50, 805, 1),
('国家地区名称', 'CompanyOverseaManage.regionName', 'INPUT', '请输入国家地区名称', NULL, 0, NULL, 50, 806, 1),
('国家地区编码(2位)', 'CompanyOverseaManage.regionCode2', 'INPUT', '请输入国家地区编码', NULL, 0, NULL, 5, 807, 1),
('国家地区编码(3位)', 'CompanyOverseaManage.regionCode', 'INPUT', '请输入国家地区编码', NULL, 0, NULL, 10, 808, 1),
('海外经营方式ID', 'CompanyOverseaManage.overseaModeId', 'SELECT', NULL, NULL, 0, NULL, NULL, 809, 1),
('海外经营方式名称', 'CompanyOverseaManage.overseaModeName', 'INPUT', '请输入海外经营方式名称', NULL, 0, NULL, 50, 810, 1);

-- ========== CompanyEprCategoryBrand EPR品牌分类 ==========
INSERT INTO `form_control` (`control_name`, `control_key`, `control_type`, `placeholder`, `tips`, `required`, `min_length`, `max_length`, `sort`, `enabled`) VALUES
('公司ID', 'CompanyEprCategoryBrand.companyId', 'NUMBER', NULL, '关联的公司ID', 0, NULL, NULL, 900, 1),
('运营类目编码', 'CompanyEprCategoryBrand.operationCategoryCode', 'INPUT', '请输入运营类目编码', '如020201', 0, NULL, 20, 901, 1),
('运营类目名称', 'CompanyEprCategoryBrand.operationCategoryName', 'INPUT', '请输入运营类目名称', '如包装法', 0, NULL, 50, 902, 1),
('国家地区编码(3位)', 'CompanyEprCategoryBrand.regionCode', 'INPUT', '请输入国家地区编码', NULL, 0, NULL, 10, 903, 1),
('国家地区编码(2位)', 'CompanyEprCategoryBrand.regionCode2', 'INPUT', '请输入国家地区编码', NULL, 0, NULL, 5, 904, 1),
('国家地区名称', 'CompanyEprCategoryBrand.regionName', 'INPUT', '请输入国家地区名称', NULL, 0, NULL, 50, 905, 1),
('产品分类', 'CompanyEprCategoryBrand.categoryName', 'INPUT', '请输入产品分类', NULL, 0, NULL, 100, 906, 1),
('品牌名称', 'CompanyEprCategoryBrand.brandName', 'INPUT', '请输入品牌名称', NULL, 0, NULL, 100, 907, 1),
('注册号', 'CompanyEprCategoryBrand.registrationNumber', 'INPUT', '请输入注册号', NULL, 0, NULL, 50, 908, 1),
('注册状态', 'CompanyEprCategoryBrand.registrationStatus', 'SELECT', NULL, '0注册 1注销', 0, NULL, NULL, 909, 1);

-- ========== CompanyTaxNo 公司税号 ==========
INSERT INTO `form_control` (`control_name`, `control_key`, `control_type`, `placeholder`, `tips`, `required`, `min_length`, `max_length`, `sort`, `enabled`) VALUES
('公司ID', 'CompanyTaxNo.companyId', 'NUMBER', NULL, '关联的公司ID', 0, NULL, NULL, 1000, 1),
('国家ID', 'CompanyTaxNo.countryId', 'NUMBER', NULL, NULL, 0, NULL, NULL, 1001, 1),
('国家编码', 'CompanyTaxNo.countryCode', 'INPUT', '请输入国家编码', NULL, 0, NULL, 10, 1002, 1),
('国家名称', 'CompanyTaxNo.countryName', 'INPUT', '请输入国家名称', NULL, 0, NULL, 50, 1003, 1),
('国家英文名称', 'CompanyTaxNo.countryEnname', 'INPUT', '请输入国家英文名称', NULL, 0, NULL, 100, 1004, 1),
('法案代码', 'CompanyTaxNo.serviceCode', 'INPUT', '请输入法案代码', NULL, 0, NULL, 20, 1005, 1),
('法案名称', 'CompanyTaxNo.serviceName', 'INPUT', '请输入法案名称', NULL, 0, NULL, 50, 1006, 1),
('税号', 'CompanyTaxNo.taxNo', 'INPUT', '请输入税号', NULL, 1, NULL, 50, 1007, 1),
('税号类型ID', 'CompanyTaxNo.taxNoTypeId', 'SELECT', NULL, '1VAT税号 2VAT税务识别号 3包装法注册号', 0, NULL, NULL, 1008, 1),
('税号类型名称', 'CompanyTaxNo.taxNoTypeName', 'INPUT', '请输入税号类型名称', NULL, 0, NULL, 50, 1009, 1),
('服务状态ID', 'CompanyTaxNo.statusId', 'SELECT', NULL, '1无服务 10服务未开始 20服务中 30服务过期 40已终止', 0, NULL, NULL, 1010, 1),
('服务状态名称', 'CompanyTaxNo.statusName', 'INPUT', '请输入服务状态名称', NULL, 0, NULL, 50, 1011, 1),
('授权状态ID', 'CompanyTaxNo.authStatusId', 'SELECT', NULL, '10未授权 20已授权 30无授权', 0, NULL, NULL, 1012, 1),
('授权状态名称', 'CompanyTaxNo.authStatusName', 'INPUT', '请输入授权状态名称', NULL, 0, NULL, 50, 1013, 1),
('服务开始时间', 'CompanyTaxNo.serviceStartTime', 'DATE', '请选择服务开始时间', NULL, 0, NULL, NULL, 1014, 1),
('服务结束时间', 'CompanyTaxNo.serviceEndTime', 'DATE', '请选择服务结束时间', NULL, 0, NULL, NULL, 1015, 1),
('授权开始时间', 'CompanyTaxNo.authStartTime', 'DATE', '请选择授权开始时间', NULL, 0, NULL, NULL, 1016, 1),
('授权失效时间', 'CompanyTaxNo.authEndTime', 'DATE', '请选择授权失效时间', NULL, 0, NULL, NULL, 1017, 1),
('账号', 'CompanyTaxNo.account', 'INPUT', '请输入账号', NULL, 0, NULL, 100, 1018, 1),
('密码', 'CompanyTaxNo.password', 'INPUT', '请输入密码', NULL, 0, NULL, 100, 1019, 1),
('英国HMRC CDS账号', 'CompanyTaxNo.hmrcCdsAccount', 'INPUT', '请输入HMRC CDS账号', NULL, 0, NULL, 100, 1020, 1),
('英国HMRC CDS密码', 'CompanyTaxNo.hmrcCdsPassword', 'INPUT', '请输入HMRC CDS密码', NULL, 0, NULL, 100, 1021, 1),
('验证手机号', 'CompanyTaxNo.phone', 'INPUT', '请输入验证手机号', NULL, 0, NULL, 20, 1022, 1),
('申报周期ID', 'CompanyTaxNo.declarationCycleId', 'SELECT', NULL, '1季度申报 2月申报 3年申报', 0, NULL, NULL, 1023, 1),
('申报周期名称', 'CompanyTaxNo.declarationCycleName', 'INPUT', '请输入申报周期名称', NULL, 0, NULL, 50, 1024, 1),
('是否生成申报义务', 'CompanyTaxNo.hasDeclarationObligation', 'SWITCH', NULL, '0未生成 1已生成', 0, NULL, NULL, 1025, 1),
('付款备注', 'CompanyTaxNo.paymentRemarks', 'TEXTAREA', '请输入付款备注', NULL, 0, NULL, 500, 1026, 1),
('下号日期', 'CompanyTaxNo.signUnderDate', 'DATE', '请选择下号日期', NULL, 0, NULL, NULL, 1027, 1),
('提交资料日期', 'CompanyTaxNo.submitInformationDate', 'DATE', '请选择提交资料日期', NULL, 0, NULL, NULL, 1028, 1),
('本土税号', 'CompanyTaxNo.localTaxNumber', 'INPUT', '请输入本土税号', '德国VAT用', 0, NULL, 50, 1029, 1),
('税率', 'CompanyTaxNo.taxRate', 'NUMBER', '请输入税率', NULL, 0, NULL, NULL, 1030, 1),
('税号生效时间', 'CompanyTaxNo.vrnEffectiveTime', 'DATE', '请选择税号生效时间', NULL, 0, NULL, NULL, 1031, 1),
('税号是否注销', 'CompanyTaxNo.vrnHasCancelled', 'SWITCH', NULL, '0否 1是', 0, NULL, NULL, 1032, 1),
('税号状态', 'CompanyTaxNo.vrnActiveStatus', 'SELECT', NULL, '1正常 2终止', 0, NULL, NULL, 1033, 1),
('税号提供方类型ID', 'CompanyTaxNo.serviceProviderType', 'SELECT', NULL, NULL, 0, NULL, NULL, 1034, 1),
('税号提供方名称', 'CompanyTaxNo.serviceProviderName', 'INPUT', '请输入税号提供方名称', NULL, 0, NULL, 100, 1035, 1),
('变更原因', 'CompanyTaxNo.changeReason', 'TEXTAREA', '请输入变更原因', NULL, 0, NULL, 500, 1036, 1),
('回收组织ID', 'CompanyTaxNo.recoveryOrganizeId', 'SELECT', NULL, NULL, 0, NULL, NULL, 1037, 1),
('回收组织名称', 'CompanyTaxNo.recoveryOrganizeName', 'INPUT', '请输入回收组织名称', NULL, 0, NULL, 100, 1038, 1),
('收款IBAN', 'CompanyTaxNo.paymentInfo.iban', 'INPUT', '请输入收款IBAN', '国际银行账号', 0, NULL, 50, 1039, 1);

-- ========== CompanyAttachment 公司附件 ==========
INSERT INTO `form_control` (`control_name`, `control_key`, `control_type`, `placeholder`, `tips`, `required`, `min_length`, `max_length`, `sort`, `enabled`) VALUES
('公司ID', 'CompanyAttachment.companyId', 'NUMBER', NULL, '关联的公司ID', 0, NULL, NULL, 1100, 1),
('附件上传', 'CompanyAttachment.file', 'UPLOAD', NULL, '上传公司相关附件', 0, NULL, NULL, 1101, 1);

-- ========== CompanyTaxNoAttachment 公司税号附件 ==========
INSERT INTO `form_control` (`control_name`, `control_key`, `control_type`, `placeholder`, `tips`, `required`, `min_length`, `max_length`, `sort`, `enabled`) VALUES
('公司税号ID', 'CompanyTaxNoAttachment.companyTaxNoId', 'NUMBER', NULL, '关联的公司税号ID', 0, NULL, NULL, 1200, 1),
('税号附件上传', 'CompanyTaxNoAttachment.file', 'UPLOAD', NULL, '上传税号相关附件', 0, NULL, NULL, 1201, 1);
