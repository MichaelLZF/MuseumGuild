package com.museumguild.entity.collection;

/**
 * Created by Administrator on 2016/8/13.
 */
public class PageParam {
    private int gotopage;
    private int nowpage;
    private int pagesize;
    private String pagetype;
    private int total;
    private int totalpage;

    public int getGotopage() {
        return gotopage;
    }

    public void setGotopage(int gotopage) {
        this.gotopage = gotopage;
    }

    public int getNowpage() {
        return nowpage;
    }

    public void setNowpage(int nowpage) {
        this.nowpage = nowpage;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public String getPagetype() {
        return pagetype;
    }

    public void setPagetype(String pagetype) {
        this.pagetype = pagetype;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    @Override
    public String toString() {
        return "PageParam{" +
                "gotopage=" + gotopage +
                ", nowpage=" + nowpage +
                ", pagesize=" + pagesize +
                ", pagetype='" + pagetype + '\'' +
                ", total=" + total +
                ", totalpage=" + totalpage +
                '}';
    }
}
