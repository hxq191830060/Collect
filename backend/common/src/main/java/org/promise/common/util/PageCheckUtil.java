package org.promise.common.util;

import org.promise.common.result.page.Page;


public class PageCheckUtil {

    public static boolean pageParamIsLegal( Page page ){
        if(page==null||page.getPageNum ()==null||page.getPageSize ()==null){
            return false;
        }
        return page.getPageNum ( ) > 0 && page.getPageSize ( ) > 0;
    }
}
