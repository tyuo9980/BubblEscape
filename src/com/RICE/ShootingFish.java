package com.RICE;

public class ShootingFish
{
	public float x, y;
	public int gridX, gridY;
	public float velo = 3;
	public float angle;
	public Projectile projectile;
	public boolean drawable;
	public long lastUpdate = 0;

	public ShootingFish(int X, int Y, float deg)
	{
		x = X * Main.blockSize;
		y = Y * Main.blockSize;
		gridX = X;
		gridY = Y;
		angle = (float) Math.toRadians(deg);
		createProjectiles(X, Y);
	}

	public void createProjectiles(int X, int Y)
	{
		projectile = new Projectile(X, Y, angle);
	}

	public void update()
	{
		if ((System.currentTimeMillis() - lastUpdate) > 30)
		{
			projectile.move();
			projectile.count++;

			if (projectile.count > 60)
			{
				projectile.x = x;
				projectile.y = y;
				projectile.angle = angle;
				projectile.velo = velo;
				projectile.dx = Main.forceTransferX(angle, velo);
				projectile.dy = Main.forceTransferY(angle, velo);
				projectile.count = 0;
			}

			lastUpdate = System.currentTimeMillis();
		}
	}
}
