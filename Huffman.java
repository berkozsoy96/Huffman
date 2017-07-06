package huffman;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Huffman {

    static List<dugum> dgm = new ArrayList<>();
    static List<dugum> temsiller = new ArrayList<>();
    
    public static void kToBSirala(){
        Collections.sort(dgm, new Comparator<dugum>() {
            @Override
            public int compare(dugum o1, dugum o2) {
                final double krt1 = o1.toplamSayi;
                final double krt2 = o2.toplamSayi;
                return krt1 > krt2? 1
                        : krt1 < krt2? -1 : 0;
            }
        });
    }
    
    public static void hesapla(String cumle){
        int i , j;
        boolean bayrak;

        //her karakterin frekansını bulup LIST te saklıyor.
        for(i=0 ; i<cumle.length() ; i++){
            bayrak=false;

            for(j=0 ; j<dgm.size() ; j++){
                if(dgm.get(j).sembol.equals(""+cumle.charAt(i))){
                    dgm.get(j).toplamSayi++;
                    bayrak=true;
                    break;
                }
            }

            if(bayrak==false){
                dugum temp = new dugum();
                temp.sembol = ""+cumle.charAt(i);
                temp.toplamSayi = 1;
                dgm.add(temp);
            }
        }
        
        // LIST deki karakterleri kucukten buyuge sıralar
        // nasi yapar hicbir fikrim yok
        kToBSirala();
        
        /*for(i=0 ; i<dgm.size(); i++){
            System.out.println(dgm.get(i).sembol + " - " + dgm.get(i).toplamSayi);
        }
        System.out.println();*/
    }
    
    public static dugum kokOlustur(){
        
        int size = dgm.size();
        
        for(int i = 0 ; i < size-1 ; i++){
            
            dugum ekle = new dugum();
            
            ekle.toplamSayi = dgm.get(0).toplamSayi + dgm.get(1).toplamSayi;
            ekle.sembol = dgm.get(0).sembol + dgm.get(1).sembol;
            ekle.sol = dgm.get(0);
            ekle.sag = dgm.get(1);
            dgm.remove(0);
            dgm.remove(0);
            dgm.add(ekle);
            
            kToBSirala();
        }
        return dgm.get(0);
    }
    
    public static String sifrele(dugum kok, String cumle){
        String encrypted = new String();
        agacGez(kok, "");
        
        for(int i=0 ; i<cumle.length() ; i++){
            for(int j=0 ; j<temsiller.size() ; j++){
                if(temsiller.get(j).sembol.equals("" + cumle.charAt(i))){
                    encrypted = encrypted + temsiller.get(j).temsil;
                }
            }
        }
        
        return encrypted;
    }
    
    public static void agacGez(dugum durum, String tms){
        if(durum == null){
            return;
        }
        if(durum.sol == null && durum.sag == null){
            durum.temsil = tms;
            temsiller.add(durum);
        }
        agacGez(durum.sol, tms+"0");
        agacGez(durum.sag, tms+"1");

    }
    
    public static void dosyala(String sifreli) throws IOException{
        File dosya = new File("C:\\Users\\Betül Özsoy\\Desktop\\encrypted.txt");
        if (!dosya.exists()) {
            dosya.createNewFile();
        }
        
        FileWriter fileWriter = new FileWriter(dosya, false);
        try (BufferedWriter bWriter = new BufferedWriter(fileWriter)) {
            for(int i=0 ; i<temsiller.size() ; i++){
                bWriter.write(temsiller.get(i).sembol + "-" + temsiller.get(i).temsil);
                bWriter.newLine();
            }
            bWriter.newLine();
            bWriter.write(sifreli);
        }
    }
    
    public static void main(String[] args) throws IOException{
        
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Cumleyi giriniz : ");
        String girilenCumle = scan.nextLine();
        
        hesapla(girilenCumle);
        
        dugum kok = kokOlustur();
        
        String encrypted = sifrele(kok , girilenCumle);
        
        dosyala(encrypted);
        
        System.out.println("Mesajınız Başarıyla Şifrelenmiştir.");
        
    }
    
}
