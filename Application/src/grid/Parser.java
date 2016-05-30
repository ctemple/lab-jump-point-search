package grid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    private Parser() {

    }

    public static Map getMap(File file) {
        Map map = null;
        int n = 0, m = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;

            // skip line 1
            br.readLine();

            // line 2 is height m
            line = br.readLine();
            m = Integer.valueOf(line.substring(7));

            // line 3 is width n
            line = br.readLine();
            n = Integer.valueOf(line.substring(6));
            System.out.print(n + " " + m);

            // skip line 4
            br.readLine();

            // init map
            map = new Map(n, m);
            map.setObstacle();

            int y = 0;
            while ((line = br.readLine()) != null) {
                char[] chars = line.toCharArray();

                for (int x = 0; x < chars.length; x++) {
                    // chars which represent passable terrain are . G S T
                    if (chars[x] == '.' || chars[x] == 'G' || chars[x] == 'S') {
                        map.setCell(x, y, Type.FLOOR);
                    }
                }

                y++;
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return map;
    }


    public static File getFile(Map map) {
        File file = null;

        // TODO: reverse the file to map process

        return file;
    }
}
