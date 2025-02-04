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
    
    public Fen(String fenInput)
    {
        List<String> fenInputs = split(fenInput, " ");
        placementData = fenInputs.get(0);
        activePlayer = fenInputs.get(1).equals("x");
        activeBoard = Integer.parseInt(fenInputs.get(2));
        boardList = split(fenInputs.get(0), "/");
        
        
    }
    
    private static List<String> split(String str, String delimiter)
    {
    return Stream.of(str.split(delimiter))
      .map (elem -> elem)
      .collect(Collectors.toList());
    }
    
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
    
    private String moveArrayToBoardState(Boolean[] moveArray)
    {
        int num = 0;
        String boardState = "";
        for (int i = 0; i < 9; ++i)
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
    
    private boolean validateMove(int majPos, int minPos)
    {
        String boardState = boardList.get(majPos);
        Boolean[] moveArray = getMoveArray(boardState);
        boolean valid = true;
        if (minPos >= 0)
        {
            valid = ((activeBoard < 0) || (majPos == activeBoard)) && (moveArray[minPos] == null);
        }
        for (int i = 0; i < 3; ++i)
            {
                if (
                        (moveArray[3*i] != null && moveArray[3*i+1] != null && moveArray[3*i+2] != null &&
                        moveArray[3*i] == moveArray[3*i+1] && moveArray[3*i+1] == moveArray[3*i+2])     ||
                        (moveArray[i] != null && moveArray[i+3] != null && moveArray[i+6] != null       &&
                        moveArray[i] == moveArray[i+3] && moveArray[i+3] == moveArray[i+6])             ||
                        (moveArray[0] != null && moveArray[4] != null && moveArray[8] != null           &&
                        moveArray[0] == moveArray[4] && moveArray[4] == moveArray[8])                   ||
                        (moveArray[2] != null && moveArray[4] != null && moveArray[6] != null           &&
                        moveArray[2] == moveArray[4] && moveArray[4] == moveArray[6])                   ||
                        (moveArray[0] != null && moveArray[1] != null && moveArray[2] != null && 
                        moveArray[3] != null && moveArray[4] != null && moveArray[5] != null && 
                        moveArray[6] != null && moveArray[7] != null && moveArray[8] != null)
                    )
                {
                    valid = false;
                    break;
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
}
