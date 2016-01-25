package com.mygdx.game;

import android.content.Context;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
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
public class Renderer implements ApplicationListener {

    private Context context;
    public Environment environment;
//    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    public Model model;
    public Model arrow;
    public ModelInstance instance;
    public ModelInstance instance2;

    public LibGDXPerspectiveCamera cam;

    public Renderer(Context context){
        this.context=context;
    }
    @Override
    public void create() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();

   /*     cam = new PerspectiveCamera(30, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(20f, 20f, 20f);
        cam.lookAt(0f,0f,0f);
        cam.near = 1f;
        cam.far = 100f;
*/
        cam=new LibGDXPerspectiveCamera(context,30f,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        cam.far=100f;
        cam.near=1f;
        cam.position.set(10f,10f,10f);
        cam.lookAt(0f,0f,0f);
      //  cam.update();

        ModelBuilder modelBuilder = new ModelBuilder();
        arrow = modelBuilder.createArrow(5f,5f,5f,0f,0f,0f,0.1f,0.1f,5,GL20.GL_TRIANGLES,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        model = modelBuilder.createBox(5f, 5f, 5f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model,0f,0f,0f);
        instance2 = new ModelInstance(arrow,0f,0f,0f);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
  //      Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  //      Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        cam.render();
        modelBatch.begin(cam);
     //   modelBatch.render(instance, environment);
        modelBatch.render(instance2,environment);
        modelBatch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        model.dispose();
        cam.dispose();
    }
}
