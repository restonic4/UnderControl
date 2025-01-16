package com.chaotic_loom.under_control.client.rendering;

import com.chaotic_loom.under_control.util.MathHelper;
import com.chaotic_loom.under_control.util.VertexArrayStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

@Environment(value = EnvType.CLIENT)
public class RenderShapes {
    private final List<Vector3f[]> originalVertices;
    private List<Vector3f[]> vertices;

    public RenderShapes(List<Vector3f[]> vertices) {
        this.vertices = deepCopy(vertices);
        this.originalVertices = deepCopy(vertices);
    }

    public void setVertices(List<Vector3f[]> vertices) {
        this.vertices = vertices;
    }

    public List<Vector3f[]> getVertices() {
        reset();
        return vertices;
    }

    public void reset() {
        for (int i = 0; i < vertices.size(); i++) {
            Vector3f[] originalArray = originalVertices.get(i);
            Vector3f[] currentArray = vertices.get(i);

            for (int j = 0; j < currentArray.length; j++) {
                currentArray[j].set(originalArray[j]);
            }
        }
    }

    private static Vector3f[] copyArray(Vector3f[] original) {
        Vector3f[] copy = new Vector3f[original.length];
        for (int i = 0; i < original.length; i++) {
            copy[i] = new Vector3f(original[i]);
        }
        return copy;
    }

    private static List<Vector3f[]> deepCopy(List<Vector3f[]> original) {
        List<Vector3f[]> copy = new ArrayList<>();
        for (Vector3f[] array : original) {
            copy.add(copyArray(array));
        }
        return copy;
    }

    public static final RenderShapes BEAM;
    public static final RenderShapes SPHERE;
    public static final RenderShapes QUAD;
    public static final RenderShapes CUBE;
    public static final RenderShapes CYLINDER;

    static {
        BEAM = new RenderShapes(buildBeamVertices());
        SPHERE = new RenderShapes(buildSphereVertices());
        QUAD = new RenderShapes(buildQuadVertices());
        CUBE = new RenderShapes(buildCubeVertices());
        CYLINDER = new RenderShapes(buildCylinderVertices());
    }

    private static List<Vector3f[]> buildBeamVertices() {
        List<Vector3f[]> verticesToSave = new ArrayList<>();

        Vector3f[] vertices = MathHelper.getQuadVertices();
        MathHelper.scaleVertices(vertices, 1, 1, 1);
        MathHelper.rotateVerticesX(vertices, -90);

        VertexArrayStack vertexArrayStack = new VertexArrayStack(vertices);

        vertexArrayStack.pushStack();
        MathHelper.translateVertices(vertexArrayStack.last(), 0, 0, 1);

        verticesToSave.add(vertexArrayStack.last());

        vertexArrayStack.popStack();

        vertexArrayStack.pushStack();
        MathHelper.rotateVerticesY(vertexArrayStack.last(), 90);
        MathHelper.translateVertices(vertexArrayStack.last(), 1, 0, 1);

        verticesToSave.add(vertexArrayStack.last());

        vertexArrayStack.popStack();

        vertexArrayStack.pushStack();
        MathHelper.rotateVerticesY(vertexArrayStack.last(), 180);
        MathHelper.translateVertices(vertexArrayStack.last(), 1, 0, 0);

        verticesToSave.add(vertexArrayStack.last());

        vertexArrayStack.popStack();

        vertexArrayStack.pushStack();
        MathHelper.rotateVerticesY(vertexArrayStack.last(), -90);
        MathHelper.translateVertices(vertexArrayStack.last(), 0, 0, 0);

        verticesToSave.add(vertexArrayStack.last());

        vertexArrayStack.popStack();

        return verticesToSave;
    }

    private static List<Vector3f[]> buildSphereVertices() {
        List<Vector3f[]> verticesToSave = new ArrayList<>();

        Vector3f[] vertices = MathHelper.getSphereVertices();
        verticesToSave.add(vertices);

        return verticesToSave;
    }

    private static List<Vector3f[]> buildQuadVertices() {
        List<Vector3f[]> verticesToSave = new ArrayList<>();

        Vector3f[] vertices = MathHelper.getQuadVertices();
        verticesToSave.add(vertices);

        return verticesToSave;
    }

