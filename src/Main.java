import ProjectionUtil.*;
import UI.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    // Window info
    private long window;
    private int width = 800;
    private int height = 600;
    private String title = "";

    // Basic mesh cube
    private mesh meshCube;

    // Projection matrix
    private mat4x4 matProj;

    public void runWindow(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;

        // Start and loop
        init3dCalc();
        initWindow();
        loopWindow();

        // Terminate
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init3dCalc() {

        // Mesh cube initialization
        meshCube = new mesh();
        meshCube.inputOBJ("input.obj");

        // Projection Matrix
        matProj = new mat4x4();
        float fNear = 0.1f;
        float fFar = 1000.0f;
        float fFov = 90.0f;
        float fAspectRatio = (float)height / (float)width;
        float fFovRad = 1.0f / (float)Math.tan(fFov * 0.5f / 180.0f * Math.PI);

        matProj.m[0][0] = fAspectRatio;
        matProj.m[1][1] = fFovRad;
        matProj.m[2][2] = fFar / (fFar - fNear);
        matProj.m[3][2] = (-fFar * fNear) / (fFar - fNear);
        matProj.m[2][3] = 1.0f;
        matProj.m[3][3] = 0.0f;

    }

    private void initWindow() {

        GLFWErrorCallback.createPrint(System.err).set();
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
        });

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void loopWindow() {
        GL.createCapabilities();

        // Rotation matrices
        mat4x4 matRotZ = new mat4x4();
        mat4x4 matRotX = new mat4x4();

        float dist = meshCube.getMax() * 2;
        float distSpeed = meshCube.getMax() * 0.004f;

        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            float frameTime = (float)glfwGetTime();

            if(glfwGetKey(window, GLFW_KEY_UP) == GL_TRUE) {
                dist -= frameTime * distSpeed;
            }
            if(glfwGetKey(window, GLFW_KEY_DOWN) == GL_TRUE) {
                dist += frameTime * distSpeed;
            }

            Info.display();

            matRotZ.m[0][0] = (float)Math.cos(frameTime);
            matRotZ.m[0][1] = (float)Math.sin(frameTime);
            matRotZ.m[1][0] = (float)-Math.sin(frameTime);
            matRotZ.m[1][1] = (float)Math.cos(frameTime);
            matRotZ.m[2][2] = 1.0f;
            matRotZ.m[3][3] = 1.0f;

            matRotX.m[0][0] = 1.0f;
            matRotX.m[1][1] = (float)Math.cos(frameTime * 0.5f);
            matRotX.m[1][2] = (float)Math.sin(frameTime * 0.5f);
            matRotX.m[2][1] = (float)-Math.sin(frameTime * 0.5f);
            matRotX.m[2][2] = (float)Math.cos(frameTime * 0.5f);
            matRotX.m[3][3] = 1.0f;

            for (face f : meshCube.faces) {
                glBegin(GL_LINE_LOOP);
                for (vec3f vec : f.verts) {
                    vec3f vecTrans = matRotZ.multiplyMatVec(vec);
                    vecTrans = matRotX.multiplyMatVec(vecTrans);
                    vecTrans.z += dist;
                    vecTrans = matProj.multiplyMatVec(vecTrans);

                    glVertex2f(vecTrans.x, vecTrans.y);
                }
                glEnd();
            }

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {

        new Main().runWindow(1280, 720, "simple 3d engine");
    }

}
