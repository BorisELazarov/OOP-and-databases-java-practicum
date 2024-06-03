import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class PersonPanel extends JPanel {
	Connection conn = null;
	PreparedStatement state = null;
	ResultSet result;
	int id = -1;

	JPanel upPanel = new JPanel();
	JPanel midPanel = new JPanel();
	JPanel downPanel = new JPanel();

	JLabel fnameL = new JLabel("Име:");
	JLabel lnameL = new JLabel("Фамилия:");
	JLabel sexL = new JLabel("Пол:");
	JLabel ageL = new JLabel("Години:");

	JTextField fnameTF = new JTextField();
	JTextField lnameTF = new JTextField();
	JTextField ageTF = new JTextField();

	String[] item = { "M", "F" };
	JComboBox<String> sexCombo = new JComboBox<String>(item);

	JButton addBt = new JButton("Добавяне");
	JButton deleteBt = new JButton("Изтриване");
	JButton editBt = new JButton("Редактиране");
	JButton searchBt = new JButton("Търсене по години");
	JButton refreshBt = new JButton("Обнови");

	JTable table = new JTable();
	JScrollPane myScroll = new JScrollPane(table);

	public PersonPanel() {
		this.setSize(400, 600);
		this.setLayout(new GridLayout(3, 1));

		// upPanel ----------------------------------
		upPanel.setLayout(new GridLayout(4, 2));
		upPanel.add(fnameL);
		upPanel.add(fnameTF);
		upPanel.add(lnameL);
		upPanel.add(lnameTF);
		upPanel.add(sexL);
		upPanel.add(sexCombo);
		upPanel.add(ageL);
		upPanel.add(ageTF);

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
		this.setVisible(true);
	}

	public void refreshTable() {
		conn = DBConnection.getConnection();
		try {
			state = conn.prepareStatement("select * from person");
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
		fnameTF.setText("");
		lnameTF.setText("");
		ageTF.setText("");
	}

	class AddAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			conn = DBConnection.getConnection();
			String sql = "insert into person(fname, lname, gender, age) values(?,?,?,?)";
			try {
				state = conn.prepareStatement(sql);
				state.setString(1, fnameTF.getText());
				state.setString(2, lnameTF.getText());
				state.setString(3, sexCombo.getSelectedItem().toString());
				state.setInt(4, Integer.parseInt(ageTF.getText()));

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
			fnameTF.setText(table.getValueAt(row, 1).toString());
			lnameTF.setText(table.getValueAt(row, 2).toString());
			ageTF.setText(table.getValueAt(row, 4).toString());
			if (table.getValueAt(row, 3).toString().equals("M")) {
				sexCombo.setSelectedIndex(0);
			} else {
				sexCombo.setSelectedIndex(1);
			}

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
			String sql = "delete from person where id=?";
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
			String sql = "update person "
					+ "set fname=?, lname=?, gender=?, age=? "
					+ "where id=?";
			try {
				state = conn.prepareStatement(sql);
				state.setString(1, fnameTF.getText());
				state.setString(2, lnameTF.getText());
				state.setString(3, sexCombo.getSelectedItem().toString());
				state.setInt(4, Integer.parseInt(ageTF.getText()));
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
			String sql = "select * from person where age=?";
			try {
				state = conn.prepareStatement(sql);
				state.setInt(1, Integer.parseInt(ageTF.getText()));
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
