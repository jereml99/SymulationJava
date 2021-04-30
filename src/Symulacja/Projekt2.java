package Symulacja;

import java.awt.*;

public class Projekt2 {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainFrame();
            }
        });
    }
}
