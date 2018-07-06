package com.suidifu.dowjones.utils;

import com.jcraft.jsch.*;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/4 <br>
 * @time: 13:25 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Data
@Slf4j
@Component
public class FTPUtils implements Serializable{
    private String ip;
    private int port;
    private String name;
    private String password;

    private String baiduIp;
    private int baiduPort;
    private String baiduName;
    private String baiduPassword;
    private String baiduRootPath;

    private String tencentIp;
    private int tencentPort;
    private String tencentName;
    private String tencentPassword;
    private String tencentRootPath;

    /**
     * Sftp客户端对象
     */
    private ChannelSftp sftp = null;

    /**
     * 构造方法
     */
    private FTPUtils() {
    }

    /**
     * 获取实例
     *
     * @return SFTPTool newInstance实例
     */
    public static FTPUtils getInstance() {
        return new FTPUtils();
    }

    /**
     * 进入指定目录
     *
     * @param sftp ChannelSftp
     * @param dir  目录名
     * @return 是否进入目录
     */
    private static boolean cd(ChannelSftp sftp, String dir) {
        try {
            log.info("dir is:{}", dir);
            String[] folders = org.apache.commons.lang.StringUtils.split(dir, Constants.SLASH);
            for (String folder : folders) {
                if (folder.length() > 0) {
                    try {
                        log.info("pwd is {}, cd folder {}..", sftp.pwd(), folder);
                        sftp.cd(folder);
                        log.info("cd end, pwd is {}", sftp.pwd());
                    } catch (SftpException e) {
                        e.printStackTrace();
                        sftp.mkdir(folder);
                        sftp.cd(folder);
                    }
                }
            }
        } catch (SftpException e) {
            log.error("openDir Exception : {}", ExceptionUtils.getStackTrace(e));
            return false;
        }
        return true;
    }

    /**
     * 上传文件: <br>
     *
     * @param file 本地文件
     * @param path 远程路径
     * @return 是否上传成功
     */
    public boolean uploadFile(File file, String path) {
        sftp = connect();
        boolean flag;
        try {
            if (!cd(sftp, path)) {
                sftp.mkdir(path);
            }
            sftp.cd(sftp.getHome() + path);
        } catch (SftpException e) {
            log.error("SftpException is: {}", ExceptionUtils.getStackTrace(e));
            return false;
        }

        try (InputStream in = new FileInputStream(file)) {
            sftp.put(in, file.getName());
            flag = file.exists();
        } catch (Exception e) {
            log.error("uploadFile Exception is: {}", ExceptionUtils.getStackTrace(e));
            return false;
        }

        disconnect();
        return flag;
    }

    private boolean uploadFile(ChannelSftp sftp, File file, String path) {
        boolean flag;
        try {
            if (!cd(sftp, path)) {
                sftp.mkdir(path);
            }
            log.info("cd dir {}", sftp.getHome() + path);
            sftp.cd(sftp.getHome() + path);
        } catch (SftpException e) {
            log.error("SftpException is: {}", ExceptionUtils.getStackTrace(e));
            return false;
        }

        try (InputStream in = new FileInputStream(file)) {
            sftp.put(in, file.getName());
            flag = file.exists();
        } catch (Exception e) {
            log.error("uploadFile Exception is: {}", ExceptionUtils.getStackTrace(e));
            return false;
        }
        return flag;
    }

