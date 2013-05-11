package com.RICE;

public class Coin
{
	public float x, y;
	public float gridX, gridY;
	public boolean drawable;
	public int value;
	public char type;

	public Coin(int X, int Y, char letter)
	{
		x = X * Main.blockSize;
		y = Y * Main.blockSize;
		gridX = X;
		gridY = Y;
		type = letter;
		drawable = true;

		if (type == 'u')
			value = 5;
		if (type == 'v')
			value = 10;
		if (type == 'w')
			value = 20;
		if (type == 'x')
			value = 50;
		if (type == 'y')
			value = 1337;
	}
}
