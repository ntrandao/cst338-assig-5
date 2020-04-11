package com.company;

public class Model {
   /**
    * booleans to track who goes first each turn
    */
   static boolean computerWin;
   static boolean humanWin;

   public static boolean isComputerWin() {
      return computerWin;
   }

   public static void setComputerWin(boolean computerWin) {
      Model.computerWin = computerWin;
   }

   public static boolean isHumanWin() {
      return humanWin;
   }

   public static void setHumanWin(boolean humanWin) {
      Model.humanWin = humanWin;
   }
}
