package com.suidifu.bridgewater.handler.mielong;

import java.util.HashMap;

import org.junit.Test;

import com.zufangbao.sun.ledgerbook.TAccountInfo;

public class RemittanceSpecialTest extends AbstractNotRollBackBaseTest {

	

	static {
		EntryBook();
	}
	
	private static HashMap<String, TAccountInfo> entryBook;

	
	public static HashMap<String, TAccountInfo> EntryBook() {
		if (entryBook == null) {
			entryBook = new HashMap<String, TAccountInfo>();
			entryBook.put("hehe", new TAccountInfo());
		}
		return entryBook;

	}

	@Test
	public void test() {
		System.out.println("xxxxxxxxxxxxxxxx" + RemittanceSpecialTest.EntryBook());
	}

}
