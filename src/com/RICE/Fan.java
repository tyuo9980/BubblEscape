package com.RICE;

import com.RICE.BubbleEscapeActivity.SView;

public class Fan
{
	public float x, y;
	public int gridX, gridY;
	public float angle;
	public float velo = 3;
	public float accel;
	public float force = 0.5f;
	public static float height = 40;
	public static float width = 120;
	public boolean drawable;
	public float newX, newY, fX, fY;
	public int xComp;

	// fan collision
	public float colX;
	public float colY;

	public Fan(int X, int Y, float deg)
	{
		x = X * Main.blockSize;
		y = Y * Main.blockSize;
		gridX = X;
		gridY = Y;
		angle = (float) Math.toRadians(deg);
	}

	public void push(int num)
	{
		// collision with bubble
		Bubble.cFan(angle, force);

		// collision with projectiles
		for (int i = 0; i < Main.numSFish; i++)
		{
			if (Main.sFish[i].projectile.drawable)
			{
				Main.sFish[i].projectile.cFan(angle, force, num);
			}
		}
	}

	public boolean collision(float oX, float oY)
	{
		newX = oX - colX;
		newY = oY - colY;

		float cAng = (float) Math.cos(angle);
		float sAng = (float) Math.sin(angle);

		float fX = newX * cAng - newY * sAng;
		float fY = newY * cAng + newX * sAng;

		fX += colX;
		fY += colY;

		return fX >= colX && fX <= colX + width && fY >= colY - height / 2 && fY <= colY + height / 2;
	}

	public void blocked(SView view)
	{
		if (view.state == 2)
		{
			int X = view.x;
			int Y = view.y;

			float rad = (float) Math.toRadians(angle);
			float newX = X - x;
			float newY = Y - y;
			float fX = (float) (newX * Math.cos(rad) - newY * Math.sin(rad));
			float fY = (float) (newY * Math.cos(rad) + newX * Math.sin(rad));
			fX += x;
			fY += y;

			if (fX >= x && fX <= x + width && fY >= y && fY <= y + height)
			{
				width = (float) (fX - x);
			} else
			{
				width = 100;
			}
		} else
		{
			width = 100;
		}
	}
}
