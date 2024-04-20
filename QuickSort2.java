import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class QuickSort2 extends JPanel
                         implements Runnable, ActionListener {
  private int[] data = new int[12];
  private final Color[] cs = {Color.RED, Color.ORANGE, Color.GREEN, Color.BLUE};
  private volatile Thread thread = null;
  private int i, j;
  private volatile boolean threadSuspended = true;

  public QuickSort2() {
    setPreferredSize(new Dimension(320, 250));
		JButton step = new JButton("Step");
		step.addActionListener(this);
		setLayout(new FlowLayout());
		add(step);
    startThread();
  }

  private void startThread() {
    if (thread == null) {
      thread = new Thread(this);
      thread.start();
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    int k;
    super.paintComponent(g);
    g.setColor(Color.YELLOW);
    g.fillOval(5, 50 + j * 10, 10, 10);
    g.setColor(Color.CYAN);
    g.fillOval(5, 50 + i * 10, 10, 10);
    for (k = 0; k < data.length; k++) {
      g.setColor(cs[k % cs.length]);
      g.fillRect(20, 50 + k * 10, data[k] * 5, 10);
    }
  }
  
  private void prepareRandomData() {
    int len = data.length;
    for (int k = 0; k < len; k++) { 
      data[k] = (int)(Math.random() * len * 4);  
    }
  }

  private static void swap(int[] v, int i, int j) {
      int tmp = v[i];
      v[i] = v[j];
      v[j] = tmp;
  }

  private void qsort(int[] v, int left, int right) {
    if (left >= right) return;
    i = left; j = right;
    int pivot = v[i + (j - i) / 2];
    while (true) {
      while (v[i] < pivot) i++;
      while (pivot < v[j]) j--;
      if (i >= j) break;
      swap(v, i, j);
      repaint();
      try { 
				synchronized (this) {
					while (threadSuspended) {
						wait();
					}
					threadSuspended = true;
				}
      } catch (InterruptedException e) {}
      i++; j--;
    }
    qsort(v, left, i - 1);
    qsort(v, j + 1, right);
  }

  public void run() {
    while (true) {
      prepareRandomData();
      qsort(data, 0, data.length-1);
      try { 
				synchronized (this) {
					while (threadSuspended) {
						wait();
					}
					threadSuspended = true;
				}
      } catch (InterruptedException e) {}
    }
  }

	public synchronized void actionPerformed(ActionEvent e) {
    threadSuspended = false;
    notify();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("クイックソート");
      frame.add(new QuickSort2());
      frame.pack();
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    });
  }
}