package opengl.Zh0k.solarsystem;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;
import java.lang.Math;

public class SolarSystemRenderer implements GLSurfaceView.Renderer {

    private boolean mTranslucentBackground;
    private Planet mPlanet;
    private float mTransY;
    private float mAngle;

    public SolarSystemRenderer(boolean useTranslucentBackground)
    {
        mTranslucentBackground = useTranslucentBackground;
        mPlanet = new Planet(100,100,1.0f, 1.0f);
    }

    public void onDrawFrame(GL10 gl)
    {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT| GL10.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(0.0f,0.0f,0.0f,1.0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        //gl.glTranslatef(0.0f,(float)Math.sin(mTransY), -4.0f);
        gl.glTranslatef(0.0f, 0.0f, -4.0f);
        gl.glRotatef(mAngle, 1, 0, 0);
        gl.glRotatef(mAngle, 0, 1, 0);
        mPlanet.draw(gl);
        mTransY+=.075f;
        mAngle+=.94;
    }

    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        gl.glViewport(0, 0, width, height);
        float aspectRatio;
        float zNear =.1f;
        float zFar =1000;
        float fieldOfView = 30.0f/57.3f; //30º converted to radians
        float size;

        gl.glEnable(GL10.GL_NORMALIZE);
        aspectRatio=(float)width/(float)height;

        gl.glMatrixMode(GL10.GL_PROJECTION);

        size = zNear * (float)(Math.tan((double)(fieldOfView/2.0f)));
        gl.glFrustumf(-size, size, -size /aspectRatio, size /aspectRatio, zNear, zFar);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

        if (mTranslucentBackground)
        {
            gl.glClearColor(0,0,0,0);
        }
        else
        {
            gl.glClearColor(1,1,1,1);
        }

        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
    }
}
