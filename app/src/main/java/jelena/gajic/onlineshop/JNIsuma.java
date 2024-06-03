package jelena.gajic.onlineshop;

public class JNIsuma {
    static{
        System.loadLibrary("MyLibrary");
    }public native int suma(int a,int b);
}
