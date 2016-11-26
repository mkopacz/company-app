package pl.kopacz.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Ingredient.
 */
@Entity
@Table(name = "ingredient")
public class Ingredient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @OneToOne
    @NotNull
    @JoinColumn(unique = true)
    private Spice spice;

    @ManyToMany(mappedBy = "ingredients")
    @JsonIgnore
    private Set<Product> products = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public Ingredient amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Spice getSpice() {
        return spice;
    }

    public Ingredient spice(Spice spice) {
        this.spice = spice;
        return this;
    }

    public void setSpice(Spice spice) {
        this.spice = spice;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Ingredient products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Ingredient addProduct(Product product) {
        products.add(product);
        product.getIngredients().add(this);
        return this;
    }

    public Ingredient removeProduct(Product product) {
        products.remove(product);
        product.getIngredients().remove(this);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ingredient ingredient = (Ingredient) o;
        if(ingredient.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ingredient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            '}';
    }
}
