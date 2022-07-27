package Window;

import static org.lwjgl.glfw.GLFW.*;

public class UserUpdate extends Window{

    private static long window;

    protected void updateWindow(long window) {
        this.window = window;
    }

    public boolean statusKeyUp () {
        if (glfwGetKey(this.window, GLFW_KEY_UP) == GLFW_TRUE) return Boolean.TRUE;
        else return Boolean.FALSE;
    }

    public boolean statusKeyDown () {
        if (glfwGetKey(this.window, GLFW_KEY_DOWN) == GLFW_TRUE) return true;
        else return false;
    }

    public boolean statusKeyLeft () {
        if (glfwGetKey(this.window, GLFW_KEY_LEFT) == GLFW_TRUE) return true;
        else return false;
    }

    public boolean statusKeyRight () {
        if (glfwGetKey(this.window, GLFW_KEY_RIGHT) == GLFW_TRUE) return true;
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
