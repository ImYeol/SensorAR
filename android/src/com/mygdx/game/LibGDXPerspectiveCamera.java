package com.mygdx.game;

import android.content.Context;
import android.opengl.Matrix;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Quaternion;

/**
 * Created by yeol on 16. 1. 25.
 */
public class LibGDXPerspectiveCamera extends PerspectiveCamera implements HeadRotationInterface{

    private boolean geo ;
    private Context context;

    private float[] lookAtVector = { 0f,0f,-1000,1f};
    private float[] upVector = { 0f,1f,0f,0f};
    private float[] positionVector= { 0f, 0f, 0f};

    private ModelCreateListener modelMaker;
    private boolean NotSetModel=true;

    private float[] modelCoord=new float[3];
    private int setCnt=0;

    private float[] traMat=new float[16];
    public LibGDXPerspectiveCamera(Context context,float fov, int width, int height) {
        super(fov,width, height);
        this.context=context;
        this.far=1000f;
        this.near=1f;
        this.position.set(0f, 0f, 0f);
        this.lookAt(0f, 0f, -far);
        this.up.set(0f, 1f, 0f);
    }

    public void render(){
        this.update();
        this.up.set(upVector[0], upVector[1], upVector[2]);
        this.lookAt(lookAtVector[0], lookAtVector[1], lookAtVector[2]);
    }

    public void setLookAtVector(float x,float y,float z){
        lookAtVector[0]=x;
        lookAtVector[1]=y;
        lookAtVector[2]=z;
    }

    public void setModelMaker(ModelCreateListener modelMaker){
        this.modelMaker=modelMaker;
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
     //   Matrix4 matT = new Matrix4(RotationMat).tra();
        //  float[] newLookAt = { 0, 0, -far, 1 };
        Matrix.transposeM(traMat,0,RotationMat,0);
        float[] newLookAt = { 0f, 0f, -far, 1 };
        float[] newUp = { 0, 1, 0, 1 };
    //    float[] newLookAt = { 0f, 10f, 0f, 1 };
    //    float[] newUp = { 0, 0, 1, 1 };

        Matrix.multiplyMV(lookAtVector,0,traMat,0,newLookAt,0);
        Matrix.multiplyMV(upVector,0,traMat,0,newUp,0);
     //   Matrix4.mulVec(matT.val, newLookAt);
     //   Matrix4.mulVec(matT.val, newUp);

     /*   if(NotSetModel) {
          //  setCnt++;
          //  float[] mx;
          //  modelCoord=rotateCoord(newLookAt);
            if( newLookAt[0] != 0.0f && modelMaker != null) {
                rotateCoord(newLookAt);
                modelMaker.MakeModels(modelCoord[0], modelCoord[1], modelCoord[2]);
                NotSetModel = false;
            }
        }
    */
    /*    lookAtVector[0] = newLookAt[0];
        lookAtVector[1] = newLookAt[1];
        lookAtVector[2] = newLookAt[2];
        upVector[0]=newUp[0];
        upVector[1]=newUp[1];
        upVector[2]=newUp[2];*/
        // up = newUp;

        showLookAt();
    }

    public float[] getModelCoord(){
        return modelCoord;
    }

    private void rotateCoord(float[] oldCoord){
     //   float[] mx=new float[3];
     /*   modelCoord[0]=(float)(oldCoord[0]*Math.cos(90 * Math.PI / 180) - oldCoord[1]*Math.sin(90 * Math.PI / 180));
        modelCoord[1]=(float)(oldCoord[0]*Math.sin(90 * Math.PI/180) + oldCoord[1]*Math.cos(90 * Math.PI/180));
        modelCoord[2]=oldCoord[2];*/
        float temp=oldCoord[0] +8;
        modelCoord[0]=(temp >= 15) ? -(temp % 15) : temp;

        temp = oldCoord[1] +4 ;
        modelCoord[1]=(temp >= 15) ? -(temp % 15) : temp;

        modelCoord[2]=oldCoord[2];
      //  return mx;

    }
    @Override
    public void onQuaternionChanged(float[] Quat) {
        Quaternion quat=new Quaternion();
        quat.set(Quat[1],Quat[2],Quat[3],Quat[0]);
        this.rotate(quat);

    }

    private void showLookAt(){
        Components.x_view.setText("" + lookAtVector[0]);
        Components.y_view.setText("" + lookAtVector[1]);
        Components.z_view.setText("" + lookAtVector[2]);

    /*    Components.up_x_view.setText("" + modelCoord[0]);
        Components.up_y_view.setText("" + modelCoord[1]);
        Components.up_z_view.setText("" + modelCoord[2]);
*/
        Components.up_x_view.setText("" + upVector[0]);
        Components.up_y_view.setText("" + upVector[1]);
        Components.up_z_view.setText("" + upVector[2]);

    }

}
