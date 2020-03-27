import java.io.IOException;
import java.util.Hashtable;

public class Main {
    public static void main(String[] argv) throws IOException{
        long debut = System.currentTimeMillis();
        Hashtable<Character,String> dico= new Hashtable<Character, String>();
        System.out.println("*************Début de la compression**************");
        Huffman huffman = new Huffman("alice.txt");
        System.out.println("Liste des fréquences des différents lettres de l'alphabet de Huffman :");
        System.out.println(huffman.lire());
        huffman.creationFichierCompresse();
        //huffman.recuperationFrequence();
        long fin = System.currentTimeMillis();

        System.out.println("Temps d'execution : " +String.valueOf((float)(fin-debut)/1000)+" secondes");

    }
}
