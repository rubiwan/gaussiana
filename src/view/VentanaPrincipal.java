package view;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import config.ImageConfigFile;
import config.LoggerFichero;
import exception.InputException;
import exception.MatrixException;
import logic.Sistema;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Ventana principal de la aplicación.
 * Permite al usuario introducir un sistema de 4 ecuaciones lineales con 4 incógnitas,
 * y resolverlo mediante el método de eliminación gaussiana con pivotaje parcial escalado.
 *
 * @author Anabel Diaz
 * @version 1.0 - 22/11/2025
 */
public class VentanaPrincipal extends JFrame {

    private static final int N = 4;
    private static final LoggerFichero log = LoggerFichero.getInstance();

    private final JTextField[][] coef = new JTextField[N][N];
    private final JTextField[] constantes = new JTextField[N];
    private final JLabel[] lblResultados = new JLabel[N];
    private final JTextPane txtMatrizTriangular = new JTextPane();

    private final JButton btnCalcular = new JButton("Calcular");
    private final JButton btnBorrar = new JButton("Borrar");
    private final JButton btnCargar = new JButton("Prueba Laboratorio");
    private final JButton btnMostrar = new JButton("Mostrar sistema");

    public VentanaPrincipal() {

        setTitle("Eliminación Gaussiana - Pivotaje Parcial Escalado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel entradas = new JPanel(new BorderLayout(5, 5));

        JLabel lblIntro = new JLabel("Introduce tu sistema (cada fila es una ecuación):");
        lblIntro.setFont(lblIntro.getFont().deriveFont(Font.BOLD));
        lblIntro.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        entradas.add(lblIntro, BorderLayout.NORTH);

        // Panel que contiene las 4 ecuaciones
        JPanel panelEcuaciones = new JPanel();
        panelEcuaciones.setLayout(new BoxLayout(panelEcuaciones, BoxLayout.Y_AXIS));
        panelEcuaciones.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        Font fontCampos = new Font("SansSerif", Font.PLAIN, 14);
        Dimension dimCampo = new Dimension(50, 24);

        for (int i = 0; i < N; i++) {
            JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
            fila.setAlignmentX(Component.LEFT_ALIGNMENT);

            for (int j = 0; j < N; j++) {
                coef[i][j] = new JTextField(4);
                coef[i][j].setHorizontalAlignment(JTextField.RIGHT);
                coef[i][j].setPreferredSize(dimCampo);
                coef[i][j].setFont(fontCampos);
                coef[i][j].setToolTipText("Coeficiente de x" + (j + 1) + " en la ecuación " + (i + 1));
            }
            constantes[i] = new JTextField(4);
            constantes[i].setHorizontalAlignment(JTextField.RIGHT);
            constantes[i].setPreferredSize(dimCampo);
            constantes[i].setFont(fontCampos);
            constantes[i].setToolTipText("Término independiente de la ecuación " + (i + 1));


            fila.add(coef[i][0]);
            fila.add(new JLabel("x₁ +"));

            fila.add(coef[i][1]);
            fila.add(new JLabel("x₂ +"));

            fila.add(coef[i][2]);
            fila.add(new JLabel("x₃ +"));

            fila.add(coef[i][3]);
            fila.add(new JLabel("x₄ ="));

            fila.add(constantes[i]);

            panelEcuaciones.add(fila);
        }


        JPanel matrizYConst = new JPanel(new BorderLayout());
        matrizYConst.setBorder(
                BorderFactory.createTitledBorder("Sistema de ecuaciones (Ax = b)")
        );
        matrizYConst.add(panelEcuaciones, BorderLayout.CENTER);

        entradas.add(matrizYConst, BorderLayout.CENTER);

        // centrar
        JPanel panelResultados = new JPanel(new GridBagLayout());
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));

        JPanel filaResultados = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        Font fontResultado = new Font("SansSerif", Font.BOLD, 14);

