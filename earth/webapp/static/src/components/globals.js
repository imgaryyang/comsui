import { Modal, ModalBody, ModalHeader, ModalFooter } from 'components/Modal';
import MessageBox from 'components/MessageBox';
import { Select, Option, SelectAllOption } from 'components/Select';
import { Address, Province, City, District } from 'components/Address';
import Cascader  from 'components/Cascader';
import Cascader2  from 'components/Cascader2';

var components =  {
    [Select.name]: Select,
    [Option.name]: Option,
    [SelectAllOption.name]: SelectAllOption,
    [Address.name]: Address,
    [Province.name]: Province,
    [City.name]: City, 
    [District.name]: District,
    [Cascader.name]: Cascader,
    [Cascader2.name]: Cascader2,
    
    Modal, ModalBody, ModalHeader, ModalFooter,
    DateTimePicker: require('components/DateTimePicker'),
    Breadcrumb: require('components/Breadcrumb'),
    PageControl: require('components/PageControl'),
};


export default components;