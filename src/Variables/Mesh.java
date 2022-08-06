package Variables;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Mesh {
    public ArrayList<Polygon> polygons = new ArrayList<>();

    public void inputOBJ(String file) {

        try {
            // File readers
            FileReader fileReader = new FileReader(file);
            BufferedReader buffReader = new BufferedReader(fileReader);

            // Read vectors
            ArrayList<Vector3f> vecs = new ArrayList<>();

            while (buffReader.ready()) {

                String line = buffReader.readLine();

                if (line.length() > 0) {

                    if (line.charAt(0) == 'v' && line.charAt(1) == ' ') {

                        Vector3f vec = new Vector3f(0.0f, 0.0f, 0.0f);
                        StringBuilder numberString = new StringBuilder();
                        float numberFloat;
                        int i = 1;

                        // Clearing spaces
                        while (line.charAt(i) == ' ') i++;

                        // X coordinate
                        while (line.charAt(i) != ' ') {
                            numberString.append(line.charAt(i));
                            i++;
                        }
                        numberFloat = Float.parseFloat(numberString.toString());
                        numberString = new StringBuilder();
                        vec.x = numberFloat;
                        i++;

                        // Y coordinate
                        while (line.charAt(i) != ' ') {
                            numberString.append(line.charAt(i));
                            i++;
                        }
                        numberFloat = Float.parseFloat(numberString.toString());
                        numberString = new StringBuilder();
                        vec.y = numberFloat;
                        i++;

                        // Z coordinate
                        while (i < line.length()) {
                            numberString.append(line.charAt(i));
                            i++;
                        }
                        numberFloat = Float.parseFloat(numberString.toString());
                        vec.z = numberFloat;

                        // Finally, adding  the vector
                        vecs.add(vec);
                    }
                    else if (line.charAt(0) == 'f' && line.charAt(1) == ' ') {

                        Polygon polygon = new Polygon();
                        StringBuilder numberString = new StringBuilder();
                        int numberInt;
                        int i = 1;

                        // Clearing spaces
                        while (line.charAt(i) == ' ') i++;

                        // Different formats
                        if (!line.contains("/")) {
                            int vector = 0;
                            Vector3f buffer = new Vector3f(0.0f, 0.0f, 0.0f);
                            while (i < line.length()) {
                                // Get to index
                                for (; i < line.length(); i++) {
                                    if (line.charAt(i) == ' ') break;
                                    numberString.append(line.charAt(i));
                                }

                                // Get the index
                                numberInt = Integer.parseInt(numberString.toString());
                                numberString = new StringBuilder();
                                if (vector < 2) {
                                    polygon.verts[vector] = vecs.get(numberInt - 1);
                                    vector++;
                                }
                                else if (vector == 2) {
                                    polygon.verts[vector] = vecs.get(numberInt - 1);
                                    vector++;
                                    this.polygons.add(polygon);
                                    buffer.newVector(polygon.verts[2]);
                                }
                                else {
                                    Polygon newPolygon = new Polygon();
                                    newPolygon.verts[0].newVector(polygon.verts[0]);
                                    newPolygon.verts[1].newVector(buffer);
                                    newPolygon.verts[2].newVector(vecs.get(numberInt - 1));
                                    this.polygons.add(newPolygon);
                                    buffer.newVector(newPolygon.verts[2]);
                                }

                                i++;
                            }
                        }
                        else {

                            // Add polygons that store 3+ vectors
                            int vector = 0;
                            Vector3f buffer = new Vector3f(0.0f, 0.0f, 0.0f);
                            while (i < line.length()) {
                                // Get to index
                                while (line.charAt(i) != '/') {
                                    numberString.append(line.charAt(i));
                                    i++;
                                }

                                // Get the index
                                numberInt = Integer.parseInt(numberString.toString());
                                numberString = new StringBuilder();
                                if (vector < 2) {
                                    polygon.verts[vector] = vecs.get(numberInt - 1);
                                    vector++;
                                }
                                else if (vector == 2) {
                                    polygon.verts[vector] = vecs.get(numberInt - 1);
                                    vector++;
                                    this.polygons.add(polygon);
                                    buffer.newVector(polygon.verts[2]);
                                }
                                else {
                                    Polygon newPolygon = new Polygon();
                                    newPolygon.verts[0].newVector(polygon.verts[0]);
                                    newPolygon.verts[1].newVector(buffer);
                                    newPolygon.verts[2].newVector(vecs.get(numberInt - 1));
                                    this.polygons.add(newPolygon);
                                    buffer.newVector(newPolygon.verts[2]);
                                }

                                // Go further or end
                                while (line.charAt(i) == '/' || Character.isDigit(line.charAt(i))) {
                                    i++;
                                    if (i >= line.length()) break;
                                }

                                i++;
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Failed to load " + file);
            throw new RuntimeException(e);
        }
    }

}
