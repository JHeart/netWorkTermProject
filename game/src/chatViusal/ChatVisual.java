package chatViusal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kr.ac.hansung.game.GameLauncher;


@SuppressWarnings("serial")
public class ChatVisual extends JPanel implements Runnable{
	public JTextArea chatScreen ; 
	private JButton sendBtn;
	private JTextField textField;
	private String id;
	private String ip;
	private int port;
	

	private Socket socket; // �������
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	
	public ChatVisual() {
		this.id = "aa";
		this.ip = null;
		this.port = 1331;
		init();
		start();
		//GameLauncher.chatVisual.chatScreen.append("�Ű� ������ �Ѿ�� �� : " + id + " " + ip + " " + port + "\n");
		network();
		

	}
	public void network() {
		// ������ ����
		try {
			socket = new Socket(ip, port);
			if (socket != null) // socket�� null���� �ƴҶ� ��! ����Ǿ�����
			{
				Connection(); // ���� �޼ҵ带 ȣ��
			}
		} catch (UnknownHostException e) {

		} catch (IOException e) {
			GameLauncher.chatVisual.chatScreen.append("���� ���� ����!!\n");
		}
	}

	public void Connection() { // ���� ���� �޼ҵ� ����κ�
		try { // ��Ʈ�� ����
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
		} catch (IOException e) {
			GameLauncher.chatVisual.chatScreen.append("��Ʈ�� ���� ����!!\n");
		}
		send_Message(id); // ���������� ����Ǹ� ���� id�� ����
		Thread th = new Thread(new Runnable() { // �����带 ������ �����κ��� �޼����� ����
			@SuppressWarnings("null")
			@Override
			public void run() {
				while (true) {
					try {
						byte[] b = new byte[128];
						dis.read(b);
						String msg = new String(b);
						msg = msg.trim();
						GameLauncher.chatVisual.chatScreen.append(msg + "\n");
						GameLauncher.chatVisual.chatScreen.setCaretPosition(GameLauncher.chatVisual.chatScreen.getText().length());				
					} catch (IOException e) {
						GameLauncher.chatVisual.chatScreen.append("�޼��� ���� ����!!\n");
						// ������ ���� ��ſ� ������ ������ ��� ������ �ݴ´�
						try {
							os.close();
							is.close();
							dos.close();
							dis.close();
							socket.close();
							break; // ���� �߻��ϸ� while�� ����
						} catch (IOException e1) {

						}

					}
				} // while�� ��
			}// run�޼ҵ� ��
		});
		th.start();
	}

	public void send_Message(String str) { // ������ �޼����� ������ �޼ҵ�
		try {
			byte[] bb;
			bb = str.getBytes();
			dos.write(bb); //.writeUTF(str);
		} catch (IOException e) {
			GameLauncher.chatVisual.chatScreen.append("�޼��� �۽� ����!!\n");
		}
	}
	
	public void init() {
		setPreferredSize(new Dimension(720, 150));
		setLayout(null);
		setBackground(new Color(255, 255, 255));
		chatScreen = new JTextArea();
		chatScreen.setRows(3);
		chatScreen.setBounds(7, 7, 720, 80);
		chatScreen.setBackground(new Color(155,155,155));
		JScrollPane js = new JScrollPane();
		js.setBounds(7,7,720,100);
		js.setViewportView(chatScreen);
		chatScreen.setDisabledTextColor(new Color(255,0,0));
		chatScreen.setEnabled(false);// disabled
		add(js);
		
		sendBtn = new JButton("send");
		sendBtn.setFont(new Font("Arial", Font.BOLD, 8));
		sendBtn.setBounds(650, 115, 70, 30);
		add(sendBtn);
		textField = new JTextField();
		textField.setBounds(7, 115, 600, 30);
		add(textField);
	}
	
	public void start() { //actionevent ���� method
		Myaction action = new Myaction();
		sendBtn.addActionListener(action);
		textField.addActionListener(action);
		
	}
	
	class Myaction implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// �׼� �̺�Ʈ�� sendBtn�϶� �Ǵ� textField ���� Enter key ġ��
						if (e.getSource() == sendBtn || e.getSource() == textField) 
						{
							String msg = null;
							msg = String.format("[%s] %s\n", id, textField.getText());
							send_Message(msg);
							textField.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
							textField.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��				
						}
		}
		
		
	}
	
	public void run() {
		
	}
	
	


	


	

	

}