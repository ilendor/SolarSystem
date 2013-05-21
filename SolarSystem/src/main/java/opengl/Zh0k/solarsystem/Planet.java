package opengl.Zh0k.solarsystem;

import java.util.*;
import java.nio.*;
import javax.microedition.khronos.opengles.GL10;
public class Planet
{
    FloatBuffer m_VertexData;
    FloatBuffer m_NormalData;
    FloatBuffer m_ColorData;
    float m_Scale;
    float m_Squash;
    float m_Radius;
    int m_Stacks, m_Slices;

    public Planet(int stacks, int slices, float radius, float squash)
    {
        this.m_Stacks = stacks;
        this.m_Slices = slices;
        this.m_Radius = radius;
        this.m_Squash=squash;
        init(m_Stacks,m_Slices,radius,squash,"dummy");
    }

    private void init(int stacks,int slices, float radius, float squash, String textureFile)
    {
        float[] vertexData;
        float[] colorData;
        float[] normalData;
        float colorIncrement=0f;
        float blue=0f;
        float red=1.0f;
        int numVertices=0;
        int vIndex=0; //Vertex index
        int cIndex=0; //Color index
        int nIndex =0;  //Normal index
        m_Scale=radius;
        m_Squash=squash;
        colorIncrement=1.0f/(float)stacks;
        m_Stacks = stacks;
        m_Slices = slices;
//Vertices
        vertexData = new float[ 3*((m_Slices*2+2) * m_Stacks)];
//Color data
        colorData = new float[ (4*(m_Slices*2+2) * m_Stacks)];
// Normalize data
        normalData = new float [ (3*(m_Slices*2+2)* m_Stacks)]; //1
        int phiIdx, thetaIdx;
//Latitude
        for(phiIdx=0; phiIdx < m_Stacks; phiIdx++)
        {
//Starts at -90 degrees (-1.57 radians) and goes up to +90 degrees (or +1.57 radians).
//The first circle
            float phi0 = (float)Math.PI * ((float)(phiIdx+0) * (1.0f/(float)
                    (m_Stacks)) - 0.5f);
//The next, or second one.
            float phi1 = (float)Math.PI * ((float)(phiIdx+1) * (1.0f/(float)
                    (m_Stacks)) - 0.5f);
            float cosPhi0 = (float)Math.cos(phi0);
            float sinPhi0 = (float)Math.sin(phi0);
            float cosPhi1 = (float)Math.cos(phi1);
            float sinPhi1 = (float)Math.sin(phi1);
            float cosTheta, sinTheta;
//Longitude
            for(thetaIdx=0; thetaIdx < m_Slices; thetaIdx++)
            {
//Increment along the longitude circle each "slice."
                float theta = (float) (2.0f*(float)Math.PI * ((float)thetaIdx) *
                        (1.0/(float)(m_Slices-1)));
                cosTheta = (float)Math.cos(theta);
                sinTheta = (float)Math.sin(theta);
//We're generating a vertical pair of points, such
//as the first point of stack 0 and the first point of stack 1
//above it. This is how TRIANGLE_STRIPS work,
//taking a set of 4 vertices and essentially drawing two triangles
//at a time. The first is v0-v1-v2 and the next is v2-v1-v3, etc.
//Get x-y-z for the first vertex of stack.
                vertexData[vIndex+0] = m_Scale*cosPhi0*cosTheta;
                vertexData[vIndex+1] = m_Scale*(sinPhi0*m_Squash);
                vertexData[vIndex+2] = m_Scale*(cosPhi0*sinTheta);
                vertexData[vIndex+3] = m_Scale*cosPhi1*cosTheta;
                vertexData[vIndex+4] = m_Scale*(sinPhi1*m_Squash);
                vertexData[vIndex+5] = m_Scale*(cosPhi1*sinTheta);
                colorData[cIndex+0] = (float)red;
                colorData[cIndex+1] = (float)0f;
                colorData[cIndex+2] = (float)blue;
                colorData[cIndex+4] = (float)red;
                colorData[cIndex+5] = (float)0f;
                colorData[cIndex+6] = (float)blue;
                colorData[cIndex+3] = (float)1.0;
                colorData[cIndex+7] = (float)1.0;
// Normalize data pointers for lighting.
                //Normal pointers for lighting
                normalData[nIndex+0] = (float)(cosPhi0 * cosTheta);
                normalData[nIndex+2] = cosPhi0 * sinTheta;
                normalData[nIndex+1] = sinPhi0;
                 //Get x-y-z for the first vertex of stack N.
                normalData[nIndex+3] = cosPhi1 * cosTheta;
                normalData[nIndex+5] = cosPhi1 * sinTheta;
                normalData[nIndex+4] = sinPhi1;

                cIndex+=2*4;
                vIndex+=2*3;  nIndex+=2*3;
            }
//Blue+=colorIncrement;
            red-=colorIncrement;
//Create a degenerate triangle to connect stacks and maintain winding order.
            vertexData[vIndex+0] = vertexData[vIndex+3] = vertexData[vIndex-3];
            vertexData[vIndex+1] = vertexData[vIndex+4] = vertexData[vIndex-2];
            vertexData[vIndex+2] = vertexData[vIndex+5] = vertexData[vIndex-1];
        }
        m_VertexData = makeFloatBuffer(vertexData);
        m_ColorData = makeFloatBuffer(colorData);
        m_NormalData = makeFloatBuffer(normalData);
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

    public void draw(GL10 gl)
    {
        gl.glMatrixMode(GL10.GL_MODELVIEW);

        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);

        gl.glNormalPointer(GL10.GL_FLOAT, 0, m_NormalData);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_VertexData);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glColorPointer(4, GL10.GL_FLOAT, 0, m_ColorData);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, (m_Slices+1)*2*(m_Stacks-1)+2);
    }
}