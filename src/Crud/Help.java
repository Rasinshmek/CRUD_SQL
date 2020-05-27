/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crud;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Rasinshmek
 */
public class Help extends JFrame {

    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int j, int i) {
            return false;
        }
    };
    JTable t1 = new JTable(model);
    JScrollPane jscrlp = new JScrollPane(t1);
    private String bd_name;
    private String table_name;
    private String URL;
    private String log;
    private String pass;

    public Help(String s) {
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
                jscrlp.setPreferredSize(new Dimension(dim.width - 50, dim.height - 50));
            }
        });
        jscrlp.setPreferredSize(new Dimension(400 - 50, 400 - 50));
        t1.getTableHeader().setReorderingAllowed(false);
        add(jscrlp);

    }

    protected void Set_bd_name(String bd_name) {
        this.bd_name = bd_name;
    }

    protected void Set_tabel_name(String table_name) {
        this.table_name = table_name;
    }

    protected void Set_URL(String s) {
        this.URL = s;
    }

    protected void Set_log(String log) {
        this.log = log;
    }

    protected void Set_pass(String pass) {
        this.pass = pass;
    }

    protected void get_column_name() {
        try (Connection connection2 = DriverManager.getConnection(URL, log, pass); Statement statement2 = connection2.createStatement()) {
            System.out.println("DESCRIBE " + table_name);
            ResultSet rss = statement2.executeQuery("DESCRIBE " + table_name);
            ResultSetMetaData rsmd = rss.getMetaData();
            model.setColumnCount(0);
            model.getDataVector().clear();

            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                model.addColumn(rsmd.getColumnName(i));
            }
            String[] tempstr = new String[rsmd.getColumnCount()];
            while (rss.next()) {
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    tempstr[i - 1] = rss.getString(i);
                }
                model.addRow(tempstr);
            }
            rss.close();
            statement2.close();
            connection2.close();
        } catch (SQLException ex) {
            Logger.getLogger(Main_class.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
