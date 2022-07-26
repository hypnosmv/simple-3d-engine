package Engine;

import Variables.*;

public class Camera {

    // Field of view
    private float cameraNear = 0.1f;
    private float cameraFar = 1000.0f;
    private float FOV = 90.0f;
    public float moveSpeed = 50.0f;
    public float rotationSpeed = 0.001f;
    public float fXaw = 0.0f;
    public float fYaw = 0.0f;

    // Position and look direction
    public Vector3f position = new Vector3f(0.0f, 0.0f, -200.0f);
    public Vector3f lookDirection = new Vector3f(0.0f, 0.0f, 0.0f);

    // Move direction vector used only for WSAD fps-like movement
    public Vector3f moveDirection  = new Vector3f(0.0f, 0.0f, 0.0f);

    public float getCameraNear() {
        return cameraNear;
    }

    public float cameraFar() {
        return cameraFar;
    }

    public float getFOV() {
        return FOV;
    }

    // Rotation along the x-axis can cause flipping over,
    // so we should check if fXaw angle is in range (-pi/2; pi/2)
    public void checkFXaw() {

        // Somehow something bigger than 2 works and camera doesn't flip
        float halfPI = (float)Math.PI / 2.001f;

        // Don't let the camera rotate through your head and legs
        if (this.fXaw < -halfPI) this.fXaw = -halfPI;
        else if (this.fXaw > halfPI) this.fXaw = halfPI;
    }
}
