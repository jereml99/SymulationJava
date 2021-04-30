package Symulacja;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import static Symulacja.Swiat.*;

public abstract class Zwierze extends Organizm {
    protected final Random  niciLosu = new Random();
    public Zwierze(int x, int y, Swiat swiatGry) {
        super(x, y, swiatGry);
    }
    protected abstract void stworzZwierze(int x,int y);
    protected Boolean przesun(int przesuniecieX,int przesuniecieY)
    {
        if (x+przesuniecieX < 0 || x+przesuniecieX>=swiatGry.getSzerokosc() || y+przesuniecieY<0 || y+przesuniecieY>=swiatGry.getWysokoscSwiata() || swiatGry.mapa[x + przesuniecieX][y + przesuniecieY] != null)
        {
            if (x + przesuniecieX < 0 || x + przesuniecieX >= swiatGry.getSzerokosc() || y + przesuniecieY < 0 || y + przesuniecieY >= swiatGry.getWysokoscSwiata()) { return false; }
            else
            {
                swiatGry.commentArray.append(toString()+"Poruszyl sie na kratke x:"+(this.x +przesuniecieX)+" y:"+(this.y + przesuniecieY)+"\n      ");
                if (swiatGry.mapa[x + przesuniecieX][y + przesuniecieY].getSila() > this.getSila())
                {
                    if (swiatGry.mapa[x + przesuniecieX][y + przesuniecieY].czyOdbilAtak(this)) return true;
                    swiatGry.mapa[x + przesuniecieX][y + przesuniecieY].kolizja(this);
                }
			    else
                {
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
    protected void prokreacja(){
        boolean tworzenieSkaczone = false;
        boolean[] CzyWszystkieMozliwosci = new boolean[4];
        for (int i = 0; i < 4; i++)
        {
            CzyWszystkieMozliwosci[i] = false;
        }
        int Kierunek;
        while (!tworzenieSkaczone)
        {
            for (int i = 0; i < 4; i++)
            {
                tworzenieSkaczone = true;
                if (!CzyWszystkieMozliwosci[i])
                {
                    tworzenieSkaczone = false;
                    break;
                }
            }
            if (tworzenieSkaczone)
            {
                swiatGry.commentArray.append(this+"Nie ma miejsca na Prokreacje \n");
                break;
            }
            Kierunek = niciLosu.nextInt(4);
            switch (Kierunek)
            {
                case GORA:
                    if (y - 1 >= 0 && swiatGry.mapa[x][y - 1] == null)
                    {
                        stworzZwierze(x, y - 1);
                        tworzenieSkaczone = true;
                    }
                    else CzyWszystkieMozliwosci[GORA] = true;
                    break;
                case DOL:
                    if (y + 1 < swiatGry.getWysokoscSwiata() && swiatGry.mapa[x][y + 1] == null)
                    {
                        stworzZwierze(x, y + 1);
                        tworzenieSkaczone = true;
                    }
                    else CzyWszystkieMozliwosci[DOL] = true;
                    break;
                case PRAWO:
                    if (x + 1 < swiatGry.getSzerokosc() && swiatGry.mapa[x + 1][y] == null)
                    {
                        stworzZwierze(x+1, y);
                        tworzenieSkaczone = true;
                    }
                    else CzyWszystkieMozliwosci[PRAWO] = true;
                    break;
                case LEWO:
                    if (x - 1 >= 0 && swiatGry.mapa[x-1][y] == null)
                    {
                        stworzZwierze(x-1, y);
                        tworzenieSkaczone = true;
                    }
                    else CzyWszystkieMozliwosci[LEWO] = true;
                    break;
                default:
                    break;
            }
        }
    }
    @Override
    public void akcja() {
        boolean ruchSkaczony = false;
        int kierunek;
        while (!ruchSkaczony)
        {
            kierunek = niciLosu.nextInt(4);
            switch (kierunek)
            {
                case LEWO:

                    ruchSkaczony=przesun(-1, 0);
                    break;
                case PRAWO:
                    ruchSkaczony = przesun(1, 0);
                    break;
                case GORA:
                    ruchSkaczony = przesun(0, -1);
                    break;
                case DOL:
                    ruchSkaczony = przesun(0, 1);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void kolizja(Organizm Przegrany) {
        swiatGry.commentArray.append(Przegrany.toString()+" Zostal zjedzony przez "+this.toString()+"\n");
            if(czyMojaTura) {
                if(swiatGry.mapa[x][y]==this)
                {
                    swiatGry.mapa[x][y]=null;
                }
                this.x=Przegrany.getX();
                this.y=Przegrany.getY();
            }
            swiatGry.usunOrganizm(Przegrany);
            swiatGry.mapa[this.x][this.y]=this;
    }


    @Override
    public Boolean czyOdbilAtak(Organizm atakujacy) {
        return false;
    }

    @Override
    public void zapiszDoPiliku(FileWriter plikZapisu) {
        try {
            plikZapisu.write(this+""+x+" "+y+" "+sila+"\n");
        } catch (IOException e) {
            System.err.println(this+" nie udalo sie zapisac");
        }
    }
}
