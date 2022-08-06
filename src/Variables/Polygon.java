package Variables;

public class Polygon {
    public Vector3f[] verts = new Vector3f[3];
    public float zDepth = 0.0f;
    public float lightDensity = 0.0f;

    public Polygon() {
        verts[0] = new Vector3f(0.0f, 0.0f, 0.0f);
        verts[1] = new Vector3f(0.0f, 0.0f, 0.0f);
        verts[2] = new Vector3f(0.0f, 0.0f, 0.0f);
    }

    public void calculateZDepth() {
        float zSum = 0.0f;
        int i = 0;
        for(; i < 3; i++) {
            zSum += verts[i].z;
        }
        zDepth = zSum / (float)(i+1);
    }
}
