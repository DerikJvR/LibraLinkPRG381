package form;

import core.classes.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Form_3 extends javax.swing.JPanel {
    private DefaultTableModel tableModel;

    public Form_3() {
        initComponents();
        setOpaque(false);
        BorrowerTable.fixTable(jScrollPane1);
        init();

        // Initialize table model for the existing BorrowerTable
        String[] columns = {"Borrower ID", "Book ID", "User ID", "Borrow Date", "Due Date", "Return Date"};
        tableModel = (DefaultTableModel) BorrowerTable.getModel();
        tableModel.setColumnIdentifiers(columns);

        // Set action listener for the BorrowersRemove button
        btnBorrowersRemove.addActionListener(evt -> btnBorrowersRemoveActionPerformed(evt));

        // Set action listener for the BorrowersSearch button
        btnBorrowersSearch.addActionListener(evt -> performSearch());

        // Set action listener for the BorrowersClear button
        btnBorrowersClear.addActionListener(evt -> btnBorrowersClearActionPerformed(evt));

        // Load data
        loadBorrowerData();
    }
    
    private void init() {
        jScrollPane1 = new javax.swing.JScrollPane();
    }
        
    private void loadBorrowerData() {
        String query = "SELECT b.BORROWER_ID, b.BOOK_ID, b.USER_ID, b.BORROW_DATE, b.DUE_DATE, b.RETURN_DATE " +
                       "FROM BORROWERS b";

        try (Connection connection = DatabaseConnection.getConnection(); // Get a new connection
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) { // Execute query and get results

            tableModel.setRowCount(0); // Clear existing rows
            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getInt("BORROWER_ID"),
                    resultSet.getInt("BOOK_ID"),
                    resultSet.getInt("USER_ID"),
                    resultSet.getDate("BORROW_DATE"),
                    resultSet.getDate("DUE_DATE"),
                    resultSet.getDate("RETURN_DATE")
                };
                tableModel.addRow(row); // Add row to table model
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading borrower data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performSearch() {
        String borrowerID = jBorrowersIDField.getText().trim();
        String bookID = jBookIDField.getText().trim();
        String userID = jUserIDField.getText().trim();
        String borrowDate = jBorrowDateField.getText().trim();
        String dueDate = jDueDateField.getText().trim();
        String returnDate = jReturnDateField.getText().trim();

        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(tableModel);
        BorrowerTable.setRowSorter(rowSorter);

        rowSorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                String entryBorrowerID = entry.getStringValue(0);
                String entryBookID = entry.getStringValue(1);
                String entryUserID = entry.getStringValue(2);
                String entryBorrowDate = entry.getStringValue(3);
                String entryDueDate = entry.getStringValue(4);
                String entryReturnDate = entry.getStringValue(5);

                boolean matchBorrowerID = borrowerID.isEmpty() || entryBorrowerID.contains(borrowerID);
                boolean matchBookID = bookID.isEmpty() || entryBookID.contains(bookID);
                boolean matchUserID = userID.isEmpty() || entryUserID.contains(userID);
                boolean matchBorrowDate = borrowDate.isEmpty() || entryBorrowDate.contains(borrowDate);
                boolean matchDueDate = dueDate.isEmpty() || entryDueDate.contains(dueDate);
                boolean matchReturnDate = returnDate.isEmpty() || entryReturnDate.contains(returnDate);

                return matchBorrowerID && matchBookID && matchUserID && matchBorrowDate && matchDueDate && matchReturnDate;
            }
        });
    }
    private void deleteBorrowerByID(int borrowerID) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();

        try {
            // Start a transaction
            connection.setAutoCommit(false);

            // Delete the borrower from the BORROWERS table
            String deleteBorrowerQuery = "DELETE FROM BORROWERS WHERE BORROWER_ID = ?";
            try (PreparedStatement deleteBorrowerStmt = connection.prepareStatement(deleteBorrowerQuery)) {
                deleteBorrowerStmt.setInt(1, borrowerID);
                deleteBorrowerStmt.executeUpdate();
            }

            // Commit the transaction
            connection.commit();

        } catch (SQLException e) {
            // Rollback the transaction in case of error
            connection.rollback();
            throw new SQLException("Error deleting borrower: " + e.getMessage(), e);
        } finally {
            // Reset auto-commit
            connection.setAutoCommit(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roundPanel1 = new swing.RoundPanel();
        btnBorrowersAdd = new swing.Button();
        btnBorrowersRemove = new swing.Button();
        btnBorrowersUpdate = new swing.Button();
        btnBorrowersSearch = new swing.Button();
        btnBorrowersClear = new swing.Button();
        roundPanel2 = new swing.RoundPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        BorrowerTable = new component.TableDark();
        roundPanel3 = new swing.RoundPanel();
        jLabel4 = new javax.swing.JLabel();
        jBorrowersIDField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jBookIDField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jUserIDField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jDueDateField = new javax.swing.JTextField();
        jBorrowDateField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jReturnDateField = new javax.swing.JTextField();

        roundPanel1.setBackground(new java.awt.Color(51, 51, 51));
        roundPanel1.setPreferredSize(new java.awt.Dimension(229, 248));

        btnBorrowersAdd.setText("Add");
        btnBorrowersAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrowersAddActionPerformed(evt);
            }
        });

        btnBorrowersRemove.setText("Remove");
        btnBorrowersRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrowersRemoveActionPerformed(evt);
            }
        });

        btnBorrowersUpdate.setText("Update");
        btnBorrowersUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrowersUpdateActionPerformed(evt);
            }
        });

        btnBorrowersSearch.setText("Search");

        btnBorrowersClear.setText("Clear");
        btnBorrowersClear.setPreferredSize(new java.awt.Dimension(59, 41));
        btnBorrowersClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrowersClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout roundPanel1Layout = new javax.swing.GroupLayout(roundPanel1);
        roundPanel1.setLayout(roundPanel1Layout);
        roundPanel1Layout.setHorizontalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnBorrowersAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                    .addComponent(btnBorrowersRemove, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                    .addComponent(btnBorrowersUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBorrowersSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBorrowersClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(60, 60, 60))
        );
        roundPanel1Layout.setVerticalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(btnBorrowersAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBorrowersRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBorrowersUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBorrowersSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBorrowersClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        roundPanel2.setBackground(new java.awt.Color(51, 51, 51));

        BorrowerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Borrower_ID", "Book_ID", "User_ID", "Borrow_Date", "Due_Date", "Return_Date"
            }
        ));
        BorrowerTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BorrowerTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(BorrowerTable);

        javax.swing.GroupLayout roundPanel2Layout = new javax.swing.GroupLayout(roundPanel2);
        roundPanel2.setLayout(roundPanel2Layout);
        roundPanel2Layout.setHorizontalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        roundPanel2Layout.setVerticalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                .addContainerGap())
        );

        roundPanel3.setBackground(new java.awt.Color(51, 51, 51));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Borrower_ID");

        jBorrowersIDField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Book_ID");

        jBookIDField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("User_ID");

        jUserIDField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Borrow_Date");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Due_Date");

        jDueDateField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jBorrowDateField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Return_Date");

        javax.swing.GroupLayout roundPanel3Layout = new javax.swing.GroupLayout(roundPanel3);
        roundPanel3.setLayout(roundPanel3Layout);
        roundPanel3Layout.setHorizontalGroup(
            roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(31, 31, 31)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBookIDField, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBorrowersIDField, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jUserIDField, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(25, 25, 25)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jBorrowDateField, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                    .addComponent(jDueDateField)
                    .addComponent(jReturnDateField))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        roundPanel3Layout.setVerticalGroup(
            roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel3Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jBorrowDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBorrowersIDField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(35, 35, 35)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jDueDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBookIDField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(35, 35, 35)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jUserIDField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel3)
                    .addComponent(jReturnDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(roundPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(roundPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                    .addComponent(roundPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(roundPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBorrowersRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrowersRemoveActionPerformed
        int selectedRow = BorrowerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a borrower to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int borrowerID = (int) tableModel.getValueAt(selectedRow, 0); // Get Borrower ID from the selected row

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this borrower?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                connection.setAutoCommit(false);

                // Delete the borrower from the BORROWERS table
                String deleteBorrowerQuery = "DELETE FROM BORROWERS WHERE BORROWER_ID = ?";
                try (PreparedStatement deleteBorrowerStmt = connection.prepareStatement(deleteBorrowerQuery)) {
                    deleteBorrowerStmt.setInt(1, borrowerID);
                    deleteBorrowerStmt.executeUpdate();
                }

                // Commit the transaction
                connection.commit();

                // Remove the row from the table model
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Borrower removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                // Rollback the transaction in case of error
                try {
                    DatabaseConnection.getConnection().rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error removing borrower. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnBorrowersRemoveActionPerformed

    private void btnBorrowersUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrowersUpdateActionPerformed
        int selectedRow = BorrowerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a borrower to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Retrieve values from text fields
        int borrowerID = Integer.parseInt(jBorrowersIDField.getText().trim());
        int bookID = Integer.parseInt(jBookIDField.getText().trim());
        int userID = Integer.parseInt(jUserIDField.getText().trim());
        java.sql.Date borrowDate = java.sql.Date.valueOf(jBorrowDateField.getText().trim());
        java.sql.Date dueDate = java.sql.Date.valueOf(jDueDateField.getText().trim());
        java.sql.Date returnDate = jReturnDateField.getText().trim().isEmpty() ? null : java.sql.Date.valueOf(jReturnDateField.getText().trim());

        // Update database with the new values
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            // Update the BORROWERS table with new values
            String updateBorrowerQuery = "UPDATE BORROWERS SET BOOK_ID = ?, USER_ID = ?, BORROW_DATE = ?, DUE_DATE = ?, RETURN_DATE = ? WHERE BORROWER_ID = ?";
            try (PreparedStatement updateBorrowerStmt = connection.prepareStatement(updateBorrowerQuery)) {
                updateBorrowerStmt.setInt(1, bookID);
                updateBorrowerStmt.setInt(2, userID);
                updateBorrowerStmt.setDate(3, borrowDate);
                updateBorrowerStmt.setDate(4, dueDate);
                updateBorrowerStmt.setDate(5, returnDate);
                updateBorrowerStmt.setInt(6, borrowerID);

                int rowsUpdated = updateBorrowerStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    // Update the table model to reflect the changes
                    tableModel.setValueAt(bookID, selectedRow, 1);
                    tableModel.setValueAt(userID, selectedRow, 2);
                    tableModel.setValueAt(borrowDate, selectedRow, 3);
                    tableModel.setValueAt(dueDate, selectedRow, 4);
                    tableModel.setValueAt(returnDate, selectedRow, 5);

                    // Commit the transaction
                    connection.commit();

                    // Display success message
                    JOptionPane.showMessageDialog(this, "Update successful.", "Update", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Rollback if no rows were updated
                    connection.rollback();
                    JOptionPane.showMessageDialog(this, "No borrower found with the provided ID.", "Update Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                connection.rollback(); // Rollback transaction on failure
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating borrower details. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBorrowersUpdateActionPerformed

    private void BorrowerTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BorrowerTableMouseClicked
        int selectedRow = BorrowerTable.getSelectedRow(); // Get the selected row index
        if (selectedRow != -1) {
            // Retrieve values from the selected row using the table model
            int borrowerID = (int) tableModel.getValueAt(selectedRow, 0);
            int bookID = (int) tableModel.getValueAt(selectedRow, 1);
            int userID = (int) tableModel.getValueAt(selectedRow, 2);
            java.sql.Date borrowDate = (java.sql.Date) tableModel.getValueAt(selectedRow, 3);
            java.sql.Date dueDate = (java.sql.Date) tableModel.getValueAt(selectedRow, 4);
            java.sql.Date returnDate = (java.sql.Date) tableModel.getValueAt(selectedRow, 5);

            // Populate text fields
            jBorrowersIDField.setText(String.valueOf(borrowerID));
            jBookIDField.setText(String.valueOf(bookID));
            jUserIDField.setText(String.valueOf(userID));
            jBorrowDateField.setText(borrowDate != null ? borrowDate.toString() : "");
            jDueDateField.setText(dueDate != null ? dueDate.toString() : "");
            jReturnDateField.setText(returnDate != null ? returnDate.toString() : "");
        }
    }//GEN-LAST:event_BorrowerTableMouseClicked

    private void btnBorrowersClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrowersClearActionPerformed
         // Clear all text fields
        jBorrowersIDField.setText("");
        jBookIDField.setText("");
        jUserIDField.setText("");
        jBorrowDateField.setText("");
        jDueDateField.setText("");
        jReturnDateField.setText("");

        // Optionally, clear any filters applied to the table
        TableRowSorter<DefaultTableModel> rowSorter = (TableRowSorter<DefaultTableModel>) BorrowerTable.getRowSorter();
        if (rowSorter != null) {
            rowSorter.setRowFilter(null);
        }
    }//GEN-LAST:event_btnBorrowersClearActionPerformed

    private void btnBorrowersAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrowersAddActionPerformed
        // Retrieve text from fields
        String borrowerIDText = jBorrowersIDField.getText().trim();
        String bookIDText = jBookIDField.getText().trim();
        String userIDText = jUserIDField.getText().trim();
        String borrowDateText = jBorrowDateField.getText().trim();
        String dueDateText = jDueDateField.getText().trim();
        String returnDateText = jReturnDateField.getText().trim();

        // Validate fields
        if (borrowerIDText.isEmpty() || bookIDText.isEmpty() ||
            userIDText.isEmpty() || borrowDateText.isEmpty() || dueDateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Convert text fields to integers
            int borrowerID = Integer.parseInt(borrowerIDText);
            int bookID = Integer.parseInt(bookIDText);
            int userID = Integer.parseInt(userIDText);

            // Convert date strings to java.sql.Date
            String dateFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

            java.util.Date borrowDate = sdf.parse(borrowDateText);
            java.util.Date dueDate = sdf.parse(dueDateText);
            java.util.Date returnDate = returnDateText.isEmpty() ? null : sdf.parse(returnDateText);

            // Convert java.util.Date to java.sql.Date
            java.sql.Date sqlBorrowDate = new java.sql.Date(borrowDate.getTime());
            java.sql.Date sqlDueDate = new java.sql.Date(dueDate.getTime());
            java.sql.Date sqlReturnDate = returnDate != null ? new java.sql.Date(returnDate.getTime()) : null;

            // Add the borrower
            DatabaseConnection.addBorrower( bookID, userID, sqlBorrowDate, sqlDueDate, sqlReturnDate);

            // Clear fields after successful addition
            jBorrowersIDField.setText("");
            jBookIDField.setText("");
            jUserIDField.setText("");
            jBorrowDateField.setText("");
            jDueDateField.setText("");
            jReturnDateField.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for IDs.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBorrowersAddActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private component.TableDark BorrowerTable;
    private swing.Button btnBorrowersAdd;
    private swing.Button btnBorrowersClear;
    private swing.Button btnBorrowersRemove;
    private swing.Button btnBorrowersSearch;
    private swing.Button btnBorrowersUpdate;
    private javax.swing.JTextField jBookIDField;
    private javax.swing.JTextField jBorrowDateField;
    private javax.swing.JTextField jBorrowersIDField;
    private javax.swing.JTextField jDueDateField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField jReturnDateField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jUserIDField;
    private swing.RoundPanel roundPanel1;
    private swing.RoundPanel roundPanel2;
    private swing.RoundPanel roundPanel3;
    // End of variables declaration//GEN-END:variables
}
