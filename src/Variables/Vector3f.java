package Variables;

import Utility.QuickSort;

import java.util.ArrayList;

public class Vector3f {

    // Coordinates in 3d space
    public float x;
    public float y;
    public float z;
    public float w = 1.0f;

    // Constructor
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Normalize in non-static context
    public void normalize () {
        float length = 1 / (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);

        this.x *= length;
        this.y *= length;
        this.z *= length;
    }

    // Normalize in static context
    public static Vector3f staticNormalize (Vector3f vector) {
        float length = 1 / (float)Math.sqrt(vector.x * vector.x + vector.y * vector.y + vector.z * vector.z);

        return new Vector3f(vector.x * length, vector.y * length, vector.z * length);
    }

    public void newVector(Vector3f vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public static Vector3f crossProduct(Vector3f vector1, Vector3f vector2) {
        return new Vector3f(vector1.y * vector2.z - vector1.z * vector2.y, vector1.z * vector2.x - vector1.x * vector2.z, vector1.x * vector2.y - vector1.y * vector2.x);
    }

    public static float dotProduct(Vector3f vector1, Vector3f vector2) {
        return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z;
    }

    public static Vector3f addVectors(Vector3f vector1, Vector3f vector2) {
        return new Vector3f(vector1.x + vector2.x, vector1.y + vector2.y, vector1.z + vector2.z);
    }

    public static Vector3f subtractVectors(Vector3f vector1, Vector3f vector2) {
        return new Vector3f(vector1.x - vector2.x, vector1.y - vector2.y, vector1.z - vector2.z);
    }

    public static Vector3f multiplyVector(Vector3f vector, float k) {
        return new Vector3f(vector.x * k, vector.y * k, vector.z * k);
    }

    public void scaleVector(float k)
    {
        this.x *= k;
        this.y *= k;
        this.z *= k;
    }

    // Return a vector on a line which intersects the plane
    private static Vector3f intersectPlane(Vector3f plane_p, Vector3f plane_n, Vector3f lineStart, Vector3f lineEnd) {

        // Normalize plane normal
        plane_n = Vector3f.staticNormalize(plane_n);

        // Line intersection algorithm
        float plane_d = -Vector3f.dotProduct(plane_n, plane_p);
        float ad = Vector3f.dotProduct(lineStart, plane_n);
        float bd = Vector3f.dotProduct(lineEnd, plane_n);
        float t = (-plane_d - ad) / (bd - ad);
        Vector3f lineStartToEnd = Vector3f.subtractVectors(lineEnd, lineStart);
        Vector3f lineToIntersect = Vector3f.multiplyVector(lineStartToEnd, t);
        return Vector3f.addVectors(lineStart, lineToIntersect);

    }

    // Simple method that returns distance between a plane and a point
    private static float calculateDistance (Vector3f plane_p, Vector3f plane_n, Vector3f vector) {
        return plane_n.x * vector.x + plane_n.y * vector.y + plane_n.z * vector.z - Vector3f.dotProduct(plane_n, plane_p);
    }

    // Return 0, 1, 2 polygons that represent the polygon intersecting the camera's plane
    public static Polygon[] clipAgainstPlane(Vector3f plane_p, Vector3f plane_n, Polygon polygon) {

        // Output array
        Polygon[] polygons = new Polygon[0];

        // Normalize plane normal
        plane_n = Vector3f.staticNormalize(plane_n);

        // Find distances between the plane and vertexes
        float[] distances = new float[3];
        for (int i = 0; i < 3; i++) {
            distances[i] = Vector3f.calculateDistance(plane_p, plane_n, polygon.verts[i]);
        }

        // Array lists that store vectors of inside and outside points accordingly
        ArrayList<Integer> insidePointsIndexes = new ArrayList<>();
        ArrayList<Integer> outsidePointsIndexes = new ArrayList<>();

        // Find indexes of points that are inside or outside
        for (int i = 0; i < 3; i++) {
            if (distances[i] >= 0.0f) {
                insidePointsIndexes.add(i);
            }
            else {
                outsidePointsIndexes.add(i);
            }
        }

        // Sort indexes in order to find two sides of a polygon that actually intersect
        insidePointsIndexes = QuickSort.quickSortIndexes(insidePointsIndexes);
        outsidePointsIndexes = QuickSort.quickSortIndexes(outsidePointsIndexes);

        // Determine how many points are inside
        int insidePointsCount = insidePointsIndexes.size();

        // Based on insidePointCount we determine the number of polygons returned
        switch (insidePointsCount) {
            case 3:
                polygons = new Polygon[1];
                polygons[0] = polygon;
                break;
            case 1:
                // We just clip the outside vertices,
                // no need to return an additional polygon
                polygons = new Polygon[1];

                // Trim both sides which contain the inside point
                polygon.verts[outsidePointsIndexes.get(0)] = Vector3f.intersectPlane(plane_p, plane_n, polygon.verts[insidePointsIndexes.get(0)], polygon.verts[outsidePointsIndexes.get(0)]);
                polygon.verts[outsidePointsIndexes.get(1)] = Vector3f.intersectPlane(plane_p, plane_n, polygon.verts[insidePointsIndexes.get(0)], polygon.verts[outsidePointsIndexes.get(1)]);

                // Add the polygon to the return array
                polygons[0] = polygon;
                break;
            case 2:
                // There is only one vertex outside, so we need to fill the gap
                // with the help of an additional polygon
                polygons = new Polygon[2];
                Polygon additionalPolygon = new Polygon();

                // One of the additional polygon's vertices is just one of the inside points,
                // the other we have to create by clipping one of the outside points
                additionalPolygon.verts[0] = polygon.verts[insidePointsIndexes.get(1)];
                additionalPolygon.verts[2] = Vector3f.intersectPlane(plane_p, plane_n, polygon.verts[insidePointsIndexes.get(1)], polygon.verts[outsidePointsIndexes.get(0)]);

                // Now, after using the outside point to create
                // additionalPolygon.verts[2], we can clip the outside point
                polygon.verts[outsidePointsIndexes.get(0)] = Vector3f.intersectPlane(plane_p, plane_n, polygon.verts[insidePointsIndexes.get(0)], polygon.verts[outsidePointsIndexes.get(0)]);

                // The clipped outside point is common to both polygons
                additionalPolygon.verts[1] = polygon.verts[outsidePointsIndexes.get(0)];

                // Add the polygons to the return array
                polygons[0] = polygon;
                polygons[1] = additionalPolygon;

                break;
        }

        // Return the array
        return polygons;
    }

}
