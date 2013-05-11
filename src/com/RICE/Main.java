package com.RICE;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.RICE.BubbleEscapeActivity.SView;

//uses floats for math instead of double
@SuppressLint("FloatMath")
public class Main
{
	// map
	public static int map;
	public static char grid[][]; 											// saves map objects
	public static int degree[][];											// saves object degrees
	public static int gridX, gridY;											// map grid size

	// gfx
	public static Paint p = new Paint();									// paint object
	public static Paint scP = new Paint();
	public static Paint airP = new Paint();
	public static Bitmap texture[] = new Bitmap[45];						// stores all textures
	public static Bitmap buffer;											// image buffer
	public static Bitmap scaledBuffer;
	public static float imageScale, imageScaleV;
	public static RadialGradient rG;

	// audio
	public static boolean mute;

	// objects
	public static Block block[];											// wall objects
	public static MovingFish mFish[];										// moving fish objects
	public static ShootingFish sFish[];										// shooting fish objects
	public static Fan fan[];												// fan objects
	public static Coin coin[];												// coin objects

	// game
	public static int x, y;
	public static int gameX, gameY;
	public static boolean storyModeOn = true;
	public static int currentLevel;
	public static int netCoins, netDistance;
	public static short air;												// life - air gauge
	public static int score;												// level score
	public static int money;												// money earned
	public static long timeN, airTimerN;
	public static long timeL = 0, airTimerL = 0;
	public static boolean collideFish = false;
	public static boolean paused, end, win;									// state variables

	// camera
	public static int camX, camY;											// camera coordinates
	public static int camW, camH;											// camera size
	public static int screenW, screenH;										// screen size
	public static boolean trueDZ, semiDZ, outsideDZ;						// deadzone states
	public static int trueDZwidth, trueDZheight;							// trueDZ size
	public static int semiDZwidth, semiDZheight;							// semiDZ size
	public static int focusX, focusY;										// camera focus point

	// misc
	public static int numBlocks, numSFish, numMFish, numFans, numCoins;		// object count
	public static byte blockSize;											// size of tile
	public static int mapHeight;											// map height in pixels
	public static float f;													// bg scrolling ratio
	public static float angle;				                				// rotation angle
	public static final float pi = (float) Math.PI;

	// image rotation
	public static Matrix matrix = new Matrix();

	// bg
	public static Rect src;													// source rectangle
	public static Rect dst;													// destination rectangle // destination rectangle

	// coin animation
	public static int aniNum = 10;
	public static int aniX = 0;
	public static int aniY = 0;
	public static Paint aniP = new Paint();
	public static Bitmap ani;

	// hp animation
	public static int hpNum = 10;
	public static int hpX = 0;
	public static int hpY = 0;
	public static Paint hpP = new Paint();
	public static Bitmap hp;

