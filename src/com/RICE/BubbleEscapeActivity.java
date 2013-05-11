package com.RICE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class BubbleEscapeActivity extends Activity
{
	// save file
	public static SharedPreferences.Editor prefEdit;
	public static SharedPreferences pref;

	// assets
	public static AssetManager am;

	// game state variables
	public static boolean pause;
	public static MediaPlayer mp;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(new SView(this));
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// assets
		am = getAssets();

		// sharedprefs
		pref = getPreferences(MODE_PRIVATE);
		prefEdit = pref.edit();
		initPref();
		readPref();

		// music
		mp = MediaPlayer.create(BubbleEscapeActivity.this, R.raw.bgmusic);
		mp.setLooping(true);
	}

	public void initPref()
	{
		for (int i = 0; i < 18; i++)
		{
			String s = pref.getString("UNLOCK" + Integer.toString(i), "error");
			String ss = pref.getString("SCORE" + Integer.toString(i), "error");

			if (s.equals("error"))
			{
				prefEdit.putString("UNLOCK" + Integer.toString(i), "0");
				prefEdit.commit();
			}
			if (ss.equals("error"))
			{
				prefEdit.putString("SCORE" + Integer.toString(i), "0");
				prefEdit.commit();
			}
		}
	}

	public static void readPref()
	{
		for (int i = 0; i < 18; i++)
		{
			LevelSelection.map[i] = Integer.parseInt(pref.getString("UNLOCK" + Integer.toString(i), ""));
			LevelSelection.score[i] = pref.getString("SCORE" + Integer.toString(i), "");
		}
	}

	public static void savePref(String name, String s)
	{
		prefEdit.putString(name, s);
		prefEdit.commit();
	}

	public void onDestroy()
	{
		mp.stop();
		finish();
		super.onDestroy();
	}

	public void onWindowFocusChanged(boolean hasFocus)
	{
		if (!hasFocus)
		{
			Main.paused = true;
			mp.pause();
		} else
		{
			try
			{
				mp.start();
			} catch (Exception e)
			{
			}
		}

		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		// ignore orientation/keyboard change
		super.onConfigurationChanged(newConfig);
	}

	public class GameThread extends Thread
	{
		SView view;
		Canvas c = null; 			// Get Canvas
		long lastTime = System.currentTimeMillis();
		long timeElapsed = 0;
		long timeNow;

		int fps = 30;
		float delay = 1000 / fps;

		boolean toMenu = false;

		public GameThread(SView view)
		{
			this.view = view;
		}

		public void run() 			// Game Loop
		{
			Main.init(view);
			Main.getImage(c);

			if (!Main.mute)
				mp.start();

			while (true)
			{
				try
				{
					Thread.sleep(1);
				} catch (Exception e)
				{
				}

				timeNow = System.currentTimeMillis();

				if (timeNow - lastTime >= delay)
				{
					lastTime = System.currentTimeMillis();

					try
					{
						c = view.getHolder().lockCanvas();      // Get Canvas
					} finally
					{
						if (c != null)
						{
							if (Menu.running)                                 // MENU
							{
								Main.scale(c, Main.imageScale, Main.imageScale);
								Main.getScaledInput(view);
								Menu.start(c, view);
							} else if (LevelSelection.running)               // LEVEL SELECTION
							{
								Main.scale(c, Main.imageScale, Main.imageScale);
								Main.getScaledInput(view);
								LevelSelection.start(c, view);
							} else
							{
								Main.scale(c, Main.imageScale, Main.imageScale);
								Main.getScaledInput(view);

								if (Main.paused)
								{
									Main.Pause(c, view);
								} else if (Main.end)
								{
									Main.endScreen(c, view);
								} else
								{
									Main.camera();
									Main.drawBackground(c);

									try
									{
										Swiper.start(c, view);
									} catch (Exception e)
									{
										e.printStackTrace();
									}

									Main.moveFish();
									Main.moveProjectiles();
									Main.moveBubble();
									Main.collision();
									Main.drawMisc(c);
									Main.drawBlocks(c);
									Main.drawFish(c);
									Main.drawFan(c, view);
									Main.drawProjectiles(c);
									Main.drawBubble(c);
									Main.drawHUD(c);
									Main.coinAnimation(c);
									Main.healthAnimation(c);
									Main.airTimer();
									Main.stateCheck(view);
								}
							}

							view.getHolder().unlockCanvasAndPost(c); // Update
						}
					}
				}
			}
		}
	}

	public class SView extends SurfaceView
	{
		int x;
		int y;
		int state = 0;
		public SurfaceHolder h;
		public GameThread GT;

		public SView(Context context)
		{
			super(context);
			x = 0;
			y = 0;
			h = getHolder();
			GT = new GameThread(this);

			setOnTouchListener(new View.OnTouchListener()
			{
				public boolean onTouch(View v, MotionEvent e)
				{
					x = (int) e.getX();
					y = (int) e.getY();
					state = e.getAction();

					Swiper.ontouch = true;

					return false;
				}
			});
			setLongClickable(true);
		}

		@Override
		protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld)
		{
			if (yNew > yOld)
			{
				GT.start();
			}

			super.onSizeChanged(xNew, yNew, xOld, yOld);
		}
	}
}
