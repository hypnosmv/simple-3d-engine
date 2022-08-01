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

    // Rotation matrices
    Matrix4x4 rotationMatrixZ = new Matrix4x4();
    Matrix4x4 rotationMatrixX = new Matrix4x4();

    // Camera
    Camera camera = new Camera();

    // Constructor
    public Output3d(int viewWidth, int viewHeight) {
        projectionMatrix.initProjectionMatrix(viewWidth, viewHeight, camera.getCameraNear(), camera.cameraFar(), camera.getFOV());

        addMesh("input.obj");
    }

    // Loop process
    public void display3d(float frameTime) {

        float elapsedTime = (float)glfwGetTime();

        // Space - move up, left shift - move down
        if (userUpdate.statusKeySpace()) camera.position.y += camera.moveSpeed * frameTime;
        if (userUpdate.statusKeyLeftShift()) camera.position.y -= camera.moveSpeed * frameTime;

        // W - move forward, S - move backwards
        Vector3f cameraForward = Vector3f.multiplyVector(camera.lookDirection, camera.moveSpeed * frameTime);
        if (userUpdate.statusKeyW()) camera.position = Vector3f.addVectors(camera.position, cameraForward);
        if (userUpdate.statusKeyS()) camera.position = Vector3f.subtractVectors(camera.position, cameraForward);

        // A - move left, D - move right
        Vector3f cameraLeft = Vector3f.multiplyVector(new Vector3f(-camera.lookDirection.z, camera.lookDirection.y, camera.lookDirection.x), camera.moveSpeed * frameTime);
        if (userUpdate.statusKeyA()) camera.position = Vector3f.addVectors(camera.position, cameraLeft);
        if (userUpdate.statusKeyD()) camera.position = Vector3f.subtractVectors(camera.position, cameraLeft);

        // Q - rotate camera to the left, E - rotate camera to the right
        if (userUpdate.statusKeyQ()) camera.fYaw += camera.rotationSpeed * frameTime;
        if (userUpdate.statusKeyE()) camera.fYaw -= camera.rotationSpeed * frameTime;

        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f cameraTarget = new Vector3f(0.0f, 0.0f, 1.0f);
        Matrix4x4 viewMatrix = Matrix4x4.rotateY(camera.fYaw);
        camera.lookDirection = viewMatrix.multiply(cameraTarget);
        cameraTarget = Vector3f.addVectors(camera.position, camera.lookDirection);

        viewMatrix = Matrix4x4.quickInverse(Matrix4x4.pointAt(camera.position, cameraTarget, cameraUp));

        rotationMatrixZ = Matrix4x4.rotateZ(0.0f * 0.5f * elapsedTime);
        rotationMatrixX = Matrix4x4.rotateX(0.0f * 0.25f * elapsedTime);

        for (Mesh mesh : meshes) {

            ArrayList<Polygon> renderQueue = new ArrayList<>();

            for (Polygon polygon : mesh.polygons) {

                Polygon renderRequest = new Polygon();

                Vector3f firstVector = new Vector3f(0.0f, 0.0f, 0.0f);
                Vector3f secondVector = new Vector3f(0.0f, 0.0f, 0.0f);
                Vector3f normal = new Vector3f(0.0f, 0.0f, 0.0f);

                for (int i = 0; i < polygon.verts.size(); i++) {

                    // Ensuring that we don't overwrite vectors of the mesh
                    Vector3f vectorTranslated = rotationMatrixZ.multiply(polygon.verts.get(i));
                    vectorTranslated = rotationMatrixX.multiply(vectorTranslated);
                    vectorTranslated.z += 600.0f;

                    switch(i) {
                        case 0:
                            firstVector.newVector(vectorTranslated);
                            break;
                        case 1:
                            secondVector.newVector(vectorTranslated);
                            break;
                        case 2:
                            normal = Vector3f.crossProduct(new Vector3f(secondVector.x - firstVector.x, secondVector.y - firstVector.y, secondVector.z - firstVector.z), new Vector3f(vectorTranslated.x - firstVector.x, vectorTranslated.y - firstVector.y, vectorTranslated.z - firstVector.z));
                            normal.normalize();
                            break;
                    }

                    renderRequest.verts.add(new Vector3f(vectorTranslated.x, vectorTranslated.y, vectorTranslated.z));
                }

                // Compare 2 vectors using dot product (taking into account the camera vector)
                if (Vector3f.dotProduct(normal, Vector3f.subtractVectors(firstVector, camera.position)) < 0) {

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

    // Add a new mesh
    private void addMesh(String file) {
        Mesh mesh = new Mesh();
        mesh.inputOBJ(file);
        meshes.add(mesh);
    }

}
