package com.flat.bargain.service;

import com.flat.bargain.model.Flat;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FileSaver {

    public void saveToFile(List<Flat> flatList, String pathToFile) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(pathToFile));
        flatList.stream().map(this::line).forEach(printWriter::println);
        printWriter.close();
    }

    private String line(Flat flat) {
        return String.format("Cena całkowita: %s ,cena za m2: %s link do oferty: %s", flat.getTotalPrice(), flat.getSquareMeterPrice(), flat.getUrl());
    }
}
