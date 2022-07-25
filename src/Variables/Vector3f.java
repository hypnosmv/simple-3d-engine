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
}
