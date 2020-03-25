package com.travel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CommentDto {

    private UserDto userDto;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm" ,timezone = "Asia/Ho_Chi_Minh")
    private Date time;

    public CommentDto(UserDto userDto,String content, Date time) {
        this.userDto = userDto;
        this.content = content;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}
