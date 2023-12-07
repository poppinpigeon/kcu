package com.cs407.badgerroomie;

public class ReadWriteUserDetails {
    public String name, gender, ethnicity, roommate, email;

    public ReadWriteUserDetails(){

    }

    public ReadWriteUserDetails(String textName, String textGender, String textEthnicity, String textRoommate, String textEmail){
        this.name = textName;
        this.gender = textGender;
        this.ethnicity = textEthnicity;
        this.roommate = textRoommate;
        this.email = textEmail;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public String getRoommate() {
        return roommate;
    }
}
