package org.promise.common.result.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: 黄相淇
 * @date: 2022年03月07日 13:03
 * @description:
 */
@Data
@NoArgsConstructor
public class Page implements Serializable {

    private Integer total;  //数据总量

    private Integer pageNum; //第几页，从1开始

    private Integer pageSize;//每页的大小

    public Page(Integer pageNum,Integer pageSize){
        this.pageNum=pageNum;
        this.pageSize=pageSize;
    }


    public Page(Integer total){
        this.total=total;
    }

    public Integer getTotal () {
        return total;
    }

    public void setTotal ( Integer total ) {
        this.total = total;
    }

    public Integer getPageNum () {
        return pageNum;
    }

    public void setPageNum ( Integer pageNum ) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize () {
        return pageSize;
    }

    public void setPageSize ( Integer pageSize ) {
        this.pageSize = pageSize;
    }
}
