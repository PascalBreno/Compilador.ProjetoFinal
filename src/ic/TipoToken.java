package ic;

public enum TipoToken {
    //token
    Identificador(""),
    Then("Then"),
    numeroInt(""),
    numeroReal(""),
    PalavraReservadaIF("If"),
    OperadordeAtribuicao(":="),
    //operadores Matematicos
    OperadorAritmeticoMais("+"),
    divisao("/"),
    subtracao("-"),
    multiplicacao("*"),
    // Operadores de atribuição
    menorIgual(">="),
    diferente("<>"),
    menor("<"),
    maior(">"),
    PontoEVirgula(";"),
    integer("integer"),
    abreParenteces("("),
    fechaParenteses(")"),
    var("var"),
    Virgula(","),
    doispontos(":"),
    program(" "),
    real("real"),
    Error(""),
    fimDeBloco("$"),
    inicioDo("do"),
    inicioWhile("while"),
    inicioWrite("write"),
    tokenReada("read"),
    tokenElse("else"),
    begin("begin"),
    end("end"),
    // Erros
    TipodeVariavel("Tipo de Variavel (Real ou Integer)"),
    IdentificadorOuPalavraReservada("Esperava um IF ou Um identificador");

    //  {var, : , id, , , integer, real, ; , :=, if, then,+}



    private String token;

    TipoToken(String token){

        this.token= token;
    }
}
