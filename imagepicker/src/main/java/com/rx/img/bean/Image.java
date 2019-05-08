package com.rx.img.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * 图片类
 * Created by henry on 2019/5/5.
 */

public class Image implements Parcelable,Comparable<Image>{
    /**
     * 图片 id.
     */
    public int id;
    /**
     * 图片地址.
     */
    public String path;
    /**
     * 图片名字.
     */
    public String name;
    /**
     * 图片插入时间.
     */
    public long addTime;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.path);
        dest.writeString(this.name);
        dest.writeLong(this.addTime);
    }

    public Image() {
    }

    protected Image(Parcel in) {
        this.id = in.readInt();
        this.path = in.readString();
        this.name = in.readString();
        this.addTime = in.readLong();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public int compareTo(@NonNull Image o) {
        long time = o.addTime - addTime;
        if (time > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else if (time < -Integer.MAX_VALUE) return -Integer.MAX_VALUE;
        return (int) time;
    }
}
