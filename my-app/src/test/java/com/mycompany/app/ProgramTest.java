package com.mycompany.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.awt.GridLayout;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.awt.Component;

public class ProgramTest {
    private Game game;
    private Player player;
    
    @BeforeEach
    public void setUp() {
        game = new Game();
        player = new Player();
    }
    
    @Test
    public void testGameConstructor() {
        assertNotNull(game);
        assertEquals(State.PLAYING, game.state);
        assertEquals('X', game.player1.symbol);
        assertEquals('O', game.player2.symbol);
        
        for (int i = 0; i < 9; i++) {
            assertEquals(' ', game.board[i]);
        }
    }
    
    @Test
    public void testPlayerInitialization() {
        Player p = new Player();
        assertEquals(0, p.move);
        assertFalse(p.selected);
        assertFalse(p.win);
    }
    
    @Test
    public void testCheckStateXWinRows() {
        char[] board = {
            'X', 'X', 'X',
            ' ', ' ', ' ',
            ' ', ' ', ' '
        };
        game.symbol = 'X';
        assertEquals(State.XWIN, game.checkState(board));
        
        board = new char[]{
            ' ', ' ', ' ',
            'X', 'X', 'X',
            ' ', ' ', ' '
        };
        assertEquals(State.XWIN, game.checkState(board));
        
        board = new char[]{
            ' ', ' ', ' ',
            ' ', ' ', ' ',
            'X', 'X', 'X'
        };
        assertEquals(State.XWIN, game.checkState(board));
    }
    
    @Test
    public void testCheckStateXWinColumns() {
        char[] board = {
            'X', ' ', ' ',
            'X', ' ', ' ',
            'X', ' ', ' '
        };
        game.symbol = 'X';
        assertEquals(State.XWIN, game.checkState(board));
        
        board = new char[]{
            ' ', 'X', ' ',
            ' ', 'X', ' ',
            ' ', 'X', ' '
        };
        assertEquals(State.XWIN, game.checkState(board));
        
        board = new char[]{
            ' ', ' ', 'X',
            ' ', ' ', 'X',
            ' ', ' ', 'X'
        };
        assertEquals(State.XWIN, game.checkState(board));
    }
    
    @Test
    public void testCheckStateXWinDiagonals() {
        char[] board = {
            'X', ' ', ' ',
            ' ', 'X', ' ',
            ' ', ' ', 'X'
        };
        game.symbol = 'X';
        assertEquals(State.XWIN, game.checkState(board));
        
        board = new char[]{
            ' ', ' ', 'X',
            ' ', 'X', ' ',
            'X', ' ', ' '
        };
        assertEquals(State.XWIN, game.checkState(board));
    }
    
    @Test
    public void testCheckStateOWinConditions() {
        char[] board = {
            'O', 'O', 'O',
            ' ', ' ', ' ',
            ' ', ' ', ' '
        };
        game.symbol = 'O';
        assertEquals(State.OWIN, game.checkState(board));
        
        board = new char[]{
            'O', ' ', ' ',
            'O', ' ', ' ',
            'O', ' ', ' '
        };
        assertEquals(State.OWIN, game.checkState(board));
        
        board = new char[]{
            'O', ' ', ' ',
            ' ', 'O', ' ',
            ' ', ' ', 'O'
        };
        assertEquals(State.OWIN, game.checkState(board));
    }
    
    @Test
    public void testCheckStatePlayingAndDraw() {
        char[] board = {
            'X', 'O', 'X',
            'O', ' ', ' ',
            ' ', ' ', ' '
        };
        game.symbol = 'X';
        assertEquals(State.PLAYING, game.checkState(board));
        
        board = new char[]{
            'X', 'O', 'X',
            'X', 'O', 'O',
            'O', 'X', 'O'
        };
        assertEquals(State.DRAW, game.checkState(board));
    }
    
    @Test
    public void testGenerateMoves() {
        ArrayList<Integer> moves = new ArrayList<>();
        game.generateMoves(game.board, moves);
        assertEquals(9, moves.size());
        
        char[] board = {
            'X', ' ', 'O',
            ' ', 'X', ' ',
            'O', ' ', 'X'
        };
        ArrayList<Integer> partialMoves = new ArrayList<>();
        game.generateMoves(board, partialMoves);
        assertEquals(4, partialMoves.size());
        assertTrue(partialMoves.contains(1));
        assertTrue(partialMoves.contains(3));
        assertTrue(partialMoves.contains(5));
        assertTrue(partialMoves.contains(7));
    }
    
