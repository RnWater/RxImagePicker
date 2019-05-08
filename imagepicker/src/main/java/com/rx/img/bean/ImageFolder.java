package com.rx.img.bean;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by henry on 2019/5/5.
 */

public class ImageFolder implements Parcelable {
    public int id;//文件夹id
    public String folderName;//文件夹名称
    public ArrayList<Image> images = new ArrayList<>();//包含的文件
    public boolean isChecked;//是否选中当前文件夹

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.folderName);
        dest.writeTypedList(this.images);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    }

    public ImageFolder() {
    }

    protected ImageFolder(Parcel in) {
        this.id = in.readInt();
        this.folderName = in.readString();
        this.images = in.createTypedArrayList(Image.CREATOR);
        this.isChecked = in.readByte() != 0;
    }

    public static final Creator<ImageFolder> CREATOR = new Creator<ImageFolder>() {
        @Override
        public ImageFolder createFromParcel(Parcel source) {
            return new ImageFolder(source);
        }

        @Override
        public ImageFolder[] newArray(int size) {
            return new ImageFolder[size];
        }
    };
}
