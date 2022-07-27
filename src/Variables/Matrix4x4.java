package Variables;

public class Matrix4x4 {
    public float[][] m = new float[4][4];

    // Multiplying a 4x4 matrix by a 4x1 matrix
    public Vector3f multiply(Vector3f in) {
        Vector3f out = new Vector3f(  in.x * m[0][0] + in.y * m[1][0] + in.z * m[2][0] + m[3][0],
                in.x * m[0][1] + in.y * m[1][1] + in.z * m[2][1] + m[3][1],
                in.x * m[0][2] + in.y * m[1][2] + in.z * m[2][2] + m[3][2]);

        // The fourth element of the 4x1 matrix [x,y,z,w]
        float w = in.x * m[0][3] + in.y * m[1][3] + in.z * m[2][3] + m[3][3];

        if (w != 0.0f) {
            out.x /= w;
            out.y /= w;
            out.z /= w;
        }

        return out;
    }

    public void initProjectionMatrix (int viewWidth, int viewHeight, float fNear, float fFar, float FOV) {

        float fFovRad = 1.0f / (float)Math.tan(FOV * 0.5f / 180.0f * Math.PI);
        this.m[0][0] = (float)viewHeight / (float)viewWidth;
        this.m[1][1] = fFovRad;
        this.m[2][2] = fFar / (fFar - fNear);
        this.m[3][2] = (-fFar * fNear) / (fFar - fNear);
        this.m[2][3] = 1.0f;
        this.m[3][3] = 0.0f;

    }

    public void initRotationMatrixX (float frameTime) {
        frameTime *= 0.25f;
        this.m[0][0] = 1.0f;
        this.m[1][1] = (float)Math.cos(frameTime);
        this.m[1][2] = (float)Math.sin(frameTime);
        this.m[2][1] = (float)-Math.sin(frameTime);
        this.m[2][2] = (float)Math.cos(frameTime);
        this.m[3][3] = 1.0f;
    }

    public void initRotationMatrixZ (float frameTime) {
        frameTime *= 0.5f;
        this.m[0][0] = (float)Math.cos(frameTime);
        this.m[0][1] = (float)Math.sin(frameTime);
        this.m[1][0] = (float)-Math.sin(frameTime);
        this.m[1][1] = (float)Math.cos(frameTime);
        this.m[2][2] = 1.0f;
        this.m[3][3] = 1.0f;
    }
}
