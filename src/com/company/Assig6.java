package com.company;

/**
 * Project Members: Ericka Koyama, Holly Stephens, Ngoc Tran Do
 * CST 338 Software Design Assignment 6 - Timed Build Game
 */

public class Assig6 {

   public static void main(String[] args) {
      // Assemble all the pieces of the MVC
      Model m = new Model();
      View v = new View();
      Controller c = new Controller(m, v);
      c.initController();
   }

}