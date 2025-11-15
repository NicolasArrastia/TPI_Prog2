package tpi_prog2.models;

import java.util.Objects;
public class Libro extends Base {

    private String titulo;
    private String autor;
    private String editorial;
    private int anioEdicion;

    private FichaBibliografica fichaBibliografica;

    public Libro(int id, boolean eliminado, String titulo, String autor,
                 String editorial, int anioEdicion,
                 FichaBibliografica fichaBibliografica) {
        super(id, eliminado);
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anioEdicion = anioEdicion;
        this.fichaBibliografica = fichaBibliografica;
    }

    public Libro() {
        super();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getAnioEdicion() {
        return anioEdicion;
    }

    public void setAnioEdicion(int anioEdicion) {
        this.anioEdicion = anioEdicion;
    }

    public FichaBibliografica getFichaBibliografica() {
        return fichaBibliografica;
    }

    public void setFichaBibliografica(FichaBibliografica fichaBibliografica) {
        this.fichaBibliografica = fichaBibliografica;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + getId() +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", editorial='" + editorial + '\'' +
                ", anioEdicion=" + anioEdicion +
                ", fichaBibliografica=" + fichaBibliografica +
                ", eliminado=" + isEliminado() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Libro)) return false;
        Libro libro = (Libro) o;
        return Objects.equals(titulo, libro.titulo) &&
               Objects.equals(autor, libro.autor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, autor);
    }
}