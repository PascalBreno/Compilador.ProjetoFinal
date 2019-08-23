package ic;

import kotlin.jvm.JvmOverloads;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.ErrorManager;

class Analisador extends Tokenizador {



    Analisador(String codigo) {
        super(codigo);
    }
    private int valorTokenAtual = -1;
    private Token tokenAtual;
    private Boolean error = false;
    private List<Token> tipotokenListId = new ArrayList<>();
    private List<Tabela> tabela = new ArrayList<>();
    private int ListID = 0;
    private Stack<String> E = new Stack<>();
    private List<String> R = new ArrayList<>();
    private List<Token> Id = new ArrayList<>();
    private Integer temp = 1;
    private List<String> Cod_inte = new ArrayList<>();
    private List<String> quad = new ArrayList<>();
    private Boolean atr = false;
    private String valorT = "";
    private Boolean valorE = false;
    private Stack<String> Temp = new Stack<String>();
    private Integer id = 0;
    private TipoToken TipoVar = TipoToken.integer;
    private List<Token> ListVar = new ArrayList<>();
    private String bloco = "Global";
    void tabela() {
        System.out.println("Inicio da Analise Descendente:\n");
        program();
        if (!error) {
            System.out.println("Passou pela analise!");
            imprimirTabela();
        } else
            System.out.println("Esta Errado");
    }

    private void imprimirTabela() {
        int tamanho = tabela.size();
        System.out.println("Tabela de Simbolos: ");
        System.out.println("------------------------");
        System.out.println("Nome\t\t\t\tTipo\t\t\t\t\tTipo Token\t\t\t\tBloco");
        System.out.println("------------------------");
        for(int i=0; i<tamanho;i++){
            System.out.println(tabela.get(i).token.cod + " \t\t\t\t"+tabela.get(i).tipoToken+"\t\t\t\t\t"+tabela.get(i).token.tipoToken+"\t\t\t\t"+tabela.get(i).bloco
                    +"\n================================================================");
        }
    }

    private void proxToken() {
        valorTokenAtual++;
        if (valorTokenAtual < token.size())
            tokenAtual = token.get(valorTokenAtual);
    }

    private void program() {
        proxToken();
        if (tokenAtual.tipoToken == TipoToken.program) {
            proxToken();
            if (tokenAtual.tipoToken == TipoToken.Identificador) {
                proxToken();
                corpo();
            } else {
                Error(TipoToken.Identificador);
            }
        } else {
            Error(TipoToken.program);
        }
    }

    private void imprimirCodInter() {
        Cod_inte.add("...");
        int tam = 0;
        do {
            System.out.println((tam + 1) + " = " + Cod_inte.get(tam) + "\n");
            tam++;
        } while (tam < Cod_inte.size());
        System.out.println("-------------------\n");
    }

    private void corpo() {
        dc();
        if (tokenAtual.tipoToken == TipoToken.begin) {
            proxToken();
            comandos();
            if (tokenAtual.tipoToken == TipoToken.end) {
            } else {
                Error(TipoToken.end);
            }
        } else {
            Error(TipoToken.begin);
        }
    }

    private void Error(TipoToken a) {
        error = true;
        System.out.println("Impossível continuar com a analise.");
        System.out.println("Error no token: " + tokenAtual.cod);
        System.out.println("Eu esperava um " + a);
        System.out.println("Linha"+ tokenAtual.linha);
        System.exit(1);
    }
    private void Error(String mensagem){
        System.out.println(mensagem);
        System.exit(1);
    }

    private void dc() {
        //<dc> ::= <dc_v> <mais_dc> | <dc_p> <mais_dc> | λ
        if (dc_v())
            mais_dc();
        else if (dc_p())
            mais_dc();
        else {
        }
    }

    private boolean dc_v() {
        //<dc_v> ::= var <variaveis> : <tipo_var>
        if (tokenAtual.tipoToken == TipoToken.var) {
            LimparListVar();
            proxToken();
            variaveis();
            if (tokenAtual.tipoToken == TipoToken.doispontos) {
                proxToken();
                tipo_var();
                CheckVar();
                InserirTSVar();
                return true;
            } else {
                Error(TipoToken.doispontos);
            }
        } else {
            return false;
        }
        return false;
    }

