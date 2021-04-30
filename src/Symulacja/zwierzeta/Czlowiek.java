package Symulacja.zwierzeta;

import Symulacja.Organizm;
import Symulacja.Swiat;
import Symulacja.Zwierze;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static Symulacja.Swiat.*;
import static Symulacja.Swiat.ROZMIAR_POLA;

public class Czlowiek extends Zwierze implements KeyListener {
    private static BufferedImage icon = null;
    private int kierunek;
    private int turaRozpoczenciaSuperUmiejsetnosci;
    static {
        try {
            icon = ImageIO.read(new File("icons\\Czlowiek.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean czyUmiejetnoscAktywan(){
        return (swiatGry.getTura() - turaRozpoczenciaSuperUmiejsetnosci <= 5 && turaRozpoczenciaSuperUmiejsetnosci!=0);
    }

    public Czlowiek(int x, int y, Swiat swiatGry){
        super(x,y,swiatGry);
        this.sila = 5;
        this.inicjatywa = 4;
        kierunek = GORA;
        turaRozpoczenciaSuperUmiejsetnosci =0;
    }
    public void uaktualnijOpis()
    {
        swiatGry.graczArray.setText(this+" o Sile:"+sila+"\nSterowanie strzalkami\n");
        if (czyUmiejetnoscAktywan())
        {
            swiatGry.graczArray.append("Tarcza Azuryna Wlaczona, potrwa jeszcze " + (5 - (swiatGry.getTura() - turaRozpoczenciaSuperUmiejsetnosci))+ " Tur\n");
        }
        if (swiatGry.getTura() - turaRozpoczenciaSuperUmiejsetnosci > 5 && turaRozpoczenciaSuperUmiejsetnosci != 0)
        {
            if (swiatGry.getTura() - turaRozpoczenciaSuperUmiejsetnosci == 10)
            {
                turaRozpoczenciaSuperUmiejsetnosci = 0;
            }
            else
            {
                swiatGry.graczArray.append("Super umiejetnosc nieaktywna Zostalo " + (10 - (swiatGry.getTura() - turaRozpoczenciaSuperUmiejsetnosci)) + " Tur\n");
            }
        }
        if (turaRozpoczenciaSuperUmiejsetnosci == 0)
        {
            swiatGry.graczArray.append("Super umiejetnosc nacisnij x\n");
        }
    }

    public void setTuraRozpoczenciaSuperUmiejsetnosci(int turaRozpoczenciaSuperUmiejsetnosci) {
        this.turaRozpoczenciaSuperUmiejsetnosci = turaRozpoczenciaSuperUmiejsetnosci;
    }

    @Override
    protected void stworzZwierze(int x, int y) {
        swiatGry.dodajOrganizm(new Czlowiek(x,y,this.swiatGry));
    }

    @Override
    public void akcja() {
        Boolean czyUdaloSieWykonacRuch  = false;
        switch (kierunek)
        {
            case LEWO:

                czyUdaloSieWykonacRuch=przesun(-1, 0);
                break;
            case PRAWO:
                czyUdaloSieWykonacRuch=przesun(1, 0);
                break;
            case GORA:
                czyUdaloSieWykonacRuch=przesun(0, -1);
                break;
            case DOL:
                czyUdaloSieWykonacRuch=przesun(0, 1);
                break;
            case KeyEvent.VK_X:
                czyUdaloSieWykonacRuch = true;
                 swiatGry.commentArray.append(this + " uzyl w tej turze tarczy Azuryna i stoi w miejscu \n      ");
                break;
            default:
                swiatGry.commentArray.append("cos poszlo nie tak przy wyborze kierunku\n        ");
                break;
        }
        if (!czyUdaloSieWykonacRuch)
        {
            swiatGry.commentArray.append(this + "Zmarnowal ture na wejscie w sciane, troche glupio xd\n       ");
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
        return "Czlowiek o id: "+id+" ";
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public Boolean czyOdbilAtak(Organizm atakujacy) {
        if (turaRozpoczenciaSuperUmiejsetnosci!=0 && swiatGry.getTura()-turaRozpoczenciaSuperUmiejsetnosci<=5)
        {
            swiatGry.commentArray.append(this + "Odbil Atak " + atakujacy + " Dzieki Tarczy Azuryna \n     ");
            if (swiatGry.mapa[atakujacy.getX()][atakujacy.getY()] == atakujacy)
            {
                swiatGry.mapa[atakujacy.getX()][atakujacy.getY()] = null;
            }
            atakujacy.setX(this.x);
            atakujacy.setY(this.y);
            atakujacy.akcja();
            if (atakujacy.getX() == this.x && atakujacy.getY() == this.y)
            {
                swiatGry.usunOrganizm(atakujacy);
            }
            return true;
        }
        return false;
    }
    @Override
    public void zapiszDoPiliku(FileWriter plikZapisu) {
        try {
            plikZapisu.write(this+""+x+" "+y+" "+sila+" "+turaRozpoczenciaSuperUmiejsetnosci+"\n");
        } catch (IOException e) {
            System.err.println(this+" nie udalo sie zapisac");
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        uaktualnijOpis();
        swiatGry.graczArray.append("Kierunek: ");
        if(e.getKeyCode()== KeyEvent.VK_UP)
        {
            kierunek = GORA;
            swiatGry.graczArray.append("Wybrano Gore");
            swiatGry.wykonajTure();
            return;
        }
        if (e.getKeyCode()== KeyEvent.VK_DOWN)
        {
            kierunek = DOL;
            swiatGry.graczArray.append("Wybrano Dol");
            swiatGry.wykonajTure();
            return;
        }
        if (e.getKeyCode()== KeyEvent.VK_RIGHT)
        {
            kierunek = PRAWO;
            swiatGry.graczArray.append("Wybrano Prawo");
            swiatGry.wykonajTure();
            return;
        }
        if (e.getKeyCode()== KeyEvent.VK_LEFT)
        {
            kierunek = LEWO;
            swiatGry.graczArray.append("Wybrano Lewo");
            swiatGry.wykonajTure();
            return;
        }
        if(e.getKeyCode() == KeyEvent.VK_X)
        {
            if (turaRozpoczenciaSuperUmiejsetnosci == 0)
            {
                kierunek=KeyEvent.VK_X;
                swiatGry.graczArray.append("Wybrano Super umiejetnosc Tarcza Azuryna");
                turaRozpoczenciaSuperUmiejsetnosci = swiatGry.getTura();
            }
            else
            {
                kierunek= GORA;
                swiatGry.graczArray.append("Nie mozna uzyc umiejetnosci");
            }
            return;
        }
        swiatGry.graczArray.append("Niczego nie wybrano");
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
