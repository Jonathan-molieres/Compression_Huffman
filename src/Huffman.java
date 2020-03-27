import java.io.*;
import java.util.Collections;
import java.util.Hashtable;
import java.util.TreeSet;
import java.util.ArrayList;
public class Huffman {

    /**
     * fichier: nom du fichier à traiter
     * tree : arbre trier des tuples de l'arbre de Huffman qui doivent être unique
     * octetHuffman : nombre d'octets du fichier binaire final
     * racine : Noeud racine de l'arbre de Huffman
     */
    private String fichier;
    private TreeSet<Tuple> tree;
    private TreeSet<Tuple> treeDecompression;
    private Integer bitsHuffman;
    private Noeud racine;

    public Huffman(String fichier) {
        this.fichier = fichier;
        tree = new TreeSet<Tuple>();
        bitsHuffman =0;
    }

    /**
     * @param lettre : Lettre a verifier
     * @return Boolean: true si la lettre est deja dans l'arbre, false sinon
     */
    private Boolean containsKey(char lettre) {
        for (Tuple t : this.tree) {
            if (t.getLettre() == lettre) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param lettre: Lettre qui faut incrementer le compteur de frequence
     * @throws IOException
     */
    private void increment(char lettre) throws IOException {
        Tuple tt = null;
        for (Tuple t : this.tree) {
            if (t.getLettre()==lettre) {
                tt = t;
            }
        }
        if (tt != null) {
            this.tree.remove(tt);
            tt.setFrequence(tt.getFrequence() + 1);
            this.tree.add(tt);
        }
    }

    /**
     * @return: tree
     * @throws IOException: gestion d'erreur de ouverture d'un fichier
     */
    public TreeSet<Tuple> lire() throws IOException {
        BufferedReader lecture = null;
        String ligne;
        //Lecture des fichiers avec gestion d'erreur
        try {
            lecture = new BufferedReader(new FileReader("data/"+this.fichier));
        } catch (FileNotFoundException exc) {
            System.out.println("Erreur d'ouverture du fichier : ".concat(this.fichier));
        }
        if (lecture == null) {
            throw new AssertionError();
        }
        while ((ligne = lecture.readLine()) != null) {

            for (int i = 0; i < ligne.length(); i++) {
                char letter = ligne.charAt(i);

                if (this.containsKey(letter)) {
                    this.increment(letter);
                }
                else {
                    tree.add(new Tuple(letter, 1));
                    }
                }

            Character sautLine =System.getProperty("line.separator").charAt(0);
            if (this.containsKey(sautLine)) {
                this.increment(sautLine);
            }
            else {
                tree.add(new Tuple(sautLine, 1));
            }
        }
        lecture.close();
        return this.tree;
    }

    /**Fonction qui creer les feuilles à partir des Tuples (tree)
     * @return TreeSet:
     */
    private TreeSet<Noeud> creationFeuille() {
        TreeSet<Noeud> noeuds = new TreeSet<Noeud>();
        for (Tuple t : this.tree) {
            noeuds.add(new Noeud(t, null, null, t.getFrequence()));
        }
        return noeuds;
    }

    /**Creaction de l'arbre avec des Listes Triées car on peut avoir des doublons.
     * @return
     */
    private ArrayList<Noeud> creationArbre() {
        TreeSet<Noeud> arbres = this.creationFeuille();
        ArrayList<Noeud> arbresList = new ArrayList<Noeud>(arbres);

        while (arbresList.size() != 1) {
            arbresList.add(new Noeud(null, arbresList.get(0), arbresList.get(1), arbresList.get(0).getFrequence() + arbresList.get(1).getFrequence()));
            arbresList.remove(arbresList.get(0));
            arbresList.remove(arbresList.get(0));
            Collections.sort(arbresList);
        }
        System.out.println("Arbres de Huffman construit :");
        System.out.println(arbresList);
        return  arbresList;
    }

    /**Creaction du fichier bin avec l'arbre de Huffman
     * @throws IOException: gestion d'erreur de ouverture/ ecriture des fichiers
     */
    public void creationFichierCompresse() throws IOException {

        this.creationFichierTxt();
        Hashtable<Character,String> dicoCodage= new Hashtable<Character, String>();
        this.racine= this.creationArbre().get(0);
        dicoCodage= racine.parcoursProfondeur("",dicoCodage);
        BufferedOutputStream  ecriture = null;
        BufferedReader lecture = null;
        String ligne;
        try {
            File inputFile = new File("data/compresser/"+this.fichier.substring(0,this.fichier.length()-4)+"_comb.bin");
            FileOutputStream  fileEcriture = new  FileOutputStream(inputFile);
            BufferedOutputStream bufferedEcriture = new BufferedOutputStream(fileEcriture);
            ecriture= new BufferedOutputStream(bufferedEcriture);
            lecture = new BufferedReader(new FileReader("data/"+this.fichier));
        } catch (FileNotFoundException exc) {
            System.out.println("Erreur d'écriture du fichier : ".concat(this.fichier));
        }
        assert lecture != null;
        String codageFin = "";
        String codageDico="";
        String codage="";
        Character letter ;
        while ((ligne = lecture.readLine()) != null) {

            for (int i = 0; i < ligne.length(); i++) {
                letter = ligne.charAt(i);
                codageDico=dicoCodage.get(letter);
                codage += codageDico;
                this.bitsHuffman += codageDico.length();// compteur pour calculer le nombre moyen de bit
            }
        }
        for(int index = 0; index < codage.length();index+=8){
            try{
                ecriture.write((byte) Integer.parseInt(codage.substring(index,index+8)));
            }
            catch (Exception e){
                for(int i=0; i<codage.length()-index;i++){
                    codageFin+="0";
                }
                ecriture.write((byte) Integer.parseInt(codage.substring(index,codage.length())+codageFin));
            }
        }
        /* Fermeture des fichiers */
        ecriture.close();
        lecture.close();
        System.out.println("*******************Compression Terminée*******************");
        //calcule des performances
        this.nombreMoyen();
        this.tauxCompression();
    }

    /** Creaction du fichier txt contenant les occurences et l'alphabet de fichier original
     * @throws IOException: gestion d'erreur d'ecriture du fichier
     */
    private void creationFichierTxt() throws IOException{

        BufferedWriter buffer = null;
        try {
            File inputFile = new File("data/compresser/"+this.fichier.substring(0,this.fichier.length()-4)+"_freq.txt");
            buffer = new BufferedWriter(new FileWriter(inputFile));
            buffer.write(String.valueOf(this.tree.size())+"\n");

            for(Tuple t : tree){
                if(t.getLettre()==System.getProperty("line.separator").charAt(0)){
                    buffer.write("[saut_ligne] "+String.valueOf(t.getFrequence())+"\n");
                }
                else{
                    buffer.write(t.getLettre()+" "+String.valueOf(t.getFrequence())+"\n");
                }

            }
            buffer.close();

        } catch (FileNotFoundException exc) {
            System.out.println("Erreur d'écriture du fichier : ".concat(this.fichier));
        }
    }

    /**Mèthode qui calcul
     * @return float moyenne de bit
     */
    private float nombreMoyen(){
        float moyenne =(float) bitsHuffman / racine.getFrequence();
        System.out.println("Le nombre moyen de bits est de : "+String.valueOf(moyenne));
        return moyenne;
    }

    /**  Méthode qui calcul le taux de compression
     * @return: float qui est ce taux et Affichage
     */
    private float tauxCompression(){

        File file= new File("data/compresser/"+this.fichier.substring(0,this.fichier.length()-4)+"_comb.bin");
        File file2 = new File("data/"+this.fichier);
        float taux= (float) (1.0 - ((float)file.length()/(float) file2.length()))*100;
        System.out.println("Le taux de compression est de : "+String.valueOf(taux)+" %");
        return taux;
    }
    
    
}