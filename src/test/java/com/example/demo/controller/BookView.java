package com.example.demo.controller;

import com.fasterxml.jackson.annotation.JsonAlias;

// Clase para poder leer el id del libro

class BookView {
    public Long id;
    public String title;
    public String author;
    public Long publicationYear;
    public String genre;
    public String language;
    public int pages;

    // Mapea tanto "isAvailable" como "available", por si el JSON viene con cualquiera de los dos
    @JsonAlias({"isAvailable","available"})
    public boolean available;
}
