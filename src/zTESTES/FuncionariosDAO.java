package zTESTES;

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
public class FuncionariosDAO {

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
     * Arquivo baseFuncionarios.dat será o destino das informações ref a Funcionários.
     */
    private static final String BASE_FUNCIONARIOS = PASTA_USUARIO + BARRA + "labprog" + BARRA + "rfassistencia" + BARRA + "baseFuncionarios.dat";

    /**
     *
     * @return Esse método retorna TRUE quando o programa acha os arquivos
     * baseFuncionarios.dat, baseFuncionarios.dat e baseFuncionarios.dat no sistema
     * de arquivos.
     */
    public static boolean verificaBaseFuncionarios() {
        File bdFunconarios = new File(BASE_FUNCIONARIOS);
        if (!bdFunconarios.exists()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Método que "zera o banco de dados", gravando no arquivo uma lista vazia
     */
    public static void gravaListaBaseFuncionarios() {
        List<Funcionarios> listaFuncionarios = new ArrayList<>();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BASE_FUNCIONARIOS))) {
            oos.writeObject(listaFuncionarios);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param o
     */
    public static void gravaObjeto(List<Funcionarios> listaFuncionarios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BASE_FUNCIONARIOS))) {
            oos.writeObject(listaFuncionarios);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param l
     * @return
     */
    public static List<Funcionarios> leObjeto() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BASE_FUNCIONARIOS))) {
            List<Funcionarios> listaFuncionarios = (List<Funcionarios>) ois.readObject();
            return listaFuncionarios;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
