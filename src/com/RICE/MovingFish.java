package com.RICE;

public class MovingFish
{
	public float x, y;
	public float AnchorX, AnchorY;
	public int gridX, gridY;
	public float dx, dy;
	public float velo;
	public float angle;
	public float accel = (float) 0.15;
	public boolean drawable;
	public float maxVelo = 3;
	public static float range = Main.gameX / 2f;
	public static boolean flipped;
	public static long timeN2;
	public static long timeL2 = 0;

	public MovingFish(int X, int Y)
	{
		x = X * Main.blockSize;
		y = Y * Main.blockSize;
		AnchorX = x;
		AnchorY = y;
		gridX = X;
		gridY = Y;
	}

	public void cSwipe(float centerX, float centerY, float ang, float force)
	{
		if (Main.sqdist(centerX, centerY, x - Main.camX + Main.blockSize, y - Main.camY + Main.blockSize) < (Main.blockSize + 10) * (Main.blockSize + 10))
		{
			dx += Main.forceTransferX(ang, (float) (force / 20.0));
			dy += Main.forceTransferY(ang, (float) (force / 20.0));
		}
	}

	public void move()
	{
		if (Main.sqdist(Bubble.x, Bubble.y, AnchorX, AnchorY) < range * range && !Main.collideFish) // homing range
		{
			angle = Main.slopeang(-(Bubble.y + Bubble.radius - (y + Main.blockSize / 2)), Bubble.x + Bubble.radius - (x + Main.blockSize / 2));
			dx += Main.forceTransferX(angle, accel);
			dy += Main.forceTransferY(angle, accel);
			velo = Main.forceTransferVelo(dx, dy);
		} else if (Main.sqdist(x + Main.blockSize, y + Main.blockSize, AnchorX, AnchorY) > 900 || Main.collideFish) // 30
		{
			angle = Main.slopeang(-(AnchorY - (y + Main.blockSize)), AnchorX - (x + Main.blockSize));
			dx += Main.forceTransferX(angle, accel);
			dy += Main.forceTransferY(angle, accel);
			velo = Main.forceTransferVelo(dx, dy);

			timeN2 = System.currentTimeMillis();
			if (timeN2 - timeL2 >= 750)
			{
				timeL2 = System.currentTimeMillis();
				Main.collideFish = false;
			}
		}
		/*
		 * if (velo > maxVelo / 2)
		 * {
		 * velo = maxVelo / 2;
		 * dx = Main.forceTransferX(angle, velo);
		 * dy = Main.forceTransferY(angle, velo);
		 * }
		 */

		if (Math.abs(dx) > 2)
		{
			dx *= 0.5;
		}
		if (Math.abs(dy) > 2)
		{
			dy *= 0.5;
		}

		angle = Main.forceTransferAngle(dx, dy);
		velo = Main.forceTransferVelo(dx, dy);

		// x += dx;
		// y -= dy;

		x += velo * Math.cos(angle);
		y -= velo * Math.sin(angle);
	}
}
