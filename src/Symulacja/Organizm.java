package Symulacja;

import java.awt.*;
import java.io.FileWriter;
import java.io.PrintStream;

public abstract class Organizm {

    protected Swiat swiatGry;
    protected int id;
    protected int sila;
    protected int inicjatywa;
    protected int x, y;
    public  Boolean czyMojaTura;
    public  Boolean czyUsuwany;
    public Boolean czyDopieroUrodzony;

    public Organizm(int x, int y, Swiat swiatGry){
        this.x=x;
        this.y=y;
        this.czyMojaTura=false;
        this.czyDopieroUrodzony=true;
        this.czyUsuwany=false;
        this.swiatGry=swiatGry;
    };

    public abstract void akcja();
    public abstract void kolizja(Organizm Przegrany);
    public abstract void rysowanie(Graphics2D out);
    public abstract Boolean czyOdbilAtak(Organizm atakujacy);
    public abstract void zapiszDoPiliku(FileWriter plikZapisu);

    int getId(){
      return  id;
    };
    public void setId(int newId)
    {
        this.id =newId;
    }
    public int getX()
    {
        return x;
    };
    public int getY(){
        return y;
    };
    public void setX(int x) {
        this.x=x;
    };
    public void setY(int y){
        this.y=y;
    };
    public int getSila(){
        return sila;
    };
    public void setSila(int sila){
        this.sila = sila;
    };
    public int getInicjatywa(){
        return inicjatywa;
    };
}
