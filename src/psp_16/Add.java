/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psp_16;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Rasinshmek
 */
public class Add extends JFrame {

    DefaultTableModel model = new DefaultTableModel() {
//        @Override
//        public boolean isCellEditable(int j, int i) {
//            return (i == 1 && j == model.getRowCount());
//        }
    };
    JTable t1 = new JTable(model);
    JScrollPane jscrlp = new JScrollPane(t1);
    JButton add = new JButton("Добавить");
    private String bd_name;
    private String table_name;
    private String URL;
    private String log;
    private String pass;
    long max_index;

    public Add(String s) {
        super(s);
        ;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main_class.class.getName()).log(Level.SEVERE, null, ex);
        }
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension dim = e.getComponent().getSize();
                jscrlp.setPreferredSize(new Dimension(dim.width - 50, dim.height - 100));
            }
        });
        add.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0
            ) {
                if (arg0.getSource() == add) {
                    add_row();
                }

            }
        }
        );
        getContentPane().setLayout(new FlowLayout());
        jscrlp.setPreferredSize(new Dimension(600 - 50, 200 - 50));
        add.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        t1.getTableHeader().setReorderingAllowed(false);
        add(jscrlp);
        add(add);

    }

    protected void Set_bd_name(String bd_name) {
        this.bd_name = bd_name;

    }

    protected void Set_URL(String s) {
        this.URL = s;
    }

    protected void Set_tabel_name(String table_name) {
        this.table_name = table_name;
    }

    protected void Set_log(String log) {
        this.log = log;
    }

    protected void Set_pass(String pass) {
        this.pass = pass;
    }

    protected void Set_max_index(long s) {
        this.max_index = s;
    }

    protected void get_column_name() {
        try (Connection connection3 = DriverManager.getConnection(URL, log, pass); Statement statement3 = connection3.createStatement()) {
            ResultSet rss = statement3.executeQuery("SELECT * FROM " + bd_name + "." + table_name);
            ResultSetMetaData rsmd = rss.getMetaData();
            model.setColumnCount(0);
            model.getDataVector().clear();
            model.addRow(new Vector());
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                model.addColumn(rsmd.getColumnName(i));
            }
            rss.close();
            statement3.close();
            connection3.close();
            model.setValueAt(max_index+1, 0, 0);
        } catch (SQLException ex) {
            Logger.getLogger(Main_class.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Что-то пошло не так ");
        }
    }

    protected void add_row() {
        t1.getSelectionModel().clearSelection();
        try (Connection connection3 = DriverManager.getConnection(URL, log, pass); Statement statement3 = connection3.createStatement()) {
            String query = "INSERT " + bd_name + "." + table_name + " (";
            int ii = 0;
            int jj = 0;
            try {
                String s = model.getValueAt(0, 0).toString();
            } catch (Exception ex) {
                ii++;
                jj++;

            }
            for (int i = ii; i < model.getColumnCount(); i++) {

                query += model.getColumnName(i) + ", ";

            }
            query = query.substring(0, query.length() - 2);
            query += ") VALUES (";
            for (int j = jj; j < model.getColumnCount(); j++) {

                query += "'" + model.getValueAt(0, j) + "', ";

            }
            query = query.substring(0, query.length() - 2);
            query += ")";

            // query = query.substring(0, query.length() - 2);
            System.out.println(query);
            statement3.executeUpdate(query);
            statement3.close();
            connection3.close();
        } catch (SQLException ex) {
            Logger.getLogger(Main_class.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Введите корректные данные ");
        }

    }
}
