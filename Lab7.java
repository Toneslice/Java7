import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.*;

/**
 * Меню:
 *   Файл  → Перезапустити програму (Ctrl+R) | Вихід (Ctrl+Q)
 *   Довідка → Довідка по програмі (F1)      | Про програму
 *
 * Керування:
 *   Кнопка ▶/⏸ — пауза/старт анімації
 *   Повзунок    — швидкість обертання
 *   Кнопка 🎨   — колір заливки чотирикутника
 */
public class Lab7 extends JFrame {

    static final String APP_NAME = "Обертання чотирикутника";
    private AnimationPanel animPanel;


    public Lab7() {
        setTitle(APP_NAME);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(820, 640);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { confirmExit(); }
        });

        buildMenuBar();

        animPanel = new AnimationPanel();
        getContentPane().add(animPanel, BorderLayout.CENTER);

        setVisible(true);
        animPanel.startAnimation();
    }


    private void buildMenuBar() {
        JMenuBar bar = new JMenuBar();
        bar.setBackground(new Color(28, 28, 42));
        bar.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));

        /* ── Файл ── */
        JMenu mFile = darkMenu("Файл");

        JMenuItem miRestart = darkItem("Перезапустити програму");
        miRestart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        miRestart.addActionListener(e -> restartApp());

        JMenuItem miExit = darkItem("Вихід");
        miExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        miExit.addActionListener(e -> confirmExit());

        mFile.add(miRestart);
        mFile.addSeparator();
        mFile.add(miExit);

        /* ── Довідка ── */
        JMenu mHelp = darkMenu("Довідка");

        JMenuItem miHelp = darkItem("Довідка по " + APP_NAME);
        miHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        miHelp.addActionListener(e -> showHelp());

        JMenuItem miAbout = darkItem("Про програму");
        miAbout.addActionListener(e -> showAbout());

        mHelp.add(miHelp);
        mHelp.addSeparator();
        mHelp.add(miAbout);

        bar.add(mFile);
        bar.add(mHelp);
        setJMenuBar(bar);
    }

    private static JMenu darkMenu(String text) {
        JMenu m = new JMenu(text);
        m.setForeground(Color.WHITE);
        m.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return m;
    }

    private static JMenuItem darkItem(String text) {
        JMenuItem i = new JMenuItem(text);
        i.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return i;
    }


    private void restartApp() {
        animPanel.stopAnimation();
        dispose();
        SwingUtilities.invokeLater(Lab7::new);
    }

    private void confirmExit() {
        int r = JOptionPane.showConfirmDialog(this,
                "Ви справді бажаєте вийти?", "Вихід",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r == JOptionPane.YES_OPTION) { animPanel.stopAnimation(); System.exit(0); }
    }

    // ─── Help window

    private void showHelp() {
        JDialog dlg = new JDialog(this, "Довідка \u2014 " + APP_NAME, false);
        dlg.setSize(700, 540);
        dlg.setLocationRelativeTo(this);

        JTextPane tp = new JTextPane();
        tp.setContentType("text/html");
        tp.setEditable(false);
        tp.setText(helpHtml());
        tp.setCaretPosition(0);
        tp.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                try { Desktop.getDesktop().browse(e.getURL().toURI()); }
                catch (Exception ignored) {}
        });

        JScrollPane sp = new JScrollPane(tp);
        sp.setBorder(BorderFactory.createEmptyBorder());

        JButton close = new JButton("Закрити");
        close.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        close.addActionListener(e -> dlg.dispose());

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        south.add(close);

        dlg.setLayout(new BorderLayout());
        dlg.add(sp,    BorderLayout.CENTER);
        dlg.add(south, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private static String helpHtml() {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'>"
            + "<style>"
            + "body{font-family:'Segoe UI',Arial,sans-serif;font-size:14px;margin:22px 28px;color:#1a1a2e;background:#f5f6fb;}"
            + "h1{color:#1e3a8a;border-bottom:2px solid #1e3a8a;padding-bottom:7px;margin-bottom:14px;}"
            + "h2{color:#2563eb;margin-top:20px;}"
            + "p,li{line-height:1.7;}"
            + "ul{margin-top:4px;}"
            + "kbd{background:#e2e8f0;border:1px solid #cbd5e1;border-radius:4px;padding:1px 6px;font-family:monospace;}"
            + "pre{background:#e8eaf6;padding:12px;border-radius:6px;font-size:13px;line-height:1.5;}"
            + ".note{background:#dbeafe;border-left:4px solid #2563eb;padding:10px 16px;border-radius:4px;margin:16px 0;}"
            + ".formula{background:#f1f5ff;border:1px solid #c7d2fe;border-radius:6px;padding:10px;margin:10px 0;}"
            + "</style></head><body>"
            + "<h1>&#128196; Довідка &mdash; Обертання чотирикутника</h1>"
            + "<h2>&#9654; Призначення програми</h2>"
            + "<p>Програма демонструє <b>рівномірне обертання чотирикутника ABCD</b> навколо "
            + "його центру тяжіння (точка O) у графічній панелі вікна. "
            + "Анімація реалізована засобами Java Swing із використанням <code>javax.swing.Timer</code>.</p>"
            + "<h2>&#127919; Керування</h2>"
            + "<ul>"
            + "<li><b>&#9646;&#9646; Пауза / &#9654; Старт</b> &mdash; зупинити або відновити обертання.</li>"
            + "<li><b>Повзунок &laquo;Швидкість&raquo;</b> &mdash; змінити кутову швидкість (1&ndash;60 од.).</li>"
            + "<li><b>&#127912; Колір</b> &mdash; відкриває діалог вибору кольору заливки фігури.</li>"
            + "</ul>"
            + "<h2>&#128290; Клавіатурні скорочення</h2>"
            + "<ul>"
            + "<li><kbd>Ctrl+R</kbd> &mdash; Перезапустити програму.</li>"
            + "<li><kbd>Ctrl+Q</kbd> &mdash; Вийти з програми.</li>"
            + "<li><kbd>F1</kbd>     &mdash; Відкрити цю довідку.</li>"
            + "</ul>"
            + "<div class='note'><b>&#128204; Примітка:</b> Центр тяжіння (червона точка O) "
            + "завжди залишається нерухомим. Пунктирні лінії &mdash; це радіус-вектори від O до вершин.</div>"
            + "<h2>&#128203; Математична основа</h2>"
            + "<p>Поворот точки <i>(x,&thinsp;y)</i> навколо центру <i>(c<sub>x</sub>,&thinsp;c<sub>y</sub>)</i> "
            + "на кут <i>&theta;</i>:</p>"
            + "<div class='formula'><pre>"
            + "x' = c\u2093 + (x\u2212c\u2093)\u00B7cos(\u03B8) \u2212 (y\u2212c\u1D67)\u00B7sin(\u03B8)\n"
            + "y' = c\u1D67 + (x\u2212c\u2093)\u00B7sin(\u03B8) + (y\u2212c\u1D67)\u00B7cos(\u03B8)</pre></div>"
            + "<p>де кут &theta; збільшується на &omega; градусів щокадру (16&thinsp;мс/кадр &asymp; 60 FPS).</p>"
            + "</body></html>";
    }


    private void showAbout() {
        JDialog dlg = new JDialog(this, "Про програму", true);
        dlg.setSize(420, 300);
        dlg.setLocationRelativeTo(this);
        dlg.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(16, 16, 30));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.insets = new Insets(9, 12, 9, 12);

        g.gridy = 0;
        JLabel lTitle = new JLabel(APP_NAME);
        lTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lTitle.setForeground(new Color(125, 185, 255));
        panel.add(lTitle, g);

        g.gridy = 1;
        JLabel lVer = new JLabel("Версія 1.0.0");
        lVer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lVer.setForeground(Color.LIGHT_GRAY);
        panel.add(lVer, g);

        g.gridy = 2;
        JLabel lDesc = new JLabel("<html><center>"
                + "<font color='#aaaacc' size='3'>"
                + "Демонстрація обертання чотирикутника<br>"
                + "навколо центру тяжіння.<br>"
                + "Реалізовано на Java Swing."
                + "</font></center></html>");
        panel.add(lDesc, g);

        g.gridy = 3;
        JLabel lCopy = new JLabel("\u00A9 2025  Лабораторна робота");
        lCopy.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lCopy.setForeground(new Color(100, 100, 130));
        panel.add(lCopy, g);

        JButton ok = new JButton("OK");
        ok.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ok.addActionListener(e -> dlg.dispose());

        JPanel south = new JPanel();
        south.setBackground(new Color(16, 16, 30));
        south.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(50, 50, 75)));
        south.add(ok);

        dlg.setLayout(new BorderLayout());
        dlg.add(panel, BorderLayout.CENTER);
        dlg.add(south, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }


    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(Lab7::new);
    }


    static class AnimationPanel extends JPanel {

        private static final double[][] BASE = {
            {-115, -72},   // A
            { 128, -55},   // B
            {  95,  88},   // C
            { -78,  82}    // D
        };

        private double angle   = 0.0;
        private double omega   = 1.5;
        private Color  fillCol = new Color(65, 135, 225, 205);
        private boolean paused = false;
        private Timer timer;

        AnimationPanel() {
            setBackground(new Color(12, 12, 26));
            setLayout(new BorderLayout());
            add(buildBar(), BorderLayout.SOUTH);
        }

        // ─── Controls ────────────────────────────────────────────────────

        private JPanel buildBar() {
            JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 7));
            bar.setBackground(new Color(20, 20, 38));
            bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(50, 52, 82)));

            JButton btnPlay = makeBtn("\u23F8  Пауза");
            btnPlay.addActionListener(e -> {
                paused = !paused;
                btnPlay.setText(paused ? "\u25B6  Старт" : "\u23F8  Пауза");
            });

            JLabel lblSpeed = new JLabel("Швидкість:");
            lblSpeed.setForeground(new Color(185, 195, 215));
            lblSpeed.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            JSlider sldSpeed = new JSlider(1, 60, 15);
            sldSpeed.setOpaque(false);
            sldSpeed.setPreferredSize(new Dimension(155, 28));
            sldSpeed.addChangeListener(e -> omega = sldSpeed.getValue() / 10.0);

            JButton btnColor = makeBtn("\uD83C\uDFA8  Колір");
            btnColor.addActionListener(e -> {
                Color c = JColorChooser.showDialog(this, "Колір заливки", fillCol);
                if (c != null) fillCol = new Color(c.getRed(), c.getGreen(), c.getBlue(), 205);
            });

            bar.add(btnPlay);
            bar.add(Box.createHorizontalStrut(8));
            bar.add(lblSpeed);
            bar.add(sldSpeed);
            bar.add(Box.createHorizontalStrut(8));
            bar.add(btnColor);
            return bar;
        }

        private static JButton makeBtn(String text) {
            JButton b = new JButton(text);
            b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            b.setForeground(Color.WHITE);
            b.setBackground(new Color(46, 52, 82));
            b.setFocusPainted(false);
            b.setBorderPainted(false);
            b.setOpaque(true);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return b;
        }


        void startAnimation() {
            timer = new Timer(16, e -> {
                if (!paused) angle = (angle + omega) % 360.0;
                repaint();
            });
            timer.start();
        }

        void stopAnimation() {
            if (timer != null) timer.stop();
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int ctrlH = 48;
            int dw = getWidth(), dh = getHeight() - ctrlH;

            drawBg(g2, dw, dh);
            drawGrid(g2, dw, dh);
            drawQuad(g2, dw, dh);
            drawOverlay(g2);

            g2.dispose();
        }

        private void drawBg(Graphics2D g2, int w, int h) {
            g2.setPaint(new GradientPaint(0, 0, new Color(12, 12, 26),
                                          w, h, new Color(20, 16, 42)));
            g2.fillRect(0, 0, w, h);
        }

        private void drawGrid(Graphics2D g2, int w, int h) {
            g2.setColor(new Color(38, 40, 68, 115));
            g2.setStroke(new BasicStroke(0.5f));
            for (int x = 0; x <= w; x += 40) g2.drawLine(x, 0, x, h);
            for (int y = 0; y <= h; y += 40) g2.drawLine(0, y, w, y);
            // coordinate axes
            g2.setColor(new Color(75, 80, 125, 170));
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawLine(0, h/2, w, h/2);
            g2.drawLine(w/2, 0, w/2, h);
        }

        private void drawQuad(Graphics2D g2, int w, int h) {
            int cx = w / 2, cy = h / 2;
            double rad = Math.toRadians(angle);
            double cosA = Math.cos(rad), sinA = Math.sin(rad);

            int[] vx = new int[4], vy = new int[4];
            for (int i = 0; i < 4; i++) {
                double bx = BASE[i][0], by = BASE[i][1];
                vx[i] = cx + (int) Math.round(bx * cosA - by * sinA);
                vy[i] = cy + (int) Math.round(bx * sinA + by * cosA);
            }
            Polygon poly = new Polygon(vx, vy, 4);

            // Тінь
            g2.setColor(new Color(0, 0, 0, 65));
            g2.fill(new Polygon(
                new int[]{vx[0]+8, vx[1]+8, vx[2]+8, vx[3]+8},
                new int[]{vy[0]+8, vy[1]+8, vy[2]+8, vy[3]+8}, 4));

            Color c2 = new Color(
                Math.max(0, fillCol.getRed()   - 55),
                Math.max(0, fillCol.getGreen() - 45),
                Math.max(0, fillCol.getBlue()  - 25),
                fillCol.getAlpha());
            g2.setPaint(new GradientPaint(vx[0], vy[0], fillCol, vx[2], vy[2], c2));
            g2.fill(poly);

            g2.setPaint(new Color(205, 220, 255, 225));
            g2.setStroke(new BasicStroke(2.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(poly);

            float[] dash = {5.0f, 5.0f};
            g2.setStroke(new BasicStroke(0.9f, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
            g2.setColor(new Color(150, 175, 225, 85));
            for (int i = 0; i < 4; i++) g2.drawLine(vx[i], vy[i], cx, cy);

            char[] lbl = {'A', 'B', 'C', 'D'};
            for (int i = 0; i < 4; i++) {
                g2.setColor(new Color(255, 222, 60));
                g2.fillOval(vx[i]-5, vy[i]-5, 11, 11);
                g2.setColor(new Color(255, 255, 255, 210));
                g2.setStroke(new BasicStroke(1.3f));
                g2.drawOval(vx[i]-5, vy[i]-5, 11, 11);
                // мітка
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                g2.setColor(new Color(255, 235, 110));
                g2.drawString(String.valueOf(lbl[i]),
                        vx[i] + (vx[i] >= cx ? 9 : -18),
                        vy[i] + (vy[i] >= cy ? 17 : -8));
            }

            g2.setColor(new Color(250, 68, 68));
            g2.fillOval(cx-5, cy-5, 11, 11);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1.3f));
            g2.drawOval(cx-5, cy-5, 11, 11);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.setColor(new Color(255, 120, 120));
            g2.drawString("O (\u0446.\u0442.)", cx + 9, cy - 6);
        }

        private void drawOverlay(Graphics2D g2) {
            g2.setFont(new Font("Courier New", Font.PLAIN, 13));
            g2.setColor(new Color(140, 195, 255, 195));
            g2.drawString(String.format("\u03B8 = %6.1f\u00B0", angle),  14, 24);
            g2.drawString(String.format("\u03C9 = %.2f \u00B0/\u043A\u0430\u0434\u0440", omega), 14, 42);
        }
    }
}
