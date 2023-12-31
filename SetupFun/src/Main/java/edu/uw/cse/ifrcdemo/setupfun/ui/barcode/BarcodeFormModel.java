package edu.uw.cse.ifrcdemo.setupfun.ui.barcode;

import edu.uw.cse.ifrcdemo.sharedlib.generator.BarcodeVoucher;

import javax.validation.constraints.NotNull;
import java.util.List;

public class BarcodeFormModel {
    private Integer rangeStart;
    @NotNull
    private Integer rangeEnd;
    private String text;
    private List<BarcodeVoucher> voucherList;


    public String getText() {
        return text;
    }
    public void setText(){
        this.text = text;
    }

    public Integer getRangeStart() {
        return rangeStart;
    }
    public void setRangeStart(Integer rangeStart){
        this.rangeStart = rangeStart;
    }

    public Integer getRangeEnd() {
        return rangeEnd;
    }

    public void setEndIndex(Integer rangeEnd){
        this.rangeEnd = rangeEnd;
    }

    public List<BarcodeVoucher> getVoucherList() {
        return voucherList;
    }

    public void setVoucherList(List<BarcodeVoucher> voucherList) {
        this.voucherList = voucherList;
    }

    @Override
    public String toString() {
        return "BarcodeModel{" +
                "rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                ", text='" + text + '\'' +
                ", voucherList=" + voucherList +
                '}';
    }
}
