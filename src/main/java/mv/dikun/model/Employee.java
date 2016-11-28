package mv.dikun.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import java.io.Serializable;

@JsonPropertyOrder({"id", "departmentId", "firstName", "secondName", "lastName"})
@Entity
public class Employee implements Serializable{
    @JsonIgnore
    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String secondName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Department department;

    @Transient
    private Long departmentId;

    public Employee() {

    }

    public Employee(String firstName, String secondName, String lastName, Department department) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.department = department;
        this.departmentId = department.getId();
    }

    @JsonGetter("id")
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
        this.departmentId = department.getId();
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    @PostLoad
    protected void postLoad() {
        departmentId = department.getId();
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", departmentId=" + departmentId +
                '}';
    }
}
