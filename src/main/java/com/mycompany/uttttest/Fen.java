/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uttttest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author nick
 */
public class Fen {
    
    private String placementData;
    private boolean activePlayer;
    private int activeBoard;
    private final List<String> boardList;
    private final Boolean[][] moveArrayArray = new Boolean[9][9];
    
    /**
     * Initializes Fen class with string input for fen notation
     * should probably add a default Fen that is the starting board
     * @param fenInput
     */
    public Fen(String fenInput)
    {
        List<String> fenInputs = split(fenInput, " ");
        
        placementData = fenInputs.get(0);
        
        activePlayer = fenInputs.get(1).equals("x");
        
        try
        {
            activeBoard = Integer.parseInt(fenInputs.get(2)) - 1;
        }
        catch (NumberFormatException NumberFormatException)
        {
            activeBoard = -1;
        }
        
        boardList = split(fenInputs.get(0), "/");
        
        for (int i = 0; i < 9; ++i)
        {
            moveArrayArray[i] = getMoveArray(boardList.get(i));
        }
        
        
    }
    
    // taken and modified from stack overflow
    // splits string into a list of strings separated by delimiter
    private static List<String> split(String str, String delimiter)
    {
    return Stream.of(str.split(delimiter))
      .map (elem -> elem)
      .collect(Collectors.toList());
    }
    
    // gets an array of Booleans that represents the state of a (mini) board
    // true = x, false = o, null = empty space
    private Boolean[] getMoveArray(String boardState)
    {
        Boolean[] boardArray = new Boolean[9];
        int arrIndex = 0;
        for (int i = 0; i < boardState.length(); ++i)
        {
            if (boardState.charAt(i) >= '0' && boardState.charAt(i) <= '9')
            {
                arrIndex += boardState.charAt(i) - '0';
            } 
            else
            {
                // sets to true if x, false if o
                boardArray[arrIndex] = boardState.substring(i,i + 1).equals("x");
                arrIndex++;
            }
        }
        
        return(boardArray);
    }
    
    // gets a string reresenting fen notation of a single mini board's state
    // provided an Array of Booleans using the same notation as getMoveArray()
    private String moveArrayToBoardState(Boolean[] moveArray)
    {
        int num = 0;
        String boardState = "";
        // iterate through moveArray
        for (int i = 0; i < moveArray.length; ++i)
        {
            if (moveArray[i] == null)
            {
                num++;
            }
            else
            {
                if (num != 0)
                {
                    boardState += num;
                }
                num = 0;
                if (moveArray[i])
                {
                    boardState += "x";
                }
                else
                {
                    boardState += "o";
                }
            }
        }
        if (num != 0)
        {
            boardState += num;
        }
        return(boardState);
    }
    
    // returns 1 if x won
    // returns 2 if o won
    // returns 0 if board is tied
    // returns -1 if board is not finished
    private int boardIsWon(Boolean[] moveArray)
    {
        int boardState = -1;
        
        // testing if board is full
        if (
                (moveArray[0] != null && moveArray[1] != null && moveArray[2] != null && 
                moveArray[3] != null && moveArray[4] != null && moveArray[5] != null && 
                moveArray[6] != null && moveArray[7] != null && moveArray[8] != null)
            )
        {
            boardState = 0;
        }
        
        // testing if there is a win on the diagonals
        // 0, 4, 8 is the main diagonal
        if (moveArray[0] != null && moveArray[0] == moveArray[4] && moveArray[4] == moveArray[8])
        {
            boardState = moveArray[0] == true ? 1 : 2;
        }
        // 2, 4, 6 is the Anti-diagonal
        if (moveArray[2] != null && moveArray[2] == moveArray[4] && moveArray[4] == moveArray[6])
        {
            boardState = moveArray[2] == true ? 1 : 2;
        }
        
        // checks for each orthogonal win
        for (int i = 0; i < 3; ++i)
            {
                if (moveArray[3*i] != null &&
                    moveArray[3*i] == moveArray[3*i+1] &&
                    moveArray[3*i+1] == moveArray[3*i+2])
                {
                    boardState = moveArray[3*i] ? 1 : 2;
                }
                if (moveArray[i] != null &&
                    moveArray[i] == moveArray[i+3] &&
                    moveArray[i+3] == moveArray[i+6])
                {
                    boardState = moveArray[i] ? 1 : 2;
                }
            }
        
        return(boardState);
    }
    
