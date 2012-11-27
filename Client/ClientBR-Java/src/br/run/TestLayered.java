package br.run;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class TestLayered {

	public static void main(String[] args) {

		JFrame jf = new JFrame("Test");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel content = new JPanel();

		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		content.setOpaque(false);
		JLabel label1 = new JLabel("Username:");

		label1.setForeground(Color.white);
		content.add(label1);

		JTextField field = new JTextField(15);
		content.add(field);

		JLabel label2 = new JLabel("Password:");
		label2.setForeground(Color.white);

		content.add(label2);
		JPasswordField fieldPass = new JPasswordField(15);

		content.add(fieldPass);
		jf.getContentPane().setLayout(new FlowLayout());

		jf.getContentPane().add(content);
		((JPanel) jf.getContentPane()).setOpaque(false);

		ImageIcon background = new ImageIcon("data/background.png");
		JLabel backlabel = new JLabel(background);
		jf.setPreferredSize(new Dimension(background.getIconWidth()
				+ jf.getInsets().left * 2, background.getIconHeight()
				+ jf.getInsets().bottom * 2));

		jf.getLayeredPane().add(backlabel, new Integer(Integer.MIN_VALUE));

		backlabel.setBounds(0, 0, background.getIconWidth(),
				background.getIconHeight());

		jf.pack();
		jf.setVisible(true);

	}

}
