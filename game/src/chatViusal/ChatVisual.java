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
	

	private Socket socket; // 연결소켓
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
		//GameLauncher.chatVisual.chatScreen.append("매개 변수로 넘어온 값 : " + id + " " + ip + " " + port + "\n");
		network();
		

	}
	public void network() {
		// 서버에 접속
		try {
			socket = new Socket(ip, port);
			if (socket != null) // socket이 null값이 아닐때 즉! 연결되었을때
			{
				Connection(); // 연결 메소드를 호출
			}
		} catch (UnknownHostException e) {

		} catch (IOException e) {
			GameLauncher.chatVisual.chatScreen.append("소켓 접속 에러!!\n");
		}
	}

	public void Connection() { // 실직 적인 메소드 연결부분
		try { // 스트림 설정
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
		} catch (IOException e) {
			GameLauncher.chatVisual.chatScreen.append("스트림 설정 에러!!\n");
		}
		send_Message(id); // 정상적으로 연결되면 나의 id를 전송
		Thread th = new Thread(new Runnable() { // 스레드를 돌려서 서버로부터 메세지를 수신
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
						GameLauncher.chatVisual.chatScreen.append("메세지 수신 에러!!\n");
						// 서버와 소켓 통신에 문제가 생겼을 경우 소켓을 닫는다
						try {
							os.close();
							is.close();
							dos.close();
							dis.close();
							socket.close();
							break; // 에러 발생하면 while문 종료
						} catch (IOException e1) {

						}

					}
				} // while문 끝
			}// run메소드 끝
		});
		th.start();
	}

	public void send_Message(String str) { // 서버로 메세지를 보내는 메소드
		try {
			byte[] bb;
			bb = str.getBytes();
			dos.write(bb); //.writeUTF(str);
		} catch (IOException e) {
			GameLauncher.chatVisual.chatScreen.append("메세지 송신 에러!!\n");
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
	
	public void start() { //actionevent 지정 method
		Myaction action = new Myaction();
		sendBtn.addActionListener(action);
		textField.addActionListener(action);
		
	}
	
	class Myaction implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// 액션 이벤트가 sendBtn일때 또는 textField 에세 Enter key 치면
						if (e.getSource() == sendBtn || e.getSource() == textField) 
						{
							String msg = null;
							msg = String.format("[%s] %s\n", id, textField.getText());
							send_Message(msg);
							textField.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
							textField.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다				
						}
		}
		
		
	}
	
	public void run() {
		
	}
	
	


	


	

	

}