    private Boolean winner()
    {
        Boolean[] boardArray = new Boolean[9];
        for (int i = 0; i < 9; ++i)
        {
            boardArray[i] = switch(boardIsWon(moveArrayArray[i]))
            {
                case 1 -> true;
                case 2 -> false;
                default -> null;
            };
        }
        return(switch(boardIsWon(boardArray))
        {
            case 1 -> true;
            case 2 -> false;
            default -> null;
        });
    }
    
    // checks if a move (majPos, minPos) is able to be played
    // given the current board state fen
    // if minPos = -1, we're testing if you can play anywhere in the majPos board
    private boolean validateMove(int majPos, int minPos)
    {
        Boolean[] moveArray = moveArrayArray[majPos];
        boolean valid = true;
        // testing if player can currently play in majPos board 
        // and if there's already a move played there
        if (minPos >= 0)
        {
            valid = ((activeBoard < 0) || (majPos == activeBoard)) && (moveArray[minPos] == null);
        }
        
       
        if (boardIsWon(moveArray) >= 0)
        {
            valid = false;
        }
        
        Boolean winner = winner();
        return(!(winner != null) && valid);
    }
    
    public void addMove(int majPos, int minPos)
    {
        Boolean[] moveArray = moveArrayArray[majPos];
        if (validateMove(majPos, minPos))
        {
            moveArray[minPos] = activePlayer;
            boardList.set(majPos, moveArrayToBoardState(moveArray));
            moveArrayArray[majPos] = moveArray;
            placementData = String.join("/", boardList);
            activePlayer = !activePlayer;
            activeBoard = minPos;
            if (!validateMove(activeBoard, -1))
            {
                activeBoard = -1;
            }
            
        }
        else
        {
            System.out.println("not a valid move");
        }
    }
    
    // returns the fen as a String
    public String getFen()
    {
        String fen = placementData + " ";
        if (activePlayer)
        {
            fen += "x";
        }
        else
        {
            fen += "o";
        }
        fen += " " + ((activeBoard >= 0) ? activeBoard + 1 : "-");
        return fen;
    }
    
    public int getActiveBoard()
    {
        return(activeBoard);
    }
    
    public List<int[]> getMoveList()
    {
        List<int[]> moveList = new ArrayList<>();
        int[] move = new int[2];
        if (activeBoard < 0)
        {
            for (int i = 0; i < 9; ++i)
            {
                for (int j = 0; j < 9; ++j)
                {
                    if (validateMove(i, j))
                    {
                        move[0] = i;
                        move[1] = j;
                        moveList.add(move.clone());
                    }
                }
            }
        }
        else
        {
            for (int i = 0; i < 9; ++i)
            {
                if (validateMove(activeBoard, i))
                {
                    move[0] = activeBoard;
                    move[1] = i;
                    moveList.add(move.clone());
                }
            }
        }
        return(moveList);
    }
    
    public void printPrettyFen()
    {
        
        String prettyFen = "";
        for (int i = 0; i < 9; ++i)
        {
            if (i % 3 == 0 && i != 0)
            {
                prettyFen += "\n";
            }
            
            for (int j = 0; j < 9; ++j)
            {
                if (j % 3 == 0 && j != 0)
            {
                prettyFen += "  ";
            }
                if (moveArrayArray[j/3+3*(i/3)][j%3 + (3*i)%9] != null)
                {
                    if (moveArrayArray[j/3+3*(i/3)][j%3 + (3*i)%9])
                    {
                        prettyFen += "x ";
                    }
                    else
                    {
                        prettyFen+= "o ";
                    }
                }
                else
                {
                    prettyFen += "_ ";
                }

            }
            prettyFen += "  \n";
            
        }
        System.out.println(prettyFen);
    }
}
