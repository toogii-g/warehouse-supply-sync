package shopping.whworker.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productNr;

    private String name;

    private String description;

    private BigDecimal price;


    public Product(int productNr, String name, String description, BigDecimal price) {
        this.productNr = productNr;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Product() {

    }

    public Product(int productNr, String name, BigDecimal price) {
        this.productNr = productNr;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productNumber=" + productNr +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}