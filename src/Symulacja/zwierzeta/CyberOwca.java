package Symulacja.zwierzeta;

import Symulacja.Organizm;
import Symulacja.Rosliny.BarszczSosnowskiego;
import Symulacja.Swiat;
import Symulacja.Zwierze;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ListIterator;

import static Symulacja.Swiat.*;
import static Symulacja.Swiat.ROZMIAR_POLA;

public class CyberOwca extends Zwierze{
    private static BufferedImage icon = null;

    static {
        try {
            icon = ImageIO.read(new File("icons\\CyberOwca.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CyberOwca(int x, int y, Swiat swiatGry){
        super(x,y,swiatGry);
        this.sila = 11;
        this.inicjatywa = 4;
    }

    private int ZnajdzNajbliszyBarsz()
    {
        int kierunek = NIE_OKRESLONY;
        int odlegloscOdNajbliszego=swiatGry.getSzerokosc()+swiatGry.getWysokoscSwiata()+1;
        Organizm aktualnyOrganizm;
        Organizm najbliszyBarszc = null;
        ListIterator<Organizm> iterator = swiatGry.organizmyWedlogInicjatywy.listIterator(swiatGry.organizmyWedlogInicjatywy.size());
        while (iterator.hasPrevious())
        {
            aktualnyOrganizm=iterator.previous();
            if(aktualnyOrganizm.getInicjatywa()>0)
            {
                if(najbliszyBarszc!=null)
                {
                    if(najbliszyBarszc.getX() > this.x)
                    {
                        return PRAWO;
                    }
                    if(najbliszyBarszc.getX() < this.x)
                    {
                        return LEWO;
                    }
                    if(najbliszyBarszc.getY() < this.y)
                    {
                        return GORA;
                    }
                    if(najbliszyBarszc.getY() > this.y)
                    {
                        return DOL;
                    }
                }
                return kierunek;
            }
            if(aktualnyOrganizm instanceof BarszczSosnowskiego)
            {
                if(Math.abs(this.x-aktualnyOrganizm.getX())+Math.abs(this.y-aktualnyOrganizm.getY()) < odlegloscOdNajbliszego)
                {
                    najbliszyBarszc = aktualnyOrganizm;
                    odlegloscOdNajbliszego = Math.abs(this.x-aktualnyOrganizm.getX())+Math.abs(this.y-aktualnyOrganizm.getY());
                }
            }
        }
        return kierunek;
    }

    @Override
    protected void stworzZwierze(int x, int y) {
        swiatGry.dodajOrganizm(new CyberOwca(x,y,this.swiatGry));
    }

    @Override
    public void kolizja(Organizm Przegrany) {
        if(Przegrany instanceof CyberOwca)
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
    public void akcja() {
        int kierunek = ZnajdzNajbliszyBarsz();
        if(kierunek == NIE_OKRESLONY)
        {
            super.akcja();
        }
        else
        {
            switch (kierunek)
            {
                case LEWO:
                    przesun(-1, 0);
                    break;
                case PRAWO:
                   przesun(1, 0);
                    break;
                case GORA:
                  przesun(0, -1);
                    break;
                case DOL:
                    przesun(0, 1);
                    break;
                default:
                    swiatGry.commentArray.append("cos poszlo nie tak przy wykonaniu ruchu"+this+"\n     ");
                    break;
            }

        }
    }

    @Override
    public void rysowanie(Graphics2D out) {
        if(icon == null)
        {
            out.setPaint(Color.GREEN);
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
        return "CyberOwca o id: "+id+" ";
    }
}
