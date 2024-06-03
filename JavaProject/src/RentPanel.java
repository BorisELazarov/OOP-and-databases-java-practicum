import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class RentPanel extends JPanel {
	Connection conn = null;
	PreparedStatement state = null;
	ResultSet result;
	int id = -1;

	JPanel upPanel = new JPanel();
	JPanel midPanel = new JPanel();
	JPanel downPanel = new JPanel();

	JLabel personIdL = new JLabel("Id на човек:");
	JLabel bookIdL = new JLabel("Id на книга:");
	JLabel startDateL = new JLabel("Начална дата на наем:");
	JLabel endDateL = new JLabel("Крайна дата за връщане:");
	
	DateFormat dateFormat = new SimpleDateFormat("yyyy-m-d");
	JFormattedTextField startDateTF=new JFormattedTextField(dateFormat);
	JFormattedTextField endDateTF=new JFormattedTextField(dateFormat);

	JComboBox<String> personCombo = new JComboBox<String>();
	JComboBox<String> bookCombo = new JComboBox<String>();

	JButton addBt = new JButton("Добавяне");
	JButton deleteBt = new JButton("Изтриване");
	JButton editBt = new JButton("Редактиране");
	JButton searchBt = new JButton("Търсене по годинa");
	JButton refreshBt = new JButton("Обнови");

	JTable table = new JTable();
	JScrollPane myScroll = new JScrollPane(table);

	public RentPanel() {
		this.setSize(400, 600);
		this.setLayout(new GridLayout(3, 1));

		// upPanel ----------------------------------
		upPanel.setLayout(new GridLayout(4, 2));
		upPanel.add(personIdL);
		upPanel.add(personCombo);
		upPanel.add(bookIdL);
		upPanel.add(bookCombo);
		upPanel.add(startDateL);
		upPanel.add(startDateTF);
		upPanel.add(endDateL);
		upPanel.add(endDateTF);

		this.add(upPanel);
		// midPanel-------------------------------------
		midPanel.add(addBt);
		midPanel.add(deleteBt);
		midPanel.add(editBt);
		midPanel.add(searchBt);
		midPanel.add(refreshBt);

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
		refreshComboPerson();
		refreshComboBook();
		this.setVisible(true);
	}
	
	public void getComboPerson() {
		personCombo.removeAllItems();
		String item="";
		conn = DBConnection.getConnection();
		try {
			state = conn.prepareStatement("select id from person");
			result = state.executeQuery();
			while (result.next()) {
				item = result.getObject(1).toString();
				personCombo.addItem(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getComboBook() {
		bookCombo.removeAllItems();
		String item="";
		conn = DBConnection.getConnection();
		try {
			state = conn.prepareStatement("select id from book");
			result = state.executeQuery();
			while (result.next()) {
				item = result.getObject(1).toString();
				bookCombo.addItem(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void refreshTable() {
		conn = DBConnection.getConnection();
		try {
			state = conn.prepareStatement("select r.id, concat(p.id,'.',p.fname,' ',p.lname) as person, "
					+ "concat(b.id,'.',b.title,' ',b.author) as book,"
					+ " date_of_rent, date_of_return from rent r "
					+ "join person p on r.person_id=p.id "
					+ "join book b on r.book_id=b.id");
			result = state.executeQuery();
			table.setModel(new MyModel(result));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startDateTF.setText("2000-1-1");
		endDateTF.setText("2000-1-1");
	}
	
	public void refreshComboBook() {

		bookCombo.removeAllItems();

		conn = DBConnection.getConnection();
		String sql = "select id, title, author from book";
		String item = "";

		try {
			state = conn.prepareStatement(sql);
			result = state.executeQuery();
			while (result.next()) {
				item = result.getObject(1).toString() + "." + result.getObject(2).toString() + " "
						+ result.getObject(3).toString();
				bookCombo.addItem(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void refreshComboPerson() {

		personCombo.removeAllItems();

		conn = DBConnection.getConnection();
		String sql = "select id, fname, lname from Person";
		String item = "";

		try {
			state = conn.prepareStatement(sql);
			result = state.executeQuery();
			while (result.next()) {
				item = result.getObject(1).toString() + "." + result.getObject(2).toString() + " "
						+ result.getObject(3).toString();
				personCombo.addItem(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void clearForm() {
		startDateTF.setText("2000-1-1");
		endDateTF.setText("2000-1-1");
		personCombo.setSelectedIndex(0);
		bookCombo.setSelectedIndex(0);
	}

	class AddAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			conn = DBConnection.getConnection();
			String sql = "insert into rent(person_id, book_id, date_of_rent, date_of_return) values(?,?,?,?)";
			try {
				state = conn.prepareStatement(sql);
				String person=personCombo.getSelectedItem().toString();
				String book=bookCombo.getSelectedItem().toString();
				state.setInt(1, Integer.parseInt(person.substring(0, person.indexOf('.'))));
				state.setInt(2, Integer.parseInt(book.substring(0, book.indexOf('.'))));
				state.setString(3, startDateTF.getText());
				state.setString(4, endDateTF.getText());

				state.execute();
				refreshTable();
				refreshComboBook();
				refreshComboPerson();
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
			for (Integer i = 0; i<personCombo.getItemCount(); i++) {
				if(table.getValueAt(row, 1).toString().equals(personCombo.getItemAt(i).toString()))
				{
					personCombo.setSelectedIndex(i);
					break;
				}
			}
			for (Integer i = 0; i<bookCombo.getItemCount(); i++) {
				if(table.getValueAt(row, 2).toString().equals(bookCombo.getItemAt(i).toString()))
				{
					bookCombo.setSelectedIndex(i);
					break;
				}
			}
			startDateTF.setText(table.getValueAt(row, 3).toString());
			endDateTF.setText(table.getValueAt(row, 4).toString());

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
			String sql = "delete from rent where id=?";
			try {
				state = conn.prepareStatement(sql);
				state.setInt(1, id);
				state.execute();
				refreshTable();
				refreshComboBook();
				refreshComboPerson();
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
			String sql = "update rent "
					+ "set person_id=?, book_id=?, date_of_rent=?, date_of_return=? "
					+ "where id=?";
			try {
				state = conn.prepareStatement(sql);
				String person=personCombo.getSelectedItem().toString();
				String book=bookCombo.getSelectedItem().toString();
				state.setInt(1, Integer.parseInt(person.substring(0, person.indexOf('.'))));
				state.setInt(2, Integer.parseInt(book.substring(0, book.indexOf('.'))));
				state.setString(3, startDateTF.getText());
				state.setString(4, endDateTF.getText());
				state.setInt(5, id);

				state.execute();
				refreshTable();
				refreshComboBook();
				refreshComboPerson();
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
			String sql = "select * from rent where date_of_rent"
					+ "=?";
			try {
				state = conn.prepareStatement(sql);
				state.setString(1, startDateTF.getText());
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
