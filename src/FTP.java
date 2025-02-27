import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FTP {
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	private static void listar(FTPClient ftpClient) throws IOException {
		System.out.println("\nListando archivos...\n\n");

		// Obtén la lista de archivos
		FTPFile[] files = ftpClient.listFiles();

		// Itera sobre los archivos y muestra si son directorios o archivos
		for (FTPFile file : files) {
			if (file.isDirectory()) {
				System.out.println("[DIR] " + file.getName());
			} else {
				System.out.println("[FILE] " + file.getName());
			}
		}
	}

	private static void crearCar(FTPClient ftpClient) throws IOException {
		String carpeta = "";

		System.out.print("\n\nInserte el nombre de la carpeta:");

		carpeta = reader.readLine();

		boolean success = ftpClient.makeDirectory("/"+carpeta);
		if (success) {
			System.out.println("\n"
					+ "-----------------------------\n"
					+ "|Carpeta creada exitosamente|\n"
					+ "-----------------------------\n");
		} else {
			System.out.println("\n"
					+ "-----------------------------\n"
					+ "|No se pudo crear la carpeta|\n"
					+ "-----------------------------\n");
		}
	}

	private static void crearFic(FTPClient ftpClient) throws IOException {
		String fichero = "";
		String ruta = "";

		System.out.print("\n\nInserte el nombre del fichero:");
		fichero = reader.readLine();

		System.out.print("\nInserte el la ruta donde desea insertarlo:");
		ruta = reader.readLine()+"/"+fichero;

		// Leer el archivo local
		File localFile = new File(fichero);
		if (!localFile.exists()) {
			localFile.createNewFile();
		}
		InputStream inputStream = new FileInputStream(localFile);

		System.out.println("\nSubiendo fichero...");
		boolean done = ftpClient.storeFile(ruta, inputStream);
		inputStream.close();

		if (done) {
			System.out.println("\n"
					+ "------------------------------\n"
					+ "|Fichero subido correctamente|\n"
					+ "------------------------------\n");
		} else {
			System.out.println("\n"
					+ "---------------------------\n"
					+ "|Error al subir el fichero|\n"
					+ "---------------------------\n");
		}
	}

	private static void descargar(FTPClient ftpClient) throws IOException {
		String fichero = "";

		System.out.print("\n\nInserte la ruta del fichero:");
		fichero = reader.readLine();

		String ruta = "C:/Users/DAM-A/eclipse-workspace/ServidorFTP/Descargas/"+fichero;

		// Leer el archivo local
		File localFile = new File(ruta);
		File parentDir = localFile.getParentFile();

		// Si la carpeta "Descargas" no existe, créala
		if (!parentDir.exists()) {
		    parentDir.mkdirs(); // Crea la carpeta
		}

		// Asegurar permisos de escritura
		if (localFile.exists()) {
		    localFile.setWritable(true);
		}
		
		if (localFile.createNewFile()) {

		} else {
			System.out.print("\nEl fichero ya existe, ¿desea sobrescribirlo?\n"
					+ "S/N:");

			Boolean correcto = true;
			while (correcto) {
				String existe = reader.readLine();
				if (existe.length() == 1) {
					switch (existe.toUpperCase()) {
					case "S":
						correcto = false;
						break;
					case "N":
						return;
					default:
						System.out.print("Error, escriba 'S' (Sí) o 'N' (No):");
						break;
					}
				} else {
					System.out.print("Error, escriba 'S' (Sí) o 'N' (No):");
				}
			}
		}
		
        // Descargar el archivo desde el FTP y sobrescribir el local
        try (OutputStream outputStream = new FileOutputStream(localFile)) {
            System.out.println("\nDescargando archivo...");
            boolean success = ftpClient.retrieveFile(fichero, outputStream);

            if (success) {
    			System.out.println("\n"
    					+ "------------------------------------------------\n"
    					+ "|Fichero descargado y sobrescrito correctamente|\n"
    					+ "------------------------------------------------\n");
            } else {
    			System.out.println("\n"
    					+ "-------------------------------\n"
    					+ "|Error al descargar el fichero|\n"
    					+ "-------------------------------\n");
            }
        }
	}

	public static void main(String[] args) {
		String in = "";
		Boolean salir = true;

		FTPClient ftpClient = new FTPClient();

		try {
			// Conéctate al servidor FTP
			ftpClient.connect("ftp.dlptest.com");
			ftpClient.login("dlpuser", "rNrKYTX9g7z3RgJRmxWuGHbeu");


			while(salir) {
				try {
					System.out.print("\n\n"
							+ "------------------------------------\n"
							+ "|        Menú de opciones          |\n"
							+ "------------------------------------\n"
							+ "|1. Listar archivos y directorios  |\n"
							+ "|2. Crear una carpeta              |\n"
							+ "|3. Crear un fichero en el servidor|\n"
							+ "|4. Descargar un fichero           |\n"
							+ "|5. Salir                          |\n"
							+ "------------------------------------\n"
							+ "Seleccione una opción:");

					in = reader.readLine();
					if (in.length() != 1) {
						System.out.println("\n\n"
								+ "-------------------------------\n"
								+ "|No escriba más de un carácter|\n"
								+ "-------------------------------\n");
					} else if (Integer.parseInt(in) > 0 && Integer.parseInt(in) < 6) {
						switch(Integer.parseInt(in)) {
						case 1:
							listar(ftpClient);
							break;
						case 2:
							crearCar(ftpClient);
							break;
						case 3:
							crearFic(ftpClient);
							break;
						case 4:
							descargar(ftpClient);
							break;
						case 5:
							salir = false;
							break;
						default:
							System.out.println("\n\n"
									+ "--------------------\n"
									+ "|Ha habido un error|\n"
									+ "--------------------\n");
							break;
						}
					} else {
						System.out.println("\n\n"
								+ "------------------------------------------------\n"
								+ "|Inserte un número perteneciente a las opciones|\n"
								+ "------------------------------------------------\n");
					}
				} catch (NumberFormatException e) {
					System.out.println("\n\n"
							+ "------------------------------\n"
							+ "|Inserte un número, no letras|\n"
							+ "------------------------------\n");
				}
			}

			// Cierra la conexión
			System.out.println("\n\n"
					+ "-------------------------\n"
					+ "|   Cerrando conexión   |\n"
					+ "-------------------------");
			ftpClient.logout();
			ftpClient.disconnect();
			System.out.println(""
					+ "|   Conexión cerrada    |\n"
					+ "-------------------------\n");



		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