	public static void init(SView view)
	{
		// gets screen info
		screenW = view.getWidth();
		screenH = view.getHeight();

		gameX = 320;
		imageScale = screenW / (float) gameX;
		gameY = (int) (screenH / imageScale);
		imageScaleV = screenH / 480;

		camW = gameX;
		camH = gameY;

		// sets deadzone sizes
		trueDZwidth = (int) (screenW / 5); 		// 20%
		trueDZheight = (int) (screenH / 2); 	// 50%

		semiDZwidth = (int) (screenW / 2.5); 	// 40%
		semiDZheight = (int) (screenH / 1.5); 	// 67%

		// sets camera focus coordinates
		focusX = screenW / 2;
		focusY = (screenH - semiDZheight + semiDZheight) / 2;

		Menu.running = true;
		blockSize = 20;
		src = new Rect(0, 0, gameX, gameY);
		dst = new Rect(src);
		Swiper.p.setStrokeWidth(6);
		Swiper.p.setColor(8576400);

		// damage paint object
		hpP.setAlpha(250);
		hpP.setColor(Color.RED);
		hpP.setTextSize(12);

		// score paint object
		Typeface tf = Typeface.createFromAsset(BubbleEscapeActivity.am, "idolwild.ttf");
		scP.setTypeface(tf);
		scP.setTextSize(26);
		scP.setColor(Color.WHITE);
		scP.setAntiAlias(true);

		// air paint object
		airP.setAlpha(150);
		airP.setStrokeWidth(20);

		// loads all textures
		texture[0] = BitmapFactory.decodeResource(view.getResources(), R.drawable.block);
		texture[1] = BitmapFactory.decodeResource(view.getResources(), R.drawable.corner1);
		texture[2] = BitmapFactory.decodeResource(view.getResources(), R.drawable.corner2);
		texture[3] = BitmapFactory.decodeResource(view.getResources(), R.drawable.corner3);
		texture[4] = BitmapFactory.decodeResource(view.getResources(), R.drawable.corner4);
		texture[5] = BitmapFactory.decodeResource(view.getResources(), R.drawable.end1);
		texture[6] = BitmapFactory.decodeResource(view.getResources(), R.drawable.end2);
		texture[7] = BitmapFactory.decodeResource(view.getResources(), R.drawable.end3);
		texture[8] = BitmapFactory.decodeResource(view.getResources(), R.drawable.end4);
		texture[9] = BitmapFactory.decodeResource(view.getResources(), R.drawable.side1);
		texture[10] = BitmapFactory.decodeResource(view.getResources(), R.drawable.side2);
		texture[11] = BitmapFactory.decodeResource(view.getResources(), R.drawable.side3);
		texture[12] = BitmapFactory.decodeResource(view.getResources(), R.drawable.side4);
		texture[13] = BitmapFactory.decodeResource(view.getResources(), R.drawable.thin2);
		texture[14] = BitmapFactory.decodeResource(view.getResources(), R.drawable.thin1);
		texture[15] = BitmapFactory.decodeResource(view.getResources(), R.drawable.sfish);
		texture[16] = BitmapFactory.decodeResource(view.getResources(), R.drawable.bullet);
		texture[17] = BitmapFactory.decodeResource(view.getResources(), R.drawable.mfish);
		texture[18] = BitmapFactory.decodeResource(view.getResources(), R.drawable.fan);
		texture[20] = BitmapFactory.decodeResource(view.getResources(), R.drawable.bg);
		texture[21] = BitmapFactory.decodeResource(view.getResources(), R.drawable.duck);
		texture[22] = BitmapFactory.decodeResource(view.getResources(), R.drawable.pause);
		texture[23] = BitmapFactory.decodeResource(view.getResources(), R.drawable.hud);
		// texture[24] = BitmapFactory.decodeResource(view.getResources(), R.drawable.air);
		texture[25] = BitmapFactory.decodeResource(view.getResources(), R.drawable.filler);
		texture[26] = BitmapFactory.decodeResource(view.getResources(), R.drawable.five);
		texture[27] = BitmapFactory.decodeResource(view.getResources(), R.drawable.ten);
		texture[28] = BitmapFactory.decodeResource(view.getResources(), R.drawable.chain);
		texture[29] = BitmapFactory.decodeResource(view.getResources(), R.drawable.coin1);
		texture[30] = BitmapFactory.decodeResource(view.getResources(), R.drawable.coin2);
		texture[31] = BitmapFactory.decodeResource(view.getResources(), R.drawable.coin3);
		texture[32] = BitmapFactory.decodeResource(view.getResources(), R.drawable.coin4);
		texture[33] = BitmapFactory.decodeResource(view.getResources(), R.drawable.coin5);
		texture[34] = BitmapFactory.decodeResource(view.getResources(), R.drawable.anchor);
		texture[35] = BitmapFactory.decodeResource(view.getResources(), R.drawable.menupage);
		texture[36] = BitmapFactory.decodeResource(view.getResources(), R.drawable.levelselectionstory);
		texture[37] = BitmapFactory.decodeResource(view.getResources(), R.drawable.levelselectionchallenge);
		texture[38] = BitmapFactory.decodeResource(view.getResources(), R.drawable.musicon);
		texture[39] = BitmapFactory.decodeResource(view.getResources(), R.drawable.musicoff);
		texture[40] = BitmapFactory.decodeResource(view.getResources(), R.drawable.victory);
		texture[41] = BitmapFactory.decodeResource(view.getResources(), R.drawable.death);
		// texture[42] = BitmapFactory.decodeResource(view.getResources(), R.drawable.credits);
		texture[43] = BitmapFactory.decodeResource(view.getResources(), R.drawable.locked);
		texture[44] = BitmapFactory.decodeResource(view.getResources(), R.drawable.unlocked);
	}

