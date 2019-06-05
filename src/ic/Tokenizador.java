package ic;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


class Tokenizador extends JFrame {

    private String Codigo;
    private int posicaoAtual = 0;
    private Peex peex = new Peex();
    List<Token> token = new ArrayList<>();
    boolean Errotokenizador = false;
    Integer linha =0;
    Tokenizador(String codigo) {
        this.Codigo = codigo;
    }  // Iniciar o Tokenizador aderindo o código lido.

    void CriarTokens() {
        char catual, cprox;
        peex.novoPeex();
        int valorMaximo = Codigo.length() - 1;
        while (posicaoAtual < valorMaximo) {
            retirarEspaco(Codigo);
            do {
                // Recebe o char atual e o seguinte.
                catual = Codigo.charAt(posicaoAtual);
                if (posicaoAtual + 1 < valorMaximo)                  //Aqui estou verificando se o próximo valor é maior que o tamanho da String
                    cprox = Codigo.charAt(posicaoAtual + 1);          // Isso evita erro de pegar um valor que não tem na string
                else
                    cprox = Codigo.charAt(posicaoAtual);

                //  {var, : , id, , , integer, real, ; , :=, if, then,+}
                //#sessao
                if (catual == ' ') {
                    AnalisarPalavra(peex);
                    peex.novoPeex();
                    posicaoAtual++;
                    break;
                }
                else if (catual=='*'){
                        AnalisarPalavra(peex);
                        AdicionarToken("*", TipoToken.multiplicacao);
                        peex.novoPeex();
                        posicaoAtual++;
                        break;
                }
                else if (catual=='<'){
                    if(cprox=='>'){
                        AnalisarPalavra(peex);
                        AdicionarToken("<>", TipoToken.diferente);//Analiza a palavra lida até o simbolo de pular Linha
                        peex.novoPeex();
                        posicaoAtual=posicaoAtual+2;
                        break;
                    }else if(cprox=='='){
                        AnalisarPalavra(peex);
                        AdicionarToken("<=", TipoToken.menorIgual);
                        peex.novoPeex();
                        posicaoAtual=posicaoAtual+2;
                        break;
                    }else{
                        AnalisarPalavra(peex);
                        AdicionarToken("<", TipoToken.menor);
                        peex.novoPeex();
                        posicaoAtual++;
                        break;
                    }
                }
                else if(catual =='$'){
                    AnalisarPalavra(peex);
                    AdicionarToken("$", TipoToken.fimDeBloco);
                    peex.novoPeex();
                    posicaoAtual++;
                    break;
                }
                else if (catual=='>'){
                    if(cprox=='='){
                        AnalisarPalavra(peex);
                        AdicionarToken(">=", TipoToken.maiorigual);//Analiza a palavra lida até o simbolo de pular Linha
                        peex.novoPeex();
                        posicaoAtual=posicaoAtual+2;
                        break;
                    }else{
                        AnalisarPalavra(peex);
                        AdicionarToken(">", TipoToken.maior);
                        peex.novoPeex();
                        posicaoAtual++;
                        break;
                    }
                }
                else if (catual == ',') {
                    AnalisarPalavra(peex);
                    AdicionarToken(",", TipoToken.Virgula);
                    peex.novoPeex();
                    posicaoAtual++;
                    break;
                } else if (catual == '{') {
                   analisarcomentario2();
                }
                else if (catual == '\n') {
                    AnalisarPalavra(peex);    //Analiza a palavra lida até o simbolo de pular Linha
                    peex.novoPeex();
                    posicaoAtual++;
                    linha++;
                    break;
                }
                else if (catual =='-'){
                    AnalisarPalavra(peex);
                    AdicionarToken("-", TipoToken.subtracao);
                    peex.novoPeex();
                    posicaoAtual++;
                    break;
                }
                else if (catual=='/'){
                    if(cprox=='*'){
                        analisarcomentario();
                    }else{
                        AnalisarPalavra(peex);
                        AdicionarToken("/", TipoToken.divisao);
                        peex.novoPeex();
                        posicaoAtual++;
                        break;
                    }
                }
                else if (catual == ';') {
                    AnalisarPalavra(peex);
                    AdicionarToken(";", TipoToken.PontoEVirgula);//Analiza a palavra lida até o simbolo de pular Linha
                    peex.novoPeex();
                    posicaoAtual++;
                    break;
                }
                else if (catual=='('){
                    AnalisarPalavra(peex);
                    AdicionarToken("(", TipoToken.abreParenteces);//Analiza a palavra lida até o simbolo de pular Linha
                    peex.novoPeex();
                    posicaoAtual++;
                    break;
                }
               else if (catual==')'){
                    AnalisarPalavra(peex);
                    AdicionarToken(")", TipoToken.fechaParenteses);//Analiza a palavra lida até o simbolo de pular Linha
                    peex.novoPeex();
                    posicaoAtual++;
                    break;
                }
                else if (catual == ':') {
                    if(cprox == '=') {
                        AnalisarPalavra(peex);
                        AdicionarToken(":=", TipoToken.OperadordeAtribuicao);
                        peex.novoPeex();
                        posicaoAtual = posicaoAtual + 2;
                        break;
                    }else{
                        AnalisarPalavra(peex);
                        AdicionarToken(":", TipoToken.doispontos);
                        peex.novoPeex();
                        posicaoAtual = posicaoAtual + 1;
                    }
                }
               else if (catual == '+') {
                    AnalisarPalavra(peex);
                    AdicionarToken("+", TipoToken.OperadorAritmeticoMais);
                    peex.novoPeex();
                    posicaoAtual++;
                    break;
                } else {
                    peex.palavra += catual;        //Adiciona ao Peex o ultimo caracter
                    posicaoAtual++;
                }
            } while (posicaoAtual < valorMaximo);
            peex.novoPeex();
        }
    }

