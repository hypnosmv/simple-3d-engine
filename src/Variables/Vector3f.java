package Variables;

public class Vector3f {

    // Coordinates in 3d space
    public float x;
    public float y;
    public float z;

    // Constructor
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void normalize () {
        float length = (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        this.x /= length;
        this.y /= length;
        this.z /= length;
    }

    public void newVector(Vector3f vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public static Vector3f crossProduct(Vector3f vector1, Vector3f vector2) {
        return new Vector3f(vector1.y * vector2.z - vector1.z * vector2.y, vector1.z * vector2.x - vector1.x * vector2.z, vector1.x * vector2.y - vector1.y * vector2.x);
    }

    public static float dotProduct(Vector3f vector1, Vector3f vector2) {
        return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z;
    }

    public static Vector3f addVectors(Vector3f vector1, Vector3f vector2) {
        return new Vector3f(vector1.x + vector2.x, vector1.y + vector2.y, vector1.z + vector2.z);
    }

    public static Vector3f subtractVectors(Vector3f vector1, Vector3f vector2) {
        return new Vector3f(vector1.x - vector2.x, vector1.y - vector2.y, vector1.z - vector2.z);
    }

    public static Vector3f multiplyVector(Vector3f vector, float k) {
        return new Vector3f(vector.x * k, vector.y * k, vector.z * k);
    }

}
