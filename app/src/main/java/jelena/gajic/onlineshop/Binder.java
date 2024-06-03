package jelena.gajic.onlineshop;

import android.os.RemoteException;

public class Binder extends  IBinderService.Stub{
    private boolean isSale;
    private String username;

    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public boolean getSale() {
        return this.isSale;
    }

    @Override
    public void setSale(boolean sale){
        this.isSale = sale;
    }
}

