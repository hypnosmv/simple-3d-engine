package Window;

import Engine.Output3d;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import java.nio.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

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
    private double frameTime = 0.001;
    private double elapsedTime = 0.0;

    // FPS stuff
    private boolean startBenchmark = false;
    private DecimalFormat df = new DecimalFormat("#.00");
    private double clock = 0.0;
    private int frames = 0;
    private double totalFrameTime = 0.0f;
    private ArrayList<Float> frameRates = new ArrayList<>();

    // User update
    private UserUpdate userUpdate;

    // Declare 3d output
    private Output3d output3d;

    public void runWindow(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        df.setMaximumFractionDigits(2);
        frameRates.add(-1.0f);

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
            this.elapsedTime = glfwGetTime();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            output3d.display3d((float)frameTime);

            glfwSwapBuffers(window);
            glfwPollEvents();

            if (startBenchmark) {
                float fps = 1.0f / (float) frameTime;
                int index = 0;
                while (fps > frameRates.get(index)) {
                    if (index + 1 != frameRates.size()) index++;
                    else break;
                }
                frameRates.add(index, fps);

                totalFrameTime += frameTime;
                frames++;
            }

            if (elapsedTime - clock > 5.0f) {
                if (!startBenchmark) {
                    startBenchmark = true;
                }
                else {
                    int onePercentCount = Math.round((frameRates.size() - 1) * 0.01f);
                    float totalOnePercent = 0;
                    for (int i = 0; i < onePercentCount; i++) {
                        totalOnePercent += frameRates.get(i);
                    }
                    System.out.println("Average FPS: " + df.format(frames / totalFrameTime) + " 1% FPS: " + df.format(totalOnePercent / (float) onePercentCount));
                }
                clock = elapsedTime;
            }

            frameTime = glfwGetTime() - elapsedTime;
        }
    }
}
