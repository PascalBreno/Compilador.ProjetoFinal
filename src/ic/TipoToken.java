package ic;

public enum TipoToken {
    Identificador(""),
    Then("Then"),
    PalavraReservadaIF("If"),
    OperadordeAtribuicao(":="),
    OperadorAritmeticoMais("+"),
    Var("Var"),
    Virgula(","),
    doispontos(":"),
    PontoEVirgula(";"),
    Integer("Integer"),
    Real("Real"),
    Error(""),
    TipodeVariavel("Tipo de Variavel (Real ou Integer)"),
    IdentificadorOuPalavraReservada("Esperava um IF ou Um identificador"),
    Final("$");

    //  {var, : , id, , , integer, real, ; , :=, if, then,+}



    private String token;

    TipoToken(String token){

        this.token= token;
    }
}
