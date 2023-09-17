package com.github.gunin_igor75.messenger.pojo;

import com.github.gunin_igor75.messenger.dto.UserDto;

public class User {

    private String id;
    private String name;
    private String lastname;

    private int age;
    private boolean online;

    public User(String id, String name, String lastname, int age, boolean online) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.age = age;
        this.online = online;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public boolean isOnline() {
        return online;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public static User userDtoToUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setLastname(userDto.getLastname());
        user.setAge(userDto.getAge());
        return user;
    }
}
