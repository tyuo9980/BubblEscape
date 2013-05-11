package com.RICE;

public class Projectile
{
	public float x, y;
	public float dx, dy;
	public float velo = 3;
	public float angle;
	public boolean drawable;
	public int count;

	public Projectile(float X, float Y, float ang)
	{
		x = X * Main.blockSize;
		y = Y * Main.blockSize;
		angle = ang;
		count = 0;
		drawable = true;
		dx = Main.forceTransferX(angle, velo);
		dy = Main.forceTransferY(angle, velo);
	}

	public void cSwipe(float centerX, float centerY, float ang, float force)
	{
		if (Main.sqdist(centerX, centerY, x - Main.camX, y - Main.camY) < 100)
		{
			dx += Main.forceTransferX(ang, (float) (force / 40.0));
			dy += Main.forceTransferY(ang, (float) (force / 40.0));
		}
	}

	public void cFan(float ang, float force, int num)
	{
		if (Main.fan[num].collision(x, y))
		{
			dx += Main.forceTransferX(ang, force);
			dy += Main.forceTransferY(ang, force);
		}
	}

	public void move()
	{
		angle = Main.forceTransferAngle(dx, dy);
		velo = Main.forceTransferVelo(dx, dy);

		if (velo > 5)
		{
			velo = 5;
			dx = Main.forceTransferX(angle, velo);
			dy = Main.forceTransferY(angle, velo);
		}

		x += velo * Math.cos(angle);
		y -= velo * Math.sin(angle);

	}
}
