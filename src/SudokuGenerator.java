import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {
    private static final int SIZE = 9;
    private static final Random rand = new Random();

    /**
     * Generates a random Sudoku puzzle and its solution.
     * @param holes number of cells to remove (difficulty: 30 easy, 40 medium, 50 hard)
     * @return a 2D String array where [0] = puzzle, [1] = solution
     */
    public static String[][] generate(int holes) {
        int[][] solutionBoard = generateFullSolution();
        int[][] puzzleBoard = copyBoard(solutionBoard);

        makeHolesUnique(puzzleBoard, holes);

        String[] puzzle = boardToStringArray(puzzleBoard, '-');
        String[] solution = boardToStringArray(solutionBoard, '0');
        return new String[][]{puzzle, solution};
    }



    // ==================== Internal Methods ====================

    private static int[][] copyBoard(int[][] b) {
        int[][] c = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) System.arraycopy(b[i], 0, c[i], 0, SIZE);
        return c;
    }

    private static String[] boardToStringArray(int[][] board, char emptyChar) {
        String[] out = new String[SIZE];
        for (int r = 0; r < SIZE; r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < SIZE; c++) {
                int v = board[r][c];
                if (v == 0) sb.append(emptyChar);
                else sb.append(v);
            }
            out[r] = sb.toString();
        }
        return out;
    }

    private static int[][] generateFullSolution() {
        int[][] board = new int[SIZE][SIZE];
        fillBoard(board, 0, 0);
        return board;
    }

    private static boolean fillBoard(int[][] board, int r, int c) {
        if (r == SIZE) return true;
        int nr = (c == SIZE - 1) ? r + 1 : r;
        int nc = (c == SIZE - 1) ? 0 : c + 1;

        List<Integer> digits = new ArrayList<>();
        for (int d = 1; d <= 9; d++) digits.add(d);
        Collections.shuffle(digits, rand);

        for (int d : digits) {
            if (canPlace(board, r, c, d)) {
                board[r][c] = d;
                if (fillBoard(board, nr, nc)) return true;
                board[r][c] = 0;
            }
        }
        return false;
    }

    private static boolean canPlace(int[][] board, int r, int c, int val) {
        for (int i = 0; i < SIZE; i++) {
            if (board[r][i] == val || board[i][c] == val) return false;
        }
        int br = (r / 3) * 3;
        int bc = (c / 3) * 3;
        for (int i = br; i < br + 3; i++)
            for (int j = bc; j < bc + 3; j++)
                if (board[i][j] == val) return false;
        return true;
    }

    private static void makeHolesUnique(int[][] board, int holesTarget) {
        List<int[]> cells = new ArrayList<>();
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                cells.add(new int[]{r, c});
        Collections.shuffle(cells, rand);

        int removed = 0;
        for (int[] cell : cells) {
            if (removed >= holesTarget) break;
            int r = cell[0], c = cell[1];
            if (board[r][c] == 0) continue;
            int backup = board[r][c];
            board[r][c] = 0;

            int[][] copy = copyBoard(board);
            int count = countSolutions(copy, 2);
            if (count != 1) board[r][c] = backup;
            else removed++;
        }
    }

    private static int countSolutions(int[][] board, int limit) {
        return solveCount(board, 0, 0, limit);
    }

    private static int solveCount(int[][] board, int r, int c, int limit) {
        if (r == SIZE) return 1;
        int nr = (c == SIZE - 1) ? r + 1 : r;
        int nc = (c == SIZE - 1) ? 0 : c + 1;

        if (board[r][c] != 0) {
            return solveCount(board, nr, nc, limit);
        } else {
            int total = 0;
            for (int d = 1; d <= 9; d++) {
                if (canPlace(board, r, c, d)) {
                    board[r][c] = d;
                    total += solveCount(board, nr, nc, limit);
                    board[r][c] = 0;
                    if (total >= limit) return total;
                }
            }
            return total;
        }
    }
}
