import java.io.IOException;

public class Run {
    public static void main(String[] args) throws Exception {
        try {
            // BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
             PowerGrid pg = new PowerGrid("E:\\V-D\\Placement\\Projects in CV\\A6\\A6answeer\\Q1\\input.txt");
                pg.preprocessImportantLines();
                System.out.println(pg.numImportantLines("Delhi","Chennai"));
                System.out.println(pg.numImportantLines("Mumbai","Kolkata")); 
                System.out.println("Expected (Important line between Bhopal-Mumbai ) : 1");
                System.out.println("Important line between Bhopal-Mumbai :   "+pg.numImportantLines("Mumbai","Bhopal"));

                System.out.println("Expected (Important line between Bhopal-Delhi ) : 1");
                System.out.println("Important line between Bhopal-Delhi :   "+pg.numImportantLines("Mumbai","Bhopal"));

                for (PowerLine line : pg.criticalLines()) {
                    System.out.println("Critical Lines( City A-City B ) : "+line.cityA + " -" + line.cityB );
                }
                System.out.println();
           
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error parsing input: " + e.getMessage());
            e.printStackTrace();
        }
        
    }
}
