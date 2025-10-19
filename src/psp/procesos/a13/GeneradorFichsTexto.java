package psp.procesos.a13;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Clase que genera N número de ficheros (5 en este caso) de X número de líneas (100000000 en este caso),
 * en el que se introducen de manera pseudoaleatoria las palabras casa, perro y coche
 * para después ser buscados por nuestro programa gestor de busquedas.
 * <br/>
 * <br/>
 * Genera ficheros con el nombre: "texto(índice).txt"
 */

public class GeneradorFichsTexto {
    private static final String FILE_NAME= "texto";
    private static final String FILE_EXT= ".txt";

    private static final String TEXTO1= "CASA";
    private static final String TEXTO2= "PERRO";
    private static final String TEXTO3= "COCHE";

    public static void main(String[] args) {

        final int NUM_LÍNEAS = 100000000;

        for(int i=1; i<=5; i++) {
            try (
                    PrintWriter salida = new PrintWriter(new FileWriter(FILE_NAME + i + FILE_EXT))
            )
            {
                Random random = new Random();
                char[] word;

                for (int num=1; num<= NUM_LÍNEAS; num++) {

                    if ((num*(i*i)) % 243 == 0) {
                        word = TEXTO1.toCharArray();
                    } else if ((num*(i*i)) % 43555 == 0) {
                        word = TEXTO2.toCharArray();
                    } else if ((num*(i*i)) % 123052 == 0) {
                        word = TEXTO3.toCharArray();
                    } else {
                        word = new char[random.nextInt(8) + 3]; // words of length 3 through 10. (1 and 2 letter words are boring.)
                        for (int j = 0; j < word.length; j++) {
                            word[j] = (char) ('a' + random.nextInt(26));
                        }
                    }
                    salida.println(word);
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
            System.out.println("Fichero " + FILE_NAME + i + FILE_EXT + " generado");
        }

    } // main
}
