package com.suidifu.dowjones.config;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/12/14 <br>
 * @time: 01:18 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
public class AuthorBanner implements Banner {
    private static final String BANNER = "    ___              __                        \n" +
            "   /   \\_____      __\\ \\  ___  _ __   ___  ___ \n" +
            "  / /\\ / _ \\ \\ /\\ / / \\ \\/ _ \\| '_ \\ / _ \\/ __|\n" +
            " / /_// (_) \\ V  V /\\_/ / (_) | | | |  __/\\__ \\\n" +
            "/___,' \\___/ \\_/\\_/\\___/ \\___/|_| |_|\\___||___/";
    private static final String AUTHOR = " :: http://gitlab.5veda.net/wujunshen/dowjones :: ";

    private static final int STRAP_LINE_SIZE = 42;

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass,
                            PrintStream printStream) {
        String version = SpringBootVersion.getVersion();
        printStream.println(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, BANNER));
        printStream.println(AnsiOutput.toString(AnsiColor.BRIGHT_YELLOW, version));

        StringBuilder padding = new StringBuilder();
        while (padding.length() < STRAP_LINE_SIZE
                - (version.length() + AUTHOR.length())) {
            padding.append(" ");
        }

        printStream.println(AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, AUTHOR,
                AnsiColor.DEFAULT, padding.toString(), AnsiStyle.FAINT));
        printStream.println();
    }
}
