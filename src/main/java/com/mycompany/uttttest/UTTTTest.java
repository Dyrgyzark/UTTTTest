/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.uttttest;

import java.util.Scanner;
public class UTTTTest {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Fen fen = new Fen("9/9/9/9/9/9/9/9/9 x -1");
        while (true) {
            int[] moves = {Integer.parseInt(sc.nextLine()), Integer.parseInt(sc.nextLine())};
            fen.addMove(moves[0], moves[1]);
            System.out.println(fen.getFen());
            fen.printPrettyFen();
        }
    }
}
