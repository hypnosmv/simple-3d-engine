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

        for (Mesh mesh : meshes) {
            for (Face face : mesh.faces) {
                // GL_LINE_LOOP ensures that the last vertex specified is connected to first vertex
                glBegin(GL_LINE_LOOP);
                for (Vector3f vector : face.verts) {

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

                    // Ensuring that we don't overwrite vectors of the mesh
                    Vector3f vectorTranslated = rotationMatrixZ.multiply(vector);
                    vectorTranslated = rotationMatrixX.multiply(vectorTranslated);
                    vectorTranslated.z += 600.0f;
                    vectorTranslated = projectionMatrix.multiply(vectorTranslated);

                    // OpenGL vertex
                    glVertex2f(vectorTranslated.x, vectorTranslated.y);

                }
                glEnd();
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
