package chat.server;
// Java Chatting Server

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JFrame;

import kr.ac.hansung.game.Game;
import kr.ac.hansung.game.GameLauncher;

public class ChatServer extends JFrame {

	private ServerSocket socket; // server socket
	private Socket soc;//connection socket
	private int port;//port number
	private Vector vc = new Vector();
	
	private Game game;
	
	
	public ChatServer(Game game) {
		this.game = game;
		try {
			this.socket = new ServerSocket(1331);
			if(socket!=null)//nomally open socket  
			{
				connection();
				
			}
		}catch (IOException e) {
			
		}
	}

	public void connection() {
		Thread th = new Thread(new Runnable(){ // ����� ������ ���� ������

			@Override
			public void run() {
				while(true) {//����� ������ ����ؼ� �ޱ� ���� while��
					try {
						soc = socket.accept();//accept�� �Ͼ�� �������� ���� �����
						GameLauncher.chatVisual.chatScreen.append("����� ����");
						UserInfo user = new UserInfo(soc, vc);
						
						vc.add(user);// add object into vector
						user.start();
					}catch(IOException e) {
						GameLauncher.chatVisual.chatScreen.append("!!!!!! accept �����߻�... !!!!!");
					}
					
				}
				
			}
			
		});
		th.start();
	}
	
	class UserInfo extends Thread {
		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;
		private Socket user_socket;
		private Vector user_vc;
		private String Nickname = "";

		public UserInfo(Socket soc, Vector vc) // �����ڸ޼ҵ�
		{
			// �Ű������� �Ѿ�� �ڷ� ����
			this.user_socket = soc;
			this.user_vc = vc;
			User_network();
		}
		public void User_network() {
			try {
				is = user_socket.getInputStream();
				dis = new DataInputStream(is);
				os = user_socket.getOutputStream();
				dos = new DataOutputStream(os);
				//Nickname = dis.readUTF(); // ������� �г��� �޴ºκ�
				byte[] b=new byte[128];
				dis.read(b);
				String Nickname = new String(b);
				Nickname = Nickname.trim();
				GameLauncher.chatVisual.chatScreen.append("ID " + Nickname + " ����\n");
				GameLauncher.chatVisual.chatScreen.setCaretPosition(GameLauncher.chatVisual.chatScreen.getText().length());		
				send_Message(Nickname + "�� ȯ���մϴ�."); // ����� ����ڿ��� ���������� �˸�
			} catch (Exception e) {
				GameLauncher.chatVisual.chatScreen.append("��Ʈ�� ���� ����\n");
				GameLauncher.chatVisual.chatScreen.setCaretPosition(GameLauncher.chatVisual.chatScreen.getText().length());
			}
		}

		public void InMessage(String str) {
			//textArea.append("����ڷκ��� ���� �޼��� : " + str+"\n");
			GameLauncher.chatVisual.chatScreen.append(str + "\n");
			GameLauncher.chatVisual.chatScreen.setCaretPosition(GameLauncher.chatVisual.chatScreen.getText().length());
			// ����� �޼��� ó��
			broad_cast(str);
		}

		public void broad_cast(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserInfo imsi = (UserInfo) user_vc.elementAt(i);
				imsi.send_Message(str);
			}
		}

		public void send_Message(String str) {
			try {
				//dos.writeUTF(str);
				byte[] bb;		
				bb = str.getBytes();
				dos.write(bb); //.writeUTF(str);
			} 
			catch (IOException e) {
				GameLauncher.chatVisual.chatScreen.append("�޽��� �۽� ���� �߻�\n");	
				GameLauncher.chatVisual.chatScreen.setCaretPosition(GameLauncher.chatVisual.chatScreen.getText().length());
			}
		}

		public void run() // ������ ����
		{

			while (true) {
				try {
					
					// ����ڿ��� �޴� �޼���
					byte[] b = new byte[128];
					dis.read(b);
					String msg = new String(b);
					msg = msg.trim();
					//String msg = dis.readUTF();
					InMessage(msg);
					
				} 
				catch (IOException e) 
				{
					
					try {
						dos.close();
						dis.close();
						user_socket.close();
						vc.removeElement( this ); // �������� ���� ��ü�� ���Ϳ��� �����
						GameLauncher.chatVisual.chatScreen.append(vc.size() +" : ���� ���Ϳ� ����� ����� ��\n");
						GameLauncher.chatVisual.chatScreen.append("����� ���� ������ �ڿ� �ݳ�\n");
						GameLauncher.chatVisual.chatScreen.setCaretPosition(GameLauncher.chatVisual.chatScreen.getText().length());
						
						break;
					
					} catch (Exception ee) {
					
					}// catch�� ��
				}// �ٱ� catch����

			}
			
			
			
		}// run�޼ҵ� ��

	} // ���� userinfoŬ������

	public void start() {
		// TODO Auto-generated method stub
		
	}

}
