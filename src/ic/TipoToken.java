package ic;

public enum TipoToken {
    Identificadores(""),
    Then("Then"),
    PalavraReservadaIF("If"),
    OperadordeAtribuicao(":="),
    OperadorAritmeticoMais("+"),
    Var("Var"),
    Virgula(","),
    PontoEVirgula(";"),
    Integer("Integer"),
    Real("Real"),
    Error(""),
    Final("$");

    //  {var, : , id, , , integer, real, ; , :=, if, then,+}



    private String token;

    TipoToken(String token){

        this.token= token;
    }
}
