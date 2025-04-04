package pkg03.des;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.security.*;

public class InterfazDES extends JFrame {

    private JTextArea textoArea;
    private JButton btnCargar, btnCifrar, btnDescifrar;
    private File archivoSeleccionado;
    private SecretKey clave;
    private Cipher cifrador;

    public InterfazDES() {
        setTitle("Cifrado y Descifrado con DES");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textoArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(textoArea);
        add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();

        btnCargar = new JButton("Cargar Archivo");
        btnCifrar = new JButton("Cifrar");
        btnDescifrar = new JButton("Descifrar");

        panelBotones.add(btnCargar);
        panelBotones.add(btnCifrar);
        panelBotones.add(btnDescifrar);

        add(panelBotones, BorderLayout.SOUTH);

        inicializarEventos();
    }

    private void inicializarEventos() {
        btnCargar.addActionListener(e -> cargarArchivo());
        btnCifrar.addActionListener(e -> cifrarArchivo());
        btnDescifrar.addActionListener(e -> descifrarArchivo());
    }

    private void cargarArchivo() {
        JFileChooser chooser = new JFileChooser();
        int opcion = chooser.showOpenDialog(this);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = chooser.getSelectedFile();
            textoArea.setText(leerContenido(archivoSeleccionado));
        }
    }

    private String leerContenido(File archivo) {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo.");
        }
        return contenido.toString();
    }

    private void cifrarArchivo() {
        if (archivoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Primero debes cargar un archivo.");
            return;
        }

        try {
            // Generar clave y cifrador
            KeyGenerator generadorDES = KeyGenerator.getInstance("DES");
            generadorDES.init(56);
            clave = generadorDES.generateKey();
            cifrador = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cifrador.init(Cipher.ENCRYPT_MODE, clave);

            // Rutas
            File archivoCifrado = new File(archivoSeleccionado.getAbsolutePath() + ".cifrado");

            try (FileInputStream entrada = new FileInputStream(archivoSeleccionado);
                 FileOutputStream salida = new FileOutputStream(archivoCifrado)) {

                byte[] buffer = new byte[1000];
                int bytesLeidos;

                while ((bytesLeidos = entrada.read(buffer)) != -1) {
                    byte[] cifrado = cifrador.update(buffer, 0, bytesLeidos);
                    salida.write(cifrado);
                }

                byte[] cifFinal = cifrador.doFinal();
                salida.write(cifFinal);

                JOptionPane.showMessageDialog(this, "Archivo cifrado correctamente:\n" + archivoCifrado.getName());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cifrar.");
        }
    }

    private void descifrarArchivo() {
        if (archivoSeleccionado == null || clave == null) {
            JOptionPane.showMessageDialog(this, "Primero debes cargar y cifrar un archivo.");
            return;
        }

        try {
            File archivoCifrado = new File(archivoSeleccionado.getAbsolutePath() + ".cifrado");
            File archivoDescifrado = new File(archivoSeleccionado.getAbsolutePath() + ".descifrado");

            cifrador.init(Cipher.DECRYPT_MODE, clave);

            try (FileInputStream entrada = new FileInputStream(archivoCifrado);
                 FileOutputStream salida = new FileOutputStream(archivoDescifrado)) {

                byte[] buffer = new byte[1000];
                int bytesLeidos;

                while ((bytesLeidos = entrada.read(buffer)) != -1) {
                    byte[] descifrado = cifrador.update(buffer, 0, bytesLeidos);
                    salida.write(descifrado);
                }

                byte[] descFinal = cifrador.doFinal();
                salida.write(descFinal);

                textoArea.setText(leerContenido(archivoDescifrado));
                JOptionPane.showMessageDialog(this, "Archivo descifrado correctamente:\n" + archivoDescifrado.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al descifrar.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfazDES().setVisible(true);
        });
    }
}