	public static void getScaledInput(SView view)
	{
		x = (int) (view.x / imageScale);
		y = (int) (view.y / imageScale);
	}

	// creates buffer bitmap and sets canvas
	public static void getImage(Canvas c)
	{
		buffer = Bitmap.createBitmap(gameX, gameY, Bitmap.Config.RGB_565);
		c = new Canvas(buffer);
	}

	// scales image to fit screen
	public static void scale(Canvas c, float x, float y)
	{
		if (screenW != 320)
			c.scale(x, y);
	}

	// collision detection
	public static void collision()
	{
		// Player-Block collision
		for (int i = 0; i < numBlocks; i++)
		{
			if (block[i].drawable)
			{
				int c = 0;

				// circle collision with block
				while ((sqdist(Bubble.x + Bubble.radius, Bubble.y + Bubble.radius, block[i].x + blockSize / 2, block[i].y + blockSize / 2)) <= (1.25 * blockSize) * (1.25 * blockSize))
				{
					c++;
					if (c > 3)
						break;

					float a = slopeang(-(Bubble.y + Bubble.radius - (block[i].y + blockSize / 2)), Bubble.x + Bubble.radius - (block[i].x + blockSize / 2));

					// hits vertical sides
					if (a > pi / 4 && a < 3 * pi / 4 || a > 5 * pi / 4 && a < 7 * pi / 4)
					{
						Bubble.angle = -Bubble.angle;
					}
					// hits horizontal sides
					else if ((a < pi / 4 || a > 7 * pi / 4) || a > 3 * pi / 4 && a < 5 * pi / 4)
					{
						Bubble.angle = -Bubble.angle + pi;
					}
					// hits corners
					else
					{
						Bubble.angle += pi;
					}

					// bounce
					Bubble.dx = Main.forceTransferX(Bubble.angle, Bubble.velo);
					Bubble.dy = -Main.forceTransferY(Bubble.angle, Bubble.velo);

					Bubble.move();
				}
			}
		}

		// Player-mFish collision
		for (int i = 0; i < numMFish; i++)
		{
			if (mFish[i].drawable)
			{
				if (sqdist(Bubble.x + Bubble.radius, Bubble.y + Bubble.radius, mFish[i].x + blockSize, mFish[i].y + blockSize) < (Bubble.radius + blockSize) * (Bubble.radius + blockSize))
				{
					Bubble.dx += Main.forceTransferX(mFish[i].angle, 10.5f);
					Bubble.dy += Main.forceTransferY(mFish[i].angle, 10.5f);
					Bubble.move();

					mFish[i].dx *= -10;
					mFish[i].dy *= -10;
					mFish[i].move();

					collideFish = true;

					timeN = System.currentTimeMillis();
					if (timeN - timeL >= 500)
					{
						timeL = System.currentTimeMillis();
						air -= 5;

						hpNum = 0;
						hp = texture[26];
						hpX = (int) (Bubble.x + Bubble.radius);
						hpY = (int) Bubble.y;
						hpP.setAlpha(250);
					}
				}
			}
		}

		// Player-Projectile collision
		for (int i = 0; i < numSFish; i++)
		{
			if (sFish[i].projectile.drawable)
			{
				if (sqdist(Bubble.x + Bubble.radius, Bubble.y + Bubble.radius, sFish[i].projectile.x + 5, sFish[i].projectile.y + 5) < (Bubble.radius) * (Bubble.radius))
				{
					sFish[i].projectile.drawable = false;

					timeN = System.currentTimeMillis();
					if (timeN - timeL >= 500)
					{
						timeL = System.currentTimeMillis();
						air -= 10;

						hpNum = 0;
						hp = texture[27];
						hpX = (int) (Bubble.x + Bubble.radius);
						hpY = (int) Bubble.y;
						hpP.setAlpha(250);
					}
				}
			}
		}

		// Player-Coin collision
		for (int i = 0; i < numCoins; i++)
		{
			if (coin[i].drawable)
			{
				for (int a = 0; a < 2; a++)
				{
					for (int b = 0; b < 2; b++)
					{
						if (Bubble.gridX + a == coin[i].gridX && Bubble.gridY + b == coin[i].gridY)
						{
							coin[i].drawable = false;
							coin[i].x = -50;
							coin[i].y = -50;
							score += coin[i].value * 10; 	// score multiplier = 10
							money += coin[i].value; 		// collects money

							aniNum = 0;
							ani = texture[(int) coin[i].type - 88];
							aniX = (int) (Bubble.x + Bubble.radius);
							aniY = (int) Bubble.y;
							aniP.setAlpha(250);
						}
					}
				}
			}
		}
	}

