package cn.edu.bupt.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by Administrator on 2017/10/23.
 */
public class Test {
    private Test(){
        System.out.print("haha");
    }

    static class A{
        private static Test t = new Test();
    }
    public static void main(String agrs[]){

       //         System.out.println("123");
          //    Test t = A.t;

//        ExecutorService service = Executors.newCachedThreadPool();
//        service.submit(new MyTask(new Callable() {
//            public Object call() throws Exception {
//                return "hahahah";
//            }
//        }));

    }
}

class MyTask extends FutureTask{
    public MyTask(Callable r){
        super(r);
    }
    @Override
    protected void done() {

    }

}
