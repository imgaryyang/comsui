package com.suidifu.morganstanley.tasks;

import static com.zufangbao.sun.utils.FilenameUtils.SUFFIX_CSV;
import static org.apache.commons.io.FilenameUtils.EXTENSION_SEPARATOR;

import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskService;
import com.zufangbao.sun.entity.BQ.BQOverdueRepay;
import com.zufangbao.sun.entity.BQ.BQRepay;
import com.zufangbao.sun.entity.BQ.BQRepayAmount;
import com.zufangbao.sun.entity.BQ.BQRepayDate;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by hwr on 17-11-28.
 */
@Component("bqFileCreateTask")
@Slf4j
public class BQFileCreateTask {
    @Autowired
    @Qualifier("delayProcessingTaskDBService")
    private DelayProcessingTaskService delayProcessingTaskDBService;

    @Autowired
    private FinancialContractService financialContractService;

    @Autowired
    private FinancialContractConfigurationService financialContractConfigurationService;

    @Value("${BQ.filePath}")
    private String filePath;

    private static final String STR_FORMAT = "000";

    private  Map<String, Integer> contentMap = new HashMap<String, Integer>();

    private List<String> filePathName = new ArrayList<String>();

    private List<String> fileName = new ArrayList<String>();

    private List<String> fileParentName = new ArrayList<String>();

    private List<com.suidifu.matryoshka.delayTask.DelayProcessingTaskLog> logs;

    //每10000记录条数一个文件
    private static final int CNT = 10000;

    //每500打一个包
    private static final int COUNT = 500;

    //佰仟二期变更还款时间
    private static final  String BQ_MODIFYREPAYMENT_TIME= "374da0c4-3935-11e7-952e-ba77244e1da4";
    //佰仟二期变更还款金额
    private static final  String BQ_MODIFYREPAYMENT_AMOUNT= "402fd71a-393b-11e7-bf99-00163e002839";
    //变迁二期还款文件
    private static final  String BQ_MODIFYREPAYMENT_REPAY= "18be8bf0-d4d2-11e7-a83d-502b73c136df";
    //佰仟二期逾期文件
    private static final  String BQ_MODIFYREPAYMENT_OVERDEE= "4d77e688-d4d2-11e7-a83d-502b73c136df";
    //佰仟二期浮动费用文件(变更还款金额)
    private static final  String BQ_MUTABLEFEE_AMOUNT= "8aec6aa6-46ac-11e7-881a-b208103af10b";

    private void init(){
        contentMap = new HashMap<>();
        filePathName = new ArrayList<String>();
        fileName = new ArrayList<>();
        fileParentName = new ArrayList<>();

        contentMap.put(BQ_MODIFYREPAYMENT_TIME,0);
        contentMap.put(BQ_MODIFYREPAYMENT_AMOUNT,1);
        contentMap.put(BQ_MODIFYREPAYMENT_REPAY,2);
        contentMap.put(BQ_MODIFYREPAYMENT_OVERDEE,3);
        contentMap.put(BQ_MUTABLEFEE_AMOUNT,1);
//        contentMap.put("91b9ec3e-d4d8-11e7-a83d-502b73c136df",4);
//        contentMap.put("097ea183-d4e3-11e7-a83d-502b73c136df",5);
        filePathName.add("/repayPlan/repayDate/"+DateUtils.today());
        filePathName.add("/repayPlan/repayAmount/"+DateUtils.today());
        filePathName.add("/repayPlan/repay/"+DateUtils.today());
        filePathName.add("/repayPlan/overdue_repay/"+DateUtils.today());
//        filePathName.add("/repay_plan_detail/repay_plan_detail_");
//        filePathName.add("/loan_detail/loan_detail_");

        fileName.add("/repayDate_");
        fileName.add("/repayAmount_");
        fileName.add("/repay_");
        fileName.add("/overdue_repay_");

        fileParentName.add("/repayDate_");
        fileParentName.add("/repayAmount_repay_");
        fileParentName.add("/repay_");
        fileParentName.add("/repay_");

        logs = new ArrayList<>();
    }

