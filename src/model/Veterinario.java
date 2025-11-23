package model;

public class Veterinario {
    private int id;
    private String nome;
    private String crmv;
    private int idEspecialidade;
    private String especialidade;
    private int idTelefone;
    private String telefone;

    public Veterinario(int id, String nome, String crmv, int idEspecialidade, String especialidade, int idTelefone, String telefone) {
        this.id = id;
        this.nome = nome;
        this.crmv = crmv;
        this.idEspecialidade = idEspecialidade;
        this.especialidade = especialidade;
        this.idTelefone = idTelefone;
        this.telefone = telefone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        this.crmv = crmv;
    }

    public int getIdEspecialidade() {
        return idEspecialidade;
    }

    public void setIdEspecialidade(int idEspecialidade) {
        this.idEspecialidade = idEspecialidade;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public int getIdTelefone() {
        return idTelefone;
    }

    public void setIdTelefone(int idTelefone) {
        this.idTelefone = idTelefone;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "Veterinario{" + "id=" + id + ", nome='" + nome + '\'' + ", crmv='" + crmv + '\'' + ", idEspecialidade=" + idEspecialidade + ", especialidade='" + especialidade + '\'' + ", idTelefone=" + idTelefone + ", telefone='" + telefone + '\'' + '}';
    }
}
