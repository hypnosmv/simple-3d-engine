package Window;

import static org.lwjgl.glfw.GLFW.*;

public class UserUpdate extends Window{

    private static long window;

    protected void updateWindow(long window) {
        this.window = window;
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

    public boolean statusKeyQ () {
        if (glfwGetKey(this.window, GLFW_KEY_Q) == GLFW_TRUE) return true;
        else return false;
    }

    public boolean statusKeyE () {
        if (glfwGetKey(this.window, GLFW_KEY_E) == GLFW_TRUE) return true;
        else return false;
    }

    public boolean statusKeyR () {
        if (glfwGetKey(this.window, GLFW_KEY_R) == GLFW_TRUE) return true;
        else return false;
    }

    public boolean statusKeyF () {
        if (glfwGetKey(this.window, GLFW_KEY_F) == GLFW_TRUE) return true;
        else return false;
    }

}
