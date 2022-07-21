package ProjectionUtil;

public class mat4x4 {
    public float[][] m = new float[4][4];

    public vec3f multiplyMatVec(vec3f in) {
        vec3f out = new vec3f(  in.x * m[0][0] + in.y * m[1][0] + in.z * m[2][0] + m[3][0],
                in.x * m[0][1] + in.y * m[1][1] + in.z * m[2][1] + m[3][1],
                in.x * m[0][2] + in.y * m[1][2] + in.z * m[2][2] + m[3][2]);

        float w = in.x * m[0][3] + in.y * m[1][3] + in.z * m[2][3] + m[3][3];

        if (w != 0.0f) {
            out.x /= w;
            out.y /= w;
            out.z /= w;
        }

        return out;
    }
}
