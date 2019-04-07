package ic;

import java.util.ArrayList;
import java.util.List;

public class Analisador extends Tokenizador {

    public Analisador(String codigo) {
        super(codigo);
    }
    String tokenAnterior = " ";
    int valorTokenAtual = -1;
    Token tokenAtual;
    Boolean error=false;
    public List<Token> tipotokenListId = new ArrayList<Token>();
    public List<Tabela> tabela = new ArrayList<Tabela>();
    int ListID =0;
    public List<String> Id = new ArrayList<String>();
    public void tabela() {
        Z();
        if(!error) {
            System.out.println("Ta tudo OK!");
            imprimirTabela();
        }
        else
            System.out.println("Esta Errado");
        /*
        Z → I S
    I → var D
     D → L : K O
    L → id X
    X → , L
    X → ε
    K → integer
    K → real
    O → ; D
    O →ε
    S → id := E
    S → if E then S
    E → T R
    R → + T R
    R → ε
    T → id
         */
    }
    private void imprimirTabela(){
        int tamanho = tabela.size();
        int i =0;
        do{
            System.out.println(tabela.get(i).cod + " " + tabela.get(i).tipoToken);
            i++;
            tamanho--;
        }while(tamanho>0);
    }

    public void proxToken() {
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
        if (tokenAtual.tipoToken == TipoToken.Var) {
            proxToken();
            D();
        } else {
            Error(TipoToken.Var);
        }
    }

    private void Error(TipoToken a) {
        error = true;
        System.out.println("Error no token: " + tokenAtual.cod);
        System.out.println("Eu esperava um " + a);
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
        /*
        O → ; D
        O →ε
         */
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
                ErrorId(Id.get(i));
            }
        ListID--;
        }while(ListID>0);
        limparidentificadores();
    }

    private void ErrorId(String id){
        System.out.println("A variavel "+ id+ " foi declarado mais de uma vez.");
        error = true;
    }
    private boolean VerificarIdTabela(String id){
        int i=0;
        int tam = tabela.size();
        if(tabela.size()==0)
            return true;
        do{
            if(tabela.get(i).cod.equals(id))
                return false;
            i++;
            tam--;
        }while(tam>0);
        return true;
    }
    private void limparidentificadores(){
        int tamanho = Id.size();
        int i=0;
        do{
            Id.remove(tamanho-1);
            i++;
            tamanho--;
        }while(tamanho>0);
    }
    private void K(){
        /*
         K → integer
         K → real
         */
        if(tokenAtual.tipoToken==TipoToken.Integer){
            adicionarIdentificadores(TipoToken.Integer);
        }else if (tokenAtual.tipoToken==TipoToken.Real){
            adicionarIdentificadores(TipoToken.Real);
        }else{
            Error(TipoToken.TipodeVariavel);
        }
        proxToken();
    }
    private void L(){
        //L → id X
        if(tokenAtual.tipoToken == TipoToken.Identificadores){
            String cod = tokenAtual.cod;
            Id.add(cod);
            ListID++;
            proxToken();
            X();
        }else{
            Error(TipoToken.Identificadores);
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
            if(tokenAtual.cod.equals(tabela.get(tam).cod))
                return true;
            tam--;
        }while(tam>-1);
        return false;
    }
    private void S() {
        if(tokenAtual.tipoToken==TipoToken.Identificadores){
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
        boolean retorno = true;
        do{
            if(!first.equals(tipotokenListId.get(tam).tipoToken.toString())) {
                return false;
            }
            tam--;
        }while(tam>-1);
        return retorno;
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
            if(tabela.get(tam).cod.equals(id))
                return tabela.get(tam).tipoToken;
            tam--;
        }while(tam>-1);
        return null;
    }
    private void T(){
        if(tokenAtual.tipoToken==TipoToken.Identificadores){
            //Fazer função que busca o tipo do identificador
            if(VerificarIdentificador()) {
                addtipotokenList();
                proxToken();
            }else{
                error=true;
                System.out.println("Variavel "+tokenAtual.cod+ " não foi declarada.");
            }
        }else{
            Error(TipoToken.Identificadores);
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
        }else{

        }
    }
    private void ErroTipo(String id, String atual, String esperado){
        error = true;
        System.out.println("Erro de Tipo na variavel '"+id+"'. Ela é do tipo "+ atual+ " e esperava um "+esperado+".");
    }
}
