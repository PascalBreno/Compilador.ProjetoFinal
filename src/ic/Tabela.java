package ic;

class Tabela {
    Token token;
    TipoToken tipoToken ;
    String bloco;
    Integer endereco;
    Tabela(Token cod, TipoToken tipoToken, String bloco, Integer endereco) {
        this.token = cod;
        this.tipoToken = tipoToken;
        this.bloco = bloco;
        this.endereco = endereco;
    }

}

