package Symulacja.Buttons;

import Symulacja.MainFrame;
import Symulacja.Swiat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LoadButon extends JButton implements ActionListener{
    MainFrame mainFrame;
    public LoadButon(MainFrame mainFrame)
    {
        super("wczytaj");
        addActionListener(this);
        this.mainFrame=mainFrame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Scanner plikZapisu = new Scanner(new File("zapis.txt"));
            Swiat swiatGry = new Swiat(plikZapisu,mainFrame);

            mainFrame.getContentPane().removeAll();
            JButton newTurButton = new JButton("Nowa tura");
            SaveButton saveButton = new SaveButton(swiatGry);
            LoadButon loadButon = new LoadButon(mainFrame);
            newTurButton.addActionListener(swiatGry);

            mainFrame.addKeyListener(swiatGry.getGracz());
            mainFrame.addMouseListener(mainFrame);

            mainFrame.add(swiatGry.planszaPanel);
            JPanel panelPomocniczy = new JPanel(new GridLayout(0, 1));
            JPanel panelPomocniczyPrzyciski = new JPanel(new GridLayout(0, 1));

            panelPomocniczyPrzyciski.add(newTurButton);
            panelPomocniczyPrzyciski.add(saveButton);
            panelPomocniczyPrzyciski.add(loadButon);
            panelPomocniczy.add(panelPomocniczyPrzyciski);
            panelPomocniczy.add(swiatGry.graczArray);
            panelPomocniczy.add(swiatGry.commentArray);
            mainFrame.add(panelPomocniczy);
            mainFrame.pack();
            mainFrame.revalidate();
            mainFrame.setTitle("176653 Jeremi Ledwoń PO2020 " + "tura: " + swiatGry.tura);
            mainFrame.requestFocusInWindow();
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}
