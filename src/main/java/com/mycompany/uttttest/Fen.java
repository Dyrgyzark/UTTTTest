/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uttttest;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    
    /**
     * Initializes Fen class with string input for fen notation
     * should probably add a default Fen that it the starting board
     * @param fenInput
     */
    public Fen(String fenInput)
    {
        List<String> fenInputs = split(fenInput, " ");
        placementData = fenInputs.get(0);
        activePlayer = fenInputs.get(1).equals("x");
        activeBoard = Integer.parseInt(fenInputs.get(2));
        boardList = split(fenInputs.get(0), "/");
        
        
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
            try
            {
                arrIndex += Integer.parseInt(boardState.substring(i,i + 1));
            } 
            catch(NumberFormatException NumberFormatException)
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
    
    // checks if a move (majPos, minPos) is able to be played
    // given the current board state fen
    // if minPos = -1, we're testing if you can play anywhere in the majPos board
    private boolean validateMove(int majPos, int minPos)
    {
        String boardState = boardList.get(majPos);
        Boolean[] moveArray = getMoveArray(boardState);
        boolean valid = true;
        // testing if player can currently play in majPos board 
        // and if there's already a move played there
        if (minPos >= 0)
        {
            valid = ((activeBoard < 0) || (majPos == activeBoard)) && (moveArray[minPos] == null);
        }
        
        // testing if there is a win on the diagonals and if the board is full
        // 0, 4, 8 is the main diagonal
        // 2, 4, 6 is the Anti-diagonal
        if (
                (Objects.equals(moveArray[0], moveArray[4]) && Objects.equals(moveArray[4], moveArray[8])) && !Objects.equals(moveArray[0], null) ||
                (Objects.equals(moveArray[2], moveArray[4]) && Objects.equals(moveArray[4], moveArray[6])) && !Objects.equals(moveArray[2], null) ||
                (moveArray[0] != null && moveArray[1] != null && moveArray[2] != null && 
                moveArray[3] != null && moveArray[4] != null && moveArray[5] != null && 
                moveArray[6] != null && moveArray[7] != null && moveArray[8] != null)
            )
        {
            valid = false;
        }
        // checks for each orthogonal win
        for (int i = 0; i < 3 && valid; ++i)
            {
                if ( 
                        (Objects.equals(moveArray[3*i], moveArray[3*i+1]) && Objects.equals(moveArray[3*i+1], moveArray[3*i+2])) && !Objects.equals(moveArray[3*i], null) ||
                        (Objects.equals(moveArray[i], moveArray[i+3]) && Objects.equals(moveArray[i+3], moveArray[i+6])) && !Objects.equals(moveArray[i], null)           
                    )
                {
                    valid = false;
                }
            }
        return valid;
    }
    
    public void addMove(int majPos, int minPos)
    {
        minPos--; majPos--;
        String boardState = boardList.get(majPos);
        Boolean[] moveArray = getMoveArray(boardState);
        if (validateMove(majPos, minPos))
        {
            moveArray[minPos] = activePlayer;
            boardList.set(majPos, moveArrayToBoardState(moveArray));
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
    
    public void printPrettyFen()
    {
        String[] fullBoardState = new String[9];
        Boolean[][] fullMoveArray = new Boolean[9][9];
        for (int i = 0; i < 9; ++i)
        {
            fullBoardState[i] = boardList.get(i);
            fullMoveArray[i] = getMoveArray(fullBoardState[i]);
        }
        
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
                if (fullMoveArray[j/3+3*(i/3)][j%3 + (3*i)%9] != null)
                {
                    if (fullMoveArray[j/3+3*(i/3)][j%3 + (3*i)%9])
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
