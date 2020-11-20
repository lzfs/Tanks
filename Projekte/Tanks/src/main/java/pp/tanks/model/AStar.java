package pp.tanks.model;

import pp.tanks.model.item.MoveDirection;
import pp.util.DoubleVec;

import java.util.*;

public class AStar {
    public final int DIAGONAL_COST;
    public final int V_H_COST;

    public AStar() {
        this.DIAGONAL_COST = 1;
        this.V_H_COST = 1;
    }

    static class Cell{
        int heuristicCost = 0; //Heuristic cost
        int finalCost = 0; //G+H
        int i, j;
        Cell parent;

        Cell(int i, int j){
            this.i = i;
            this.j = j;
        }

        @Override
        public String toString(){
            return "["+this.i+", "+this.j+"]";
        }
    }

    //Blocked cells are just null Cell values in grid
    static Cell [][] grid = new Cell[5][5];

    static PriorityQueue<Cell> open;

    static boolean[][] closed;
    static int startI, startJ;
    static int endI, endJ;

    /**
     * TODO: add JavaDoc
     * @param i
     * @param j
     */
    public void setBlocked(int i, int j){
        grid[i][j] = null;
    }

    /**
     * TODO: add JavaDoc
     * @param i
     * @param j
     */
    public void setStartCell(int i, int j){
        startI = i;
        startJ = j;
    }

    /**
     * TODO: add JavaDoc
     * @param i
     * @param j
     */
    public void setEndCell(int i, int j){
        endI = i;
        endJ = j;
    }

    /**
     * TODO: add JavaDoc
     * @param current
     * @param t
     * @param cost
     */
    static void checkAndUpdateCost(Cell current, Cell t, int cost){
        if(t == null || closed[t.i][t.j])return;
        int t_final_cost = t.heuristicCost+cost;

        boolean inOpen = open.contains(t);
        if(!inOpen || t_final_cost<t.finalCost){
            t.finalCost = t_final_cost;
            t.parent = current;
            if(!inOpen)open.add(t);
        }
    }

    /**
     * TODO: add JavaDoc
     */
    public void aStar(){
        open.add(grid[startI][startJ]);

        Cell current;

        while(true){
            current = open.poll();
            if(current==null)break;
            closed[current.i][current.j]=true;

            if(current.equals(grid[endI][endJ])){
                return;
            }

            Cell t;
            if(current.i-1>=0){
                t = grid[current.i-1][current.j];
                checkAndUpdateCost(current, t, current.finalCost+V_H_COST);

                if(current.j-1>=0){
                    t = grid[current.i-1][current.j-1];
                    checkAndUpdateCost(current, t, current.finalCost+DIAGONAL_COST);
                }

                if(current.j+1<grid[0].length){
                    t = grid[current.i-1][current.j+1];
                    checkAndUpdateCost(current, t, current.finalCost+DIAGONAL_COST);
                }
            }

            if(current.j-1>=0){
                t = grid[current.i][current.j-1];
                checkAndUpdateCost(current, t, current.finalCost+V_H_COST);
            }

            if(current.j+1<grid[0].length){
                t = grid[current.i][current.j+1];
                checkAndUpdateCost(current, t, current.finalCost+V_H_COST);
            }

            if(current.i+1<grid.length){
                t = grid[current.i+1][current.j];
                checkAndUpdateCost(current, t, current.finalCost+V_H_COST);

                if(current.j-1>=0){
                    t = grid[current.i+1][current.j-1];
                    checkAndUpdateCost(current, t, current.finalCost+DIAGONAL_COST);
                }

                if(current.j+1<grid[0].length){
                    t = grid[current.i+1][current.j+1];
                    checkAndUpdateCost(current, t, current.finalCost+DIAGONAL_COST);
                }
            }
        }
    }

    /**
     * TODO: add JavaDoc
     * @param x
     * @param y
     * @param si
     * @param sj
     * @param ti
     * @param tj
     * @param blocked
     * @return
     */
    //si, sj = start pos
    //ti, tj = end pos
    public ArrayList<DoubleVec> getPath(int x, int y, int si, int sj, int ti, int tj, int[][] blocked){
        grid = new Cell[x][y];
        closed = new boolean[x][y];
        open = new PriorityQueue<>((Object o1, Object o2) -> {
            Cell c1 = (Cell)o1;
            Cell c2 = (Cell)o2;

            return Integer.compare(c1.finalCost, c2.finalCost);
        });

        setStartCell(si, sj);
        setEndCell(ti, tj);

        for(int i=0;i<x;++i){
            for(int j=0;j<y;++j){
                grid[i][j] = new Cell(i, j);
                grid[i][j].heuristicCost = Math.abs(i-endI)+Math.abs(j-endJ);
            }
        }
        grid[si][sj].finalCost = 0;

        for (int[] ints : blocked) {
            setBlocked(ints[0], ints[1]);
        }

        this.aStar();

        ArrayList<DoubleVec> positions = new ArrayList<>();
        if(closed[endI][endJ]){
            Cell current = grid[endI][endJ];
            positions.add(new DoubleVec(current.i, current.j));
            while(current.parent!=null){
                positions.add(new DoubleVec(current.parent.i, current.parent.j));
                current = current.parent;
            }
        } else {
            System.out.println("No possible path");
        }
        return positions;
    }

    /**
     * TODO: add JavaDoc
     * @param positions
     * @return
     */
    public ArrayList<MoveDirection> getDirsList(ArrayList<DoubleVec> positions) {
        ArrayList<MoveDirection> moveDirs = new ArrayList<>();
        for(int i = 1; i < positions.size(); i++) {
            DoubleVec diff = positions.get(i).sub(positions.get(i-1));
            if(getMoveDirToVec(diff) != null) {
                moveDirs.add(getMoveDirToVec(diff));
            } else {
                throw new IllegalArgumentException("Falscher Parameter bei getMoveDirToVec(diff) in AStar");
            }
        }
        return moveDirs;
    }

    /**
     * TODO: add JavaDoc
     * @param vec
     * @return
     */
    public MoveDirection getMoveDirToVec(DoubleVec vec) {
        if(vec.equals(MoveDirection.UP.getVec())) {
            return MoveDirection.UP;
        } else if(vec.equals(MoveDirection.DOWN.getVec())) {
            return MoveDirection.DOWN;
        } else if(vec.equals(MoveDirection.LEFT.getVec())) {
            return MoveDirection.LEFT;
        } else if(vec.equals(MoveDirection.RIGHT.getVec())) {
            return MoveDirection.RIGHT;
        } else if(vec.equals(MoveDirection.RIGHT_DOWN.getVec())) {
            return MoveDirection.RIGHT_DOWN;
        } else if(vec.equals(MoveDirection.RIGHT_UP.getVec())) {
            return MoveDirection.RIGHT_UP;
        } else if(vec.equals(MoveDirection.LEFT_DOWN.getVec())) {
            return MoveDirection.LEFT_DOWN;
        } else if(vec.equals(MoveDirection.LEFT_UP.getVec())) {
            return MoveDirection.LEFT_UP;
        } else {
            return null;
        }
    }
}