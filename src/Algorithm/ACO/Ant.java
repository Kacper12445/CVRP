package Algorithm.ACO;

public class Ant {
    protected boolean[] visited;
    protected Integer[] trail;
    protected int trailSize;


    public Ant(int tourSize){
        this.trailSize = tourSize;
        this.trail = new Integer[tourSize];
        this.visited = new boolean[tourSize];
    }
    protected void visitCity(int currentIndex, int city) {
        trail[currentIndex] = city;
        visited[city] = true;
    }

    protected boolean visited(int i) {
        return visited[i];
    }

    protected void resetVisitedCities(){
        for(int i = 0; i < trailSize; i++){
            visited[i] = false;
        }
    }

}
