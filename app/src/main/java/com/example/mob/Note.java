package com.example.mob;
public class Note {
    private int id; // Уникальный идентификатор заметки
    private String title; // Заголовок заметки
    private String location; // Информация о местоположении

    // Конструктор без id, используется при создании новой заметки
    public Note(String title, String location) {
        this.title = title;
        this.location = location;
    }

    // Конструктор с id, используется при чтении заметки из базы данных
    public Note(int id, String title, String location) {
        this.id = id;
        this.title = title;
        this.location = location;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
