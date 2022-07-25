package Engine;

import java.util.ArrayList;
import Variables.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.*;

public class Output3d {

    // 3d objects
    private ArrayList<Mesh> meshes = new ArrayList<>();

    // Projection matrix
    private Matrix4x4 projectionMatrix;

    // Rotation matrices
    Matrix4x4 rotationMatrixZ = new Matrix4x4();
    Matrix4x4 rotationMatrixX = new Matrix4x4();

    // Camera
    Camera camera = new Camera();

    // Constructor
    public Output3d(int viewWidth, int viewHeight) {
        projectionMatrix = camera.initProjectionMatrix(viewWidth, viewHeight);

        addMesh("input.obj");
    }

    // Loop process
    public void display3d() {

        float frameTime = (float)glfwGetTime();

        rotationMatrixZ.m[0][0] = (float)Math.cos(frameTime);
        rotationMatrixZ.m[0][1] = (float)Math.sin(frameTime);
        rotationMatrixZ.m[1][0] = (float)-Math.sin(frameTime);
        rotationMatrixZ.m[1][1] = (float)Math.cos(frameTime);
        rotationMatrixZ.m[2][2] = 1.0f;
        rotationMatrixZ.m[3][3] = 1.0f;

        rotationMatrixX.m[0][0] = 1.0f;
        rotationMatrixX.m[1][1] = (float)Math.cos(frameTime * 0.5f);
        rotationMatrixX.m[1][2] = (float)Math.sin(frameTime * 0.5f);
        rotationMatrixX.m[2][1] = (float)-Math.sin(frameTime * 0.5f);
        rotationMatrixX.m[2][2] = (float)Math.cos(frameTime * 0.5f);
        rotationMatrixX.m[3][3] = 1.0f;

        for (Mesh mesh : meshes) {
            for (Face face : mesh.faces) {

                Vector3f[] renderQueue = new Vector3f[face.verts.size()];

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
                            firstVector.x = vectorTranslated.x;
                            firstVector.y = vectorTranslated.y;
                            firstVector.z = vectorTranslated.z;
                            break;
                        case 1:
                            secondVector.x = vectorTranslated.x;
                            secondVector.y = vectorTranslated.y;
                            secondVector.z = vectorTranslated.z;
                            break;
                        case 2:
                            Vector3f line1 = new Vector3f(secondVector.x - firstVector.x, secondVector.y - firstVector.y, secondVector.z - firstVector.z);
                            Vector3f line2 = new Vector3f(vectorTranslated.x - firstVector.x, vectorTranslated.y - firstVector.y, vectorTranslated.z - firstVector.z);
                            normal.x = line1.y * line2.z - line1.z * line2.y;
                            normal.y = line1.z * line2.x - line1.x * line2.z;
                            normal.z = line1.x * line2.y - line1.y * line2.x;

                            normal.normalize();
                            break;
                    }

                    renderQueue[i] = new Vector3f(vectorTranslated.x, vectorTranslated.y, vectorTranslated.z);
                }

                // Draw the face if it is visible (using the dot product)
                if (normal.x * (firstVector.x - camera.position.x) +  normal.y * (firstVector.y - camera.position.y) + normal.z * (firstVector.z - camera.position.z) < 0) {

                    glBegin(GL_POLYGON);
                    for (Vector3f vector : renderQueue) {

                        Vector3f lightDirection = new Vector3f(0.0f, 0.0f, -1.0f);
                        lightDirection.normalize();
                        float lightColor = normal.x * lightDirection.x + normal.y * lightDirection.y + normal.z * lightDirection.z;

                        vector = projectionMatrix.multiply(vector);

                        glColor3f(lightColor, lightColor, lightColor);
                        glVertex2f(vector.x, vector.y);

                    }
                    glEnd();

                }

            }
        }
    }

    // Adding a new mesh
    private void addMesh(String file) {
        Mesh mesh = new Mesh();
        mesh.inputOBJ(file);
        meshes.add(mesh);
    }
}
