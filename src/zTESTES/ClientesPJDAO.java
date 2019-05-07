package zTESTES;

import MODEL.ClientesPJModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rodrigo
 */
public class ClientesPJDAO {

    /**
     * Variável para introduzir a barra correta, indepentende do S.O
     */
    private static final String BARRA = System.getProperty("file.separator");

    /**
     * Variável que indica a pasta do próprio usuário, afim de não haver
     * problemas par a gravação de arquivos.
     */
    private static final String PASTA_USUARIO = System.getProperty("user.home");

    /**
     * Arquivo baseClientesPJ.dat será o destino das informações ref a Clientes
     * Pessoa Jurídica.
     */
    private static final String BASE_CLIENTESPJ = PASTA_USUARIO + BARRA + "labprog" + BARRA + "rfassistencia" + BARRA + "baseClientesPJ.dat";

    /**
     * Arquivo baseClientesPJ.dat será o destino das informações ref a Clientes
     * Pessoa Jurídica.
     */
    /**
     *
     * @return Esse método retorna TRUE quando o programa acha os arquivos
     * baseClientesPJ.dat, baseClientesPJ.dat e baseFuncionarios.dat no sistema
     * de arquivos.
     */
    public static boolean verificaBaseClientesPJ() {
        File bdCliPf = new File(BASE_CLIENTESPJ);
        if (!bdCliPf.exists()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Método que "zera o banco de dados", gravando no arquivo uma lista vazia
     */
    public static void gravaListaBaseClientesPJ() {
        List<ClientesPJModel> listaClientesPJ = new ArrayList<>();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BASE_CLIENTESPJ))) {
            oos.writeObject(listaClientesPJ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param o
     */
    public static void gravaObjeto(List<ClientesPJModel> listaPJ) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BASE_CLIENTESPJ))) {
            oos.writeObject(listaPJ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param l
     * @return
     */
    public static List<ClientesPJModel> leObjeto() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BASE_CLIENTESPJ))) {
            List<ClientesPJModel> listaClientesPJ = (List<ClientesPJModel>) ois.readObject();
            return listaClientesPJ;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
