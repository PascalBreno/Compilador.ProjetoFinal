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

                if (catual == ' ') {
                    AnalisarPalavra(peex);
                    peex.novoPeex();
                    posicaoAtual++;
                    break;
                }
                if (catual == ',') {
                    AnalisarPalavra(peex);
                    AdicionarToken(",", TipoToken.Virgula);
                    peex.novoPeex();
                    posicaoAtual++;
                    break;
                }
                if (catual == ':' && cprox != '=') {
                    AnalisarPalavra(peex);
                    AdicionarToken(":", TipoToken.doispontos);
                    peex.novoPeex();
                    posicaoAtual++;
                    break;
                }
                if (catual == '\n') {
                    AnalisarPalavra(peex);    //Analiza a palavra lida até o simbolo de pular Linha
                    peex.novoPeex();
                    posicaoAtual++;
                    break;
                }
                if (catual == ';') {
                    AnalisarPalavra(peex);
                    AdicionarToken(";", TipoToken.PontoEVirgula);//Analiza a palavra lida até o simbolo de pular Linha
                    peex.novoPeex();
                    posicaoAtual++;
                    break;
                }
                if ((catual == ':') && (cprox == '=')) {
                    AnalisarPalavra(peex);
                    AdicionarToken(":=", TipoToken.OperadordeAtribuicao);
                    peex.novoPeex();
                    posicaoAtual = posicaoAtual + 2;
                    break;
                }

                if (catual == '+') {
                    AnalisarPalavra(peex);
                    AdicionarToken("+", TipoToken.OperadorAritmeticoMais);
                    peex.novoPeex();
                    posicaoAtual++;
                    break;
                }
                peex.palavra += catual;        //Adiciona ao Peex o ultimo caracter
                posicaoAtual++;
            } while (posicaoAtual < valorMaximo);
            peex.novoPeex();
        }
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
        } else {
            //Aqui irei analisar a palavra que foi inserida antes dos operadores de CHAR
            if (!(peex.palavra.equals("")) && !(peex.palavra.equals(" "))) {
                if (peex.palavra.equals("int ") || peex.palavra.equals("int")) {
                    analisarInt(peex);
                }
                if (!(peex.palavra.equals(" ")) && !(peex.palavra.equals("\n"))) {

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
            }
        }
    }

    private void AdicionarToken(String string_Token, TipoToken tipoToken) {
        Token newtoken;
        newtoken = new Token(string_Token, tipoToken);
        token.add(newtoken);
    }

    private void analisarIF(Peex peex) {
        Token palavrareservadaIF;
        palavrareservadaIF = new Token(peex.palavra, TipoToken.PalavraReservadaIF);
        token.add(palavrareservadaIF);
    }

    void ImprimirTokens() {
        StringBuilder codigo = new StringBuilder();
        for (Token token1 : token) {
            codigo.append(token1.tipoToken).append("\t----> ").append(token1.cod).append("\n");
        }
        System.out.println(codigo);
        System.out.println("==========Fim da analise de Tokens==========\n");
    }

    private void analisarInt(Peex peex) {
        Token integer;
        integer = new Token(peex.palavra, TipoToken.integer);
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
