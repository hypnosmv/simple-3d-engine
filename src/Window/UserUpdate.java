package Window;

import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class UserUpdate extends Window{

    private static long window;
    private double currentCursorPosX = 0.0;
    private double currentCursorPosY = 0.0;

    protected void updateWindow(long window) {
        this.window = window;
    }

    public float getCursorPosX() {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(this.window, null, posX);
        double output = posX.get(0) - currentCursorPosX;
        currentCursorPosX = posX.get(0);
        return (float)output;
    }

    public float getCursorPosY() {
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(this.window, posY, null);
        double output = currentCursorPosY - posY.get(0);
        currentCursorPosY = posY.get(0);
        return (float)output;
    }


    public boolean statusKeySpace () {
        if (glfwGetKey(this.window, GLFW_KEY_SPACE) == GLFW_TRUE) return Boolean.TRUE;
        else return Boolean.FALSE;
    }

    public boolean statusKeyLeftShift () {
        if (glfwGetKey(this.window, GLFW_KEY_LEFT_SHIFT) == GLFW_TRUE) return true;
        else return false;
    }

    public boolean statusKeyW () {
        if (glfwGetKey(this.window, GLFW_KEY_W) == GLFW_TRUE) return true;
        else return false;
    }

    public boolean statusKeyS () {
        if (glfwGetKey(this.window, GLFW_KEY_S) == GLFW_TRUE) return true;
        else return false;
    }

    public boolean statusKeyA () {
        if (glfwGetKey(this.window, GLFW_KEY_A) == GLFW_TRUE) return true;
        else return false;
    }

    public boolean statusKeyD () {
        if (glfwGetKey(this.window, GLFW_KEY_D) == GLFW_TRUE) return true;
        else return false;
    }
}
