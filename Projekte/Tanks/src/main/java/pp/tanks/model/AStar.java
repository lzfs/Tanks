package pp.tanks.model;

import pp.tanks.model.item.MoveDirection;
import pp.util.DoubleVec;

import java.util.*;

public class AStar {
    public static final int DIAGONAL_COST = 14;
    public static final int V_H_COST = 10;

    static class Cell {
        int heuristicCost = 0; //Heuristic cost
        int finalCost = 0; //G+H
        int i, j;
        Cell parent;

        Cell(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public String toString() {
            return "[" + this.i + ", " + this.j + "]";
        }
    }

    //Blocked cells are just null Cell values in grid
    static Cell[][] grid = new Cell[5][5];

    static PriorityQueue<Cell> open;

    static boolean closed[][];
    static int startI, startJ;
    static int endI, endJ;

    public static void setBlocked(int i, int j) {
        grid[i][j] = null;
    }

    public static void setStartCell(int i, int j) {
        startI = i;
        startJ = j;
    }

    public static void setEndCell(int i, int j) {
        endI = i;
        endJ = j;
    }

    static void checkAndUpdateCost(Cell current, Cell t, int cost) {
        if (t == null || closed[t.i][t.j]) return;
        int t_final_cost = t.heuristicCost + cost;

        boolean inOpen = open.contains(t);
        if (!inOpen || t_final_cost < t.finalCost) {
            t.finalCost = t_final_cost;
            t.parent = current;
            if (!inOpen) open.add(t);
        }
    }

    public static void AStar() {

        //add the start location to open list.
        open.add(grid[startI][startJ]);

        Cell current;

        while (true) {
            current = open.poll();
            if (current == null) break;
            closed[current.i][current.j] = true;

            if (current.equals(grid[endI][endJ])) {
                return;
            }

            Cell t;
            if (current.i - 1 >= 0) {
                t = grid[current.i - 1][current.j];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);

                if (current.j - 1 >= 0) {
                    t = grid[current.i - 1][current.j - 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }

                if (current.j + 1 < grid[0].length) {
                    t = grid[current.i - 1][current.j + 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }
            }

            if (current.j - 1 >= 0) {
                t = grid[current.i][current.j - 1];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
            }

            if (current.j + 1 < grid[0].length) {
                t = grid[current.i][current.j + 1];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
            }

            if (current.i + 1 < grid.length) {
                t = grid[current.i + 1][current.j];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);

                if (current.j - 1 >= 0) {
                    t = grid[current.i + 1][current.j - 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }

                if (current.j + 1 < grid[0].length) {
                    t = grid[current.i + 1][current.j + 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }
            }
        }
    }

    /*
    Params :
    tCase = test case No.
    x, y = Board's dimensions
    si, sj = start location's x and y coordinates
    ei, ej = end location's x and y coordinates
    int[][] blocked = array containing inaccessible cell coordinates
    */
    public static List<DoubleVec> execute(int x, int y, int si, int sj, int ei, int ej, int[][] blocked) {
        //Reset
        grid = new Cell[x][y];
        closed = new boolean[x][y];
        open = new PriorityQueue<>(Comparator.comparingInt(c -> c.finalCost)
        );
        //Set start position
        setStartCell(si, sj);  //Setting to 0,0 by default. Will be useful for the UI part

        //Set End Location
        setEndCell(ei, ej);

        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                grid[i][j] = new Cell(i, j);
                grid[i][j].heuristicCost = Math.abs(i - endI) + Math.abs(j - endJ);
//                  System.out.print(grid[i][j].heuristicCost+" ");
            }
//              System.out.println();
        }
        grid[si][sj].finalCost = 0;
           
           /*
             Set blocked cells. Simply set the cell values to null
             for blocked cells.
           */
        for (int i = 0; i < blocked.length; ++i) {
            setBlocked(blocked[i][0], blocked[i][1]);
        }

        AStar();
        List<DoubleVec> pathPositions = new ArrayList<>();

        if (closed[endI][endJ]) {
            //Trace back the path
            System.out.println("Path: ");
            Cell current = grid[endI][endJ];
            System.out.print(current);
            pathPositions.add(new DoubleVec(current.i, current.j));
            while (current.parent != null) {
                System.out.print(" -> " + current.parent);
                pathPositions.add(new DoubleVec(current.parent.i, current.parent.j));
                current = current.parent;
            }
            System.out.println();
        }
        else System.out.println("No possible path");

        return pathPositions;
    }

    /**
     * TODO: add JavaDoc
     *
     * @param positions
     * @return
     */
    public static List<MoveDirection> getDirsList(List<DoubleVec> positions) {
        List<MoveDirection> moveDirs = new ArrayList<>();
        for (int i = 1; i < positions.size(); i++) {
            DoubleVec diff = positions.get(i).sub(positions.get(i - 1));
            if (getMoveDirToVec(diff) != null) {
                moveDirs.add(getMoveDirToVec(diff));
            }
            else {
                throw new IllegalArgumentException("Falscher Parameter bei getMoveDirToVec(diff) in AStar");
            }
        }
        return moveDirs;
    }

    /**
     * TODO: add JavaDoc
     *
     * @param vec
     * @return
     */
    public static MoveDirection getMoveDirToVec(DoubleVec vec) {
        if (vec.equals(MoveDirection.UP.getVec())) {
            return MoveDirection.UP;
        }
        else if (vec.equals(MoveDirection.DOWN.getVec())) {
            return MoveDirection.DOWN;
        }
        else if (vec.equals(MoveDirection.LEFT.getVec())) {
            return MoveDirection.LEFT;
        }
        else if (vec.equals(MoveDirection.RIGHT.getVec())) {
            return MoveDirection.RIGHT;
        }
        else if (vec.equals(MoveDirection.RIGHT_DOWN.getVec())) {
            return MoveDirection.RIGHT_DOWN;
        }
        else if (vec.equals(MoveDirection.RIGHT_UP.getVec())) {
            return MoveDirection.RIGHT_UP;
        }
        else if (vec.equals(MoveDirection.LEFT_DOWN.getVec())) {
            return MoveDirection.LEFT_DOWN;
        }
        else if (vec.equals(MoveDirection.LEFT_UP.getVec())) {
            return MoveDirection.LEFT_UP;
        }
        else {
            return null;
        }
    }
}