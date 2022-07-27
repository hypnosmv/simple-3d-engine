package Window;

import Engine.Output3d;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import java.nio.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    // Window info
    private long window;
    private int width;
    private int height;
    private String title = "";

    // Calculate frame timings (essential for proper movement and camera rotation)
    private float prevFrameTime = 0.0f;
    private float frameTime = 0.0f;

    // User update
    private UserUpdate userUpdate;

    // Declare 3d output
    private Output3d output3d;

    public void runWindow(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;

        userUpdate = new UserUpdate();
        output3d = new Output3d(this.width, this.height);

        initWindow();
        userUpdate.updateWindow(window);
        loopWindow();

        // Terminate
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
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

            // Center the window
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

        while ( !glfwWindowShouldClose(window) ) {
            this.frameTime = (float)glfwGetTime();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            output3d.display3d(this.prevFrameTime);

            glfwSwapBuffers(window);
            glfwPollEvents();
            this.prevFrameTime = (float)glfwGetTime() - this.frameTime;
        }
    }
}
