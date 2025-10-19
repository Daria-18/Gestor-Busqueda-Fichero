package psp.procesos.a13;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que emplea swing para propocionar una GUI al usuario y poder interactuar con nuestro programa
 * gestor de busquedas.
 * <br/>
 * Se hace uso de una lista de procesos para poder borrarlos correctamente
 */

public class GestorBusqueda extends JFrame implements ActionListener {

    // Constantes para la comprobación en la llamada al metodo
    private static final String opc1= "CASA";
    private static final String opc2= "PERRO";
    private static final String opc3= "COCHE";

    // Creacion de objetos usados por Swing
    private JLabel titulo, opcionesLabel, ficherosLabel, opcionInputLabel, ficheroInputLabel;
    private JTextField opcionTextField, ficheroTextField;
    private JButton buscarButton, cancelarButton;

    // Lista de procesos para poder finalizarlos en los metodos de creacion y eliminación de procesos
    private final List<Process> listaProcesos = new ArrayList<Process>();

    /**
     * Constructor que inicializa la GUI de Swing con todos sus componentes
     */
    public GestorBusqueda() {
        super();
        this.configurarGUI();
        this.inicializarComponentes();
    }


    /**
     * Main que crea un nuevo Gestor
     * @param args -> Null
     */
    public static void main(String[] args) {
        GestorBusqueda gestor = new GestorBusqueda(); //creacion del objeto para crear la ventana
    }

    /**
     * Genera la ventana básica sobre la que se imprirán los componentes
     * cuando se realice la debida llamada
     */

    private void configurarGUI(){
        this.setTitle("Gestor de Busqueda");
        this.setSize(400,350); //400x350 pixeles
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //finaliza el programa al darle a la cruz
        this.setVisible(true);
    }

    /**
     * Método que inicializa todos los componentes a ser dibujados en el panel de
     * JFrame y reinicia este panel para la correcta visualización
     */
    private void inicializarComponentes() {
        // Label para el título
        titulo = new JLabel("<html><b><i>BUSCAR TEXTO EN FICHERO</i></b></html>");
        titulo.setBounds(100, 20, 300, 30);
        this.add(titulo);

        // Label para opciones de búsqueda
        opcionesLabel = new JLabel("Opciones de búsqueda: CASA PERRO COCHE");
        opcionesLabel.setBounds(50, 60, 300, 20);
        this.add(opcionesLabel);

        // Label para ficheros disponibles
        ficherosLabel = new JLabel("Ficheros a buscar: 1 al 5");
        ficherosLabel.setBounds(50, 80, 300, 20);
        this.add(ficherosLabel);

        // Label y campo de texto para input de los strings a buscar
        opcionInputLabel = new JLabel("Introduzca una opción:");
        opcionInputLabel.setBounds(50, 130, 150, 20);
        this.add(opcionInputLabel);

        opcionTextField = new JTextField();
        opcionTextField.setBounds(200, 130, 100, 25);
        this.add(opcionTextField);

        // Label y campo de texto para el input del fichero
        ficheroInputLabel = new JLabel("Introduzca un fichero:");
        ficheroInputLabel.setBounds(50, 170, 150, 20);
        this.add(ficheroInputLabel);

        ficheroTextField = new JTextField();
        ficheroTextField.setBounds(200, 170, 100, 25);
        this.add(ficheroTextField);

        // Cuadramos la posición de los botones con los labels en x
        // Botón de busqueda que manda una accion al listener de buscar
        buscarButton = new JButton("Buscar");
        buscarButton.setBounds(50, 220, 100, 30);
        buscarButton.addActionListener(this);
        buscarButton.setActionCommand("buscar");
        this.add(buscarButton);

        // Botón de cancelar manda una accion al listener de cancelar
        cancelarButton = new JButton("Cancelar");
        cancelarButton.setBounds(200, 220, 100, 30);
        cancelarButton.addActionListener(this);
        cancelarButton.setActionCommand("cancelar");
        this.add(cancelarButton);

        //Refrescamos el panel para que imprima correctamente
        JPanel panel = (JPanel) this.getContentPane(); //obtiene los contenidos del panel
        panel.revalidate(); //actualiza el layout
        panel.repaint(); //refresh del panel
    }


