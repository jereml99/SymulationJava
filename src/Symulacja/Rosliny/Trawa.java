package Symulacja.Rosliny;


import Symulacja.Roslina;
import Symulacja.Swiat;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static Symulacja.Swiat.*;
import static Symulacja.Swiat.ROZMIAR_POLA;

public class Trawa extends Roslina {

    private static BufferedImage icon = null;

    static {
        try {
            icon = ImageIO.read(new File("icons\\trawa.png"));//grafika By Freepik
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Trawa(int x, int y, Swiat swiatGry){
        super(x,y,swiatGry);
        szansaNaRozsianie = 23;
    }

    @Override
    protected void stworzNowaRosline(int x, int y) {
        swiatGry.dodajOrganizm(new Trawa(x,y,this.swiatGry));
    }


    @Override
    public void rysowanie(Graphics2D out) {
        if(icon == null)
        {
            out.setPaint(new Color(0x2BF611));
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
        return "Trawa o id: "+id+" ";
    }
}
