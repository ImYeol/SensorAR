package com.mygdx.game;

/**
 * Created by yeol on 16. 2. 11.
 */
public interface HeadRotationInterface {
    void onRotationChanged(float[] RotationMat);

    void onQuaternionChanged(float[] Quat);
}
