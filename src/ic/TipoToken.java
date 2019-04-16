package ic;

public enum TipoToken {
    Identificador(""),
    Then("Then"),
    PalavraReservadaIF("If"),
    OperadordeAtribuicao(":="),
    OperadorAritmeticoMais("+"),
    var("var"),
    Virgula(","),
    doispontos(":"),
    PontoEVirgula(";"),
    integer("integer"),
    real("real"),
    Error(""),
    TipodeVariavel("Tipo de Variavel (Real ou Integer)"),
    IdentificadorOuPalavraReservada("Esperava um IF ou Um identificador");

    //  {var, : , id, , , integer, real, ; , :=, if, then,+}



    private String token;

    TipoToken(String token){

        this.token= token;
    }
}
