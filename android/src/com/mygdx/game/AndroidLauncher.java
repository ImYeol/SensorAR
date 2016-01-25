package com.mygdx.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {

	private Renderer renderer;
	private View view;
	private FrameLayout main;
	private RelativeLayout controllerView;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

//		initialize(new MyGdxGame(),config);

		config.useGLSurfaceView20API18=false;
		config.r=8;
		config.g=8;
		config.b=8;
		config.a=8;
		config.useAccelerometer=false;
		config.useCompass=false;


		renderer=new Renderer(this);
		view=initializeForView(renderer,config);

		main=(FrameLayout)findViewById(R.id.main_frame);
		main.addView(view,0);

		controllerView=(RelativeLayout)findViewById(R.layout.controller);
		addContentView(LayoutInflater.from(this).inflate(R.layout.controller,null),
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

	}

	@Override
	protected void onDestroy() {
	//	renderer.dispose();
		super.onDestroy();
	}
}
