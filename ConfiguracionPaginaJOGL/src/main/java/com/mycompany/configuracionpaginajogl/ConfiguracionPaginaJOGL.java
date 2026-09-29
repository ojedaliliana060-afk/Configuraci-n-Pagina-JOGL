/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.configuracionpaginajogl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfiguracionPaginaJOGL extends JFrame implements GLEventListener {

    private JSpinner spinMargenInferior;
    private JSpinner spinMargenSuperior;
    private JComboBox<String> comboOrientacion;
    private JButton btnInicializar;
    private GLJPanel glPanel;

    private int margenInferior = 10;
    private int margenSuperior = 5;
    private String orientacion = "Horizontal";

    private GLU glu = new GLU();

    public ConfiguracionPaginaJOGL() {
        setTitle("Configuración de Páginas - JOGL");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Configuración explícita del perfil
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // Usamos GLJPanel en lugar de GLCanvas para evitar el error de AWT en Windows
        glPanel = new GLJPanel(capabilities);
        glPanel.addGLEventListener(this);
        glPanel.setBounds(330, 150, 260, 160);
        add(glPanel);

        // Vista previa simple izquierda
        JLabel lblHoja = new JLabel("Hoja", SwingConstants.CENTER);
        lblHoja.setBounds(30, 20, 120, 20);
        lblHoja.setFont(new Font("SansSerif", Font.BOLD, 12));
        add(lblHoja);

        JPanel panelHojaIzquierda = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLUE);
                g2.drawRect(5, 5, 100, 150);
                g2.setColor(Color.RED);
                g2.drawLine(5, 20, 105, 20);
                g2.drawLine(5, 140, 105, 140);
            }
        };
        panelHojaIzquierda.setBounds(30, 45, 115, 165);
        add(panelHojaIzquierda);

        // Controles de Margen
        JLabel lblMargenInf = new JLabel("Margen inferior");
        lblMargenInf.setBounds(180, 20, 120, 20);
        lblMargenInf.setFont(new Font("SansSerif", Font.BOLD, 12));
        add(lblMargenInf);

        spinMargenInferior = new JSpinner(new SpinnerNumberModel(margenInferior, 0, 40, 1));
        spinMargenInferior.setBounds(180, 45, 60, 30);
        add(spinMargenInferior);

        JLabel lblMargenSup = new JLabel("Margen superior");
        lblMargenSup.setBounds(180, 100, 120, 20);
        lblMargenSup.setFont(new Font("SansSerif", Font.BOLD, 12));
        add(lblMargenSup);

        spinMargenSuperior = new JSpinner(new SpinnerNumberModel(margenSuperior, 0, 40, 1));
        spinMargenSuperior.setBounds(180, 125, 60, 30);
        add(spinMargenSuperior);

        // Control de Orientación
        JLabel lblOrientacion = new JLabel("Orientación de página.");
        lblOrientacion.setBounds(330, 20, 200, 20);
        lblOrientacion.setFont(new Font("SansSerif", Font.BOLD, 12));
        add(lblOrientacion);

        comboOrientacion = new JComboBox<>(new String[]{"Horizontal", "Vertical"});
        comboOrientacion.setSelectedItem(orientacion);
        comboOrientacion.setBounds(330, 45, 200, 25);
        add(comboOrientacion);

        // Botón Reset
        btnInicializar = new JButton("Inicializar");
        btnInicializar.setBounds(50, 240, 180, 30);
        add(btnInicializar);

        // Escuchadores
        ChangeListener changeListener = e -> {
            margenInferior = (int) spinMargenInferior.getValue();
            margenSuperior = (int) spinMargenSuperior.getValue();
            glPanel.repaint();
        };

        spinMargenInferior.addChangeListener(changeListener);
        spinMargenSuperior.addChangeListener(changeListener);

        comboOrientacion.addActionListener(e -> {
            orientacion = (String) comboOrientacion.getSelectedItem();
            glPanel.repaint();
        });

        btnInicializar.addActionListener(e -> {
            spinMargenInferior.setValue(0);
            spinMargenSuperior.setValue(0);
            comboOrientacion.setSelectedItem("Vertical");
            margenInferior = 0;
            margenSuperior = 0;
            orientacion = "Vertical";
            glPanel.repaint();
        });
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.93f, 0.93f, 0.93f, 1.0f);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        float anchoHoja = orientacion.equals("Horizontal") ? 160.0f : 100.0f;
        float altoHoja = orientacion.equals("Horizontal") ? 100.0f : 160.0f;

        float xMin = -anchoHoja / 2.0f;
        float xMax = anchoHoja / 2.0f;
        float yMin = -altoHoja / 2.0f;
        float yMax = altoHoja / 2.0f;

        // Fondo hoja
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2f(xMin, yMin);
            gl.glVertex2f(xMax, yMin);
            gl.glVertex2f(xMax, yMax);
            gl.glVertex2f(xMin, yMax);
        gl.glEnd();

        // Borde rojo
        gl.glColor3f(0.8f, 0.0f, 0.0f);
        gl.glLineWidth(2.0f);
        gl.glBegin(GL2.GL_LINE_LOOP);
            gl.glVertex2f(xMin, yMin);
            gl.glVertex2f(xMax, yMin);
            gl.glVertex2f(xMax, yMax);
            gl.glVertex2f(xMin, yMax);
        gl.glEnd();

        // Márgenes azules
        float posMargenSup = yMax - margenSuperior;
        float posMargenInf = yMin + margenInferior;

        gl.glColor3f(0.0f, 0.2f, 0.8f);
        gl.glLineWidth(1.5f);
        gl.glBegin(GL2.GL_LINES);
            if (posMargenSup >= yMin && posMargenSup <= yMax) {
                gl.glVertex2f(xMin, posMargenSup);
                gl.glVertex2f(xMax, posMargenSup);
            }
            if (posMargenInf >= yMin && posMargenInf <= yMax) {
                gl.glVertex2f(xMin, posMargenInf);
                gl.glVertex2f(xMax, posMargenInf);
            }
        gl.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        if (height <= 0) height = 1;

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluOrtho2D(-120.0, 120.0, -100.0, 100.0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConfiguracionPaginaJOGL app = new ConfiguracionPaginaJOGL();
            app.setVisible(true);
        });
    }
}