    private void analisarcomentario2() {
        char catual =' ';
        do{
            catual = Codigo.charAt(this.posicaoAtual);
            this.posicaoAtual++;
        }while(catual!='}');
    }

    private void analisarcomentario() {
        char catual =' ';
        char cprox =' ';
        do{
            catual = Codigo.charAt(this.posicaoAtual);
            cprox = Codigo.charAt(this.posicaoAtual+1);
            this.posicaoAtual++;
        }while(catual!='*' || cprox !='/');
        this.posicaoAtual++;
    }

    private void AnalisarPalavra(Peex peex) {
        if (peex.palavra.equals("then")) {
            AdicionarToken(peex.palavra, TipoToken.Then);
        } else if (peex.palavra.equals("if")) {
            analisarIF(peex);
        } else if (peex.palavra.equals(TipoToken.real.toString())) {
            AdicionarToken(peex.palavra, TipoToken.real);
        } else if (peex.palavra.equals(TipoToken.integer.toString())) {
            AdicionarToken(peex.palavra, TipoToken.integer);
        } else if (peex.palavra.equals(TipoToken.var.toString())) {
            AdicionarToken(peex.palavra, TipoToken.var);
        } else if (peex.palavra.equals("do")) {
            AdicionarToken(peex.palavra, TipoToken.inicioDo);
        } else if (peex.palavra.equals("while")) {
            AdicionarToken(peex.palavra, TipoToken.inicioWhile);
        } else if (peex.palavra.equals("write")) {
            AdicionarToken(peex.palavra, TipoToken.inicioWrite);
        } else if (peex.palavra.equals("read")) {
            AdicionarToken(peex.palavra, TipoToken.tokenReada);
        } else if (peex.palavra.equals("else")) {
            AdicionarToken(peex.palavra, TipoToken.tokenElse);
        } else if (peex.palavra.equals(TipoToken.begin.toString())) {
            AdicionarToken(peex.palavra, TipoToken.begin);
        } else if (peex.palavra.equals(TipoToken.program.toString())) {
            AdicionarToken(peex.palavra, TipoToken.program);
        } else if (peex.palavra.equals(TipoToken.procedure.toString())) {
            AdicionarToken(peex.palavra, TipoToken.procedure);
        } else if (peex.palavra.equals(TipoToken.end.toString())) {
            AdicionarToken(peex.palavra, TipoToken.end);
        } else {
            //Aqui irei analisar a palavra que foi inserida antes dos operadores de CHAR
            if (!(peex.palavra.equals("")) && !(peex.palavra.equals(" ") ) && !(peex.palavra.equals("\n"))) {
                if (!analisarnumero(peex.palavra)) {
                    if (!analisarnumerodouble(peex.palavra)){
                        String alfabetoError = "0123456789!@#$%&;,./[]^~:|";
                        String simbolosError = "!@#$%&;,./[]^~:|";
                        boolean error = true;
                    for (int i = 0; i < 26; i++) {
                        if (peex.palavra.charAt(0) == alfabetoError.charAt(i)) {
                            error = true;
                            break;
                        } else {
                            error = false;
                        }
                    }
                    if (error) {
                        AdicionarToken(peex.palavra, TipoToken.Error);
                        Errotokenizador = true;
                    } else {
                        for (int x = 0; x < peex.palavra.length(); x++) {
                            for (int y = 0; y < 16; y++) {
                                if (peex.palavra.charAt(x) == simbolosError.charAt(y)) {
                                    error = true;
                                    break;
                                } else {
                                    error = false;
                                }
                            }
                        }
                        if (error) {
                            AdicionarToken(peex.palavra, TipoToken.Error);
                            Errotokenizador = true;
                        } else
                            AdicionarToken(peex.palavra, TipoToken.Identificador);
                    }
                }

            }}
        }
    }

