package com.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ReaderInAction {
    public static void main(String[] args) {
        File file = new File("files/data.txt");
        System.out.println(file.getName());
        System.out.println(file.exists());
        file.mkdir();
        try {
            file.createNewFile();// throws exception if folder doesnt exist
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Reader reader = new FileReader(file);){
            int nextChar = reader.read();
            while(nextChar!=-1){
                System.out.println((char)nextChar);
                nextChar = reader.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Reader reader = new FileReader(file);
             BufferedReader bufferedReader = new
                     BufferedReader(reader)){
            String line = bufferedReader.readLine();
            while(line!=null){
                System.out.println(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //JAvA 7 way
        Path path = Paths.get("files/data.txt");
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)){
            String line = bufferedReader.readLine();
            while(line!=null){
                System.out.println(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Java 8 way
        Path path2 = Paths.get("files/data.txt");
        try (BufferedReader bufferedReader = Files.newBufferedReader(path2)){
            bufferedReader.lines().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Streams are autoclosable so..
        try (Stream<String> lines = Files.newBufferedReader(path2).lines()){
            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
