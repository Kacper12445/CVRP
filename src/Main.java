import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        Loader loader = new Loader();
        loader.loadFileData("Easy - P-n20-k2");
        System.out.println("COORD DATA");
        loader.storedCoordData.forEach(data -> {
            System.out.printf("%d %d %d", data[0], data[1], data[2]);
            System.out.println(" ");
        });
        System.out.println(" ");
        System.out.println("DEMAND DATA");
        loader.storedDemandData.forEach(data -> {
            System.out.printf("%d %d", data[0], data[1]);
            System.out.println(" ");
        });

    }
}