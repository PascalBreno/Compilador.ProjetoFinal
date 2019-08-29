package ic;

import java.util.ArrayList;
import java.util.List;

public class Procedure {
    public String nome;
    public List<String> listArg = new ArrayList<>();
    public List<TipoToken> listTipoToken = new ArrayList<>();
    public Procedure() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getListArg() {
        return listArg;
    }

    public void setListArg(String valor) {
        this.listArg.add(valor);
    }

    public List<TipoToken> getListTipoToken() {
        return listTipoToken;
    }

    public void setListTipoToken(List<TipoToken> listTipoToken) {
        this.listTipoToken = listTipoToken;
    }
}
