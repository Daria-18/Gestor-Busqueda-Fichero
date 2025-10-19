package psp.procesos.a13;

import javax.swing.*;
import java.io.*;

/**
 * Clase que busca todas las apariciones de una string en el
 * fichero especificado mediante un iterador (ficheros generados por la clase {@link GeneradorFichsTexto})
 */

public class BuscadorTexto{


    /**
     * Método main con la lógica que hará la busqueda de la string en los ficheros.
     * <br/>
     * En caso de los argumentos no ser los especificados, se da fin al programa con un print por defecto.
     * @param args -> Únicamente soporta 2 argumentos (índice, string) en el orden determinado.
     */
    public static void main(String[] args) {
        if(args.length != 2){
            System.out.println("Debes introducir dos parámetros unicamente");
            return; // da fin al método main
        }

        // Variables que capturan los argumentos
        int indiceFichero = Integer.parseInt(args[0]);
        String textoFichero = args[1];

        int total = buscarTexto(indiceFichero,textoFichero);

         //Imprimimos el contador para ser capturado
        System.out.println(total);
    }

    /**
     * Método que emplea un BufferedReader y devuelve un contador en base a las apariciones de cierta cadena en el fichero.
     * @param indiceFichero índice
     * @param textoFichero cadena
     * @return Contador como {@code int}
     */

    public static int buscarTexto(int indiceFichero, String textoFichero){
        //Variables necesarias para buscar las palabras en los ficheros
        String fichero = "texto"+indiceFichero+".txt"; // crea una string con el nombre del fichero y la pasa a un File
        File archivo = new File(fichero);
        int contador = 0; // Devolvera un entero por pantalla que será capturado en el método del Gestor
        //System.out.println(fichero);

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))){
            String lineaArchivo;

            while ((lineaArchivo = reader.readLine()) != null){
                if (lineaArchivo.equalsIgnoreCase(textoFichero)) contador++;
            }

        } catch (IOException e) {
            System.out.println("Error de lectura en el fichero: " + fichero);
            e.printStackTrace();
        }
        return contador;
    }

}