    private void InserirTSVar() {
        int tam = this.ListVar.size();

        for (int i=0; i<tam;i++){
            Tabela valor;
            if(this.TipoVar ==TipoToken.integer)
                valor = new Tabela(this.ListVar.get(i),TipoToken.integer, this.bloco );
            else
                valor = new Tabela(this.ListVar.get(i),TipoToken.real, this.bloco );
            this.tabela.add(valor);
        }
        LimparListVar();
    }

    private void CheckVar() {
        int tamVar = this.ListVar.size(); // Nº De variaveis
        int tamTabSim = this.tabela.size(); // Nº De Valores na tabela de Símbolo
        for (int i=0; i<tamVar; i++){
            for(int j=0;j<tamTabSim;j++){
                //Para cada Iteração você deve verificar com toda a Tabela.
                checkTS(j, i);
                }
            }
        }

    public void checkTS(int j, int i){
        if(this.tabela.get(j).token.cod.equals(this.ListVar.get(i).cod)
                && (this.tabela.get(j).bloco.equals(this.bloco))
                && this.tabela.get(j).tipoToken == this.TipoVar){
            int linha = this.tabela.get(j).token.linha+1;
            String msg = "Impossível Compilar. A variável {"+this.tabela.get(j).token.cod+"} foi declarado mais de uma vez na linha "+linha+"." ;
            Error(msg);

        }
    }
    private void LimparListVar() {
        this.ListVar.removeAll(this.ListVar);
    }

    private boolean dc_p() {
        //<dc_p> ::= procedure ident <parametros> <corpo_p>
        if (tokenAtual.tipoToken == TipoToken.procedure) {
            proxToken();
            if (tokenAtual.tipoToken == TipoToken.Identificador) {
                this.bloco = tokenAtual.cod;
                proxToken();
                parametros();
                corpo_p();
                return true;
            } else {
                Error(TipoToken.Identificador);
            }
        } else {
            return false;
        }
        return false;
    }

    private void corpo_p() {
        //<dc_loc> begin <comandos> end
        //<dc_loc> begin <comandos> end
        dc_loc();
        if (tokenAtual.tipoToken == TipoToken.begin) {
            proxToken();
            comandos();
            if (tokenAtual.tipoToken == TipoToken.end) {
                proxToken();
            } else {
                Error(TipoToken.end);
            }
        } else {
            Error(TipoToken.begin);
        }
    }

    private void dc_loc() {
        //<dc_v> <mais_dcloc> | λ
        if (dc_v()) {
            mais_dcloc();
        } else {

        }
    }

    private void mais_dcloc() {
        //; <dc_loc> | λ
        if (tokenAtual.tipoToken == TipoToken.PontoEVirgula) {
            proxToken();
            dc_loc();
        } else {

        }
    }

    private void lista_arg() {
        //(<argumentos>) | λ
        if (tokenAtual.tipoToken == TipoToken.abreParenteces) {
            proxToken();
            argumentos();
            if (tokenAtual.tipoToken == TipoToken.fechaParenteses) {
                proxToken();
            } else {
                Error(TipoToken.fechaParenteses);
            }
        } else {
            Error(TipoToken.abreParenteces);
        }
    }

    private void argumentos() {
        //ident <mais_ident>
        if (tokenAtual.tipoToken == TipoToken.Identificador) {
            proxToken();

            mais_ident();
        } else {
            Error(TipoToken.Identificador);
        }
    }

    private void mais_ident() {
        //; <argumentos> | λ

        if (tokenAtual.tipoToken == TipoToken.PontoEVirgula) {
            proxToken();
            argumentos();
        } else {
        }
    }

    private void pfalsa() {
        //else <comandos> | λ
        if (tokenAtual.tipoToken == TipoToken.tokenElse) {
            proxToken();
            comandos();
        } else {
            Error(TipoToken.tokenElse);
        }
    }

    private void comandos() {
        //<comando> <mais_comandos>
        comando();
        mais_comandos();
    }

