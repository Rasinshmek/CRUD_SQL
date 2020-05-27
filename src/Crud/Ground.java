/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crud;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Миша Карнилов
 */
public class Ground extends JFrame {

    Handler handler = new Handler();
    JButton login = new JButton("Войти");
    JLabel login_text = new JLabel("Имя пользователя");
    JLabel password_text = new JLabel("Пароль                 ");
    JLabel bd_name_text = new JLabel("Имя базы данных ");
    JLabel IP_text = new JLabel("IP сервера            ");
    JTextField login_field = new JTextField();
    JTextField bd_name_field = new JTextField();
    JTextField IP_field = new JTextField();
    JPasswordField password_field = new JPasswordField();
    Main_class f;

    public Ground(String s) {
        super(s);
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main_class.class.getName()).log(Level.SEVERE, null, ex);
        }
        setLayout(new FlowLayout());
        login.addActionListener(handler);
        getContentPane().setLayout(new FlowLayout());
        login.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        login_text.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        password_text.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        IP_text.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        login_field.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        bd_name_field.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        bd_name_text.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        IP_field.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        password_field.setEchoChar('☼');
        password_field.setPreferredSize(new Dimension(100, 30));
        login_field.setPreferredSize(new Dimension(100, 30));
        bd_name_field.setPreferredSize(new Dimension(100, 30));
        IP_field.setPreferredSize(new Dimension(100, 30));
        add(login_text);
        add(login_field);
        add(password_text);
        add(password_field);
        add(bd_name_text);
        add(bd_name_field);
        add(IP_text);
        add(IP_field);
        add(login);
    }

    protected void setObjectMain(Main_class o) {
        this.f = o;
    }

    private class Handler implements ActionListener {//Класс слушателя      

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == login) {

                String URL = "jdbc:mysql://" + IP_field.getText() + ":3306/" + bd_name_field.getText() + "?autoReconnect=true&useSSL=false";
                if (IP_field.getText().isEmpty() || bd_name_field.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Возможно вы не ввели IP или имя базы");
                } else {
                    try (Connection connection = DriverManager.getConnection(URL, login_field.getText(), new String(password_field.getPassword())); Statement statement = connection.createStatement()) {
                        f.setTitle(bd_name_field.getText()); //Название формы 
                        f.set_URL(URL);
                        f.set_bd_name(bd_name_field.getText());
                        f.set_login(login_field.getText());
                        f.set_password(new String(password_field.getPassword()));
                        f.setVisible(true);//Видимость формы
                        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Действие при на жатии на крестик (закрытие программы)
                        f.setSize(600, 600);//Размеры окна
                        //  f.setResizable(false);//Масштабируемость
                        f.setLocationRelativeTo(null);//Установка окнна пе центру 
                        dispose();
                        f.setEnabled(true);
                        connection.close();

                    } catch (Exception EX) {
                        EX.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Не удалось подключиться к базе данных " + bd_name_field.getText());
                    }

                }
            }

        }
    }
}
