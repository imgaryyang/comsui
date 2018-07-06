package com.suidifu.rabbitmq.manager;

import com.suidifu.rabbitmq.manager.models.CommandModel;
import com.suidifu.rabbitmq.manager.models.CommandModelHelper;
import com.suidifu.rabbitmq.manager.models.ParameterModel;

import java.util.List;
import java.util.Scanner;

/**
 * rabbitMq相关管理功能 Created by wukai on 2017/12/12.
 */
public class RabbitMqManagerEntry {

    public static String RABBIT_CONFIG_PATH = RabbitMqManagerEntry.class.getClassLoader().getResource("config.properties").getPath();

    public static void main(String[] args) {
        if (args.length > 0) {
            if ("--help".equalsIgnoreCase(args[0]) || "-h".equalsIgnoreCase(args[0])) {
                System.out.println("java -jar rabbitmq-manager-1.0.0-SNAPSHOT-spring-boot.jar rabbitmq路径配置");
                System.exit(0);
            } else {
                RABBIT_CONFIG_PATH = args[0];
            }
        } else {
            System.out.println("请按照下面方式使用");
            System.out.println("java -jar rabbitmq-manager-1.0.0-SNAPSHOT-spring-boot.jar rabbitmq路径配置");
            System.exit(0);
        }
        List<CommandModel> commaModels = CommandModelHelper.collectCommandModels();
        Scanner scanner = new Scanner(System.in);
        int index = 0;
        int commandModelSize = commaModels.size();
        while (true) {
            printHelpInfo(commaModels);
            index = printTip(scanner, commandModelSize);
            CommandModel commandModel = commaModels.get(index);
            String name = commandModel.getName();
            interactSingleCommandModel(scanner, commandModel);
            boolean execReuslt = CommandModelHelper.execCommandModel(commandModel);
            System.out.println(String.format("执行命令%s,结果%s", commandModel.getName(), (execReuslt ? "成功" : "失败")));
            System.out.println("还需要继续吗？Y/N");
            if ("N".equalsIgnoreCase(scanner.nextLine())) {
                System.exit(0);
            }
        }
    }

    private static int printTip(Scanner scanner, int commandModelSize) {
        int index;
        System.out.println("请选择命令的编号:");
        index = Integer.parseInt(scanner.next());
        if (index <= -1 || index >= commandModelSize) {
            System.err.println("选择命令编号有误");
            System.exit(-1);
        }
        return index;
    }

    private static void interactSingleCommandModel(Scanner scanner, CommandModel commandModel) {

        if (!commandModel.isNeedInteract()) {
            return;
        }
        System.out.println(String.format("将使用%s[%s]命令", commandModel.getName(), commandModel.getDescription()));
        List<ParameterModel> parameterModels = commandModel.getParameterModels();
        for (ParameterModel parameterModel : parameterModels
                ) {
            if (!parameterModel.isNeedInteract()) {
                continue;
            }
            System.out.println(String.format("参数%s[%s],不填将使用默认值[%s]", parameterModel.getName(), parameterModel.getDescription(), parameterModel.getDefaultValue()));
            parameterModel.setValue(scanner.nextLine());
        }
    }

    private static void printHelpInfo(List<CommandModel> commandModels) {
        int i = 0;
        System.out.println("RabbitMQ配置帮助手册:");
        for (CommandModel commandModel : commandModels
                ) {
            System.out.println(String.format("%s.%s[%s]", i++, commandModel.getName(), commandModel.getDescription()));
        }
    }


}
