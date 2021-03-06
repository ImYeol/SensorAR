package com.mygdx.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication  {

	private MainApplicationRenderer mainApplicationRenderer;
	private View view;
	private FrameLayout main;

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

	//	cvCameraView= (JavaCameraView)findViewById(R.id.java_surface_view);
	//	cvCameraView.setCvCameraViewListener(this);
		mainApplicationRenderer =new MainApplicationRenderer(this);
		view=initializeForView(mainApplicationRenderer,config);

		main=(FrameLayout)findViewById(R.id.main_frame);
		main.addView(view, 0);
		addContentView(LayoutInflater.from(this).inflate(R.layout.controller, null),
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		setComponents();
		setListener();

	}

	private void setListener() {
		Components.btn_x_plus.setOnClickListener(mainApplicationRenderer.btnListener);
		Components.btn_y_plus.setOnClickListener(mainApplicationRenderer.btnListener);
		Components.btn_z_plus.setOnClickListener(mainApplicationRenderer.btnListener);
		Components.btn_x_minus.setOnClickListener(mainApplicationRenderer.btnListener);
		Components.btn_y_minus.setOnClickListener(mainApplicationRenderer.btnListener);
		Components.btn_z_minus.setOnClickListener(mainApplicationRenderer.btnListener);
	}

	private void setComponents(){
		Components.btn_x_minus=(Button)findViewById(R.id.btn_x_minus);
		Components.btn_y_minus=(Button)findViewById(R.id.btn_y_minus);
		Components.btn_z_minus=(Button)findViewById(R.id.btn_z_minus);
		Components.btn_x_plus=(Button)findViewById(R.id.btn_x_plus);
		Components.btn_y_plus=(Button)findViewById(R.id.btn_y_plus);
		Components.btn_z_plus=(Button)findViewById(R.id.btn_z_plus);
		Components.x_view=(TextView)findViewById(R.id.lookat_x);
		Components.y_view=(TextView)findViewById(R.id.lookat_y);
		Components.z_view=(TextView)findViewById(R.id.lookat_z);
		Components.up_x_view=(TextView)findViewById(R.id.up_x);
		Components.up_y_view=(TextView)findViewById(R.id.up_y);
		Components.up_z_view=(TextView)findViewById(R.id.up_z);

	}

	@Override
	protected void onDestroy() {
	//	mainApplicationRenderer.dispose();
		super.onDestroy();
	}

}
