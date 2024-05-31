package com.t13max.client.view.window;

import battle.api.LoginBattleReq;
import com.t13max.client.player.Player;
import message.id.MessageId;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author: t13max
 * @since: 19:32 2024/5/29
 */
public class LoginWindow extends AbstractWindow {

    private volatile boolean logging;

    public LoginWindow()  {
        super("登录",new Dimension(530, 410),false);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        initWindowContent();
    }


    public void loginSuccess() {
        Player.PLAYER.openWindow("home");
        LoginWindow.this.dispose();
    }

    public void loginFail(String message) {
        this.logging = false;
        JOptionPane.showMessageDialog(LoginWindow.super.getContentPane(), message == null ? "账号或密码错误！" : message, "提示", JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    protected void initWindowContent() {
        Container container = this.getContentPane();

        container.setLayout(null);
        //账号窗口设置
        JLabel label1 = new JLabel();

        //label1.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/logo1.png")).getFile()));
        JLabel ljName = new JLabel("账号:");
        JLabel ljName1 = new JLabel("密码:");

        JTextField jtName = new JTextField("", 25);
        jtName.setBounds(125, 155, 300, 40);
        ljName.setBounds(75, 155, 50, 40);
        label1.setBounds(160, 30, 200, 100);

        //密码窗口设置
        JPasswordField jpf = new JPasswordField(25);
        jpf.setBounds(125, 200, 300, 40);

        JLabel fpd = new JLabel("忘记密码");
        fpd.setBounds(350, 240, 100, 30);
        fpd.setFont(new Font("宋体", Font.PLAIN, 12));
        fpd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ljName1.setBounds(75, 200, 50, 40);

        //登入按钮设置
        JButton btn1 = new JButton("登入");
        btn1.setBounds(125, 290, 300, 40);

        container.add(fpd);
        container.add(label1);
        container.add(btn1);
        container.add(jtName);
        container.add(ljName);
        container.add(jpf);
        container.add(ljName1);

        btn1.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (logging) return;

                String name = jtName.getText();
                String password = String.valueOf(jpf.getPassword());
                if ("".equals(name) || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginWindow.super.getContentPane(), "账号密码不能为空！", "提示", JOptionPane.PLAIN_MESSAGE);
                } else {
                    //尝试登录 正常应该http去login 拿到token在尝试连接gateway 但是太麻烦了 先直接连battle然后Login 这样方便测试
                    LoginBattleReq.Builder builder = LoginBattleReq.newBuilder();
                    builder.setUsername(name);
                    builder.setPassword(password);
                    //发送消息
                    Player.PLAYER.sendMessage(MessageId.C_BATTLE_LOGIN_VALUE, builder.build());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
}