    private void mais_comandos() {
        //; <comandos> | λ
        if (tokenAtual.tipoToken == TipoToken.PontoEVirgula) {
            proxToken();
            comandos();
        }
    }

    private void comando() {
        //<comando> ::= read (<variaveis>) |
        // write (<variaveis>) |
        // while <condicao> do <comandos> $ |
        // if <condicao> then <comandos> <pfalsa> $ |
        // ident <restoIdent>
        if (tokenAtual.tipoToken == TipoToken.tokenReada) {
            proxToken();
            if(tokenAtual.tipoToken==TipoToken.abreParenteces){
                proxToken();
                LimparListVar();
                variaveis();

                if(tokenAtual.tipoToken==TipoToken.multiplicacao.fechaParenteses){
                    proxToken();
                }else{
                    Error(TipoToken.fechaParenteses);
                }
            }else{
                Error(TipoToken.abreParenteces);
            }

        } else if (tokenAtual.tipoToken == TipoToken.inicioWrite) {
// write (<variaveis>) |
            proxToken();
            if(tokenAtual.tipoToken==TipoToken.abreParenteces){
                proxToken();
                LimparListVar();
                variaveis();
                if(tokenAtual.tipoToken==TipoToken.fechaParenteses){
                    proxToken();
                }else{
                    Error(TipoToken.fechaParenteses);
                }
            }else{
                Error(TipoToken.abreParenteces);
            }
        } else if (tokenAtual.tipoToken == TipoToken.inicioWhile) {
// while <condicao> do <comandos> $ |
            proxToken();
            condicao();
            if(tokenAtual.tipoToken == TipoToken.inicioDo){
                proxToken();
                comandos();
                if(tokenAtual.tipoToken==TipoToken.fimDeBloco){
                    proxToken();
                }else{
                    Error(TipoToken.fimDeBloco);
                }
            }else{
                Error(TipoToken.inicioDo);
            }
        } else if (tokenAtual.tipoToken == TipoToken.PalavraReservadaIF) {
            // if <condicao> then <comandos> <pfalsa> $ |
            proxToken();
            condicao();
            if(tokenAtual.tipoToken==TipoToken.Then){
                proxToken();
                comandos();
                pfalsa();
                if(tokenAtual.tipoToken==TipoToken.fimDeBloco){
                    proxToken();
                }else{
                    Error(TipoToken.fimDeBloco);
                }
            }else{
                Error(TipoToken.Then);
            }
        } else if (tokenAtual.tipoToken == TipoToken.Identificador) {

            restoIdent();
        } else {
            Error(TipoToken.Error);
        }
    }
    private Token tipoVarTS(){
        int tam =this.tabela.size();
        for(int i=0; i<tam;i++){
            if(this.tabela.get(i).token.cod.equals(tokenAtual.cod) && this.tabela.get(i).bloco.equals(this.bloco)){
                return this.tabela.get(i).token;
            }
        }
        for(int i=0; i<tam;i++){
            if(this.tabela.get(i).token.cod.equals(tokenAtual.cod)){
                return this.tabela.get(i).token;
            }
        }
        return null;
    }

    private void restoIdent() {
        Token token = tipoVarTS();
        if(token==null) {
            token = tokenAtual;
            this.TipoVar = token.tipoToken;
            proxToken();
            if (tokenAtual.tipoToken == TipoToken.OperadordeAtribuicao)
                Error("Error: É impossível continuar, a variável '" + token.cod + "' não foi declarada " +
                        "na linha " + token.linha+1);
        }else{
            proxToken();
        }
        // := <expressao> | <lista_arg>
        if (tokenAtual.tipoToken == TipoToken.OperadordeAtribuicao) {
            proxToken();
            expressao();
        } else {
            lista_arg();
        }
    }

    private void condicao() {
        //<condicao> ::= <expressao> <relacao> <expressao>
        expressao();
        relacao();
        expressao();
    }