        for (int i = 0; i < N; i++) {
            lblResultados[i] = new JLabel("x" + (i + 1) + " = ");
            lblResultados[i].setFont(fontResultado);
            lblResultados[i].setToolTipText("Valor de x" + (i + 1) + " tras resolver el sistema");
            lblResultados[i].setHorizontalAlignment(SwingConstants.CENTER);
            filaResultados.add(lblResultados[i]);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        panelResultados.add(filaResultados, gbc);

        txtMatrizTriangular.setEditable(false);
        txtMatrizTriangular.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtMatrizTriangular.setOpaque(false);

        Dimension dimScroll = new Dimension(300, 120);
        JScrollPane scrollMatriz = new JScrollPane();
        scrollMatriz.setPreferredSize(dimScroll);
        scrollMatriz.setMinimumSize(new Dimension(200, 120));
        scrollMatriz.setMaximumSize(new Dimension(Short.MAX_VALUE, 400));

        JPanel viewportPanel = new JPanel(new GridBagLayout());
        viewportPanel.setOpaque(false);
        viewportPanel.add(txtMatrizTriangular);

        viewportPanel.setPreferredSize(new Dimension(dimScroll.width - 8, dimScroll.height - 8));

        scrollMatriz.setViewportView(viewportPanel);
        scrollMatriz.setViewportBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        JPanel panelMatrizTriangular = new JPanel(new BorderLayout());
        panelMatrizTriangular.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Matriz triangular superior (U | b̃)"),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                )
        );

        scrollMatriz.setViewportBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        panelMatrizTriangular.add(scrollMatriz, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botones.add(btnCargar);
        botones.add(btnMostrar);
        botones.add(btnCalcular);
        botones.add(btnBorrar);


        btnCalcular.setToolTipText("Resuelve el sistema usando eliminación gaussiana con pivotaje parcial escalado.");
        btnBorrar.setToolTipText("Limpia todas las casillas y resultados.");
        btnCargar.setToolTipText("Carga un sistema de ejemplo en los campos.");
        btnMostrar.setToolTipText("Muestra el sistema de ecuaciones que has introducido.");

        aplicarEstiloBoton(btnCalcular, new Color(0x2E8B57), Color.WHITE);
        aplicarEstiloBoton(btnBorrar,  new Color(0xD9534F), Color.WHITE);
        aplicarEstiloBoton(btnCargar,  new Color(0x0275D8), Color.WHITE);
        aplicarEstiloBoton(btnMostrar, new Color(0x6C757D), Color.WHITE);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        entradas.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelResultados.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelMatrizTriangular.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel imgPanel = ImageConfigFile.createImagePanelFromFile("Ficheros/gauss.jpg", true);

        imgPanel.setPreferredSize(new Dimension(100, 100));

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(imgPanel, BorderLayout.WEST);
        panelSuperior.add(entradas, BorderLayout.CENTER);
        panelSuperior.setAlignmentX(Component.LEFT_ALIGNMENT);

        centro.add(panelSuperior);
        centro.add(Box.createVerticalStrut(10));
        centro.add(panelResultados);
        centro.add(Box.createVerticalStrut(10));
        centro.add(panelMatrizTriangular);

        main.add(centro, BorderLayout.CENTER);
        main.add(botones, BorderLayout.SOUTH);

        setContentPane(main);
    }

    public void addCalcularListener(ActionListener l) {
        btnCalcular.addActionListener(l);
    }

    public void addBorrarListener(ActionListener l) {
        btnBorrar.addActionListener(l);
    }

    public void addCargarListener(ActionListener l) {
        btnCargar.addActionListener(l);
    }

    public void addMostrarListener(ActionListener l) {
        btnMostrar.addActionListener(l);
    }

    /**
     * Muestra los resultados en las etiquetas correspondientes.
     *
     * @param x array con los valores de las incógnitas
     */
    public void mostrarResultados(double[] x) {
        for (int i = 0; i < N; i++) {
            lblResultados[i].setText(String.format("x%d = %.6f", i + 1, x[i]));
        }
    }

    /**
     * Limpia las etiquetas de resultados y el área de texto de la matriz triangular.
     */
    public void limpiarResultados() {
        for (int i = 0; i < N; i++) {
            lblResultados[i].setText("x" + (i+1) + " = ");
        }
        txtMatrizTriangular.setText("");
    }