    /**
     * Método que va a capturar el click de los botones realizados por el usuario en la ventana
     * @param evento Evento capturado por los botones (Buscar o Cancelar)
     */
    @Override
    public void actionPerformed(ActionEvent evento) {

        String comando = evento.getActionCommand();

        // Si se le da al botón de búsqueda se llamará al método buscarTexto para llamar al proceso hijo
        if (comando.equalsIgnoreCase("buscar")){
            String opcionUser = opcionTextField.getText().trim(); // Recogemos el input de la opcion del textField
            int ficheroUser;

            // Parsear un valor no válido a int puede tirar un number format exception
            try {
                ficheroUser = Integer.parseInt(ficheroTextField.getText()); // Recogemos el input del fichero del textField como int
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,"Introduce un valor entero como indice del fichero");
                return;
            }

            if (!opcionUser.equalsIgnoreCase(opc1) && !opcionUser.equalsIgnoreCase(opc2) && !opcionUser.equalsIgnoreCase(opc3)){
                JOptionPane.showMessageDialog(this,"Introduciste una opción invalida, debe de ser: " + opc1 + ", " + opc2 + ", " + opc3);
                return; // return para que no ejecute el resto del codigo
            } else if (ficheroUser <= 0 || ficheroUser > 5) {
                JOptionPane.showMessageDialog(this, "El número de fichero debe estar entre 1 y 5.");
                return; // return para que no ejecute el resto del codigo
            } else {
                new Thread(() -> crearProceso(ficheroUser, opcionUser)).start(); //Debemos de usar Thread para evitar el bloqueo de la GUI
            }
        }
        if (comando.equalsIgnoreCase("cancelar")){
            matarProcesos();
        }

    }

    /**
     *  Método que se encarga de llamar al proceso hijo mediante la clase {@link BuscadorTexto}.
     *  <br/>
     *  Se hace uso de {@link ProcessBuilder} para las creaciones de los procesos; ProcessBuilder ejecuta comandos en el
     *  intérprete de comandos del propio sistema operativo.
     * @param idFichero índice del fichero que se usara en la llamada al proceso hijo (en este caso en un rango de entre 1 y 5)
     * @param opcionBuscar string introducida por el usuario que se pasara al proceso hijo (entre las constantes declaradas)
     */
    public void crearProceso(int idFichero, String opcionBuscar){
        ProcessBuilder pb;
        Process process;

        String classname ="psp.procesos.a13.BuscadorTexto"; //clase BuscadorTexto a llamar con el main
        String currentPath = System.getProperty("user.dir");
        String classpath = currentPath + "/out/production/Actividad1_3";

        //JOptionPane.showMessageDialog(this,"Introduciste la opcion: " + opcionBuscar + " para el fichero: " + idFichero);

        try {

            //Debemos pasar el id del fichero como string al processbuilder para que trate el parametro correctamente
            pb = new ProcessBuilder("java", "-cp", classpath, classname, String.valueOf(idFichero), opcionBuscar);

            process = pb.start();
            listaProcesos.add(process);// Se añade el proceso a la lista al quedar en ejecución

            int codigoSalida = process.waitFor(); //Return 0 si ejecucion correcta

            if (codigoSalida == 0){
                //Buffered Reader para capturar la salida del proceso hijo
                BufferedReader readerProceso = new BufferedReader(new InputStreamReader(process.getInputStream()));

                /*
                //Almacenamos en una string la salida para después comprobar si es nula o no
                String linea = readerProceso.readLine();
                int contador = 0;

                if (linea != null){
                    contador = Integer.parseInt(linea); //Lee la salida del proceso hijo
                }
                 */

                int contador = Integer.parseInt(readerProceso.readLine());

                String output = "Valores encontrados: " + contador +"\n Opción -> " + opcionBuscar
                        +"\n Fichero -> " + idFichero;

                JOptionPane.showMessageDialog(this,output);
            }

            listaProcesos.remove(process);// Cuando finalice este proceso, se borrará de la lista

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Accede a la lista y mata a todos los procesos vivos
     */
    private void matarProcesos() {
        // Placeholder
        JOptionPane.showMessageDialog(this,"Pulsaste cancelar, se cerrarán los procesos.");

        for (Process process : listaProcesos) if (process.isAlive()) process.destroy();

        listaProcesos.clear();
        JOptionPane.showMessageDialog(this,"Se eliminaron todos los procesos activos");
    }

}

