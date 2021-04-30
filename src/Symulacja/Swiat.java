package Symulacja;

import Symulacja.zwierzeta.*;
import Symulacja.Rosliny.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Swiat implements ActionListener{

    private   int wysokoscSwiata,szerokoscSwiata;
    public   LinkedList<Organizm> organizmyWedlogInicjatywy;
    private LinkedList<Organizm> organizmyDoUsuniecia;
    private LinkedList<Organizm> organizmyDoDodania;
    private final static Comparator<Organizm> organizmComparator = new Comparator<Organizm>() {
        @Override
        public int compare(Organizm o1, Organizm o2) {
            if(o1.getInicjatywa()<o2.getInicjatywa())
            {
                return 1;
            }
            else
            {
                if(o1.getInicjatywa()>o2.getInicjatywa())
                {
                    return -1;
                }
                else
                {
                    if(o1.getId() < o2.getId())
                    {
                        return -1;
                    }
                    else
                    {
                        return 1;
                    }
                }
            }
        }
    };
    private int lastId;
    private int LICZBA_ORGANIZMOW = (wysokoscSwiata*szerokoscSwiata / 50) + 1;
    private int LICZBA_ORGANIZMOW_NA_LINI=(((LICZBA_ORGANIZMOW)*11/(wysokoscSwiata-1))+1);
    private int licznik;
    private int dodatkowePrzesuniecie;

    public int tura;

    protected Czlowiek gracz;

    public MainFrame mainFrame;
    public PlanszaPanel planszaPanel;
    public TextArea graczArray;
    public TextArea commentArray;
    public Organizm[][] mapa;

    public static final int GORA = 0;
    public static final int DOL = 1;
    public static final int PRAWO = 2;
    public static final int LEWO = 3;
    public static final int NIE_OKRESLONY = -1;
    public static final int ROZMIAR_POLA = 40;
    public static final int SZEROKOSC_CHARA = 7;
    public static final Color KOLOR_ID = new Color(234, 5, 5);

    private int przesunieciePoX()
    {
        Random random = new Random();
        int przesunieciePoX =dodatkowePrzesuniecie*(szerokoscSwiata/LICZBA_ORGANIZMOW_NA_LINI) + random.nextInt(szerokoscSwiata/LICZBA_ORGANIZMOW_NA_LINI);
        return przesunieciePoX;
    }
    private int wybierzLinie(int[] tablicaLini)
    {
        Random random = new Random();
        int index = random.nextInt(licznik);
        int linia;
        int tmp=tablicaLini[index];
        tablicaLini[index] =tablicaLini[licznik-1];
        tablicaLini[licznik-1] = tmp;
        linia = tablicaLini[licznik - 1];
        licznik--;
        if (licznik == 0)
        {
            for (int i = 0; i < wysokoscSwiata-1; i++)
            {
                tablicaLini[i] = i + 1;
            }
            dodatkowePrzesuniecie++;
            licznik = wysokoscSwiata-1;
        }
        return linia;
    }
    private Organizm wybierzOrganizm(String rodzaj)
    {
        switch (rodzaj)
        {
            case "BarszczSosnowskiego":
                return new BarszczSosnowskiego(0,0,this);
            case "Guarana":
                return new Guarana(0,0,this);
            case "Mlecz":
                return new Mlecz(0,0,this);
            case "Trawa":
                return new Trawa(0,0,this);
            case "WilczaJagoda":
                return new WJagoda(0,0,this);
            case "Antylopa":
                return new Antylopa(0,0,this);
            case "CyberOwca":
                return new CyberOwca(0,0,this);
            case "Czlowiek":
                return new Czlowiek(0,0,this);
            case "Lis":
                return new Lis(0,0,this);
            case "Owca":
                return new Owca(0,0,this);
            case "Wilk":
                return new Wilk(0,0,this);
            case "Zolw":
                return new Zolw(0,0,this);
            default:
                return null;
        }
    }
    public void wykonajTure()
    {
        tura++;
        mainFrame.setTitle("176653 Jeremi Ledwoń PO2020 " + "tura: " + tura);
        commentArray.setText("");
        for (Organizm majacyTure : organizmyWedlogInicjatywy) {
            if (!majacyTure.czyUsuwany) {
                majacyTure.czyMojaTura = true;
                majacyTure.akcja();
                majacyTure.czyMojaTura = false;
                commentArray.append("\n");
            }
        }

        organizmyWedlogInicjatywy.addAll(organizmyDoDodania);
        for (Organizm usuwany : organizmyDoUsuniecia) {
            organizmyWedlogInicjatywy.remove(usuwany);
        }


        organizmyWedlogInicjatywy.sort(organizmComparator);
        organizmyDoDodania.clear();
        organizmyDoUsuniecia.clear();
        planszaPanel.repaint();
        mainFrame.requestFocusInWindow();
        if (gracz != null) {
            gracz.uaktualnijOpis();
        }
    }
    private void stworzGUI()
    {
        this.planszaPanel = new PlanszaPanel();
        this.graczArray = new TextArea("");
        this.commentArray = new TextArea("");
        graczArray.setSize(100,10);
        graczArray.setEditable(false);
        graczArray.setFocusable(false);

        commentArray.setEditable(false);

    }
    private void stworzOrganizmy(){
        licznik = wysokoscSwiata-1;
        LICZBA_ORGANIZMOW = (wysokoscSwiata*szerokoscSwiata / 50) + 1;
        LICZBA_ORGANIZMOW_NA_LINI=(((LICZBA_ORGANIZMOW)*11/(wysokoscSwiata-1))+1);
        int tablicaLini[] = new int[wysokoscSwiata];
        for (int i = 0; i < wysokoscSwiata-1; i++)
        {
            tablicaLini[i] = i + 1;
        }
        gracz = new Czlowiek(0,0,this);
        dodajOrganizm(gracz);
        dodajOrganizm(new CyberOwca(szerokoscSwiata-1, 0 , this));
        dodajOrganizm(new CyberOwca(szerokoscSwiata/2, 0 , this));
        for (int i = 0; i < LICZBA_ORGANIZMOW; i++)
        {
            dodajOrganizm(new Owca(przesunieciePoX(), wybierzLinie(tablicaLini), this));
            dodajOrganizm(new Lis(przesunieciePoX(), wybierzLinie(tablicaLini ) , this));
            dodajOrganizm(new Wilk(przesunieciePoX(), wybierzLinie(tablicaLini ) , this));
            dodajOrganizm(new Antylopa(przesunieciePoX(), wybierzLinie(tablicaLini) , this));
            dodajOrganizm(new Zolw(przesunieciePoX(), wybierzLinie(tablicaLini) , this));
            dodajOrganizm(new Trawa(przesunieciePoX(), wybierzLinie(tablicaLini) , this));
            dodajOrganizm(new Mlecz(przesunieciePoX(), wybierzLinie(tablicaLini) , this));
            dodajOrganizm(new WJagoda(przesunieciePoX(), wybierzLinie(tablicaLini) , this));
            dodajOrganizm(new Guarana(przesunieciePoX(), wybierzLinie(tablicaLini), this));
            dodajOrganizm(new BarszczSosnowskiego(przesunieciePoX(), wybierzLinie(tablicaLini) , this));
        }
        organizmyWedlogInicjatywy.addAll(organizmyDoDodania);
        organizmyWedlogInicjatywy.sort(organizmComparator);
        organizmyDoDodania.clear();

    }
    private void wczytajOrganizmy(Scanner plikZapisu)
    {
        Organizm tmpOrganizm;
        String rodzaj;
        int finalId;
        while(plikZapisu.hasNext())
        {
            rodzaj=plikZapisu.next();
            tmpOrganizm = wybierzOrganizm(rodzaj);
            plikZapisu.next();
            plikZapisu.next();
            finalId = plikZapisu.nextInt();
            tmpOrganizm.setX(plikZapisu.nextInt());
            tmpOrganizm.setY(plikZapisu.nextInt());
            if(tmpOrganizm instanceof Zwierze)
            {
                tmpOrganizm.setSila(plikZapisu.nextInt());
                if(tmpOrganizm instanceof Czlowiek)
                {
                    gracz = (Czlowiek) tmpOrganizm;
                    gracz.setTuraRozpoczenciaSuperUmiejsetnosci(plikZapisu.nextInt());
                }
            }
            dodajOrganizm(tmpOrganizm);
            tmpOrganizm.setId(finalId);
        }
        organizmyWedlogInicjatywy.addAll(organizmyDoDodania);
        organizmyWedlogInicjatywy.sort(organizmComparator);
        organizmyDoDodania.clear();
    }
    public Swiat(int szerokosc,int wysokosc,MainFrame main)
    {
        tura=0;
        this.mainFrame=main;
        this.szerokoscSwiata = szerokosc;
        this.wysokoscSwiata = wysokosc;
        stworzGUI();

        organizmyWedlogInicjatywy = new LinkedList<Organizm>();
        organizmyDoUsuniecia = new LinkedList<Organizm>();
        organizmyDoDodania = new LinkedList<Organizm>();
        mapa = new Organizm[szerokoscSwiata][wysokoscSwiata];
        stworzOrganizmy();
    }
    public Swiat(Scanner plikZapisu,MainFrame mainFrame)
    {
        this.mainFrame = mainFrame;
        this.wysokoscSwiata = plikZapisu.nextInt();
        this.szerokoscSwiata = plikZapisu.nextInt();
        int finalId =  plikZapisu.nextInt();
        this.tura =  plikZapisu.nextInt();

        organizmyWedlogInicjatywy = new LinkedList<Organizm>();
        organizmyDoUsuniecia = new LinkedList<Organizm>();
        organizmyDoDodania = new LinkedList<Organizm>();
        mapa = new Organizm[szerokoscSwiata][wysokoscSwiata];

        wczytajOrganizmy(plikZapisu);
        stworzGUI();
        lastId = finalId;
    }
    public void dodajOrganizm(Organizm newOrganizm)
    {
        if(mapa[newOrganizm.x][newOrganizm.y]== null){
            mapa[newOrganizm.x][newOrganizm.y] = newOrganizm;
            newOrganizm.setId(lastId);
            organizmyDoDodania.add(newOrganizm);
            lastId++;
        }

    }
    public void usunOrganizm(Organizm usuwany)
    {
        if(mapa[usuwany.getX()][usuwany.getY()]==usuwany)
        {
            mapa[usuwany.getX()][usuwany.getY()] = null;
        }
        if(usuwany == gracz)
        {

            mainFrame.removeKeyListener(gracz);
            graczArray.setText(usuwany+" Nie zyje ;c");
            gracz = null;
        }
        usuwany.czyUsuwany = true;
        organizmyDoUsuniecia.add(usuwany);

    }
    public void zapiszStan()
    {
        try {
            FileWriter plikZapisu = new FileWriter("zapis.txt");
            plikZapisu.write(String.valueOf(wysokoscSwiata)+"\n");
            plikZapisu.write(String.valueOf(szerokoscSwiata)+"\n");
            plikZapisu.write(String.valueOf(lastId)+"\n");
            plikZapisu.write(String.valueOf(tura)+"\n");
            for (Organizm organizm : organizmyWedlogInicjatywy)
            {
                organizm.zapiszDoPiliku(plikZapisu);
            }
            plikZapisu.close();
            graczArray.append("\nGra Zapisane z sukcesem\n");
            mainFrame.requestFocusInWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int getSzerokosc() {
        return szerokoscSwiata;
    }

    public int getWysokoscSwiata() {
        return wysokoscSwiata;
    }

    public Czlowiek getGracz() {
        return gracz;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                    wykonajTure();
            }
        });

    }

    public int getTura() {
        return tura;
    }


    public class PlanszaPanel extends JPanel {

        public PlanszaPanel() {
            setPreferredSize(new Dimension(szerokoscSwiata*40+10, wysokoscSwiata *40+10));
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            Rectangle2D rectangle = new InteraktywnePole();

            for(int i=0;i<szerokoscSwiata;i++)
            {

                for(int j = 0; j< wysokoscSwiata; j++)
                {
                    rectangle.setRect(i*ROZMIAR_POLA,j*ROZMIAR_POLA , ROZMIAR_POLA, ROZMIAR_POLA);
                    if(mapa[i][j] != null)
                    {
                        mapa[i][j].rysowanie(g2d);
                    }
                    g2d.setPaint(Color.BLACK);
                    g2d.drawString("("+String.valueOf(i) + "," + String.valueOf(j)+")", i * ROZMIAR_POLA, j * ROZMIAR_POLA + 10);
                    g2d.draw(rectangle);

                }
            }
        }
    }
}
