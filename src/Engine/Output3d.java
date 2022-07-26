package Engine;

import java.util.ArrayList;

import Utility.QuickSort;
import Variables.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.*;

public class Output3d {

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
    public void display3d() {

        float frameTime = (float)glfwGetTime();

        rotationMatrixZ.initRotationMatrixZ(frameTime);
        rotationMatrixX.initRotationMatrixX(frameTime);

        for (Mesh mesh : meshes) {

            ArrayList<Face> renderQueue = new ArrayList<>();

            for (Face face : mesh.faces) {

                Face renderRequest = new Face();

                Vector3f firstVector = new Vector3f(0.0f, 0.0f, 0.0f);
                Vector3f secondVector = new Vector3f(0.0f, 0.0f, 0.0f);
                Vector3f normal = new Vector3f(0.0f, 0.0f, 0.0f);

                for (int i = 0; i < face.verts.size(); i++) {

                    // Ensuring that we don't overwrite vectors of the mesh
                    Vector3f vectorTranslated = rotationMatrixZ.multiply(face.verts.get(i));
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
                            normal.crossProduct(new Vector3f(secondVector.x - firstVector.x, secondVector.y - firstVector.y, secondVector.z - firstVector.z), new Vector3f(vectorTranslated.x - firstVector.x, vectorTranslated.y - firstVector.y, vectorTranslated.z - firstVector.z));
                            normal.normalize();
                            break;
                    }

                    renderRequest.verts.add(new Vector3f(vectorTranslated.x, vectorTranslated.y, vectorTranslated.z));
                }

                if (normal.x * (firstVector.x - camera.position.x) +  normal.y * (firstVector.y - camera.position.y) + normal.z * (firstVector.z - camera.position.z) < 0) {

                    Vector3f lightDirection = new Vector3f(0.0f, 0.0f, -1.0f);
                    lightDirection.normalize();

                    renderRequest.color = normal.x * lightDirection.x + normal.y * lightDirection.y + normal.z * lightDirection.z;
                    renderRequest.calculateZDepth();

                    renderQueue.add(renderRequest);

                }

            }

            // Sort using z-depth (painter's algorithm)
            renderQueue = QuickSort.quickSortFace(renderQueue);

            // Draw faces of the mesh
            for (Face face : renderQueue) {
                glBegin(GL_POLYGON);
                for (Vector3f vector : face.verts) {

                    // Project onto the display using the projection matrix
                    vector = projectionMatrix.multiply(vector);

                    // Color and display cords
                    glColor3f(face.color, face.color, face.color);
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