    private boolean analisarnumerodouble(String numero) {
        String numeroDouble= ".0123456789";
        boolean result = false;
        for(int i=0; i<numero.length();i++){
            for (int j=0; j<11;j++) {
                if (numero.charAt(i) == numeroDouble.charAt(j)) {
                    result = true;
                    break;
                }else{
                    result = false;
                }
            }
            if(!result)
                break;
        }
        if(result)
            AdicionarToken(peex.palavra, TipoToken.numeroReal);

        return result;
    }

    private boolean analisarnumero(String numero) {
        String numeroInt = "0123456789";
        String numeroDouble= ".0123456789";
        boolean result = false;
        for(int i=0; i<numero.length();i++){
            for (int j=0; j<10;j++) {
                if (numero.charAt(i) == numeroInt.charAt(j)) {
                    result = true;
                }else{
                    result = false;
                    break;
                }
            }
        }
        if(result)
            AdicionarToken(peex.palavra, TipoToken.numeroInt);


        return result;
    }

    private void AdicionarToken(String string_Token, TipoToken tipoToken) {
        Token newtoken;
        newtoken = new Token(string_Token, tipoToken,this.linha);
        token.add(newtoken);
    }

    private void analisarIF(Peex peex) {
        Token palavrareservadaIF;
        palavrareservadaIF = new Token(peex.palavra, TipoToken.PalavraReservadaIF,this.linha);
        token.add(palavrareservadaIF);
    }

    void ImprimirTokens() {
        StringBuilder codigo = new StringBuilder();
        for (Token token1 : token) {
            codigo.append(token1.tipoToken).append("\t----> ").append(token1.cod)
                    .append("\t Na linha : "+ (token1.linha+1)).append("\n");
        }
        System.out.println(codigo);
        System.out.println("==========Fim da analise de Tokens==========\n");
    }

    private void analisarInt(Peex peex) {
        Token integer;
        integer = new Token(peex.palavra, TipoToken.integer,this.linha);
        token.add(integer);
        peex.novoPeex();
    }

    private void retirarEspaco(String codigo) {
        char catual = codigo.charAt(this.posicaoAtual);
        if (catual == ' ') {
            do {
                catual = codigo.charAt(this.posicaoAtual);
                this.posicaoAtual++;
            } while (catual != ' ');
        }
    }
}
