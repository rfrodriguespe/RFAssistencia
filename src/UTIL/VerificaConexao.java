package UTIL;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class VerificaConexao {

    public static boolean consegueConectar() {
        try {
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (IOException e) {
            System.out.println(e.toString().substring(9, 29));
            return false;
        }
    }
}
