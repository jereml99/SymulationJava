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
import static Symulacja.Swiat.DOL;

public class Lis extends Zwierze {
    private static BufferedImage icon = null;

    static {
        try {
            icon = ImageIO.read(new File("icons\\lis.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Lis(int x, int y, Swiat swiatGry){
        super(x,y,swiatGry);
        this.sila = 3;
        this.inicjatywa = 7;
    }

    @Override
    protected void stworzZwierze(int x, int y) {
        swiatGry.dodajOrganizm(new Lis(x,y,this.swiatGry));
    }

    @Override
    public void kolizja(Organizm Przegrany) {
        if(Przegrany instanceof Lis)
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
    protected Boolean przesun(int przesuniecieX, int przesuniecieY) {
        if (x+przesuniecieX < 0 || x+przesuniecieX>=swiatGry.getSzerokosc() || y+przesuniecieY<0 || y+przesuniecieY>=swiatGry.getWysokoscSwiata() || swiatGry.mapa[x + przesuniecieX][y + przesuniecieY] != null)
        {
            if (x + przesuniecieX < 0 || x + przesuniecieX >= swiatGry.getSzerokosc() || y + przesuniecieY < 0 || y + przesuniecieY >= swiatGry.getWysokoscSwiata()) { return false; }
            else
            {
                if (swiatGry.mapa[x + przesuniecieX][y + przesuniecieY].getSila() > this.getSila())
                {
                   return false;//lis unika silnejszego przeciwnika
                }
                else
                {
                    swiatGry.commentArray.append(toString()+"Poruszyl sie na kratke x:"+(this.x +przesuniecieX)+" y:"+(this.y + przesuniecieY)+"\n      ");
                    if (swiatGry.mapa[x + przesuniecieX][y + przesuniecieY].czyOdbilAtak(this)) return true;
                    this.kolizja(swiatGry.mapa[x + przesuniecieX][y + przesuniecieY]);
                }
                return true;
            }
        }
        else
        {
            swiatGry.commentArray.append(toString()+"Poruszyl sie na kratke x:"+(this.x +przesuniecieX)+" y:"+(this.y + przesuniecieY)+"\n     ");
            if (swiatGry.mapa[x][y] == this)
            {
                swiatGry.mapa[x][y] = null;
            }
            swiatGry.mapa[x + przesuniecieX][y + przesuniecieY] = this;
            x+=przesuniecieX;
            y+= przesuniecieY;
            return true;
        }
    }

    @Override
    public void akcja() {
        boolean ruchSkaczony = false;
        int kierunek;
        boolean[] CzyWszystkieMozliwosci = new boolean[4];
        for (int i = 0; i < 4; i++)
        {
            CzyWszystkieMozliwosci[i] = false;
        }
        while (!ruchSkaczony)
        {
            for (int i = 0; i < 4; i++)
            {
                ruchSkaczony = true;
                if (CzyWszystkieMozliwosci[i] == false)
                {
                    ruchSkaczony = false;
                    break;
                }
            }
            if (ruchSkaczony)
            {
                swiatGry.commentArray.append(this+" nie mial bezpiecznego miejsca do przeniesienia \n       ");
                break;
            }
            kierunek = niciLosu.nextInt(4);
            switch (kierunek)
            {
                case LEWO:

                    ruchSkaczony=przesun(-1, 0);
                    CzyWszystkieMozliwosci[LEWO]=true;
                    break;
                case PRAWO:
                    ruchSkaczony = przesun(1, 0);
                    CzyWszystkieMozliwosci[PRAWO]=true;
                    break;
                case GORA:
                    ruchSkaczony = przesun(0, -1);
                    CzyWszystkieMozliwosci[GORA]=true;
                    break;
                case DOL:
                    ruchSkaczony = przesun(0, 1);
                    CzyWszystkieMozliwosci[DOL]=true;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void rysowanie(Graphics2D out) {
        if(icon == null)
        {
            out.setPaint(Color.red);
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
        return  "Lis o id: "+id+" ";
    }
}