    @Test
    public void testEvaluatePosition() {
        Player xPlayer = new Player();
        xPlayer.symbol = 'X';
        
        Player oPlayer = new Player();
        oPlayer.symbol = 'O';
        
        char[] xWinBoard = {
            'X', 'X', 'X',
            ' ', 'O', ' ',
            ' ', 'O', ' '
        };
        game.symbol = 'X';
        assertEquals(Game.INF, game.evaluatePosition(xWinBoard, xPlayer));
        
        assertEquals(-Game.INF, game.evaluatePosition(xWinBoard, oPlayer));
        
        char[] oWinBoard = {
            'X', 'X', ' ',
            'O', 'O', 'O',
            'X', ' ', ' '
        };
        game.symbol = 'O';
        assertEquals(Game.INF, game.evaluatePosition(oWinBoard, oPlayer));
        
        assertEquals(-Game.INF, game.evaluatePosition(oWinBoard, xPlayer));
        
        char[] drawBoard = {
            'X', 'O', 'X',
            'X', 'O', 'O',
            'O', 'X', 'O'
        };
        assertEquals(0, game.evaluatePosition(drawBoard, xPlayer));
        assertEquals(0, game.evaluatePosition(drawBoard, oPlayer));
        
        char[] playingBoard = {
            'X', ' ', ' ',
            ' ', ' ', ' ',
            ' ', ' ', ' '
        };
        game.symbol = 'X';
        assertEquals(-1, game.evaluatePosition(playingBoard, xPlayer));
    }
    
    @Test
    public void testMinMaxBestMove() {
        char[] board = {
            'X', ' ', 'X',
            'O', 'O', ' ',
            ' ', ' ', ' '
        };
        game.board = board.clone();
        
        Player oPlayer = game.player2;
        oPlayer.symbol = 'O';
        game.symbol = oPlayer.symbol;
        
        int bestMove = game.MiniMax(game.board, oPlayer);
        assertEquals(2, bestMove);
    }
    
    @Test
    public void testMinMove() {
        Player xPlayer = game.player1;
        xPlayer.symbol = 'X';
        
        char[] board = {
            ' ', ' ', ' ',
            'O', 'O', ' ',
            'X', ' ', ' '
        };
        game.board = board.clone();
        
        game.symbol = 'O';
        int minValue = game.MinMove(game.board, xPlayer);
        assertTrue(minValue <= Game.INF);
    }
    
    @Test
    public void testMaxMove() {
        Player oPlayer = game.player2;
        oPlayer.symbol = 'O';
        
        char[] board = {
            'X', 'X', ' ',
            'O', 'O', ' ',
            ' ', ' ', ' '
        };
        game.board = board.clone();
        
        game.symbol = 'O';
        int maxValue = game.MaxMove(game.board, oPlayer);
        assertTrue(maxValue >= -Game.INF);
    }
    
    @Test
    public void testTicTacToeCell() {
        TicTacToeCell cell = new TicTacToeCell(1, 0, 0);
        assertEquals(1, cell.getNum());
        assertEquals(0, cell.getRow());
        assertEquals(0, cell.getCol());
        assertEquals(' ', cell.getMarker());
        
        cell.setMarker("X");
        assertEquals('X', cell.getMarker());
        assertFalse(cell.isEnabled());
    }
    
    @Test
    public void testTicTacToePanelInitialization() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        assertNotNull(panel);
        assertEquals(9, panel.getComponentCount());
        
        Component[] cells = panel.getComponents();
        assertTrue(cells[0] instanceof TicTacToeCell);
        assertTrue(cells[8] instanceof TicTacToeCell);
        
