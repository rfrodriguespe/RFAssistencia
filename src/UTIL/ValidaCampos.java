package UTIL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidaCampos {
    
    public static boolean validaEmail(String email) {
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

    public static boolean validaCnpj(String cnpj) {

        String cnpjfCRU = cnpj.replace(".", "").replace("/", "").replace("-", "");
        String primeiraparte = cnpjfCRU.substring(0, 12);
        String div1primeiraparte = cnpjfCRU.substring(0, 4);
        String div2primeiraparte = cnpjfCRU.substring(4, 12);
        String segundaparte = cnpjfCRU.substring(0, 13);
        String div1segundaparte = cnpjfCRU.substring(0, 5);
        String div2segundaparte = cnpjfCRU.substring(5, 13);
        int pDigito = Integer.parseInt(cnpjfCRU.substring(12, 13));
        int sDigito = Integer.parseInt(cnpjfCRU.substring(13, 14));
        int primSoma = 0, div1primSoma = 0, div2primSoma = 0;
        int div1segSoma = 0, div2segSoma = 0, segSoma = 0;

        //Caclula o primeiro digito
        int cont = 5;
        for (int i = 0; i < div1primeiraparte.length(); i++) {
            div1primSoma += Character.getNumericValue(div1primeiraparte.charAt(i)) * cont;
            cont--;
        }
        int cont2 = 9;
        for (int i = 0; i < div2primeiraparte.length(); i++) {
            div2primSoma += Character.getNumericValue(div2primeiraparte.charAt(i)) * cont2;
            cont2--;
        }
        primSoma = div1primSoma + div2primSoma;

        int primeirodigito = 9999;
        if (primSoma % 11 < 2) {
            primeirodigito = 0;
        } else {
            primeirodigito = 11 - primSoma % 11;
        }
        //fim do calculo

        //calcula o segundo digito
        int cont3 = 6;
        for (int i = 0; i < div1segundaparte.length(); i++) {
            div1segSoma += Character.getNumericValue(div1segundaparte.charAt(i)) * cont3;
            cont3--;
        }
        int cont4 = 9;
        for (int i = 0; i < div2segundaparte.length(); i++) {
            div2segSoma += Character.getNumericValue(div2segundaparte.charAt(i)) * cont4;
            cont4--;
        }
        segSoma = div1segSoma + div2segSoma;

        int segundodigito = 9999;
        if (segSoma % 11 < 2) {
            segundodigito = 0;
        } else {
            segundodigito = 11 - segSoma % 11;
        }
        //fim do calculo

        if (pDigito == primeirodigito && sDigito == segundodigito) {
            return true;
        } else {
            return false;
        }

    }
    
    public static boolean validaCpf(String cpf) {

        String cpfCRU = cpf.replace(".", "").replace("-", "");
        
        //Verifica se o CPF digitado não é uma sequencia
        if (cpfCRU.equals("00000000000") || cpfCRU.equals("11111111111")
                || cpfCRU.equals("22222222222") || cpfCRU.equals("33333333333")
                || cpfCRU.equals("44444444444") || cpfCRU.equals("55555555555")
                || cpfCRU.equals("66666666666") || cpfCRU.equals("77777777777")
                || cpfCRU.equals("88888888888") || cpfCRU.equals("99999999999")
                || (cpfCRU.length() != 11)){
            return false;
        }
        //
        
        String primeiraparte = cpfCRU.substring(0, 9);
        String segundaparte = cpfCRU.substring(0, 10);
        int pDigito = Integer.parseInt(cpfCRU.substring(9, 10));
        int sDigito = Integer.parseInt(cpfCRU.substring(10, 11));
        int primeirasoma = 0, segundasoma = 0;
        
        
        
        //Caclula o primeiro digito
        int cont = 10;
        for (int i = 0; i < primeiraparte.length(); i++) {
            primeirasoma += Character.getNumericValue(primeiraparte.charAt(i)) * cont;
            cont--;
        }

        int primeirodigito = 9999;
        if (primeirasoma % 11 < 2) {
            primeirodigito = 0;
        } else {
            primeirodigito = 11 - primeirasoma % 11;
        }
        //fim do calculo

        //calcula o segundo digito
        int cont2 = 11;
        for (int i = 0; i < segundaparte.length(); i++) {
            segundasoma += Character.getNumericValue(segundaparte.charAt(i)) * cont2;
            cont2--;
        }

        int segundodigito = 9999;
        if (segundasoma % 11 < 2) {
            segundodigito = 0;
        } else {
            segundodigito = 11 - segundasoma % 11;
        }
        //fim do calculo

        if (pDigito == primeirodigito && sDigito == segundodigito) {
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean validaPis(String pis) {
        String pisCRU = pis.replace(".", "").replace("-", "");
        String primPartePis = pisCRU.substring(0, 10);
        String div1primPartePis = primPartePis.substring(0, 2);
        String div2primPartePis = primPartePis.substring(2, 10);
        int pDigito = Integer.parseInt(pisCRU.substring(10, 11));
        int primSoma = 0, div1primSoma = 0, div2primSoma = 0;

        //Calculando o digito verificador
        int cont = 3;
        for (int i = 0; i < div1primPartePis.length(); i++) {
            div1primSoma += Character.getNumericValue(div1primPartePis.charAt(i)) * cont;
            cont--;
        }
        int cont2 = 9;
        for (int i = 0; i < div2primPartePis.length(); i++) {
            div2primSoma += Character.getNumericValue(div2primPartePis.charAt(i)) * cont2;
            cont2--;
        }
        primSoma = div1primSoma + div2primSoma;

        int primeirodigito = 9999;
        if (primSoma % 11 < 2) {
            primeirodigito = 0;
        } else {
            primeirodigito = 11 - primSoma % 11;
        }
        //fim do calculo
        
        if (pDigito == primeirodigito) {
            return true;
        } else {
            return false;
        }
    }

}
