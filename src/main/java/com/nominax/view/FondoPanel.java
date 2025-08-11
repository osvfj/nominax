package com.nominax.view; 

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FondoPanel extends JPanel {

    private Image backgroundImage;

    public FondoPanel() {
        setOpaque(true); 
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/images/background.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            e.printStackTrace();
            setBackground(Color.LIGHT_GRAY); 
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

            // 2. Centrar la imagen (no escala, si es más pequeña)
            // int x = (getWidth() - backgroundImage.getWidth(null)) / 2;
            // int y = (getHeight() - backgroundImage.getHeight(null)) / 2;
            // g.drawImage(backgroundImage, x, y, this);

            // 3. Mosaico (tile) la imagen si es más pequeña (repetir)
            // TexturePaint texture = new TexturePaint(
            //     new BufferedImage(backgroundImage.getWidth(null), backgroundImage.getHeight(null), BufferedImage.TYPE_INT_ARGB),
            //     new Rectangle(0, 0, backgroundImage.getWidth(null), backgroundImage.getHeight(null))
            // );
            // ((Graphics2D) g).setPaint(texture);
            // g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}