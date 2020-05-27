/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psp_16;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Миша Карнилов
 */
public class Main_class extends JFrame {

    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel model_dialog = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int j, int i) {
            return false;
        }
    };
    JTable t1 = new JTable(model);
    JScrollPane jscrlp = new JScrollPane(t1);
    JMenuBar menuBar = new JMenuBar();
    JButton replace = new JButton("Сменить сервер");
    JButton help = new JButton("Помощь");
    JButton enter = new JButton("ENTER");
    JTextField custom = new JTextField();
    private String log;
    private String pass;
    private String URL;
    ArrayList< JMenuItem> array = new ArrayList< JMenuItem>();
    ArrayList<String[]> buffer = new ArrayList<String[]>();
    int table_number;
    String bd_name;
    Ground f2;

    public Main_class(String s) {
        super(s);
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main_class.class.getName()).log(Level.SEVERE, null, ex);
        }
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension dim = e.getComponent().getSize();
                jscrlp.setPreferredSize(new Dimension(dim.width - 50, dim.height - 150));
                custom.setPreferredSize(new Dimension(dim.width - 150, 40));
            }
        });
        help.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0
            ) {
                if (arg0.getSource() == help) {
                    if (model.getRowCount() != 0) {
                        Help f1 = new Help("Помощь"); //Название формы        
                        f1.setVisible(true);//Видимость формы 
                        f1.Set_URL(URL);
                        f1.Set_bd_name(bd_name);
                        f1.Set_tabel_name(array.get(table_number).getText());
                        f1.Set_log(log);
                        f1.Set_pass(pass);
                        f1.get_column_name();
                        f1.setSize(400, 400);//Размеры окна
                        // f.setResizable(false);//Масштабируемость
                        f1.setLocationRelativeTo(null);//Установка окнна пе центру 

                    } else {
                        JOptionPane.showMessageDialog(null, "Сначала выберите таблицу");
                    }
                }

            }
        }
        );
        replace.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0
            ) {
                if (arg0.getSource() == replace) {

                    f2.setVisible(true);//Видимость формы
                    f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Действие при на жатии на крестик (закрытие программы)
                    f2.setSize(300, 300);//Размеры окна
                    f2.setResizable(false);//Масштабируемость
                    f2.setLocationRelativeTo(null);//Установка окнна пе центру                     
                }

            }
        }
        );
        enter.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0
            ) {
                if (arg0.getSource() == enter) {

                    try (Connection connection2 = DriverManager.getConnection(URL, log, pass); Statement statement2 = connection2.createStatement()) {
                        String[] time = custom.getText().split(" ");

                        if (time[0].equalsIgnoreCase("UPDATE") || time[0].equalsIgnoreCase("DELETE") || time[0].equalsIgnoreCase("INSERT")) {
                            statement2.executeUpdate(custom.getText());
                            statement2.close();
                            connection2.close();
                            zapr(array.get(table_number).getText(), 0);
                        } else {
                            zapr(custom.getText(), 1);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Ошибка в вводе запроса");
                        Logger.getLogger(Main_class.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }
        );
        setLayout(new FlowLayout());
        t1.setShowGrid(false);
        t1.setShowVerticalLines(false);
        t1.setShowHorizontalLines(false);
        t1.getTableHeader().setReorderingAllowed(false);
        jscrlp.setPreferredSize(new Dimension(550, 450));
        custom.setPreferredSize(new Dimension(450, 40));
        help.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        replace.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        replace.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        custom.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        enter.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        menuBar.add(FileMenu());
        add(menuBar);
        add(Updatebutton());
        add(help);
        add(replace);
        add(jscrlp);
        add(custom);
        add(enter);

    }

    protected void set_URL(String s) {
        this.URL = s;
    }

    protected void set_bd_name(String s) {
        this.bd_name = s;
    }

    protected void set_login(String login) {
        this.log = login;
    }

    protected void set_password(String password) {
        this.pass = password;
    }

    protected void setObjectGround(Ground f) {
        this.f2 = f;
    }

    public void zapr(String param, int mode) {
        buffer.clear();
        try (Connection connection2 = DriverManager.getConnection(URL, log, pass); Statement statement2 = connection2.createStatement()) {
            ResultSet rss = null;

            if (mode == 0) {
                System.out.println("SELECT * FROM " + param);
                rss = statement2.executeQuery("SELECT * FROM " + param);
                setTitle(bd_name + " — " + param);
            } else {
                System.out.println(param);
                rss = statement2.executeQuery(param);
            }
            ResultSetMetaData rsmd = rss.getMetaData();
            model.setColumnCount(0);
            model.getDataVector().clear();
            model_dialog.setColumnCount(0);
            model_dialog.getDataVector().clear();

            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                model.addColumn(rsmd.getColumnName(i));
                model_dialog.addColumn(rsmd.getColumnName(i));
            }

            while (rss.next()) {
                String[] tempstr = new String[rsmd.getColumnCount()];
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    tempstr[i - 1] = rss.getString(i);
                }
                buffer.add(tempstr);
                model.addRow(tempstr);
            }
            rss.close();
            statement2.close();
            connection2.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ошибка в вводе запроса");
            Logger.getLogger(Main_class.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JMenu FileMenu() {
        // Создание выпадающего меню
        JMenu file = new JMenu("Файл");
        JMenu open = new JMenu("Открыть");
        JMenuItem add = new JMenuItem("Добавить строку");
        add.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0
            ) {
                if (arg0.getSource() == add) {
                    if (model.getRowCount() != 0) {
                        Add f = new Add("Добавить"); //Название формы  
                        f.Set_URL(URL);
                        f.Set_bd_name(bd_name);
                        f.Set_tabel_name(array.get(table_number).getText());
                        f.Set_pass(pass);
                        f.Set_log(log);
                        f.Set_max_index(max_index());
                        f.get_column_name();
                        f.setVisible(true);//Видимость формы
                        // f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Действие при на жатии на крестик (закрытие программы)
                        f.setSize(600, 300);//Размеры окна
                        // f.setResizable(false);//Масштабируемость
                        f.setLocationRelativeTo(null);//Установка окнна пе центру 
                    } else {
                        JOptionPane.showMessageDialog(null, "Выберите таблицу для добавления");
                    }
                }

            }
        }
        );
        open.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                open.removeAll();
                array.clear();
                try (Connection connection = DriverManager.getConnection(URL, log, pass); Statement statement = connection.createStatement()) {
                    DatabaseMetaData md = connection.getMetaData();
                    ResultSet rs = md.getTables(null, null, "%", null);

                    int i = 0;
                    while (rs.next()) {
                        array.add(new JMenuItem(rs.getString(3)));
                        open.add(array.get(i));
                        array.get(i).addActionListener(
                                new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent arg0
                            ) {

                                for (int i = 0; i < array.size(); i++) {
                                    if (arg0.getSource().equals(array.get(i))) {
                                        table_number = i;
                                        zapr(array.get(i).getText(), 0);

                                    }
                                }

                            }
                        }
                        );
                        i++;
                    }
                    rs.close();
                    statement.close();
                    connection.close();
                } catch (Exception EX) {
                    EX.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Не удалось подключиться к серверу");
                }
            }
        });
        file.add(open);
        file.add(add);
        return file;
    }

    private long max_index() {
        long max = 0;
        try {
            for (int i = 0; i < model.getRowCount(); i++) {
                if (max < Long.parseLong(model.getValueAt(i, 0).toString())) {
                    max = Long.parseLong(model.getValueAt(i, 0).toString());
                }
            }
        } catch (Exception ex) {
        }
        return max;
    }

    private JButton Updatebutton() {
        JButton update = new JButton("Обновить");
        update.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        update.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (arg0.getSource() == update) {
                    model_dialog.getDataVector().clear();
                    for (int i = 0; i < buffer.size(); i++) {
                        for (int j = 0; j < model.getColumnCount(); j++) {
                            if (!buffer.get(i)[j].equals(model.getValueAt(i, j))) {
                                String[] temp_row = new String[model.getColumnCount()];
                                for (int k = 0; k < model.getColumnCount(); k++) {
                                    temp_row[k] = t1.getValueAt(i, k).toString();
                                }
                                model_dialog.addRow(temp_row);
                                System.out.println("done");
                                break;
                            } else {
                            }
                        }
                    }
                    if (model.getRowCount() != 0) {
                        int result = JOptionPane.showOptionDialog(
                                null,
                                "Вы действительно хотите внести данные изменения ?",
                                "Тут могла быть ваша реклама ☺",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                new Object[]{"Да", "Нет", new JScrollPane(new JTable(model_dialog))},
                                "Да");
                        if (result == JOptionPane.YES_OPTION) {
                            Delete_and_Update();
                        }
                        if (result == JOptionPane.NO_OPTION) {
                            zapr(array.get(table_number).getText(), 0);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Сначала откойте таблицу");
                    }
                }
            }
        }
        );
        return update;
    }

    private void Delete_and_Update() {

        try (Connection connection2 = DriverManager.getConnection(URL, log, pass); Statement statement2 = connection2.createStatement()) {
            for (int i = 0; i < buffer.size(); i++) {
                int counter = 0;
                String query = "UPDATE " + bd_name + "." + array.get(table_number).getText() + " SET ";
                for (int j = 0; j < model.getColumnCount(); j++) {
                    if (!buffer.get(i)[j].equals(model.getValueAt(i, j))) {
                        query += model.getColumnName(j) + "=" + "'" + model.getValueAt(i, j) + "'" + ", ";
                        counter++;
                    } else {
                    }
                }
                if (counter != 0) {
                    query = query.substring(0, query.length() - 2);
                    query += " WHERE " + model.getColumnName(0) + "=" + model.getValueAt(i, 0);
                    if (model.getValueAt(i, 0).toString().isEmpty()) {
                        query = "DELETE FROM " + bd_name + "." + array.get(table_number).getText() + " WHERE " + model.getColumnName(0) + " = '" + buffer.get(i)[0] + "'";
                    }
                    System.out.println(query);
                    statement2.executeUpdate(query);
                }

            }
            statement2.close();
            connection2.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ошибка в вводе данных");
            Logger.getLogger(Main_class.class.getName()).log(Level.SEVERE, null, ex);
        }
        zapr(array.get(table_number).getText(), 0);
    }
}
