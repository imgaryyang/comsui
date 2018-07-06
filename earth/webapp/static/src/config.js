const debug = process.env.NODE_ENV !== 'production';

export const production = process.env.DEPLOY_PRODUCTION;

const prefix = production == 'weifang' ? '/weifang' : ''

export const ctx = `${prefix}/v`; // 指向新框架
export const ctx_deprecated = `${prefix}`; // 指向就框架
export const api = `${prefix}`;
export const resource =  debug ? `${prefix}/static`: `${prefix}/static/dist`;

export const bankLogo = resource + '/images/bank-logo';
export const root = ctx;

export const thumbnail = 'http://zufangbao1.img-cn-shanghai.aliyuncs.com/';
export const original = 'http://zufangbao1.oss-cn-shanghai.aliyuncs.com/';