	// when player gets a coin
	public static void coinAnimation(Canvas c)
	{
		if (aniNum < 10)
		{
			c.drawBitmap(ani, aniX - camX, aniY - camY, aniP);
			aniY -= 2;
			aniP.setAlpha(aniP.getAlpha() - 25);
			aniNum++;
		}
	}

	// when player gets hit
	public static void healthAnimation(Canvas c)
	{
		if (hpNum < 10)
		{
			c.drawBitmap(hp, hpX - camX, hpY - camY, hpP);
			hpY -= 2;
			hpP.setAlpha(hpP.getAlpha() - 25);
			hpNum++;
		}
	}

	// air timer
	public static void airTimer()
	{
		airTimerN = System.currentTimeMillis();
		if (airTimerN - airTimerL > 3800)
		{
			airTimerL = System.currentTimeMillis();
			air--;
		}
	}

	// camera - follows player
	public static void camera()
	{
		// TRUE DEADZONE
		if (Bubble.x < camX + trueDZwidth)					// left
			camX = (int) Bubble.x - trueDZwidth;
		if (Bubble.x > camX + camW - trueDZwidth)			// right
			camX = (int) Bubble.x + trueDZwidth - camW;
		if (Bubble.y < camY + trueDZheight)					// up
			camY = (int) Bubble.y - trueDZheight;
		if (Bubble.y > camY + camH - trueDZwidth)			// down
			camY = (int) Bubble.y + trueDZwidth - camH;

		// SEMI DEADZONE
		if (Bubble.x < camX + semiDZwidth)					// left
			camX--;
		if (Bubble.x > camX + camW - semiDZwidth)			// right
			camX++;
		if (Bubble.y < camY + semiDZheight)					// up
			camY--;
		if (Bubble.y > camY + camH - semiDZwidth)			// down
			camY++;

		if (camY < 0)										// limits top
			camY = 0;
		else if (camY + camH > mapHeight)					// limits bottom
			camY = mapHeight - camH;
	}

	// scans level and assigns variable values
	public static int readMap(SView view, int id)
	{
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(view.getResources().openRawResource(id)), 16384);

			char letter;
			String temp;
			String lineOne[] = in.readLine().split(" ");

			gridX = Integer.parseInt(lineOne[0]);
			gridY = Integer.parseInt(lineOne[1]);
			numBlocks = Integer.parseInt(lineOne[2]);
			numFans = Integer.parseInt(lineOne[3]);
			numSFish = Integer.parseInt(lineOne[4]);
			numMFish = Integer.parseInt(lineOne[5]);
			numCoins = Integer.parseInt(lineOne[6]);

			// bg scrolling and camera limiter
			mapHeight = gridY * blockSize;
			f = (float) (gridY * blockSize - gameY) / (float) (128 * blockSize - gameY);

