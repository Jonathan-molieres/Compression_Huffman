import java.io.IOException;
import java.util.Hashtable;

public class Main {
    public static void main(String[] argv) throws IOException{
        Hashtable<Character,String> dico= new Hashtable<Character, String>();
        Huffman huffman = new Huffman("textesimple.txt");
        System.out.println("Liste des fréquences des différents lettres de l'alphabet de Huffman :");
        System.out.println(huffman.lire());
        huffman.creactionFichierCompresse();

        //huffman.recuperationFrequence();
    }
}
