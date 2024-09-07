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

public class Form_2 extends javax.swing.JPanel {
    private DefaultTableModel tableModel;
    private boolean isLoadingData = false; // Flag to indicate table is being loaded

    public Form_2() {
        initComponents();
        setOpaque(false);
        BookTable.fixTable(jScrollPane1);
        init();

        // Initialize table model for the existing BookTable
        String[] columns = {"Book ID", "Title", "Author", "ISBN", "Published Year", "Copies Available", "Category"};
        tableModel = (DefaultTableModel) BookTable.getModel();
        tableModel.setColumnIdentifiers(columns);

        // Set action listener for the BooksRemove button
        btnBooksRemove.addActionListener(evt -> btnBooksRemoveActionPerformed(evt));

        // Set action listener for the BooksSearch button
        btnBooksSearch.addActionListener(evt -> performSearch());

        // Load data
        loadBookData();
    }

    private void init() {
        jScrollPane1 = new javax.swing.JScrollPane();
    }

    private void loadBookData() {
        isLoadingData = true; // Set the flag to true to avoid search while loading data

        try {
            // Query to join Books and Authors tables
            String query = "SELECT b.BOOK_ID, b.TITLE, a.NAME, a.SURNAME, b.ISBN, b.PUBLISHED_YEAR, b.COPIES_AVAILABLE, b.CATEGORY "
                         + "FROM BOOKS b "
                         + "JOIN AUTHORS a ON b.AUTHOR_ID = a.AUTHOR_ID";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows
            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getInt("BOOK_ID"),
                    resultSet.getString("TITLE"),
                    resultSet.getString("NAME") + " " + resultSet.getString("SURNAME"), // Concatenate name and surname
                    resultSet.getString("ISBN"),
                    resultSet.getInt("PUBLISHED_YEAR"),
                    resultSet.getInt("COPIES_AVAILABLE"),
                    resultSet.getString("CATEGORY")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading book data.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            isLoadingData = false; // Reset the flag after loading data
        }
    }

