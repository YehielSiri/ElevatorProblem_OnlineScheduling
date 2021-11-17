package ex0.algo;

import ex0.Building;
import ex0.CallForElevator;
import ex0.Elevator;
import ex0.Schedule;

public class Look implements ElevatorAlgo{
    public static final int UP = 1, DOWN = -1;
    public static final int STOP = 1, KEEPGOING = -1;

    private int[] _direction;
    private Building _building;
    private Schedule _schedule;

    public Look(Building b) {
        _building = b;

        _direction = new int[this.getBuilding().numberOfElevetors()];
        for(int i = 0; i < _direction.length; i++) {
            _direction[i] = UP;
        }

        _schedule = new Schedule(b);
    }

    @Override
    public Building getBuilding() {
        return _building;
    }

    public Schedule getSchedule() {
        return this._schedule;
    }

    public int[] getDirection() {
        return this._direction;
    }

    public void setDirection(int elev, int dir) {
        this._direction[elev] = dir;
    }

    @Override
    public String algoName() {
        return "Ex0_OOP_Look_upgraded_Algo";
    }

    /**
     * This function allocates elevators and builds the schedule, allocates only by distance from the source.
     * This way is on the assumption that the 'shortest distance from the source' together with the
     * 'allocation rules' will lead to the 'shortest distance from the destination' too.
     * The Dist function is the one who keeps on the 'allocation rules'.
     * @param c the call for elevator (src, dest)
     * @return the elevator number which has allocated to the call.
     */
    @Override
    public int allocateAnElevator(CallForElevator c) {
        int ans = 0, elevNum = _building.numberOfElevetors();
        int src = c.getSrc();
        for (int i = 1; i < elevNum; i++) {
            //Allocates only by distance from the source.
            if(dist(src, i) < dist(src, ans)) {
                ans = i;
            }
        }
        this.getSchedule().getSchedule()[ans][src] = STOP;
        this.getSchedule().getSchedule()[ans][c.getDest()] = STOP;
        return ans;
    }

    private double dist(int src, int elev) {
        double ans = -1;
        Elevator thisElev = this._building.getElevetor(elev);
        int pos = thisElev.getPos();
        double speed = thisElev.getSpeed();
        int min = this._building.minFloor(), max = this._building.maxFloor();
        double up2down = (max-min)*speed;
        double floorTime = speed+thisElev.getStopTime()+thisElev.getStartTime()+thisElev.getTimeForOpen()+thisElev.getTimeForClose();
        if(elev%2==1) { // up
            if(pos<=src) {ans = (src-pos)*floorTime;}
            else {
                ans = ((max-pos) + (pos-min))*floorTime + up2down;
            }
        }
        else {
            if(pos>=src) {ans = (pos-src)*floorTime;}
            else {
                ans = ((max-pos) + (pos-min))*floorTime + up2down;
            }
        }
        return ans;
    }
    @Override
    public void cmdElevator(int elev) {
        Elevator curr = this._building.getElevetor(elev);
        int nextStation = theNextStation(elev);
        curr.goTo(nextStation);
    }

    public int theNextStation(int elev) {
        Elevator curr = this.getBuilding().getElevetor(elev);
        int[][] currScheduel = this.getSchedule().getSchedule();

        final int pos = floorToColumn(curr.getPos());     //For work simplicity, we won't use the 'floor' number as is, but the serial number instead.
        int nextPos = pos;
        final int direction = this.getDirection()[elev];

        //up
        if (direction == UP) {           //Checking the local variable
            for (int floor = (pos+1) ; floor < currScheduel[elev].length; floor++) {
                if (nextPos != pos) break;              //the next position has found already
                if (currScheduel[elev][floor] == STOP) {
                    nextPos = floor;
                }
            }
            //For case in which the next pos is in the next side:
            if (nextPos == pos) {
                for (int floor = (pos-1) ; floor >= 0; floor--) {
                    if (nextPos != pos) break;          //the next position has found already
                    if (currScheduel[elev][floor] == STOP) {
                        nextPos = floor;
                        this.setDirection(elev, DOWN);      //Updating the object's field, not the local variable
                    }
                }
            }
        }

        //down
        else if (direction == DOWN) {                   //Checking the local variable
            for (int floor = (pos-1) ; floor > -1; floor--) {
                if (nextPos != pos) break;              //the next position has found already
                if (currScheduel[elev][floor] == STOP) {
                    nextPos = floor;
                }
            }
            //For case in which the next pos is in the next side:
            if (nextPos == pos) {
                for (int floor = (pos+1); floor < currScheduel[elev].length; floor++) {
                    if (nextPos != pos) break;          //the next position has found already
                    if (currScheduel[elev][floor] == STOP) {
                        nextPos = floor;
                        this.setDirection(elev, UP);      //Updating the object's field, not the local variable
                    }
                }
            }
        }

        return columnToFloor(nextPos);
    }

    public int floorToColumn(int floor) {
        int column = (floor - this.getBuilding().minFloor());
        return column;
    }

    public int columnToFloor(int column) {
        int floor = (column + this.getBuilding().minFloor());
        return floor;
    }
}
