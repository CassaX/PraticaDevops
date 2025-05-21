package main.devops;

public class Pessoa {
    private int id;
    private String nome;
    private int idade;
    private String cpf;
    private String telefone;
    private String email;

    public Pessoa() {}

    public Pessoa(int id, String nome, int idade, String cpf, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
    }

    // Getters e setters
    public int getId(){
         return id;
        }

    public void setId(int id){ 
        this.id = id;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public int getIdade(){
        return idade;
    }

    public void setIdade(int idade){
        this.idade = idade;
    }

    public String getCpf(){
        return cpf;
    }

    public void setCpf(String cpf){
        this.cpf = cpf;
    }

    public String getTelefone(){
        return telefone;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }
}
