import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class main {

	static Scanner scan = new Scanner(System.in);
	static File fichero = new File("src/ficheros/partidos.csv");
	static final int NUM_SB = 20;
	
	public static void main(String[] args) {
		menu();
	}
	
	private static void menu() {
		System.out.println("GESTIÓN DE PARTIDOS DE RUBGY");
		System.out.println("...................................");
		System.out.println("0. Salir");
		System.out.println("1. Mostrar todos los partidos");
		int opcion = scan.nextInt();
		switch(opcion) {
			case 1:
				mostrarTodo();
				break;
		}
	}
	
	/**
	 * 
	 */	
	private static void mostrarTodo() {
		// Mostrar primera fila y configurar el StringBuffer
		StringBuffer celda = null;
		celda = new StringBuffer(" FECHA");
		celda.setLength(NUM_SB);
		System.out.print(celda);
		celda = new StringBuffer(" LOCAL");
		celda.setLength(NUM_SB);
		System.out.print(celda);
		celda = new StringBuffer(" VISITANTE");
		celda.setLength(NUM_SB);
		System.out.println(celda);
		System.out.println("---------------------------------------------------------");
		// Leemos el fichero e imprimimos las líneas
		try {
			FileReader fReader = new FileReader(fichero);
			BufferedReader bufReader = new BufferedReader(fReader);
			String linea = null;
			while((linea = bufReader.readLine()) != null) {
				Partido nuevoPartido = dividirDatos(linea);
				celda = new StringBuffer(nuevoPartido.getFecha());
				celda.setLength(NUM_SB);
				System.out.print(celda);
				celda = new StringBuffer(nuevoPartido.getJugadorLocal().concat(" -> ".concat(String.valueOf(nuevoPartido.getPuntosLocal()))));
				celda.setLength(NUM_SB);
				System.out.print(celda);
				celda = new StringBuffer(nuevoPartido.getJugadorVisitante().concat(" -> ".concat(String.valueOf(nuevoPartido.getPuntosVisitante()))));
				celda.setLength(NUM_SB);
				System.out.println(celda);
			}
			fReader.close();
			bufReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método que pide una línea y nos almacena todos los datos en
	 * un objeto de tipo Partido donde podremos recoger y manejar los datos
	 * @return (Partido) nuevoPartido
	 */
	private static Partido dividirDatos(String linea) {
		Partido nuevoPartido = new Partido();
		String[] datos = linea.split(";");
		// Agregamos los datos al partido
		nuevoPartido.setFecha(datos[0]);
		nuevoPartido.setEspectadores(Integer.parseInt(datos[1]));
		nuevoPartido.setEstadio(datos[2]);
		nuevoPartido.setJugadorLocal(datos[3]);
		nuevoPartido.setJugadorVisitante(datos[4]);
		nuevoPartido.setPuntosLocal(datos[5]);
		nuevoPartido.setPuntosVisitante(datos[6]);
		
		return nuevoPartido;
	}

	/**
	 * 
	 * @return (int) número de líneas en el fichero
	 */
	private static int numeroLineasFichero() {
		int numLineas = 0;
		try {
			String linea = "";
			FileReader fReader = new FileReader(fichero);
			BufferedReader bufReader = new BufferedReader(fReader);
			while((linea =bufReader.readLine()) != null) {
				numLineas++;
			}
			fReader.close();
			bufReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return numLineas;
	}
}
