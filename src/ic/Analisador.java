package ic;

import java.util.ArrayList;
import java.util.List;
class Analisador extends Tokenizador {

    Analisador(String codigo) {
        super(codigo);
    }
    private int valorTokenAtual = -1;
    private Token tokenAtual;
    private Boolean error=false;
    private List<Token> tipotokenListId = new ArrayList<>();
    private List<Tabela> tabela = new ArrayList<>();
    private int ListID =0;
    private List<Token> Id = new ArrayList<>();
    void tabela() {
        System.out.println("Inicio da Analise Descendente:\n");
        Z();
        if(!error) {
            System.out.println("Passou pela analise!");
            imprimirTabela();
        }
        else
            System.out.println("Esta Errado");
    }
    private void imprimirTabela(){
        int tamanho = tabela.size();
        int i =0;
        System.out.println("Tabela de Simbolos: ");
        System.out.println("------------------------");
        System.out.println("Nome\t\tTipo\t\t\tTipo Token");
        do{
            System.out.println(tabela.get(i).token.cod + "\t\t|\t" + tabela.get(i).token.tipoToken
                    + "\t|\t" + tabela.get(i).tipoToken);
            i++;
            tamanho--;
        }while(tamanho>0);
        System.out.println("------------------------");
    }

    private void proxToken() {
        valorTokenAtual++;
        if(valorTokenAtual<token.size())
        tokenAtual = token.get(valorTokenAtual);
    }

    private void Z() {
        proxToken();
        I();
        S();
    }

    private void I() {
        if (tokenAtual.tipoToken == TipoToken.var) {
            proxToken();
            D();
        } else {
            Error(TipoToken.var);
        }
    }

    private void Error(TipoToken a) {
        error = true;
        System.out.println("Impossível continuar com a analise.");
        System.out.println("Error no token: " + tokenAtual.cod);
        System.out.println("Eu esperava um " + a);
        System.exit(1);
    }

    private void D() {
        // D → L : K O
        L();
        if(tokenAtual.tipoToken==TipoToken.doispontos){
            proxToken();
            K();
            O();
        }else{
            Error(TipoToken.doispontos);
        }
    }

    private void O(){
        if(tokenAtual.tipoToken==TipoToken.PontoEVirgula){
            proxToken();
            D();
        }
    }

    private void adicionarIdentificadores(TipoToken tipotoken){
        int i=0;
        do{
            Tabela newtabela;
            if(VerificarIdTabela(Id.get(i))) {
                newtabela = new Tabela(Id.get(i), tipotoken);
                i++;
                tabela.add(newtabela);
            }else{
                ErrorId(Id.get(i).cod);
            }
        ListID--;
        }while(ListID>0);
        LimparIdentificadores();
    }

    private void ErrorId(String id){
        System.out.println("Erro na Analise:");
        System.out.println("A variavel "+ id+ " foi declarado mais de uma vez.");
        System.out.println("O compilador não pode continuar");
        error = true;
        System.exit(0);
    }
    private boolean VerificarIdTabela(Token id){
        int i=0;
        int tam = tabela.size();
        if(tabela.size()==0)
            return true;
        do{
            if(tabela.get(i).token.cod.equals(id.cod))
                return false;
            i++;
            tam--;
        }while(tam>0);
        return true;
    }
    private void LimparIdentificadores(){
        int tamanho = Id.size();
        do{
            Id.remove(tamanho-1);
            tamanho--;
        }while(tamanho>0);
    }
    private void K(){
        /*
         K → integer
         K → real
         */
        if(tokenAtual.tipoToken==TipoToken.integer){
            adicionarIdentificadores(TipoToken.integer);
        }else if (tokenAtual.tipoToken==TipoToken.real){
            adicionarIdentificadores(TipoToken.real);
        }else{
            Error(TipoToken.TipodeVariavel);
        }
        proxToken();
    }
    private void L(){
        //L → id X
        if(tokenAtual.tipoToken == TipoToken.Identificador){
            Id.add(tokenAtual);
            ListID++;
            proxToken();
            X();
        }else{
            Error(TipoToken.Identificador);
        }
    }

