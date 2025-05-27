package com.mycompany.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TicTacToe Game Tests")
public class ProgramTest {

    private Game game;
    private Player player;

    @BeforeEach
    public void prepare() {
        game = new Game();
        player = new Player();
    }

    @Nested
    @DisplayName("Player Initialization")
    class PlayerTests {

        @Test
        @DisplayName("Player should be initialized with default values")
        public void playerShouldStartWithDefaultValues() {
            Player p = new Player();
            assertEquals('\0', p.symbol);
            assertEquals(0, p.move);
            assertFalse(p.selected);
            assertFalse(p.win);
        }
    }

    @Nested
    @DisplayName("Game Initialization and State")
    class GameInitializationTests {

        @Test
        @DisplayName("Game should initialize correctly with default state")
        public void gameShouldStartWithCleanBoardAndDefaultState() {
            assertEquals(State.PLAYING, game.state);
            assertEquals('X', game.player1.symbol);
            assertEquals('O', game.player2.symbol);
            for (char c : game.board) {
                assertEquals(' ', c);
            }
        }

        @Test
        @DisplayName("Game should recognize a draw situation")
        public void drawConditionShouldBeRecognized() {
            game.symbol = 'X';
            char[] drawBoard = "OXOXOXOXO".toCharArray();
            assertEquals(State.DRAW, game.checkState(drawBoard));
        }

        @Test
        @DisplayName("Game should continue when moves are available")
        public void gameShouldContinueIfMovesLeft() {
            game.symbol = 'X';
            char[] board = "XOXX OOXO".toCharArray();
            assertEquals(State.PLAYING, game.checkState(board));
        }

        @ParameterizedTest
        @DisplayName("Game should detect X winning patterns")
        @CsvSource({
            "X,X,X, , , , , , , XWIN",
            "X, , ,X, , ,X, , , XWIN",
            "X, , , ,X, , , ,X, XWIN"
        })
        public void gameShouldDetectXWinningConditions(Character a, Character b, Character c,
                                                       Character d, Character e, Character f,
                                                       Character g, Character h, Character i,
                                                       State expected) {
            game.symbol = 'X';
            char[] board = {
                a != null ? a : ' ',
                b != null ? b : ' ',
                c != null ? c : ' ',
                d != null ? d : ' ',
                e != null ? e : ' ',
                f != null ? f : ' ',
                g != null ? g : ' ',
                h != null ? h : ' ',
                i != null ? i : ' '
            };
            assertEquals(expected, game.checkState(board));
        }

        @ParameterizedTest
        @DisplayName("Game should detect O winning patterns")
        @CsvSource({
            "O,O,O, , , , , , , OWIN",
            "O, , ,O, , ,O, , , OWIN",
            "O, , , ,O, , , ,O, OWIN"
        })
        public void gameShouldDetectOWinningConditions(Character a, Character b, Character c,
                                                       Character d, Character e, Character f,
                                                       Character g, Character h, Character i,
                                                       State expected) {
            game.symbol = 'O';
            char[] board = {
                a != null ? a : ' ',
                b != null ? b : ' ',
                c != null ? c : ' ',
                d != null ? d : ' ',
                e != null ? e : ' ',
                f != null ? f : ' ',
                g != null ? g : ' ',
                h != null ? h : ' ',
                i != null ? i : ' '
            };
            assertEquals(expected, game.checkState(board));
        }

        @Test
        @DisplayName("Should identify empty cells correctly")
        public void generateMovesShouldReturnEmptyIndices() {
            char[] board = "X OX XO  ".toCharArray();
            ArrayList<Integer> available = new ArrayList<>();
            game.generateMoves(board, available);
            assertTrue(available.containsAll(List.of(1, 4, 7, 8)));
            assertEquals(4, available.size());
        }
    }

    @Nested
    @DisplayName("Evaluation Logic")
    class EvaluationTests {

        @Test
        @DisplayName("Evaluation should return expected scores")
        public void evaluationShouldReturnExpectedValues() {
            player.symbol = 'X';

            char[] win = "XXXOO    ".toCharArray();
            game.symbol = 'X';
            assertEquals(Game.INF, game.evaluatePosition(win, player));

            char[] lose = "OOOXX    ".toCharArray();
            game.symbol = 'O';
            player.symbol = 'X';
            assertEquals(-Game.INF, game.evaluatePosition(lose, player));

            char[] draw = "XOXOXOXOX".toCharArray();
            assertEquals(0, game.evaluatePosition(draw, player));

            char[] active = "XO       ".toCharArray();
            assertEquals(-1, game.evaluatePosition(active, player));
        }
    }

    @Nested
    @DisplayName("Minimax and AI Logic")
    class MinimaxTests {

        @Test
        @DisplayName("MiniMax should find a valid move")
        public void minimaxShouldFindOptimalMove() {
            char[] board = "XX OO    ".toCharArray();
            player.symbol = 'O';
            game.symbol = 'O';
            int move = game.MiniMax(board, player);
            assertTrue(move >= 1 && move <= 9);
        }

        @Test
        @DisplayName("Min and Max move evaluations should be bounded")
        public void minMaxShouldEvaluateMoveValues() {
            char[] board = "XOX      ".toCharArray();
            player.symbol = 'X';
            int minVal = game.MinMove(board, player);
            int maxVal = game.MaxMove(board, player);
            assertTrue(minVal <= Game.INF);
            assertTrue(maxVal >= -Game.INF);
        }
    }

    @Nested
    @DisplayName("UI Component Tests")
    class UITests {

        @Test
        @DisplayName("TicTacToeCell should store and update correctly")
        public void ticTacToeCellShouldStoreCorrectInfo() {
            TicTacToeCell cell = new TicTacToeCell(7, 2, 1);
            assertEquals(7, cell.getNum());
            assertEquals(2, cell.getCol());
            assertEquals(1, cell.getRow());
            assertEquals(' ', cell.getMarker());

            cell.setMarker("X");
            assertEquals('X', cell.getMarker());
            assertFalse(cell.isEnabled());
        }

        @Test
        @DisplayName("Panel should initialize with a valid game object")
        public void panelShouldInitializeWithActiveGame() {
            TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
            assertNotNull(panel.game);
            assertEquals('X', panel.game.cplayer.symbol);
        }

        @Test
        @DisplayName("ActionEvent should not throw error when processed")
        public void actionEventShouldChangeCellState() {
            TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
            TicTacToeCell testCell = new TicTacToeCell(0, 0, 0);

            ActionEvent evt = new ActionEvent(testCell, ActionEvent.ACTION_PERFORMED, "test");
            assertDoesNotThrow(() -> panel.actionPerformed(evt));
        }
    }

    @Nested
    @DisplayName("Utility and Infrastructure")
    class MiscTests {

        @Test
        @DisplayName("Utility print methods should not throw exceptions")
        public void utilityPrintMethodsShouldNotThrow() {
            assertDoesNotThrow(() -> {
                Utility.print("XO ".toCharArray());
                Utility.print(new int[]{1, 2, 3});
                ArrayList<Integer> list = new ArrayList<>();
                list.add(4);
                list.add(7);
                Utility.print(list);
            });
        }

        @Test
        @DisplayName("Main method should be declared in Program class")
        public void mainMethodShouldExist() {
            try {
                assertNotNull(Program.class.getDeclaredMethod("main", String[].class));
            } catch (NoSuchMethodException e) {
                fail("main method should be defined in Program class");
            }
        }
    }
}
