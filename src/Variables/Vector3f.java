package Variables;

import Utility.QuickSort;

import java.util.ArrayList;

public class Vector3f {

    // Coordinates in 3d space
    public float x;
    public float y;
    public float z;

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

    public static Polygon clipAgainstPlane(Vector3f plane_p, Vector3f plane_n, Polygon polygon) {

        // Normalize plane normal
        plane_n = Vector3f.staticNormalize(plane_n);

        // Find distances
        ArrayList<Float> distances = new ArrayList<>();
        for (int i = 0; i < polygon.verts.size(); i++) {
            distances.add(Vector3f.calculateDistance(plane_p, plane_n, polygon.verts.get(i)));
        }

        // Array lists that store vectors of inside and outside points accordingly
        ArrayList<Integer> insidePointsIndexes = new ArrayList<>();
        ArrayList<Integer> outsidePointsIndexes = new ArrayList<>();

        // Find indexes of points that are inside or outside
        for (int i = 0; i < distances.size(); i++) {
            if (distances.get(i) >= 0.0f) {
                insidePointsIndexes.add(i);
            }
            else {
                outsidePointsIndexes.add(i);
            }
        }

        // Sort indexes in order to find two sides of a polygon that actually intersect
        insidePointsIndexes = QuickSort.quickSortIndexes(insidePointsIndexes);
        outsidePointsIndexes = QuickSort.quickSortIndexes(outsidePointsIndexes);

        // Determine how many points are inside or outside
        int insidePointsCount = insidePointsIndexes.size();
        int outsidePointsCount = outsidePointsIndexes.size();

        // If all points are inside, just return the input polygon
        if (outsidePointsCount == 0) {
            return polygon;
        }

        // If all points are outside, just return a polygon
        // without any vectors and no vectors will be processed
        if (insidePointsCount == 0) {
            return new Polygon();
        }

        // If only one point is outside, return a new polygon
        // that contains an additional point
        if (outsidePointsCount == 1) {

            // Initialization
            Polygon newPolygon = new Polygon();

            // Add all inside points
            for (int i = 0; i < insidePointsCount; i++) {
                newPolygon.verts.add(polygon.verts.get(insidePointsIndexes.get(i)));
            }

            // Add two points that were created by
            // clipping the outside one
            newPolygon.verts.add(Vector3f.intersectPlane(plane_p, plane_n, polygon.verts.get(insidePointsIndexes.get(insidePointsCount - 1)), polygon.verts.get(outsidePointsIndexes.get(0))));
            newPolygon.verts.add(Vector3f.intersectPlane(plane_p, plane_n, polygon.verts.get(insidePointsIndexes.get(0)), polygon.verts.get(outsidePointsIndexes.get(outsidePointsCount - 1))));

            return newPolygon;
        }

        // If some points are inside and some outside, we need to
        // properly trim the exact two sides that intersect
        if (insidePointsCount != polygon.verts.size()) {

            // Clip the outside point with index 0
            // and clip the one with index (size-1)
            polygon.verts.set(outsidePointsIndexes.get(0), Vector3f.intersectPlane(plane_p, plane_n, polygon.verts.get(insidePointsIndexes.get(insidePointsCount - 1)), polygon.verts.get(outsidePointsIndexes.get(0))));
            polygon.verts.set(outsidePointsIndexes.get(outsidePointsCount - 1), Vector3f.intersectPlane(plane_p, plane_n, polygon.verts.get(insidePointsIndexes.get(0)), polygon.verts.get(outsidePointsIndexes.get(outsidePointsCount - 1))));

            // Here should be a loop that removes (.remove()) the not-trimmed vectors outside (3+ vector polygon case),
            // but OpenGL doesn't draw outside the window, so it's all fine for the performance

        }

        // If something goes wrong, just return the original one
        return polygon;
    }

}
