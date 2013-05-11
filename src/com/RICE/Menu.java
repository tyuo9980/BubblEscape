package com.RICE;

import android.graphics.Canvas;

import com.RICE.BubbleEscapeActivity.SView;

public class Menu
{
	public static boolean running;
	public static int pX1, pX2, pY1, pY2;
	public static int mX1, mX2, mY1, mY2;
	public static int x, y;

	public static void start(Canvas c, SView view)
	{
		pX1 = 65;
		pX2 = 255;
		pY1 = 200;
		pY2 = 265;

		mX1 = 270;
		mX2 = 300;
		mY1 = 435;
		mY2 = 465;

		c.drawBitmap(Main.texture[35], 0, 0, null);

		if (Main.mute)
		{
			c.drawBitmap(Main.texture[39], 270, 435, null);
		} else
		{
			c.drawBitmap(Main.texture[38], 270, 435, null);
		}

		x = Main.x;
		y = Main.y;

		if (view.state == 1)
		{
			view.state = 0;

			// PLAY BUTTON
			if (x >= pX1 && x <= pX2 && y >= pY1 && y <= pY2)
			{
				running = false;
				LevelSelection.running = true;
			}

			// MUTE BUTTON
			if (x >= mX1 && x <= mX2 && y >= mY1 && y <= mY2)
			{
				if (Main.mute)
				{
					Main.mute = false;
					try
					{
						BubbleEscapeActivity.mp.start();
					} catch (Exception e)
					{
					}
				} else
				{
					Main.mute = true;
					BubbleEscapeActivity.mp.pause();
				}
			}
		}
	}
}
