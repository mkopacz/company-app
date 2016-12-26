package pl.kopacz.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class SpiceDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String producer;

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

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SpiceDTO spiceDTO = (SpiceDTO) o;

        if (!Objects.equals(id, spiceDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SpiceDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", producer='" + producer + "'" +
            '}';
    }

}
