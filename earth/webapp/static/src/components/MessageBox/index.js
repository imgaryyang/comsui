import Vue from 'vue';
import MsgVueCfg from './MessageBox';

const doc = document;
const MessageBoxConstructor = Vue.extend(MsgVueCfg);
const instance  = new MessageBoxConstructor({el: doc.createElement('div')});

const TITLE = '提示';
const MESSAGE = '系统繁忙请稍候重试';
const BUTTONS = [{
    text: '关闭',
    type: 'default',
    handler: close
}];

function isHtml(str) {    
    var div = doc.createElement('div');
    div.innerHTML = str;
    return div.children.length > 0;
};

function processArguments(args) {
    let message;
    let title;
    let buttons;

    if (2 in args) {
        buttons = args[2];
        title = args[1];
        message = args[0];
    } else if (1 in args) {
        buttons = BUTTONS;
        title = args[1];
        message = args[0];
    } else if (0 in args) {
        buttons = BUTTONS;
        title = TITLE;
        message = args[0];
    }

    if (!title) {
        title = TITLE;
    }
    if (!message) {
        message = MESSAGE;
    }
    if (!buttons) {
        buttons = BUTTONS;
    }

    return { message, title, buttons };
}

function once(event, callback) {
    instance.$once(event, callback);
}

function open() {
    const args = Array.from(arguments);
    const { message, title, buttons } = processArguments(args);

    doc.body.appendChild(instance.$el);

    instance.message = typeof message === 'object' ? message.toString() : message,
    instance.title = title;
    instance.buttons = buttons;
    instance.messageIsHtml = isHtml(message);

    Vue.nextTick(() => {
        instance.visible = true;
    });
}

function close() {
    instance.visible = false;
}

// 没有必要暴露事件监听接口吧
export default { open, close, once };