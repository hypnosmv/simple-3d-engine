package UI;

import static org.lwjgl.opengl.GL11.*;

public class Info {

    public static void display() {
        glBegin(GL_LINE_LOOP);
        glVertex2f(0.87f, 0.97f);
        glVertex2f(0.98f, 0.97f);
        glVertex2f(0.98f, 0.77f);
        glVertex2f(0.87f, 0.77f);
        glEnd();

        glBegin(GL_LINE_LOOP);
        glVertex2f(0.87f, 0.72f);
        glVertex2f(0.98f, 0.72f);
        glVertex2f(0.98f, 0.52f);
        glVertex2f(0.87f, 0.52f);
        glEnd();

        glBegin(GL_LINE_LOOP);
        glVertex2f(0.925f, 0.95f);
        glVertex2f(0.96f, 0.88f);
        glVertex2f(0.94f, 0.88f);
        glVertex2f(0.94f, 0.79f);
        glVertex2f(0.91f, 0.79f);
        glVertex2f(0.91f, 0.88f);
        glVertex2f(0.89f, 0.88f);
        glEnd();

        glBegin(GL_LINE_LOOP);
        glVertex2f(0.925f, 0.54f);
        glVertex2f(0.96f, 0.61f);
        glVertex2f(0.94f, 0.61f);
        glVertex2f(0.94f, 0.70f);
        glVertex2f(0.91f, 0.70f);
        glVertex2f(0.91f, 0.61f);
        glVertex2f(0.89f, 0.61f);
        glEnd();
    }
}
