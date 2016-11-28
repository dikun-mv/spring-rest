package mv.dikun.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@JsonPropertyOrder({"id", "name"})
@Entity
public class Department implements Serializable{
    @JsonIgnore
    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "department", cascade = {CascadeType.REMOVE})
    private List<Employee> employees = new LinkedList<>();

    public Department() {
    }

    public Department(String name) {
        this.name = name;
    }

    @JsonGetter("id")
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
