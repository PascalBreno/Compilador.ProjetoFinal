package ic;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String nome = JOptionPane.showInputDialog("Digite o nome do arquivo.: ");
        System.out.println("Olá, seja bem vindo ao compilador do pascalzinho!");
        try {
            FileReader arq = new FileReader(nome);
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            String cod = linha;
            cod += '\n';
            while (linha != null) {
                linha = lerArq.readLine(); // lê da segunda até a última linha
                cod += linha;
                cod += '\n';
            }
            Analisador codigo = new Analisador(cod);
            codigo.CriarTokens();
            String escolha;
            if (codigo.Errotokenizador) {
                codigo.ImprimirTokens();
                System.out.println("Erro no tokenizador. Impossivel fazer a analise.");
            } else {
                System.out.println("Analise dos Tokens finalizados com sucesso. Deseja ver os tokens?");
                do {
                System.out.println("1 - Sim\t2- Não");
                Scanner scanner = new Scanner(System.in);
                escolha = scanner.nextLine();
            }while (!(escolha.equals("1") || escolha.equals("2")));
                    if (escolha.equals("1"))
                        codigo.ImprimirTokens();
                    codigo.tabela();
                }
                arq.close();
            } catch(IOException e){
                System.err.printf("Erro na abertura do arquivo: %s.\n",
                        e.getMessage());
            }
        }
    }
