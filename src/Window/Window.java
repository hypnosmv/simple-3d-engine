package Window;

import Engine.Output3d;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import java.nio.*;
import java.text.DecimalFormat;

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
    private float frameTime = 0.00001f;
    private float elapsedTime = 0.0f;

    // FPS stuff
    private DecimalFormat df = new DecimalFormat("#.00");
    private float titleClock = 0.0f;
    private long frames = 1;
    private long secondsTimer = 0;

    // User update
    private UserUpdate userUpdate;

    // Declare 3d output
    private Output3d output3d;

    public void runWindow(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        df.setMaximumFractionDigits(2);

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

        if (glfwRawMouseMotionSupported()) {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
        }

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
        //glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void loopWindow() {
        GL.createCapabilities();

        while ( !glfwWindowShouldClose(window) ) {
            this.elapsedTime = (float)glfwGetTime();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            output3d.display3d(frameTime);

            frames++;
            String fpsInfo = "FPS: " + df.format(1/frameTime) + " Average FPS: " + df.format(1 / (elapsedTime / (float)frames));
            if (elapsedTime - titleClock > 0.2f) {
                titleClock = elapsedTime;
                glfwSetWindowTitle(window, title + "  " + fpsInfo);
            }
            if ((int)elapsedTime > secondsTimer + 5) {
                secondsTimer = (int)elapsedTime;
                System.out.println(fpsInfo);
            }

            glfwSwapBuffers(window);
            glfwPollEvents();

            frameTime = (float)glfwGetTime() - elapsedTime;
        }
    }
}