    public static String addOne(int intHao){
        DecimalFormat df = new DecimalFormat(STR_FORMAT);
        String result = df.format(intHao);
        intHao++;
        return result;
    }

    public void BQCreateFileV2(){
        init();
        //(现在佰仟二期已经暂时废弃,改用三期,而三期配置沿用二期的,主要是涉及产品代码和后置任务配置)
        List<String> financialContractUuids = financialContractConfigurationService.getCodeConfiguredFinancialContractUuids(
            FinancialContractConfigurationCode.REPAYMENT_ORDER_CHECK_DELAY_TASK.getCode());
        if(CollectionUtils.isEmpty(financialContractUuids)){
            return;
        }
        //只支持取一个
        FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuids.get(0));
        if(financialContract == null){
            log.error("financialContract is null,please check productCode");
            return ;
        }
        List<DelayProcessingTask> BQDelayProcessingTasks = delayProcessingTaskDBService.getDelayProcessingTaskByFinancialContract(financialContract.getFinancialContractUuid());
        if(BQDelayProcessingTasks == null){
            log.error("no data in table[DelayProcessingTask]");
            return ;
        }
        List<DelayProcessingTask> BQTimeDelayProcessingTasks = new ArrayList<>();
        List<DelayProcessingTask> BQAmountDelayProcessingTasks = new ArrayList<>();
        List<DelayProcessingTask> BQRepayDelayProcessingTasks = new ArrayList<>();
        List<DelayProcessingTask> BQOverDueDelayProcessingTasks = new ArrayList<>();
        List<DelayProcessingTask> BQMutableFeeDelayProcessingTasks = new ArrayList<>();
        for(DelayProcessingTask task: BQDelayProcessingTasks){
            if(BQ_MODIFYREPAYMENT_TIME.equals(task.getConfigUuid())){
                BQTimeDelayProcessingTasks.add(task);
            }
            if(BQ_MODIFYREPAYMENT_AMOUNT.equals(task.getConfigUuid())){
                BQAmountDelayProcessingTasks.add(task);
            }
            if(BQ_MODIFYREPAYMENT_REPAY.equals(task.getConfigUuid())){
                BQRepayDelayProcessingTasks.add(task);
            }
            if(BQ_MODIFYREPAYMENT_OVERDEE.equals(task.getConfigUuid())){
                BQOverDueDelayProcessingTasks.add(task);
            }
            if(BQ_MUTABLEFEE_AMOUNT.equals(task.getConfigUuid())){
                BQMutableFeeDelayProcessingTasks.add(task);
            }
        }
        log.info("BQ repayment  time size is {}", BQTimeDelayProcessingTasks.size()+"");
        BQCreate(BQ_MODIFYREPAYMENT_TIME,BQTimeDelayProcessingTasks);
        log.info("BQ repayment  amount size is {}", BQAmountDelayProcessingTasks.size()+"");
        BQCreate(BQ_MODIFYREPAYMENT_AMOUNT,BQAmountDelayProcessingTasks);
        log.info("BQ repayment  repay size is {}", BQRepayDelayProcessingTasks.size()+"");
        BQCreate(BQ_MODIFYREPAYMENT_REPAY,BQRepayDelayProcessingTasks);
        log.info("BQ repayment  overdue size is {}", BQOverDueDelayProcessingTasks.size()+"");
        BQCreate(BQ_MODIFYREPAYMENT_OVERDEE,BQOverDueDelayProcessingTasks);
        log.info("BQ repayment  mutablefee size is {}", BQMutableFeeDelayProcessingTasks.size()+"");
        BQCreate(BQ_MUTABLEFEE_AMOUNT,BQMutableFeeDelayProcessingTasks);
    }

    public void BQCreate(String uuid, List<DelayProcessingTask> BQDelayProcessingTasks){
        Integer cnt = contentMap.get(uuid);
        if(cnt == null){
            log.error("无法取到该config_uuid["+uuid+"]对于的映射值");
            return;
        }
        File file = new File(filePath+filePathName.get(cnt));
        if(!file.exists()){
            file.mkdirs();
        }
        buildFileV2(uuid,  filePathName.get(cnt) + fileName.get(cnt) + DateUtils.format(new Date(), "yyyyMMddHHmmss") + "_", BQDelayProcessingTasks);
        if(file.listFiles() == null){
            log.info("该文件下不存在子文件 "+file.getPath()+"");
            return ;
        }
        log.info("start to compressing Directory");
        for(File listFile : file.listFiles()){
            if(!listFile.isDirectory()){
                continue;
            }
            String s = listFile.getPath();
            log.info("start to compressing Directory : "+s+"");
            try {
                CompressedFiles_Gzip(s, s+".tar", s+".tar.gz");
            } catch (IOException e) {
                e.printStackTrace();
                log.error("CompressedFiles_Gzip id error");
            }
        }
        //删除原有文件
        log.info("start to deleting old file Directory");
        for(File listFile : file.listFiles()){
            if(!listFile.isDirectory()){
                continue;
            }
            try {
                log.info("start to delete Directory : "+listFile.getPath()+"");
                FileUtils.deleteDirectory(listFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void buildFileV2(String configUuid, String path, List<DelayProcessingTask> tasks){
        long startTime = System.currentTimeMillis();
        try {
            StringBuffer fileContent = new StringBuffer("");
            int count = 0;
            int seq = 0;
            File result = null;
            int j = 0;
            for(int i = 0;i < tasks.size();){
                for(j = 0;j < COUNT; ){
                    result = createFile(path, j++, configUuid);
                    for(int z = 0;z < CNT; z ++){
                        DelayProcessingTask task = tasks.get(i++);
                        String jsonObject = task.getWorkParams();
                        createContent(jsonObject, configUuid, fileContent, result);
                        modifyStatus(task);
                        if(i >= tasks.size()){
                            break;
                        }
                    }
                    if(i >= tasks.size()){
                        break;
                    }
                }
                seq =  makirParent(configUuid, j, seq, result);
            }
            //如果该次扫描没有文件生成，则生成一份空文件
//            if(j == 0){
//                result = createFile(path, fileContent, j++, configUuid);
//            }
            long endTime = System.currentTimeMillis();
            log.info("take it "+(endTime - startTime)+" ms.");
        } catch (IOException e) {
            log.error("流错误");
            e.printStackTrace();
        }

    }

    private void modifyStatus(DelayProcessingTask task) {
        try {
            task.setExecuteStatus(1);
            delayProcessingTaskDBService.updateByTask(task);
            buildTaskLog(task);
            log.info("task update success ,uuid : "+task.getUuid()+"");
        } catch (Exception e) {
            log.error("task update error uuid : "+task.getUuid()+"");
            e.printStackTrace();
        }
    }

    private void buildTaskLog(DelayProcessingTask task) {
        com.suidifu.matryoshka.delayTask.DelayProcessingTaskLog taskLog = new com.suidifu.matryoshka.delayTask.DelayProcessingTaskLog();
        taskLog.setConfigUuid(task.getConfigUuid());
        taskLog.setContractUuid(task.getContractUuid());
        taskLog.setCustomerUuid(task.getCustomerUuid());
        taskLog.setExecuteStatus(task.getExecuteStatus());
        taskLog.setFinancialContractUuid(task.getFinancialContractUuid());
        taskLog.setRepaymentPlanUuid(task.getRepaymentPlanUuid());
        taskLog.setTaskExecuteDate(task.getTaskExecuteDate());
        taskLog.setUuid(task.getUuid());
        taskLog.setWorkParams(task.getWorkParams());
        logs.add(taskLog);
    }

    private int makirParent(String configUuid, int count, int seq, File result) {
        String parent = "";
        File parentFile = null;
        File[] files = result.getParentFile().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith(".csv")){
                    return true;
                }
                return false;
            }
        });
        if(files == null){
            files = new File[0];
        }
        if(files.length>=COUNT){
            parent = result.getParent()+fileParentName.get(contentMap.get(configUuid))
                    + DateUtils.format(new Date(), "yyyyMMddHHmmss")+"_"+addOne(seq++);
            parentFile = new File(parent);
            if(!parentFile.exists()){
                if(!parentFile.mkdirs()){
                    return seq;
                }
            }
            File pFile = result.getParentFile();
            if(pFile == null){
                log.error("file is not exists");
                return seq;
            }
            int fileCount = 0;
            for(File listFile : pFile.listFiles()){
                if(listFile.isDirectory()||listFile.getName().lastIndexOf(".tar.gz") != -1){
                    continue;
                }
                try {
                    FileUtils.moveFile(listFile, new File(parent +"/" + listFile.getName()));
                    fileCount ++;
                } catch (IOException e) {
                    log.error("移动文件失败");
                    e.printStackTrace();
                }
                if(fileCount == COUNT){
                    break;
                }
            }
        }
        return seq;
    }

    private void createContent(String jsonObject, String configUuid, StringBuffer fileContent, File file) {
        try {
            switch (contentMap.get(configUuid)){
                case 0:
                    BQRepayDate bqRepayDate = com.demo2do.core.utils.JsonUtils.parse(jsonObject, BQRepayDate.class);
                    if(bqRepayDate == null){
                        log.info("bqRepayDate  id null");
                        break;
                    }
                    FileUtils.writeStringToFile(file,
                            bqRepayDate.getContent() + "\n","utf-8",true);
                    break;
                case 1:
                    BQRepayAmount bqRepayAmount = com.demo2do.core.utils.JsonUtils.parse(jsonObject, BQRepayAmount.class);
                    if(bqRepayAmount == null){
                        log.info("bqRepayAmount  id null");
                        break;
                    }
                    FileUtils.writeStringToFile(file,
                            bqRepayAmount.getContent() + "\n","utf-8",true);
                    break;
                case 2:
                    BQRepay bqRepay = com.demo2do.core.utils.JsonUtils.parse(jsonObject, BQRepay.class);
                    if(bqRepay == null){
                        log.info("bqRepay  id null");
                        break;
                    }
                    FileUtils.writeStringToFile(file,
                            bqRepay.getContent() + "\n","utf-8",true);
                    break;
                case 3:
                    BQOverdueRepay bqOverdueRepay = com.demo2do.core.utils.JsonUtils.parse(jsonObject, BQOverdueRepay.class);
                    if(bqOverdueRepay == null){
                        log.info("bqOverdueRepay  id null");
                        break;
                    }
                    FileUtils.writeStringToFile(file,
                            bqOverdueRepay.getContent() + "\n","utf-8",true);
                    break;
//                case 4:
//                    fileContent.append(""+jsonObject.getString("借款申请号")+","+jsonObject.getString("当前期数")+","+jsonObject.getString("应还本金")+","
//                            +jsonObject.getString("应还利息")+","+jsonObject.getString("应还费用")+","+jsonObject.getString("已还本金")+","
//                            +jsonObject.getString("已还利息")+","
//                            +jsonObject.getString("已还费用")+","+jsonObject.getString("约定还款日")+","+jsonObject.getString("实际还款日")+
//                            ","+jsonObject.getString("对账目标日")+"\n");
//                    break;
//                case 5:
//                    fileContent.append(""+jsonObject.getString("借款申请号")+","+jsonObject.getString("贷款本金")+","+jsonObject.getString("放款日")+","
//                            +jsonObject.getString("总分期数")+"\n");
//                    break;
                default:
                    log.info("no such selection");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createFile(String path, int count, String configUuid) throws IOException {
        File file = new File(filePath+path+addOne(count)+EXTENSION_SEPARATOR+SUFFIX_CSV);
        createFileHeader(file, configUuid);
        return file;
    }

    private void createFileHeader(File file, String configUuid) {
        log.info("start to writing header,configUuid:"+configUuid+"");
        try {
            switch (contentMap.get(configUuid)) {
                case 0:
                    FileUtils.writeStringToFile(file,
                            BQRepayDate.getHeader() + "\n", "utf-8", true);
                    break;
                case 1:
                    FileUtils.writeStringToFile(file,
                            BQRepayAmount.getHeader() + "\n", "utf-8", true);
                    break;
                case 2:
                    FileUtils.writeStringToFile(file,
                            BQRepay.getHeader() + "\n", "utf-8", true);
                    break;
                case 3:
                    FileUtils.writeStringToFile(file,
                            BQOverdueRepay.getHeader() + "\n", "utf-8", true);
                    break;
//            case 4:
//                FileUtils.writeStringToFile(file,
//                        "借款申请号,当前期数,应还本金,应还利息,应还费用,已还本金,已还利息,已还费用,约定还款日,实际还款日,对账目标日" + "\n","utf-8",true);
//                break;
//            case 5:
//                FileUtils.writeStringToFile(file,
//                        "借款申请号,贷款本金,放款日,总分期数,principal,interest,repayCharge,changeType,tradeTime" + "\n","utf-8",true);
//                break;
                default:
                    log.info("no such selection");
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void CompressedFiles_Gzip(String folderPath, String targzipFilePath, String targzipFileName) throws IOException {
        File srcPath =new File(folderPath);
        int length = 0;
        if(srcPath.listFiles() != null){
            length=srcPath.listFiles().length;
        }
        byte[] buf = new byte[1024]; //设定读入缓冲区尺寸
        File[] files   =   srcPath.listFiles();
        log.info("start to CompressedFiles_Gzip");
        FileOutputStream fout = null;
        TarArchiveOutputStream tout = null;
        FileInputStream fin = null;
        GZIPOutputStream gzout = null;
        try {
            //建立压缩文件输出流
            fout=new FileOutputStream(targzipFilePath);
            //建立tar压缩输出流
            tout=new TarArchiveOutputStream(fout);
            for(int i=0;i<length;i++)
            {
                try {
                    String filename = srcPath.getPath() + File.separator + files[i].getName();
                    //打开需压缩文件作为文件输入流
                    fin = new FileInputStream(filename);   //filename是文件全路径
                    TarArchiveEntry tarEn = new TarArchiveEntry(files[i]); //此处必须使用new TarEntry(File file);
                    tarEn.setName(files[i].getName());  //此处需重置名称，默认是带全路径的，否则打包后会带全路径
                    tout.putArchiveEntry(tarEn);
                    int num;
                    while ((num = fin.read(buf, 0, 1024)) != -1) {
                        tout.write(buf, 0, num);
                    }
                    tout.closeArchiveEntry();
                } finally {
                    fin.close();
                }
            }

            //建立压缩文件输出流
            try(FileOutputStream gzFile=new FileOutputStream(targzipFilePath+".gz")){
                //建立gzip压缩输出流
                gzout = new GZIPOutputStream(gzFile);
                //打开需压缩文件作为文件输入流
                try (FileInputStream tarin = new FileInputStream(targzipFilePath)) {  //targzipFilePath是文件全路径
                    int len;
                    while ((len = tarin.read(buf, 0, 1024)) != -1) {
                        gzout.write(buf, 0, len);
                    }
                }
            }

            log.info("end CompressedFiles_Gzip");

            File f = new File(targzipFilePath);
            log.info(f.getPath());
            f.deleteOnExit();
        } catch(FileNotFoundException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch(IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        } finally{
            if(tout !=null) {
                tout.close();
            }
            if(fout != null) {
                fout.close();
            }
            if(gzout != null) {
                gzout.close();
            }
        }
    }

}