    /**
     * 列出目录下的文件
     *
     * @param path  要列出的目录
     * @param regex 指定文件名的格式
     * @return 文件列表
     */
    public List<String> listFiles(String path, String regex) {
        sftp = connect();
        List<String> ftpFileNameList = new ArrayList<>();
        Vector<ChannelSftp.LsEntry> sftpFile;
        try {
            log.info("path is:{}", sftp.getHome() + path);
            sftpFile = sftp.ls(sftp.getHome() + path);
        } catch (SftpException e) {
            log.error("SftpException is: {}", ExceptionUtils.getStackTrace(e));
            return ftpFileNameList;
        }
        String fileName;
        for (ChannelSftp.LsEntry aSftpFile : sftpFile) {
            fileName = aSftpFile.getFilename();
            log.info("fileName is:{}", fileName);

            if (StringUtils.equals(fileName, ".") || StringUtils.equals(fileName, "..")) {
                continue;
            }

            if (StringUtils.isNotEmpty(regex) && fileName.contains(regex)) {
                ftpFileNameList.add(fileName);
            }
        }

        disconnect();
        return ftpFileNameList;
    }

    /**
     * 连接sftp服务器
     *
     * @return channelSftp
     */
    private ChannelSftp connect() {
        sftp = new ChannelSftp();
        try {
            JSch jsch = new JSch();

            Session sshSession = jsch.getSession(name, ip, port);
            log.info("Session created");

            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            // 设置超时时间
            sshSession.setTimeout(2 * 60 * 60 * 1000);
            sshSession.connect();

            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            log.error("connect fail,the reason is: {}", ExceptionUtils.getStackTrace(e));
        }

        log.info("connect success!!!!");
        return sftp;
    }

    private ChannelSftp connect(String host, int port, String name, String password) {
        ChannelSftp xxx = new ChannelSftp();
        try {
            JSch jsch = new JSch();

            Session sshSession = jsch.getSession(name, host, port);
            log.info("Session created");

            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            // 设置超时时间
            sshSession.setTimeout(2 * 60 * 60 * 1000);
            sshSession.connect();

            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            xxx = (ChannelSftp) channel;
        } catch (JSchException e) {
            log.error("connect fail,the reason is: {}", ExceptionUtils.getStackTrace(e));
        }

        log.info("connect success!!!!");
        return xxx;
    }

    /**
     * 断开连接
     */
    private void disconnect() {
        if (null != sftp) {
            sftp.disconnect();

            try {
                if (null != sftp.getSession()) {
                    sftp.getSession().disconnect();
                }
            } catch (JSchException e) {
                log.error("disconnect fail, the reason is: {}", ExceptionUtils.getStackTrace(e));
            }
        }

        log.info("disconnect success!!!!");
    }

    private void disconnect(ChannelSftp sftp) {
        if (null != sftp) {
            sftp.disconnect();

            try {
                if (null != sftp.getSession()) {
                    sftp.getSession().disconnect();
                }
            } catch (JSchException e) {
                log.error("disconnect fail, the reason is: {}", ExceptionUtils.getStackTrace(e));
            }
        }

        log.info("disconnect success!!!!");
    }


    public boolean uploadFileToBaidu(File file, Date date) {
        boolean flag = false;
        try {
            ChannelSftp baiduSftp = connect(baiduIp, baiduPort, baiduName, baiduPassword);
            flag = uploadFile(baiduSftp, file, baiduRootPath + DateUtils.getDateFormatYYMM(date) + "/");
            disconnect(baiduSftp);
            log.info("ftp path :{}", baiduRootPath + DateUtils.getDateFormatYYMM(date) + "/");
            if (flag) {
                log.info("upload file[{}] to baidu success.", file.getAbsolutePath());
            } else {
                log.error("upload file[{}] to baidu fail.", file.getAbsolutePath());
            }
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            return flag;
        }
    }

    public boolean uploadFileToTencent(File file, Date date) {
        boolean flag = false;
        try {
            ChannelSftp tencentSftp = connect(tencentIp, tencentPort, tencentName, tencentPassword);
            flag = uploadFile(tencentSftp, file, tencentRootPath);
            disconnect(tencentSftp);
            log.info("ftp path :{}", tencentRootPath);
            if (flag) {
                log.info("upload file[{}] to tencent success.", file.getAbsolutePath());
            } else {
                log.error("upload file[{}] to tencent fail.", file.getAbsolutePath());
            }
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            return flag;
        }
    }
}