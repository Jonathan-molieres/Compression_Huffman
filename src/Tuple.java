/** Class Tuple  */
public class Tuple implements Comparable<Object> {

    private char lettre;
    private Integer frequence;

    /** Constructeur
     * @param lettre : Lettre de l'alphabet de Huffman
     * @param frequence : nombre d'occurence de la lettre
     */
    public Tuple(char lettre, Integer frequence){
        this.lettre=lettre;
        this.frequence = frequence;
    }

    public Integer getFrequence(){return this.frequence;}

    public char getLettre() {return this.lettre;}

    public void setFrequence(Integer enter) {
        this.frequence = enter;
    }

    public int compareTo(Object o) {
        if (o instanceof Tuple){
            Tuple t = (Tuple)o;
            if (this.frequence < t.getFrequence())
                {return -1;}
            else if (this.frequence > t.getFrequence())
                {return 1; }
            else if (this.frequence.equals(t.getFrequence())){

                if ( (int) this.lettre < (int) t.getLettre() ) /* difference entre String et un caracetere */
                    {return -1; }
                else if ((int) this.lettre > (int) t.getLettre())
                    {return 1;}
                else {return 0;}
            }
        }
        return 0;

    }

    /**
     * @return String pour l'affichage
     */
    public String toString(){
        if (this.lettre!=System.getProperty("line.separator").charAt(0)) {
            return (this.lettre + " = " + this.frequence);
        }
        else{
            return("[saut_ligne] = " + this.frequence);
        }
    }

}

