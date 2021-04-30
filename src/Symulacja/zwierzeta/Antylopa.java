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
import static Symulacja.Swiat.ROZMIAR_POLA;

public class Antylopa extends Zwierze {
    private static BufferedImage icon = null;

    static {
        try {
            icon = ImageIO.read(new File("icons\\antylopa.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Antylopa(int x, int y, Swiat swiatGry){
        super(x,y,swiatGry);
        this.sila = 4;
        this.inicjatywa = 4;
    }

    private boolean przesunNaWolne(int przesuniecieX,int przesuniecieY)
    {
        if (x + przesuniecieX < 0 || x + przesuniecieX >= swiatGry.getSzerokosc() || y + przesuniecieY < 0 || y + przesuniecieY >= swiatGry.getWysokoscSwiata() || swiatGry.mapa[x+przesuniecieX][y + przesuniecieY] != null)
        {
            return false;
        }
        else
        {
            swiatGry.commentArray.append(this+" uciekla na pole "+(this.x + przesuniecieX)+" y:"+(this.y + przesuniecieY)+"\n        ");
            if (swiatGry.mapa[x][y] == this)
            {
                swiatGry.mapa[x][y] = null;
            }
            swiatGry.mapa[x + przesuniecieX][y + przesuniecieY] = this;
            x += przesuniecieX;
            y += przesuniecieY;
            return true;
        }
    };

    @Override
    protected void stworzZwierze(int x, int y) {
        swiatGry.dodajOrganizm(new Antylopa(x,y,this.swiatGry));
    }

    @Override
    public void akcja() {
        super.akcja();
        if(!czyUsuwany)
        {
            super.akcja();
        }
    }

    @Override
    public Boolean czyOdbilAtak(Organizm atakujacy) {
        if (niciLosu.nextInt(100)   < 50)
        {
            if (!przesunNaWolne(1, 0))
            {
                if (!przesunNaWolne(-1, 0))
                {
                    if (!przesunNaWolne(0, 1))
                    {
                        if (!przesunNaWolne(0, -1))
                        {
                            swiatGry.commentArray.append(this + " Nie ma miejsca na ucieczke przed " + atakujacy+"\n        ");
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void kolizja(Organizm Przegrany) {
        if(Przegrany instanceof Antylopa)
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
            out.setPaint(Color.darkGray);
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
        return "Antylopa o id: "+id+" ";
    }
}
