package com.mygdx.game;

import android.content.Context;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;

/**
 * Created by yeol on 16. 1. 25.
 */
public class LibGDXPerspectiveCamera extends PerspectiveCamera implements HeadRotationInterface{

    private boolean geo ;
    private Context context;

    private float[] lookAtVector = new float[3];
    private float[] upVector = new float[3];
    private float[] positionVector= { 0f, 0f, 0f};

    public LibGDXPerspectiveCamera(Context context,float fov, int width, int height) {
        super(fov,width, height);
        this.context=context;
        this.far=1000f;
        this.near=1f;
        this.position.set(0f, 0f, 0f);
        this.lookAt(10f, 10f, 10f);
        this.up.set(0f,0f,1f);
    }

    public void render(){
        this.update();
        this.up.set(upVector[0], upVector[1], upVector[2]);
        this.lookAt(lookAtVector[0], lookAtVector[1], lookAtVector[2]);
    }

    @Override
    public void update() {
        super.update();

    }

    @Override
    public void update(boolean updateFrustum) {
        super.update(updateFrustum);

    }

    public void dispose(){

    }

    @Override
    public void onRotationChanged(float[] RotationMat) {
        Matrix4 matT = new Matrix4(RotationMat).tra();
        //  float[] newLookAt = { 0, 0, -far, 1 };
        float[] newLookAt = { 10f, 10f, 10f, 1 };
        float[] newUp = { 0, 1, 0, 1 };
        Matrix4.mulVec(matT.val, newLookAt);
        Matrix4.mulVec(matT.val, newUp);

        lookAtVector[0] = newLookAt[0] + positionVector[0];
        lookAtVector[1] = newLookAt[1] + positionVector[1];
        lookAtVector[2] = newLookAt[2] + positionVector[2];
        upVector[0]=newUp[0];
        upVector[1]=newUp[1];
        upVector[2]=newUp[2];
        // up = newUp;

        showLookAt();
    }

    private void showLookAt(){
        Components.x_view.setText("" + lookAtVector[0]);
        Components.y_view.setText("" + lookAtVector[1]);
        Components.z_view.setText("" + lookAtVector[2]);

        Components.up_x_view.setText("" + upVector[0]);
        Components.up_y_view.setText("" + upVector[1]);
        Components.up_z_view.setText("" + upVector[2]);
    }

}
