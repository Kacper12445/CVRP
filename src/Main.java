import Loader.Loader;

import java.io.IOException;
import Loader.*;


public class Main {

    public static void main(String[] args) throws IOException {
        Loader loader = new Loader();
        CVRPModel model = loader.loadFileData("V_Difficult - Leuven1-3000N, 25Q");
        System.out.println("COORD DATA");
        for (int[] data: model.getCoords()) {
            System.out.printf("%d %d %d", data[0], data[1], data[2]);
            System.out.println(" ");
        }
        System.out.println(" ");
        System.out.println("DEMAND DATA");
        for (int[] data: model.getDemands()) {
            System.out.printf("%d %d", data[0], data[1]);
            System.out.println(" ");
        }
    }
}