package com.suidifu.rabbitmq.manager.models;

import com.suidifu.rabbitmq.manager.commands.AddBusinessConsumerCommand;
import com.suidifu.rabbitmq.manager.commands.AddFinancialContractGroupCommand;
import com.suidifu.rabbitmq.manager.commands.Command;
import com.suidifu.rabbitmq.manager.commands.InitNewTopologyCommand;
import com.suidifu.watchman.message.amqp.utils.AmqpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wukai on 2017/12/12.
 */
public class CommandModelHelper {

    /**
     * 收集所有的命令模型
     */
    public static List<CommandModel> collectCommandModels() {

        List<CommandModel> commandModels = new ArrayList<>();

        commandModels.add(buildInitCommandModel());

        commandModels.add(buildAddFinancialContractGroupQueueCommandModel());

        commandModels.add(buildAddConsumerCommandModel());

        return commandModels;
    }

    /**
     * 构建初始化Mq模型
     */
    public static CommandModel buildInitCommandModel() {

        Command initServerIdentityCommand = new InitNewTopologyCommand();

        CommandModel initMqCommandModel = new CommandModel("initMqConfig",
                initServerIdentityCommand.classpath(),
                "初始化新框架的mq的配置");

        initMqCommandModel.addParameterModel(
                new ParameterModel(InitNewTopologyCommand.Rabbit_Mq_Connection_Config_Path, "",
                        "Mq链接配置路径", false));
        initMqCommandModel
                .addParameterModel(new ParameterModel(InitNewTopologyCommand.Vhost, "/business", "MQ的Vhost空间"));
        initMqCommandModel.addParameterModel(
                new ParameterModel(InitNewTopologyCommand.Hash_Queue_Start_Index, "1",
                        "MQ的一致HASH队列的起始地3址"));
        initMqCommandModel.addParameterModel(
                new ParameterModel(InitNewTopologyCommand.Hash_Queue_End_Index, "10",
                        "MQ的一致HASH队列的终止地址"));
        initMqCommandModel.addParameterModel(
                new ParameterModel(InitNewTopologyCommand.Financial_Contract_Service_Name, "finacial-contract",
                        "MQ的信托服务名称", false));

        initMqCommandModel.addParameterModel(
                new ParameterModel(InitNewTopologyCommand.Loan_Contract_Service_Name, "loan-contract",
                        "MQ的贷款合同服务名称", false));

        initMqCommandModel.addParameterModel(
                new ParameterModel(InitNewTopologyCommand.Loan_Contract_Routing_Key, ".fin-con.*",
                        "MQ的贷款合同服务路由key", false));

        initMqCommandModel.addParameterModel(
                new ParameterModel(InitNewTopologyCommand.Business_Service_Name, "business",
                        "MQ的业务服务名称", false));

        initMqCommandModel.addParameterModel(
                new ParameterModel(InitNewTopologyCommand.Grant_Privileges, "Y", "给当前mq用户赋予vhost的所有权限"));

        return initMqCommandModel;
    }

    /**
     * 构建增加信托合同队列需求模型
     */
    public static CommandModel buildAddFinancialContractGroupQueueCommandModel() {

        Command addGroupQueueCommand = new AddFinancialContractGroupCommand();

        CommandModel addGroupCommandModel = new CommandModel("addGroupQueue",
                addGroupQueueCommand.classpath(), "增加信托合同配置");

        addGroupCommandModel.addParameterModel(
                new ParameterModel(AddFinancialContractGroupCommand.Rabbit_Mq_Connection_Config_Path, "", "Mq链接配置路径", false));
        addGroupCommandModel
                .addParameterModel(new ParameterModel(AddFinancialContractGroupCommand.Vhost, "/business", "MQ的Vhost空间"));
        addGroupCommandModel.addParameterModel(
                new ParameterModel(AddFinancialContractGroupCommand.Parent_Exchange_Name, AmqpUtils.buildExchangeName("finacial-contract"), "Mq的信托合同uuid", false));
        addGroupCommandModel.addParameterModel(
                new ParameterModel(AddFinancialContractGroupCommand.Service_Name, "Unknow-Financial-Contract-UUID", "Mq的信托合同uuid"));

        addGroupCommandModel.addParameterModel(
                new ParameterModel(InitNewTopologyCommand.Hash_Queue_Start_Index, "1",
                        "MQ的一致HASH队列的起始地3址"));
        addGroupCommandModel.addParameterModel(
                new ParameterModel(InitNewTopologyCommand.Hash_Queue_End_Index, "10",
                        "MQ的一致HASH队列的终止地址"));

        return addGroupCommandModel;
    }

    /**
     * 构建增加业务消费
     */
    public static CommandModel buildAddConsumerCommandModel() {

        Command addGroupQueueCommand = new AddBusinessConsumerCommand();

        CommandModel addGroupCommandModel = new CommandModel("addBusinessConsumerQueue",
                addGroupQueueCommand.classpath(), "增加业务消费配置");

        addGroupCommandModel.addParameterModel(
                new ParameterModel(AddBusinessConsumerCommand.Rabbit_Mq_Connection_Config_Path, "", "Mq链接配置路径", false));
        addGroupCommandModel
                .addParameterModel(new ParameterModel(AddFinancialContractGroupCommand.Vhost, "/business", "MQ的Vhost空间"));

        addGroupCommandModel.addParameterModel(
                new ParameterModel(AddBusinessConsumerCommand.Exchange_Name, AmqpUtils.buildExchangeName("business"), "Mq的业务交换机名", false));

        addGroupCommandModel.addParameterModel(
                new ParameterModel(AddBusinessConsumerCommand.Service_Name, "Unknow-Service-Name", "Mq的业务名"));

        addGroupCommandModel.addParameterModel(
                new ParameterModel(AddBusinessConsumerCommand.Routing_Key, "Unknow-Routing_Key", "Mq的业务的routing key"));

        addGroupCommandModel.addParameterModel(
                new ParameterModel(AddBusinessConsumerCommand.Concurrent_Consumer_Size, "1", "Mq的业务的消费者数目"));

        return addGroupCommandModel;
    }

    /**
     * 执行命令
     */
    public static boolean execCommandModel(CommandModel commandModel) {

        try {

            String classPath = commandModel.getClassPath();

            Class<?> commandClass = Class.forName(classPath);

            Command command = (Command) commandClass.newInstance();

            command.exec(commandModel.extractArguments());

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
