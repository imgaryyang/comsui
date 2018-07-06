export default [
//一、基础问题
    //1.单据关系图
    {
        question: '放款与还款的单据关系图',
        answer: [],
        imgSrc: ['base1.1','base1.2'],
        mainType: 'base',
        minorType: 'b1',
        rate: 0
    },
    //2.名词解析
    {
        question: '放款策略',
        answer: ['放款策略主要有优先级放款策略和先放后扣策略两种策略。单向放款模式与多向放款模式均为优先级放款策略类型，放扣联动放款模式为先放后扣策略类型。'],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b2',
        rate: 0
    },
    {
        question: '放款模式',
        answer: [
            '①单向放款：放款计划中只有一个放款对象。',
            '②多向放款：放款计划中有多个放款对象，可根据对象分成1-N优先级，放款单操作按优先级顺序进行放款；也可以对多个放款对象同时进行放款。',
            '③放扣联动：先放款到借款人账户，再立即将此款项划扣至商户账户。',
            '④线下放款：未通过本系统进行的放款操作。'
        ],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b2',
        rate: 0
    },
    {
        question: '计划订单',
        answer: ['系统接受到放款指令会自动生成的单据。'],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b2',
        rate: 0
    },
    {
        question: '放款单',
        answer: ['根据放款模式（单向放款、多向放款、放扣联动、线下放款，线下放款不生成放款单）的不同，由每一个计划订单自动拆分成N（N≥1）个的单据。'],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b2',
        rate: 0
    },
    {
        question: '线上代付单',
        answer: ['每一个放款单会在每一次放款执行时生成的一张单据。'],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b2',
        rate: 0
    },
    {
        question: '还款模式',
        answer: [
            '①线上还款：商户或贷后系统在应还日向指定借款人账户发起扣款。',
            '②商户代偿：商户主动对资产进行还款的行为。', '③个人主动还款：借款人个人主动对资产进行还款的行为，此类型分主动付款（本人还款）和他人代偿（打款人非借款人）。'
        ],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b2',
        rate: 0
    },
    {
        question: '还款类型',
        answer: [
            '①提前还款：还款计划在计划还款日前完成还款。',
            '②正常还款：还款计划在应还日成功完成还款。',
            '③逾期还款：还款计划在超过应还日或宽限日后完成还款。'
        ],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b2',
        rate: 0
    },
    {
        question: '还款计划',
        answer: ['资产包导入成功后，根据资产包相应信息生成的计划。'],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b2',
        rate: 0
    },
    {
        question: '结算单',
        answer: [
            '①系统在还款日开始当天根据还款计划生成的单据。结算单每天生成一张（结算单可拆分），直至还款全部成功。',
            '②资产方或资金方先进行扣款然后反向生成的单据。',
            '③由商户/主动付款凭证反向生成的单据。'
        ],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b2',
        rate: 0
    },
    {
        question: '扣款订单',
        answer: [
            '①系统根据结算单状态生成的扣款单据。',
            '②资产方或资金方发起代扣请求，系统生成的扣款单据。'
        ],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b2',
        rate: 0
    },
    {
        question: '线上支付单',
        answer: ['系统根据扣款订单生成的单据。'],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b2',
        rate: 0
    },
    {
        question: '担保补足单',
        answer: ['当还款计划过了物理逾期日（过了免息天数），系统生成的单据。'],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b2',
        rate: 0
    },
    {
        question: '担保清算单',
        answer: ['商户先进行了打款（担保补足款），然后借款人还款后生成的单据。'],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b2',
        rate: 0
    },
    {
        question: '商户回购单',
        answer: ['贷款合同触发对方回购逻辑自动生成的单据，然后由商户进行回购。可手工生成。'],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b2',
        rate: 0
    },
    {
        question: '商户付款凭证分以下五种情况',
        answer: [
            '①代偿：当还款计划未逾期时，由商户代偿还计划还款金额。',
            '②担保补足：当还款计划已逾期请况时，由商户支付相应还款金额进行担保。',
            '③回购：当贷款合同发生坏账时，由商户回购整个贷款合同。',
            '④差额划拨：借款人进行了一部分还款，由商户对剩余还款金额进行补足。',
            '⑤委托转付：借款人将钱还给商户，再由商户转付这笔计划还款金额。'
        ],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b2',
        rate: 0
    },
    {
        question: '第三方支付凭证',
        answer: ['通过第三方机构进行代扣的还款依据。'],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b2',
        rate: 0
    },
    //3.状态位解析
    {
        question: '贷款合同状态',
        answer: [
            '①放款中：当前贷款合同正在放款时的状态。',
            '②未生效：在放款成功后，贷款合同未生成/未激活时的状态。',
            '③已生效：资产包状态已激活后，此时贷款合同的状态。',
            '④异常中止：当前贷款合同作废的状态。',
            '⑤回购中：已生成回购单，等待商户打款回购的状态。',
            '⑥已回购：当前贷款合同已经回购成功的状态。',
            '⑦违约：商户在合同规定时间内未出资回购的状态。'
        ],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b3',
        rate: 0
    },
    {
        question: '线上代付单放款状态',
        answer: [
            '①已创建：线上代付单成功创建的状态。',
            '②本端处理中：此线上代付单信息还在系统中排队等待处理的状态。',
            '③对端处理中：此线上代付单信息已到达银行进行处理的状态。',
            '④成功：成功将款项打至用户指定银行账户的状态。',
            '⑤失败：未成功将款项打至用户指定银行账户的状态。',
            '⑥异常：预留状态位。',
            '⑦撤销：成功撤回放款指令操作，未将款项打出的状态。'
        ],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b3',
        rate: 0
    },
    {
        question: '还款状态',
        answer: [
            '①未到期：还款计划还未到期的状态。',
            '②待处理：到计划还款日但未到系统开始处理扣款的状态。',
            '③处理中：支付单在本端系统中开始处理的状态。 支付单操作未成功，返回处理中状态。',
            '④扣款中：支付单已到达对端并正在处理本次扣款的状态。',
            '⑤成功：借款人还款成功的状态。',
            '⑥失败：当前支付单操作未成功，没有扣取还款的状态。',
            '⑦回购中：已生成回购单，等待商户打款回购的状态。',
            '⑧已回购：当前贷款合同已经回购成功的状态。',
            '⑨违约：商户在合同规定时间内未出资回购的状态。'
        ],
        imgSrc: [],
        mainType: 'base',
        minorType: 'b3',
        rate: 0
    },
//二、流程问题
    //1.整体流程图
    {
        question: '整体流程图',
        answer: [],
        imgSrc: ['process1'],
        mainType: 'process',
        minorType: 'p1',
        rate: 0
    },
    //2.放款
    {
        question: '如果放款失败后，应如何处理？',
        answer: ['此时需要核对银企直连流水明细，确认此单是否需要冲账。确认之后若需要冲账，但未冲账，联系银行对接人查询具体情况，若不需要冲账或已冲账，则在确认后联系对手方进行重新放款。如果一个计划订单对应N个放款单，有一个放款单放款成功，其余N-1个放款单放款失败，可以重新执行放款。'],
        imgSrc: [],
        mainType: 'process',
        minorType: 'p2',
        rate: 1
    },
    {
        question: '如果放款单处理中时间超过半小时应该如何处理？',
        answer: ['先确认银行是否收到放款请求，若确认银行已收到放款请求，联系对接人查询具体情况，根据反馈的具体信息与技术确认最终处理方案。若银行未收到放款请求，由技术介入。需要查看通道流水、贷款合同数量，进行核对。'],
        imgSrc: [],
        mainType: 'process',
        minorType: 'p2',
        rate: 0
    },
    //3.导入资产包
    {
        question: '如果放款成功了但资产包未导入，应该如何处理？',
        answer: ['核查资产包是否进行了导入操作，发现异常之后，告知五维金融运维团队回调资产包，并和技术团队落实原因。'],
        imgSrc: [],
        mainType: 'process',
        minorType: 'p3',
        rate: 0
    },
    //4.还款
    {
        question: '如果出现扣款失败后，应该如何处理？',
        answer: [
            '扣款失败原因基本分为五类：',
            'a.余额不足：运营需要确认是否余额真实不足，然后作出相应操作。',
            'b.渠道不支持或者商户不支持此渠道：一般是限额原因，邮件联系第三方系统对接人询问是否需要拆分扣款。',
            'c.银行系统关闭：等银行维护结束，等待反馈结果。失败单据可以重新发起扣款。',
            'd.其他（账户销卡、账户冻结、账户余额不足、银行为附属卡不能扣款、因帐户长期不动帐请到柜台办理业务、超出了单卡累计次数限制等）：确认真实原因并作出相应操作。',
            'e.系统原因：通知运维和技术对系统进行修复，修复完成后系统自动扣款。'
        ],
        imgSrc: [],
        mainType: 'process',
        minorType: 'p4',
        rate: 0
    },
    {
        question: '如果扣款单处理中时间超过半小时应该如何处理？',
        answer: ['运营关注，与五维技术确认第三方支付是否收到扣款请求，若第三方支付已收到扣款请求，联系相关对接人查询具体情况，根据反馈情况处理。'],
        imgSrc: [],
        mainType: 'process',
        minorType: 'p4',
        rate: 0
    },
    {
        question: '如果扣款单金额与T+1日第三方支付清算金额异常，应该如何处理？',
        answer: ['运营关注T日扣款金额与T+1日第三方支付清算金额，如清算金额多，与信托方联系是否是财务线下扣款造成的，及时关联订单；如清算金额少，联系第三方机构确认原因。'],
        imgSrc: [],
        mainType: 'process',
        minorType: 'p4',
        rate: 0
    },
//三、操作问题
    //1.资产管理
    {
        question: '贷款合同中提前还款操作的前提条件是什么，并且应该怎样操作？',
        answer: [
            '前提条件：存在还未到期的还款计划。',
            '操作流程：①确认是否存在还未到期的还款计划；②点击提前还款按钮；③确认提前还款金额；④查看还款计划版本是否更新。'
        ],
        imgSrc: ['operation/data/data1.1', 'operation/data/data1.2'],
        mainType: 'operation',
        minorType: 'op1',
        rate: 0
    },
    {
        question: '贷款合同中关闭合同操作的前提条件是什么，并且应该怎样操作？',
        answer: ['收到资产方或资金方的通知，并且此批资产的还款计划都未到期，借款人尚未进行过任何还款。'],
        imgSrc: ['operation/data/data2.1', 'operation/data/data2.2'],
        mainType: 'operation',
        minorType: 'op1',
        rate: 0
    },
    {
        question: '贷款合同中回购操作的前提条件是什么，并且应该怎样操作？',
        answer: ['未满足贷款坏账条件，由资产方或资金方提出了回购请求，那么便可执行坏账回购。在还款计划未完成之前，贷款合同均能被手动回购。'],
        imgSrc: ['operation/data/data3.1', 'operation/data/data3.2'],
        mainType: 'operation',
        minorType: 'op1',
        rate: 0
    },
    {
        question: '贷款合同中的编辑操作作用是什么？',
        answer: ['应实际需求，运营人员可以对贷款合同中的还款计划进行编辑更改，然后生成新的还款计划版本。编辑操作主要作用于三种情况：a.还款计划进行项目结清；b.还款计划进行提前部分还款；c.还款计划进行错误更正。编辑时本金与原本金必须相等。'],
        imgSrc: ['operation/data/data4.1', 'operation/data/data4.2'],
        mainType: 'operation',
        minorType: 'op1',
        rate: 0
    },
    {
        question: '为何需要新增主动付款凭证，并且应该如何操作？',
        answer: ['当借款人进行线下还款操作时，需要新增主动付款凭证作为该借款人的还款依据。新增时，可以通过信托项目与借款人名称筛选出相应的贷款合同编号，此处一条流水可以支持多个贷款合同。若凭证类型选择主动付款，则会自动填充还款方账号、还款方户名和账户开户行三条信息，若凭证类型选择他人代偿，则三条信息均需手动输入。用户付款时产生的付款流水号若获取不到，则需填写与当前系统所存在的付款流水号不同的流水号信息。流水信息会根据前三项信息加载，业务信息会根据贷款合同编号加载。'],
        imgSrc: ['operation/data/data5.1', 'operation/data/data5.2'],
        mainType: 'operation',
        minorType: 'op1',
        rate: 0
    },
    {
        question: '资产包导入方式有哪些？',
        answer: ['①放款后自动导入；②若没自动导入资产包则需要运营人员人工导入。导入后需要手动激活。'],
        imgSrc: ['operation/data/data6.1', 'operation/data/data6.2'],
        mainType: 'operation',
        minorType: 'op1',
        rate: 1
    },
    {
        question: '何时允许执行计划订单中“重新回调结果”？',
        answer: ['当发生放款回调结果异常时，运营人员与资产方或资金方确认后，重新执行回调操作。'],
        imgSrc: ['operation/data/data7'],
        mainType: 'operation',
        minorType: 'op1',
        rate: 1
    },
    //2.信托管理
    {
        question: '怎么进行新增信托合同操作？',
        answer: ['点击信托管理-信托合同下的新增合同按钮，根据相关步骤补充相应参数完成新增。'],
        imgSrc: ['operation/finacial/finacial1.1', 'operation/finacial/finacial1.2'],
        mainType: 'operation',
        minorType: 'op2',
        rate: 1
    },
    {
        question: '如何对通道进行配置管理？',
        answer: ['放款和还款增加新的通道配置目前需要技术人员在后台完成，运营人员仅可以对已有通道进行修改，点击“详情”重新配置通道基础信息。'],
        imgSrc: ['operation/finacial/finacial2.1', 'operation/finacial/finacial2.2', 'operation/finacial/finacial2.3'],
        mainType: 'operation',
        minorType: 'op2',
        rate: 1
    },
    {
        question: '如何对银行方限额调整？',
        answer: ['若运营人员收到银行方通知限额调整，则可在：信托管理-通道管理-第三方限额页面中点击“编辑”修改。'],
        imgSrc: ['operation/finacial/finacial3.1', 'operation/finacial/finacial3.2'],
        mainType: 'operation',
        minorType: 'op2',
        rate: 1
    },
    //3.还款管理
    {
        question: '如何拆分结算单？',
        answer: ['因为银行限额或者其他原因，会需要运营手动拆分结算单。点击要拆分的“还款编号”，在详情页点“拆分”进行操作。拆分后立即进行扣款。针对线下还款的还款计划做的拆分结算单需要马上关闭。'],
        imgSrc: ['operation/repayment/repay1.1', 'operation/repayment/repay1.2'],
        mainType: 'operation',
        minorType: 'op3',
        rate: 1
    },
    //4.资金管理
    {
        question: '如何查询银企直联流水中专户的实时余额？',
        answer: ['在银企直联流水界面中选择相关专户，在选项中点击显示余额便可查看。'],
        imgSrc: ['operation/capital/capital1'],
        mainType: 'operation',
        minorType: 'op4',
        rate: 1
    },
    //5.系统管理
    {
        question: '如何添加系统管理员与用户？',
        answer: ['由系统其他管理员或用户添加输入新用户的信息，并且进行权限设置，最后由系统自动生成密码（建议用户第一时间修改密码）。'],
        imgSrc: [],
        mainType: 'operation',
        minorType: 'op5',
        rate: 1
    },
    {
        question: '如何区别用户与管理员？',
        answer: ['目前用户仅可操作自己当前权限下的相关项目，管理员可操作全部的信托项目。'],
        imgSrc: [],
        mainType: 'operation',
        minorType: 'op5',
        rate: 1
    },
//四、异常问题
    {
        question: '如果贷后系统更新后，出现页面模块消失、数据逻辑变更、页面查询不到数据等问题，应该如何处理？',
        answer: ['运营关注，如出现以上问题及时提醒相关人员关注，并向产品对功能进行咨询，如果是技术原因，找技术立即修改。'],
        imgSrc: [],
        mainType: 'error',
        minorType: '',
        rate: 0
    },
    {
        question: '对手方每月月初数据报送过来，需要注意点什么？',
        answer: ['运营关注，提醒交易对手上传凭证数据，保证数据全部同步到资产方或资金方数据中心。同时，每周二进行对账，及时修正错误数据，每周小结发给资产方或资金方。'],
        imgSrc: [],
        mainType: 'error',
        minorType: '',
        rate: 0
    },
    {
        question: '在人行维护或其他银行维护时，需要注意什么？',
        answer: ['银行维护前3天通知对手方和第三方，2017年人行维护时间表已经记录，后续遇到维护时间会提前3天通知相关人员，并在贷后系统通知栏中提示。每天关注银行和其他渠道的系统维护时间，如有会及时通知相关人员做好调整。'],
        imgSrc: [],
        mainType: 'error',
        minorType: '',
        rate: 0
    },
    {
        question: '广银联代扣配置需要注意什么？',
        answer: ['需要联系广银联，确认各银行的代扣配置是否已完成。'],
        imgSrc: [],
        mainType: 'error',
        minorType: '',
        rate: 0
    },
    {
        question: '放款通道流水和线上代付单不能匹配，应该如何处理？',
        answer: ['查下机构对账，将本端、对端多账的情况告知银行对接人，先核实真实情况，然后修改数据。'],
        imgSrc: [],
        mainType: 'error',
        minorType: '',
        rate: 0
    },
    {
        question: '商户付款凭证的单笔明细金额大于还款计划金额，应该如何处理？',
        answer: ['运营作废商户付款凭证或者商户撤销凭证，要求商户通过接口修改还款计划的明细数据，重传商户付款凭证。'],
        imgSrc: [],
        mainType: 'error',
        minorType: '',
        rate: 0
    },
    {
        question: '商户付款凭证无法上传，应该如何处理？',
        answer: ['查询是否因为新增银行账号，未进行配置导致，可以通过专户账户管理-银行对账，对首次交易的银行账号进行充值操作，配置完成。'],
        imgSrc: [],
        mainType: 'error',
        minorType: '',
        rate: 0
    },
]