    /**
     * Limpia todos los campos de entrada y los resultados.
     */
    public void limpiarCampos() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                coef[i][j].setText("");
            }
            constantes[i].setText("");
        }
        limpiarResultados();
    }

    /**
     * Muestra un cuadro de diálogo con un mensaje.
     *
     * @param mensaje el mensaje a mostrar
     * @param titulo  el título del cuadro de diálogo
     * @param tipo    el tipo de mensaje (JOptionPane.INFORMATION_MESSAGE, etc.)
     */
    public void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }

    /**
     * Proporciona un sistema de ejemplo para cargar en los campos.
     * Nota: sistema utilizado en la presentación de la actividad
     *
     * @return matriz A del sistema de ejemplo
     */
    public double[][] getEjemploA() {
        return new double[][]{
                {0.001, 1, 2, 3},
                {1, 2, 3, 4},
                {0.002, -1, 0, 1},
                {0.5, 0, 0, 1}
        };
    }

    /**
     * Proporciona un sistema de ejemplo para cargar en los campos.
     * Nota: sistema utilizado en la presentación de la actividad.
     *
     * @return vector b del sistema de ejemplo
     */
    public double[] getEjemploB() {
        return new double[]{1, 2, 3, 4};
    }

    /**
     * Carga el sistema de ejemplo en los campos de texto.
     *
     * @param A matriz de coeficientes del sistema
     * @param b vector de términos independientes del sistema
     */
    public void cargarEjemplo(double[][] A, double[] b) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                coef[i][j].setText(String.valueOf(A[i][j]));
            }
            constantes[i].setText(String.valueOf(b[i]));
        }
    }

    /**
     * Lee los campos de texto, valida vacíos, convierte comas a punto,
     * valida que sean numéricos y construye las matrices A y b.
     *
     * @return un objeto Sistema con A y b
     * @throws InputException si alguno de los campos es inválido
     */
    public Sistema leerSistema() throws InputException {

        double[][] A = new double[N][N];
        double[] b = new double[N];

        for (int i = 0; i < N; i++) {

            for (int j = 0; j < N; j++) {

                String txt = coef[i][j].getText().trim();

                if (txt.isEmpty()) {
                    throw new InputException(
                            "El coeficiente de x" + (j + 1) + " en la ecuación " + (i + 1) + " está vacío."
                    );
                }

                txt = txt.replace(',', '.');

                if (!esNumeroValido(txt)) {
                    throw new InputException(
                            "El valor \"" + txt + "\" en A[" + (i + 1) + "][" + (j + 1) +
                                    "] no es numérico."
                    );
                }

                A[i][j] = Double.parseDouble(txt);
            }

            String txtB = constantes[i].getText().trim();

            if (txtB.isEmpty()) {
                throw new InputException(
                        "El término independiente de la ecuación " + (i + 1) + " está vacío."
                );
            }

            txtB = txtB.replace(',', '.');

            if (!esNumeroValido(txtB)) {
                throw new InputException(
                        "El valor \"" + txtB + "\" en b[" + (i + 1) + "] no es numérico."
                );
            }

            b[i] = Double.parseDouble(txtB);
        }

        return new Sistema(A, b);
    }

    /**
     * Muestra la matriz triangular superior U y el vector modificado b̃ en el área de texto.
     *
     * @param U    matriz triangular superior
     * @param bMod vector modificado
     */
    public void mostrarMatrizTriangular(double[][] U, double[] bMod) {
        StringBuilder sb = new StringBuilder();
        int n = U.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(String.format("%10.4f", U[i][j]));
            }
            sb.append("   |");
            sb.append(String.format("%10.4f", bMod[i]));
            sb.append("\n");
        }

        txtMatrizTriangular.setText(sb.toString());

        StyledDocument doc = txtMatrizTriangular.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }


    /**
     * Valida si el texto es un número válido (entero o decimal)
     *
     * @param texto
     * @return true si es un número válido, false en caso contrario
     */
    private boolean esNumeroValido(String texto) {
        try {
            Double.parseDouble(texto);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /** Aplica estilo personalizado a un botón.
     *
     * @param btn el botón al que aplicar el estilo
     * @param bg  color de fondo
     * @param fg  color de texto
     */
    private void aplicarEstiloBoton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);

        Color hover = adjustBrightness(bg, 1.10f); 
        Color pressed = adjustBrightness(bg, 0.90f);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            @Override
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
            @Override
            public void mousePressed(MouseEvent e) { btn.setBackground(pressed); }
            @Override
            public void mouseReleased(MouseEvent e) { btn.setBackground(hover); }
        });
    }

    /**
     * Ajusta el hover de un color.
     *
     * @param color  el color original
     * @param factor el factor de ajuste (mayor que 1 para aclarar, menor que 1 para oscurecer)
     * @return el color ajustado
     */
    private Color adjustBrightness(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float brightness = Math.max(0f, Math.min(1f, hsb[2] * factor));
        return Color.getHSBColor(hsb[0], hsb[1], brightness);
    }

}

