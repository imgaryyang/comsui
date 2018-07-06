package com.zufangbao.earth.kaptcha;

import com.google.code.kaptcha.text.WordRenderer;
import com.google.code.kaptcha.util.Configurable;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 重写kaptcha的文字渲染器
 * @author zsh
 *
 */
public class VerificationCodeWordRenderer  extends Configurable implements WordRenderer{

    @Override
    public BufferedImage renderWord(String word, int width, int height){

    	int fontSize = getConfig().getTextProducerFontSize();
        Font[] fonts = generateFonts(fontSize);
        int charSpace = getConfig().getTextProducerCharSpace();
        BufferedImage image = new BufferedImage(width, height, 2);
        Graphics2D g2D = gerenate2D(image);
        int startPosY = (height - fontSize) / 5 + fontSize;
        char wordChars[] = word.toCharArray();
        Font chosenFonts[] = new Font[wordChars.length];
        int charWidths[] = new int[wordChars.length];
        int startPosX = calStartPosX(g2D,width, fonts, charSpace,wordChars, chosenFonts, charWidths);

        drawChars(charSpace, g2D, startPosY, wordChars, chosenFonts, charWidths, startPosX);

        return image;
    }

	private void drawChars(int charSpace, Graphics2D g2D, int startPosY, char[] wordChars, Font[] chosenFonts,
			int[] charWidths, int startPosX) {
		for(int i = 0; i < wordChars.length; i++)
        {
        	g2D.setColor(VirificationCodeUtil.getRandColor(0,200));
            g2D.setFont(chosenFonts[i]);
            char charToDraw[] = {
                wordChars[i]
            };
            g2D.drawChars(charToDraw, 0, charToDraw.length, startPosX, startPosY);
            startPosX = startPosX + charWidths[i] + charSpace;
        }
	}

	private Font[] generateFonts(int fontSize) {
		String paramName = "kaptcha.textproducer.font.names";
        String paramValue = (String)getConfig().getProperties().get(paramName);
        String fontNames[] = paramValue.split(",");
        Font fonts[] = new Font[fontNames.length];
        for(int i = 0; i < fontNames.length; i++){
            fonts[i] = new Font(fontNames[i], Font.BOLD, fontSize);
        }
		return fonts;
	}
    private Graphics2D gerenate2D(BufferedImage image){
         Graphics2D g2D = image.createGraphics();
         RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
         g2D.setRenderingHints(hints);
         return g2D;
    }
	private int calStartPosX(Graphics2D g2D,int width, Font[] fonts, int charSpace,
			char[] wordChars, Font[] chosenFonts, int[] charWidths) {
		int widthNeeded = 0;
	    FontRenderContext frc = g2D.getFontRenderContext();
		Random random = new Random();
        for(int i = 0; i < wordChars.length; i++)
        {
            chosenFonts[i] = fonts[random.nextInt(fonts.length)];
            char charToDraw[] = {wordChars[i]};
            GlyphVector gv = chosenFonts[i].createGlyphVector(frc, charToDraw);
            charWidths[i] = (int)gv.getVisualBounds().getWidth();
            if(i > 0)
                widthNeeded += charSpace;
            widthNeeded += charWidths[i];
        }
		return (width - widthNeeded) / 2;
	}
}