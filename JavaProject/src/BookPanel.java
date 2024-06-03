import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class BookPanel extends JPanel {
	Connection conn = null;
	PreparedStatement state = null;
	ResultSet result;
	int id = -1;

	JPanel upPanel = new JPanel();
	JPanel midPanel = new JPanel();
	JPanel downPanel = new JPanel();

	JLabel titleL = new JLabel("Заглавие:");
	JLabel authorL = new JLabel("Автор:");
	JLabel yearL = new JLabel("Година:");
	JLabel publisherL = new JLabel("Издател:");

	JTextField titleTF = new JTextField();
	JTextField authorTF = new JTextField();
	JTextField yearTF = new JTextField();
	JTextField publisherTF = new JTextField();

	JComboBox<String> bookCombo = new JComboBox<String>();

	JButton addBt = new JButton("Добавяне");
	JButton deleteBt = new JButton("Изтриване");
	JButton editBt = new JButton("Редактиране");
	JButton searchBt = new JButton("Търсене по годинa");
	JButton refreshBt = new JButton("Обнови");

	JTable table = new JTable();
	JScrollPane myScroll = new JScrollPane(table);

	public BookPanel() {
		this.setSize(400, 600);
		this.setLayout(new GridLayout(3, 1));

		// upPanel ----------------------------------
		upPanel.setLayout(new GridLayout(4, 2));
		upPanel.add(titleL);
		upPanel.add(titleTF);
		upPanel.add(authorL);
		upPanel.add(authorTF);
		upPanel.add(yearL);
		upPanel.add(yearTF);
		upPanel.add(publisherL);
		upPanel.add(publisherTF);

		this.add(upPanel);
		// midPanel-------------------------------------
		midPanel.add(addBt);
		midPanel.add(deleteBt);
		midPanel.add(editBt);
		midPanel.add(searchBt);
		midPanel.add(refreshBt);
		midPanel.add(bookCombo);

		this.add(midPanel);

		addBt.addActionListener(new AddAction());
		deleteBt.addActionListener(new DeleteAction());
		editBt.addActionListener(new UpdateAction());
		searchBt.addActionListener(new SearchAction());
		refreshBt.addActionListener(new RefreshAction());

		// downPanel-------------------------------------
		myScroll.setPreferredSize(new Dimension(350, 150));
		downPanel.setLayout(new GridLayout(1,1));
		downPanel.add(myScroll);

		this.add(downPanel);
		table.addMouseListener(new MouseAction());

		refreshTable();
		this.setVisible(true);
	}

	public void refreshTable() {
		conn = DBConnection.getConnection();
		try {
			state = conn.prepareStatement("select * from book");
			result = state.executeQuery();
			table.setModel(new MyModel(result));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clearForm() {
		titleTF.setText("");
		authorTF.setText("");
		yearTF.setText("");
		publisherTF.setText("");
	}

	class AddAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			conn = DBConnection.getConnection();
			String sql = "insert into book(title, author, year_of_publishing, publisher) values(?,?,?,?)";
			try {
				state = conn.prepareStatement(sql);
				state.setString(1, titleTF.getText());
				state.setString(2, authorTF.getText());
				state.setInt(3, Integer.parseInt(yearTF.getText()));
				state.setString(4, publisherTF.getText());

				state.execute();
				refreshTable();
				clearForm();

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	class MouseAction implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {

			int row = table.getSelectedRow();
			id = Integer.parseInt(table.getValueAt(row, 0).toString());
			titleTF.setText(table.getValueAt(row, 1).toString());
			authorTF.setText(table.getValueAt(row, 2).toString());
			yearTF.setText(table.getValueAt(row, 3).toString());
			publisherTF.setText(table.getValueAt(row, 4).toString());

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	class DeleteAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			conn = DBConnection.getConnection();
			String sql = "delete from book where id=?";
			try {
				state = conn.prepareStatement(sql);
				state.setInt(1, id);
				state.execute();
				refreshTable();
				clearForm();
				id = -1;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}
	
	class UpdateAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			conn = DBConnection.getConnection();
			String sql = "update book "
					+ "set title=?, author=?, year_of_publishing=?, publisher=? "
					+ "where id=?";
			try {
				state = conn.prepareStatement(sql);
				state.setString(1, titleTF.getText());
				state.setString(2, authorTF.getText());
				state.setInt(3, Integer.parseInt(yearTF.getText()));
				state.setString(4, publisherTF.getText());
				state.setInt(5, id);

				state.execute();
				refreshTable();
				clearForm();
				id = -1;
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}

	class SearchAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			conn = DBConnection.getConnection();
			String sql = "select * from book where year_of_publishing=?";
			try {
				state = conn.prepareStatement(sql);
				state.setInt(1, Integer.parseInt(yearTF.getText()));
				result = state.executeQuery();
				table.setModel(new MyModel(result));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	class RefreshAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			refreshTable();
			clearForm();

		}

	}
}
