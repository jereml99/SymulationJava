package Symulacja.Buttons;

import Symulacja.Swiat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveButton extends JButton implements ActionListener {
    Swiat swiatGry;
    public SaveButton(Swiat swiatGry){
        super("zapisz");
        addActionListener(this);
        this.swiatGry=swiatGry;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        swiatGry.zapiszStan();
    }
}
