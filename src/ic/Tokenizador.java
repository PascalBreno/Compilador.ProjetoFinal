package ic;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class Tokenizador extends JFrame {

    public String Codigo;
    private int posicaoAtual = 0;
    private Peex peex = new Peex();
    public List<Token> token = new ArrayList<Token>();
    private int ValorMaximo = 0;

    public Tokenizador(String codigo) {
        this.Codigo = codigo;
    }  // Iniciar o Tokenizador aderindo o código lido.

    public void CriarTokens() {
        char catual, cprox = ' ', canterior;
        peex.novoPeex();
        this.ValorMaximo = Codigo.length() - 1;
        while (posicaoAtual < ValorMaximo) {
            retirarEspaco(Codigo);
            do {
                // Recebe o char atual e o seguinte.
                catual = Codigo.charAt(posicaoAtual);
                if (posicaoAtual == 0)
                    canterior = Codigo.charAt(posicaoAtual);
                else
                    canterior = Codigo.charAt(posicaoAtual - 1);
                if (posicaoAtual + 1 < ValorMaximo)                  //Aqui estou verificando se o próximo valor é maior que o tamanho da String
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
                if(catual ==','){
                    AnalisarPalavra(peex);
                    AdicionarToken(",", TipoToken.Virgula);
                    peex.novoPeex();
                    posicaoAtual++;
                    break;
                }
                if(catual == ':' && cprox!='='){
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
                if (catual==';'){
                    AnalisarPalavra(peex);
                    AdicionarToken(";",TipoToken.PontoEVirgula);//Analiza a palavra lida até o simbolo de pular Linha
                    peex.novoPeex();
                    posicaoAtual++;
                    break;
                }
                if (catual == ':' && cprox == '=') {
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
                if (catual == ' ')
                    break;
                posicaoAtual++;
            } while (posicaoAtual < ValorMaximo);
            peex.novoPeex();
        }
    }

    public void AnalisarPalavra(Peex peex) {
        char catual = Codigo.charAt(posicaoAtual);
        char cprox = ' ';
        if (peex.palavra.equals("then")){
            AdicionarToken(peex.palavra, TipoToken.Then);
        }else if (peex.palavra.equals("if")) {
            analisarIF(peex);
        } else if(peex.palavra.equals("Real")) {
            AdicionarToken(peex.palavra, TipoToken.Real);

        }else if(peex.palavra.equals("Integer")|| peex.palavra.equals("integer")) {
            AdicionarToken(peex.palavra, TipoToken.Integer);
        }else if(peex.palavra.equals("var")) {
            AdicionarToken(peex.palavra, TipoToken.Var);
        }else if(peex.palavra.equals("Real") || peex.palavra.equals("real")) {
            AdicionarToken("Real", TipoToken.Real);
        }else{
            if((posicaoAtual + 1 < Codigo.length()))
                cprox = Codigo.charAt(posicaoAtual + 1);
            cprox = Codigo.charAt(posicaoAtual);

            //Aqui irei analisar a palavra que foi inserida antes dos operadores de CHAR
            if (!(peex.palavra.equals("")) && !(peex.palavra.equals(" "))) {
                if (peex.palavra.equals("int ") || peex.palavra.equals("int")) {
                    analisarInt(peex, Codigo, catual, cprox);
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
                        AdicionarToken("Error", TipoToken.Error);
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
                        if (error)
                            AdicionarToken("Error", TipoToken.Error);
                        else
                            AdicionarToken(peex.palavra, TipoToken.Identificadores);
                    }
                }
            }
            }
        }

    public void AdicionarToken(String string_Token, TipoToken tipoToken) {
        Token newtoken;
        newtoken = new Token(string_Token, tipoToken);
        token.add(newtoken);
    }

    public void analisarIF(Peex peex) {
        Token palavrareservadaIF;
        palavrareservadaIF = new Token(peex.palavra, TipoToken.PalavraReservadaIF);
        token.add(palavrareservadaIF);
    }

    public void ImprimirTokens() {
        String codigo = "";
        for (int i = 0; i < token.size(); i++) {
            codigo += "\n\t" + token.get(i).tipoToken + "----> " + token.get(i).cod + "\n";
        }
        System.out.println(codigo);
    }

    public void analisarInt(Peex peex, String codigo, char catual, char cprox) {
        Token integer;
        integer = new Token(peex.palavra, TipoToken.Integer);
        token.add(integer);
        peex.novoPeex();
        //Adicionando o token de comentário na lista
    }

    public void retirarEspaco(String codigo) {
        char catual = codigo.charAt(this.posicaoAtual);
        if (catual == ' ') {
            do {
                catual = codigo.charAt(this.posicaoAtual);
                this.posicaoAtual++;
            } while (catual != ' ');
        }
    }
}
