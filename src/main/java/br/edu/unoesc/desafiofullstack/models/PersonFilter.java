package br.edu.unoesc.desafiofullstack.models;

public class PersonFilter {

    private Long id;

    private String name;

    private String cpf;

    private String birthdate;

    private String gender;

    private String sortBy;

    private String orderBy;

    public boolean isEmpty() {
        return (id == null)
                && (name == null || name.isBlank())
                && (cpf == null || cpf.isBlank())
                && (birthdate == null || birthdate.isBlank())
                && (gender == null || gender.isBlank())
                && (sortBy == null || sortBy.isBlank())
                && (orderBy == null || orderBy.isBlank());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
