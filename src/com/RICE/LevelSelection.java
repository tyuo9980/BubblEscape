package com.RICE;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.RICE.BubbleEscapeActivity.SView;

public class LevelSelection
{
	public static int tab = 1;
	public static int selection;
	public static int page;
	public static int numMaps;
	public static int map[] = new int[18];
	public static String score[] = new String[18];
	public static int x, y;
	public static boolean running;
	public static int marginX = 92, marginY = 113;
	public static Paint p = new Paint();

	public static void start(Canvas c, SView view)
	{
		map[0] = 1;
		map[9] = 1;

		// draws level page
		if (tab == 1)
		{
			c.drawBitmap(Main.texture[36], 0, 0, null);
		} else
		{
			c.drawBitmap(Main.texture[37], 0, 0, null);
		}

		// draws level icon
		for (int yy = 0; yy < 3; yy++)
		{
			for (int xx = 0; xx < 3; xx++)
			{
				int i = xx + 3 * yy;

				if (tab == 2)
					i += 9;

				if (map[i] == 1)
					c.drawBitmap(Main.texture[44], (xx + 1) * marginX - 60, (yy + 1) * marginY, null);
				else
					c.drawBitmap(Main.texture[43], (xx + 1) * marginX - 60, (yy + 1) * marginY, null);
			}
		}

		// draws score
		for (int yy = 0; yy < 3; yy++)
		{
			for (int xx = 0; xx < 3; xx++)
			{
				int i = xx + 3 * yy;

				if (tab == 2)
					i += 9;

				c.drawText(score[i], (xx + 1) * marginX - 60, (yy + 1) * marginY + 85, p);
			}
		}

		x = Main.x;
		y = Main.y;

		// selection detection
		if (view.state == 1)
		{
			view.state = 0;

			// tabs
			if (x > 30 && x < 150 && y > 65 && y < 90)
			{
				tab = 1;
			} else if (x > 180 && x < 290 && y > 65 && y < 90)
			{
				tab = 2;
			}

			// levels
			for (int yy = 0; yy < 3; yy++)
			{
				for (int xx = 0; xx < 3; xx++)
				{
					int i = xx + 3 * yy;

					if (tab == 2)
						i += 9;

					if (map[i] == 1)
					{
						if (x > (xx + 1) * marginX - 60 && x < (xx + 1) * marginX + 10 && y > (yy + 1) * marginY && y < (yy + 1) * marginY + 70)
						{
							selection = i;
							loadMap(view);
						}
					}
				}
			}
		}
	}

	public static void loadMap(SView view)
	{
		Main.resetAll();

		if (selection == 0)
			Main.readMap(view, R.raw.lvl1);
		else if (selection == 1)
			Main.readMap(view, R.raw.lvl2);
		else if (selection == 2)
			Main.readMap(view, R.raw.lvl3);
		else if (selection == 3)
			Main.readMap(view, R.raw.lvl4);
		else if (selection == 4)
			Main.readMap(view, R.raw.lvl5);
		else if (selection == 5)
			Main.readMap(view, R.raw.lvl6);
		else if (selection == 6)
			Main.readMap(view, R.raw.lvl7);
		else if (selection == 7)
			Main.readMap(view, R.raw.lvl8);
		else if (selection == 8)
			Main.readMap(view, R.raw.lvl9);
		else if (selection == 9)
			Main.readMap(view, R.raw.lvl10);
		else if (selection == 10)
			Main.readMap(view, R.raw.lvl11);
		else if (selection == 11)
			Main.readMap(view, R.raw.lvl12);
		else if (selection == 12)
			Main.readMap(view, R.raw.lvl13);
		else if (selection == 13)
			Main.readMap(view, R.raw.lvl14);
		else if (selection == 11)
			Main.readMap(view, R.raw.lvl12);
		else if (selection == 14)
			Main.readMap(view, R.raw.lvl15);
		else if (selection == 15)
			Main.readMap(view, R.raw.lvl16);
		else if (selection == 16)
			Main.readMap(view, R.raw.lvl17);
		else if (selection == 17)
			Main.readMap(view, R.raw.lvl18);

		Main.focusCamera(Bubble.x, Bubble.y);

		Swiper.ontouch = false;
		running = false;
	}
}
