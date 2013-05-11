package com.RICE;

public class Bubble
{
	public static float x, y;
	public static int gridX, gridY;
	public static float angle, rAngle;
	public static float lastAngle = 0;
	public static float dx = 0;
	public static float dy = 1;
	public static float velo;
	public static float maxVelo = 6;
	public static float size;
	public static float radius = (float) (0.75 * Main.blockSize);
	public static float diameter = 2 * radius;
	public static float accel = (float) 0.25;
	public static boolean flipped = false;

	public static void cSwipe(float centerX, float centerY, float ang, float force)
	{
		if (Main.sqdist(centerX, centerY, x + radius - Main.camX, y + radius - Main.camY) < (radius + 10) * (radius + 10))
		{
			dx += Main.forceTransferX(ang, (float) (force / 35.0));
			dy += Main.forceTransferY(ang, (float) (force / 35.0));
		}

		rAngle = (float) (angle - Math.toRadians(90));
		if (flipped)
		{
			rAngle += Math.toRadians(180);
		}

	}

	public static void cFan(float ang, float force)
	{
		for (int i = 0; i < Main.numFans; i++)
		{
			if (Main.fan[i].collision(x + radius, y + radius))
			{
				dx += Main.forceTransferX(Main.fan[i].angle, force);
				dy += Main.forceTransferY(Main.fan[i].angle, force);
			}
		}
	}

	public static void move()
	{
		angle = Main.forceTransferAngle(dx, dy);
		velo = Main.forceTransferVelo(dx, dy);

		if (velo > maxVelo)
		{
			velo = maxVelo;
			dx = Main.forceTransferX(angle, velo);
			dy = Main.forceTransferY(angle, velo);
		}

		if (dx > 0)
		{
			dx -= accel;
			if (dx < 0)
				dx = 0;
		} else if (dx < 0)
		{
			dx += accel;
			if (dx > 0)
				dx = 0;
		}

		if (dy > 1)
		{
			dy -= accel;
			if (dy < 1)
				dy = 1;
		} else if (dy < 1)
		{
			dy += accel;
			if (dy > 1)
				dy = 1;
		}

		x += velo * Math.cos(angle);
		y -= velo * Math.sin(angle);
	}

	public static void updateAngle()
	{
		if (Math.abs(rAngle - lastAngle) < 0.1)
		{
			return;
		} else if (rAngle < lastAngle)
		{
			lastAngle -= 0.05;
		} else if (rAngle > lastAngle)
		{
			lastAngle += 0.05;
		}
	}
}
