package Engine;

import Variables.*;

public class Camera {

    // Field of view
    private float FOV = 90.0f;

    // Position
    public Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);

    // Initialization of the projection matrix
    public Matrix4x4 initProjectionMatrix (int viewWidth, int viewHeight) {

        Matrix4x4 projectionMatrix = new Matrix4x4();

        float fNear = 0.1f;
        float fFar = 1000.0f;
        float fAspectRatio = (float)viewHeight / (float)viewWidth;
        float fFovRad = 1.0f / (float)Math.tan(FOV * 0.5f / 180.0f * Math.PI);

        projectionMatrix.m[0][0] = fAspectRatio;
        projectionMatrix.m[1][1] = fFovRad;
        projectionMatrix.m[2][2] = fFar / (fFar - fNear);
        projectionMatrix.m[3][2] = (-fFar * fNear) / (fFar - fNear);
        projectionMatrix.m[2][3] = 1.0f;
        projectionMatrix.m[3][3] = 0.0f;

        return projectionMatrix;
    }
}
