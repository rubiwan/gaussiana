package config;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;


/**
 * Clase que implementa un logger en un fichero de texto

 * @author Anabel Diaz
 * @version 2.0 - 22/11/2025
 */
public class LoggerFichero {
    
    private static LoggerFichero log ;
    private BufferedWriter buffer;

    private LoggerFichero() {
        try {
            String rutafichero = System.getProperty("user.dir")+"//Ficheros//";
            String ficheroLog=rutafichero+"log.txt";
            buffer = new BufferedWriter(new FileWriter(ficheroLog,true));
        } catch (IOException ex) {
            System.err.println("Error al crear el fichero de log: " + ex.getMessage());
        }
    }


    public static LoggerFichero getInstance() {
        if(null == log) {
            log = new LoggerFichero();
        }
        return log;
    }

    public void info(String msg) {
        writeLog("INFO", msg);
    }

    public void error(String msg) {
        writeLog("ERROR", msg);
    }

    public void error(String msg, Exception e) {
        writeLog("ERROR", msg + " - " + e.getMessage());
    }
    
    public void warn(String msg) {
    	writeLog("WARN", msg);
    }

    private void writeLog(String level, String msg) {
        String linea = LocalDateTime.now() + " [" + level + "] " + msg;
        try {
            buffer.write(linea);
            buffer.newLine();
            buffer.flush();
        } catch (IOException ex) {
            System.err.println("Error al escribir en el fichero de log: " + ex.getMessage());
        }
    }
    
    public void closeLog() {
        try {
            if (buffer != null) {
                buffer.close();
            }
        } catch (IOException ex) {
            System.err.println("Error al cerrar el fichero de log: " + ex.getMessage());
        }
    }
}

