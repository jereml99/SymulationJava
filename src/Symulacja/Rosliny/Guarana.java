package Symulacja.Rosliny;

import Symulacja.Organizm;
import Symulacja.Roslina;
import Symulacja.Swiat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static Symulacja.Swiat.*;
import static Symulacja.Swiat.ROZMIAR_POLA;

public class Guarana extends Roslina {

    private static BufferedImage icon = null;

    static {
        try {
            icon = ImageIO.read(new File("icons\\guarana.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Guarana(int x, int y, Swiat swiatGry){
        super(x,y,swiatGry);
        szansaNaRozsianie = 20;
    }

    @Override
    protected void stworzNowaRosline(int x, int y) {
        swiatGry.dodajOrganizm(new Guarana(x,y,this.swiatGry));
    }

    @Override
    public Boolean czyOdbilAtak(Organizm atakujacy) {
        if (swiatGry.mapa[atakujacy.getX()][atakujacy.getY()] == atakujacy)
        {
            swiatGry.mapa[atakujacy.getX()][atakujacy.getY()] = null;
        }
        atakujacy.setX(this.x);
        atakujacy.setY(this.y);
        atakujacy.setSila(atakujacy.getSila() + 3);
        swiatGry.commentArray.append(atakujacy + " Zjadl " + this + " Zwiekszeni sily do: " + atakujacy.getSila()+"\n       ");
        swiatGry.mapa[atakujacy.getX()][atakujacy.getY()] = atakujacy;
        swiatGry.usunOrganizm(this);
        return true;
    }

    @Override
    public void rysowanie(Graphics2D out) {
        if(icon == null)
        {
            out.setPaint(new Color(0x9EF611));
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
        return "Guarana o id: "+id+" ";
    }
}
