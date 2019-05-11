package ic;

public class Token {
    String cod="";
    TipoToken tipoToken ;
    Integer linha;
    public Token(String cod, TipoToken tipoToken, Integer linha) {
        this.cod = cod;
        this.tipoToken = tipoToken;
        this.linha = linha;
    }

}
