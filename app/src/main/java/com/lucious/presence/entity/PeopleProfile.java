package com.lucious.presence.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ztx on 2017/2/8.
 */

public class PeopleProfile implements Parcelable {
    public static final String UID = "uid";
    public static final String AVATAR = "avatar";
    public static final String NAME = "name";
    public static final String GENDER = "gender";
    public static final String AGE = "age";
    public static final String CONSTELLATION = "constellation";
    public static final String TIME = "time";
    public static final String SIGNATURE = "signature";
    public static final String SIGN = "sign";
    public static final String SIGN_PIC = "sign_pic";
    public static final String PHOTOS = "photos";

    private String uid;
    private String avatar;
    private String name;
    private int gender;
    private int genderId;
    private int genderBgId;
    private int age;
    private String constellation;
    private String time;
    private boolean isHasSign;
    private String sign;
    private String signPicture;
    private List<String> photos;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public int getGenderBgId() {
        return genderBgId;
    }

    public void setGenderBgId(int genderBgId) {
        this.genderBgId = genderBgId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isHasSign() {
        return isHasSign;
    }

    public void setHasSign(boolean hasSign) {
        isHasSign = hasSign;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSignPicture() {
        return signPicture;
    }

    public void setSignPicture(String signPicture) {
        this.signPicture = signPicture;
    }

    public static Creator<PeopleProfile> getCreator() {
        return CREATOR;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public static final Creator<PeopleProfile> CREATOR = new Creator<PeopleProfile>() {
        @Override
        public PeopleProfile createFromParcel(Parcel source) {
            PeopleProfile profile = new PeopleProfile();
            profile.setUid(source.readString());
            profile.setAvatar(source.readString());
            profile.setName(source.readString());
            profile.setGender(source.readInt());
            profile.setGenderId(source.readInt());
            profile.setGenderBgId(source.readInt());
            profile.setAge(source.readInt());
            profile.setConstellation(source.readString());
            profile.setTime(source.readString());
            profile.setHasSign(source.readInt() == 1 ? true : false);
            profile.setSign(source.readString());
            profile.setSignPicture(source.readString());
            profile.setPhotos(source.readArrayList(PeopleProfile.class.getClassLoader()));
            return profile;
        }

        @Override
        public PeopleProfile[] newArray(int size) {
            return new PeopleProfile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(avatar);
        dest.writeString(name);
        dest.writeInt(gender);
        dest.writeInt(genderId);
        dest.writeInt(genderBgId);
        dest.writeInt(age);
        dest.writeString(constellation);
        dest.writeString(time);
        dest.writeInt(isHasSign ? 1 : 0);
        dest.writeString(sign);
        dest.writeString(signPicture);
        dest.writeList(photos);
    }
}
