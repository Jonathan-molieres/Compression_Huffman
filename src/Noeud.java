import java.util.ArrayList;
import java.util.Hashtable;

public class Noeud implements Comparable<Object> {
    /**Explication variable
     * t : tuple contenant la lettre
     * droite : Noeud droite de l'arbre
     * gauche : Noeud gauche de l'arbre
     * frequence : nombre d'occurence de la lettre ou la somme des occurences
     */
    private Tuple t;
    private Noeud droite;
    private Noeud gauche;
    private Integer frequence;
    //construteur
    public Noeud(Tuple t, Noeud gauche, Noeud droite, Integer frequence) {
        this.t = t;
        this.droite = droite;
        this.gauche = gauche;
        this.frequence = frequence;
    }

    public Integer getFrequence() {
        return frequence;
    }

    public Noeud getDroite() {
        return droite;
    }

    public Noeud getGauche() {
        return gauche;
    }
    /* affichage  recursive pour visualiser l'arbre*/
    public String toString() {
        if(t!=null && t.getLettre()!=System.getProperty("line.separator").charAt(0))
        {
            return "(f= "+String.valueOf(this.frequence)+" L= "+this.t.getLettre()+")";
        }
        else if(t!=null && t.getLettre()==System.getProperty("line.separator").charAt(0)){
            return "(f= "+String.valueOf(this.frequence)+" L=[saut_ligne])";
        }
        else {
            return "(f= " + String.valueOf(this.frequence) + " g= " + this.getGauche() + " d= " + this.getDroite()+"";
        }
    }

    public Tuple getT() {
        return t;
    }

    public Boolean isLeaf() {
        return droite == null & gauche == null;
    }

    /**
     * @param codage: contient le code d'une lettre
     * @param dico : dictionnaire des codages des lettres
     * @return
     */
    public Hashtable<Character, String> parcoursProfondeur(String codage, Hashtable <Character, String> dico) {
        if (!this.isLeaf()) {
            String octet = "0";
            codage = codage + octet;
            this.gauche.parcoursProfondeur(codage, dico);
            octet = "1";
            codage = codage.substring(0, codage.length() - 1);
            codage = codage + octet;
            this.droite.parcoursProfondeur(codage, dico);

        } else {
            dico.put(this.t.getLettre(), codage);
            return dico;
        }
        return dico;
    }

    /** Methode de comparaison pour les Noeuds
     * @param o : Objet
     * @return : int
     */
    public int compareTo(Object o) {
        if (o instanceof Noeud) {
            Noeud n = (Noeud) o;
            if (this.frequence < n.getFrequence()) {
                return -1;
            } else if (this.frequence > n.getFrequence()) {
                return 1;
            } else if (this.frequence.equals(n.getFrequence())) {
                {
                    if(this.t!=null && n.getT()!=null){
                        if ((int) this.t.getLettre() < (int) n.getT().getLettre()){// difference entre String et un caracetere
                            return -1; }
                        else if ((int) this.t.getLettre() > (int) n.getT().getLettre()){
                            return 1;}
                        else{
                            return 0;}

                    }else{
                        return 1;}

                }
            }
        }
        return 0;
    }
}