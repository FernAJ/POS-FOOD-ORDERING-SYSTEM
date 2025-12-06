import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {

    DefaultTableModel tableModel;
    JLabel lblSubtotal, lblTax, lblTotal;

    public Main() {

        Color xpBlue = new Color(180, 200, 240);
        Color xpGray = new Color(235, 235, 235);
        Font xpFont = new Font("Tahoma", Font.PLAIN, 11);

        UIManager.put("Label.font", xpFont);
        UIManager.put("Button.font", xpFont);
        UIManager.put("Table.font", xpFont);
        UIManager.put("TitledBorder.font", xpFont);

        setTitle("Food Ordering System");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel BlueBar = new JPanel(new BorderLayout());
        BlueBar.setBackground(new Color(49, 106, 197));
        BlueBar.setPreferredSize(new Dimension(getWidth(), 28));

        JLabel Title = new JLabel("  Food Ordering System");
        Title.setForeground(Color.white);
        Title.setFont(new Font("Tahoma", Font.BOLD, 12));

        BlueBar.add(Title, BorderLayout.WEST);

        add(BlueBar, BorderLayout.NORTH);

        JPanel productPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        productPanel.setPreferredSize(new Dimension(200, 0));
        productPanel.setBorder(BorderFactory.createTitledBorder("Products"));
        productPanel.setBackground(xpBlue);

        String[] sampleProducts = {"Hotdog", "Burger", "Fries", "Soda", "Yakult", "Pizza"};
        double[] productPrices = {30.00, 85.00, 45.00, 25.00, 15.00, 150.00};

        for (int i = 0; i < sampleProducts.length; i++) {
            String itemName = sampleProducts[i];
            double itemPrice = productPrices[i];

            JButton btn = new JButton(itemName + " (P" + itemPrice + ")");
            btn.setBackground(new Color(240, 240, 240));
            btn.setFocusPainted(false);

            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addProductToTable(itemName, itemPrice);
                    calculateTotals();
                }
            });

            productPanel.add(btn);
        }

        add(productPanel, BorderLayout.EAST);

        String[] tabCategory = {"Items", "Quantity", "Price", "Total"};

        tableModel = new DefaultTableModel(tabCategory, 0);
        JTable orderTable = new JTable(tableModel);
        orderTable.setEnabled(false);

        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setBorder(BorderFactory.createTitledBorder("Order"));
        orderScroll.getViewport().setBackground(Color.white);

        add(orderScroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 3, 10, 5));
        bottomPanel.setBackground(xpGray);

        lblSubtotal = new JLabel(" Subtotal: P 0.00");
        lblTax = new JLabel(" Tax (12%): P 0.00");
        lblTotal = new JLabel(" Total: P 0.00");

        JButton btnDone = new JButton("Done");
        JButton btnClear = new JButton("Clear Order");

        btnDone.setBackground(new Color(240, 240, 240));
        btnDone.setFocusPainted(false);

        btnClear.setBackground(new Color(240, 240, 240));
        btnClear.setFocusPainted(false);

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);
                calculateTotals();
            }
        });

        btnDone.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tableModel.getRowCount() > 0) {
                    StringBuilder receipt = new StringBuilder();
                    receipt.append("----- OFFICIAL RECEIPT -----\n\n");

                    for(int i = 0; i < tableModel.getRowCount(); i++) {
                        String name = tableModel.getValueAt(i, 0).toString();
                        String qty = tableModel.getValueAt(i, 1).toString();
                        String price = tableModel.getValueAt(i, 3).toString();

                        receipt.append(name + "  x" + qty + "  ---  P" + price + "\n");
                    }
                    receipt.append("\n--------------------------\n");
                    receipt.append(lblSubtotal.getText() + "\n");
                    receipt.append(lblTax.getText() + "\n");
                    receipt.append(lblTotal.getText() + "\n");
                    receipt.append("\nThank you for ordering!");

                    JOptionPane.showMessageDialog(null, receipt.toString());

                    tableModel.setRowCount(0);
                    calculateTotals();
                } else {
                    JOptionPane.showMessageDialog(null, "Cart is empty.");
                }
            }
        });

        bottomPanel.add(lblSubtotal);
        bottomPanel.add(lblTax);
        bottomPanel.add(lblTotal);
        bottomPanel.add(btnDone);
        bottomPanel.add(btnClear);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void addProductToTable(String name, double price) {
        boolean found = false;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String existingName = (String) tableModel.getValueAt(i, 0);

            if (existingName.equals(name)) {
                int currentQty = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                int newQty = currentQty + 1;
                double newTotal = newQty * price;

                tableModel.setValueAt(newQty, i, 1);
                tableModel.setValueAt(newTotal, i, 3);
                found = true;
                break;
            }
        }
        if (!found) {
            Object[] row = {name, 1, price, price};
            tableModel.addRow(row);
        }
    }

    public void calculateTotals() {
        double subtotal = 0.0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            double rowTotal = Double.parseDouble(tableModel.getValueAt(i, 3).toString());
            subtotal += rowTotal;
        }
        double tax = subtotal * 0.10;
        double total = subtotal + tax;

        lblSubtotal.setText(String.format(" Subtotal: P %.2f", subtotal));
        lblTax.setText(String.format(" Tax (10%%): P %.2f", tax));
        lblTotal.setText(String.format(" Total: P %.2f", total));
    }
    public static void main(String[] args) {
        new Main();
    }
}


