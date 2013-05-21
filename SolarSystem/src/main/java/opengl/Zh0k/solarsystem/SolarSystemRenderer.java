package opengl.Zh0k.solarsystem;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;
import java.lang.Math;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class SolarSystemRenderer implements GLSurfaceView.Renderer {

    private boolean mTranslucentBackground;
    private Planet mPlanet;
    private float mTransY;
    private float mAngle;

    public final static int SS_SUNLIGHT= GL10.GL_LIGHT0;

    public SolarSystemRenderer(boolean useTranslucentBackground)
    {
        mTranslucentBackground = useTranslucentBackground;
        mPlanet = new Planet(100,100,1.0f, 1.0f);
    }

    public void onDrawFrame(GL10 gl)
    {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT| GL10.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(0.2f,0.2f,0.2f,1.0f);
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

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        //gl.glDepthMask(false);
        //initGeometry(gl);
        initLighting(gl);
    }

    private void initLighting(GL10 gl)
    {
        float[] diffuse = {1.0f, 1.0f, 0.0f, 1.0f}; //1
        float[] pos = {10.0f, 0.0f, 3.0f, 1.0f}; //2

        float[] white = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] red={1.0f, 0.0f, 0.0f, 1.0f};
        float[] green={0.0f,1.0f,0.0f,1.0f};
        float[] blue={0.0f, 0.0f, 1.0f, 1.0f};
        float[] cyan={0.0f, 1.0f, 1.0f, 1.0f};
        float[] yellow={1.0f, 1.0f, 0.0f, 1.0f};
        float[] magenta={1.0f, 0.0f, 1.0f, 1.0f};
        float[] halfcyan={0.0f, 0.5f, 0.5f, 1.0f};

        float[] materiallight = {0.5f,0.5f,0.5f,1f};

        gl.glLightfv(SS_SUNLIGHT, GL10.GL_POSITION, makeFloatBuffer(pos)); //3

        //LUZ DIFUSA
        gl.glLightfv(SS_SUNLIGHT, GL10.GL_DIFFUSE, makeFloatBuffer(green)); //4

        //LUZ ESPECULAR
        gl.glLightfv(SS_SUNLIGHT,GL10.GL_SPECULAR, makeFloatBuffer(red));

        //LUZ AMBIENTE
        gl.glLightfv(SS_SUNLIGHT, GL10.GL_AMBIENT, makeFloatBuffer(blue));

        //ATENUACIÓN
        gl.glLightf(SS_SUNLIGHT, GL10.GL_LINEAR_ATTENUATION, .025f);

        //LUZ DIFUSA
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(green)); //1

        //LUZ ESPECULAR
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(red));
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK,GL10.GL_SHININESS, 5);

        //LUZ PROPIA
        //gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_EMISSION, makeFloatBuffer(materiallight));

        gl.glShadeModel(GL10.GL_SMOOTH); //5
        gl.glEnable(GL10.GL_LIGHTING); //6
        gl.glEnable(SS_SUNLIGHT); //7

        gl.glLoadIdentity(); //2
    }

    protected static FloatBuffer makeFloatBuffer(float[] arr)
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }
}