    private static List<Vector3f[]> buildCubeVertices() {
        List<Vector3f[]> verticesToSave = new ArrayList<>();

        float size = 0.5f;

        Vector3f[] frontFace = {
                new Vector3f(-size, -size, size),
                new Vector3f(size, -size, size),
                new Vector3f(size, size, size),
                new Vector3f(-size, size, size)
        };

        Vector3f[] backFace = {
                new Vector3f(size, -size, -size),
                new Vector3f(-size, -size, -size),
                new Vector3f(-size, size, -size),
                new Vector3f(size, size, -size)
        };

        Vector3f[] leftFace = {
                new Vector3f(-size, -size, -size),
                new Vector3f(-size, -size, size),
                new Vector3f(-size, size, size),
                new Vector3f(-size, size, -size)
        };

        Vector3f[] rightFace = {
                new Vector3f(size, -size, size),
                new Vector3f(size, -size, -size),
                new Vector3f(size, size, -size),
                new Vector3f(size, size, size)
        };

        Vector3f[] topFace = {
                new Vector3f(-size, size, size),
                new Vector3f(size, size, size),
                new Vector3f(size, size, -size),
                new Vector3f(-size, size, -size)
        };

        Vector3f[] bottomFace = {
                new Vector3f(-size, -size, -size),
                new Vector3f(size, -size, -size),
                new Vector3f(size, -size, size),
                new Vector3f(-size, -size, size)
        };

        verticesToSave.add(new Vector3f[]{frontFace[0], frontFace[1], frontFace[2]});
        verticesToSave.add(new Vector3f[]{frontFace[0], frontFace[2], frontFace[3]});

        verticesToSave.add(new Vector3f[]{backFace[0], backFace[1], backFace[2]});
        verticesToSave.add(new Vector3f[]{backFace[0], backFace[2], backFace[3]});

        verticesToSave.add(new Vector3f[]{leftFace[0], leftFace[1], leftFace[2]});
        verticesToSave.add(new Vector3f[]{leftFace[0], leftFace[2], leftFace[3]});

        verticesToSave.add(new Vector3f[]{rightFace[0], rightFace[1], rightFace[2]});
        verticesToSave.add(new Vector3f[]{rightFace[0], rightFace[2], rightFace[3]});

        verticesToSave.add(new Vector3f[]{topFace[0], topFace[1], topFace[2]});
        verticesToSave.add(new Vector3f[]{topFace[0], topFace[2], topFace[3]});

        verticesToSave.add(new Vector3f[]{bottomFace[0], bottomFace[1], bottomFace[2]});
        verticesToSave.add(new Vector3f[]{bottomFace[0], bottomFace[2], bottomFace[3]});

        return verticesToSave;
    }

    private static List<Vector3f[]> buildCylinderVertices() {
        List<Vector3f[]> verticesToSave = new ArrayList<>();

        int segments = 15;
        float radius = 0.5f;
        float height = 1.0f;
        Vector3f[] baseVertices = new Vector3f[segments];
        Vector3f[] topVertices = new Vector3f[segments];

        for (int i = 0; i < segments; i++) {
            float angle = (float) (i * 2 * Math.PI / segments);
            baseVertices[i] = new Vector3f(
                    radius * (float) Math.cos(angle),
                    -height / 2,
                    radius * (float) Math.sin(angle)
            );
        }

        for (int i = 0; i < segments; i++) {
            float angle = (float) (i * 2 * Math.PI / segments);
            topVertices[i] = new Vector3f(
                    radius * (float) Math.cos(angle),
                    height / 2,
                    radius * (float) Math.sin(angle)
            );
        }

        // Round faces
        for (int i = 0; i < segments; i++) {
            int nextIndex = (i + 1) % segments;

            verticesToSave.add(new Vector3f[]{baseVertices[i], topVertices[nextIndex], baseVertices[nextIndex]});
            verticesToSave.add(new Vector3f[]{baseVertices[i], topVertices[i], topVertices[nextIndex]});
        }

        // Bottom face
        for (int i = 1; i < segments - 1; i++) {
            verticesToSave.add(new Vector3f[]{baseVertices[0], baseVertices[i], baseVertices[i + 1]});
        }

        // Top face
        for (int i = 1; i < segments - 1; i++) {
            verticesToSave.add(new Vector3f[]{topVertices[0], topVertices[i + 1], topVertices[i]});
        }

        return verticesToSave;
    }

}
