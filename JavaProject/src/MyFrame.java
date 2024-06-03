import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;

public class MyFrame extends JFrame {


	PersonPanel panelPerson=new PersonPanel();
	BookPanel panelBook=new BookPanel();
	RentPanel panelRenta=new RentPanel();
	QueryPanel sprPanel=new QueryPanel();

	JTabbedPane tab = new JTabbedPane();

	public MyFrame() {
		this.setSize(400, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		tab.add(panelPerson, "Клиенти");
		tab.add(panelBook, "Книги");
		tab.add(panelRenta, "Наем");
		tab.add(sprPanel, "Справка по...");
		tab.addMouseListener(new MouseAction());
		this.add(tab);

		this.setVisible(true);
	}
	
	class MouseAction implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			panelRenta.refreshTable();
			panelRenta.refreshComboPerson();
			panelRenta.refreshComboPerson();
			sprPanel.refreshTable();
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

}