    private void performSearch() {
        if (isLoadingData) return; // Skip search if the table is being loaded

        String bookID = jBookIDField.getText().trim().toLowerCase();
        String title = jTitlefield.getText().trim().toLowerCase();
        String authorName = jAuthorNameField.getText().trim().toLowerCase();
        String authorSurname = jAuthorSurnameField.getText().trim().toLowerCase();
        String isbn = jISBNField.getText().trim().toLowerCase();
        String publishedYear = jPublishedYearField.getText().trim();
        String copies = jCopiesField.getText().trim();
        String category = jCategoryField.getText().trim().toLowerCase();

        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(tableModel);
        BookTable.setRowSorter(rowSorter);

        rowSorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                String entryBookID = entry.getStringValue(0); // Book ID
                String entryTitle = entry.getStringValue(1).toLowerCase(); // Title
                String entryAuthor = entry.getStringValue(2).toLowerCase(); // Author (Concatenated Name and Surname)
                String entryISBN = entry.getStringValue(3).toLowerCase(); // ISBN
                String entryPublishedYear = entry.getStringValue(4); // Published Year
                String entryCopies = entry.getStringValue(5); // Copies Available
                String entryCategory = entry.getStringValue(6).toLowerCase(); // Category

                // Split the author field into name and surname
                String[] authorParts = entryAuthor.split(" ", 2);
                String entryAuthorName = authorParts.length > 0 ? authorParts[0] : "";
                String entryAuthorSurname = authorParts.length > 1 ? authorParts[1] : "";

                boolean matchBookID = bookID.isEmpty() || entryBookID.contains(bookID);
                boolean matchTitle = title.isEmpty() || entryTitle.contains(title);
                boolean matchAuthorName = authorName.isEmpty() || entryAuthorName.contains(authorName);
                boolean matchAuthorSurname = authorSurname.isEmpty() || entryAuthorSurname.contains(authorSurname);
                boolean matchISBN = isbn.isEmpty() || entryISBN.contains(isbn);
                boolean matchPublishedYear = publishedYear.isEmpty() || entryPublishedYear.contains(publishedYear);
                boolean matchCopies = copies.isEmpty() || entryCopies.contains(copies);
                boolean matchCategory = category.isEmpty() || entryCategory.contains(category);

                return matchBookID && matchTitle && matchAuthorName && matchAuthorSurname && matchISBN && matchPublishedYear && matchCopies && matchCategory;
            }
        });
    }

    private void deleteBookByID(int bookID) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();

        try {
            // Start a transaction
            connection.setAutoCommit(false);

            // First, delete related records from other tables (e.g., Borrowers)
            String deleteBorrowersQuery = "DELETE FROM BORROWERS WHERE BOOK_ID = ?";
            try (PreparedStatement deleteBorrowersStmt = connection.prepareStatement(deleteBorrowersQuery)) {
                deleteBorrowersStmt.setInt(1, bookID);
                deleteBorrowersStmt.executeUpdate();
            }

            // Now delete the book from the Books table
            String deleteBookQuery = "DELETE FROM BOOKS WHERE BOOK_ID = ?";
            try (PreparedStatement deleteBookStmt = connection.prepareStatement(deleteBookQuery)) {
                deleteBookStmt.setInt(1, bookID);
                deleteBookStmt.executeUpdate();
            }

            // Commit the transaction
            connection.commit();

        } catch (SQLException e) {
            // Rollback the transaction in case of error
            connection.rollback();
            throw new SQLException("Error deleting book and related records: " + e.getMessage(), e);
        } finally {
            // Reset auto-commit
            connection.setAutoCommit(true);
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roundPanel4 = new swing.RoundPanel();
        swing.Button btnBooksAdd = new swing.Button();
        btnBooksRemove = new swing.Button();
        btnBooksUpdate = new swing.Button();
        btnBooksSearch = new swing.Button();
        btnBooksClear = new swing.Button();
        roundPanel2 = new swing.RoundPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        BookTable = new component.TableDark();
        roundPanel3 = new swing.RoundPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jISBNField = new javax.swing.JTextField();
        jPublishedYearField = new javax.swing.JTextField();
        jCopiesField = new javax.swing.JTextField();
        jBookIDField = new javax.swing.JTextField();
        jTitlefield = new javax.swing.JTextField();
        jAuthorNameField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jAuthorSurnameField = new javax.swing.JTextField();
        jCategoryField = new javax.swing.JTextField();

        roundPanel4.setBackground(new java.awt.Color(51, 51, 51));
        roundPanel4.setPreferredSize(new java.awt.Dimension(229, 248));

        btnBooksAdd.setText("Add");
        btnBooksAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBooksAddActionPerformed(evt);
            }
        });

        btnBooksRemove.setText("Remove");
        btnBooksRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBooksRemoveActionPerformed(evt);
            }
        });

        btnBooksUpdate.setText("Update");
        btnBooksUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBooksUpdateActionPerformed(evt);
            }
        });

        btnBooksSearch.setText("Search");
        btnBooksSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBooksSearchActionPerformed(evt);
            }
        });

        btnBooksClear.setText("Clear");
        btnBooksClear.setPreferredSize(new java.awt.Dimension(59, 41));
        btnBooksClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBooksClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout roundPanel4Layout = new javax.swing.GroupLayout(roundPanel4);
        roundPanel4.setLayout(roundPanel4Layout);
        roundPanel4Layout.setHorizontalGroup(
            roundPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel4Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(roundPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnBooksAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBooksRemove, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                    .addComponent(btnBooksUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBooksSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBooksClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(60, 60, 60))
        );
        roundPanel4Layout.setVerticalGroup(
            roundPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(btnBooksAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBooksRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBooksUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBooksSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBooksClear, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        roundPanel2.setBackground(new java.awt.Color(51, 51, 51));

        BookTable.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Book_ID", "Title", "Author", "ISBN", "Published_Year", "Copies Available", "Category"
            }
        ));
        BookTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BookTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(BookTable);

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
                .addContainerGap())
        );

        roundPanel3.setBackground(new java.awt.Color(51, 51, 51));
        roundPanel3.setPreferredSize(new java.awt.Dimension(0, 0));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("ISBN");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Published_Year");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Copies Available");

        jISBNField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jPublishedYearField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jCopiesField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jBookIDField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jTitlefield.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jAuthorNameField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Book_ID");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Title");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText(" Author Name");

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Author Surname");

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Category");

        javax.swing.GroupLayout roundPanel3Layout = new javax.swing.GroupLayout(roundPanel3);
        roundPanel3.setLayout(roundPanel3Layout);
        roundPanel3Layout.setHorizontalGroup(
            roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addGroup(roundPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel7)))
                .addGap(20, 20, 20)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jAuthorSurnameField, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jAuthorNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTitlefield, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBookIDField, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel8))
                .addGap(20, 20, 20)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jISBNField, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                    .addComponent(jPublishedYearField)
                    .addComponent(jCopiesField)
                    .addComponent(jCategoryField))
                .addGap(15, 15, 15))
        );
        roundPanel3Layout.setVerticalGroup(
            roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel3Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jISBNField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBookIDField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jPublishedYearField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTitlefield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jAuthorNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jCopiesField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jAuthorSurnameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jCategoryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(roundPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(roundPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 877, Short.MAX_VALUE))
            .addComponent(roundPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(roundPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addComponent(roundPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(roundPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBooksAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBooksAddActionPerformed
        // Retrieve text from fields
        String title = jTitlefield.getText().trim();

        String copiesText = jCopiesField.getText().trim();
        String authorName = jAuthorNameField.getText().trim();
        String authorSurname = jAuthorSurnameField.getText().trim();
        String isbn = jISBNField.getText().trim();
        String publishedYearText = jPublishedYearField.getText().trim();
        String category = jCategoryField.getText().trim();

        // Validate fields
        if (title.isEmpty() || copiesText.isEmpty() ||
            authorName.isEmpty() || authorSurname.isEmpty() || isbn.isEmpty() ||
            publishedYearText.isEmpty() || category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);

        } else if (isbn.length() != 10 && isbn.length() != 13) {
            System.out.println("Invalid ISBN. It must be exactly 10 or 13 characters.");
            return;
        }else

        if (Integer.parseInt(copiesText) >= 20) {
            System.out.println("Invalid number of copies. It must be less than 20.");
            return;
        }


        else{
             // Convert fields to appropriate types

            int copies = Integer.parseInt(copiesText);
            int publishedYear = Integer.parseInt(publishedYearText);

            // Example method to get author ID; you need to implement this method
            int authorID = 7;

            // Add the book/Author
            //DatabaseConnection.addAuthor(authorID, authorName, authorSurname);
            DatabaseConnection.addBook( title, authorID, isbn, publishedYear, copies ,category);


            jBookIDField.setText("");
            jAuthorNameField.setText("");
            jAuthorSurnameField.setText("");
            jPublishedYearField.setText("");
            jCategoryField.setText("");
            jISBNField.setText("");
            jCopiesField.setText("");
            jTitlefield.setText("");
        }
    }//GEN-LAST:event_btnBooksAddActionPerformed

    private void btnBooksRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBooksRemoveActionPerformed
        int selectedRow = BookTable.getSelectedRow();
        if (selectedRow == -1) {
            //JOptionPane.showMessageDialog(this, "Please select a book to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookID = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this book?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Attempt to delete the book and handle integrity constraints
                deleteBookByID(bookID);
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Book removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                if (e.getMessage().contains("violation of foreign key constraint")) {
                    JOptionPane.showMessageDialog(this, "Cannot delete the book because it is referenced by other records.", "Delete Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error removing book. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btnBooksRemoveActionPerformed

    private void BookTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BookTableMouseClicked
        // Get the selected row index
        int selectedRow = BookTable.getSelectedRow();
        if (selectedRow != -1) {
            // Retrieve values from the selected row using the table model
            int bookID = (int) tableModel.getValueAt(selectedRow, 0);
            String title = (String) tableModel.getValueAt(selectedRow, 1);
            String author = (String) tableModel.getValueAt(selectedRow, 2);
            String isbn = (String) tableModel.getValueAt(selectedRow, 3);
            int publishedYear = (int) tableModel.getValueAt(selectedRow, 4);
            int copies = (int) tableModel.getValueAt(selectedRow, 5);
            String category = (String) tableModel.getValueAt(selectedRow, 6);

            // Split author into name and surname if they are combined
            String[] authorParts = author.split(" ");
            String authorName = authorParts.length > 0 ? authorParts[0] : "";
            String authorSurname = authorParts.length > 1 ? authorParts[1] : "";

            // Populate text fields
            jBookIDField.setText(String.valueOf(bookID));
            jTitlefield.setText(title);
            jAuthorNameField.setText(authorName);
            jAuthorSurnameField.setText(authorSurname);
            jISBNField.setText(isbn);
            jPublishedYearField.setText(String.valueOf(publishedYear));
            jCopiesField.setText(String.valueOf(copies));
            jCategoryField.setText(category);
        }
    }//GEN-LAST:event_BookTableMouseClicked

    private void btnBooksUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBooksUpdateActionPerformed
        // Get the selected row index
        int selectedRow = BookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Retrieve current values from the text fields
        int bookID = Integer.parseInt(jBookIDField.getText().trim());
        String newTitle = jTitlefield.getText().trim();
        String newAuthorName = jAuthorNameField.getText().trim();
        String newAuthorSurname = jAuthorSurnameField.getText().trim();
        String newISBN = jISBNField.getText().trim();
        int newPublishedYear = Integer.parseInt(jPublishedYearField.getText().trim());
        int newCopies = Integer.parseInt(jCopiesField.getText().trim());
        String newCategory = jCategoryField.getText().trim();

        // Retrieve the original values from the table model
        String oldTitle = (String) tableModel.getValueAt(selectedRow, 1);
        String oldAuthor = (String) tableModel.getValueAt(selectedRow, 2);
        String oldISBN = (String) tableModel.getValueAt(selectedRow, 3);
        int oldPublishedYear = (int) tableModel.getValueAt(selectedRow, 4);
        int oldCopies = (int) tableModel.getValueAt(selectedRow, 5);
        String oldCategory = (String) tableModel.getValueAt(selectedRow, 6);

        // Split the original author name and surname
        String[] oldAuthorParts = oldAuthor.split(" ");
        String oldAuthorName = oldAuthorParts.length > 0 ? oldAuthorParts[0] : "";
        String oldAuthorSurname = oldAuthorParts.length > 1 ? oldAuthorParts[1] : "";

        // Compare and update changed fields
        StringBuilder changes = new StringBuilder();
        boolean isChanged = false;

        if (!newTitle.equals(oldTitle)) {
            changes.append(String.format("Title changed from '%s' to '%s'.\n", oldTitle, newTitle));
            isChanged = true;
        }

        boolean authorChanged = !newAuthorName.equals(oldAuthorName) || !newAuthorSurname.equals(oldAuthorSurname);
        if (authorChanged) {
            changes.append(String.format("Author changed from '%s %s' to '%s %s'.\n", oldAuthorName, oldAuthorSurname, newAuthorName, newAuthorSurname));
            isChanged = true;
        }

        if (!newISBN.equals(oldISBN)) {
            changes.append(String.format("ISBN changed from '%s' to '%s'.\n", oldISBN, newISBN));
            isChanged = true;
        }
        if (newPublishedYear != oldPublishedYear) {
            changes.append(String.format("Published Year changed from '%d' to '%d'.\n", oldPublishedYear, newPublishedYear));
            isChanged = true;
        }
        if (newCopies != oldCopies) {
            changes.append(String.format("Copies changed from '%d' to '%d'.\n", oldCopies, newCopies));
            isChanged = true;
        }
        if (!newCategory.equals(oldCategory)) {
            changes.append(String.format("Category changed from '%s' to '%s'.\n", oldCategory, newCategory));
            isChanged = true;
        }

        // If no changes, inform the user
        if (!isChanged) {
            JOptionPane.showMessageDialog(this, "No changes were made.", "Update", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Update the database with the new values
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            int authorID = -1;
            if (authorChanged) {
                // Check if the new author exists
                String checkAuthorQuery = "SELECT AUTHOR_ID FROM AUTHORS WHERE NAME = ? AND SURNAME = ?";
                try (PreparedStatement checkAuthorStmt = connection.prepareStatement(checkAuthorQuery)) {
                    checkAuthorStmt.setString(1, newAuthorName);
                    checkAuthorStmt.setString(2, newAuthorSurname);
                    ResultSet rs = checkAuthorStmt.executeQuery();
                    if (rs.next()) {
                        authorID = rs.getInt("AUTHOR_ID"); // Use existing author ID
                    } else {
                        // Insert new author
                        String insertAuthorQuery = "INSERT INTO AUTHORS (NAME, SURNAME) VALUES (?, ?)";
                        try (PreparedStatement insertAuthorStmt = connection.prepareStatement(insertAuthorQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                            insertAuthorStmt.setString(1, newAuthorName);
                            insertAuthorStmt.setString(2, newAuthorSurname);
                            insertAuthorStmt.executeUpdate();
                            ResultSet generatedKeys = insertAuthorStmt.getGeneratedKeys();
                            if (generatedKeys.next()) {
                                authorID = generatedKeys.getInt(1); // Get new author ID
                            }
                        }
                    }
                }
            } else {
                // No author change, get the existing author ID from the selected row
                String getAuthorIDQuery = "SELECT AUTHOR_ID FROM BOOKS WHERE BOOK_ID = ?";
                try (PreparedStatement getAuthorIDStmt = connection.prepareStatement(getAuthorIDQuery)) {
                    getAuthorIDStmt.setInt(1, bookID);
                    ResultSet rs = getAuthorIDStmt.executeQuery();
                    if (rs.next()) {
                        authorID = rs.getInt("AUTHOR_ID");
                    }
                }
            }

            // Update the BOOKS table with the new values
            String updateBookQuery = "UPDATE BOOKS SET TITLE = ?, AUTHOR_ID = ?, ISBN = ?, PUBLISHED_YEAR = ?, COPIES_AVAILABLE = ?, CATEGORY = ? WHERE BOOK_ID = ?";
            try (PreparedStatement updateBookStmt = connection.prepareStatement(updateBookQuery)) {
                updateBookStmt.setString(1, newTitle);
                updateBookStmt.setInt(2, authorID);
                updateBookStmt.setString(3, newISBN);
                updateBookStmt.setInt(4, newPublishedYear);
                updateBookStmt.setInt(5, newCopies);
                updateBookStmt.setString(6, newCategory);
                updateBookStmt.setInt(7, bookID);

                int rowsUpdated = updateBookStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    // Update the table model to reflect the changes
                    tableModel.setValueAt(newTitle, selectedRow, 1);
                    tableModel.setValueAt(newAuthorName + " " + newAuthorSurname, selectedRow, 2);
                    tableModel.setValueAt(newISBN, selectedRow, 3);
                    tableModel.setValueAt(newPublishedYear, selectedRow, 4);
                    tableModel.setValueAt(newCopies, selectedRow, 5);
                    tableModel.setValueAt(newCategory, selectedRow, 6);

                    // Commit the transaction
                    connection.commit();

                    // Display the changes
                    JOptionPane.showMessageDialog(this, "Update successful:\n" + changes.toString(), "Update", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Rollback if no rows were updated
                    connection.rollback();
                }
            } catch (SQLException e) {
                connection.rollback(); // Rollback transaction on failure
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating book details. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBooksUpdateActionPerformed

    private void btnBooksSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBooksSearchActionPerformed

    }//GEN-LAST:event_btnBooksSearchActionPerformed

    private void btnBooksClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBooksClearActionPerformed
        // Clear all text fields
        jBookIDField.setText("");
        jTitlefield.setText("");
        jAuthorNameField.setText("");
        jAuthorSurnameField.setText("");
        jISBNField.setText("");
        jPublishedYearField.setText("");
        jCopiesField.setText("");
        jCategoryField.setText("");

        // Optionally, clear any filters applied to the table
        TableRowSorter<DefaultTableModel> rowSorter = (TableRowSorter<DefaultTableModel>) BookTable.getRowSorter();
        if (rowSorter != null) {
            rowSorter.setRowFilter(null);
        }
    }//GEN-LAST:event_btnBooksClearActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private component.TableDark BookTable;
    private swing.Button btnBooksClear;
    private swing.Button btnBooksRemove;
    private swing.Button btnBooksSearch;
    private swing.Button btnBooksUpdate;
    private javax.swing.JTextField jAuthorNameField;
    private javax.swing.JTextField jAuthorSurnameField;
    private javax.swing.JTextField jBookIDField;
    private javax.swing.JTextField jCategoryField;
    private javax.swing.JTextField jCopiesField;
    private javax.swing.JTextField jISBNField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField jPublishedYearField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTitlefield;
    private swing.RoundPanel roundPanel2;
    private swing.RoundPanel roundPanel3;
    private swing.RoundPanel roundPanel4;
    // End of variables declaration//GEN-END:variables
}
