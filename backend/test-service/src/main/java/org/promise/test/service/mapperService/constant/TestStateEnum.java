package org.promise.test.service.mapperService.constant;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 15:48
 * @description:
 */
public enum TestStateEnum {
    running,finish,cancel;

    public static TestStateEnum parse(String state){
        if(running.toString ().equals (state)){
            return running;
        }
        if(finish.toString ().equals (state)){
            return finish;
        }
        if(cancel.toString ().equals (state)){
            return cancel;
        }
        return null;
    }
}
