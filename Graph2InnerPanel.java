import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Graph2InnerPanel extends JPanel {
  private int[] is = {};
  private final Color[] cs = {Color.RED, Color.BLUE};
  private final int scale = 15;
  
  public Graph2InnerPanel() {
    setPreferredSize(new Dimension(200, 230));
    JTextField input = new JTextField("", 16);
    input.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String[] args = input.getText().split(" ");
        int n = args.length;
        is = new int[n];

        int i;
        for (i = 0; i < n; i++) {
          is[i] = Integer.parseInt(args[i]);
        }
        repaint();
      }
    });
    setLayout(new FlowLayout());
    add(input);
    JPanel innerPanel = new JPanel() {
      @Override
      public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int i;
        int n = is.length;
        for (i = 0; i < n; i++) {
          g.setColor(cs[i % 2]);
          g.fillRect(0, i * scale,
                     is[i] * scale, scale);
        }
      }        
    };
    innerPanel.setPreferredSize(
      new Dimension(200, 200));
    add(innerPanel);
  } 

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("グラフ");
      frame.add(new Graph2InnerPanel());
      frame.pack();
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    });
  }
}