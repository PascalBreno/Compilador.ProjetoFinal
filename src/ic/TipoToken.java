package ic;

public enum TipoToken {
    //token
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
    abreParenteces("("),
    fechaParenteses(")"),
    diferente("<>"),
    menor("<"),
    maior(">"),
    menorIgual(">="),
    real("real"),
    Error(""),
    fimDeBloco("$"),
    inicioDo("do"),
    inicioWhile("While"),
    inicioWrite("write"),
    tokenReada("read"),
    tokenElse("else"),
    // Erros
    TipodeVariavel("Tipo de Variavel (Real ou Integer)"),
    IdentificadorOuPalavraReservada("Esperava um IF ou Um identificador");

    //  {var, : , id, , , integer, real, ; , :=, if, then,+}



    private String token;

    TipoToken(String token){

        this.token= token;
    }
}
