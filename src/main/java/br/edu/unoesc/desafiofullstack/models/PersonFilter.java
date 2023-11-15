package br.edu.unoesc.desafiofullstack.models;

public class PersonFilter {

    private Long id;

    private String name;

    private String cpf;

    private String birthdate;

    private String gender;

    private String sortBy;

    private String orderBy;

    private int size;

    public boolean isEmpty() {
        return (id == null)
                && (name == null || name.isBlank())
                && (cpf == null || cpf.isBlank())
                && (birthdate == null || birthdate.isBlank())
                && (gender == null || gender.isBlank())
                && (sortBy == null || sortBy.isBlank())
                && (orderBy == null || orderBy.isBlank());
    }

    public String asQueryString() {
        final StringBuilder builder = new StringBuilder();
        if (id != null && id != 0) {
            builder.append("&id=");
            builder.append(id);
        }
        if (name != null && !name.isEmpty()) {
            builder.append("&name=");
            builder.append(name);
        }
        if (cpf != null && !cpf.isEmpty()) {
            builder.append("&cpf=");
            builder.append(cpf);
        }
        if (birthdate != null && !birthdate.isEmpty()) {
            builder.append("&birthdate=");
            builder.append(birthdate);
        }
        if (gender != null && !gender.isEmpty()) {
            builder.append("&gender=");
            builder.append(gender);
        }
        if (sortBy != null && !sortBy.isEmpty()) {
            builder.append("&sortBy=");
            builder.append(sortBy);
        }
        if (orderBy != null && !orderBy.isEmpty()) {
            builder.append("&orderBy=");
            builder.append(orderBy);
        }
        if (size != 0) {
            builder.append("&size=");
            builder.append(size);
        }
        return builder.toString();
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
