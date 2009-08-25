import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.apple.dnssd.*;

/*  Hacked together in 20 Minutes by Patrick Borgeat
    http://www.cappel-nord.de
    Found inspiration on http://momo.brauchtman.net/2008/12/zeroconf-made-easy-using-bonjour-for-java/
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
*/

public class AnnounceOSC extends JFrame implements RegisterListener{

	final static String SERVICE = "_osc._udp";
	
	private boolean registered;
	private DNSSDRegistration serviceRecord;
	
	private JTextField portText;
	private JTextField nameText;
	private JButton button;
	
	private static final long serialVersionUID = 169553622779629068L;


	public static void main(String[] args) {
		new AnnounceOSC();
	}
	
	AnnounceOSC()
	{
		super();
		this.setTitle("Announce OSC via Zeroconf");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		
		nameText = new JTextField("SuperCollider");
		nameText.setPreferredSize(new Dimension(150,20));
		portText = new JTextField("57120");
		portText.setPreferredSize(new Dimension(100,20));
		button = new JButton("Go!");
		button.setPreferredSize(new Dimension(100,20));
		
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(!registered)
				{
					registerService();
					button.setText("...");
				}
				else
				{
					unregisterService();
					button.setText("Go!");
				}
			}
		});
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(new JLabel("Announce"));
		this.add(nameText);
		this.add(new JLabel("on Port"));
		this.add(portText);
		this.add(button);
		
		this.pack();
		this.setVisible(true);
	}

	public void serviceRegistered(DNSSDRegistration arg0, int arg1,
			String arg2, String arg3, String arg4) {
		button.setText("Stop!");
		registered = true;
	}

	public void operationFailed(DNSSDService arg0, int arg1) {
		JOptionPane.showMessageDialog(null, "Registering Service failed.", "Sorry!", JOptionPane.ERROR_MESSAGE);
		button.setText("Go!");
		registered = false;
	}

	public void registerService() {
		try {
			serviceRecord = DNSSD.register(0,0,nameText.getText(),SERVICE, null,null,Integer.parseInt(portText.getText()),null,this);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Something went wrong!.", "Sorry!", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void unregisterService() {
		serviceRecord.stop();
		registered = false;
	}
}
