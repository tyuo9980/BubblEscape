package com.RICE;

public class Block
{
	public float x, y;
	public int gridX, gridY;
	public char type;
	public boolean drawable;

	public Block(int X, int Y, char letter)
	{
		x = X * Main.blockSize;
		y = Y * Main.blockSize;
		gridX = X;
		gridY = Y;
		type = letter;
	}
}
