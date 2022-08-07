package Variables;

public class Matrix4x4 {
    public float[][] m = new float[4][4];

    // Multiplying a 4x4 matrix by a 4x1 matrix
    public Vector3f multiply(Vector3f in) {
        Vector3f out = new Vector3f(0.0f, 0.0f, 0.0f);
        out.x = in.x * m[0][0] + in.y * m[1][0] + in.z * m[2][0] + in.w * m[3][0];
        out.y = in.x * m[0][1] + in.y * m[1][1] + in.z * m[2][1] + in.w * m[3][1];
        out.z = in.x * m[0][2] + in.y * m[1][2] + in.z * m[2][2] + in.w * m[3][2];
        out.w = in.x * m[0][3] + in.y * m[1][3] + in.z * m[2][3] + in.w * m[3][3];
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

    public static Matrix4x4 rotateX (float angleRad) {
        Matrix4x4 matrix = new Matrix4x4();

        matrix.m[0][0] = 1.0f;
        matrix.m[1][1] = (float)Math.cos(angleRad);
        matrix.m[1][2] = (float)Math.sin(angleRad);
        matrix.m[2][1] = (float)-Math.sin(angleRad);
        matrix.m[2][2] = (float)Math.cos(angleRad);
        matrix.m[3][3] = 1.0f;

        return matrix;
    }

    public static Matrix4x4 rotateY (float angleRad) {
        Matrix4x4 matrix = new Matrix4x4();

        matrix.m[0][0] = (float)Math.cos(angleRad);
        matrix.m[0][2] = (float)Math.sin(angleRad);
        matrix.m[2][0] = (float)-Math.sin(angleRad);
        matrix.m[1][1] = 1.0f;
        matrix.m[2][2] = (float)Math.cos(angleRad);
        matrix.m[3][3] = 1.0f;

        return matrix;
    }

    public static Matrix4x4 rotateZ (float angleRad) {
        Matrix4x4 matrix = new Matrix4x4();

        matrix.m[0][0] = (float)Math.cos(angleRad);
        matrix.m[0][1] = (float)Math.sin(angleRad);
        matrix.m[1][0] = (float)-Math.sin(angleRad);
        matrix.m[1][1] = (float)Math.cos(angleRad);
        matrix.m[2][2] = 1.0f;
        matrix.m[3][3] = 1.0f;

        return matrix;
    }

    public static Matrix4x4 multiplyMat4x4Mat4x4 (Matrix4x4 matrix1, Matrix4x4 matrix2) {
        Matrix4x4 matrix = new Matrix4x4();
        for (int c = 0; c < 4; c++)
            for (int r = 0; r < 4; r++)
                matrix.m[r][c] = matrix1.m[r][0] * matrix2.m[0][c] + matrix1.m[r][1] * matrix2.m[1][c] + matrix1.m[r][2] * matrix2.m[2][c] + matrix1.m[r][3] * matrix2.m[3][c];
        return matrix;
    }

    public static Matrix4x4 pointAt (Vector3f position, Vector3f target, Vector3f up) {

        // Calculate new forward direction
        Vector3f newForward = Vector3f.subtractVectors(target, position);
        newForward.normalize();

        // Calculate new up direction
        Vector3f a = Vector3f.multiplyVector(newForward, Vector3f.dotProduct(up, newForward));
        Vector3f newUp = Vector3f.subtractVectors(up, a);
        newUp.normalize();

        // Calculate new right direction
        Vector3f newRight = Vector3f.crossProduct(newUp, newForward);

        // Construct dimensioning and translation matrix
        Matrix4x4 matrix = new Matrix4x4();
        matrix.m[0][0] = newRight.x;
        matrix.m[1][0] = newUp.x;
        matrix.m[2][0] = newForward.x;
        matrix.m[3][0] = position.x;
        matrix.m[0][1] = newRight.y;
        matrix.m[1][1] = newUp.y;
        matrix.m[2][1] = newForward.y;
        matrix.m[3][1] = position.y;
        matrix.m[0][2] = newRight.z;
        matrix.m[1][2] = newUp.z;
        matrix.m[2][2] = newForward.z;
        matrix.m[3][2] = position.z;
        matrix.m[0][3] = 0.0f;
        matrix.m[1][3] = 0.0f;
        matrix.m[2][3] = 0.0f;
        matrix.m[3][3] = 1.0f;

        return matrix;
    }

    // Matrix inverse only for rotation/translation
    public static Matrix4x4 quickInverse(Matrix4x4 matrix) {

        Matrix4x4 outputMatrix = new Matrix4x4();

        outputMatrix.m[0][0] = matrix.m[0][0]; outputMatrix.m[0][1] = matrix.m[1][0]; outputMatrix.m[0][2] = matrix.m[2][0]; outputMatrix.m[0][3] = 0.0f;
        outputMatrix.m[1][0] = matrix.m[0][1]; outputMatrix.m[1][1] = matrix.m[1][1]; outputMatrix.m[1][2] = matrix.m[2][1]; outputMatrix.m[1][3] = 0.0f;
        outputMatrix.m[2][0] = matrix.m[0][2]; outputMatrix.m[2][1] = matrix.m[1][2]; outputMatrix.m[2][2] = matrix.m[2][2]; outputMatrix.m[2][3] = 0.0f;
        outputMatrix.m[3][0] = -(matrix.m[3][0] * outputMatrix.m[0][0] + matrix.m[3][1] * outputMatrix.m[1][0] + matrix.m[3][2] * outputMatrix.m[2][0]);
        outputMatrix.m[3][1] = -(matrix.m[3][0] * outputMatrix.m[0][1] + matrix.m[3][1] * outputMatrix.m[1][1] + matrix.m[3][2] * outputMatrix.m[2][1]);
        outputMatrix.m[3][2] = -(matrix.m[3][0] * outputMatrix.m[0][2] + matrix.m[3][1] * outputMatrix.m[1][2] + matrix.m[3][2] * outputMatrix.m[2][2]);
        outputMatrix.m[3][3] = 1.0f;

        return outputMatrix;
    }
}