    private void relacao() {
        //<relacao> ::= = | <> | >= | <= | > | <
        if (tokenAtual.tipoToken == TipoToken.igual) {
            proxToken();
        } else if (tokenAtual.tipoToken == TipoToken.diferente) {
            proxToken();
        } else if (tokenAtual.tipoToken == TipoToken.menorIgual) {
            proxToken();
        } else if (tokenAtual.tipoToken == TipoToken.maiorigual) {
            proxToken();
        } else if (tokenAtual.tipoToken == TipoToken.maior) {
            proxToken();
        } else if (tokenAtual.tipoToken == TipoToken.menor) {
            proxToken();
        } else {
            Error(TipoToken.Error);
        }
    }

    private void expressao() {
        //<expressao> ::= <termo> <outros_termos>
        termo();
        outros_termos();
    }

    private void op_un() {
        //<op_un> ::= + | - | λ
        if (tokenAtual.tipoToken == TipoToken.OperadorAritmeticoMais) {
            proxToken();

        } else if (tokenAtual.tipoToken == TipoToken.subtracao) {
            proxToken();

        }
    }

    private void outros_termos() {
        //<outros_termos> ::= <op_ad> <termo> <outros_termos> | λ
        if (op_ad()) {
            termo();
            outros_termos();
        } else {

        }
    }

    private boolean op_ad() {
        //<op_ad> ::= + | -
        if (tokenAtual.tipoToken == TipoToken.OperadorAritmeticoMais) {
            proxToken();
            return true;
        } else if (tokenAtual.tipoToken == TipoToken.subtracao) {
            proxToken();
            return true;
        } else {
            return false;
        }
    }

    private void termo() {
        //<termo> ::= <op_un> <fator> <mais_fatores>
        op_un();
        fator();
        mais_fatores();
    }

    private void mais_fatores() {
        //<mais_fatores> ::= <op_mul> <fator> <mais_fatores> | λ
        if (op_mul()) {
            fator();
            mais_fatores();
        } else {

        }
    }

    private boolean op_mul() {
        //<op_mul> ::= * | /
        if (tokenAtual.tipoToken == TipoToken.multiplicacao) {
            proxToken();
            return true;
        } else if (tokenAtual.tipoToken == TipoToken.divisao) {
            proxToken();
            return true;
        } else {
            return false;
        }
    }

    private void fator() {
        //<fator> ::= ident | numero_int | numero_real | (<expressao>)
        if (tokenAtual.tipoToken == TipoToken.Identificador) {
            proxToken();
        } else if (tokenAtual.tipoToken == TipoToken.numeroInt) {
            if(this.TipoVar!=TipoToken.numeroInt)
                Error("Tipos de variáveis não são compatíveis na linha "+this.tokenAtual.cod+" "+this.linha);
            proxToken();
        } else if (tokenAtual.tipoToken == TipoToken.numeroReal) {
            if(this.TipoVar!=TipoToken.numeroReal)
                Error("Tipos de variáveis não são compatíveis: "+this.TipoVar+ (this.tokenAtual.linha+1));
            proxToken();
        } else if (tokenAtual.tipoToken == TipoToken.abreParenteces) {
            proxToken();
            expressao();
            if (tokenAtual.tipoToken == TipoToken.fechaParenteses) {
                proxToken();
            } else {
                Error(TipoToken.fechaParenteses);
            }
        } else {
            Error(TipoToken.Error);
        }
    }

    private void parametros() {
        if (tokenAtual.tipoToken == TipoToken.abreParenteces) {
            proxToken();
            lista_par();
            if (tokenAtual.tipoToken == TipoToken.fechaParenteses) {
                proxToken();
            } else {
                Error(TipoToken.fechaParenteses);
            }
        } else {
        }

    }

    private void lista_par() {
        LimparListVar();
        variaveis();
        if (tokenAtual.tipoToken == TipoToken.doispontos) {
            proxToken();
            tipo_var();
            CheckVar();
            InserirTSVar();
            mais_par();
        } else {
            Error(TipoToken.doispontos);
        }
    }

    private void mais_par() {
        if (tokenAtual.tipoToken == TipoToken.PontoEVirgula) {
            proxToken();
            lista_par();
        } else {

        }
    }

    private void tipo_var() {
        if (tokenAtual.tipoToken == TipoToken.integer) {
            this.TipoVar = TipoToken.integer;
            proxToken();
        } else if (tokenAtual.tipoToken == TipoToken.real) {
            this.TipoVar = TipoToken.real;
            proxToken();
        } else {
            Error(TipoToken.numeroInt);
        }
    }