			// creates objects
			block = new Block[numBlocks];
			fan = new Fan[numFans];
			sFish = new ShootingFish[numSFish];
			mFish = new MovingFish[numMFish];
			coin = new Coin[numCoins];

			grid = new char[gridX][gridY];
			degree = new int[gridX][gridY];

			for (int i = 0; i < gridY; i++)
			{
				temp = in.readLine();

				for (int j = 0; j < temp.length(); j++)
				{
					letter = temp.charAt(j);
					grid[j][i] = letter;
				}
			}

			while ((temp = in.readLine()) != null)
			{
				String info[] = temp.split(" ");
				int degx = Integer.parseInt(info[0]);
				int degy = Integer.parseInt(info[1]);
				degree[degx][degy] = Integer.parseInt(info[2]);
			}

			setObjects(gridX, gridY); 			// initializes objects
			in.close(); 						// closes reader

			return 1; 							// Success

		} catch (Exception qe)
		{
			System.out.println("readmap: " + qe.toString() + " " + qe.getLocalizedMessage());
			return 0; 							// Failed
		}
	}

	// initializes objects. sets coordinates and angles
	public static void setObjects(int maxX, int maxY)
	{
		int countB = 0;
		int countF = 0;
		int countMF = 0;
		int countSF = 0;
		int countC = 0;

		for (int i = 0; i < maxY; i++)
		{
			for (int j = 0; j < maxX; j++)
			{
				char letter = grid[j][i];

				if (letter == 'b')												// fans
					fan[countF++] = new Fan(j, i, degree[j][i]);
				else if (letter == 'c') 										// shooting fish
					sFish[countSF++] = new ShootingFish(j, i, degree[j][i]);
				else if ((letter >= 'd' && letter <= 'r') || letter == '{') 	// block tiles
					block[countB++] = new Block(j, i, letter);
				else if (letter == 's')											// moving fish
					mFish[countMF++] = new MovingFish(j, i);
				else if (letter == 't')											// player
				{
					Bubble.x = j * blockSize;
					Bubble.y = i * blockSize;
				} else if (letter >= 'u' && letter <= 'z')						// coins
					coin[countC++] = new Coin(j, i, letter);
			}
		}
	}

	// moves player
	public static void moveBubble()
	{
		BubbleToGrid();
		Bubble.move();
	}

	// moves fish
	public static void moveFish()
	{
		for (int i = 0; i < numMFish; i++)
		{
			if (mFish[i].drawable)
				mFish[i].move();
		}
	}

	// moves projectiles
	public static void moveProjectiles()
	{
		for (int f = 0; f < numSFish; f++)
		{
			if (sFish[f].projectile.drawable)
				sFish[f].projectile.move();
		}
	}

	// draws player/duck
	public static void drawBubble(Canvas c)
	{
		Bubble.updateAngle();

		matrix.reset();
		matrix.postRotate((float) Math.toDegrees(Bubble.lastAngle), Bubble.radius, Bubble.radius);
		matrix.postTranslate((float) (Bubble.x - camX), (float) (Bubble.y - camY));

		c.drawBitmap(texture[21], matrix, null);
	}

	// draws fish
	public static void drawFish(Canvas c)
	{
		// shooting fish
		for (int i = 0; i < numSFish; i++)
		{
			// image rotation
			if (onScreen(sFish[i].x, sFish[i].y))
			{
				angle = (float) Math.toDegrees(sFish[i].angle);
				matrix.reset();

				if (angle > 90 && angle <= 270)
				{
					matrix.setRotate(angle - 180, blockSize, blockSize / 2);
					matrix.postScale(-1, 1, blockSize, blockSize / 2);
				} else
					matrix.setRotate(-angle, blockSize, blockSize / 2);

				matrix.postTranslate(sFish[i].x - camX, sFish[i].y - camY);

				c.drawBitmap(texture[15], matrix, null);

				sFish[i].drawable = true;

				// sFish functions
				sFish[i].update();
			} else
				sFish[i].drawable = false;
		}

		// moving fish
		for (int i = 0; i < numMFish; i++)
		{
			// chain
			float d = (mFish[i].y + blockSize - mFish[i].AnchorY) / (mFish[i].x - mFish[i].AnchorX + blockSize);
			float ang = (float) Math.atan(d);

			if (mFish[i].drawable && mFish[i].x + blockSize <= mFish[i].AnchorX)
			{
				ang += pi;
			}

			matrix.reset();
			matrix.setRotate((float) Math.toDegrees(ang), 0, 4);

			int temp = blockSize;
			float dx = temp * (float) Math.cos(ang);
			float dy = temp * (float) Math.sin(ang);

			double distance = dist(mFish[i].AnchorX, mFish[i].AnchorY, mFish[i].x + blockSize, mFish[i].y + blockSize);
			matrix.postTranslate(mFish[i].AnchorX - camX - dx, mFish[i].AnchorY - camY - dy);

			for (int n = 0; n < distance - 15; n += temp)
			{
				matrix.postTranslate(dx, dy);
				c.drawBitmap(texture[28], matrix, null);
			}

			// anchor
			c.drawBitmap(texture[34], mFish[i].AnchorX - camX - blockSize / 2, mFish[i].AnchorY - camY - blockSize / 2, null);

			// image rotation
			if (onScreen(mFish[i].x, mFish[i].y))
			{
				angle = (float) Math.toDegrees(mFish[i].angle);
				matrix.reset();

				if (angle < -90 && angle >= -270)
				{
					angle += 180;
					matrix.setRotate(angle, blockSize, blockSize);
					matrix.postScale(-1, 1, blockSize, blockSize);
				} else
				{
					angle = -angle;
					matrix.setRotate(angle, blockSize, blockSize);
				}
				matrix.postTranslate(mFish[i].x - camX, mFish[i].y - camY);

				mFish[i].drawable = true;

				c.drawBitmap(texture[17], matrix, null);
			} else
				mFish[i].drawable = false;
		}
	}

	// draws block tiles
	public static void drawBlocks(Canvas c)
	{
		for (int i = 0; i < numBlocks; i++)
		{
			if (onScreen(block[i].x, block[i].y))
			{
				block[i].drawable = true;

				if (block[i].type == '{')
					c.drawBitmap(texture[25], block[i].x - camX, block[i].y - camY, null);
				else
					c.drawBitmap(texture[block[i].type - 'd'], block[i].x - camX, block[i].y - camY, null);
			} else
				block[i].drawable = false;
		}
	}

	// draws in-game GUI
	public static void drawHUD(Canvas c)
	{
		c.drawBitmap(texture[23], 0, 0, null);
		// c.drawBitmap(texture[24], 0, 0, null);

		rG = new RadialGradient(35, 35, 90, Color.YELLOW, Color.CYAN, RadialGradient.TileMode.MIRROR);
		airP.setShader(rG);
		c.drawLine(170, 31, 190 + (int) (air * 0.7), 31, airP);

		c.drawText(Integer.toString(air), 180, 37, scP);
		c.drawText(Integer.toString(score), 60, 37, scP);
	}

	// draws projectiles
	public static void drawProjectiles(Canvas c)
	{
		for (int i = 0; i < numSFish; i++)
		{
			if (sFish[i].projectile.drawable)
			{
				// projectile transparency
				int a = p.getAlpha();
				p.setAlpha(240 - (4 * sFish[i].projectile.count));
				p.setAlpha(a);

				angle = (float) Math.toDegrees(sFish[i].projectile.angle);
				matrix.reset();

				if (angle < 0)
					matrix.setRotate(360 - angle, texture[16].getWidth() / 2, texture[16].getHeight() / 2);
				else
					matrix.setRotate(360 - angle, texture[16].getWidth() / 2, texture[16].getHeight() / 2);

				matrix.postTranslate(sFish[i].projectile.x - camX, sFish[i].projectile.y - camY);

				c.drawBitmap(texture[16], matrix, p);
			}
		}
	}

	// draws fans
	public static void drawFan(Canvas c, SView v)
	{
		for (int i = 0; i < numFans; i++)
		{
			// image rotation
			if (onScreen(fan[i].x, fan[i].y))
			{
				fan[i].drawable = true;

				angle = (float) Math.toDegrees(fan[i].angle);
				matrix.reset();
				if (angle > 90 && angle <= 270)
				{
					fan[i].colX = fan[i].x - blockSize / 4;
					fan[i].colY = fan[i].y + blockSize / 2;
					matrix.setRotate(angle - 180, blockSize / 4, blockSize / 2);
					matrix.postScale(-1, 1, blockSize / 4, blockSize / 2);

					fan[i].colX += Main.rotateX(0, 0, blockSize / 4, (blockSize / 2), (float) Math.toRadians(angle - 180));
					fan[i].colY += Main.rotateY(0, 0, blockSize / 4, (blockSize / 2), (float) Math.toRadians(angle - 180));
				} else
				{
					fan[i].colX = fan[i].x + blockSize;
					fan[i].colY = fan[i].y + blockSize / 2;
					matrix.setRotate(-angle, blockSize / 4, blockSize / 2);
					fan[i].colX += Main.rotateX(0, 0, (blockSize / 4), (blockSize / 2), (float) Math.toRadians(-angle));
					fan[i].colY += Main.rotateY(0, 0, (blockSize / 4), (blockSize / 2), (float) Math.toRadians(-angle));
				}
				matrix.postTranslate(fan[i].x - camX, fan[i].y - camY);

				c.drawBitmap(texture[18], matrix, null); // draws fan

				fan[i].push(i);
			} else
				fan[i].drawable = false;
		}
	}

	// draws misc. objects
	public static void drawMisc(Canvas c)
	{
		// coins
		for (int i = 0; i < numCoins; i++)
		{
			if (coin[i].drawable)
			{
				if (coin[i].type != 'z')
					c.drawBitmap(texture[(int) coin[i].type - 88], coin[i].x - camX, coin[i].y - camY, null);
				else
					c.drawBitmap(texture[33], coin[i].x - camX, coin[i].y - camY, null);
			}
		}
	}

	// draws background image
	public static void drawBackground(Canvas c)
	{
		src.set(0, (int) (camY / f), gameX, gameY + (int) (camY / f)); // camera scrolling
		c.drawBitmap(texture[20], src, dst, null);
	}

	// checks for level completion
	public static void stateCheck(SView view)
	{
		if (Bubble.y < 100)
		{
			if (LevelSelection.selection != 17)
			{
				BubbleEscapeActivity.savePref("UNLOCK" + Integer.toString(LevelSelection.selection + 1), Integer.toString(1));
				LevelSelection.map[LevelSelection.selection + 1] = 1;
			}

			BubbleEscapeActivity.savePref("SCORE" + Integer.toString(LevelSelection.selection), Integer.toString(score));
			LevelSelection.score[LevelSelection.selection] = Integer.toString(score);

			win = true;
			end = true;
		} else if (air <= 0)
		{
			air = 0;
			end = true;
		} else if (view.state == 1 && x > 275 && x < 310 && y > 15 && y < 50)
		{
			paused = true;
		}
	}

	// win/lose menu
	public static void endScreen(Canvas c, SView view)
	{
		int x = Main.x;
		int y = Main.y;

		c.drawRect(0, 480, 320, 600, p);

		if (win)
		{
			c.drawBitmap(texture[40], 0, 0, null);

			if (view.state == 1)
			{
				view.state = 0;

				if (x > 70 && x < 250 && y > 125 && y < 150)
				{
					LevelSelection.selection++;
					LevelSelection.loadMap(view);
				} else if (x > 70 && x < 250 && y > 197 && y < 223)
				{
					LevelSelection.loadMap(view);
				} else if (x > 70 && x < 250 && y > 265 && y < 290)
				{
					end = false;
					Menu.running = true;
					x = 0;
					y = 0;
				}
			}
		} else
		{
			c.drawBitmap(texture[41], 0, 0, null);

			if (view.state == 1)
			{
				view.state = 0;

				if (x > 70 && x < 250 && y > 220 && y < 245)
				{
					LevelSelection.loadMap(view);
				} else if (x > 70 && x < 250 && y > 285 && y < 310)
				{
					end = false;
					Menu.running = true;
					x = 0;
					y = 0;
				}
			}
		}
	}

	// in-game pause function
	public static void Pause(Canvas c, SView view)
	{
		int x = Main.x;
		int y = Main.y;

		c.drawRect(0, 480, 320, 600, p);
		c.drawBitmap(texture[22], 0, 0, null);

		if (view.state == 1)
		{
			view.state = 0;

			if (x > 70 && x < 250 && y > 105 && y < 130)
			{
				paused = false;
				x = 0;
				y = 0;
			} else if (x > 70 && x < 250 && y > 175 && y < 200)
			{
				LevelSelection.loadMap(view);
			} else if (x > 70 && x < 250 && y > 245 && y < 272)
			{
				end = false;
				Menu.running = true;
				x = 0;
				y = 0;
			}
		}
	}

	// calculates x velocity
	public static float forceTransferX(float ang, float force)
	{
		return (float) (force * Math.cos(ang));
	}

	// calculates y velocity
	public static float forceTransferY(float ang, float force)
	{
		return (float) (force * Math.sin(ang));
	}

	// calculates angle from x and y velocities
	public static float forceTransferAngle(float dx, float dy)
	{
		return slopeang(dy, dx);
	}

	// calculates velocity from x and y velocities
	public static float forceTransferVelo(float dx, float dy)
	{
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	// translates bubble x,y to gridX,Y
	public static void BubbleToGrid()
	{
		Bubble.gridX = (int) (Bubble.x / blockSize + 0.5);
		Bubble.gridY = (int) (Bubble.y / blockSize + 0.5);
	}

	// focuses camera on bubble
	public static void focusCamera(float x, float y)
	{
		camX = (int) Bubble.x - focusX;
		camY = (int) Bubble.y - focusY;
	}

	// resets game variables
	public static void resetAll()
	{
		numBlocks = 0;
		numMFish = 0;
		numSFish = 0;
		numFans = 0;

		air = 100;
		score = 0;

		paused = false;
		end = false;
		win = false;

		Bubble.x = (int) (screenW / 2);
		Bubble.y = screenH - 50;
		Bubble.lastAngle = 0;
		Bubble.dx = 0;
		Bubble.dy = 1;
		Bubble.angle = Main.slopeang(Bubble.dy, Bubble.dx);
		Bubble.angle -= pi / 2;
	}

	// on screen function - checks if object is on screen
	public static boolean onScreen(float x, float y)
	{
		return x >= camX - blockSize && x < camX + camW + blockSize && y >= camY - blockSize && y < camY + camH + blockSize;
	}

	// arc-tangeant function
	public static float slopeang(float y, float x)
	{
		if (y == 0 && x > 0 || x == 0 && y == 0)
			return 0;
		else if (y == 0 && x < 0)
			return pi;
		else if (x == 0 && y > 0)
			return pi / 2;
		else if (x == 0 && y < 0)
			return 1.5f * pi;
		else if (x < 0)
			return (float) (Math.atan(y / x) - pi) % 360;
		else
			return (float) Math.atan(y / x) % 360;
	}

	// distance function
	public static float dist(float x1, float y1, float x2, float y2)
	{
		return (float) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

	// squared distance function
	public static float sqdist(float x1, float y1, float x2, float y2)
	{
		return (y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1);
	}

	public static float rotateX(float x, float y, float centerx, float centery, float rad)
	{
		float tempx = x - centerx;
		float tempy = y - centery;
		x = (float) (tempx * Math.cos(rad) - tempy * Math.sin(rad));
		x += centerx;
		return x;
	}

	public static float rotateY(float x, float y, float centerx, float centery, float rad)
	{
		float tempx = x - centerx;
		float tempy = y - centery;
		y = (float) (tempy * Math.cos(rad) + tempx * Math.sin(rad));
		y += centery;
		return y;
	}
}
