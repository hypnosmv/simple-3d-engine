package ProjectionUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class mesh {
    public ArrayList<face> faces = new ArrayList<>();

    public void inputOBJ(String file) {

        try {
            // Handle file
            FileReader fileReader = new FileReader(file);
            BufferedReader buffReader = new BufferedReader(fileReader);

            // Read vectors
            ArrayList<vec3f> vecs = new ArrayList<>();

            while (buffReader.ready()) {

                String line = buffReader.readLine();

                if (line.length() > 0) {

                    if (line.charAt(0) == 'v' && line.charAt(1) == ' ') {

                        vec3f vec = new vec3f(0.0f, 0.0f, 0.0f);
                        StringBuilder numberString = new StringBuilder();
                        float numberFloat;
                        int i = 1;

                        // Clear spaces
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

                        // Finally, add the vector
                        vecs.add(vec);
                    }
                    else if (line.charAt(0) == 'f' && line.charAt(1) == ' ') {

                        face f = new face();
                        StringBuilder numberString = new StringBuilder();
                        int numberInt;
                        int i = 1;

                        // Clear spaces
                        while (line.charAt(i) == ' ') i++;

                        // Old vs new-school
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
                                System.out.println(numberInt);
                                i++;
                            }
                        }
                        else {

                            // Add faces that store 3+ vectors
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

                        // Finally, add the face
                        this.faces.add(f);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Failed to load " + file);
            throw new RuntimeException(e);
        }

    }

    public float getMax() {

        float max = 0.0f;

        for (face f : this.faces) {
            for (vec3f vec : f.verts) {
                if (Math.abs(vec.x) > max) max = vec.x;
                if (Math.abs(vec.y) > max) max = vec.y;
                if (Math.abs(vec.z) > max) max = vec.z;
            }
        }

        return max;
    }

}
