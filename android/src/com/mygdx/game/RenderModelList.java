package com.mygdx.game;


import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;

/**
 * Created by yeol on 16. 2. 11.
 */
public class RenderModelList extends ArrayList<ModelInstance> {

    public void Render(ModelBatch batch,Environment env){
        for(ModelInstance instance : this){
            batch.render(instance,env);
        }
    }
}
