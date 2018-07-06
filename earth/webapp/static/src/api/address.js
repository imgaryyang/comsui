import { ajaxPromise } from 'assets/javascripts/util';

export function getProvince() {
    return ajaxPromise({
        url: '/area/getProvinceList',
        parse: data => data.provinceList
    });
}

export function getCity(provinceCode) {
    return ajaxPromise({
        url: '/area/getCityList',
        data: { provinceCode },
        parse: data => data.cityList
    });
}

export function getDistrict(cityCode, provinceCode) {
    return ajaxPromise({
        url: '/area/getDistrictList',
        data: { cityCode, provinceCode },
        parse: data => data.districtList
    });
}