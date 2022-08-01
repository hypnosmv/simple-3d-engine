package Variables;

import java.util.ArrayList;

public class Polygon {
    public ArrayList<Vector3f> verts = new ArrayList<>();
    public float zDepth = 0.0f;
    public float color = 0.0f;

    public void calculateZDepth() {
        float zSum = 0.0f;
        int i = 0;
        for(; i < verts.size(); i++) {
            zSum += verts.get(i).z;
        }
        zDepth = zSum / (float)(i+1);
    }
}