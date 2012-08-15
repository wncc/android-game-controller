import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class gamecontroller {
	public static void main(String args[]) {

		final JFrame frame;
		Container content;
		JLabel iplabel, portlabel;
		final JLabel output = new JLabel("");
		final myserver server = new myserver(output);
		final JList<String> iplist;
		final JScrollPane ipscrollpane;
		JButton refreshlist, start, stop;
		final JTextField portinput;
		frame = new JFrame("Insane Game Controller");
		frame.setSize(300, 300);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		content = frame.getContentPane();
		content.setLayout(new FlowLayout());
		content.setBackground(Color.WHITE);
		iplabel = new JLabel("List of Ip Addresses: ");
		String[] ipdata = listipaddress();
		iplist = new JList<String>(ipdata);
		iplist.setVisibleRowCount(5);
		iplist.setSelectedIndex(0);
		ipscrollpane = new JScrollPane(iplist);
		ipscrollpane.setPreferredSize(new Dimension(250, 100));
		refreshlist = new JButton("Refresh List");
		refreshlist.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String[] ipnewdata = listipaddress();
				iplist.setListData(ipnewdata);
				ipscrollpane.revalidate();
				ipscrollpane.repaint();

			}
		});
		refreshlist.setPreferredSize(new Dimension(250, 30));
		portlabel = new JLabel("Port Number: ");
		portinput = new JTextField(5);
		portinput.setText("4007");
		start = new JButton("Start");
		stop = new JButton("Stop");
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int port;
				try {
					port = Integer.parseInt(portinput.getText());
					server.run(port);
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					output.setText("Use Only Integers for Port Number");
				}
			}
		});
		stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				server.stopserver();
			}
		});

		content.add(iplabel);
		content.add(ipscrollpane);
		content.add(refreshlist);
		content.add(portlabel);
		content.add(portinput);
		content.add(start);
		content.add(stop);
		content.add(output);
		frame.setVisible(true);
		while (true) {
			server.getinput();
		}
	}

	static String[] listipaddress() {
		List<String> ip_list = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> e = NetworkInterface
					.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) e.nextElement();
				Enumeration<InetAddress> e2 = ni.getInetAddresses();
				while (e2.hasMoreElements()) {
					InetAddress ip = (InetAddress) e2.nextElement();
					if (ip.toString().contains(".")) {
						ip_list.add(ip.toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] iplist = new String[ip_list.size()];
		iplist = ip_list.toArray(iplist);
		return iplist;
	}
}
