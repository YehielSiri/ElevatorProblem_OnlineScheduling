package ex0;

/**
 * For upgrading 'ShabatElevatorAlgo', the following stations of each elevator are required to be known.
 * That what Schedule object is keeping
 * The interface is part of Ex0 - OOP (Ariel U.)
 */
public class Schedule{
    public static final int STOP = 1, KEEPGOING = -1;

    private int[][] schedule;       //keeps for any elevator what floors is it going to stop at
    private int numOfElevators;     //num of rows
    private int numOfFloors;        //num of columns

    public Schedule(Building b) {
        this.numOfElevators = b.numberOfElevetors();
        this.numOfFloors = (b.maxFloor() - b.minFloor()) + 1;
        this.schedule = new int[numOfElevators][numOfFloors];
        for (int i = 0; i < this.schedule.length; i++) {
            for (int j = 0; j < this.schedule[0].length; j++) {
                this.schedule[i][j] = KEEPGOING;
            }
        }
    }

    public int[][] getSchedule(){
        return this.schedule;
    }

    public int getNumOfFloors() {
        return numOfFloors;
    }
}
