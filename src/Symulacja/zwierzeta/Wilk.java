package Symulacja.zwierzeta;

import Symulacja.Organizm;
import Symulacja.Swiat;
import Symulacja.Zwierze;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static Symulacja.Swiat.*;

public class Wilk extends Zwierze {
    private static BufferedImage icon = null;

    static {
        try {
            icon = ImageIO.read(new File("icons\\wilk.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Wilk(int x, int y, Swiat swiatGry){
        super(x,y,swiatGry);
        this.sila = 9;
        this.inicjatywa = 5;
    }

    @Override
    protected void stworzZwierze(int x, int y) {
        swiatGry.dodajOrganizm(new Wilk(x,y,this.swiatGry));
    }

    @Override
    public void kolizja(Organizm Przegrany) {
        if(Przegrany instanceof Wilk)
        {
            swiatGry.commentArray.append(this.toString()+" prokreuje z "+Przegrany.toString()+"\n       ");
            prokreacja();
        }
        else
        {
            super.kolizja(Przegrany);
        }

    }

    @Override
    public void rysowanie(Graphics2D out) {
        if(icon == null)
        {
            out.setPaint(Color.gray);
            out.fillRect(x*40,y*40,40,40);
        }
        else
        {
            out.drawImage(icon,x*40,y*40,swiatGry.planszaPanel);
            out.setPaint(KOLOR_ID);
            out.drawString(String.valueOf(this.id),x*ROZMIAR_POLA+ROZMIAR_POLA-String.valueOf(this.id).length()*SZEROKOSC_CHARA,y*ROZMIAR_POLA+ROZMIAR_POLA);
        }
    }

    @Override
    public String toString() {
        return "Wilk o id: "+id+" ";
    }
}
