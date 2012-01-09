package at.andiwand.packettracer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public final class Gui extends JFrame {

	private JTextField addressField;
	private JTextField nameField;
	private JPasswordField passwordField;
	private JLabel errorLabel;

	private OnInputValidatedCallback callback;


	public Gui(OnInputValidatedCallback callback) {
		this.callback = callback;

		setTitle("PacketTracer Bridge Connection");
		setLayout(new BorderLayout());

		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel headerLabel = new JLabel("PacketTracer Bridge Connection");

		headerPanel.add(headerLabel);

		GridLayout contentLayout = new GridLayout(0, 2);
		contentLayout.setHgap(20);
		contentLayout.setVgap(3);

		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 3, 10, 3));
		contentPanel.setLayout(contentLayout);

		JLabel addressLabel = new JLabel("Peer Address:");
		addressField = new JTextField("localhost:38000");

		JLabel nameLabel = new JLabel("Peer Name:");
		nameField = new JTextField("Lab_10");

		JLabel passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField("123");

		errorLabel = new JLabel();
		errorLabel.setForeground(Color.RED);

		contentPanel.add(addressLabel);
		contentPanel.add(addressField);
		contentPanel.add(nameLabel);
		contentPanel.add(nameField);
		contentPanel.add(passwordLabel);
		contentPanel.add(passwordField);

		JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JButton connectButton = new JButton("Connect");
		connectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String address = addressField.getText();

				String[] temp = address.split(":");
				if (temp.length != 2) {
					errorLabel.setText("Couldn't parse address.");
					// pack();

					return;
				}

				int port = Integer.parseInt(temp[1]);

				Gui.this.callback.onInputValidated(nameField.getText(), temp[0], port, passwordField.getText());

				setVisible(false);
			}
		});

		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(1);
			}
		});

		controlsPanel.add(errorLabel);
		controlsPanel.add(connectButton);
		controlsPanel.add(closeButton);

		add(headerPanel, BorderLayout.NORTH);
		add(contentPanel, BorderLayout.CENTER);
		add(controlsPanel, BorderLayout.SOUTH);

		// pack();
		setSize(new Dimension(400, 200));
		setVisible(true);
	}


	public interface OnInputValidatedCallback {
		public void onInputValidated(String name, String host, int port, String password);
	}


	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		new Gui(new OnInputValidatedCallback() {

			@Override
			public void onInputValidated(String name, String host, int port,
					String password) {
				System.out.println(host);
				System.out.println(Integer.toString(port));
				System.out.println(name);
				System.out.println(password);
				
				System.exit(0);
			}
		});
	}
}
