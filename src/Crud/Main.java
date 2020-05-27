/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crud;

import javax.swing.JFrame;

/**
 *
 * @author alpha
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Main_class f = new Main_class(""); //Название формы        
        f.setVisible(true);//Видимость формы
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Действие при на жатии на крестик (закрытие программы)
        f.setSize(600, 600);//Размеры окна
       // f.setResizable(false);//Масштабируемость
        f.setLocationRelativeTo(null);//Установка окнна пе центру 
        f.setEnabled(false);
        Ground f2 = new Ground("Авторизация"); //Название формы        
        f2.setVisible(true);//Видимость формы
        f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Действие при на жатии на крестик (закрытие программы)
        f2.setSize(300, 300);//Размеры окна
        f2.setResizable(false);//Масштабируемость
        f2.setLocationRelativeTo(null);//Установка окнна пе центру 
        f2.setObjectMain(f);
        f.setObjectGround(f2);

    }

}
