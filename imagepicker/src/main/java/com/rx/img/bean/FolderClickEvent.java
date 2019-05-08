package com.rx.img.bean;

/**
 * Folder的点击事件
 * Created by henry on 2019/5/5.
 */

public class FolderClickEvent{
    public int position;
    public ImageFolder folder;

    public FolderClickEvent(int position, ImageFolder folder) {
        this.position = position;
        this.folder = folder;
    }
    public ImageFolder getFolder(){
        return folder;
    }
    public int getPosition(){
        return position;
    }
}
