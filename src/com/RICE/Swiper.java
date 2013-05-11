package com.RICE;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;

import com.RICE.BubbleEscapeActivity.SView;

public class Swiper
{
	public static int cW = Main.screenW, cH = Main.screenH;

	public static int mX, mY;
	public static int mCount, state;

	public static boolean released = true, ontouch;

	public static long now, mainTimer;

	public static int deadzone = 15;
	public static int wavelength = 6;
	public static float initForce = 30;
	public static float waveVelo = 3.8f;

	public static int pX[] = new int[wavelength];
	public static int pY[] = new int[wavelength];
	public static List<Integer> list = new LinkedList<Integer>();

	public static float lX[] = new float[wavelength];
	public static float lY[] = new float[wavelength];
	public static float lA[] = new float[wavelength]; // left angle

	public static float rX[] = new float[wavelength];
	public static float rY[] = new float[wavelength];
	public static float rA[] = new float[wavelength]; // right angle

	public static float force[] = new float[wavelength];

	public static RadialGradient rG;
	public static Paint p = new Paint();

	public static void start(Canvas c, SView view) throws Exception
	{
		mX = Main.x;
		mY = Main.y;
		state = view.state;

		if (ontouch) // fingerDown
		{
			if (released)
			{
				pX[mCount] = -50;
				pY[mCount] = -50;
			}

			if (Main.sqdist(pX[mCount], pY[mCount], mX, mY) > deadzone * deadzone)
			{
				if (released)
				{
					mCount = -1;
					list.clear();
					released = false;
				}

				mCount++;
				mCount = mCount % wavelength;
				force[mCount] = initForce;
				pX[mCount] = mX;
				pY[mCount] = mY;
				lX[mCount] = mX;
				lY[mCount] = mY;
				rX[mCount] = mX;
				rY[mCount] = mY;

				list.add(mCount);

				if (list.size() > wavelength)
					list.remove(0);
			}

			ontouch = false;
		} else
		{
			mCount = 0;
			released = true;
		}

		// draw
		for (int i = list.size() - 1; i >= 0 && list.size() > 1; i--)
		{
			int m = 0, o = 0;
			int n = list.get(i);

			if (i != 0)
				m = list.get(i - 1);
			else if (i == 0 && list.size() != 1)
				o = list.get(i + 1);

			if (force[n] > 0)
			{
				force[n] -= 0.8;

				if (i == 0)
				{
					lA[n] = lA[o];
					rA[n] = rA[o];
				} else
				{
					lA[n] = (float) (Main.slopeang(-(pY[n] - pY[m]), pX[n] - pX[m]) + Main.pi / 2);
					rA[n] = (float) (lA[n] - Main.pi);
				}

				lX[n] += waveVelo * Math.cos(lA[n]);
				lY[n] -= waveVelo * Math.sin(lA[n]);

				rX[n] += waveVelo * Math.cos(rA[n]);
				rY[n] -= waveVelo * Math.sin(rA[n]);

				if (i != 0)
				{
					p.setAlpha((int) (force[n] * 2.5));
					rG = new RadialGradient((int) (pX[m] + pX[n]) / 2, (int) pY[m], 30, Color.BLUE, Color.CYAN, RadialGradient.TileMode.MIRROR);
					p.setShader(rG);

					c.drawLine((int) lX[m], (int) lY[m], (int) lX[n], (int) lY[n], p);
					c.drawLine((int) rX[m], (int) rY[m], (int) rX[n], (int) rY[n], p);

					SwipeCollision(((int) lX[n] + (int) lX[m]) / 2, ((int) lY[n] + (int) lY[m]) / 2, lA[n], force[m]);
					SwipeCollision(((int) rX[n] + (int) rX[m]) / 2, ((int) rY[n] + (int) rY[m]) / 2, rA[n], force[m]);
				}
			}
		}
	}

	public static void SwipeCollision(float centerX, float centerY, float angle, float force)
	{
		// Swipe with bubble
		Bubble.cSwipe(centerX, centerY, angle, force);

		// Swipe with projectiles
		for (int i = 0; i < Main.numSFish; i++)
		{
			if (Main.sFish[i].projectile.drawable)
			{
				Main.sFish[i].projectile.cSwipe(centerX, centerY, angle, force);
			}
		}

		// Swipe with mFish
		for (int i = 0; i < Main.numMFish; i++)
		{
			if (Main.mFish[i].drawable)
			{
				Main.mFish[i].cSwipe(centerX, centerY, angle, force);
			}
		}
	}
}
