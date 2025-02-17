/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.uttttest;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
public class UTTTTest {
    static Scanner sc = new Scanner(System.in);
    
    private static int[] genRandomMove(Fen fen)
    {
        Random rand = new Random();
        List<int[]> moveList = fen.getMoveList();
        return(moveList.get(rand.nextInt(0, moveList.size())));
    }
    
    private static void playRandomGame(String[] game)
    {
        int[] move;
        Fen fen = new Fen("9/9/9/9/9/9/9/9/9 x -");
            while (!fen.getMoveList().isEmpty())
            {
               move = genRandomMove(fen);
                fen.addMove(move[0], move[1]);
                game[0] += Arrays.toString(move) + " ";
            }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String[] game = new String[1];
//        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("Games.txt")), true));
//        for (int i = 0; i < 10000000; ++i)
//        {
//            game[0] = "";
//            playRandomGame(game);
//            System.out.println(game[0] + "\n");
//        }
//        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
//        while (true)
//        {
//            String inputFen = sc.nextLine();
//            Fen fen = new Fen(inputFen);
//            fen.printPrettyFen();
//        }

        int[] moves = new int[2];
        Fen fen = new Fen("9/9/9/9/9/9/9/9/9 x -");
        System.out.println(fen.getFen());
        fen.printPrettyFen();
        while (true)
        {
            moves[0] = Integer.parseInt(sc.nextLine());
            moves[1] = Integer.parseInt(sc.nextLine());
            fen.addMove(moves[0] - 1, moves[1] - 1);
            System.out.println(fen.getFen());
            fen.printPrettyFen();
        }
    }
}