    private void ErrorId(String id) {
        System.out.println("Erro na Analise:");
        System.out.println("A variavel " + id + " foi declarado mais de uma vez.");
        System.out.println("O compilador não pode continuar");
        error = true;
        System.exit(0);
    }

    private boolean VerificarIdTabela(Token id) {
        int i = 0;
        int tam = tabela.size();
        if (tabela.size() == 0)
            return true;
        do {
            if (tabela.get(i).token.cod.equals(id.cod))
                return false;
            i++;
            tam--;
        } while (tam > 0);
        return true;
    }

    private void LimparIdentificadores() {
        int tamanho = Id.size();
        do {
            Id.remove(tamanho - 1);
            tamanho--;
        } while (tamanho > 0);
    }

    private void variaveis() {
        int tam;
        if (tokenAtual.tipoToken == TipoToken.Identificador) {
            if(this.ListVar.size()>0){
                tam =this.ListVar.size();
                for(int i=0; i<tam;i++){
                    if(tokenAtual.cod.equals(this.ListVar.get(i).cod)) {
                        Error("Erro no token '"+ tokenAtual.cod+"', ele foi declardo mais de uma vez.");
                    }
                }
            }
            this.ListVar.add(tokenAtual);
            proxToken();
            mais_var();
        } else {
            Error(TipoToken.Identificador);
        }
    }

    private void mais_var() {
        //L → id X
        if (tokenAtual.tipoToken == TipoToken.Virgula) {
            proxToken();
            variaveis();

        } else {

        }
    }

    private void mais_dc() {
        //<mais_dc> ::= ; <dc> | λ
            if (tokenAtual.tipoToken == TipoToken.PontoEVirgula) {
                proxToken();
                dc();
            } else {

        }
    }

    private boolean VerificarIdentificador() {
        int tam = tabela.size() - 1;
        do {
            if (tokenAtual.cod.equals(tabela.get(tam).token.cod))
                return true;
            tam--;
        } while (tam > -1);
        return false;
    }


    private void gerarR_sem_iden(String cod_atual) {
        String cod_int;
        cod_int = "[ := " + cod_atual + " " + R.get(0) + " ]";
        Cod_inte.add(cod_int);
    }

    private void gerarR_com_ident(String id) {
        String cod_int = "[ := " + id + " " + Temp.peek() + " ]";
        Cod_inte.add(cod_int);

    }

    private void limparTipoTokenListId() {
        int tam = tipotokenListId.size() - 1;
        do {
            tipotokenListId.remove(tam);
            tam--;
        } while (tam > -1);
    }

    private String voltarTipoError() {
        int tam = tipotokenListId.size() - 1;
        String first = tipotokenListId.get(0).tipoToken.toString();
        do {
            if (!first.equals(tipotokenListId.get(tam).tipoToken.toString())) {
                return tipotokenListId.get(tam).cod;
            }
            tam--;
        } while (tam > -1);
        return "";
    }

    private boolean VerificarosTipos() {
        int tam = tipotokenListId.size() - 1;
        String first = tipotokenListId.get(0).tipoToken.toString();
        do {
            if (!first.equals(tipotokenListId.get(tam).tipoToken.toString())) {
                return false;
            }
            tam--;
        } while (tam > -1);

        return true;
    }

    private TipoToken buscartipodevarTab(String id) {
        int tam = tabela.size() - 1;
        do {
            if (tabela.get(tam).token.cod.equals(id))
                return tabela.get(tam).tipoToken;
            tam--;
        } while (tam > -1);
        return null;
    }


    private void addtipotokenList() {
        Token newtoken;
        newtoken = new Token(tokenAtual.cod, buscartipodevarTab(tokenAtual.cod), this.linha);
        tipotokenListId.add(newtoken);
    }

    private void ErroTipo(String id, String atual, String esperado) {
        error = true;
        System.out.println("Erro de Tipo na variavel '" + id + "'. Ela é do tipo " + esperado + " e esperava um " + atual + ".");
        System.exit(1);
    }
}
