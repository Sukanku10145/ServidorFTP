import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FTP {
	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String in = "";
		
		FTPClient ftpClient = new FTPClient();

		try {
			// Conéctate al servidor FTP
			ftpClient.connect("ftp.dlptest.com");
			ftpClient.login("dlpuser", "rNrKYTX9g7z3RgJRmxWuGHbeu");

			// Obtén la lista de archivos
			FTPFile[] files = ftpClient.listFiles();
			
			while(in != "5") {
				System.out.print("Menú de opciones\n"
						+ "1. Listar archivos y directorios\n"
						+ "2. Crear una carpeta\n"
						+ "3. Crearun fichero en el servidor\n"
						+ "4. Descargar un fichero\n"
						+ "5. Salir\n"
						+ "Seleccione una opción:");
			}

			// Itera sobre los archivos y muestra si son directorios o archivos
			/*for (FTPFile file : files) {
				if (file.isDirectory()) {
					System.out.println("[DIR] " + file.getName());
				} else {
					System.out.println("[FILE] " + file.getName());
				}
			}*/

			// Cierra la conexión
			ftpClient.logout();
			ftpClient.disconnect();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
