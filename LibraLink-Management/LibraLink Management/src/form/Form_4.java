package form;

import core.classes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Form_4 extends javax.swing.JPanel {
    private DefaultTableModel tableModel;

    public Form_4() {
        initComponents();
        setOpaque(false);
        OverdueTable.fixTable(jScrollPane1);
        init();

        // Initialize table model for the overdue books table
        String[] columns = {"Borrower ID", "Book ID", "Email", "Title", "Borrow Date","Due Date", "Days Overdue"};
        tableModel = (DefaultTableModel) OverdueTable.getModel();
        tableModel.setColumnIdentifiers(columns);

        // Load data
        loadOverdueBooksData();
    }

    private void init() {
        jScrollPane1 = new javax.swing.JScrollPane();
    }

     private void loadOverdueBooksData() {
        try {
            ResultSet resultSet = OverdueBooksFetcher.getOverdueBooks();
            tableModel.setRowCount(0); // Clear existing rows
            while (resultSet.next()) {
                int borrowerId = resultSet.getInt("BORROWER_ID");
                int bookId = resultSet.getInt("BOOK_ID");
                String email = resultSet.getString("EMAIL");
                String title = resultSet.getString("TITLE");
                java.sql.Date borrowDate = resultSet.getDate("BORROW_DATE");
                java.sql.Date dueDate = resultSet.getDate("DUE_DATE");

                // Calculate days overdue
                long daysOverdue = OverdueBooksFetcher.calculateDaysOverdue(dueDate);

                Object[] row = {
                    borrowerId,
                    bookId,
                    email,
                    title,
                    borrowDate,
                    dueDate,
                    daysOverdue
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading overdue books data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roundPanel2 = new swing.RoundPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        OverdueTable = new component.TableDark();

        roundPanel2.setBackground(new java.awt.Color(51, 51, 51));

        OverdueTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Borrower_ID", "Book_ID", "Email", "Title", "Borrow_Date", "Due_Date", "Days_Overdue"
            }
        ));
        jScrollPane1.setViewportView(OverdueTable);

        javax.swing.GroupLayout roundPanel2Layout = new javax.swing.GroupLayout(roundPanel2);
        roundPanel2.setLayout(roundPanel2Layout);
        roundPanel2Layout.setHorizontalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1093, Short.MAX_VALUE)
                .addContainerGap())
        );
        roundPanel2Layout.setVerticalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private component.TableDark OverdueTable;
    private javax.swing.JScrollPane jScrollPane1;
    private swing.RoundPanel roundPanel2;
    // End of variables declaration//GEN-END:variables
}