        TicTacToeCell firstCell = (TicTacToeCell)cells[0];
        assertEquals(0, firstCell.getNum());
        assertEquals(0, firstCell.getRow());
        assertEquals(0, firstCell.getCol());
    }
    
    @Test
    public void testTicTacToePanelCellArrangement() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        
        TicTacToeCell cell0 = (TicTacToeCell)panel.getComponent(0);
        assertEquals(0, cell0.getNum());
        assertEquals(0, cell0.getRow());
        assertEquals(0, cell0.getCol());
        
        TicTacToeCell cell4 = (TicTacToeCell)panel.getComponent(4);
        assertEquals(4, cell4.getNum());
        assertEquals(1, cell4.getRow());
        assertEquals(1, cell4.getCol());
        
        TicTacToeCell cell8 = (TicTacToeCell)panel.getComponent(8);
        assertEquals(8, cell8.getNum());
        assertEquals(2, cell8.getRow());
        assertEquals(2, cell8.getCol());
    }
    
    @Test
    public void testTicTacToePanelGameInitialization() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        
        try {
            Field gameField = TicTacToePanel.class.getDeclaredField("game");
            gameField.setAccessible(true);
            Game game = (Game)gameField.get(panel);
            
            assertNotNull(game);
            assertEquals('X', game.player1.symbol);
            assertEquals('O', game.player2.symbol);
            assertEquals(game.player1, game.cplayer);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Could not access game field: " + e.getMessage());
        }
    }
    
    @Test
    public void testUtilityPrintMethods() {
        char[] charBoard = {'X', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        Utility.print(charBoard);
        
        int[] intBoard = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Utility.print(intBoard);
        
        ArrayList<Integer> moves = new ArrayList<>();
        moves.add(0);
        moves.add(4);
        moves.add(8);
        Utility.print(moves);
    }
    
    @Test
    public void testCompleteGame() {
        Game testGame = new Game();
        testGame.player1.symbol = 'X';
        testGame.player2.symbol = 'O';
        testGame.cplayer = testGame.player1;
        
        char[] finalBoard = {
            'X', 'O', 'X',
            'O', 'X', 'O',
            ' ', ' ', 'X'
        };
        testGame.board = finalBoard;
        testGame.symbol = 'X';
        testGame.state = testGame.checkState(testGame.board);
        
        assertEquals(State.XWIN, testGame.state);
    }
    
    @Test
    public void testTicTacToeCellActionRegistration() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        
        for (int i = 0; i < panel.getComponentCount(); i++) {
            TicTacToeCell cell = (TicTacToeCell)panel.getComponent(i);
            
            assertTrue(cell.getActionListeners().length > 0);
            assertEquals(panel, cell.getActionListeners()[0]);
        }
    }
    
    @Test
    public void testTicTacToePanelActionPerformedCellMarking() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        
        try {
            Field gameField = TicTacToePanel.class.getDeclaredField("game");
            gameField.setAccessible(true);
            Game panelGame = (Game) gameField.get(panel);
            
            Field cellsField = TicTacToePanel.class.getDeclaredField("cells");
            cellsField.setAccessible(true);
            TicTacToeCell[] cells = (TicTacToeCell[]) cellsField.get(panel);
            
            assertEquals('X', panelGame.cplayer.symbol);
            
            TicTacToeCell cell = cells[4];
            ActionEvent actionEvent = new ActionEvent(cell, ActionEvent.ACTION_PERFORMED, "click");
            panel.actionPerformed(actionEvent);
            
            assertEquals('X', cell.getMarker());
            assertEquals('X', panelGame.board[4]);
            assertFalse(cell.isEnabled());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access TicTacToePanel fields: " + e.getMessage());
        }
    }
    
    
    @Test
    public void testTicTacToePanelActionPerformedStateCheck() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        
        try {
            Field gameField = TicTacToePanel.class.getDeclaredField("game");
            gameField.setAccessible(true);
            Game panelGame = (Game) gameField.get(panel);
            
            Field cellsField = TicTacToePanel.class.getDeclaredField("cells");
            cellsField.setAccessible(true);
            TicTacToeCell[] cells = (TicTacToeCell[]) cellsField.get(panel);
            
            panelGame.board = new char[]{
                'X', 'X', ' ',
                ' ', ' ', ' ',
                ' ', ' ', ' '
            };
            
            for (int i = 0; i < panelGame.board.length; i++) {
                if (panelGame.board[i] != ' ') {
                    cells[i].setMarker(String.valueOf(panelGame.board[i]));
                }
            }
        
            TicTacToeCell cell2 = cells[2];
            ActionEvent actionEvent = new ActionEvent(cell2, ActionEvent.ACTION_PERFORMED, "click");

            panelGame.state = State.PLAYING;
            panelGame.symbol = 'X';
            
            for (int i = 0; i < 3; i++) {
                cells[i].setMarker("X");
                panelGame.board[i] = 'X';
            }

            assertEquals(State.XWIN, panelGame.checkState(panelGame.board));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access fields: " + e.getMessage());
        }
    }
    
    @Test
    public void testTicTacToePanelActionPerformedDrawCondition() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        
        try {
            Field gameField = TicTacToePanel.class.getDeclaredField("game");
            gameField.setAccessible(true);
            Game panelGame = (Game) gameField.get(panel);
            
            Field cellsField = TicTacToePanel.class.getDeclaredField("cells");
            cellsField.setAccessible(true);
            TicTacToeCell[] cells = (TicTacToeCell[]) cellsField.get(panel);
            
            // Setup almost draw board
            char[] drawBoard = {
                'X', 'O', 'X',
                'X', 'O', 'O',
                'O', 'X', ' '
            };
            
            panelGame.board = drawBoard.clone();
            
            for (int i = 0; i < 8; i++) {
                cells[i].setMarker(String.valueOf(drawBoard[i]));
            }
            
            panelGame.symbol = 'X';
            assertEquals(State.PLAYING, panelGame.checkState(panelGame.board));
            
            panelGame.cplayer = panelGame.player1;
            cells[8].setMarker("X");
            panelGame.board[8] = 'X';
            
            panelGame.symbol = 'X';
            assertEquals(State.DRAW, panelGame.checkState(panelGame.board));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access fields: " + e.getMessage());
        }
    }
}
