package com.zufangbao.earth.kaptcha;

import com.google.code.kaptcha.GimpyEngine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 重写kaptcha的验证码样式引擎
 * @author zsh
 *
 */
public class VerificationCodeGimpyEngine  implements GimpyEngine{
	private static final int NOISE_NUM = 500;
	@Override
	public BufferedImage getDistortedImage(BufferedImage arg0) {
		Graphics g  = arg0.getGraphics();
		g.setColor(VirificationCodeUtil.getRandColor(160, 200));
		generateNoise(arg0, g); 
		return arg0;
	}
	private void generateNoise(BufferedImage arg0, Graphics g) {
		Random random = new Random();
		for (int i = 0; i < NOISE_NUM; i++) {
			int x = random.nextInt(arg0.getWidth());
			int y = random.nextInt(arg0.getHeight());
			int xl = random.nextInt(2);
			int yl = random.nextInt(2);
			g.drawLine(x, y, x + xl, y + yl);
		}
	}
}