    private void X(){
        /*X → , L
        X → ε
        */
        if(tokenAtual.tipoToken==TipoToken.Virgula){
            proxToken();
            L();
        }
    }
    private boolean VerificarIdentificador(){
        int tam = tabela.size()-1;
        do{
            if(tokenAtual.cod.equals(tabela.get(tam).token.cod))
                return true;
            tam--;
        }while(tam>-1);
        return false;
    }
    private void S() {
        if(tokenAtual.tipoToken==TipoToken.Identificador){
            if(VerificarIdentificador()) {
                addtipotokenList();
                proxToken();
                if (tokenAtual.tipoToken == TipoToken.OperadordeAtribuicao) {
                    proxToken();
                    E();
                } else {
                    Error(TipoToken.OperadordeAtribuicao);
                }
            }else{
                error=true;
                System.out.println("Identificador não declarado "+tokenAtual.cod);
            }

        }else if(tokenAtual.tipoToken==TipoToken.PalavraReservadaIF) {
            proxToken();
            E();
            if(tokenAtual.tipoToken==TipoToken.Then){
                proxToken();
                S();
            }else{
                Error(TipoToken.Then);
            }
        }else{
            Error(TipoToken.IdentificadorOuPalavraReservada);
        }

    }
    private void limparTipoTokenListId(){
        int tam = tipotokenListId.size()-1;
        do{
            tipotokenListId.remove(tam);
            tam--;
        }while(tam>-1);
    }
    private String voltarTipoError(){
        int tam = tipotokenListId.size()-1;
        String first = tipotokenListId.get(0).tipoToken.toString();
        do{
            if(!first.equals(tipotokenListId.get(tam).tipoToken.toString())) {
                return tipotokenListId.get(tam).cod;
            }
            tam--;
        }while(tam>-1);
        return "";
    }
    private boolean VerificarosTipos(){
        int tam = tipotokenListId.size()-1;
        String first = tipotokenListId.get(0).tipoToken.toString();
        do{
            if(!first.equals(tipotokenListId.get(tam).tipoToken.toString())) {
                return false;
            }
            tam--;
        }while(tam>-1);

        return true;
    }


    private void E(){
        T();
        R();
        if(!VerificarosTipos()) {
            String id = voltarTipoError();
            //Sabar onde ficou o Erro e identificar...
            Token first = tipotokenListId.get(0);
            if(first.tipoToken.toString().equals("Integer"))
                ErroTipo(id, "Real", "Integer");
            else
            ErroTipo(id, "Integer", "Real");
        }
        limparTipoTokenListId();
    }
    private TipoToken buscartipodevarTab(String id){
        int tam = tabela.size()-1;
        do{
            if(tabela.get(tam).token.cod.equals(id))
                return tabela.get(tam).tipoToken;
            tam--;
        }while(tam>-1);
        return null;
    }
    private void T(){
        if(tokenAtual.tipoToken==TipoToken.Identificador){
            //Fazer função que busca o tipo do identificador
            if(VerificarIdentificador()) {
                addtipotokenList();
                proxToken();
            }else{
                error=true;
                System.out.println("Variavel "+tokenAtual.cod+ " não foi declarada.");
                System.exit(1);
            }
        }else{
            Error(TipoToken.Identificador);
            System.exit(1);
        }
    }
    private void addtipotokenList(){
        Token newtoken;
        newtoken = new Token(tokenAtual.cod,buscartipodevarTab(tokenAtual.cod));
        tipotokenListId.add(newtoken);
    }
    private void R(){
        if(tokenAtual.tipoToken==TipoToken.OperadorAritmeticoMais){
            proxToken();
            T();
            R();
        }
    }
    private void ErroTipo(String id, String atual, String esperado){
        error = true;
        System.out.println("Erro de Tipo na variavel '"+id+"'. Ela é do tipo "+ atual+ " e esperava um "+esperado+".");
        System.exit(1);
    }
}
