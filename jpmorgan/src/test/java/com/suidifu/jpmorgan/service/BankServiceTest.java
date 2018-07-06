package com.suidifu.jpmorgan.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.suidifu.jpmorgan.entity.Bank;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml", })
@TransactionConfiguration(defaultRollback=false)
@Transactional()
public class BankServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	BankService bankService;

	@Test
	public void insertToBankTable() {

		File file = new File("src/test/resources/test/支行联行号.txt");
        BufferedReader reader = null;
        try {
        	System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            
            int line = 1;

            while ((tempString = reader.readLine()) != null) {
            	// 显示行号
                System.out.println("line " + line + ": " + tempString);
                
                String[] split = tempString.split("	");
                
                Bank bank = new Bank(UUID.randomUUID().toString(), split[1], split[2], split[3], null, null);
                
                bankService.save(bank);
            }
        	
        	
        	
		} catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
	}
}
