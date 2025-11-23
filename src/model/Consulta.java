package model;

import java.time.LocalDateTime;

public class Consulta {

    private int id;
    private LocalDateTime horarioConsulta;

    private Animal animal;
    private Veterinario veterinario;

    private String diagnostico;
    private double valor;

    public Consulta(int id, LocalDateTime horarioConsulta, Animal animal, Veterinario veterinario, String diagnostico, double valor) {
        this.id = id;
        this.horarioConsulta = horarioConsulta;
        this.animal = animal;
        this.veterinario = veterinario;
        this.diagnostico = diagnostico;
        this.valor = valor;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getHorarioConsulta() {
        return horarioConsulta;
    }

    public void setHorarioConsulta(LocalDateTime horarioConsulta) {
        this.horarioConsulta = horarioConsulta;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Consulta{" + "id=" + id + ", horarioConsulta=" + horarioConsulta + ", animal=" + animal + ", veterinario=" + veterinario + ", diagnostico='" + diagnostico + '\'' + ", valor=" + valor + '}';
    }
}