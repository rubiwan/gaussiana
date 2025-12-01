package config;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Helpers para cargar imágenes desde fichero del sistema de archivos.
 *
 * @author Anabel Diaz
 * @version 1.0 - 22/11/2025
 */
public final class ImageConfigFile {

    private ImageConfigFile() {}

    public static JPanel createImagePanelFromFile(String filePath, boolean scaleToFit) {
        File f = new File(filePath);
        if (!f.exists() || !f.isFile()) {
            JPanel p = new JPanel(new BorderLayout());
            p.add(new JLabel("Imagen no encontrada: " + filePath, SwingConstants.CENTER), BorderLayout.CENTER);
            p.setPreferredSize(new Dimension(200, 100));
            return p;
        }

        BufferedImage img;
        try {
            img = ImageIO.read(f);
            if (img == null) throw new IOException("Formato no soportado o fichero vacío");
        } catch (IOException e) {
            JPanel p = new JPanel(new BorderLayout());
            p.add(new JLabel("Error al leer imagen: " + f.getName(), SwingConstants.CENTER), BorderLayout.CENTER);
            p.setPreferredSize(new Dimension(200, 100));
            return p;
        }

        final BufferedImage image = img;
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                if (scaleToFit) {
                    g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                } else {
                    int x = (getWidth() - image.getWidth()) / 2;
                    int y = (getHeight() - image.getHeight()) / 2;
                    g2.drawImage(image, Math.max(0, x), Math.max(0, y), this);
                }
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(image.getWidth(), image.getHeight());
            }
        };
    }
}
