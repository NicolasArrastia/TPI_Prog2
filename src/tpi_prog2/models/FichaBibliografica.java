package tpi_prog2.models;

import java.util.Objects;

public class FichaBibliografica extends Base {

    private String isbn;
    private String clasificacionDewey;
    private String estanteria;
    private String idioma;

    public FichaBibliografica(int id, boolean eliminado, String isbn,
                              String clasificacionDewey, String estanteria,
                              String idioma) {
        super(id, eliminado);
        this.isbn = isbn;
        this.clasificacionDewey = clasificacionDewey;
        this.estanteria = estanteria;
        this.idioma = idioma;
    }

    public FichaBibliografica() {
        super();
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getClasificacionDewey() {
        return clasificacionDewey;
    }

    public void setClasificacionDewey(String clasificacionDewey) {
        this.clasificacionDewey = clasificacionDewey;
    }

    public String getEstanteria() {
        return estanteria;
    }

    public void setEstanteria(String estanteria) {
        this.estanteria = estanteria;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    @Override
    public String toString() {
        return "FichaBibliografica{" +
                "id=" + getId() +
                ", isbn='" + isbn + '\'' +
                ", clasificacionDewey='" + clasificacionDewey + '\'' +
                ", estanteria='" + estanteria + '\'' +
                ", idioma='" + idioma + '\'' +
                ", eliminado=" + isEliminado() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FichaBibliografica)) return false;
        FichaBibliografica that = (FichaBibliografica) o;
        return Objects.equals(isbn, that.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}
