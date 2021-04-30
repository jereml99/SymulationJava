package Symulacja.Buttons;

import Symulacja.MainFrame;
import Symulacja.Swiat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewGamePanel extends JPanel implements ActionListener {
    MainFrame mainFrame;
    TextField WysokoscTextField, SzerokoscTextField;
    public NewGamePanel(MainFrame mainFrame)
    {
        WysokoscTextField = new TextField("Wysoksc");
        SzerokoscTextField = new TextField("szerokosc");
        JButton newGameButton = new JButton("Nowa Gra");
        newGameButton.addActionListener(this);
        this.mainFrame = mainFrame;
        this.mainFrame.add(WysokoscTextField);
        this.mainFrame.add(SzerokoscTextField);
        this.mainFrame.add(newGameButton);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int szerokosc=Integer.parseInt(SzerokoscTextField.getText());
            int wysokosc=Integer.parseInt(WysokoscTextField.getText());


        if(Integer.valueOf(WysokoscTextField.getText())>0 &&Integer.valueOf(SzerokoscTextField.getText())>0) {
            mainFrame.getContentPane().removeAll();

            Swiat swiatGry = new Swiat(szerokosc, wysokosc, mainFrame);
            
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
        }
    } catch (NumberFormatException numberFormatException) {
            System.err.println("Podaj liczbe" );
            WysokoscTextField.setText("20");
            SzerokoscTextField.setText("20");
        }
    }
    }
