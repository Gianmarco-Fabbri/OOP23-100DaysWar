package it.unibo.the100dayswar.model.pathfinder.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import it.unibo.the100dayswar.model.cell.api.Cell;
import it.unibo.the100dayswar.model.pathfinder.api.PathFinder;

/**
 * A pathfinding implementation using Breadth-First Search (BFS).
 */
public class BfsPathFinder implements PathFinder {

    private final Map<Coordinate, Cell> cellMap;

    private record Coordinate(int x, int y) { }

    /**
     * Constructor for BfsPathFinder.
     *
     * @param allCells the list of all cells in the map
     */
    public BfsPathFinder(final Set<Cell> allCells) {
        this.cellMap = allCells.stream()
            .collect(Collectors.toMap(
                c -> new Coordinate(c.getPosition().getX(), c.getPosition().getY()),
                c -> c
            ));
    }

    /**
     * Finds the shortest path between a start cell and a destination cell.
     *
     * @param start the starting cell
     * @param destination the destination cell
     * @return a list of cells representing the path, or an empty list if no path exists
     */
    @Override
    public List<Cell> findPath(final Cell start, final Cell destination) {
        if (start.equals(destination)) {
            return Collections.singletonList(start);
        }
        final Map<Cell, Cell> cameFrom = new HashMap<>();
        final Queue<Cell> queue = new LinkedList<>();
        final Set<Cell> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            final Cell current = queue.poll();

            if (current.equals(destination)) {
                return reconstructPath(cameFrom, start, destination);
            }
            for (final Cell neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor) && neighbor.isFree()) {
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        return Collections.emptyList(); // No path found
    }

    /**
     * Reconstructs the path from the start cell to the destination cell.
     *
     * @param cameFrom a map tracking the previous cell for each visited cell
     * @param start the starting cell
     * @param destination the destination cell
     * @return the list of cells representing the path
     */
    private List<Cell> reconstructPath(final Map<Cell, Cell> cameFrom, final Cell start, final Cell destination) {
        final List<Cell> path = new ArrayList<>();
        Cell current = destination;

        while (!current.equals(start)) {
            path.add(current);
            current = cameFrom.get(current);
        }
        path.add(start);
        Collections.reverse(path);
        return path;
    }

    /**
     * Gets the neighbors of a cell.
     *
     * @param cell the cell for which to get neighbors
     * @return a list of adjacent cells that are free
     */
    private List<Cell> getNeighbors(final Cell cell) {
        final int x = cell.getPosition().getX();
        final int y = cell.getPosition().getY();
        final List<Cell> neighbors = new ArrayList<>();

        final int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (final int[] dir : directions) {
            final Cell neighbor = cellMap.get(new Coordinate(x + dir[0], y + dir[1]));
            if (neighbor != null && neighbor.isFree()) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }
}
