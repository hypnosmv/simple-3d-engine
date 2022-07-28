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

                        Polygon f = new Polygon();
                        StringBuilder numberString = new StringBuilder();
                        int numberInt;
                        int i = 1;

                        // Clearing spaces
                        while (line.charAt(i) == ' ') i++;

                        // Different formats
                        if (!line.contains("/")) {
                            while (i < line.length()) {
                                // Get to index
                                for (; i < line.length(); i++) {
                                    if (line.charAt(i) == ' ') break;
                                    numberString.append(line.charAt(i));
                                }

                                // Get the index
                                numberInt = Integer.parseInt(numberString.toString());
                                numberString = new StringBuilder();
                                f.verts.add(vecs.get(numberInt - 1));
                                i++;
                            }
                        }
                        else {

                            // Add polygons that store 3+ vectors
                            while (i < line.length()) {
                                // Get to index
                                while (line.charAt(i) != '/') {
                                    numberString.append(line.charAt(i));
                                    i++;
                                }

                                // Get the index
                                numberInt = Integer.parseInt(numberString.toString());
                                numberString = new StringBuilder();
                                f.verts.add(vecs.get(numberInt - 1));

                                // Go further or end
                                while (line.charAt(i) == '/' || Character.isDigit(line.charAt(i))) {
                                    i++;
                                    if (i >= line.length()) break;
                                }

                                i++;
                            }
                        }

                        // Finally, adding the polygon
                        this.polygons.add(f);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Failed to load " + file);
            throw new RuntimeException(e);
        }

    }

}
