/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

/**
 *
 * @author Rodrigo
 */
public class testeGeral {
    
    public static void main(String[] args) {
        
        BdGeralController bdGeralCtrl = new BdGeralController();
        
        if (!bdGeralCtrl.verificaCaminhoGeral()) {
            System.out.println("parece que nem a pasta tem viu");
            bdGeralCtrl.criaCaminhoBaseGeral();
            System.out.println("Criei ta");
            System.out.println("vou criar os arquivos agora  gravar a lista base");
            bdGeralCtrl.criaArquivoBaseChamados();
            bdGeralCtrl.gravaListaBaseChamados();
        } else {
            System.out.println("Tudo sussa");
        }
        
        
    }
}
