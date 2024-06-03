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
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class QueryPanel extends JPanel{
	Connection conn = null;
	PreparedStatement state = null;
	ResultSet result;
	int id = -1;
	
	JPanel upPanel = new JPanel();
	JPanel midPanel = new JPanel();
	JPanel downPanel = new JPanel();


	JLabel fnameL = new JLabel("Име:");
	JLabel lnameL = new JLabel("Фамилия:");
	JLabel titleL = new JLabel("Заглавие:");
	
	JTextField titleTF = new JTextField();
	JTextField fnameTF = new JTextField();
	JTextField lnameTF = new JTextField();

	JButton searchBt = new JButton("Търсене");
	JButton refreshBt = new JButton("Обнови");

	JTable table = new JTable();
	JScrollPane myScroll = new JScrollPane(table);

	public QueryPanel() {
		this.setSize(400, 600);
		this.setLayout(new GridLayout(3, 1));

		// upPanel ----------------------------------
		upPanel.setLayout(new GridLayout(3, 2));
		
		upPanel.add(fnameL);
		upPanel.add(fnameTF);
		upPanel.add(lnameL);
		upPanel.add(lnameTF);
		upPanel.add(titleL);
		upPanel.add(titleTF);

		this.add(upPanel);
		// midPanel-------------------------------------
		midPanel.add(searchBt);
		midPanel.add(refreshBt);

		this.add(midPanel);

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
			state = conn.prepareStatement("select person_id, fname, lname, title from person p "
					+ "join rent r on p.id=r.person_id "
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
	}

	public void clearForm() {
		fnameTF .setText("");
		lnameTF .setText("");
		titleTF .setText("");
	}

	class MouseAction implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {

			int row = table.getSelectedRow();
			id = Integer.parseInt(table.getValueAt(row, 0).toString());
			fnameTF.setText(table.getValueAt(row, 1).toString());
			lnameTF.setText(table.getValueAt(row, 2).toString());
			titleTF.setText(table.getValueAt(row, 3).toString());

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
	class SearchAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			conn = DBConnection.getConnection();
			String sql = "select person_id, fname, lname, title from person p "
					+ "join rent r on p.id=r.person_id "
					+ "join book b on r.book_id=b.id "
					+"where fname=? and lname=? and title=?";
			try {
				state = conn.prepareStatement(sql);
				state.setString(1, fnameTF.getText());
				state.setString(2, lnameTF.getText());
				state.setString(3, titleTF.getText());
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
