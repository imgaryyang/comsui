package com.suidifu.dowjones.utils;

public class Constants {
    public static final String NULL_DATA = "";
    public static final String CONTRACT = "contract";
    public static final String LEDGER_BOOK_SHELF = "ledger_book_shelf";
    public static final String REPURCHASE_DOC = "repurchase_doc";
    public static final String UTF_8 = "UTF-8";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String FILE_DATE_PATTERN = "yyyyMMdd";
    public static final String CSV = "csv";
    public static final String FTP_PATH = "/datastream/";
    public static final String RETURN = "\n";
    public static final String COMMA = ",";
    public static final String SLASH = "/";
    public static final byte[] BOM = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    //公式id：0动态池逾期率 1月度静态池逾期率
    public static final int FORMULA_DYNAMIC = 0;
    public static final int FORMULA_STATIC = 1;

    private Constants() {
    }

}