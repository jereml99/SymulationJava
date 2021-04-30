package Symulacja.Rosliny;

import Symulacja.Organizm;
import Symulacja.Roslina;
import Symulacja.Swiat;
import Symulacja.Zwierze;
import Symulacja.zwierzeta.CyberOwca;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static Symulacja.Swiat.*;
import static Symulacja.Swiat.ROZMIAR_POLA;

public class BarszczSosnowskiego extends Roslina {
    private static BufferedImage icon = null;

    static {
        try {
            icon = ImageIO.read(new File("icons\\BarszczSosnowskiego.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BarszczSosnowskiego(int x, int y, Swiat swiatGry){
        super(x,y,swiatGry);
        sila = 10;
        szansaNaRozsianie = 6;
    }
    private void zabijJesliZwierze(int przesuniecieX,int przesuniecieY){
        if (x + przesuniecieX < 0 || x + przesuniecieX >= swiatGry.getSzerokosc() || y + przesuniecieY < 0 || y + przesuniecieY >= swiatGry.getWysokoscSwiata())
        {
            return;
        }
        Organizm wOpszarzeRazenia = swiatGry.mapa[x + przesuniecieX][y + przesuniecieY];
        if (wOpszarzeRazenia != null)
        {
            if (wOpszarzeRazenia instanceof Zwierze)
            {
                if(wOpszarzeRazenia instanceof CyberOwca)
                {
                    swiatGry.commentArray.append( wOpszarzeRazenia + " jest odporna na poparzenie " + this+"\n      ");
                }
                else
                {
                    swiatGry.commentArray.append( wOpszarzeRazenia + " Zgina od poparzen spowdoanch przez " + this+"\n      ");
                    swiatGry.usunOrganizm(wOpszarzeRazenia);
                }

            }
        }
    }

    @Override
    protected void stworzNowaRosline(int x, int y) {
        swiatGry.dodajOrganizm(new BarszczSosnowskiego(x,y,this.swiatGry));
    }

    @Override
    public void akcja() {
        super.akcja();
        zabijJesliZwierze(0,1);
        zabijJesliZwierze(0,-1);
        zabijJesliZwierze(1,0);
        zabijJesliZwierze(-1,0);

    }

    @Override
    public void kolizja(Organizm Przegrany) {
        swiatGry.commentArray.append(Przegrany+" Zmarl po zjedzeniu "+this+"\n      ");
        swiatGry.usunOrganizm(this);
        swiatGry.usunOrganizm(Przegrany);
    }

    @Override
    public void rysowanie(Graphics2D out) {
        if(icon == null)
        {
            out.setPaint(new Color(0xCDCD12));
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
        return "BarszczSosnowskiego o id: "+id+" ";
    }
}
