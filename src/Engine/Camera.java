package Engine;

import Variables.*;

public class Camera {

    // Field of view
    private float cameraNear = 0.1f;
    private float cameraFar = 1000.0f;
    private float FOV = 90.0f;

    // Position
    public Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);

    public float getCameraNear() {
        return cameraNear;
    }

    public float cameraFar() {
        return cameraFar;
    }

    public float getFOV() {
        return FOV;
    }
}
