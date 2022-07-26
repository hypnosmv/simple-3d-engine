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

    public void crossProduct(Vector3f vector1, Vector3f vector2) {
        this.x = vector1.y * vector2.z - vector1.z * vector2.y;
        this.y = vector1.z * vector2.x - vector1.x * vector2.z;
        this.z = vector1.x * vector2.y - vector1.y * vector2.x;
    }
}
