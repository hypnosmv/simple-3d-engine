package Engine;

import Window.*;

import java.util.ArrayList;

import Utility.QuickSort;
import Variables.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.*;

public class Output3d {

    // User input
    private UserUpdate userUpdate = new UserUpdate();

    // 3d objects
    private ArrayList<Mesh> meshes = new ArrayList<>();

    // Projection matrix
    private Matrix4x4 projectionMatrix = new Matrix4x4();

    // View matrix
    private Matrix4x4 viewMatrix = new Matrix4x4();

    // Camera
    private Camera camera = new Camera();

    // Frame time
    private float frameTime = 0.0f;

    // Constructor
    public Output3d(int viewWidth, int viewHeight) {
        projectionMatrix.initProjectionMatrix(viewWidth, viewHeight, camera.getCameraNear(), camera.cameraFar(), camera.getFOV());

        addMesh("input.obj");
    }

    // Loop process
    public void display3d(float frameTime) {

        this.frameTime = frameTime;
        float elapsedTime = (float)glfwGetTime();


        keyboardInput();

        Vector3f cameraTarget = new Vector3f(0.0f, 0.0f, 1.0f);
        camera.lookDirection = Matrix4x4.multiplyMat4x4Mat4x4(Matrix4x4.rotateX(camera.fXaw), Matrix4x4.rotateY(camera.fYaw)).multiply(cameraTarget);
        cameraTarget = Vector3f.addVectors(camera.position, camera.lookDirection);

        // Get independent of fXaw look direction vector
        // Move direction vector is used only for WSAD movement
        camera.moveDirection.x = camera.lookDirection.x;
        camera.moveDirection.z = camera.lookDirection.z;
        camera.moveDirection.normalize();

        viewMatrix = Matrix4x4.quickInverse(Matrix4x4.pointAt(camera.position, cameraTarget, new Vector3f(0.0f, 1.0f, 0.0f)));

        for (int mesh = 0; mesh < meshes.size(); mesh++) {

            ArrayList<Polygon> renderQueue = new ArrayList<>();

            for (int polygon = 0; polygon < meshes.get(mesh).polygons.size(); polygon++) {

                Polygon renderRequest = new Polygon();

                for(int i = 0; i < meshes.get(mesh).polygons.get(polygon).verts.size(); i++) {
                    renderRequest.verts.add(meshes.get(mesh).polygons.get(polygon).verts.get(i));
                }

                // Find normal to polygon plane
                Vector3f normal = Vector3f.crossProduct(new Vector3f(renderRequest.verts.get(1).x - renderRequest.verts.get(0).x, renderRequest.verts.get(1).y - renderRequest.verts.get(0).y, renderRequest.verts.get(1).z - renderRequest.verts.get(0).z), new Vector3f(renderRequest.verts.get(2).x - renderRequest.verts.get(0).x, renderRequest.verts.get(2).y - renderRequest.verts.get(0).y, renderRequest.verts.get(2).z - renderRequest.verts.get(0).z));
                normal.normalize();

                // Compare 2 vectors using dot product (taking into account the camera vector)
                if (Vector3f.dotProduct(normal, Vector3f.subtractVectors(renderRequest.verts.get(0), camera.position)) < 0) {

                    // Convert world space into view space
                    for (int i = 0; i < renderRequest.verts.size(); i++) {
                        renderRequest.verts.set(i, viewMatrix.multiply(renderRequest.verts.get(i)));
                    }

                    // View space trimming, we don't need to process window space trim,
                    // because OpenGL doesn't draw outside the window
                    renderRequest = Vector3f.clipAgainstPlane(new Vector3f(0.0f, 0.0f, 0.1f), new Vector3f(0.0f, 0.0f, 1.0f), renderRequest);

                    // Light's color intensity (very primitive)
                    renderRequest.color = - normal.x * camera.lookDirection.x - normal.y * camera.lookDirection.y - normal.z * camera.lookDirection.z;

                    // Calculate depth of polygon
                    renderRequest.calculateZDepth();

                    // Add it to the queue
                    renderQueue.add(renderRequest);

                }

            }

            // Sort using z-depth (painter's algorithm)
            renderQueue = QuickSort.quickSortPolygons(renderQueue);

            // Draw polygons of the mesh
            for (Polygon polygon : renderQueue) {
                glBegin(GL_POLYGON);
                for (Vector3f vector : polygon.verts) {

                    // Project onto the display using the projection matrix
                    vector = projectionMatrix.multiply(vector);

                    // Color and display cords
                    glColor3f(polygon.color, polygon.color, polygon.color);
                    glVertex2f(vector.x, vector.y);

                }
                glEnd();
            }
        }
    }

    // Movement handling
    private void keyboardInput() {
        // Space - move up, left shift - move down
        if (userUpdate.statusKeySpace()) camera.position.y += camera.moveSpeed * this.frameTime;
        if (userUpdate.statusKeyLeftShift()) camera.position.y -= camera.moveSpeed * this.frameTime;

        // Take into account multiplication of camera move speed by sqrt(2)
        float cameraMoveSpeed = camera.moveSpeed;
        if ((userUpdate.statusKeyW() && userUpdate.statusKeyD()) || (userUpdate.statusKeyD() && userUpdate.statusKeyS()) ||
                (userUpdate.statusKeyS() && userUpdate.statusKeyA()) || (userUpdate.statusKeyA() && userUpdate.statusKeyW())) {
            // 1 / sqrt(2) = 0.70710678
            cameraMoveSpeed *= 0.70710678f;
        }

        // W - move forward, S - move backwards
        Vector3f cameraForward = Vector3f.multiplyVector(new Vector3f(camera.moveDirection.x, 0.0f, camera.moveDirection.z), cameraMoveSpeed * this.frameTime);
        if (userUpdate.statusKeyW()) camera.position = Vector3f.addVectors(camera.position, cameraForward);
        if (userUpdate.statusKeyS()) camera.position = Vector3f.subtractVectors(camera.position, cameraForward);

        // A - move left, D - move right
        Vector3f cameraLeft = Vector3f.multiplyVector(new Vector3f(-camera.moveDirection.z, 0.0f, camera.moveDirection.x), cameraMoveSpeed * this.frameTime);
        if (userUpdate.statusKeyA()) camera.position = Vector3f.addVectors(camera.position, cameraLeft);
        if (userUpdate.statusKeyD()) camera.position = Vector3f.subtractVectors(camera.position, cameraLeft);

        // Mouse camera rotation
        camera.fXaw += userUpdate.getCursorPosX() * camera.rotationSpeed * this.frameTime;
        camera.checkFXaw();
        camera.fYaw += userUpdate.getCursorPosY() * camera.rotationSpeed * this.frameTime;

    }

    // Add a new mesh
    private void addMesh(String file) {
        Mesh mesh = new Mesh();
        mesh.inputOBJ(file);
        meshes.add(mesh);
    }

}
