package Symulacja;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import static Symulacja.Swiat.*;

public abstract class Roslina extends Organizm{
    protected final Random sloneczkoIDeszczyk = new Random();
    protected int szansaNaRozsianie;
    public Roslina(int x, int y, Swiat swiatGry) {
        super(x, y, swiatGry);
        inicjatywa = 0;
        sila = 0;
    }
    protected abstract void stworzNowaRosline(int x,int y);
    protected boolean rozprzestrzen(int przesuniecieX, int przesuniecieY)
    {
        if (x + przesuniecieX < 0 || x + przesuniecieX >= swiatGry.getSzerokosc() || y + przesuniecieY < 0 || y + przesuniecieY >= swiatGry.getWysokoscSwiata() || swiatGry.mapa[x + przesuniecieX][y + przesuniecieY] != null)
        {
            return false;
        }
        else
        {
            swiatGry.commentArray.append(this+ " rozprzestrzenil sie na kratke x:" + (this.x+przesuniecieX) + " y:" + (this.y+przesuniecieY) + "\n      ");
            stworzNowaRosline(this.x + przesuniecieX, this.y + przesuniecieY);
            return true;
        }
    }

    @Override
    public void akcja() {
        int los = sloneczkoIDeszczyk.nextInt(100);
        if (los < szansaNaRozsianie)
        {

            Boolean rosiewanieSkaczone = false;
            Boolean[] CzyWszystkieMozliwosci= new Boolean[4];
            for (int i = 0; i < 4; i++)
            {
                CzyWszystkieMozliwosci[i] = false;
            }
            int Kierunek;
            while (!rosiewanieSkaczone)
            {
                for (int i = 0; i < 4; i++)
                {
                    rosiewanieSkaczone = true;
                    if (CzyWszystkieMozliwosci[i] == false)
                    {
                        rosiewanieSkaczone = false;
                        break;
                    }
                }
                if (rosiewanieSkaczone)
                {
                    swiatGry.commentArray.append( this + "Nie ma miejsca na rozsianie \n        ");
                    break;
                }
                Kierunek = sloneczkoIDeszczyk.nextInt(4);
                switch (Kierunek)
                {
                    case GORA:
                        rosiewanieSkaczone = rozprzestrzen(0, -1);
                        CzyWszystkieMozliwosci[GORA] = !rosiewanieSkaczone;
                        break;
                    case DOL:
                        rosiewanieSkaczone = rozprzestrzen(0, 1);
                        CzyWszystkieMozliwosci[DOL] = !rosiewanieSkaczone;
                        break;
                    case PRAWO:
                        rosiewanieSkaczone = rozprzestrzen(1, 0);
                        CzyWszystkieMozliwosci[PRAWO] = !rosiewanieSkaczone;
                        break;
                    case LEWO:
                        rosiewanieSkaczone = rozprzestrzen(-1, 0);
                        CzyWszystkieMozliwosci[LEWO] = !rosiewanieSkaczone;
                        break;
                    default:
                        break;
                }
            }
        }
        else
        {
             swiatGry.commentArray.append(this + " Nie rozsiala sie w tej turze, brakowalo jej " + (los - szansaNaRozsianie) + "ptk. proc \n        ");
        }
    }

    @Override
    public void kolizja(Organizm Przegrany) {
        swiatGry.commentArray.append(this+" Jestem zwykla roslina nie powinnam nikogo atakowac \n       ");
    }

    @Override
    public Boolean czyOdbilAtak(Organizm atakujacy) {
        return false;
    }

    @Override
    public void zapiszDoPiliku(FileWriter plikZapisu) {
        try {
            plikZapisu.write(this+""+x+" "+y+"\n");
        } catch (IOException e) {
            System.err.println(this+" nie udalo sie zapisac");
        }
    }
}
