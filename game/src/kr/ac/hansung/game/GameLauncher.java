package kr.ac.hansung.game;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import chatViusal.ChatVisual;

@SuppressWarnings("serial")
public class GameLauncher extends Applet {

    private static Game game = new Game();
    public static final boolean DEBUG = false;
    public static ChatVisual chatVisual = new ChatVisual();
    
    @Override
    public void init() {
    	
        setLayout(new BorderLayout());
        add(game, BorderLayout.CENTER);
        add(chatVisual, BorderLayout.SOUTH);
        setMaximumSize(Game.DIMENSIONS);
        setMinimumSize(Game.DIMENSIONS);
        setPreferredSize(Game.DIMENSIONS);
        game.debug = DEBUG;
        game.isApplet = true;
    }

    @Override
    public void start() {
        game.start();
    }

    @Override
    public void stop() {
        game.stop();
    }

    public static void main(String[] args) {
        game.setMinimumSize(Game.DIMENSIONS);
        game.setMaximumSize(Game.DIMENSIONS);
        game.setPreferredSize(new Dimension(720,700));

        game.frame = new JFrame(Game.NAME);

        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLayout(new BorderLayout());

        game.frame.add(game, BorderLayout.CENTER);
        game.frame.add(chatVisual, BorderLayout.SOUTH);
        game.frame.pack();

        // ������ ũ�� ���� ����/�Ұ���
        game.frame.setResizable(false);
        // �ʱ� ������ ��ġ�� �����
        game.frame.setLocationRelativeTo(null);
        // ������ ���̱�
        game.frame.setVisible(true);

        game.windowHandler = new WindowHandler(game);
        game.debug = DEBUG;

        game.start();
    }
}
