package com.example.camera;

public class Stock {

    String name;
    String lotno;
    String pieces;
    String sizelen;
    String sizebred;
    String remark;
    String sqft;
    String thickness;

    public Stock(String name, String lotno, String thickness, String pieces, String sizelen, String sizebred, String remark, String sqft) {

        this.name = name;
        this.lotno = lotno;
        this.pieces = pieces;
        this.sizelen = sizelen;
        this.sizebred = sizebred;
        this.remark = remark;
        this.sqft = sqft;
        this.thickness = thickness;
    }

    public String getName() {
        return name;
    }

    public String getLotno() {
        return lotno;
    }

    public String getThickness() {
        return pieces;
    }

    public String getPieces() {
        return pieces;
    }

    public String getSizelen() {
        return sizelen;
    }

    public String getSizebred() {
        return sizebred;
    }

    public String getRemark() {
        return remark;
    }

    public String getSqft() {
        return sqft;
    }
}
