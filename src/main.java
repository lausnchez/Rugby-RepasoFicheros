import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.text.DateFormat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;

public class main {

	static Scanner scan = new Scanner(System.in);
	static File fichero = new File("src/ficheros/partidos.csv");
	static final int NUM_SB = 20;
	
	public static void main(String[] args) {
		menu();
	}
	
	private static void menu() {
		int opcion;
		do{
			System.out.println("\nGESTIÓN DE PARTIDOS DE RUBGY");
			System.out.println("...................................");
			System.out.println("0. Salir");
			System.out.println("1. Mostrar todos los partidos");
			System.out.println("2. Mostrar estadios con más espectadores que la media");
			System.out.println("3. Mostrar el país con más tantos");
			System.out.println("4. Mostrar el país con menos tantos");
			System.out.println("5. Mostrar el país con más tantos recibidos");
			System.out.println("6. Pasar de fichero csv a fichero dat");
			opcion = scan.nextInt();
			switch(opcion) {
			case 1:
				mostrarTodo();
				break;
			case 2:
				estadiosMayoresMedia();
				break;
			case 3:
				paisMasTantos();
				break;
			case 4:
				paisMenosTantos();
				break;
			case 5:
				paisMasRecibidos();
				break;
			case 6:
				pasarDat();
				break;
			case 0: 
				System.out.println("Hasta pronto!");
				break;
			default:
				System.out.println("Opción incorrecta");
				break;
			}
		}while(opcion!= 0);
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
				
				nuevoPartido.setFecha(nuevoPartido.getFecha().replace('-', '/'));
				String fechaString = nuevoPartido.getFecha();
				DateFormat DFormat = new SimpleDateFormat("dd/MM/yyyy");
				DateTimeFormatter formatEntrada = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				DateTimeFormatter formatSalida = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate fecha = LocalDate.parse(nuevoPartido.getFecha(), formatEntrada);
				nuevoPartido.setFecha(fecha.format(formatSalida));
				celda = new StringBuffer(nuevoPartido.getFecha());
				celda.setLength(NUM_SB);
				System.out.print(celda);
				
				if(nuevoPartido.getPuntosLocal().equalsIgnoreCase("x")){
					celda = new StringBuffer(nuevoPartido.getJugadorLocal());	// Imprimir jugador local
					celda.setLength(NUM_SB);
					System.out.print(celda);
					celda = new StringBuffer(nuevoPartido.getJugadorVisitante());
					celda.setLength(nuevoPartido.getJugadorVisitante().length());
					System.out.print(celda);
					System.out.println(" -> PARTIDO APLAZADO");
				}else {
					celda = new StringBuffer(nuevoPartido.getJugadorVisitante().concat(" -> ".concat(String.valueOf(nuevoPartido.getPuntosVisitante()))));
					celda.setLength(NUM_SB);
					System.out.print(celda);
					celda = new StringBuffer(nuevoPartido.getJugadorVisitante().concat(" -> ".concat(String.valueOf(nuevoPartido.getPuntosVisitante()))));
					celda.setLength(NUM_SB);
					System.out.println(celda);
				}	
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
	 * 
	 */
	private static void estadiosMayoresMedia() {
		int media = mediaEspectadores();
		System.out.println("MEDIA -> " + media);
		try {
			FileReader fReader = new FileReader(fichero);
			BufferedReader bufReader = new BufferedReader(fReader);
			String linea = "";			
			while((linea = bufReader.readLine()) != null) {
				Partido nuevoPartido = dividirDatos(linea);
				if(nuevoPartido.getEspectadores()>media) System.out.println(nuevoPartido.getEstadio() + " -> " + nuevoPartido.getEspectadores());
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
		if(nuevoPartido.getPuntosLocal().equals("x")) {
			nuevoPartido.setPuntosVisitante("APLAZADO");
		}else {
			nuevoPartido.setPuntosVisitante(datos[6]);
		}
		return nuevoPartido;
	}

	/**
	 * 
	 * @return
	 */
	private static int mediaEspectadores() {
		int total = 0;
		int contador = 0;
		try {
			FileReader fReader = new FileReader(fichero);
			BufferedReader bufReader = new BufferedReader(fReader);
			String linea = "";			
			while((linea = bufReader.readLine()) != null) {
				Partido nuevoPartido = dividirDatos(linea);
				total += nuevoPartido.getEspectadores();
				contador++;
			}		
			fReader.close();
			bufReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return total/contador;
	}
	
	/**
	 * 
	 */
	private static void paisMasTantos() {
		HashMap<String, String> tantos = new HashMap<String, String>();
		try {
			FileReader fReader = new FileReader(fichero);
			BufferedReader bufReader = new BufferedReader(fReader);
			String linea = "";			
			while((linea = bufReader.readLine()) != null) {
				Partido nuevoPartido = dividirDatos(linea);
				// Si no los contiene
				if(!tantos.containsKey(nuevoPartido.getJugadorLocal())) {
					if(nuevoPartido.getPuntosLocal().equals("x")) tantos.put(nuevoPartido.getJugadorLocal(), "0");
					else tantos.put(nuevoPartido.getJugadorLocal(), nuevoPartido.getPuntosLocal());
				}
				if(!tantos.containsKey(nuevoPartido.getJugadorVisitante())) {
					if(nuevoPartido.getPuntosVisitante().equals("APLAZADO")) tantos.put(nuevoPartido.getJugadorVisitante(), "0");
					else tantos.put(nuevoPartido.getJugadorVisitante(), nuevoPartido.getPuntosVisitante());
				}
				// Si los contiene
				if(tantos.containsKey(nuevoPartido.getJugadorLocal())) {
					int sumatorio = 0;
					if(nuevoPartido.getPuntosLocal().equals("x")) sumatorio = 0;
					else sumatorio = Integer.parseInt(nuevoPartido.getPuntosLocal());
					int sumaPuntos = sumatorio + Integer.parseInt(tantos.get(nuevoPartido.getJugadorLocal()));
					tantos.put(nuevoPartido.getJugadorLocal(), String.valueOf(sumaPuntos));
				}
				if(tantos.containsKey(nuevoPartido.getJugadorVisitante())) {
					int sumatorio = 0;
					if(nuevoPartido.getPuntosVisitante().equals("APLAZADO")) sumatorio = 0;
					else sumatorio = Integer.parseInt(nuevoPartido.getPuntosVisitante());
					int sumaPuntos = sumatorio + Integer.parseInt(tantos.get(nuevoPartido.getJugadorVisitante()));
					tantos.put(nuevoPartido.getJugadorVisitante(), String.valueOf(sumaPuntos));
				}
			}	
			int mayorTantos = 0;
			String seleccion = "";
			for(String clave : tantos.keySet()) {
				//System.out.println(clave + ": " + tantos.get(clave));
				if(mayorTantos < Integer.parseInt(tantos.get(clave))) {
					mayorTantos = Integer.parseInt(tantos.get(clave));
					seleccion = clave;
				}
			}
			
			System.out.println("MAYOR CANTIDAD DE TANTOS : " + seleccion + ", " + mayorTantos);
			fReader.close();
			bufReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	private static void paisMenosTantos() {
		HashMap<String, String> tantos = new HashMap<String, String>();
		try {
			FileReader fReader = new FileReader(fichero);
			BufferedReader bufReader = new BufferedReader(fReader);
			String linea = "";			
			while((linea = bufReader.readLine()) != null) {
				Partido nuevoPartido = dividirDatos(linea);
				// Si no los contiene
				if(!tantos.containsKey(nuevoPartido.getJugadorLocal())) {
					if(nuevoPartido.getPuntosLocal().equals("x")) tantos.put(nuevoPartido.getJugadorLocal(), "0");
					else tantos.put(nuevoPartido.getJugadorLocal(), nuevoPartido.getPuntosLocal());
				}
				if(!tantos.containsKey(nuevoPartido.getJugadorVisitante())) {
					if(nuevoPartido.getPuntosVisitante().equals("APLAZADO")) tantos.put(nuevoPartido.getJugadorVisitante(), "0");
					else tantos.put(nuevoPartido.getJugadorVisitante(), nuevoPartido.getPuntosVisitante());
				}
				// Si los contiene
				if(tantos.containsKey(nuevoPartido.getJugadorLocal())) {
					int sumatorio = 0;
					if(nuevoPartido.getPuntosLocal().equals("x")) sumatorio = 0;
					else sumatorio = Integer.parseInt(nuevoPartido.getPuntosLocal());
					int sumaPuntos = sumatorio + Integer.parseInt(tantos.get(nuevoPartido.getJugadorLocal()));
					tantos.put(nuevoPartido.getJugadorLocal(), String.valueOf(sumaPuntos));
				}
				if(tantos.containsKey(nuevoPartido.getJugadorVisitante())) {
					int sumatorio = 0;
					if(nuevoPartido.getPuntosVisitante().equals("APLAZADO")) sumatorio = 0;
					else sumatorio = Integer.parseInt(nuevoPartido.getPuntosVisitante());
					int sumaPuntos = sumatorio + Integer.parseInt(tantos.get(nuevoPartido.getJugadorVisitante()));
					tantos.put(nuevoPartido.getJugadorVisitante(), String.valueOf(sumaPuntos));
				}
			}	
			int menorTantos = 999999999;
			String seleccion = "";
			for(String clave : tantos.keySet()) {
				//System.out.println(clave + ": " + tantos.get(clave));
				if(menorTantos > Integer.parseInt(tantos.get(clave))) {
					menorTantos = Integer.parseInt(tantos.get(clave));
					seleccion = clave;
				}
			}
			
			System.out.println("MENOR CANTIDAD DE TANTOS : " + seleccion + ", " + menorTantos);
			fReader.close();
			bufReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Recoge todas las líneas transformadas en partidos y crea un HashMap donde almacena el total
	 * de tantos recibidos. Comprueba cuál es el equipo que más tantos en contra tiene y lo muestra
	 * por consola.
	 */
	private static void paisMasRecibidos() {
		HashMap<String, Integer> tantos = new HashMap<String, Integer>();
		try {
			FileReader fReader = new FileReader(fichero);
			BufferedReader bufReader = new BufferedReader(fReader);
			String linea = "";			
			while((linea = bufReader.readLine()) != null) {
				Partido nuevoPartido = dividirDatos(linea);
			
				if(!tantos.containsKey(nuevoPartido.getJugadorLocal())) {
					tantos.put(nuevoPartido.getJugadorLocal(), 0);
				}
				if(!tantos.containsKey(nuevoPartido.getJugadorVisitante())) {
					tantos.put(nuevoPartido.getJugadorVisitante(), 0);
				}
				if(tantos.containsKey(nuevoPartido.getJugadorLocal())) {
					int suma;
					if(!nuevoPartido.getPuntosLocal().equals("x")) 
						suma = tantos.get(nuevoPartido.getJugadorLocal()) + Integer.parseInt(nuevoPartido.getPuntosVisitante());
					else suma = tantos.get(nuevoPartido.getJugadorLocal());
					tantos.put(nuevoPartido.getJugadorLocal(), suma);
				}
				if(tantos.containsKey(nuevoPartido.getJugadorVisitante())) {
					int suma;
					if(!nuevoPartido.getPuntosVisitante().equals("APLAZADO")) 
						suma = tantos.get(nuevoPartido.getJugadorVisitante()) + Integer.parseInt(nuevoPartido.getPuntosLocal());
					else suma = tantos.get(nuevoPartido.getJugadorVisitante());
					tantos.put(nuevoPartido.getJugadorVisitante(), suma);
				}				
			}
			
			String seleccion = "";
			int puntosMax = 0;
			for(String clave: tantos.keySet()) {
				if(puntosMax < tantos.get(clave)) {
					puntosMax = tantos.get(clave);
					seleccion = clave;
				}
			}
			System.out.println("La selección que ha recibido más goles es " + seleccion + " con " + puntosMax + " goles recibidos.");
			fReader.close();
			bufReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	private static void pasarDat() {
		try {
			FileReader fReader = new FileReader(fichero);
			BufferedReader bufReader = new BufferedReader(fReader);
			File ficheroDat = new File("src/ficheros/partidos.dat");
			RandomAccessFile raf = new RandomAccessFile(ficheroDat, "rw");
			String linea;
			while((linea = bufReader.readLine()) != null) {
				Partido nuevoPartido = dividirDatos(linea);
				raf.writeChars(new StringBuffer(pasarSBuffer(nuevoPartido.getFecha())).toString());
				raf.writeChars(new StringBuffer(pasarSBuffer(String.valueOf(nuevoPartido.getEspectadores()))).toString());
				raf.writeChars(new StringBuffer(pasarSBuffer(nuevoPartido.getEstadio())).toString());
				raf.writeChars(new StringBuffer(pasarSBuffer(nuevoPartido.getJugadorLocal())).toString());
				raf.writeChars(new StringBuffer(pasarSBuffer(nuevoPartido.getPuntosLocal())).toString());
				raf.writeChars(new StringBuffer(pasarSBuffer(nuevoPartido.getJugadorVisitante())).toString());
				raf.writeChars(new StringBuffer(pasarSBuffer(nuevoPartido.getPuntosVisitante())).toString());
			}	
			
			leerDat(ficheroDat);
			raf.close();
			bufReader.close();
			fReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void leerDat(File ficheroDat) {
		Partido[] listadoPartidos =  new Partido[numeroLineasFichero()];
		try {
			RandomAccessFile raf = new RandomAccessFile(ficheroDat, "r");
			String linea;
			int contador = 0, espectadores = 0, numPartidos = 0;
			String fecha ="", estadio ="", jugadorLocal="", puntosLocal = "", jugadorVisitante ="", puntosVisitante = "";			
			while((linea = raf.readLine()) != null) {
//				switch (contador) {
//				case 0:
//					fecha = linea;
//					break;
//				case 1:
//					espectadores = Integer.parseInt(linea);
//					break;
//				case 2:
//					estadio = linea;
//					break;
//				case 3:
//					jugadorLocal = linea;
//					break;
//				case 4:
//					puntosLocal = linea;
//					break;
//				case 5:
//					jugadorVisitante = linea; 
//					break;
//				case 6:
//					puntosVisitante = linea;
//					Partido nuevoPartido = new Partido(fecha, espectadores, estadio, jugadorLocal, puntosLocal, jugadorVisitante, puntosVisitante);
//					listadoPartidos[numPartidos] = nuevoPartido;
//					numPartidos++;
//					break;
//				}
//				if(contador < 6) contador++;
//				if(contador == 6) contador = 0;
				contador = 0;
				while(contador<6) {
					switch (contador) {
						case 0:
							fecha = linea;
							break;
						case 1:
							espectadores = Integer.parseInt(linea);
							break;
						case 2:
							estadio = linea;
							break;
						case 3:
							jugadorLocal = linea;
							break;
						case 4:
							puntosLocal = linea;
							break;
						case 5:
							jugadorVisitante = linea; 
							break;
						case 6:
							puntosVisitante = linea;
							Partido nuevoPartido = new Partido(fecha, espectadores, estadio, jugadorLocal, puntosLocal, jugadorVisitante, puntosVisitante);
							listadoPartidos[numPartidos] = nuevoPartido;
							numPartidos++;
							break;
					}
					contador++;
				}
			}
			raf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param linea
	 * @return
	 */
	private static StringBuffer pasarSBuffer(String linea) {
		StringBuffer sb =  new StringBuffer(linea);
		sb.setLength(30);
		return sb;
	}
	
	/**
	 * 
	 * @return
	 */
	private static HashMap<Integer, Partido> listadoPartidos(){
		try {
			FileReader fReader = new FileReader(fichero);
			BufferedReader bufReader = new BufferedReader(fReader);
			String linea = "";
			HashMap<Integer, Partido> listadoPartidos = new HashMap<Integer, Partido>();
			int id = 0;
			while((linea = bufReader.readLine()) != null) {
				Partido nuevoPartido = dividirDatos(linea);
				listadoPartidos.put(id, nuevoPartido);
				id++;
			}		
			fReader.close();
			bufReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listadoPartidos();
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
