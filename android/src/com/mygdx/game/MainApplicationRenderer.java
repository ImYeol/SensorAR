package com.mygdx.game;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

/**
 * Created by yeol on 16. 1. 25.
 */
public class MainApplicationRenderer implements ApplicationListener,ModelCreateListener {

    private static final String TAG="MainApplicationRenderer";
    private Context context;
    public Environment environment;
//    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    public Model model;
    public Model arrow;
    public ModelInstance instance;
    public AssetManager assets;

    public ModelInstance box;

    public LibGDXPerspectiveCamera GDXCam;
    public ButtonClickListener btnListener;
    private boolean loading=false;

    private RenderModelList RenderModels;

    private float lookX=10;
    private float lookY=10;
    private float lookZ=10;


    public MainApplicationRenderer(Context context){

        this.context=context;
        btnListener=new ButtonClickListener();
    }

    @Override
    public void create() {

        setEnvironment();
        modelBatch = new ModelBatch();
        GDXCam=new LibGDXPerspectiveCamera(context,40f,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        SensorData.getInstance().registerRotationListener(GDXCam);
        GDXCam.setModelMaker(this);
        RenderModels=new RenderModelList();
        assets = new AssetManager();
        MakeDefaultModels();
    //    SensorData.getInstance().startSensing(context);
    }

    @Override
    public void MakeDefaultModels(){
        assets.load("arrow.g3db",Model.class);
        loading=true;
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(5f, 5f, 5f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    }

    @Override
    public void MakeModels(float mx,float my,float mz){
        assets.load("arrow.g3db",Model.class);
        loading=true;
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(10f, 10f, 10f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    }
    private void setEnvironment(){
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 11f, 12f, 10f));
    }

    private void doneLoading(){
     //   Log.d(TAG,"doneLoading");
        float[] modelCoord={0f,1f,1f};
        //modelCoord=GDXCam.getModelCoord();
        arrow= assets.get("arrow.g3db",Model.class);
        instance= new ModelInstance(arrow);
        instance.transform.setToTranslation(modelCoord[0], modelCoord[1], modelCoord[2]);
        instance.transform.rotate(0, 1, 0, -20);
        instance.transform.rotate(0, 0, 1, 130);
        instance.transform.rotate(1,0,0,-10);
        instance.transform.rotate(0,1,0,10);
        instance.transform.scale(5f, 5f, 5f);


        box= new ModelInstance(model);

        RenderModels.add(instance);
        RenderModels.add(box);
        loading=false;
        //SensorData.getInstance().startSensing(context);
        Log.d(TAG,"doneLoading");
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
  //      Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  //      Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        if(loading && assets.update())
            doneLoading();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        GDXCam.render();
    //    cam.update();
        modelBatch.begin(GDXCam);
        if(instance != null)
            RenderModels.Render(modelBatch,environment);
        modelBatch.end();
    }

    @Override
    public void pause() {
        if(SensorData.getInstance().IsStarted())
            SensorData.getInstance().stopSensing();
    }

    @Override
    public void resume() {
    //    if(SensorData.getInstance().IsStarted() == false)
    //        SensorData.getInstance().startSensing(context);
    //    SensorData.getInstance().startSensing(context);
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        arrow.dispose();
        SensorData.getInstance().stopSensing();
    //    assets.dispose();

    }

    class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_x_plus:
                //    GDXCam.lookAt(++lookX,lookY,lookZ);
                //    instance.transform.rotate(1,0,0,10);
                    if(SensorData.getInstance().IsStarted() == false)
                        SensorData.getInstance().startSensing(context);
                    break;
                case R.id.btn_y_plus:
                //    GDXCam.lookAt(lookX,++lookY,lookZ);
                //    instance.transform.rotate(0,1,0,10);
                    if(SensorData.getInstance().IsStarted())
                        SensorData.getInstance().stopSensing();
                    break;
                case R.id.btn_z_plus:
                    GDXCam.lookAt(lookX,lookY,++lookZ);
                //    instance.transform.rotate(0,0,1,10);
                    break;
                case R.id.btn_x_minus:
                    GDXCam.lookAt(--lookX,lookY,lookZ);
                //    instance.transform.rotate(1,0,0,-10);
                    break;
                case R.id.btn_y_minus:
                    GDXCam.lookAt(lookX,--lookY,lookZ);
                //    instance.transform.rotate(0,1,0,-10);
                    break;
                case R.id.btn_z_minus:
                    GDXCam.lookAt(lookX,lookY,--lookZ);
               //     instance.transform.rotate(0,0,1,-10);
                    break;
            }
            showLookAt();
        }
    }
    private void showLookAt(){
        Components.x_view.setText("" + lookX);
        Components.y_view.setText("" + lookY);
        Components.z_view.setText("" + lookZ);

    }

}
